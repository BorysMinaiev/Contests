import java.io.*;
import java.util.*;

public class A {
	FastScanner in;
	PrintWriter out;

	int MAX = 100000;
	Random rng = new Random(123);

	long COEF = (long) 1e9;
	int TAKE = 1000;
	int X = 10;
	Set<Long> set = new HashSet<>();

	long[] a, b;
	long[] bDiff;
	
	int LOOKUP_SIZE = 10_000_000;
	
	void precalcB() {
		Arrays.sort(b);
		bDiff = new long[b.length - 1];
		for (int i = 0; i < b.length - 1; i++) {
			bDiff[i] = bDiff[i + b.length] = b[i + 1] - b[i];
		}
		
		for (int i = 0; i < b.length; i++) {
			for (int j = i + 1; j < b.length; j++) {
				
			}
		}
	}

	void solve() {
		int n = in.nextInt();
		a = new long[n];
		b = new long[n];
		for (int i = 0; i < n; i++) {
			a[i] = in.nextLong();
		}
		long maxB = 0;
		for (int i = 0; i < n; i++) {
			b[i] = in.nextLong();
			maxB = Math.max(maxB, b[i]);
		}

		precalcB();

		for (long m = maxB + 1;; m++) {

			m++;
		}
	}

	void stress() {
		for (int IT = 0; IT < 100; IT++) {
			long m = randomLong() % 10_000_000_000L + 1;
			long[][] as = generate(m);

			long min = Long.MAX_VALUE, max = Long.MIN_VALUE;
			for (long i : as[1]) {
				min = Math.min(min, i);
				max = Math.max(max, i);
			}
			System.err.println(Math.abs(min + max - m));
		}
	}

	long randomLong() {
		return (long) 1e9 * rng.nextInt((int) 1e9) + rng.nextInt((int) 1e9) + 1;
	}

	long[][] generate(long m) {
		long[] a = new long[MAX];
		long[] b = new long[MAX];

		long k = randomLong() % m;
		for (int i = 0; i < a.length; i++) {
			a[i] = randomLong();
			b[i] = (a[i] + k) % m;
		}
		for (int i = 1; i < b.length; i++) {
			int pos = rng.nextInt(i + 1);
			long tmp = b[i];
			b[i] = b[pos];
			b[pos] = tmp;
		}
		return new long[][] { a, b };
	}

	void run() {

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
		new A().run();
	}
}