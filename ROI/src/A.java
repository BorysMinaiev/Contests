import java.io.*;
import java.util.*;

public class A {
	FastScanner in;
	PrintWriter out;

	boolean ok(int l, int r) {
		out.println("? " + (l + 1) + " " + (r + 1));
		out.flush();
		return in.next().equals("Yes");
	}

	void solve() {
		int n = in.nextInt();
		char[] res = new char[n];
		int cur = 0;
		ArrayList<Integer> open = new ArrayList();
		while (cur != n) {
			res[cur] = '(';
			open.add(cur);
			for (int next = cur + 1;; next++) {
				if (ok(open.get(open.size() - 1), next)) {
					res[next] = ')';
					open.remove(open.size() - 1);
					if (open.size() == 0) {
						cur = next + 1;
						break;
					}
				} else {
					res[next] = '(';
					open.add(next);
				}
			}
		}
		out.println("! " + new String(res));
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