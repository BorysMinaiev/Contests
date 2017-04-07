import java.io.*;
import java.util.*;

public class Solution {
	FastScanner in;
	PrintWriter out;
	

	Random rnd = new Random(122223);
	
	double func(int a, int b, double c) {
		return a * 1.0 / Math.pow(b, 0.777) * c;
	}

	void solve() {
		int videos = in.nextInt();
		int endPoints = in.nextInt();
		int req = in.nextInt();
		int cacheServers = in.nextInt();
		int capacity = in.nextInt();
		System.err.println("videos " + videos);
		System.err.println("endPoints " + endPoints);
		System.err.println("caches " + cacheServers);
		int[] videoSizes = new int[videos];
		int[] sumLatency = new int[cacheServers];
		for (int i = 0; i < videos; i++) {
			videoSizes[i] = in.nextInt();
		}
		int[][] len = new int[endPoints][cacheServers];
		for (int i = 0; i < endPoints; i++) {
			int latency = in.nextInt();
			int k = in.nextInt();
			for (int  j = 0; j < cacheServers; j++) {
				sumLatency[j] += latency;
			}
			for (int j = 0; j < k; j++) {
				int id = in.nextInt();
				sumLatency[id] -= latency;
				len[i][id] = Math.max(len[i][id], latency - in.nextInt());
				sumLatency[id] += latency - len[i][id];
			}
		}
		for (int z = 0; z < sumLatency.length; z++) {
			sumLatency[z] = 1000 + rnd.nextInt(100);
		}
		long[][] requests = new long[endPoints][videos];
		HashSet<Integer>[] endPointsForVideoHash = new HashSet[videos];
		for (int i = 0; i < videos; i++) {
			endPointsForVideoHash[i] = new HashSet<>();
		}
		for (int i = 0; i < req; i++) {
			int videoId = in.nextInt();
			int endPoint = in.nextInt();
			int cnt = in.nextInt();
			requests[endPoint][videoId] += cnt;
			endPointsForVideoHash[videoId].add(endPoint);
		}
		int[][] endPointsForVideo = new int[videos][];
		for (int i = 0; i < videos; i++) {
			HashSet<Integer> h = endPointsForVideoHash[i];
			endPointsForVideo[i] = new int[h.size()];
			int ptr = 0;
			for (int elem : h) {
				endPointsForVideo[i][ptr++] = elem;
			}
		}
		Random rnd = new Random(123);
		boolean[] used = new boolean[videos];
		Sol sol = new Sol(cacheServers);
		int[][] time = new int[endPoints][videos];
		int[][] profit = new int[videos][cacheServers];
		int[] space = new int[cacheServers];
		Arrays.fill(space, capacity);
		for (int i = 0; i < videos; i++) {
			for (int j = 0; j < cacheServers; j++) {
				for (int k : endPointsForVideo[i]) {
					profit[i][j] += requests[k][i]
							* Math.max(0, len[k][j] - time[k][i]);
				}
			}
		}
		double[] optimalProfit = new double[videos];
		int[] optimalCacheServer = new int[videos];
		for (int i = 0; i < videos; i++) {
			double bestProfit = 0.0;
			int bestCacheServer = -1;
			for (int j = 0; j < cacheServers; j++) {
				if (space[j] < videoSizes[i]) {
					continue;
				}
				double unitProfit = func(profit[i][j], videoSizes[i], sumLatency[j]);
				if (unitProfit > bestProfit) {
					bestProfit = unitProfit;
					bestCacheServer = j;
				}
			}
			optimalProfit[i] = bestProfit;
			optimalCacheServer[i] = bestCacheServer;
		}
		System.err.println("calculated profit");
		long puts = 0;
		while (true) {
			puts++;
			if (puts % 1000 == 0) {
				System.err.println("putting another 1000!");
			}
			double bestProfit = 0.0;
			int bestVideo = -1;
			int bestCacheServer = -1;
			for (int i = videos - 1; i >= 0; i--) {
				int server = optimalCacheServer[i];
				if (server == -1) {
					continue;
				}
				if (space[server] < videoSizes[i]) {
					double curBestProfit = 0.0;
					int curBestCacheServer = -1;
					for (int j = 0; j < cacheServers; j++) {
						if (space[j] < videoSizes[i]) {
							continue;
						}
						double unitProfit = func(profit[i][j], videoSizes[i], sumLatency[j]);
						if (unitProfit > curBestProfit) {
							curBestProfit = unitProfit;
							curBestCacheServer = j;
						}
					}
					optimalProfit[i] = curBestProfit;
					optimalCacheServer[i] = curBestCacheServer;
					server = optimalCacheServer[i];
					if (server == -1) {
						continue;
					}
				}
				if (optimalProfit[i] > bestProfit && rnd.nextDouble() < 1) {// || (optimalProfit[i] ==
													// bestProfit && (bestVideo
													// == -1 || videoSizes[i] >
													// videoSizes[bestVideo])))
													// {
					bestProfit = optimalProfit[i];
					bestVideo = i;
					bestCacheServer = server;
				}
			}
			if (bestVideo == -1) {
				break;
			}
			space[bestCacheServer] -= videoSizes[bestVideo];
			sol.add(bestCacheServer, bestVideo);
			for (int k = 0; k < endPoints; k++) {
				time[k][bestVideo] = Math.max(time[k][bestVideo],
						len[k][bestCacheServer]);
			}
			for (int j = 0; j < cacheServers; j++) {
				profit[bestVideo][j] = 0;
				for (int k : endPointsForVideo[bestVideo]) {
					profit[bestVideo][j] += requests[k][bestVideo]
							* Math.max(0, len[k][j] - time[k][bestVideo]);
				}
			}
			bestProfit = 0.0;
			bestCacheServer = -1;
			for (int j = 0; j < cacheServers; j++) {
				if (space[j] < videoSizes[bestVideo]) {
					continue;
				}
				double unitProfit = func(profit[bestVideo][j],
						videoSizes[bestVideo], sumLatency[j]);
				if (unitProfit > bestProfit) {
					bestProfit = unitProfit;
					bestCacheServer = j;
				}
			}
			optimalProfit[bestVideo] = bestProfit;
			optimalCacheServer[bestVideo] = bestCacheServer;
		}
		/*
		 * for (int i = 0; i < cacheServers; i++) { Arrays.fill(used, false);
		 * int cntSum = 0; for (int j = 0; j < videos * 10; j++) { int id =
		 * rnd.nextInt(videos); if (!used[id] && cntSum + videoSizes[id] <=
		 * capacity) { used[id] = true; cntSum += videoSizes[id]; sol.add(i,
		 * id); } } }
		 */
		System.err.println("									score = "
				+ (int) getScore(sol, requests, len));
		sol.print();
	}

	double getScore(Sol sol, long[][] req, int[][] len) {
		double res = 0;
		long totalReq = 0;
		int videos = (int) 1e4;
		BitSet[] has = new BitSet[sol.ids.length];
		for (int i = 0; i < has.length; i++) {
			has[i] = new BitSet(videos);
			for (int j : sol.ids[i]) {
				has[i].set(j);
			}
		}
		for (int i = 0; i < req.length; i++) {
			for (int j = 0; j < req[i].length; j++) {
				if (req[i][j] != 0) {
					totalReq += req[i][j];
					int cost = 0;
					for (int k = 0; k < len[i].length; k++) {
						if (len[i][k] > cost && has[k].get(j)) {
							cost = len[i][k];
						}
					}
					res += cost * 1000.0 * req[i][j];
				}
			}
		}
		System.err.println("total = " + totalReq + ", sum =  " + res);
		return res / totalReq;
	}

	class Sol {
		ArrayList<Integer>[] ids;

		Sol(int servers) {
			ids = new ArrayList[servers];
			for (int i = 0; i < servers; i++) {
				ids[i] = new ArrayList<>();
			}
		}

		void add(int server, int video) {
			ids[server].add(video);
		}

		void print() {
			out.println(ids.length);
			for (int i = 0; i < ids.length; i++) {
				out.print(i);
				for (int x : ids[i]) {
					out.print(" " + x);
				}
				out.println();
			}
		}
	}

	void run() {
//		String[] names = new String[] { "me_at_the_zoo", "trending_today",
//				"videos_worth_spreading", "kittens" };
		// String[] names = new String[] { "me_at_the_zoo",
		// "videos_worth_spreading", "kittens" };
		// String[] names = new String[] { "kittens" };//"me_at_the_zoo",
		// "videos_worth_spreading", "kittens" };
		// String[] names = new String[] { "me_at_the_zoo",
		// "videos_worth_spreading"};
		 String[] names = new String[] { "kittens" };
		// String[] names = new String[] { "trending_today" };

		try {
			for (String name : names) {
				System.err.println("Start " + name);
				in = new FastScanner(new File(name + ".in"));
				out = new PrintWriter(new File(name + ".out"));

				solve();

				out.close();
				System.err.println(name + " done");
			}
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
		new Solution().run();
	}
}