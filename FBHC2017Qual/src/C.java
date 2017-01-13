import java.io.*;
import java.util.*;

public class C {
	FastScanner in;
	PrintWriter out;

	void solveOneTest() {
		int h = in.nextInt();
		int s = in.nextInt();
		double res = 0;
		for (int i = 0; i < s; i++) {
			String cmd = in.next();
			int nh = h;
			int pos = cmd.indexOf('+');
			if (pos == -1) {
				pos = cmd.indexOf('-');
				if (pos != -1) {
					nh += Integer.parseInt(cmd.substring(pos + 1));
				}
			} else {
				if (pos != -1) {
					nh -= Integer.parseInt(cmd.substring(pos + 1));
				}
			}
			if (pos != -1) {
				cmd = cmd.substring(0, pos);
			}
			pos = cmd.indexOf('d');
			int cnt = Integer.parseInt(cmd.substring(0, pos));
			int sz = Integer.parseInt(cmd.substring(pos + 1));
			double[] prob = new double[] { 1 };
			for (int it = 0; it < cnt; it++) {
				double[] next = new double[prob.length + sz + 1];
				for (int j = 0; j < prob.length; j++) {
					for (int k = 0; k < sz; k++) {
						next[j + k + 1] += prob[j] / sz;
					}
				}
				prob = next;
			}
			double curRes = 0;
			for (int j = 0; j < prob.length; j++) {
				if (j >= nh) {
					curRes += prob[j];
				}
			}
			res = Math.max(res, curRes);
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