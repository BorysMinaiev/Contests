import java.io.*;
import java.util.*;

public class D {
	FastScanner in;
	PrintWriter out;

	void solve() {
		int n = in.nextInt();
		String s = in.next();
		final int MAX = 21;
		int[][] dp = new int[n + 1][1 << MAX];
		// [end][mask]
		for (int i = 0; i <= n; i++) {
			dp[i][0] = 1;
		}
		final int mod = (int) 1e9 + 7;
		for (int last = 0; last < n; last++) {
			for (int next = last + 1; next <= n && next < last + 7; next++) {
				int value = Integer.parseInt(s.substring(last, next), 2);
				if (value > 0 && value <= MAX) {
					value--;
					for (int mask = 0; mask < 1 << MAX; mask++) {
						if (dp[last][mask] != 0) {
							dp[next][mask | (1 << value)] += dp[last][mask];
							if (dp[next][mask | (1 << value)] >= mod) {
								dp[next][mask | (1 << value)] -= mod;
							}
						}
					}
				}
			}
		}
		long result = 0;
		for (int last = 1; last <= n; last++) {
			for (int mask = 1; mask < 1 << MAX; mask = mask * 2 + 1) {
				result += dp[last][mask];
			}
		}
		out.println(result % mod);
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