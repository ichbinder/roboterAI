package client.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import client.utils.Vector2Int;
import lenz.htw.zaip.net.NetworkClient;

public class Map {
	
	final int MAP_SIZE = 32;
	
	private int[][] map = new int[MAP_SIZE][MAP_SIZE];
	private NetworkClient gameSocket;
	
	public Map(NetworkClient gameSocket) {
		this.gameSocket = gameSocket;	
	}
	
	public void Setup()
	{
		GetMapFromServer();
		CancelOutEdges();
		//CancelOutInefficentTiles
	}
	
	private void GetMapFromServer()
	{
		for (int i = 0; i < MAP_SIZE; i++) {
			for (int j = 0; j < MAP_SIZE; j++) {
				map[i][j] = this.gameSocket.getBoard(i, j);
			}
		}
	}
	
	private void CancelOutEdges()
	{
		FloodFill(new Vector2Int(0,0));
		FloodFill(new Vector2Int(MAP_SIZE - 1,0));
		FloodFill(new Vector2Int(0, MAP_SIZE - 1));
		FloodFill(new Vector2Int(MAP_SIZE - 1, MAP_SIZE - 1));
	}
	
	private void FloodFill(Vector2Int Start)
	{
		Queue<Vector2Int> Queue = new LinkedList<Vector2Int>();
		Vector2Int CurrentField;
		
		Queue.add(Start);
		
		while((CurrentField = Queue.poll()) != null)
		{
			if(map[CurrentField.X][CurrentField.Y] != -1)
			{
				map[CurrentField.X][CurrentField.Y] = -1;
				AddToFloodFillQueue(Queue, CurrentField.Add(Vector2Int.Up));
				AddToFloodFillQueue(Queue, CurrentField.Add(Vector2Int.Down));
				AddToFloodFillQueue(Queue, CurrentField.Add(Vector2Int.Left));
				AddToFloodFillQueue(Queue, CurrentField.Add(Vector2Int.Right));	
			}
			
		}
	}
	
	private void AddToFloodFillQueue(Queue<Vector2Int> Queue, Vector2Int Field)
	{
		if((Field.X >= 0) && (Field.X < MAP_SIZE) && (Field.Y >= 0) && (Field.Y < MAP_SIZE))
		{
			if(map[Field.X][Field.Y] != -1)
			{
				Queue.add(Field);
			}
		}
	}
	
	public void PrintMap()
	{
		String zeile = "";
		for (int i = 0; i < MAP_SIZE; i++) {
			zeile = "";
			for (int j = 0; j < MAP_SIZE; j++) {
				if(map[i][j] < 0) {
					zeile += " -1";
				} else {
					zeile += "  0";
				}
			}
			System.out.println(zeile);
		}
	}
}
