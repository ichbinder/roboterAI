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
	
	protected NetworkClient GameSocket;
	protected Map Map;
	protected int RoboterID;
	
	protected IPathfinder Pathfinder = null;
	
	protected List<Vector2Float> Path = null;
	
	protected float LastDistance = Float.POSITIVE_INFINITY;
	
	public void Setup(NetworkClient GameSocket, Map Map, int RoboterID) 
	{
		this.GameSocket = GameSocket;
		this.Map = Map;
		this.RoboterID = RoboterID;
		this.Pathfinder = new SimplePathfinder(Map);
	}

	public void Tick() 
	{
		if(IsIdle())
		{
			return;
		}

		Vector2Float CurrentPosition = GetPosition();
		
		if(GetRoboterType() == RoboterType.Plotter)
		{
			if(!Map.IsMyColor(new Vector2Int(CurrentPosition)))
			{
				SetDirectionTowards(CurrentPosition, true);
				return;
			}
			else
			{
				SetDirectionTowards(Path.get(0), true);
			}
		}
		
		
		Vector2Float NextPoint = Path.get(0);
		Vector2Int NextPointField = new Vector2Int(NextPoint);
		Vector2Int CurrentField = new Vector2Int(CurrentPosition);

		float Distance = NextPoint.Subtract(CurrentPosition).Length();

		if(((NextPointField == CurrentField) && (Distance < 0.2f)) || (Distance > LastDistance))
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

	protected void SetDirectionTowards(Vector2Float NextPoint)
	{
		SetDirectionTowards(NextPoint, false);
	}
	
	protected void SetDirectionTowards(Vector2Float NextPoint, boolean KeepLastDistance)
	{
		Vector2Float CurrentPosition = GetPosition();
		Vector2Float Direction = NextPoint.Subtract(CurrentPosition);
		GameSocket.setMoveDirection(RoboterID, Direction.X, Direction.Y);
		if(!KeepLastDistance)
		{
			LastDistance = Float.POSITIVE_INFINITY;
		}
	}

	public void GoTo(Vector2Float Destination) 
	{
		System.out.println(RoboterID + " from " + GetPosition() + " to " + Destination);
		Path = Pathfinder.GetPath(GetPosition(), Destination);
		if(Path != null)
		{
			SetDirectionTowards(Path.get(0));
		}
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
				return client.agent.roboter.IRoboterAgent.RoboterType.Plotter;
			case 1:
				return client.agent.roboter.IRoboterAgent.RoboterType.Normal;
			case 2:
				return client.agent.roboter.IRoboterAgent.RoboterType.BigOne;
			default:
				return client.agent.roboter.IRoboterAgent.RoboterType.Plotter;
		}
	}

}
