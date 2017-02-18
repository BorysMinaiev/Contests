import java.io.*;
import java.util.*;

public class A {
	FastScanner in;
	PrintWriter out;

	class O {
		int need;
		int add;

		public O(int need, int add) {
			super();
			this.need = need;
			this.add = add;
		}

	}

	void solve() {
		int n = in.nextInt();
		int[] v = new int[n];
		int[] op = new int[n];
		int[] me = new int[n];
		int[] und = new int[n];
		int cntV = 0;
		int alr = 0;
		ArrayList<O> a = new ArrayList<A.O>();
		for (int i = 0; i < n; i++) {
			v[i] = in.nextInt();
			op[i] = in.nextInt();
			me[i] = in.nextInt();
			und[i] = in.nextInt();
			cntV += v[i];
			int tot = op[i] + me[i] + und[i];
			if (op[i] * 2 > tot) {
				alr += v[i];
				continue;
			}
			if ((op[i] + und[i]) * 2 <= tot) {
				continue;
			}
			a.add(new O((tot + 2) / 2 - op[i], v[i]));
		}
		int[] f = new int[cntV + 1];
		Arrays.fill(f, Integer.MAX_VALUE);
		f[alr] = 0;
		for (O o : a) {
			for (int x = cntV; x >= o.add; x--) {
				if (f[x - o.add] != Integer.MAX_VALUE) {
					f[x] = Math.min(f[x], f[x - o.add] + o.need);
				}
			}
		}
		int res = Integer.MAX_VALUE;
		for (int z = (cntV + 2) / 2; z <= cntV; z++) {
			res = Math.min(res, f[z]);
		}
		if (res == Integer.MAX_VALUE) {
			out.println("impossible");
			return;
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
		new A().runIO();
	}
}