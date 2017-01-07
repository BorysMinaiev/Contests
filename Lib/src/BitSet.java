import java.io.*;
import java.util.*;

public class BitSet {
	FastScanner in;
	PrintWriter out;

	class MyBitSet {
		int n;
		long[] a;

		public MyBitSet(int n) {
			this.n = n;
			a = new long[(n + 63) >> 6];
		}

		public MyBitSet(String s) {
			n = s.length();
			a = new long[(n + 63) >> 6];
			for (int i = 0; i < s.length(); i++) {
				if (s.charAt(i) == '1') {
					set(i);
				}
			}
		}

		void set(int bit) {
			a[bit >> 6] |= 1L << (bit & 63);
		}

		void rotateLeft(int sz) {
			sz %= n;
			long[] b = new long[(n + 63) >> 6];
			int full = sz >> 6;
			int small = sz & 63;
			for (int i = full; i < a.length; i++) {
				b[i - full] |= a[i] >>> small;
			}
			if (small != 0) {
				for (int i = full + 1; i < a.length; i++) {
					b[i - 1 - full] |= a[i] << (64 - small);
				}
			}
			if (sz != 0) {
				int right = n - sz;
				full = right >> 6;
				small = right & 63;
				for (int i = 0; i + full < a.length; i++) {
					b[i + full] |= a[i] << small;
				}
				if (small != 0) {
					for (int i = 0; i + full + 1 < a.length; i++) {
						b[i + full + 1] |= a[i] >>> (64 - small);
					}
				}
			}
			a = b;
		}

		int get(int bit) {
			return (int) ((a[bit >> 6] >> (bit & 63)) & 1);
		}

		public String toString() {
			char[] res = new char[n];
			for (int i = 0; i < n; i++) {
				res[i] = get(i) == 1 ? '1' : '0';
			}
			return new String(res);
		}

		void clear() {
			Arrays.fill(a, 0);
		}

		void andNot(MyBitSet another) {
			for (int i = 0; i < a.length; i++) {
				a[i] &= ~another.a[i];
			}
		}
	}

	void solve() {
		Random rnd = new Random(123);
		final int MAX = 8000;
		for (int it = 0; it < 123123; it++) {
			System.err.println("iter = " + it);
			int n = 1 + rnd.nextInt(MAX);
			char[] a = new char[n];
			int shift = 1 + rnd.nextInt(MAX);
			char[] b = new char[n];
			for (int i = 0; i < n; i++) {
				a[i] = rnd.nextBoolean() ? '1' : '0';
				int pos = ((i - shift) % n + n) % n;
				b[pos] = a[i];
			}
			MyBitSet bs = new MyBitSet(new String(a));
			bs.rotateLeft(shift);
			String got = bs.toString();
			String need = new String(b);
			if (!got.equals(need)) {
				System.err.println("was = " + new String(a));
				System.err.println("shift = " + shift);
				System.err.println("got = " + got);
				System.err.println("ned = " + need);
				throw new AssertionError();
			}
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
		new BitSet().runIO();
	}
}