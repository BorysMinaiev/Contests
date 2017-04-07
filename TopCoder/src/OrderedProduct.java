import java.util.Arrays;

public class OrderedProduct {
	final static int mod = (int) 1e9 + 7;

	static int[][] c;

	static int get(int a, int b) {
		if (a == 0 && b == 0) {
			return 1;
		}
		return c[a + b - 1][a];
	}

	public static int count(int[] a) {
		int sum = 0;
		for (int i : a) {
			sum += i;
		}
		final int N = sum + 1;
		c = new int[N + 100][100];
		c[0][0] = 1;
		for (int i = 1; i < c.length; i++) {
			c[i][0] = 1;
			for (int j = 1; j < c[i].length; j++) {
				c[i][j] = c[i - 1][j - 1] + c[i - 1][j];
				if (c[i][j] >= mod) {
					c[i][j] -= mod;
				}
			}
		}
		int[] cnt = new int[N];
		int[] next = new int[N];
		cnt[0] = 1;
		for (int cur : a) {
//			System.err.println(Arrays.toString(cnt));
			Arrays.fill(next, 0);
			for (int use = 0; use < N; use++) {
				if (cnt[use] == 0) {
					continue;
				}
				for (int newNum = 0; newNum <= cur; newNum++) {
					long val = 0;
					for (int forNew = newNum; forNew <= cur; forNew++) {
						int mul = get(cur - forNew, use);
						val += get(forNew - newNum, newNum) * 1L * mul % mod;
					}
					val %= mod;
					val *= c[newNum + use][newNum] * 1L * cnt[use] % mod;
					val += next[use + newNum];
					next[use + newNum] = (int) (val % mod);
				}
			}
			int[] tmp = cnt;
			cnt = next;
			next = tmp;
		}
//		System.err.println(Arrays.toString(cnt));
		long result = 0;
		for (int i : cnt) {
			result += i;
		}
		return (int) (result % mod);
	}

	public static void main(String[] args) {
//		System.err.println(count(new int[] { 1, 1 }));
		System.err.println(count(new int[] { 1, 1, 1, 1, 1 }));
	}
}
