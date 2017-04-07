import java.io.*;
import java.util.*;

public class CopyOfMeAtZoo {
	FastScanner in;
	PrintWriter out;

	class O implements Comparable<O> {
		int videoId, cacheId;
		double fit;

		public O(int videoId, int cacheId, double fit) {
			super();
			this.videoId = videoId;
			this.cacheId = cacheId;
			this.fit = fit;
		}

		@Override
		public int compareTo(O o) {
			return Double.compare(o.fit, fit);
		}

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
		for (int i = 0; i < videos; i++) {
			videoSizes[i] = in.nextInt();
		}
		System.err.println(Arrays.toString(videoSizes));
		int[] latency = new int[endPoints];

		int[][] len = new int[endPoints][cacheServers];
		for (int i = 0; i < endPoints; i++) {
			latency[i] = in.nextInt();
			Arrays.fill(len[i], latency[i]);
			int k = in.nextInt();
			for (int j = 0; j < k; j++) {
				int id = in.nextInt();
				len[i][id] = Math.min(len[i][id], in.nextInt());
			}
		}
		long[][] requests = new long[endPoints][videos];
		for (int i = 0; i < req; i++) {
			int videoId = in.nextInt();
			int endPoint = in.nextInt();
			int cnt = in.nextInt();
			requests[endPoint][videoId] += cnt;
		}
		long[] wantVideo = new long[videos];
		for (int i = 0; i < endPoints; i++) {
			for (int j = 0; j < videos; j++) {
				wantVideo[j] += requests[i][j];
			}
		}
		// for (int z = 0; z < 100 && z < allVideos.length; z++) {
		// System.err.print(allVideos[z].cnt + " ");
		// }
		// System.err.println();
		Random rnd = new Random(123);

		// boolean[] used = new boolean[videos];
		Sol besol = null;
		int iter = 0;
		double bestScore = 0;
		while (iter < 10000) {
			iter++;
			Sol sol = new Sol(cacheServers, videos, capacity);

			if (rnd.nextBoolean()) {
				int[][] currentCost = new int[endPoints][videos];
				for (int i = 0; i < endPoints; i++) {
					Arrays.fill(currentCost[i], latency[i]);
				}
				while (true) {
					double bestTotalAdd = 0;
					int bestVideoId = -1, bestCacheId = -1;
					for (int videoId = 0; videoId < videos; videoId++) {
						for (int cacheId = 0; cacheId < cacheServers; cacheId++) {
							if (sol.already[cacheId] + videoSizes[videoId] <= capacity) {
								double totalAdd = 0;
								for (int end = 0; end < endPoints; end++) {
									long one = Math.max(0,
											currentCost[end][videoId]
													- len[end][cacheId]);
									totalAdd += one * requests[end][videoId];
								}
								totalAdd /= videoSizes[videoId];
								if (totalAdd >= bestTotalAdd) {
									bestTotalAdd = totalAdd;
									bestVideoId = videoId;
									bestCacheId = cacheId;
								}
							}
						}
					}
					if (bestTotalAdd == 0) {
						break;
					}
					sol.add(bestCacheId, bestVideoId, videoSizes[bestVideoId]);
					for (int end = 0; end < endPoints; end++) {
						long one = Math.max(0, currentCost[end][bestVideoId]
								- len[end][bestCacheId]);
						currentCost[end][bestVideoId] -= one;
					}
				}
			}

			double score = getScore(sol, requests, latency, len);
			System.err.println(score + " " + bestScore);
			double bs = score;
			int fails = 0;
			while (fails < 1000) {
				fails++;
				score = getScore(sol, requests, latency, len);
				if (score <= bs) {
					// throw new AssertionError();
				} else {
					fails = 0;
					bs = score;
				}
				// System.err.println("!!! " + score + " " + bestScore
				// + ", fails = " + fails + " " + bs);
				if (rnd.nextBoolean()) {
					if (rnd.nextBoolean()) {
						int servId = rnd.nextInt(cacheServers);
						int vid = -1;
						for (int it = 0; it < videos; it++) {
							vid = rnd.nextInt(videos);
							if (sol.hasVideo[servId][vid]) {
								break;
							} else {
								vid = -1;
							}
						}
						if (vid != -1) {
							sol.remove(servId, vid, videoSizes[vid]);
							double ns = getScore(sol, requests, latency, len);
							if (ns >= score) {
//								fails = 0;
//								System.err.println("OK REMOVE!");
							} else {
								if (rnd.nextDouble() < 0.9)
									sol.add(servId, vid, videoSizes[vid]);
							}
						}
					} else {
						// System.err.println("first");
						int video = rnd.nextInt(videos);
						int cacheId = rnd.nextInt(cacheServers);
						if (sol.add(cacheId, video, videoSizes[video])) {
							double ns = getScore(sol, requests, latency, len);
							if (ns > score) {
								// fails = 0;
							} else {
								sol.remove(cacheId, video, videoSizes[video]);
							}
						}
					}
				} else {
					// System.err.println("last");
					int servId = rnd.nextInt(cacheServers);
					int vid = -1;
					for (int it = 0; it < videos; it++) {
						vid = rnd.nextInt(videos);
						if (sol.hasVideo[servId][vid]) {
							break;
						} else {
							vid = -1;
						}
					}
					if (vid != -1) {
						// System.err
						// .println("remove " + vid + ", from " + servId);
						double sc = getScore(sol, requests, latency, len);
						sol.remove(servId, vid, videoSizes[vid]);
						// System.err.println("!!!");
						int video = rnd.nextInt(videos);
						int cacheId = servId;
						if (sol.add(cacheId, video, videoSizes[video])) {
							double nextSc = getScore(sol, requests, latency,
									len);
							// System.err.println("nextScore " + nextSc);
							if (nextSc < sc) {
								sol.remove(cacheId, video, videoSizes[video]);
								if (!sol.add(servId, vid, videoSizes[vid])) {
									throw new AssertionError();
								}
							} else {
								// fails = 0;
							}
						} else {
							sol.add(servId, vid, videoSizes[vid]);
						}
					}
				}
			}
			score = getScore(sol, requests, latency, len);
			System.err.println(score + " " + bestScore);
			if (score > bestScore) {
				bestScore = score;
				besol = sol;
			}
		}
		System.err.println("SCORE                  = "
				+ getScore(besol, requests, latency, len));
		// for (int i = 0; i < cacheServers; i++) {
		// // Arrays.fill(used, false);
		// int cntSum = 0;
		// for (Video v : allVideos) {
		// if (cntSum + videoSizes[v.id] <= capacity) {
		// cntSum += videoSizes[v.id];
		// sol.add(i, v.id);
		// }
		// }
		// // for (int j = 0; j < videos * 10; j++) {
		// // int id = rnd.nextInt(videos);
		// // if (!used[id] && cntSum + videoSizes[id] <= capacity) {
		// // used[id] = true;
		// // sol.add(i, id);
		// // cntSum += videoSizes[id];
		// // }
		// // }
		// }
		besol.print();
	}

	double getScore(Sol sol, long[][] req, int[] lat, int[][] len) {
		double res = 0;
		long totalReq = 0;
		int videos = (int) 1e4;
		for (int i = 0; i < req.length; i++) {
			for (int j = 0; j < req[i].length; j++) {
				if (req[i][j] != 0) {
					totalReq += req[i][j];
					int cost = lat[i];
					for (int k = 0; k < len[i].length; k++) {
						if (len[i][k] < cost && sol.hasVideo[k][j]) {
							cost = len[i][k];
						}
					}
					res += (lat[i] - cost) * 1.0 * req[i][j];
				}
			}
		}
		return 1000 * res / totalReq;
	}

	class Sol {
		boolean[][] hasVideo;
		int[] already;
		int capacity;

		Sol(int caches, int videos, int capacity) {
			hasVideo = new boolean[caches][videos];
			already = new int[caches];
			this.capacity = capacity;
		}

		boolean add(int server, int video, int videoSize) {
			if (hasVideo[server][video]) {
				return false;
			}
			// System.err.println("add " + server + " " + video + " " +
			// videoSize);
			if (already[server] + videoSize > capacity) {
				return false;
			}
			hasVideo[server][video] = true;
			already[server] += videoSize;
			return true;
		}

		void remove(int server, int video, int videoSize) {
			// System.err.println("remove " + server + " " + video + " "
			// + videoSize);
			already[server] -= videoSize;
			hasVideo[server][video] = false;
		}

		void print() {
			out.println(hasVideo.length);
			for (int i = 0; i < hasVideo.length; i++) {
				out.print(i);
				for (int j = 0; j < hasVideo[i].length; j++) {
					if (hasVideo[i][j]) {
						out.print(" " + j);
					}
				}
				out.println();
			}
		}
	}

	void run() {
		ArrayList<String> names = new ArrayList<>();
		// names.add("kittens");
		names.add("me_at_the_zoo");
		// names.add("trending_today");
		// names.add("videos_worth_spreading");
		// String[] names = new String[] { "me_at_the_zoo", "trending_today",
		// "videos_worth_spreading", "" };
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
		new CopyOfMeAtZoo().run();
	}
}