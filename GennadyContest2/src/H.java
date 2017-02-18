import java.io.*;
import java.util.*;

public class H {
	FastScanner in;
	PrintWriter out;

	boolean go(boolean[] used, int[] a, int pos, int n, int sum) {
		if (pos == n) {
			return true;
		}
		for (int x = 1; x <= n; x++) {
			if (!used[x] && !isPrime[sum + x]) {
				used[x] = true;
				a[pos] = x;
				if (go(used, a, pos + 1, n, sum + x)) {
					return true;
				}
				used[x] = false;
			}
		}
		return false;
	}

	final int MAX = 100 * 100;

	int[] solve(int n) {
		int sum = 0;
		for (int i = 1; i <= n; i++) {
			sum += i;
		}
		if (isPrime[sum]) {
			return null;
		}
		int[] res = new int[n];
		if (!go(new boolean[n + 1], res, 0, n, 0)) {
			return null;
		}
		return res;
	}

	boolean[] isPrime;

	void solve() {
		isPrime = new boolean[MAX];
		Arrays.fill(isPrime, true);
//		isPrime[1] = false;
		for (int i = 2; i < MAX; i++) {
			if (isPrime[i]) {
				for (int j = i + i; j < MAX; j += i) {
					isPrime[j] = false;
				}
			}
		}
		int n = in.nextInt();
		int[] res = solve(n);
		if (res == null) {
			out.println(-1);
		} else {
			for (int x : res ) {
				out.print(x+" ");
			}
		}
		// for (int n = 1; n <= 100; n++) {
		// System.err.println(Arrays.toString(solve(n)));
		// }
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
		new H().runIO();
	}
}