import java.io.*;
import java.util.*;

public class B {
	FastScanner in;
	PrintWriter out;

	int[] change = new int[32];
	int[] stay = new int[32];

	void add(int x, int y, int mul) {
		for (int bit = 31; bit >= 0; bit--) {
			int v1 = x & (1 << bit);
			int v2 = y & (1 << bit);
			if (v1 == v2) {
				continue;
			}
			if (v1 > v2) {
				change[bit] += mul;
			} else {
				stay[bit] += mul;
			}
			return;
		}
	}

	int getAnswer() {
		int res = 0;
		for (int bit = 31; bit >= 0; bit--) {
			if (stay[bit] > 0 && change[bit] > 0) {
				return -1;
			}
			if (change[bit] > 0) {
				res |= 1 << bit;
			}
		}
		return res;
	}

	void solve() {
		int n = in.nextInt();
		int[] a = new int[n];
		for (int i = 0; i < n; i++) {
			a[i] = in.nextInt();
		}
		for (int i = 0; i + 1 < n; i++) {
			add(a[i], a[i + 1], 1);
		}
		out.println(getAnswer());
		int q = in.nextInt();
		for (int i = 0; i < q; i++) {
			int pos = in.nextInt() - 1;
			int newVal = in.nextInt();
			if (pos > 0) {
				add(a[pos - 1], a[pos], -1);
			}
			if (pos + 1 < n) {
				add(a[pos], a[pos + 1], -1);
			}
			a[pos] = newVal;
			if (pos > 0) {
				add(a[pos - 1], a[pos], 1);
			}
			if (pos + 1 < n) {
				add(a[pos], a[pos + 1], 1);
			}
			out.println(getAnswer());
		}
	}

	void run() {
		try {
			in = new FastScanner(new File("order.in"));
			out = new PrintWriter(new File("order.out"));

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
		new B().runIO();
	}
}