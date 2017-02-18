import java.io.*;
import java.util.*;

public class CopyOfD {
	FastScanner in;
	PrintWriter out;

	Random rnd = new Random(123);

	void solve() {
		for (int it = 0; it < 123123; it++) {
			System.err.println("oter = " + it);
			solve123();
		}
	}

	final int M = 10;

	boolean[] y;
	int[] best;
	int ans;

	private int best(int x, String mask) {
		int n = mask.length();
		y = new boolean[n];
		for (int i = 0; i < n; i++) {
			y[i] = mask.charAt(i) == 'Y';
		}
		ans = Integer.MAX_VALUE;
		cur = new int[n];

		go(n - 1, x, 0, 0);
		out.println("cost = " + ans);
		return ans;
	}

	int[] cur;

	void go(int pos, int sum, int last, int cost) {
		if (pos < 0) {
			if (cost < ans && sum == 0) {
				best = cur.clone();
				ans = cost;
			}
			return;
		}

		for (int p = last; p * (pos + 1) <= sum; p++) {
			cur[pos] = p;
			go(pos - 1, sum - p, p, cost + (y[pos] ? p : 0));
		}
	}


	void solve123() {
		// long START = System.currentTimeMillis();
		int x = 1 + rnd.nextInt(66);
		char[] ss = new char[1 + rnd.nextInt(M)];
		double p = rnd.nextDouble();
		for (int i = 0; i < ss.length; i++) {
			ss[i] = rnd.nextDouble() < p ? 'Y' : 'N';
		}
		String s = new String(ss);
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
				if (best(x, s) != 0) {
					throw new AssertionError();
				}
				out.println(0);
			} else {
				if (best(x, s) != x) {
					throw new AssertionError();
				}
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
		System.err.println(best);
		if (best != best(x, s)) {
			System.err.println(x + " "+  s);
			System.err.println("my =  "+ best);
			System.err.println("correct = " + best(x, s));
			throw new AssertionError();
		}
		int[] res = new int[s.length()];
		res[a[n].pos - 1] = (x - bestSum) / a[n].pos;
		if (res[a[n].pos - 1] < 0) {
			throw new AssertionError();
		}
		for (int i = n - 1; i >= 0; i--) {
			while (bestSum >= a[i].pos
					&& dp[i][bestSum - a[i].pos] + a[i].cntY == dp[i][bestSum]) {
				bestSum -= a[i].pos;
				res[a[i].pos - 1]++;
			}
		}
		for (int i = res.length - 1; i > 0; i--) {
			res[i - 1] += res[i];
		}
		int sum = 0;
		int sum2 = 0;
		for (int i = 0; i < res.length; i++) {
			if (i != 0 && res[i - 1] < res[i]) {
				throw new AssertionError();
			}
			out.print(res[i] + " ");
			sum += res[i];
			sum2 += s.charAt(i) == 'Y' ? res[i] : 0;
		}
		if (sum != x) {
			throw new AssertionError();
		}
		if (sum2 != best) {
			throw new AssertionError();
		}
		// System.err.println(System.currentTimeMillis() - START);
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
			in = new FastScanner(new File("input.txt"));
			out = new PrintWriter(new File("output.txt"));

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
		new CopyOfD().runIO();
	}
}