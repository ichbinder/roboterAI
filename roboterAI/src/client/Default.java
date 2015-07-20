package client;

import java.util.Random;

import lenz.htw.zaip.net.NetworkClient;

public class Default implements Runnable {

	private String name;
	private String ip;
	
	public static void main(String[] args) {
		for (int i = 0; i < 4; i++) {
			new Thread(new Default("Client" + i)).start();
		}
	}
	
	public Default(String name) {
		this("localhost", name);
	}
	
	public Default(String ip, String name) {
		this.ip = ip;
		this.name = name;
	}

	public void run() {
		try {
			NetworkClient client = new NetworkClient(this.ip, this.name);
			Random random = new Random();
			while (client.isAlive()) {
				for (int i = 0; i < 3; i++) {
					if (i == 0 && random.nextBoolean())
						client.setMoveDirection(0, 0, 0);
					else 
						client.setMoveDirection(i, random.nextFloat() - 0.5f, random.nextFloat() - 0.5f);
				}
				Thread.sleep(500);
			}
		} catch (Exception e) {
			System.err.println("Ouch! " + name);
			e.printStackTrace(System.err);
		}
	}
}
