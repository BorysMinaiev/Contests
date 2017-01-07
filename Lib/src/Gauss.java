import java.io.*;
import java.util.*;

public class Gauss {
	FastScanner in;
	PrintWriter out;

	void doGauss(double[][] a) {
		int n = a.length, m = a[0].length;
		int cur = 0;
		for (int i = 0; i + 1 < m; i++) {
			if (cur == n) {
				break;
			}
			int swap = cur;
			for (int j = cur + 1; j < n; j++) {
				if (Math.abs(a[j][i]) > Math.abs(a[swap][j])) {
					swap = j;
				}
			}
			double[] tmp = a[cur];
			a[cur] = a[swap];
			a[swap] = tmp;
			if (Math.abs(a[i][cur]) < 1e-9) {
				continue;
			}
			for (int j = 0; j < n; j++) {
				if (j != cur) {
					double mul = a[j][i] / a[cur][i];
					for (int k = 0; k < m; k++) {
						a[j][k] -= mul * a[cur][k];
					}
				}
			}
			double div = a[cur][i];
			for (int j = 0; j < m; j++) {
				a[cur][j] /= div;
			}
			cur++;
		}
	}

	void solve() {

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
		new Gauss().runIO();
	}
}