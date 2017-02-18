import java.io.*;
import java.util.*;

public class B {
	FastScanner in;
	PrintWriter out;

	void solve() {
		int n = in.nextInt();
		int m = in.nextInt();
		boolean[] opened = new boolean[n];
		boolean[] closed = new boolean[n];
		int totalClosed = 0;
		int alr = 0;
		String[] cmd = new String[n];
		for (int i = 0; i < m; i++) {
			int id1 = in.nextInt() - 1;
			int id2 = in.nextInt() - 1;
			String s1 = in.next();
			String s2 = in.next();
			cmd[id1] = s1;
			cmd[id2] = s2;
			opened[id1] = opened[id2] = true;
			if (s1.equals(s2)) {
				totalClosed++;
				alr++;
				closed[id1] = closed[id2] = true;
			}
		}
		int res = 0;
		for (int i = 0; i < n; i++) {
			if (opened[i] && !closed[i]) {
				boolean know = false;
				for (int j = i + 1; j < n; j++) {
					if (opened[j] && cmd[i].equals(cmd[j])) {
						know = true;
						closed[i] = true;
						closed[j] = true;
					}
				}
				if (know) {
					res++;
					totalClosed++;
				}
			}
		}
//		System.err.println(Arrays.toString(opened));
//		System.err.println(Arrays.toString(closed));
//		System.err.println(res);
		if (totalClosed == n / 2 - 1) {
			res++;
		} else {
			for (int i = 0; i < n; i++) {
				if (opened[i] && !closed[i]) {
					totalClosed++;
				}
			}
			if (totalClosed == n / 2) {
				res = n / 2 - alr;
			}
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
		new B().runIO();
	}
}