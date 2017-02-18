import java.io.*;
import java.util.*;

public class E {
	FastScanner in;
	PrintWriter out;

	class SegmentTree {
		int[] vals;
		int[] min;

		public SegmentTree(int n, int[] vals) {
			this.vals = vals;
			min = new int[n * 4];
			build(0, 0, n - 1);
		}

		int getMin(int x, int y) {
			if (x == -1) {
				return y;
			}
			if (y == -1) {
				return x;
			}
			return vals[x] < vals[y] ? x : y;
		}

		void build(int v, int l, int r) {
			if (l == r) {
				min[v] = l;
			} else {
				int m = (l + r) >> 1;
				build(v * 2 + 1, l, m);
				build(v * 2 + 2, m + 1, r);
				min[v] = getMin(min[v * 2 + 1], min[v * 2 + 2]);
			}
		}

		int get(int v, int l, int r, int needL, int needR) {
			if (needL > needR) {
				return -1;
			}
			if (needL == l && needR == r) {
				return min[v];
			}
			int m = (l + r) >> 1;
			return getMin(get(v * 2 + 1, l, m, needL, Math.min(m, needR)),
					get(v * 2 + 2, m + 1, r, Math.max(needL, m + 1), needR));
		}

		void update(int v, int l, int r, int pos, int newVal) {
			if (l == r) {
				vals[l] = newVal;
			} else {
				int m = (l + r) >> 1;
				if (m >= pos) {
					update(v * 2 + 1, l, m, pos, newVal);
				} else {
					update(v * 2 + 2, m + 1, r, pos, newVal);
				}
				min[v] = getMin(min[v * 2 + 1], min[v * 2 + 2]);
			}
		}

	}

	ArrayList<Integer>[] g;

	int[] a;
	int[] id;
	int[] vals;
	int[] right;
	int time;

	void go(int v) {
		id[time] = v;
		vals[time] = a[v];
		time++;
		for (int i = 0; i < g[v].size(); i++) {
			go(g[v].get(i));
		}
		right[v] = time - 1;
	}

	SegmentTree st;
	int n;

	void rec(int l, int r) {
		while (true) {
//			System.err.println("rec " + l + " "+ r);
			int minId = st.get(0, 0, n - 1, l, r);
//			System.err.println("minId = " + minId);
			if (vals[minId] == Integer.MAX_VALUE) {
				break;
			}
			int ri = right[id[minId]];
			if (ri != minId) {
				rec(minId + 1, ri);
			}
			st.update(0, 0, n - 1, minId, Integer.MAX_VALUE);
			out.print((1 + id[minId]) + " ");
		}
	}

	void solve() {
		n = in.nextInt();
		g = new ArrayList[n];
		for (int i = 0; i < n; i++) {
			g[i] = new ArrayList<Integer>();
		}
		id = new int[n];
		for (int i = 1; i < n; i++) {
			int pi = in.nextInt() - 1;
			g[pi].add(i);
		}
//		System.err.println(Arrays.toString(g));
		a = new int[n];
		for (int i = 0; i < n; i++) {
			a[i] = in.nextInt();
		}
		vals = new int[n];
		right = new int[n];
		go(0);
//		System.err.println(Arrays.toString(vals));
		st = new SegmentTree(n, vals);
		rec(0, n - 1);
	}

	void run() {
		try {
			in = new FastScanner(new File("E.in"));
			out = new PrintWriter(new File("E.out"));

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