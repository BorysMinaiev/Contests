import java.util.*;
import java.io.*;

public class B {

	class Edge {
		int from, to, f1, f2, cap;
		Edge rev;

		public Edge(int from, int to, int cap) {
			this.from = from;
			this.to = to;
			this.cap = cap;
		}

		@Override
		public String toString() {
			return "Edge [from=" + from + ", to=" + to + ", f1=" + f1 + ", f2="
					+ f2 + ", cap=" + cap + "]";
		}

	}

	List<Edge>[] graph;
	int s1, s2, t1, t2;

	List<Edge> back, forw;
	List<Edge> stack;
	boolean[] vis;

	int[] q;
	int[] h;
	int[] pos;

	boolean bfs(int s, int t) {
		int qIt = 0, qSz = 0;
		q[qSz++] = s;
		Arrays.fill(h, -1);
		h[s] = 0;
		while (qIt < qSz) {
			int v = q[qIt++];
			for (Edge e : graph[v]) {
				if (e.f1 == e.cap)
					continue;
				if (h[e.to] == -1) {
					h[e.to] = h[e.from] + 1;
					q[qSz++] = e.to;
				}
			}
		}
		return h[t] != -1;
	}

	int findPath1(int cur, int end, int flow) {
		if (cur == end) {
			return flow;
		}
		for (; pos[cur] < graph[cur].size(); pos[cur]++) {
			Edge e = graph[cur].get(pos[cur]);
			if (h[e.to] != h[e.from] + 1) {
				continue;
			}
			if (e.f1 + e.f2 < e.cap) {
				int endFlow = findPath1(e.to, end,
						Math.min(flow, e.cap - e.f1 - e.f2));
				if (endFlow > 0) {
					e.f1 += endFlow;
					e.rev.f1 -= endFlow;
					return endFlow;
				}
			}
		}
		return 0;
	}

	boolean bfs2(int s, int t) {
		int qIt = 0, qSz = 0;
		q[qSz++] = s;
		Arrays.fill(h, -1);
		h[s] = 0;
		while (qIt < qSz) {
			int v = q[qIt++];
			for (Edge e : graph[v]) {
				if (Math.abs(e.f1) + e.f2 >= e.cap)
					continue;
				if (h[e.to] == -1) {
					h[e.to] = h[e.from] + 1;
					q[qSz++] = e.to;
				}
			}
		}
		return h[t] != -1;
	}
	
	int findPath2(int cur, int end, int flow) {
		if (cur == end) {
			return flow;
		}
		for (;pos[cur]< graph[cur].size(); pos[cur]++) {
			Edge e = graph[cur].get(pos[cur]);
			if (h[e.to] != h[e.from] + 1) {
				continue;
			}
			if (Math.abs(e.f1) + e.f2 < e.cap) {
				int endFlow = findPath2(e.to, end,
						Math.min(flow, e.cap - Math.abs(e.f1) - e.f2));
				if (endFlow > 0) {
					e.f2 += endFlow;
					e.rev.f2 -= endFlow;
					return endFlow;
				}
			}
		}
		return 0;
	}

	boolean findForwardPath(int cur, int end) {
		if (vis[cur]) {
			return false;
		}
		vis[cur] = true;
		if (cur == end) {
			forw = new ArrayList<>(stack);
			return true;
		}
		for (Edge e : graph[cur]) {
			stack.add(e);
			if (Math.abs(e.f1) + e.f2 < e.cap || e.f1 > 0 || e.f2 < 0) {
				if (findForwardPath(e.to, end)) {
					return true;
				}
			}
			stack.remove(stack.size() - 1);
		}
		return false;
	}

	boolean findBackwardPath(int cur, int end) {
		if (vis[cur]) {
			return false;
		}
		vis[cur] = true;
		if (cur == end) {
			back = new ArrayList<>(stack);
			return true;
		}
		for (Edge e : graph[cur]) {
			stack.add(e);
			if (Math.abs(e.f1) + e.f2 < e.cap || e.f1 < 0 || e.f2 < 0) {
				if (findBackwardPath(e.to, end)) {
					return true;
				}
			}
			stack.remove(stack.size() - 1);
		}
		return false;
	}

	void solve() {
		int T = in.nextInt();
		while (T-- > 0) {
			int n = in.nextInt(), m = in.nextInt();
			h = new int[n];
			q = new int[n];
			pos = new int[n];
			graph = new List[n];
			for (int i = 0; i < n; i++) {
				graph[i] = new ArrayList<>();
			}
			for (int i = 0; i < m; i++) {
				int from = in.nextInt() - 1, to = in.nextInt() - 1;
				int cap = in.nextInt() * 2;
				Edge st = new Edge(from, to, cap), rev = new Edge(to, from, cap);
				st.rev = rev;
				rev.rev = st;
				graph[from].add(st);
				graph[to].add(rev);
			}
			s1 = in.nextInt() - 1;
			t1 = in.nextInt() - 1;
			s2 = in.nextInt() - 1;
			t2 = in.nextInt() - 1;

			int result = 0;
			int tmp;
			vis = new boolean[n];
			while (bfs(s1, t1)) {
				Arrays.fill(pos, 0);
				while ((tmp = findPath1(s1, t1, Integer.MAX_VALUE)) > 0) {
					result += tmp;
				}
			}

			while (true) {
				while (bfs2(s2, t2)) {
					Arrays.fill(pos, 0);
					while ((tmp = findPath2(s2, t2, Integer.MAX_VALUE)) > 0) {
						result += tmp;
					}	
				}
				
				Arrays.fill(vis, false);

				back = null;
				forw = null;
				stack = new ArrayList<>();
				findForwardPath(s2, t2);
				Arrays.fill(vis, false);
				stack.clear();
				findBackwardPath(s2, t2);
				Arrays.fill(vis, false);

				if (back == null || forw == null) {
					break;
				}

				int delta = Integer.MAX_VALUE;
				for (Edge e : forw) {
					if (e.f1 > 0) {
						delta = Math.min(delta, e.f1);
					}
					delta = Math.min(delta, (e.cap + e.f1 - e.f2) / 2);
				}
				for (Edge e : back) {
					if (e.rev.f1 > 0) {
						delta = Math.min(delta, e.rev.f1);
					}
					delta = Math.min(delta, (e.cap - e.f1 - e.f2) / 2);
				}

				for (Edge e : forw) {
					e.f1 -= delta;
					e.rev.f1 += delta;
					e.f2 += delta;
					e.rev.f2 -= delta;
				}
				for (Edge e : back) {
					e.f1 += delta;
					e.rev.f1 -= delta;
					e.f2 += delta;
					e.rev.f2 -= delta;
				}
				result += delta * 2;
				if (delta == 0) {
					break;
				}
			}
			out.println(result * 0.5);
		}
	}

	FastScanner in;
	PrintWriter out;

	void run() {
		in = new FastScanner();
		out = new PrintWriter(System.out);
		solve();
		out.close();
	}

	class FastScanner {
		BufferedReader br;
		StringTokenizer st;

		public FastScanner() {
			br = new BufferedReader(new InputStreamReader(System.in));
		}

		public FastScanner(String s) {
			try {
				br = new BufferedReader(new FileReader(s));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public String nextToken() {
			while (st == null || !st.hasMoreTokens()) {
				try {
					st = new StringTokenizer(br.readLine());
				} catch (IOException e) {
				}
			}
			return st.nextToken();
		}

		public int nextInt() {
			return Integer.parseInt(nextToken());
		}

		public long nextLong() {
			return Long.parseLong(nextToken());
		}

		public double nextDouble() {
			return Double.parseDouble(nextToken());
		}
	}

	public static void main(String[] args) {
		new B().run();
	}
}
