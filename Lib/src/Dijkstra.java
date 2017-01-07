import java.io.*;
import java.util.*;

public class Dijkstra {
	FastScanner in;
	PrintWriter out;

	class Edge {
		int to, cost;

		public Edge(int to, int cost) {
			super();
			this.to = to;
			this.cost = cost;
		}

	}

	int[] getDist(int from, ArrayList<Edge>[] g) {
		int n = g.length;
		int[] dist = new int[n];
		Arrays.fill(dist, Integer.MAX_VALUE);
		dist[from] = 0;
		PriorityQueue<Edge> pq = new PriorityQueue<>();
		pq.add(new Edge(from, 0));
		boolean[] was = new boolean[n];
		while (pq.size() > 0) {
			Edge cur = pq.poll();
			if (was[cur.to]) {
				continue;
			}
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

	void solve() {

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
		new Dijkstra().runIO();
	}
}