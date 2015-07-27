package client.game.pathfinding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;

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
		List<Vector2Float> Path;
		
		if(Destination != null)
		{
			Vector2Int SimpleTest = CheckPathForObstacle(Start, Destination);
			
			if(SimpleTest == null)
			{
				Path = new ArrayList<Vector2Float>();
				Path.add(Destination);
				return Path;			
			}
		}
		
		Vector2Int DestinationField = Destination == null ? null : new Vector2Int(Destination);
		List<Vector2Float> DjikstraPath = Dijkstra(new Vector2Int(Start), DestinationField);
		
		if(DjikstraPath == null)
		{
			return null;
		}
		
		DjikstraPath.remove(0);
		DjikstraPath.add(0,  Start);
		
		if(Destination != null)
		{
			DjikstraPath.remove(DjikstraPath.size() - 1);
			DjikstraPath.add(Destination);
		}
		else
		{
			Destination = DjikstraPath.get(DjikstraPath.size() - 1); 
		}
		
		Path = SimplifyPath(Start, Destination, DjikstraPath);
		Path.remove(0);
		
		String Buffer = "Path: ";
		
		for(int i = 0; i < Path.size(); i++)
		{
			Buffer += " - " + Path.get(i);
		}
		System.out.println(Buffer);
		
		return Path;
	}
	
	private List<Vector2Float> Dijkstra(Vector2Int Start, Vector2Int Destination)
	{
		List<PathfindingNode> OpenNodes = new Vector<PathfindingNode>();
		List<Vector2Int> ClosedNodes = new Vector<Vector2Int>();
		
		OpenNodes.add(new PathfindingNode(Start, Destination));
		
		while(!OpenNodes.isEmpty())
		{			
			PathfindingNode CurrentNode = OpenNodes.get(0);
			OpenNodes.remove(0);
			ClosedNodes.add(CurrentNode.Position);
			
			if(((Destination != null) && (CurrentNode.Position.equals(Destination))) || ((Destination == null) && (!Map.IsMyColor(CurrentNode.Position))))
			{
				return MakePathList(CurrentNode);
			}
			
			AddNode(OpenNodes, ClosedNodes, CurrentNode, CurrentNode.Position.Add(Vector2Int.Up), Destination);
			AddNode(OpenNodes, ClosedNodes, CurrentNode, CurrentNode.Position.Add(Vector2Int.Left), Destination);
			AddNode(OpenNodes, ClosedNodes, CurrentNode, CurrentNode.Position.Add(Vector2Int.Down), Destination);
			AddNode(OpenNodes, ClosedNodes, CurrentNode, CurrentNode.Position.Add(Vector2Int.Right), Destination);
			
		}
		
		return null;
	}
	
	private List<Vector2Float> MakePathList(PathfindingNode Node)
	{
		List<Vector2Float> Path = new LinkedList<Vector2Float>();
		while(Node != null)
		{
			Path.add(0, new Vector2Float(Node.Position));
			Node = Node.Previous;
		} 
		
		return Path;
	}
	
	private void AddNode(List<PathfindingNode> OpenNodes, List<Vector2Int> ClosedNodes, PathfindingNode Parent, Vector2Int Position, Vector2Int Destination)
	{
		if(!Map.IsWalkable(Position))
		{
			return;
		}
		
		if(ClosedNodes.contains(Position))
		{
			return;
		}
		
		float CostMultiplier = Map.IsMyColor(Position) ? 2.f : 1.0f;
		
		PathfindingNode NewNode = new PathfindingNode(Position, Destination);
		NewNode.Previous = Parent;
		NewNode.CostSoFar = Parent.CostSoFar + (Position.Substract(Parent.Position).Length() * CostMultiplier);
		
		float NewNodeValue = NewNode.GetValue();
		boolean Inserted = false;
		
		
		for(int i = 0; i < OpenNodes.size(); i++)
		{
			PathfindingNode Node = OpenNodes.get(i);
			
			if(Node.equals(NewNode))
			{
				Inserted = true;
				break;
			}
			
			if(NewNodeValue < Node.GetValue())
			{
				Inserted = true;
				OpenNodes.add(i, NewNode);
				break;
			}
		}
		
		if(!Inserted)
		{
			OpenNodes.add(NewNode);
		}
	}
	
	private List<Vector2Float> SimplifyPath(Vector2Float Start, Vector2Float Destination, List<Vector2Float> Path)
	{
		List<Vector2Float> SimplifiedPath = new ArrayList<Vector2Float>();
		SimplifiedPath.add(Destination);
		
		Vector2Float CurrentDestination = Destination;
		//while(start != destination)
		//for start to destination
			//Check for Obstacle
				//Add to Simplified path & break
		
		int CurrentDestinationIndex = Path.size() - 1;

		while(!Start.equals(CurrentDestination))
		{
			for(int i = 0; i < CurrentDestinationIndex; i++)
			{
				Vector2Float CurrentStart = Path.get(i);
				Vector2Int Obstacle = CheckPathForObstacle(CurrentStart, CurrentDestination);
				if(Obstacle == null)
				{
					SimplifiedPath.add(0, CurrentStart);
					CurrentDestination = CurrentStart;
					CurrentDestinationIndex = i;
					break;
				}
			}
		}
		
		return SimplifiedPath;
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
		
		while(!CurrentField.equals(EndField))
		{			
			LineScaleToIntersection.X = CalculateLineScale(Start.X, Direction.X, StartingIntersections.X + Steps.X * StepDirection.X);
			LineScaleToIntersection.Y = CalculateLineScale(Start.Y, Direction.Y, StartingIntersections.Y + Steps.Y * StepDirection.Y);
			
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
