import java.util.*;
import java.io.*;

public class FFTModulo {

	int MAX = 500_000;
	int LOG = 20;
	int len = 1 << LOG;

	final int MOD = 7 * 17 * (1 << 23) + 1;
	int G = 3;

	int pow(int a, int b) {
		int res = 1;
		while (b > 0) {
			if ((b & 1) == 1) {
				res = (int) ((1L * res * a) % MOD);
			}
			a = (int) ((1L * a * a) % MOD);
			b >>= 1;
		}
		return res;
	}

	int[] divs = new int[len], res = new int[len];
	int[] tail = new int[len], omega = new int[len];

	void FFT(int[] a, boolean inv) {
		for (int i = 0; i < len; i++) {
			if (i < tail[i]) {
				int tmp = a[i];
				a[i] = a[tail[i]];
				a[tail[i]] = tmp;
			}
		}

		for (int len = 2; len <= a.length; len *= 2) {
			for (int i = 0; i < a.length; i += len) {
				int pos = 0;
				for (int j = 0, k = len / 2; k < len; j++, k++) {
					int u = a[i + j], v = (int) ((1L * a[i + k] * omega[pos]) % MOD);

					a[i + j] = u + v;
					if (a[i + j] >= MOD) {
						a[i + j] -= MOD;
					}
					a[i + k] = u - v;
					if (a[i + k] < 0) {
						a[i + k] += MOD;
					}

					if (inv) {
						pos -= a.length / len;
						if (pos < 0) {
							pos += a.length;
						}
					} else {
						pos += a.length / len;
					}
				}
			}
		}
		if (inv) {
			int coef = pow(a.length, MOD - 2);
			for (int i = 0; i < a.length; i++) {
				a[i] = (int) ((1L * a[i] * coef) % MOD);
			}
		}
	}

	void prepare() {
		int om = pow(G, (MOD - 1) / len);
		omega[0] = 1;
		for (int i = 1; i < len; i++) {
			omega[i] = (int) ((1L * omega[i - 1] * om) % MOD);
			tail[i] = ((tail[i >> 1] >> 1) | ((i & 1) << (LOG - 1)));
		}

		for (int i = 1; i <= MAX; i++) {
			for (int j = 1; i * j <= MAX; j++) {
				divs[i * j]++;
			}
		}
	}

	void solve() {
		prepare();

		FFT(divs, false);
		for (int i = 0; i < len; i++) {
			res[i] = (int) ((1L * divs[i] * divs[i]) % MOD);
		}
		FFT(res, true);

		int n = in.nextInt();
		for (int i = 0; i < n; i++) {
			int l = in.nextInt(), r = in.nextInt();
			int mx = l;
			for (int j = l + 1; j <= r; j++) {
				if (res[j] > res[mx]) {
					mx = j;
				}
			}
			out.println(mx + " " + res[mx]);
		}
	}

	FastScanner in;
	PrintWriter out;

	void run() {
		in = new FastScanner();
		out = new PrintWriter(System.out);
		solve();
		out.close();
	}

	class FastScanner {
		BufferedReader br;
		StringTokenizer st;

		public FastScanner() {
			br = new BufferedReader(new InputStreamReader(System.in));
		}

		String next() {
			while (st == null || !st.hasMoreElements()) {
				try {
					st = new StringTokenizer(br.readLine());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return st.nextToken();
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
		new FFTModulo().run();
	}
}
