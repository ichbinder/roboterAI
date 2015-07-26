package client.game.pathfinding;

import java.util.ArrayList;
import java.util.List;

import client.game.Map;
import client.utils.Vector2Float;
import client.utils.Vector2Int;

public class SimplePathfinder implements IPathfinder {
	
	private Map Map;
	
	public SimplePathfinder(Map Map)
	{
		this.Map = Map;
	}

	public List<Vector2Float> GetPath(Vector2Float Start, Vector2Float Destination) 
	{
		List<Vector2Float> Path = new ArrayList<Vector2Float>();
		
		Vector2Int Obstacle = CheckPathForObstacle(Start, Destination);
		
		if(Obstacle != null)
		{
			System.out.println("Obstacle on Path from " + Start +" to " + Destination + " at " + Obstacle);
		}
		
		Path.add(Destination);
		
		return Path;
	}
	
	public Vector2Int CheckPathForObstacle(Vector2Float Start, Vector2Float Destination)
	{
		Vector2Float Direction = Destination.Subtract(Start).Normalize();
		Vector2Float LineScaleToIntersection = new Vector2Float();
		Vector2Int StepDirection = CalculateStepDirection(Direction);
		Vector2Float StartingIntersections = GetStartingIntersections(Start, StepDirection);

		Vector2Int StartField = new Vector2Int(Start);
		Vector2Int CurrentField = StartField;
		Vector2Int EndField = new Vector2Int(Destination);
		Vector2Int Steps = new Vector2Int();
		

		System.out.println("Start: " + Start);
		System.out.println("Destination: " + Destination);
		System.out.println("Direction: " + Direction);
		System.out.println("StepDirection: " + StepDirection);
		System.out.println("StartingIntersections: " + StartingIntersections);
		System.out.println("StartField: " + StartField);
		System.out.println("EndField: " + EndField);
		
		while(!CurrentField.equals(EndField))
		{			
			LineScaleToIntersection.X = CalculateLineScale(Start.X, Direction.X, StartingIntersections.X + Steps.X * StepDirection.X);
			LineScaleToIntersection.Y = CalculateLineScale(Start.Y, Direction.Y, StartingIntersections.Y + Steps.Y * StepDirection.Y);

			System.out.println("LineScaleToIntersection: " + LineScaleToIntersection);
			
			if(LineScaleToIntersection.X > LineScaleToIntersection.Y)
			{
				Steps.Y++;
			}
			else
			{
				Steps.X++;
			}
			
			CurrentField = new Vector2Int(Start);
			CurrentField.X += Steps.X * StepDirection.X;
			CurrentField.Y += Steps.Y * StepDirection.Y;
			System.out.println("Checking: " + CurrentField);
			
			if(!Map.IsWalkable(CurrentField))
			{
				return CurrentField;
			}
		}
		
		return null;
	}
	
	private Vector2Float GetStartingIntersections(Vector2Float Start, Vector2Int StepDirection)
	{
		Vector2Float StartingIntersections = new Vector2Float();

		//Snap to Grid (0.5, 1.5, 2.5, ...
		if(StepDirection.X != 0)
		{
			StartingIntersections.X = StepDirection.X > 0 ? GetUpperIntersections(Start.X) : GetLowerIntersections(Start.X);
		}
		
		if(StepDirection.Y != 0)
		{
			StartingIntersections.Y = StepDirection.Y > 0 ? GetUpperIntersections(Start.Y) : GetLowerIntersections(Start.Y);
		}
		
		return StartingIntersections;
	}
	
	private float GetUpperIntersections(float Value)
	{
		return Math.round(Value) + 0.5f;
	}
	
	private float GetLowerIntersections(float Value)
	{
		return Math.round(Value) - 0.5f;
	}
	
	private Vector2Int CalculateStepDirection(Vector2Float Direction)
	{
		Vector2Int StepDirection = new Vector2Int();
		
		if(Direction.X != 0.f)
		{
			StepDirection.X = (Direction.X) > 0.f ? 1 : -1; 
		}
		
		if(Direction.Y != 0.f)
		{
			StepDirection.Y = (Direction.Y) > 0.f ? 1 : -1; 
		}
		
		return StepDirection;
	}
	
	private float CalculateLineScale(float StartCompoment, float DirectionComponent, float GridLineComponent)
	{
		if(DirectionComponent != 0.f)
		{
			return (GridLineComponent - StartCompoment) / DirectionComponent;
		}
		else
		{
			return Float.POSITIVE_INFINITY;
		}
	}

}
