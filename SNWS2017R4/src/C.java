import java.io.*;
import java.util.*;

public class C {
	FastScanner in;
	PrintWriter out;

	boolean[] used;
	ArrayList<Integer> order = new ArrayList<Integer>();
	ArrayList<Integer>[] g;
	ArrayList<Integer>[] gt;

	void dfs1(int v) {
		used[v] = true;
		for (int i = 0; i < g[v].size(); ++i) {
			int to = g[v].get(i);
			if (!used[to])
				dfs1(to);
		}
		order.add(v);
	}

	int[] comp;

	void dfs2(int v, int cl) {
		comp[v] = cl;
		for (int i = 0; i < gt[v].size(); ++i) {
			int to = gt[v].get(i);
			if (comp[to] == -1)
				dfs2(to, cl);
		}
	}

	void solve() {
		int size = in.nextInt();
		int k = in.nextInt();
		int n = in.nextInt();
		g = new ArrayList[2 * n];
		gt = new ArrayList[2 * n];
		for (int i = 0; i < 2 * n; i++) {
			g[i] = new ArrayList<Integer>();
			gt[i] = new ArrayList<Integer>();
		}
		int[] x = new int[n];
		int[] y = new int[n];
		for (int i = 0; i < n; i++) {
			x[i] = in.nextInt();
			y[i] = in.nextInt();
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == j) {
					continue;
				}
				if (x[i] == x[j] && Math.abs(y[i] - y[j]) <= 2 * k) {
					g[i].add(n + j);
					gt[n + j].add(i);
				}
				if (y[i] == y[j] && Math.abs(x[i] - x[j]) <= 2 * k) {
					g[i + n].add(j);
					gt[j].add(i + n);
				}
			}
		}
		used = new boolean[n * 2];
		comp = new int[n * 2];
		Arrays.fill(comp, -1);
		for (int i = 0; i < n * 2; ++i)
			if (!used[i])
				dfs1(i);
		for (int i = 0, j = 0; i < n * 2; ++i) {
			int v = order.get(2 * n - i - 1);
			if (comp[v] == -1)
				dfs2(v, j++);
		}
		for (int i = 0; i < n; ++i)
			if (comp[i] == comp[i + n]) {
				out.println("NO");
				return;
			}
		out.println("YES");
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