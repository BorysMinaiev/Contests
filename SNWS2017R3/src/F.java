import java.io.*;
import java.util.*;

public class F {
	FastScanner in;
	PrintWriter out;

	class SegmentTreeMax {
		int[] max;

		SegmentTreeMax(int n) {
			max = new int[n * 4];
		}

		void upd(int v, int l, int r, int pos, int val) {
			if (l == r) {
				max[v] = Math.max(max[v], val);
			} else {
				int mid = (l + r) >> 1;
				if (mid >= pos) {
					upd(v * 2 + 1, l, mid, pos, val);
				} else {
					upd(v * 2 + 2, mid + 1, r, pos, val);
				}
				max[v] = Math.max(max[v * 2 + 1], max[v * 2 + 2]);
			}
		}

		int get(int v, int l, int r, int needL, int needR) {
			if (needL > needR) {
				return 0;
			}
			if (l == needL && r == needR) {
				return max[v];
			}
			int mid = (l + r) >> 1;
			return Math
					.max(get(v * 2 + 1, l, mid, needL, Math.min(needR, mid)),
							get(v * 2 + 2, mid + 1, r,
									Math.max(mid + 1, needL), needR));
		}
	}

	void solve() {
		final int MAX = (int) 1e5 + 10;
		SegmentTreeMax stUp = new SegmentTreeMax(MAX);
		SegmentTreeMax stDown = new SegmentTreeMax(MAX);
		int n = in.nextInt();
		int res = 0;
		for (int i = 0; i < n; i++) {
			int val = in.nextInt();
			int r = stUp.get(0, 0, MAX - 1, 0, val - 1);
			stDown.upd(0, 0, MAX - 1, val, r + 1);
			res = Math.max(res, r + 1);
			r = stDown.get(0, 0, MAX - 1, val + 1, MAX - 1);
			res = Math.max(res, r + 1);
			stUp.upd(0, 0, MAX - 1, val, r + 1);
		}
		out.println(res);
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
		new F().runIO();
	}
}