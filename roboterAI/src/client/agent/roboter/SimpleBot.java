package client.agent.roboter;

import java.util.List;

import lenz.htw.zaip.net.NetworkClient;
import client.agent.roboter.IRoboterAgent.RoboterType;
import client.game.Map;
import client.game.pathfinding.IPathfinder;
import client.game.pathfinding.SimplePathfinder;
import client.utils.Vector2Float;
import client.utils.Vector2Int;

public class SimpleBot implements IRoboterAgent {
	
	private NetworkClient GameSocket;
	private Map Map;
	private int RoboterID;
	private RoboterType RoboterType;
	
	private IPathfinder Pathfinder = null;
	
	private List<Vector2Float> Path = null;
	
	private float LastDistance = Float.POSITIVE_INFINITY;
	private Vector2Int LastField;
	
	public void Setup(NetworkClient GameSocket, Map Map, int RoboterID) 
	{
		this.GameSocket = GameSocket;
		this.Map = Map;
		this.RoboterID = RoboterID;
		this.Pathfinder = new SimplePathfinder(Map);
		
		LastField = new Vector2Int(GetPosition());
	}

	public void Tick() 
	{
		if(IsIdle())
		{
			return;
		}

		Vector2Float CurrentPosition = GetPosition();
		Vector2Float NextPoint = Path.get(0);

		float Distance = NextPoint.Subtract(CurrentPosition).Length();
		
		if(Distance > LastDistance)
		{
			System.out.println(RoboterID + " reached Waypoint.");
			Path.remove(0);
			if(!IsIdle())
			{
				SetDirectionTowards(Path.get(0));
			}
			else
			{
				GameSocket.setMoveDirection(RoboterID, 0.f, 0.f);
			}
		}
		else
		{
			LastDistance = Distance;
		}
	}
	
	private void SetDirectionTowards(Vector2Float NextPoint)
	{
		Vector2Float CurrentPosition = GetPosition();
		Vector2Float Direction = NextPoint.Subtract(CurrentPosition);
		GameSocket.setMoveDirection(RoboterID, Direction.X, Direction.Y);
		LastDistance = Float.POSITIVE_INFINITY;
	}

	public void GoTo(Vector2Float Destination) 
	{
		System.out.println(RoboterID + " set target " + Destination);
		Path = Pathfinder.GetPath(GetPosition(), Destination);
		SetDirectionTowards(Path.get(0));
	}

	public boolean IsIdle() 
	{
		return (Path == null) || (Path.isEmpty());
	}

	public Vector2Float GetPosition()
	{
		return Map.GetPlayerPosition(GameSocket.getMyPlayerNumber(), RoboterID);
	}
	
	public client.agent.roboter.IRoboterAgent.RoboterType GetRoboterType() 
	{
		switch(RoboterID)
		{
			case 0:
				return client.agent.roboter.IRoboterAgent.RoboterType.Normal;
			case 1:
				return client.agent.roboter.IRoboterAgent.RoboterType.BigOne;
			case 2:
				return client.agent.roboter.IRoboterAgent.RoboterType.Plotter;
			default:
				return client.agent.roboter.IRoboterAgent.RoboterType.Normal;
		}
	}

}
