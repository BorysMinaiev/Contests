package SocketGame;

class Point {
	int x, y;

	public Point(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public Point(double x, double y) {
		this.x = (int) x;
		this.y = (int) y;
	}
}
