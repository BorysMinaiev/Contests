import java.io.*;
import java.util.*;

public class F {
	FastScanner in;
	PrintWriter out;

	int getAns(ArrayList<Boolean> q) {
		int res = 0;
		boolean was = false;
		for (Boolean z : q) {
			if (!was && z) {
				res++;
			}
			was = z;
		}
		return res;
	}

	int getAns(boolean[][] a) {
		int res = 0;
		for (int i = 0; i < a.length; i++) {
			ArrayList<Boolean> tmp = new ArrayList<Boolean>();
			for (int j = 0; j < a[i].length && i + j < a.length; j++) {
				tmp.add(a[i + j][j]);
			}
			res += getAns(tmp);
		}
		for (int i = 1; i < a[0].length; i++) {
			ArrayList<Boolean> tmp = new ArrayList<Boolean>();
			for (int j = 0; i + j < a[0].length && j < a.length; j++) {
				tmp.add(a[j][i + j]);
			}
			res += getAns(tmp);
		}
		return res;
	}

	void solve() {
		int n = in.nextInt();
		int m = in.nextInt();
		char[][] a = new char[n][];
		for (int i = 0; i < n; i++) {
			a[i] = in.next().toCharArray();
		}
		int res = 0;
		boolean[][] red = new boolean[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				red[i][j] = (a[i][j] == 'R' || a[i][j] == 'M');
			}
		}
		res += getAns(red);
		boolean[][] blue = new boolean[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				blue[i][m - 1 - j] = (a[i][j] == 'B' || a[i][j] == 'M');
			}
		}
		res += getAns(blue);
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
		new F().runIO();
	}
}