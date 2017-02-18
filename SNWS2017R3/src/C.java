import java.io.*;
import java.util.*;

public class C {
	FastScanner in;
	PrintWriter out;

	int[][] g;
	int[] used;

	int[] q;
	int[] dist;
	
	int[] bfs(int from) {
		Arrays.fill(dist, Integer.MAX_VALUE);
		dist[from] = 0;
		q[0] = from;
		int qIt = 0, qSz = 1;
		while (qIt < qSz) {
			int v = q[qIt++];
			for (int to : g[v]) {
				if (dist[to] == Integer.MAX_VALUE) {
					dist[to] = dist[v] + 1;
					q[qSz++] = to;
				}
			}
		}
		return dist;
	}

	void solve() {
		int n = in.nextInt();
		g = new int[n][];
		used = new int[n];
		for (int i = 0; i < n; i++) {
			used[i] = in.nextInt();
		}
		int[] from = new int[n - 1];
		int[] to1 =new int[n - 1];
		int[] sz = new int[n];
		
		for (int i = 0; i + 1 < n; i++) {
			int fr = in.nextInt() - 1;
			int to = in.nextInt() - 1;
			from[i] =fr;
			to1[i] = to;
			sz[fr]++;
			sz[to]++;
		}
		for (int i = 0; i < n; i++) {
			g[i] = new int[sz[i]];
		}
		for (int i = 0; i + 1 < n; i++) {
			g[from[i]][--sz[from[i]]] =  to1[i];
			g[to1[i]][--sz[to1[i]]] = from[i];
		}
		dist = new int[n];
		q = new int[n];
		int id = 0;
		while (id != n && used[id] == 0) {
			id++;
		}
		if (id == n) {
			out.println(0);
			return;
		}
		int[] d1 = bfs(id);
		int id2 = id;
		for (int i = 0; i < n; i++) {
			if (used[i] == 1 && d1[i] > d1[id2]) {
				id2 = i;
			}
		}
		int[] d2 = bfs(id2);
		int id3 = id2;
		for (int i = 0; i < n; i++) {
			if (used[i] == 1 && d2[i] > d2[id3]) {
				id3 = i;
			}
		}
		out.println((1 + d2[id3]) / 2);
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
		new C().runIO();
	}
}