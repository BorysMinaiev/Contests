import java.io.*;
import java.util.*;

public class A {
	FastScanner in;
	PrintWriter out;

	class Rectangle {
		int x1, y1, x2, y2;

		public Rectangle(int x1, int y1, int x2, int y2) {
			super();
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}

		public Rectangle() {
			x1 = Integer.MIN_VALUE;
			y1 = Integer.MIN_VALUE;
			x2 = Integer.MAX_VALUE;
			y2 = Integer.MAX_VALUE;
		}

		void read() {
			int _x1 = in.nextInt();
			int _y1 = in.nextInt();
			int _x2 = in.nextInt();
			int _y2 = in.nextInt();
			x1 = Math.min(_x1, _x2);
			x2 = Math.max(_x2, _x1);
			y1 = Math.min(_y1, _y2);
			y2 = Math.max(_y2, _y1);
		}

		long sq() {
			long dx = Math.max(0, x2 - x1);
			long dy = Math.max(0, y2 - y1);
			return dx * dy;
		}
	}

	Rectangle intersection(Rectangle r1, Rectangle r2) {
		int x2 = Math.min(r1.x2, r2.x2);
		int x1 = Math.max(r1.x1, r2.x1);
		int y2 = Math.min(r1.y2, r2.y2);
		int y1 = Math.max(r1.y1, r2.y1);
		return new Rectangle(x1, y1, x2, y2);
	}

	void solve() {
		int n = 3;
		Rectangle[] a = new Rectangle[n];
		for (int i = 0; i < n; i++) {
			a[i] = new Rectangle();
			a[i].read();
		}
		long res = 0;
		for (int mask = 1; mask < 1 << n; mask++) {
			Rectangle r = new Rectangle();
			for (int i = 0; i < n; i++) {
				if (((1 << i) & mask) != 0) {
					r = intersection(r, a[i]);
				}
			}
			res += ((Integer.bitCount(mask) % 2 == 0) ? 1 : -1) * r.sq();
		}
		out.println(Math.abs(res));
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
		new A().runIO();
	}
}