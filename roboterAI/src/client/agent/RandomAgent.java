package client.agent;

import java.util.Random;

import client.agent.roboter.IRoboterAgent;
import client.agent.roboter.IRoboterAgent.RoboterType;
import client.agent.roboter.SimpleBot;
import client.game.Map;
import client.game.pathfinding.IPathfinder;
import client.game.pathfinding.SimplePathfinder;
import client.utils.Vector2Float;
import lenz.htw.zaip.net.NetworkClient;

public class RandomAgent implements IAgent {
	
	private NetworkClient GameSocket = null;
	private Map Map = null;
	private Random Random = new Random(1);
	
	private IRoboterAgent RoboterAgents[] = new IRoboterAgent[3];
	
	public void Setup(NetworkClient GameSocket, Map Map)
	{
		this.GameSocket = GameSocket;
		this.Map = Map;

		for (int i = 0; i < 3; i++)
		{
			RoboterAgents[i] = new SimpleBot();
			RoboterAgents[i].Setup(GameSocket, Map, i);
		}	
	}
	
	public void Tick() 
	{
		for (int i = 0; i < 3; i++) {
			IRoboterAgent Bot = RoboterAgents[i];
			
			if(Bot.IsIdle())
			{
				Vector2Float NewDestination = Map.GetRandomWalkablePoint(Random);
				Bot.GoTo(NewDestination);
			}
			
			Bot.Tick();
		}
	}
	
}
