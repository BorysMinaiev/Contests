import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class J {
	FastScanner in;
	PrintWriter out;

	class O implements Comparable<O> {
		int p, q, b, id;

		public O(int p, int q, int b, int id) {
			super();
			this.p = p;
			this.q = q;
			this.b = b;
			this.id = id;
		}

		public int compareTo(O o) {
			int s = signOne(p, q);
			int s2 = signOne(o.p, o.q);
			if (s != s2) {
				return Integer.compare(s, s2);
			}
			if (s == 0) {
				return 0;
			}
			return -cmp(b * 1L * q, p - q, o.b * 1L * o.q, o.p - o.q);
		}

	}

	int signOne(int p, int q) {
		if (p < q) {
			return -1;
		}
		return p > q ? 1 : 0;
	}

	// a/ b < c / d
	int cmp(long a, long b, long c, long d) {
		if (b == 0 || d == 0) {
			throw new AssertionError();
		}
		if (b < 0) {
			a *= -1;
			b *= -1;
		}
		if (d < 0) {
			c *= -1;
			d *= -1;
		}
		long f1 = a / b;
		if (a < 0 && a % b != 0) {
			f1--;
		}
		long f2 = c / d;
		if (c < 0 && c % d != 0) {
			f2--;
		}
		if (f1 != f2) {
			return Long.compare(f1, f2);
		}
		a -= f1 * b;
		c -= f2 * d;
		long left = a * d;
		long right = c * b;
		int cmpUp = Long.compare(left, right);
		return cmpUp;
	}

	double solve(O[] a, boolean[] was, double res) {
		double ans = res;
		for (int i = 0; i < was.length; i++) {
			if (!was[i]) {
				was[i] = true;
				ans = Math.max(
						ans,
						solve(a, was,
								Math.max(res, res * a[i].p / a[i].q + a[i].b)));
				was[i] = false;
			}
		}
		return ans;
	}

	double solveStupid(O[] a) {
		return solve(a, new boolean[a.length], 0);
	}

	double getAns(O[] a) {
		double res = 0;
		for (O o : a) {
			res = Math.max(res, res * o.p / o.q + o.b);
		}
		return res;
	}

	void solve123() {
		Random rnd = new Random(123);
		final int MAX = 10;
		for (int it = 0; it < 123123; it++) {
			System.err.println("ote =" + it);
			int n = 1 + rnd.nextInt(7);
			O[] a = new O[n];
			for (int i = 0; i < n; i++) {
				a[i] = new O(1 + rnd.nextInt(MAX), 1 + rnd.nextInt(MAX),
						rnd.nextInt(MAX) - MAX / 2, i + 1);
			}
			Arrays.sort(a);
			double my = getAns(a);
			System.err.println(my);
			double correct = solveStupid(a);
			if (Math.abs(correct - my) > 1e-9) {
				System.err.println("my " + my);
				System.err.println("correct " + correct);
				throw new AssertionError();
			}
		}
	}

	void solve() {
//		long START = System.currentTimeMillis();
		int tc = in.nextInt();
//		final int MAX = (int) 1e9;
//		Random rnd = new Random(123);
		for (int t = 0; t < tc; t++) {
			int n = in.nextInt();
			O[] a = new O[n];
			 for (int i = 0; i < n; i++) {
			 a[i] = new O(in.nextInt(), in.nextInt(), in.nextInt(), i + 1);
			 }
//			for (int i = 0; i < n; i++) {
//				a[i] = new O(1 + rnd.nextInt(MAX), 1 + rnd.nextInt(MAX),
//						rnd.nextInt(MAX) - MAX / 2, i + 1);
//			}
			Arrays.sort(a);
			 for (O o : a) {
			 out.print(o.id + " ");
			 }
			 out.println();
		}
		// System.err.println(System.currentTimeMillis() - START);
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
		new J().runIO();
	}
}