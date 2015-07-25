package client.agent;

import java.util.Random;

import client.game.Map;
import lenz.htw.zaip.net.NetworkClient;

public class RandomAgent implements IAgent {
	
	private NetworkClient GameSocket = null;
	private Map Map = null;;
	private Random Random = new Random();
	
	public void Setup(NetworkClient GameSocket, Map Map)
	{
		this.GameSocket = GameSocket;
		this.Map = Map;
	}
	
	public void Tick() 
	{
		for (int i = 0; i < 3; i++) {
			if (i == 0 && Random.nextBoolean())
				GameSocket.setMoveDirection(0, 0, 0);
			else 
				GameSocket.setMoveDirection(i, Random.nextFloat() - 0.5f, Random.nextFloat() - 0.5f);
		}
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
