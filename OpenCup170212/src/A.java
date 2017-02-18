import java.util.*;
import java.io.*;

public class A {

	final int MAX = 5_000_001;

	byte[] n = new byte[MAX];
	byte[] merged = new byte[MAX];
	int[] pref = new int[MAX];
	int[][] next = new int[10][MAX];

	int len;

	Random rnd = new Random(123);

	void solve() throws IOException {
		while (true) {
			String s = Math.abs(rnd.nextInt(10000)) + "";
			System.err.println(s);
			if (s.length() < 4) {
				continue;
			}
			int len = s.length();
			for (int i = 0; i < len; i++) {
				n[i] = (byte) (s.charAt(i) - '0');
			}
			int lenMerged = 0;
			for (int i = 0; i < len; i++) {
				if (i == 0 || n[i] != n[i - 1]) {
					merged[lenMerged++] = n[i];
				}
			}

			for (int i = 1; i < lenMerged; i++) {
				updatePrefix(i);
			}
			for (int i = 0; i < lenMerged; i++) {
				updateNext(i);
			}

			int foundLcp = -1;
			int foundDigit = -1;
			int foundPos = -1;
			int curPos = lenMerged - 1;
			for (int lcp = len - 1; lcp >= 0; lcp--) {
				if (lcp == 0 || n[lcp] != n[lcp - 1]) {
					curPos--;
				}
				int startDigit = 0;
				if (lcp == 0) {
					startDigit = 1;
				}
				int found = -1;
				for (int digit = n[lcp] - 1; digit >= startDigit; digit--) {
					boolean ok = false;

					int cur;
					int curLen = curPos + 1;
					if (curPos < 0 || digit != merged[curPos]) {
						if (curPos >= 0) {
							cur = next[digit][curPos];
						} else {
							cur = 0;
						}
						cur = next[digit][curPos];
						curLen++;
					} else {
						cur = pref[curPos];
					}
					int minAppend = getMinAppend(cur, curLen);
					if (curLen == 1) {
						if (len - lcp - 1 >= 3) {
							ok = true;
						}
					} else {
						if (len - lcp - 1 >= minAppend) {
							ok = true;
						}
					}
					if (ok) {
						found = digit;
						foundPos = curPos;
						break;
					}
				}
				if (found != -1) {
					foundLcp = lcp;
					foundDigit = found;
					break;
				}
			}
			// System.err.println(foundLcp + " " + foundDigit + " " + foundPos);
			if (foundLcp != -1) {
				if (foundLcp == 0 || foundDigit != merged[foundPos]) {
					foundPos++;
					merged[foundPos] = (byte) foundDigit;
					updatePrefix(foundPos);
					updateNext(foundPos);
				}
				n[foundLcp] = (byte) foundDigit;

				for (int lcp = foundLcp + 1; lcp < len; lcp++) {
					boolean ass = false;
					for (int digit = 9; digit >= 0; digit--) {
						boolean ok = false;

						int cur;
						int curLen = foundPos + 1;
						if (digit != merged[foundPos]) {
							cur = next[digit][foundPos];
							curLen++;
						} else {
							cur = pref[foundPos];
						}
						int minAppend = getMinAppend(cur, curLen);
						if (curLen == 1) {
							if (len - lcp - 1 >= 3) {
								ok = true;
							}
						} else {
							if (len - lcp - 1 >= minAppend) {
								ok = true;
							}
						}
						if (ok) {
							ass = true;
							if (digit != merged[foundPos]) {
								foundPos++;
								merged[foundPos] = (byte) digit;
								updatePrefix(foundPos);
								updateNext(foundPos);
							}
							n[lcp] = (byte) digit;
							break;
						}
					}
					if (!ass) {
						System.err.println(lcp);
						throw new AssertionError();
					}
				}

				StringBuilder sb = new StringBuilder(len);
				for (int i = 0; i < len; i++) {
					sb.append(n[i]);
				}
				out.println(sb.toString());
				continue;
			}

			if (len >= 5) {
				StringBuilder sb = new StringBuilder(len - 1);
				for (int i = 0; i < len - 4; i++) {
					sb.append('9');
				}
				sb.append("898");
				out.println(sb.toString());
			} else {
				out.println(-1);
			}
		}
	}

	private void updateNext(int i) {
		if (i == 0) {
			for (int c = 0; c < 10; c++) {
				next[c][i] = 0;
			}
			next[merged[0]][0] = 1;
			return;
		}
		for (int c = 0; c < 10; c++) {
			int k = pref[i];
			if (c == merged[k]) {
				next[c][i] = k + 1;
			} else {
				if (k == 0) {
					next[c][i] = 0;
				} else {
					next[c][i] = next[c][k - 1];
				}
			}
		}
	}

	private void updatePrefix(int i) {
		if (i == 0) {
			return;
		}
		int k = pref[i - 1];
		while (k > 0 && merged[k] != merged[i]) {
			k = pref[k - 1];
		}
		if (merged[k] == merged[i]) {
			k++;
		}
		pref[i] = k;
	}

	private int getMinAppend(int border, int len) {
		if (border * 2 <= len) {
			return len - border * 2;
		}
		int period = len - border;
		int count = len / period;
		if (count * period != len) {
			count++;
		}
		return ((count + 1) >> 1) * 2 * period - len;
	}

	FastScanner in;
	PrintWriter out;

	void run() {
		in = new FastScanner();
		out = new PrintWriter(System.out);
		try {
			solve();
		} catch (IOException e) {
		}
		out.close();
	}

	class FastScanner {
		BufferedReader br;
		StringTokenizer st;

		public FastScanner() {
			br = new BufferedReader(new InputStreamReader(System.in));
		}

		boolean hasMoreTokens() {
			while (st == null || !st.hasMoreTokens()) {
				try {
					String line = br.readLine();
					if (line == null) {
						return false;
					}
					st = new StringTokenizer(line);
				} catch (IOException e) {
					return false;
				}
			}
			return true;
		}

		public FastScanner(String s) {
			try {
				br = new BufferedReader(new FileReader(s));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public String nextToken() {
			while (st == null || !st.hasMoreTokens()) {
				try {
					st = new StringTokenizer(br.readLine());
				} catch (IOException e) {
				}
			}
			return st.nextToken();
		}

		public int nextInt() {
			return Integer.parseInt(nextToken());
		}

		public long nextLong() {
			return Long.parseLong(nextToken());
		}

		public double nextDouble() {
			return Double.parseDouble(nextToken());
		}
	}

	public static void main(String[] args) {
		new A().run();
	}
}
