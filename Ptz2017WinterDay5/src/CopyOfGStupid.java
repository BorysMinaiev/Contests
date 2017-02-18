import java.io.*;
import java.util.*;

public class CopyOfGStupid {
	FastScanner in;
	PrintWriter out;

	void check() {
//		if (p[p.length - 1] != 0) {
//			return;
//		}
		int ans = 0;
		int[] p = this.p.clone();
		while (p[0] != 0) {
			int x = p[0];
			for (int i = 0; i < x; i++) {
				p[i] = p[i + 1];
			}
			p[x] = x;
			ans++;
		}
		out.println(Arrays.toString(this.p) + " " + ans + " " + ((1 << (p.length - 1)) - 1 - ans));
	}
	
	void go(int pos) {
		if (pos == p.length) {
			check();
			return;
		}
		for (int i = 0; i < p.length; i++) {
			if (!used[i]) {
				p[pos] = i;
				used[i] = true;
				go(pos + 1);
				used[i] = false;
			}
		}
	}

	int[] p;
	boolean[] used;
	
	void solve() {
		int n = in.nextInt();
		p = new int[n];
		used = new boolean[n];
		go(0);
	}

	void run() {

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
		new CopyOfGStupid().run();
	}
}