import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class AOK {
	FastScanner in;
	PrintWriter out;

	long gcd(long x, long y) {
		return x == 0 ? y : gcd(y % x, x);
	}

	void solve() {
		int n = in.nextInt();
		long[] a = new long[n];
		long[] c = new long[n];
		BigInteger sum = BigInteger.ZERO;
		for (int i = 0; i < n; i++) {
			a[i] = in.nextLong();
			sum = sum.add(BigInteger.valueOf(a[i]));
		}
		long[] b = new long[n];
		long bSum = 0;
		for (int i = 0; i < n; i++) {
			b[i] = in.nextLong();
			bSum += b[i];
		}
		Arrays.sort(b);
		for (long mod = b[n - 1] + 1;; mod++) {
			long sA = sum.mod(BigInteger.valueOf(mod)).longValue();
			long sB = bSum % mod;
			// (sA + k * n) % mod == sB
			long a1 = n, b1 = (sB - sA + mod) % mod;
			long g = gcd(a1, mod);
			if (b1 % g != 0) {
				continue;
			}
			long a2 = a1 / g;
			long b2 = b1 / g;
			long k = b2
					* BigInteger.valueOf(a2)
							.modInverse(BigInteger.valueOf(mod / g))
							.longValue() % mod;
//			if (a2 * k % (mod / g) != b2) {
//				throw new AssertionError();
//			}
			for (int i = 0; i < g; i++) {
//				if ((sA + k * n) % mod != sB) {
//					throw new AssertionError();
//				}
				boolean ok = true;
				for (long x : a) {
					long nx = (x + k) % mod;
					if (Arrays.binarySearch(b, nx) < 0) {
						ok = false;
						break;
					}
				}
				if (ok) {
					for (int j = 0; j < n; j++) {
						c[j] = (a[j] + k) % mod;
					}
					Arrays.sort(c);
					if (Arrays.equals(b, c)) {
						out.println(mod + " " + k);
						return;
					}
				}
				k = (k + (mod / g));
				k %= mod;
			}
		}
	}

	void run() {
		try {
			in = new FastScanner(new File("A.in"));
			out = new PrintWriter(new File("A.out"));

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
		new AOK().runIO();
	}
}