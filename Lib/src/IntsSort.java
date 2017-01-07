import java.io.*;
import java.util.*;

public class IntsSort {
	FastScanner in;
	PrintWriter out;

	// [from, to)
	public static void sort(int[] a, int from, int to) {
		int n = to - from;
		int[] temp = new int[n];
		int[] cnt = new int[1 << 16];
		for (int i = to - 1; i >= from; --i) {
			++cnt[low(a[i])];
		}
		for (int i = 0; i < cnt.length - 1; ++i) {
			cnt[i + 1] += cnt[i];
		}
		for (int i = to - 1; i >= from; --i) {
			temp[--cnt[low(a[i])]] = a[i];
		}

		Arrays.fill(cnt, 0);
		for (int i = n - 1; i >= 0; --i) {
			++cnt[high(temp[i])];
		}
		cnt[0] += from;
		for (int i = 0; i < cnt.length - 1; ++i) {
			cnt[i + 1] += cnt[i];
		}
		for (int i = n - 1; i >= 0; --i) {
			a[--cnt[high(temp[i])]] = temp[i];
		}
	}

	private static int high(int a) {
		return (a ^ Integer.MIN_VALUE) >>> 16;
	}

	private static int low(int a) {
		return a & 0xFFFF;
	}

	void solve() {

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
		new IntsSort().runIO();
	}
}