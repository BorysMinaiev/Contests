import java.io.*;
import java.util.*;

public class B {
	FastScanner in;
	PrintWriter out;

	int[] x;
	int[] y;
	int[] val;

	class SegmentTreeMax {
		int[] max;

		SegmentTreeMax(int n) {
			max = new int[n * 4];
			Arrays.fill(max, -1);
		}

		void upd(int v, int l, int r, int pos, int val) {
			if (l == r) {
				max[v] = getMax(max[v], val);
			} else {
				int mid = (l + r) >> 1;
				if (mid >= pos) {
					upd(v * 2 + 1, l, mid, pos, val);
				} else {
					upd(v * 2 + 2, mid + 1, r, pos, val);
				}
				max[v] = getMax(max[v * 2 + 1], max[v * 2 + 2]);
			}
		}

		int get(int v, int l, int r, int needL, int needR) {
			needL =Math.max(needL, l);
			needR = Math.min(needR, r);
			if (needL > needR) {
				return -1;
			}
			if (l == needL && r == needR) {
				return max[v];
			}
			int mid = (l + r) >> 1;
			return getMax(get(v * 2 + 1, l, mid, needL, Math.min(needR, mid)),
					get(v * 2 + 2, mid + 1, r, Math.max(mid + 1, needL), needR));
		}
	}

	int cmp(int id1, int id2) {
		if (x[id1] != x[id2]) {
			return Integer.compare(x[id1], x[id2]);
		}
		return Integer.compare(y[id1], y[id2]);
	}

	int cmp(int id, int xx, int yy) {
		if (x[id] != xx) {
			return Integer.compare(x[id], xx);
		}
		return Integer.compare(y[id], yy);
	}

	int getMax(int id1, int id2) {
		if (id1 == -1) {
			return id2;
		}
		if (id2 == -1) {
			return id1;
		}
		if (val[id1] != val[id2]) {
			return val[id1] > val[id2] ? id1 : id2;
		}
		return id1 < id2 ? id1 : id2;
	}

	class SegmTree {
		int[] points;
		SegmTree left, right;
		int[] byY;
		SegmentTreeMax st;

		SegmTree(int[] points) {
			this.points = points;
			if (points.length != 1) {
				int[] l = new int[points.length / 2];
				int[] r = new int[(points.length + 1) / 2];
				for (int i = 0; i < l.length; i++) {
					l[i] = points[i];
				}
				for (int i = 0; i < r.length; i++) {
					r[i] = points[i + l.length];
				}
				left = new SegmTree(l);
				right = new SegmTree(r);
			}
			byY = new int[points.length];
			for (int i = 0; i < byY.length; i++) {
				byY[i] = y[points[i]];
			}
			Arrays.sort(byY);
			st = new SegmentTreeMax(byY.length);
		}

		int getInside(int y1, int y2) {
			int from = Arrays.binarySearch(byY, y1);
			int to = Arrays.binarySearch(byY, y2);
			if (from < 0) {
				from = -from - 1;
			}
			if (to < 0) {
				to = -to;
			}
			return st.get(0, 0, byY.length - 1, from, to);
		}

		int get(int x1, int y1, int x2, int y2) {
			int c1 = cmp(points[0], x1, y1);
			int c2 = cmp(points[points.length - 1], x2, y2);
			if (c1 >= 0 && c2 <= 0) {
				return getInside(y1, y2);
			}
			int c11 = cmp(points[0], x2, y2);
			int c22 = cmp(points[points.length - 1], x1, y1);
			if (c11 > 0 || c22 < 0) {
				return -1;
			}
			return getMax(left.get(x1, y1, x2, y2), right.get(x1, y1, x2, y2));
		}

		void update(int x, int y, int id) {
			int c1 = cmp(points[0], x, y);
			int c2 = cmp(points[points.length - 1], x, y);
			if (c1 > 0) {
				return;
			}
			if (c2 < 0) {
				return;
			}
			int pos = Arrays.binarySearch(byY, y);
			st.upd(0, 0, byY.length - 1, pos, id);
			if (left != null) {
				left.update(x, y, id);
				right.update(x, y, id);
			}
		}
	}

	void solve() {
		int n = in.nextInt();
		val = new int[n];
		x = new int[n];
		y = new int[n];
		for (int i = 0; i < n; i++) {
			x[i] = in.nextInt();
		}
		for (int i = 0; i < n; i++) {
			y[i] = in.nextInt();
		}
		Integer[] points = new Integer[n];
		for (int i = 0; i < points.length; i++) {
			points[i] = i;
		}
		Arrays.sort(points, new Comparator<Integer>() {

			public int compare(Integer o1, Integer o2) {
				return cmp(o1, o2);
			}
		});
		int sz = 1;
		for (int i = 1; i < n; i++) {
			if (cmp(points[i], points[sz - 1]) != 0) {
				points[sz++] = points[i];
			}
		}
		int[] pts = new int[sz];
		for (int i = 0; i < sz; i++) {
			pts[i] = points[i];
		}
		SegmTree st = new SegmTree(pts);
		int[] prev = new int[n];
		int maxId = 0;
		for (int i = 0; i < n; i++) {
			prev[i] = st.get(x[i], 0, Integer.MAX_VALUE, y[i]);
			val[i] = 1 + (prev[i] == -1 ? 0 : val[prev[i]]);
			st.update(x[i], y[i], i);
			if (val[i] > val[maxId]) {
				maxId = i;
			}
		}
		ArrayList<Integer> res = new ArrayList<Integer>();
		while (maxId != -1) {
			res.add(maxId);
			maxId = prev[maxId];
		}
		Collections.reverse(res);
		out.println(res.size());
		for (int i = 0; i < res.size(); i++) {
			out.print((1 + res.get(i)) + " ");
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
		new B().runIO();
	}
}