import java.io.*;
import java.util.*;

public class D {
	FastScanner in;
	PrintWriter out;

	int[] groupId, left, right;
	int groups;
	final int[] a = new int[1 << 20];
	long[] groupSum;
	Integer[] sorted;
	int[] groupMin;
	int[] groupIter;

	void updateGroup(int grId) {
		for (int i = left[grId]; i < right[grId]; i++) {
			a[i] = Math.min(a[i], groupMin[grId]);
		}
		Arrays.sort(sorted, left[grId], right[grId], cmp);
		groupSum[grId] = 0;
		for (int i = left[grId]; i < right[grId]; i++) {
			groupSum[grId] += a[i];
		}
		groupIter[grId] = right[grId];
	}

	int getGroupMax(int grId) {
		return Math.min(groupMin[grId], a[sorted[right[grId] - 1]]);
	}

	long getGroupSum(int grId) {
		return groupSum[grId];
	}

	void setGroupMin(int grId, int nextMin) {
		if (nextMin > groupMin[grId]) {
			return;
		}
		if (groupIter[grId] != right[grId]) {
			groupSum[grId] -= (right[grId] - groupIter[grId]) * 1L
					* (groupMin[grId] - nextMin);
		}
		while (groupIter[grId] > left[grId]
				&& a[sorted[groupIter[grId] - 1]] > nextMin) {
			groupSum[grId] -= a[sorted[groupIter[grId] - 1]] - nextMin;
			groupIter[grId]--;
		}
		groupMin[grId] = nextMin;
	}

	Comparator<Integer> cmp = new Comparator<Integer>() {

		public int compare(Integer o1, Integer o2) {
			return Integer.compare(a[o1], a[o2]);
		}
	};

	void solve() {
		final int SQRT = 300;
		int tc = in.nextInt();
		for (int t = 0; t < tc; t++) {
			int n = in.nextInt();
			int m = in.nextInt();
			for (int i = 0; i < n; i++) {
				a[i] = in.nextInt();
			}
			groupId = new int[n];
			left = new int[n];
			Arrays.fill(left, Integer.MAX_VALUE);
			right = new int[n];
			groups = 0;
			for (int i = 0; i < n; i++) {
				groupId[i] = i / SQRT;
				left[groupId[i]] = Math.min(left[groupId[i]], i);
				right[groupId[i]] = i + 1;
				groups = Math.max(groups, groupId[i] + 1);
			}
			groupSum = new long[groups];
			groupMin = new int[groups];
			groupIter = new int[groups];
			Arrays.fill(groupMin, Integer.MAX_VALUE);
			sorted = new Integer[n];
			for (int i = 0; i < n; i++) {
				sorted[i] = i;
			}
			for (int grId = 0; grId < groups; grId++) {
				updateGroup(grId);
			}
			for (int i = 0; i < m; i++) {
				char type = in.next().charAt(0);
				int from = in.nextInt() - 1;
				int to = in.nextInt() - 1;
				if (type == 'o') {
					// set min
					int val = in.nextInt();
					for (int pos = from; pos <= to
							&& groupId[pos] == groupId[from]; pos++) {
						a[pos] = Math.min(a[pos], val);
					}
					updateGroup(groupId[from]);
					for (int grId = groupId[from] + 1; grId < groupId[to]; grId++) {
						setGroupMin(grId, val);
					}
					if (groupId[from] != groupId[to]) {
						for (int pos = to; pos >= from
								&& groupId[pos] == groupId[to]; pos--) {
							a[pos] = Math.min(a[pos], val);
						}
						updateGroup(groupId[to]);
					}
				} else {
					if (type == 'm') {
						// find max
						int res = 0;
						for (int pos = from; pos <= to
								&& groupId[pos] == groupId[from]; pos++) {
							res = Math.max(res, a[pos]);
						}
						res = Math.min(res, groupMin[groupId[from]]);
						for (int grId = groupId[from] + 1; grId < groupId[to]; grId++) {
							res = Math.max(res, getGroupMax(grId));
						}
						if (groupId[from] != groupId[to]) {
							int res2 = 0;
							for (int pos = to; pos >= from
									&& groupId[pos] == groupId[to]; pos--) {
								res2 = Math.max(res2, a[pos]);
							}
							res2 = Math.min(res2, groupMin[groupId[to]]);
							res = Math.max(res, res2);
						}
						out.println(res);
					} else {
						// find sum
						long res = 0;
						for (int pos = from; pos <= to
								&& groupId[pos] == groupId[from]; pos++) {
							res += Math.min(groupMin[groupId[from]], a[pos]);
						}
						for (int grId = groupId[from] + 1; grId < groupId[to]; grId++) {
							res += getGroupSum(grId);
						}
						if (groupId[from] != groupId[to]) {
							for (int pos = to; pos >= from
									&& groupId[pos] == groupId[to]; pos--) {
								res += Math.min(groupMin[groupId[to]], a[pos]);
							}
						}
						out.println(res);
					}
				}
			}
		}
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
		new D().runIO();
	}
}