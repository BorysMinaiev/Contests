import java.io.*;
import java.util.*;

public class D {
	FastScanner in;
	PrintWriter out;

	void solve() {
		int n = in.nextInt();
		int m = in.nextInt();
		boolean[][] g = new boolean[n][n];
		for (int i = 0; i < m; i++) {
			int fr = in.nextInt();
			int to = in.nextInt();
			g[fr][to] = g[to][fr] = true;
		}
		double[] sz = new double[n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (g[i][j]) {
					sz[i]++;
				}
			}
		}
		double[] res = new double[n];
		for (int it = 0; it < 100000; it++) {
			for (int i = 0; i < n - 1; i++) {
				res[i] = 0;
				for (int j = 0; j < n; j++) {
					if (g[i][j]) {
						res[i] += res[j];
					}
				}
				res[i] /= sz[i];
				res[i] += 1;
			}
		}
		out.println(res[0]);
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
		new D().runIO();
	}
}