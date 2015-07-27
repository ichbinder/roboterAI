package client.game.pathfinding;

import client.utils.Vector2Int;

public class PathfindingNode {
	public float CostSoFar = 0.f;
	public float EstCostLeft = 0.f;
	
	public Vector2Int Position;
	public PathfindingNode Previous = null;
	
	public PathfindingNode(Vector2Int Position)
	{
		this.Position = Position;
	}
	
	public PathfindingNode(Vector2Int Position, Vector2Int End)
	{
		this(Position);
		
		if(End != null)
		{
			this.EstCostLeft = End.Substract(End).Length();	
		}
	}

	public boolean equals(Object other)
	{
		if(this == other)
		{
			return true;
		}

		if(other == null)
		{
			return false;	
		}
		
		if(!this.getClass().isInstance(other))
		{
			return false;
		}
		
		return this.Position.equals(((PathfindingNode)other).Position);
	}
	
	public float GetValue()
	{
		return CostSoFar + EstCostLeft;
	}
}
