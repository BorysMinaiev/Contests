import java.io.*;
import java.util.*;

public class C {
	FastScanner in;
	PrintWriter out;

	void solveOneTest() {
		int n = in.nextInt();
		int m = in.nextInt();
		int k = in.nextInt();
		long[][] d = new long[n][n];
		for (int i = 0; i < n; i++) {
			Arrays.fill(d[i], Long.MAX_VALUE / 2);
			d[i][i] = 0;
		}
		for (int i = 0; i < m; i++) {
			int fr = in.nextInt() - 1;
			int to = in.nextInt() - 1;
			int cost = in.nextInt();
			d[to][fr] = Math.min(d[to][fr], cost);
			d[fr][to] = Math.min(d[fr][to], cost);
		}
		int[] from = new int[k];
		int[] to = new int[k];
		for (int i = 0; i < k; i++) {
			from[i] = in.nextInt() - 1;
			to[i] = in.nextInt() - 1;
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				for (int kk = 0; kk < n; kk++) {
					d[j][kk] = Math.min(d[j][kk], d[j][i] + d[i][kk]);
				}
			}
		}
		// where? who in track? number of guys in track
		long[][][] dp = new long[n][k + 1][3];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j <= k; j++) {
				Arrays.fill(dp[i][j], Long.MAX_VALUE);
			}
		}
		dp[0][0][0] = 0;
		long res = Long.MAX_VALUE;
		for (int id = 0; id <= k; id++) {
			for (int cnt = 0; cnt < 3; cnt++) {
				for (int where = 0; where < n; where++) {
					long cdp = dp[where][id][cnt];
					if (cdp >= Long.MAX_VALUE / 2) {
						continue;
					}
					if (id == k && cnt == 0) {
						res = Math.min(res, cdp);
					}
					int nextGuy = id + cnt;
					if (cnt < 2 && nextGuy < k
							&& d[where][from[nextGuy]] < Long.MAX_VALUE / 2) {
						dp[from[nextGuy]][id][cnt + 1] = Math.min(
								dp[from[nextGuy]][id][cnt + 1], cdp
										+ d[where][from[nextGuy]]);
					}
					if (cnt != 0 && d[where][to[id]] < Long.MAX_VALUE / 2) {
						dp[to[id]][id + 1][cnt - 1] = Math.min(
								dp[to[id]][id + 1][cnt - 1], cdp
										+ d[where][to[id]]);
					}
				}
			}
		}
		out.println(res < Long.MAX_VALUE / 2 ? res : -1);
	}

	void solve() {
		int tc = in.nextInt();
		for (int t = 0; t < tc; t++) {
			System.err.println("test " + t);
			out.print("Case #" + (t + 1) + ": ");
			solveOneTest();
		}
	}

	void run() {
		try {
			in = new FastScanner(new File("C.in"));
			out = new PrintWriter(new File("C.out"));

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
		new C().run();
	}
}