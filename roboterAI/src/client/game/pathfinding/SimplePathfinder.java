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
		
		Path.add(Destination);
		
		return Path;
	}
	
}
