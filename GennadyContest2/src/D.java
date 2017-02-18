import java.io.*;
import java.util.*;

public class D {
	FastScanner in;
	PrintWriter out;

	void solve() {
		int x = in.nextInt();
		String s = in.next();
		O[] a = new O[s.length()];
		int cntY = 0;
		for (int i = 0; i < s.length(); i++) {
			cntY += s.charAt(i) == 'Y' ? 1 : 0;
			a[i] = new O(i + 1, cntY);
		}
		Arrays.sort(a);
		int n = a.length - 1;
		final int MAX = n * n + 10;
		int[][] dp = new int[n][MAX];
		for (int i = 0; i < n; i++) {
			Arrays.fill(dp[i], Integer.MAX_VALUE);
			dp[i][0] = 0;
			for (int j = 1; j < MAX; j++) {
				if (i != 0) {
					dp[i][j] = dp[i - 1][j];
				}
				if (j >= a[i].pos && dp[i][j - a[i].pos] != Integer.MAX_VALUE) {
					dp[i][j] = Math.min(dp[i][j], dp[i][j - a[i].pos]
							+ a[i].cntY);
				}
			}
		}
		if (n == 0) {
			if (a[0].cntY == 0) {
				out.println(0);
			} else {
				out.println(x);
			}
			out.println(x);
			return;
		}
		int best = Integer.MAX_VALUE;
		int bestSum = -1;
		for (int su = 0; su < MAX && su <= x; su++) {
			if (dp[n - 1][su] != Integer.MAX_VALUE && (x - su) % a[n].pos == 0) {
				int curSum = a[n].cntY * ((x - su) / a[n].pos) + dp[n - 1][su];
				if (best > curSum) {
					best = curSum;
					bestSum = su;
				}
			}
		}
		out.println(best);
		int[] res = new int[n + 1];
		res[a[n].pos - 1] = (x - bestSum) / a[n].pos;
		for (int i = n - 1; i >= 0; i--) {
			while (bestSum >= a[i].pos
					&& dp[i][bestSum - a[i].pos] + a[i].cntY == dp[i][bestSum]) {
				bestSum -= a[i].pos;
				res[a[i].pos - 1]++;
			}
		}
		for (int i = n; i > 0; i--) {
			res[i - 1] += res[i];
		}
		for (int i = 0; i <= n; i++) {
			out.print(res[i] + " ");
		}
	}

	class O implements Comparable<O> {
		int pos;
		int cntY;

		public O(int pos, int cntY) {
			super();
			this.pos = pos;
			this.cntY = cntY;
		}

		public int compareTo(O o) {
			return -Integer.compare(cntY * o.pos, o.cntY * pos);
		}

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