import java.io.*;
import java.util.*;

public class J {
	FastScanner in;
	PrintWriter out;

	void solve() {
		int n = in.nextInt();
		int m = in.nextInt();
		int k = in.nextInt();
		int[] ids = new int[n];
		for (int i = 0; i < n; i++) {
			ids[i] = in.nextInt() - 1;
		}
		int[] sum = new int[m];
		for (int i : ids) {
			sum[i]++;
		}
		int[] cur = new int[m];
		boolean[] can = new boolean[n + 1];
		can[0] = true;
		int maxRes = -1;
		long bestResult = Long.MAX_VALUE;
		for (int pos = 0; pos < n; pos++) {
			cur[ids[pos]]++;
			if (cur[ids[pos]] == sum[ids[pos]]) {
				int sz = sum[ids[pos]];
				for (int i = can.length - 1; i >= sz; i--) {
					can[i] |= can[i - sz];
				}
				for (int i = Math.max(maxRes, Math.max(0, n - k)); i <= k; i++) {
					if (can[i]) {
						maxRes = i;
					}
				}
				if (maxRes != -1) {
					bestResult = Math.min(bestResult, maxRes * 1L * (pos + 1)
							+ (n - maxRes) * 1L * n);
				}
			}
		}
		out.println(bestResult == Long.MAX_VALUE ? -1 : bestResult);
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
		new J().runIO();
	}
}