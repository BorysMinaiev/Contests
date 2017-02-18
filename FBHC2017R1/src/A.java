import java.io.*;
import java.util.*;

public class A {
	FastScanner in;
	PrintWriter out;

	void solveOneTest() {
		int n = in.nextInt();
		int m = in.nextInt();
		PriorityQueue<Integer> pq = new PriorityQueue();
		long res = 0;
		for (int i = 0; i < n; i++) {
			int[] a = new int[m];
			for (int j = 0; j < m; j++) {
				a[j] = in.nextInt();
			}
			Arrays.sort(a);
			for (int j = 0; j < m; j++) {
				a[j] += ((j + 1) * (j + 1) - j * j);
				pq.add(a[j]);
			}
			res += pq.poll();
		}
		out.println(res);
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