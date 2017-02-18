import java.io.*;
import java.util.*;

public class K {
	FastScanner in;
	PrintWriter out;

	void solve() {
		int tc = in.nextInt();
		for (int t = 0; t < tc; t++) {
			char[] s = in.next().toCharArray();
			int n = s.length;
			ArrayList<Integer>[] cur = new ArrayList[n];
			for (int i = 0; i < n; i++) {
				cur[i] = new ArrayList<Integer>();
			}
			for (char c = 'a'; c <= 'z'; c++) {
				ArrayList<Integer> positions = new ArrayList<Integer>();
				for (int i = 0; i < n; i++) {
					if (s[i] == c) {
						positions.add(i);
					}
				}
				int cnt = positions.size();
				if (cnt == 0) {
					continue;
				}
				int result = cnt - 1;
				if (result != 0) {
					for (int dist = 1;; dist++) {
						if ((cnt - 1) * dist + 1 > n) {
							break;
						}
						for (int i = 0; i < dist; i++) {
							cur[i].clear();
						}
						for (int p : positions) {
							cur[p % dist].add(p);
						}
						for (int i = 0; i < dist; i++) {
							ArrayList<Integer> now = cur[i];
							int it = 0;
							for (int firstIt = 0; firstIt < now.size(); firstIt++) {
								int first = now.get(firstIt);
								while (it != now.size()
										&& now.get(it) - first <= (cnt - 1)
												* dist) {
									it++;
								}
								int max = 1
										+ (first == 0 ? 0 : (first) / dist)
										+ (first == n - 1 ? 0 : (n - first - 1)
												/ dist);
								if (max >= cnt) {
									int already = it - firstIt;
									result = Math.min(result, cnt - already);
								}
							}
						}
					}
				}
				out.print(result + " ");
			}
			out.println();
		}
	}

	void run() {
		try {
			in = new FastScanner(new File("K.in"));
			out = new PrintWriter(new File("K.out"));

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
		new K().runIO();
	}
}