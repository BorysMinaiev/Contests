import java.io.*;
import java.util.*;

public class A {
	FastScanner in;
	PrintWriter out;

	class O implements Comparable<O> {
		int id, value;

		public O(int id, int value) {
			super();
			this.id = id;
			this.value = value;
		}

		public int compareTo(O o) {
			return Integer.compare(value, o.value);
		}

	}

	void solveOneTest() {
		int n = in.nextInt();
		int[] p = new int[n];
		for (int i = 0; i < n; i++) {
			p[i] = in.nextInt() - 1;
		}
		O[] ids = new O[n];
		for (int i = 0; i < n; i++) {
			ids[i] = new O(i, p[i]);
		}
		Arrays.sort(ids);
		char curC = 'A';
		char[] res = new char[n];
		res[ids[0].id] = curC;
		for (int i = 0; i + 1 < n; i++) {
			int pos1 = ids[i].id;
			int pos2 = ids[i + 1].id;
			int val1 = (pos1 == n - 1 ? -1 : p[pos1 + 1]);
			int val2 = (pos2 == n - 1 ? -1 : p[pos2 + 1]);
			if (val1 > val2) {
				curC++;
			}
			if (curC > 'Z') {
				out.println(-1);
				return;
			}
			res[ids[i + 1].id] = curC;
		}
		String ans = new String(res);
		out.println(ans);
		Sub[] allS = new Sub[n];
		for (int i = 0; i < n; i++) {
			allS[i] = new Sub(ans.substring(i), i);
		}
		Arrays.sort(allS);
		int[] p2 = new int[n];
		for (int i = 0; i < n; i++) {
			p2[allS[i].id] = i;
		}
		for (int i = 0; i < n; i++) {
			if (p[i] != p2[i]) {
				throw new AssertionError();
			}
		}
	}

	class Sub implements Comparable<Sub> {
		String s;
		int id;

		public Sub(String s, int id) {
			super();
			this.s = s;
			this.id = id;
		}

		public int compareTo(Sub o) {
			return s.compareTo(o.s);
		}

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