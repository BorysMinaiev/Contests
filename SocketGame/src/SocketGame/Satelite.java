package SocketGame;

class Satelite implements Comparable<Satelite> {
	int id;
	int x, y;
	int speed;
	int memory;
	int maxSygnal;
	double minSINR;
	int lvlSil, lvlPam, lvlNad, lvlOdb;
	int[] packets;

	public Satelite(int id, int x, int y, int speed, int memory, int maxSygnal,
			double minSINR, int lvlSil, int lvlPam, int lvlNad, int lvlOdb) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.memory = memory;
		this.maxSygnal = maxSygnal;
		this.minSINR = minSINR;
		this.lvlSil = lvlSil;
		this.lvlPam = lvlPam;
		this.lvlNad = lvlNad;
		this.lvlOdb = lvlOdb;
		packets = new int[memory];
	}

	@Override
	public int compareTo(Satelite o) {
		return Integer.compare(id, o.id);
	}

	boolean hasMemory() {
		for (int id : packets) {
			if (id < 0) {
				return true;
			}
			if (Strategy.packetsHave.contains(id)) {
				return true;
			}
		}
		return false;
	}

	boolean needToMother() {
		for (int packet : packets) {
			if (packet >= 0 && !Strategy.packetsHave.contains(packet)) {
				return true;
			}
		}
		return false;
	}
	
	int memoryUsed() {
		int res = 0;
		for (int packet : packets) {
			if (packet >= 0 && !Strategy.packetsHave.contains(packet)) {
				res++;
			}
		}
		return res;
	}
}