package client.agent;

import java.util.Random;

import lenz.htw.zaip.net.NetworkClient;
import client.agent.roboter.IRoboterAgent;
import client.agent.roboter.SimpleBot;
import client.game.Map;
import client.utils.Vector2Float;

public class TacticsAgent implements IAgent {

	private NetworkClient GameSocket = null;
	private Map Map = null;
	private Random Random = new Random();

	private IRoboterAgent Plotter = new SimpleBot();
	private IRoboterAgent Normal = new SimpleBot();
	private IRoboterAgent BigOne = new SimpleBot();
	
	public void Setup(NetworkClient GameSocket, Map Map) {
		this.GameSocket = GameSocket;
		this.Map = Map;
		
		Plotter.Setup(GameSocket, Map, 0);
		Normal.Setup(GameSocket, Map, 1);
		BigOne.Setup(GameSocket, Map, 2);
	}

	public void Tick() {

		if(Plotter.IsIdle())
		{
			Vector2Float NewDestination = Map.GetRandomWalkablePoint(Random);
			Plotter.GoTo(NewDestination);
		}

		Plotter.Tick();
		Normal.Tick();
		BigOne.Tick();
	}

}
