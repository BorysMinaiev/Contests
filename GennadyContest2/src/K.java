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
			int[][] cur = new int[n][];
			int[] curSz = new int[n];
			int[] time = new int[n];
			int tt = 0;
			int[] positions = new int[n];
			int[] podvoh = new int[n];
			for (char c = 'a'; c <= 'z'; c++) {
				int cnt = 0;
				for (int i = 0; i < n; i++) {
					if (s[i] == c) {
						positions[cnt++] = i;
					}
				}
				if (cnt == 0) {
					continue;
				}
				int result = cnt - 1;
				if (result != 0) {
					for (int dist = 1;; dist++) {
						if ((cnt - 1) * dist + 1 > n) {
							break;
						}
						++tt;
						int podvohSz = 0;
						
						for (int i = 0; i <cnt; i++) {
							int p = positions[i];
							int mod = p % dist;
							if (time[mod] != tt) {
								time[mod] = tt;
								curSz[mod] = 0;
								podvoh[podvohSz++] = mod;
							}
							curSz[mod]++;
						}
						tt++;
						for (int i = 0; i <cnt; i++) {
							int p = positions[i];
							int mod = p % dist;
							if (time[mod] != tt) {
								time[mod] = tt;
								cur[mod] = new int[curSz[mod]];
								curSz[mod] = 0;
							}
							cur[mod][curSz[mod]++] = p;
						}
						for (int itt = 0; itt < podvohSz; itt++) {
							int i = podvoh[itt];
							int[] now = cur[i];
							if (time[i] != tt) {
								continue;
							}
							int it = 0;
							for (int firstIt = 0; firstIt < curSz[i]; firstIt++) {
								int first = now[firstIt];
								while (it != curSz[i]
										&& now[it] - first <= (cnt - 1)
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