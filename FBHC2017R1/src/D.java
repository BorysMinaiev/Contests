import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class D {
	FastScanner in;
	PrintWriter out;

	final int mod = (int) 1e9 + 7;
	final int MAX = 4444;

	int[] fact;
	int[] factInv;

	int c(int n, int k) {
		if (k >= mod) {
			throw new AssertionError();
		}
		int res = factInv[k];
		for (int i = n; i > n - k; i--) {
			res = (int) ((res * 1L * i) % mod);
		}
		return res;
	}

	void solveOneTest() {
		int n = in.nextInt();
		int m = in.nextInt();
		int[] r = new int[n];
		int sumR2 = 0;
		for (int i = 0; i < n; i++) {
			r[i] = in.nextInt();
			sumR2 += r[i] * 2;
		}
		if (n == 1) {
			out.println(m);
			return;
		}
		int[] sum = new int[4004];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i != j) {
					sum[r[i] + r[j]]++;
				}
			}
		}
		long res = 0;
		for (int ss = 0; ss < sum.length; ss++) {
			int cnt = sum[ss];
			if (cnt == 0) {
				continue;
			}
			int needLen = sumR2 - ss;
			int free = m - 1 - needLen;
			if (free < 0) {
				continue;
			}
			res = (res + cnt * 1L * fact[n - 2] % mod * 1L * c(free + n, n)) % mod;
		}
		out.println(res % mod);
	}

	void solve() {
		fact = new int[MAX];
		fact[0] = 1;
		factInv = new int[MAX];
		for (int i = 1; i < MAX; i++) {
			fact[i] = (int) ((fact[i - 1] * 1L * i) % mod);
		}
		for (int i = 0; i < MAX; i++) {
			factInv[i] = BigInteger.valueOf(fact[i])
					.modInverse(BigInteger.valueOf(mod)).intValue();
		}
		int tc = in.nextInt();
		for (int t = 0; t < tc; t++) {
			System.err.println("test " + t);
			out.print("Case #" + (t + 1) + ": ");
			solveOneTest();
		}
	}

	void run() {
		try {
			in = new FastScanner(new File("D.in"));
			out = new PrintWriter(new File("D.out"));

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
		new D().run();
	}
}