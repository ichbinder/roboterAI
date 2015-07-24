package client.game;

import lenz.htw.zaip.net.NetworkClient;

public class Map {
	
	final int MAP_SIZE = 32;
	
	private int[][] map = new int[MAP_SIZE][MAP_SIZE];
	private NetworkClient gameSocket;
	
	public Map(NetworkClient gameSocket) {
		this.gameSocket = gameSocket;
		String zeile = "";
		for (int i = 0; i < MAP_SIZE; i++) {
			zeile = "";
			for (int j = 0; j < MAP_SIZE; j++) {
				map[i][j] = this.gameSocket.getBoard(i, j);
				if(map[i][j] < 0) {
					zeile += " -1";
				} else {
					zeile += "  0";
				}
			}
			System.out.println(zeile);
		}
	}
}
