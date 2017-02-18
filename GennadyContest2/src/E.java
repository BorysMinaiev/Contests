import java.io.*;
import java.util.*;

public class E {
	FastScanner in;
	PrintWriter out;

	class O implements Comparable<O> {
		int id;
		int r, p, fr, to;

		public O(int id, int r, int p, int fr, int to) {
			super();
			this.id = id;
			this.r = r;
			this.p = p;
			this.fr = fr;
			this.to = to;
		}

		public int compareTo(O o) {
			return Integer.compare(r, o.r);
		}

		@Override
		public String toString() {
			return "O [id=" + id + ", r=" + r + ", p=" + p + ", fr=" + fr
					+ ", to=" + to + "]";
		}

	}

	class SegmentTree {
		long[] allMax;
		long[] curMax;
		long[] add;
		long[] maxAdd;

		int n;

		SegmentTree(int n) {
			this.n = n;
			allMax = new long[n * 4];
			curMax = new long[n * 4];
			add = new long[n * 4];
			maxAdd = new long[n * 4];
		}

		void print() {
			System.err.println("!");
			System.err.println(Arrays.toString(allMax));
			System.err.println(Arrays.toString(curMax));
			System.err.println(Arrays.toString(add));
			System.err.println(Arrays.toString(maxAdd));

		}

		void relaxGet(int v, long currentAdd, long maxAddd) {
			maxAdd[v] = Math.max(maxAdd[v], add[v] + maxAddd);
			add[v] += currentAdd;
			allMax[v] = Math.max(allMax[v], curMax[v] + maxAddd);
			curMax[v] += currentAdd;
		}

		void relax(int v) {
			relaxGet(v * 2 + 1, add[v], maxAdd[v]);
			relaxGet(v * 2 + 2, add[v], maxAdd[v]);
			add[v] = 0;
			maxAdd[v] = 0;
		}

		void join(int v) {
			allMax[v] = Math.max(allMax[v * 2 + 1], allMax[v * 2 + 2]);
			curMax[v] = Math.max(curMax[v * 2 + 1], curMax[v * 2 + 2]);
		}

		long getMax(int v, int l, int r, int needL, int needR) {
			if (needL > needR) {
				return Long.MIN_VALUE / 2;
			}
			if (needL == l && needR == r) {
				return allMax[v];
			}
			int m = (l + r) >> 1;
			relax(v);
			long left = (getMax(v * 2 + 1, l, m, needL, Math.min(m, needR)));
			long right = getMax(v * 2 + 2, m + 1, r, Math.max(m + 1, needL),
					needR);
//			System.err.println(left + " " + right);
			return Math.max(left, right);

		}

		void add(int v, int l, int r, int needL, int needR, int val) {
			if (needL > needR) {
				return;
			}
			if (l == needL && r == needR) {
				relaxGet(v, val, val);
				return;
			}
			relax(v);
			int m = (l + r) >> 1;
			add(v * 2 + 1, l, m, needL, Math.min(m, needR), val);
			add(v * 2 + 2, m + 1, r, Math.max(m + 1, needL), needR, val);
			join(v);
		}
	}

	void solve() {
		int n = in.nextInt();
		O[] a = new O[n];
		for (int i = 0; i < n; i++) {
			a[i] = new O(i, in.nextInt(), in.nextInt(), in.nextInt() - 1,
					in.nextInt() - 1);
		}
		int max = 0;
		for (int i = 0; i < n; i++) {
			max = Math.max(max, a[i].to);
		}
		SegmentTree st = new SegmentTree(max + 1);
		Arrays.sort(a);
		long[] res = new long[n];
//		System.err.println("max = " + max);
		for (int i = 0; i < n;) {
			int j = i;
			while (j != n && a[j].r == a[i].r) {
				j++;
			}
//			System.err.println(i);
			for (int k = i; k < j; k++) {
				res[a[k].id] = a[k].p + st.getMax(0, 0, max, a[k].fr, a[k].to);
			}
			for (int k = i; k < j; k++) {
				st.add(0, 0, max, a[k].fr, a[k].to, a[k].p);
			}
			i = j;
//			st.print();
		}
		for (int i = 0; i < n; i++) {
			out.println(res[i]);
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
		new E().runIO();
	}
}