import java.io.*;
import java.util.*;

public class A {
	FastScanner in;
	PrintWriter out;

	boolean isBlack(int p, int x, int y) {
		if (p == 0) {
			return false;
		}
		x -= 50;
		y -= 50;
		if (x == 0 && y == 0) {
			return true;
		}
		double r = Math.sqrt(x * x + y * y);
		if (r > 50) {
			return false;
		}
		if (p == 100) {
			return true;
		}
		double angle = -Math.atan2(y, x) + Math.PI / 2;
		while (angle < 0) {
			angle += Math.PI * 2;
		}
		while (angle > Math.PI * 2) {
			angle -= Math.PI * 2;
		}
		return (angle / Math.PI <= p / 50.);
	}

	void solveOneTest() {
		int p = in.nextInt();
		int x = in.nextInt();
		int y = in.nextInt();
		out.println(isBlack(p, x, y) ? "black" : "white");
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
			in = new FastScanner(new File("A.in"));
			out = new PrintWriter(new File("A.out"));

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
		new A().run();
	}
}