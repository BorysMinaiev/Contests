import java.io.*;
import java.util.*;

public class C {
	FastScanner in;
	PrintWriter out;

	void solve() {
		String s = in.next();
		int n = s.length();
		int[][] dp = new int[s.length() + 1][27];
		for (int i = 0; i < dp.length; i++) {
			Arrays.fill(dp[i], Integer.MAX_VALUE / 2);
		}
		dp[0][0] = 0;
		int res = Integer.MAX_VALUE;
		for (int len = 0; len <= s.length(); len++) {
			for (int alr = 0; alr < 26; alr++) {
				if (len < s.length() && s.charAt(len) == 'a' + alr) {
					dp[len + 1][alr + 1] = Math.min(dp[len + 1][alr + 1],
							dp[len][alr]);
				}
				if (len < s.length()) {
					dp[len + 1][alr] = Math.min(dp[len + 1][alr], dp[len][alr]);
				}
				dp[len][alr + 1] = Math.min(dp[len][alr + 1], dp[len][alr] + 1);
			}
			res = Math.min(res, dp[len][26]);
		}
		out.println(res);
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