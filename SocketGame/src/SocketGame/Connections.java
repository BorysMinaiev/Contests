package SocketGame;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connections {
	static PrintWriter out, roundInfo;
	static BufferedReader in, stdIn;

	public static class Event {
		boolean fromStdIn;
		MyTokenizer tokenizer;
		String line;

		public Event(boolean fromStdIn, String line) {
			super();
			this.fromStdIn = fromStdIn;
			this.line = line;
			this.tokenizer = new MyTokenizer(line);
		}

	}

	static boolean expectOK() {
		return expect("OK");
	}

	static boolean expect(String s) {
		try {
			String got = in.readLine();
			if (got.equals(s)) {
				return true;
			}
			roundInfo.println("expected OK, found " + got);
			roundInfo.flush();
			System.err.println("EXPECT OK, FOUND: " + got);
			return false;
			// throw new AssertionError();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	static MyTokenizer tokenizer;

	static String nextToken() {
		while (tokenizer == null || !tokenizer.hasMoreElements()) {
			try {
				tokenizer = new MyTokenizer(in.readLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tokenizer.nextToken();
	}

	public static int nextInt() {
		return Integer.parseInt(nextToken());
	}

	public static double nextDouble() {
		return Double.parseDouble(nextToken());
	}

	static Event getNextEvent() throws IOException {
		if (in.ready()) {
			String s = in.readLine();
			System.err.println("get line from net: " + s);
			return new Event(false, s);
		} else {
			if (stdIn.ready()) {
				String s = stdIn.readLine();
				System.err.println("get line from stdin: " + s);
				return new Event(true, s);
			}
		}
		try {
			Thread.sleep(Constants.READ_TIMEOUT_MS);
		} catch (InterruptedException e) {

		}
		return getNextEvent();
	}

	static void sendToServer(String line) {
		Strategy.commandsSend++;
		System.err.println("send to net: " + line);
		roundInfo.println("Send: " + System.currentTimeMillis() + ": " + line);
		roundInfo.flush();
		out.println(line);
	}

	public static void createConnections() throws UnknownHostException,
			IOException {
		System.err.println("!!!");
		while (true) {
			try {
				Socket socket = new Socket(Constants.HOSTNAME, Constants.PORT);
				out = new PrintWriter(socket.getOutputStream(), true);
				roundInfo = new PrintWriter(new File("roundinfo.txt"));
				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				stdIn = new BufferedReader(new InputStreamReader(System.in));
				System.err.println("???");
				break;
			} catch (Exception e) {
				System.err.println(" :( " + e.getMessage());

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// break;
			}
		}
	}
}
