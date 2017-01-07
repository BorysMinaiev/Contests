import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Karatsuba {
	FastScanner in;
	PrintWriter out;

	void run() {
		try {
			in = new FastScanner(new File("transportation.txt"));
			out = new PrintWriter(new File("E.out"));

			solve();

			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	void solve() {
		fact = new long[MAX];
		factInv = new long[MAX];
		fact[0] = 1;
		for (int i = 1; i < MAX; i++) {
			fact[i] = i * 1L * fact[i - 1] % mod;
		}
		BigInteger MOD = BigInteger.valueOf(mod);
		for (int i = 0; i < MAX; i++) {
			factInv[i] = BigInteger.valueOf(fact[i]).modInverse(MOD).intValue();
		}
		solve(1, Integer.MAX_VALUE, 4);
		// solve(2, 2, 1);
	}

	long c(int n, int k) {
		if (k < 0 || k > n) {
			return 0;
		}
		long res = fact[n] * factInv[k] % mod * factInv[n - k] % mod;
		return res;
	}

	final int MAX = 500000 + 100;
	long[] fact;
	long[] factInv;

	final int mod = (int) 1e9 + 7;
	final long mod2 = mod * 1L * mod;
	final int C = 10;

	long[] mult(long[] a, long[] b) {

		long[] res = new long[a.length + b.length - 1];
		int n = a.length;
		if (b.length != n) {
			throw new AssertionError();
		}
		if (n <= C) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					res[i + j] += a[i] * 1L * b[j];
					if (res[i + j] >= mod2) {
						res[i + j] -= mod2;
					}
				}
			}
		} else {
			int mid = n / 2;
			long[] aLeft = new long[mid];
			long[] aRight = new long[n - mid];
			long[] bLeft = new long[mid];
			long[] bRight = new long[n - mid];
			for (int i = 0; i < mid; i++) {
				aLeft[i] = a[i];
				bLeft[i] = b[i];
			}
			for (int i = 0; i < n - mid; i++) {
				aRight[i] = a[i + mid];
				bRight[i] = b[i + mid];
			}
			long[] Q = mult(aLeft, bLeft);
			long[] W = mult(aRight, bRight);
			long[] tmpA = new long[Math.max(aLeft.length, aRight.length)];
			long[] tmpB = new long[tmpA.length];
			for (int i = 0; i < aLeft.length; i++) {
				tmpA[i] = aLeft[i];
				tmpB[i] = bLeft[i];
			}
			for (int i = 0; i < aRight.length; i++) {
				tmpA[i] += aRight[i];
				tmpB[i] += bRight[i];
				if (tmpA[i] >= mod) {
					tmpA[i] -= mod;
				}
				if (tmpB[i] >= mod) {
					tmpB[i] -= mod;
				}
			}
			long[] E = mult(tmpA, tmpB);
			for (int i = 0; i < E.length; i++) {
				if (i < Q.length) {
					E[i] -= Q[i];
					if (E[i] < 0) {
						E[i] += mod;
					}
				}
				if (i < W.length) {
					E[i] -= W[i];
					if (E[i] < 0) {
						E[i] += mod;
					}
				}
			}
			for (int i = 0; i < Q.length; i++) {
				res[i] = Q[i];
			}
			for (int i = 0; i < E.length; i++) {
				res[i + mid] += E[i];
				if (res[i + mid] >= mod) {
					res[i + mid] -= mod;
				}
			}
			for (int i = 0; i < W.length; i++) {
				res[i + mid + mid] += W[i];
				if (res[i + mid + mid] >= mod) {
					res[i + mid + mid] -= mod;
				}
			}
		}
		for (int i = 0; i < res.length; i++) {
			res[i] %= mod;
		}
		return res;
	}

	class Testcase implements Callable<Testcase> {
		int test;
		long result;

		Testcase(int test) {
			this.test = test;
		}

		int n, k, l;
		long[] pos;

		void readInput() {
			n = in.nextInt();
			k = in.nextInt();
			l = in.nextInt();
			String s = in.next();
			pos = new long[n];
			n = 0;
			for (int i = 0; i < s.length(); i++) {
				if (s.charAt(i) == '1') {
					pos[n++] = i;
				}
			}
		}

		void solve() {
			BigInteger MOD = BigInteger.valueOf(mod);
			if (k == n - 1) {
				result = l * 1L
						* BigInteger.valueOf(4).modInverse(MOD).intValue()
						% mod;
				return;
			}
			long[] posRev = new long[n];
			for (int i = 0; i < n; i++) {
				posRev[i] = pos[n - 1 - i];
			}
			long[] mul = mult(pos, posRev);
			long[] avDist = new long[n + 1];
			long sum2 = 0, sum2shift = 0;
			for (int i = 0; i < n; i++) {
				sum2 += pos[i] * pos[i];
				sum2 %= mod;
			}
			long sum = 0;
			sum2shift = sum2;
			long[] pos2 = new long[n + n];
			for (int i = 0; i < n; i++) {
				pos2[i] = pos[i];
				pos2[i + n] = pos[i] + l;
			}
			pos = pos2;
			for (int d = 1; d < n; d++) {
				sum2shift -= pos[d - 1] * pos[d - 1];
				sum2shift += pos[n + d - 1] * pos[n + d - 1];
				sum2shift %= mod;
				sum += pos[n - d];
				sum %= mod;
				long tmp1 = mul[n - d - 1];
				long myMul = tmp1;
				long tmp2 = mul[2 * n - 1 - d];
				myMul += tmp2 + sum * l;
				myMul %= mod;
				avDist[d] = sum2 + sum2shift - 2 * myMul;
				avDist[d] %= mod;
				if (avDist[d] < 0) {
					avDist[d] += mod;
				}
			}
			long total = c(n, k);
			for (int d = 1; d < n; d++) {
				int needRem = k - (d - 1);
				if (needRem < 0) {
					break;
				}
				long now = c(n - 2 - d + 1, needRem);
				result += now * avDist[d] % mod
						* BigInteger.valueOf(4).modInverse(MOD).intValue()
						% mod;
				result %= mod;
			}
			result = result
					* BigInteger.valueOf(total).modInverse(MOD).intValue()
					% mod;
			result = result * BigInteger.valueOf(l).modInverse(MOD).intValue()
					% mod;
		}

		@Override
		public Testcase call() throws Exception {
			System.err.println("start doing test #" + test);
			solve();
			int total = testDone.addAndGet(1);
			System.err.println("done test #" + test + " (result = " + result
					+ "), total " + total + "/" + tc + " tests done in "
					+ (System.currentTimeMillis() - START_TIME) + " ms");
			return this;
		}

		public void writeOutput() {
			out.println("Case #" + test + ": " + result);
			out.flush();
		}

	}

	long START_TIME;
	int tc;
	AtomicInteger testDone = new AtomicInteger();

	void solve(int testFrom, int testTo, int numThreads) {
		START_TIME = System.currentTimeMillis();
		tc = in.nextInt();
		List<Future<Testcase>> tests = new ArrayList<Future<Testcase>>();
		ExecutorService pool = Executors.newFixedThreadPool(numThreads);
		for (int t = 0; t < tc; t++) {
			Testcase test = new Testcase(t + 1);
			test.readInput();
			if (t + 1 >= testFrom && t + 1 <= testTo) {
				tests.add(pool.submit(test));
			}
		}
		for (Future<Testcase> test : tests) {
			try {
				Testcase testcase = test.get();
				testcase.writeOutput();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} catch (ExecutionException e) {
				throw new RuntimeException(e);
			}
		}
		pool.shutdown();
		System.err.println("all in "
				+ (System.currentTimeMillis() - START_TIME) + " ms");
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
		new Karatsuba().run();
	}
}