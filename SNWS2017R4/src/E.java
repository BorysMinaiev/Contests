import java.io.*;
import java.util.*;

public class E {
	FastScanner in;
	PrintWriter out;

	final int SQRT = 333;

	int[] blockId;
	int[] blockPos;
	byte[][][] bits;
	int[][] sum;
	int[] totSum;

	int k;

	int getSum(int fr, int to) {
		int res = 0;
		int bId = blockId[fr];
		int it = fr % SQRT;
		while (fr <= to && blockId[fr] == bId) {
			for (int z = 1; z < k; z++) {
				res += bits[bId][z][it] * z;
			}
			it++;
			fr++;
		}
		while (fr <= to && blockId[fr] != blockId[to]) {
			res += totSum[blockId[fr]];
			fr += SQRT;
		}
		if (fr <= to) {
			it = 0;
			bId = blockId[fr];
			while (fr <= to) {
				for (int z = 1; z < k; z++) {
					res += bits[bId][z][it] * z;
				}
				fr++;
				it++;
			}
		}
		return res;
	}

	byte[][] tmpB = new byte[10][];
	int[] tmpI = new int[10];

	void changeBlock(int bId, int d) {
		for (int z = 1; z < k; z++) {
			totSum[bId] -= z * sum[bId][z];
		}
		for (int z = 0; z < k; z++) {
			tmpB[z] = bits[bId][z];
			tmpI[z] = sum[bId][z];
		}
		int n = d;
		for (int z = 0; z < k; z++) {
			bits[bId][n] = tmpB[z];
			sum[bId][n] = tmpI[z];
			totSum[bId] += n * tmpI[z];
			n++;
			if (n == k) {
				n = 0;
			}
		}
	}

	int n;

	void print() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < k; j++) {
				if (bits[blockId[i]][j][i % SQRT] == 1) {
					System.err.print(j + " ");
				}
			}
		}
		System.err.println();
	}

	void change(int fr, int to, int d) {
		if (d == 0) {
			return;
		}
		int bId = blockId[fr];
		int it = fr % SQRT;
		while (fr <= to && blockId[fr] == bId) {
			for (int z = 0; z < k; z++) {
				if (bits[bId][z][it] == 1) {
					bits[bId][z][it] = 0;
					totSum[bId] -= z;
					sum[bId][z]--;
					int n = (z + d) % k;
					totSum[bId] += n;
					sum[bId][n]++;
					bits[bId][n][it] = 1;
					break;
				}
			}
			it++;
			fr++;
		}
		while (fr <= to && blockId[fr] != blockId[to]) {
			changeBlock(blockId[fr], d);
			fr += SQRT;
		}
		if (fr <= to) {
			it = 0;
			bId = blockId[fr];
			while (fr <= to) {
				for (int z = 0; z < k; z++) {
					if (bits[bId][z][it] == 1) {
						bits[bId][z][it] = 0;
						totSum[bId] -= z;
						sum[bId][z]--;
						int n = (z + d) % k;
						totSum[bId] += n;
						sum[bId][n]++;
						bits[bId][n][it] = 1;
						break;
					}
				}
				it++;
				fr++;
			}
		}
	}

	void solve() {
		n = in.nextInt();
		k = in.nextInt();
		int q = in.nextInt();
		int blocks = 1 + (n - 1) / SQRT;
		blockId = new int[n];
		for (int i = 0; i < n; i++) {
			blockId[i] = i / SQRT;
		}
		bits = new byte[blocks][k][SQRT];
		sum = new int[blocks][k];
		totSum = new int[blocks];
		blockPos = new int[n];
		for (int i = 0; i < n; i++) {
			blockPos[i] = i % SQRT;
		}
		for (int i = 0; i < n; i++) {
			int val = in.nextInt();
			bits[blockId[i]][val][blockPos[i]] = 1;
			sum[blockId[i]][val]++;
		}
		for (int i = 0; i < blocks; i++) {
			for (int j = 0; j < k; j++) {
				totSum[i] += j * sum[i][j];
			}
		}
		for (int i = 0; i < q; i++) {
			if (in.nextInt() == 0) {
				int fr = in.nextInt() - 1;
				int to = in.nextInt() - 1;
				int diff = in.nextInt();
				change(fr, to, diff);
			} else {
				out.println(getSum(in.nextInt() - 1, in.nextInt() - 1));
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
		new E().runIO();
	}
}