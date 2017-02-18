import java.io.*;
import java.util.*;

public class F {
	FastScanner in;
	PrintWriter out;

	int[] w;
	int[] opt;
	int[] vals;

	class O implements Comparable<O> {
		int pos;
		int value;

		public O(int pos, int value) {
			super();
			this.pos = pos;
			this.value = value;
		}

		public int compareTo(O o) {
			return Integer.compare(o.value, value);
		}

	}

	int[] ans;

	void update(int x, int w) {
		opt[x] = vals[x];
		if (x * 2 + 1 < n) {
			opt[x] += opt[x * 2 + 1];
		}
		if (x * 2 + 2 < n) {
			opt[x] += opt[x * 2 + 2];
		}
		opt[x] = Math.max(opt[x], 0);
		if (opt[x] - 1 >= 0) {
			ans[opt[x] - 1] = Math.max(ans[opt[x] - 1], w);
		}
		if (x > 0) {
			update((x - 1) / 2, w);
		}
	}

	int n;

	void solve() {
		while (in.hasMoreTokens()) {
			n = in.nextInt();
			O[] a = new O[n];
			for (int i = 0; i < n; i++) {
				a[i] = new O(i, in.nextInt());
			}
			Arrays.sort(a);
			ans = new int[n];
			opt = new int[n];
			vals = new int[n];
			Arrays.fill(vals, -1);
			for (O o : a) {
				vals[o.pos] = 1;
				update(o.pos, o.value);
			}
			for (int i = ans.length - 2; i >= 0; i--) {
				ans[i] = Math.max(ans[i], ans[i + 1]);
			}
			for (int i = 0; i < ans.length; i++) {
				out.print(ans[i] + " ");
			}
			out.println();
		}
	}

	void run() {
		try {
			in = new FastScanner(new File("F.in"));
			out = new PrintWriter(new File("F.out"));

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
		new F().runIO();
	}
}