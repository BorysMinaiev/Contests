import java.io.*;
import java.util.*;

public class F {
	FastScanner in;
	PrintWriter out;

	class Edge implements Comparable<Edge> {
		int to;
		long cost;

		public Edge(int to, long cost) {
			super();
			this.to = to;
			this.cost = cost;
		}

		@Override
		public int compareTo(Edge o) {
			return Long.compare(cost, o.cost);
		}

	}

	int[] queue = new int[1 << 20];
	int qSz = 0;

	long[] getDist(int from, ArrayList<Edge>[] g) {
		int n = g.length;
		long[] dist = new long[n];
		Arrays.fill(dist, Long.MAX_VALUE);
		dist[from] = 0;
		PriorityQueue<Edge> pq = new PriorityQueue<>();
		pq.add(new Edge(from, 0));
		boolean[] was = new boolean[n];
		while (pq.size() > 0) {
			Edge cur = pq.poll();
			if (was[cur.to]) {
				continue;
			}
			queue[qSz++] = cur.to;
			was[cur.to] = true;
			for (Edge e : g[cur.to]) {
				if (dist[e.to] > dist[cur.to] + e.cost) {
					dist[e.to] = dist[cur.to] + e.cost;
					pq.add(new Edge(e.to, dist[e.to]));
				}
			}
		}
		return dist;
	}

	int[][] g2;

	int ans = 0;
	int n;

	void go() {
		final int M = 32;
		long[][] mask = new long[n][M];
		int[] prev = new int[n];
		Arrays.fill(prev, -1);
		for (int from = (n / 64 / M) * 64 * M; from >= 0; from -= 64 * M) {
			for (int it = from; it < qSz; it++) {
				int v = queue[it];
				if (it != 0) {
					for (int z = 0; z < M; z++) {
						mask[v][z] = mask[g2[v][0]][z];
					}
				}
				for (int to : g2[v]) {
					for (int z = 0; z < M; z++) {
						mask[v][z] &= mask[to][z];
					}
				}
				if (it >= from && it < from + 64 * M) {
					mask[v][(it - from) >> 6] |= 1L << ((it - from) & 63);
				}
				if (prev[v] == -1) {
					for (int k = M - 1; k >= 0; k--) {
						if (prev[v] == -1 && mask[v][k] != 0) {
							for (int z = 63; z >= 0; z--) {
								if (((1L << z) & mask[v][k]) != 0) {
									if (queue[from + z + 64 * k] != v) {
										prev[v] = queue[from + z + 64 * k];
										break;
									}
								}
							}
						}
					}
				}
			}
		}
		int[] cnt = new int[n];
		Arrays.fill(cnt, 1);
		for (int it = qSz - 1; it > 0; it--) {
			int v = queue[it];
			ans = Math.max(ans, cnt[v]);
			cnt[prev[v]] += cnt[v];
		}

	}

	void solve() {
		n = in.nextInt();
		int m = in.nextInt();
		int s = in.nextInt() - 1;
		ArrayList<Edge>[] g = new ArrayList[n];
		for (int i = 0; i < n; i++) {
			g[i] = new ArrayList<>();
		}
		for (int i = 0; i < m; i++) {
			int fr = in.nextInt() - 1;
			int to = in.nextInt() - 1;
			int cost = in.nextInt();
			g[fr].add(new Edge(to, cost));
			g[to].add(new Edge(fr, cost));
		}
		long[] dist = getDist(s, g);
		g2 = new int[n][];
		int[] g2Sz = new int[n];
		for (int i = 0; i < n; i++) {
			for (Edge e : g[i]) {
				if (dist[e.to] == dist[i] + e.cost) {
					g2Sz[e.to]++;
				}
			}
		}
		for (int i = 0; i < n; i++) {
			g2[i] = new int[g2Sz[i]];
		}
		for (int i = 0; i < n; i++) {
			for (Edge e : g[i]) {
				if (dist[e.to] == dist[i] + e.cost) {
					g2[e.to][--g2Sz[e.to]] = i;
				}
			}
		}
		go();
		out.println(ans);
	}

	void run() {
		try {
			in = new FastScanner(new File("object.in"));
			out = new PrintWriter(new File("object.out"));

			solve();

			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	void runIO() {

		in = new FastScanner(System.in);
		out = new PrintWriter(System.out);

		solve();

		out.close();
	}

	class FastScanner {
		BufferedReader br;
		StringTokenizer st;

		public FastScanner(File f) {
			try {
				br = new BufferedReader(new FileReader(f));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		public FastScanner(InputStream f) {
			br = new BufferedReader(new InputStreamReader(f));
		}

		String next() {
			while (st == null || !st.hasMoreTokens()) {
				String s = null;
				try {
					s = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (s == null)
					return null;
				st = new StringTokenizer(s);
			}
			return st.nextToken();
		}

		boolean hasMoreTokens() {
			while (st == null || !st.hasMoreTokens()) {
				String s = null;
				try {
					s = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (s == null)
					return false;
				st = new StringTokenizer(s);
			}
			return true;
		}

		int nextInt() {
			return Integer.parseInt(next());
		}

		long nextLong() {
			return Long.parseLong(next());
		}

		double nextDouble() {
			return Double.parseDouble(next());
		}
	}

	public static void main(String[] args) {
		new F().runIO();
	}
}