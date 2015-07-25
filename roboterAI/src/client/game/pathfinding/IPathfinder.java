package client.game.pathfinding;

import java.util.List;

import client.utils.Vector2Float;

public interface IPathfinder {
	List<Vector2Float> GetPath(Vector2Float Start, Vector2Float Destination);
}
