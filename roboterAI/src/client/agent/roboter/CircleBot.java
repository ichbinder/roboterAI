package client.agent.roboter;

import client.utils.Vector2Float;
import client.utils.Vector2Int;

public class CircleBot extends SimpleBot implements IRoboterAgent {
	
	Vector2Float CurrentDirection = new Vector2Float();
	boolean DirectionDirtyFlag = true;

	public void Tick()
	{
		this.Verbose = true;
		if(!super.IsIdle())
		{
			super.Tick();
			return;
		}
		
		if(CurrentDirection.Length() == 0.f)
		{
			CurrentDirection = new Vector2Float(Vector2Float.Up);
		}
		
		if(DirectionDirtyFlag)
		{
			DirectionDirtyFlag = false;
			GameSocket.setMoveDirection(RoboterID, CurrentDirection.X, CurrentDirection.X);
		}
		
		Vector2Float CurrentPostion = GetPosition();
		boolean SearchNewPlace = true;;
		Vector2Float NewDirection = CurrentDirection.RightRotation();
		
		for(int i = 0; i < 4; i++)
		{
			Vector2Float TestField = CurrentPostion.Add(NewDirection);
			if(Map.IsWalkable(TestField) && (!Map.IsMyColor(TestField)))
			{	
				SearchNewPlace = false;
				break;
			}
			
			NewDirection.RotateLeft();
		}
		
		if(CurrentDirection == NewDirection)
		{
			return;
		}
		
		if(!SearchNewPlace)
		{
			CurrentDirection = NewDirection;
			Vector2Int NewField = new Vector2Int(CurrentPostion.Add(NewDirection));
			Vector2Float NewTarget = new Vector2Float(NewField);
			NewTarget.AddInPlace(CurrentDirection.Multiply(0.25f).LeftRotation());
			GoTo(NewTarget);
		}
		else
		{
			SetPath(Pathfinder.GetPath(CurrentPostion, null));		
			DirectionDirtyFlag = true;
		}	
	}
	
	public void GoTo(Vector2Float Destination) 
	{
		super.GoTo(Destination);
		DirectionDirtyFlag = true;
	}
}
