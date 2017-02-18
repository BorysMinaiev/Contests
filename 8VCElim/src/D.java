import java.io.*;
import java.util.*;

public class D {
	FastScanner in;
	PrintWriter out;

	class Fenw {
		int[] sum;
		int n;

		Fenw(int n) {
			this.n = n;
			sum = new int[n];
		}

		int get(int x) {
			int res = 0;
			for (; x >= 0; x = (x & (x + 1)) - 1) {
				res += sum[x];
			}
			return res;
		}

		void add(int pos) {
			for (; pos < n; pos |= pos + 1) {
				sum[pos]++;
			}
		}

		int get(int l, int r) {
			if (l > r) {
				return 0;
			}
			return get(r) - get(l - 1);
		}
	}

	void solve() {
		int n = in.nextInt();
		int k = in.nextInt();
		Fenw f = new Fenw(n);
		int cur = 0;
		long res = 1;
		for (int i = 0; i < n; i++) {
			int sum = 0;
			if (cur + k <= n) {
				sum = f.get(cur + 1, cur + k - 1);
			} else {
				sum = f.get(cur + 1, n - 1) + f.get(0, k - (n - cur) - 1);
			}
			f.add(cur);
			cur = (cur + k) % n;
			f.add(cur);
			res += sum + 1;
			out.println(res);
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