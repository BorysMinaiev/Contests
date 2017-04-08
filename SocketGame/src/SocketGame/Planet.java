package SocketGame;
import java.util.HashSet;


class Planet {
	int id, x, y, prom;
	HashSet<Integer> packetsHere;
	boolean used;

	public Planet(int id, int x, int y, int prom) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
		this.prom = prom;
		packetsHere = new HashSet<>();
	}

	boolean isEmpty() {
		for (int packetId : packetsHere) {
			if (!Strategy.packetsHave.contains(packetId)) {
				return false;
			}
		}
		return true;
	}

	int getInterestingSize() {
		int res = 0;
		for (int packetId : packetsHere) {
			if (!Strategy.packetsHave.contains(packetId)
					&& !Strategy.packetsOnWay.contains(packetId)) {
				res++;
			}
		}
		return res;
	}
	
	boolean isInteresting() {
		for (int packetId : packetsHere) {
			if (!Strategy.packetsHave.contains(packetId)
					&& !Strategy.packetsOnWay.contains(packetId)) {
				return true;
			}
		}
		return false;
	}
}