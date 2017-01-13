import java.io.*;
import java.util.*;

public class B {
	FastScanner in;
	PrintWriter out;

	void solve() {
		char[] s = in.next().toCharArray();
		int[][] next = new int[26][26];
		for (int i = 0; i + 1 < s.length; i++) {
			next[s[i] - 'a'][s[i + 1] - 'a']++;
		}
		int[] cnt = new int[26];
		for (int i = 0; i < s.length; i++) {
			cnt[s[i] - 'a']++;
		}
		int maxCnt = 0;
		for (int i = 0; i < 26; i++) {
			maxCnt = Math.max(maxCnt, cnt[i]);
		}
		int maxId = -1, maxLen = -1;
		for (int i = 0; i < 26; i++) {
			if (cnt[i] != maxCnt) {
				continue;
			}
			int len = 1;
			int cur = i;
			while (true) {
				int ne = -1;
				for (int j = 0; j < 26; j++) {
					if (next[cur][j] == maxCnt) {
						ne = j;
					}
				}
				if (ne == -1) {
					break;
				}
				len++;
				cur = ne;
			}
			if (len > maxLen) {
				maxLen = len;
				maxId = i;
			}
		}
		int cur = maxId;
		while (true) {
			out.print((char) ('a' + cur));
			int ne = -1;
			for (int j = 0; j < 26; j++) {
				if (next[cur][j] == maxCnt) {
					ne = j;
				}
			}
			if (ne == -1) {
				break;
			}
			cur = ne;
		}
		out.println();
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