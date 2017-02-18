import java.io.*;
import java.util.*;

public class D {
	FastScanner in;
	PrintWriter out;

	void solve() {
		int n = in.nextInt();
		int[] a = new int[n];
		int sum = 0;
		for (int i = 0; i < n; i++) {
			a[i] = in.nextInt();
			sum += a[i];
		}
		int[] cnt = new int[1 << 25];
		int[] all = new int[n];
		for (int k = 1; k <= n; k++) {
			if (sum % k != 0) {
				out.print(0);
			} else {
				if (sum == 0) {
					out.print(1);
					continue;
				}
				int need = sum / k;
				boolean ok = false;
				if (need < cnt.length) {
					for (int i = 0; i < need; i++) {
						cnt[i] = 0;
					}
					int su = 0;
					for (int i = 0; i < n; i++) {
						su += a[i];
						if (a[i] == 0)
							continue;
						su %= need;
						cnt[su]++;
						if (cnt[su] >= k) {
							ok = true;
							break;
						}
					}
				} else {
					int su = 0;
					int sz = 0;
					for (int i = 0; i < n; i++) {
						su += a[i];
						if (a[i] == 0)
							continue;
						su %= need;
						all[sz++] = su;
					}
					Arrays.sort(all, 0, sz);
					for (int i = 0; i + k <= n; i++) {
						if (all[i] - all[i + k - 1] == 0) {
							ok = true;
							break;
						}
					}
				}
				out.print(ok ? 1 : 0);
			}
		}
		out.println();
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