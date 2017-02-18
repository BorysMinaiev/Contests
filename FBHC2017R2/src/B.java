import java.io.*;
import java.util.*;

public class B {
	FastScanner in;
	PrintWriter out;

	int[] gen(int x1, int ax, int bx, int cx, int n) {
		int[] res = new int[n];
		res[0] = x1;
		for (int i = 1; i < n; i++) {
			res[i] = (int) (1 + ((ax * 1L * res[i - 1] + bx) % cx));
		}
		return res;
	}

	class Fenwick {
		int[] sum;

		int n;

		Fenwick(int n) {
			this.n = n;
			sum = new int[n];
		}

		int get(int x) {
			int res = 0;
			for (; x >= 0; x = (x & (x + 1)) - 1) {
				res += sum[x];
			}
			return res;
		}

		void add(int x, int v) {
			for (; x < n; x |= x + 1) {
				sum[x] += v;
			}
		}

		int firstLeft(int x) {
			int left = -1, right = x;
			int s = get(x);
			if (s == 0) {
				return -1;
			}
			while (left + 1 < right) {
				int mid = (left + right) >> 1;
				if (get(mid) == s) {
					right = mid;
				} else {
					left = mid;
				}
			}
			return right;
		}

		int firstRight(int x) {
			int s = get(x);
			int left = x;
			int right = n;
			while (right - left > 1) {
				int mid = (left + right) >> 1;
				if (get(mid) > s) {
					right = mid;
				} else {
					left = mid;
				}
			}
			return right;
		}
	}

	double getSLeft(double x1, double x2, double h1, double h2) {
		if (x2 - x1 > h1 + h2) {
			return h1 * h1 / 2;
		}
		double x = x2 - x1;
		double dx = (h1 - h2 + x) / 2.;
		double hMid = h1 - dx;
		return dx * (h1 + hMid) / 2;
	}

	double cur;

	void remove(int pos) {
		f.add(pos, -1);
		cur -= addLeft[pos];
		cur -= addRight[pos];
		ansH[pos] = 0;
	}

	double getSRight(double x1, double x2, double h1, double h2) {
		if (x2 - x1 > h1 + h2) {
			return h2 * h2 / 2;
		}
		double x = x2 - x1;
		double dx = (h1 - h2 + x) / 2.;
		double hMid = h1 - dx;
		return (x - dx) * (h2 + hMid) / 2;
	}

	double[] addLeft, addRight;
	int[] ansH;
	Fenwick f;

	void solveOneTest() {
		int n = in.nextInt();
		int x1 = in.nextInt();
		int ax = in.nextInt();
		int bx = in.nextInt();
		int cx = in.nextInt();
		int h1 = in.nextInt();
		int ah = in.nextInt();
		int bh = in.nextInt();
		int ch = in.nextInt();
		int[] a = gen(x1, ax, bx, cx, n);
		int[] h = gen(h1, ah, bh, ch, n);
		int[] allX = (int[]) a.clone();
		Arrays.sort(allX);
		for (int i = 0; i + 1 < n; i++) {
			if (allX[i] == allX[i + 1]) {
				throw new AssertionError();
			}
		}
		f = new Fenwick(n);
		ansH = new int[n];
		double res = 0;
		cur = 0;
		addLeft = new double[n];
		addRight = new double[n];
		for (int i = 0; i < n; i++) {
			int pos = Arrays.binarySearch(allX, a[i]);
			int left = f.firstLeft(pos);
			while (left != -1 && h[i] - (allX[pos] - allX[left]) >= ansH[left]) {
				remove(left);
				left = f.firstLeft(pos);
			}
			if (left != -1 && ansH[left] - (allX[pos] - allX[left]) >= h[i]) {
				res += cur;
				continue;
			}
			int right = f.firstRight(pos);
			while (right != n
					&& h[i] - (allX[right] - allX[pos]) >= ansH[right]) {
				remove(right);
				right = f.firstRight(pos);
			}
			if (right != n && ansH[right] - (allX[right] - allX[pos]) >= h[i]) {
				res += cur;
				continue;
			}
			if (left != -1) {
				cur -= addRight[left];
			}
			if (right != n) {
				cur -= addLeft[right];
			}
			if (left == -1) {
				addLeft[pos] = h[i] * 0.5 * h[i];
			} else {
				addLeft[pos] = getSRight(allX[left], allX[pos], ansH[left],
						h[i]);
				addRight[left] = getSLeft(allX[left], allX[pos], ansH[left],
						h[i]);
			}
			if (right == n) {
				addRight[pos] = h[i] * 0.5 * h[i];
			} else {
				addRight[pos] = getSLeft(allX[pos], allX[right], h[i],
						ansH[right]);
				addLeft[right] = getSRight(allX[pos], allX[right], h[i],
						ansH[right]);
			}
			if (left != -1) {
				cur += addRight[left];
			}
			if (right != n) {
				cur += addLeft[right];
			}
			cur += addLeft[pos];
			cur += addRight[pos];
			ansH[pos] = h[i];
			f.add(pos, 1);
			res += cur;
		}
		out.printf(Locale.US, "%.10f\n", res);
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
			in = new FastScanner(new File("B.in"));
			out = new PrintWriter(new File("B.out"));

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
		new B().run();
	}
}