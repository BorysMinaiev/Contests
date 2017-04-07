import java.io.*;
import java.util.*;

public class Main {
	FastScanner in;
	PrintWriter out;

	void solve() {
		int n = in.nextInt();
		char[][] a = new char[n][];
		for (int i = 0; i < n; i++) {
			a[i] = in.next().toCharArray();
		}
		boolean full = true;
		boolean empty = true;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (a[i][j] == '.') {
					full = false;
				} else {
					empty = false;
				}
			}
		}
		if (empty) {
			out.println(-1);
			return;
		}
		if (full) {
			out.println(0);
			return;
		}
		int res = Integer.MAX_VALUE;
		for (int r = 0; r < n; r++) {
			boolean ok = false;
			for (int i = 0; i < n; i++) {
				if (a[i][r] == '#') {
					ok = true;
				}
			}
			int cnt = ok ? 0 : 1;
			for (int i = 0; i < n; i++) {
				if (a[r][i] == '.') {
					cnt++;
				}
			}
			for (int c = 0; c < n; c++) {
				boolean ok2 = true;
				for (int i = 0; i < n; i++) {
					if (a[i][c] == '.') {
						ok2 = false;
					}
				}
				if (!ok2) {
					cnt++;
				}
			}
			res = Math.min(res, cnt);

		}
		out.println(res == Integer.MAX_VALUE ? -1 : res);
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
		new Main().runIO();
	}
}