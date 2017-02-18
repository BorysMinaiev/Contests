import java.io.*;
import java.util.*;

public class C {
	FastScanner in;
	PrintWriter out;

	int[] genW(int n, int w1, int aw, int bw, int m) {
		int[] res = new int[m];
		res[0] = w1;
		for (int i = 1; i < m; i++) {
			res[i] = (int) (((res[i - 1] * 1L * aw + bw) % n)) + 1;
		}
		return res;
	}

	int[] genD(int d1, int ad, int bd, int m) {
		int[] res = new int[m];
		res[0] = d1;
		for (int i = 1; i < m; i++) {
			res[i] = (int) (((res[i - 1] * 1L * ad + bd) % 3));
		}
		return res;
	}

	int[] genS(int s1, int as, int bs, int m) {
		int[] res = new int[m];
		res[0] = s1;
		for (int i = 1; i < m; i++) {
			res[i] = (int) (((res[i - 1] * 1L * as + bs) % 1000000000)) + 1;
		}
		return res;
	}

	final int mod = (int) 1e9 + 7;

	class Segment {
		int[][] vals;

		public Segment(int goLeft, int stayHere, int goRight) {
			vals = new int[2][2];
			vals[0][0] = stayHere;
			vals[1][0] = goLeft;
			vals[0][1] = goRight;
		}

		public Segment() {
			vals = new int[2][2];
		}
	}

	Segment join(Segment l, Segment r) {
		Segment res = new Segment();
		for (int left = 0; left < 2; left++) {
			for (int right = 0; right < 2; right++) {
				res.vals[left][right] = (int) ((l.vals[left][0] * 1L
						* r.vals[0][right] + l.vals[left][1] * 1L
						* r.vals[1][right]) % mod);
			}
		}
		return res;
	}

	class SegmentTree {
		int n;
		Segment[] segs;

		public SegmentTree(int n) {
			this.n = n;
			segs =new Segment[n * 4];
			build(0, 0, n - 1);
		}

		void build(int v, int l, int r) {
			if (l == r) {
				segs[v] = new Segment(0, 1, 0);
			} else {
				int mid = (l + r) >> 1;
				build(v * 2 + 1, l, mid);
				build(v * 2 + 2, mid + 1, r);
				segs[v] = join(segs[v * 2 + 1], segs[v * 2 + 2]);
			}
		}

		void update(int v, int l, int r, int pos, Segment x) {
			if (l == r) {
				segs[v] = x;
			} else {
				int mid = (l + r) >> 1;
				if (mid >= pos) {
					update(v * 2 + 1, l, mid, pos, x);
				} else {
					update(v * 2 + 2, mid + 1, r, pos, x);
				}
				segs[v] = join(segs[v * 2 + 1], segs[v * 2 + 2]);
			}
		}
	}

	void solveOneTest() {
		int n = in.nextInt();
		int m = in.nextInt();
		int w1 = in.nextInt();
		int aw = in.nextInt();
		int bw = in.nextInt();
		int d1 = in.nextInt();
		int ad = in.nextInt();
		int bd = in.nextInt();
		int s1 = in.nextInt();
		int as = in.nextInt();
		int bs = in.nextInt();
		int[] w = genW(n, w1, aw, bw, m);
		int[] d = genD(d1, ad, bd, m);
		int[] s = genS(s1, as, bs, m);
		int[] left = new int[n];
		int[] right = new int[n];
		int[] stay = new int[n];
		Arrays.fill(stay, 1);
		SegmentTree st = new SegmentTree(n);
		long result = 0;
		for (int i = 0; i < m; i++) {
			int pos = w[i] - 1;
			int npos = Math.max(0, Math.min(n - 1, pos - 1 + d[i]));
			if (npos < pos) {
				left[pos] = (left[pos] + s[i]) % mod;
			}
			if (npos == pos) {
				stay[pos] = (stay[pos] + s[i]) % mod;
			}
			if (npos > pos) {
				right[pos] = (right[pos] + s[i]) % mod;
			}
			st.update(0, 0, n - 1, pos, new Segment(left[pos], stay[pos],
					right[pos]));
			result += st.segs[0].vals[0][0];
		}
		out.println(result % mod);
	}

	void solve() {
		int tc = in.nextInt();
		for (int t = 0; t < tc; t++) {
			System.err.println("test " + t);
			out.print("Case #" + (t + 1) + ": ");
			solveOneTest();
		}
	}

	void run() {
		try {
			in = new FastScanner(new File("C.in"));
			out = new PrintWriter(new File("C.out"));

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
		new C().run();
	}
}