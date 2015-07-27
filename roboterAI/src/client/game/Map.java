package client.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import client.utils.Vector2Float;
import client.utils.Vector2Int;
import lenz.htw.zaip.net.NetworkClient;

public class Map {
	
	final int MAP_SIZE = 32;
	
	private int[][] Map = new int[MAP_SIZE][MAP_SIZE];
	private NetworkClient gameSocket;
	private boolean Verbose;
	
	public Map(NetworkClient gameSocket, boolean Verbose) {
		this.gameSocket = gameSocket;
		this.Verbose = Verbose;
	}
	
	public void Setup()
	{
		SetupMap();
	}
	
	private void SetupMap()
	{
		InitMap();
		FloodFillMap(GetMapFromServer());
	}
	
	private void InitMap()
	{
		for (int i = 0; i < MAP_SIZE; i++) {
			for (int j = 0; j < MAP_SIZE; j++) {
				Map[i][j] = -1;
			}
		}
	}
	
	private int[][] GetMapFromServer()
	{
		int[][] ServerMap = new int[MAP_SIZE][MAP_SIZE];
		for (int i = 0; i < MAP_SIZE; i++) {
			for (int j = 0; j < MAP_SIZE; j++) {
				ServerMap[i][j] = gameSocket.getBoard(i, j);
			}
		}
		
		return ServerMap;
	}
	
	private void FloodFillMap(int[][] ServerMap)
	{
		Queue<Vector2Int> Queue = new LinkedList<Vector2Int>();
		Vector2Int ValidStart = GetValidStart(ServerMap);
		Vector2Int CurrentField;
		
		Queue.add(ValidStart);
		
		while((CurrentField = Queue.poll()) != null)
		{
			if(NotFilled(ServerMap, CurrentField))
			{	
				Map[CurrentField.X][CurrentField.Y] = 0;
				AddToFloodFillQueue(Queue, ServerMap, CurrentField.Add(Vector2Int.Up));
				AddToFloodFillQueue(Queue, ServerMap, CurrentField.Add(Vector2Int.Down));
				AddToFloodFillQueue(Queue, ServerMap, CurrentField.Add(Vector2Int.Left));
				AddToFloodFillQueue(Queue, ServerMap, CurrentField.Add(Vector2Int.Right));	
			}
		}
	}
	
	private Vector2Int GetValidStart(int[][] ServerMap)
	{
		Vector2Int Middle = new Vector2Int(MAP_SIZE/2, MAP_SIZE/2);
		Vector2Int CurrentField = Middle;
		
		while(ServerMap[CurrentField.X][CurrentField.Y] != 0)
		{
			CurrentField = CurrentField.Add(Vector2Int.Up);
		}
		
		return CurrentField;
	}
	
	private boolean NotFilled(int[][] ServerMap, Vector2Int Field)
	{
		return (ServerMap[Field.X][Field.Y] != -1) && (Map[Field.X][Field.Y] == -1);
	}
	
	private void AddToFloodFillQueue(Queue<Vector2Int> Queue, int[][] ServerMap, Vector2Int Field)
	{
		if((Field.X >= 0) && (Field.X < MAP_SIZE) && (Field.Y >= 0) && (Field.Y < MAP_SIZE))
		{
			if(NotFilled(ServerMap, Field))
			{
				Queue.add(Field);
			}
		}
	}
	
	public Vector2Float GetPlayerPosition(int PlayerID, int BotID)
	{
		Vector2Float Position = new Vector2Float(gameSocket.getX(PlayerID, BotID), gameSocket.getY(PlayerID, BotID));
		Position.AddInPlace(new Vector2Float(1,1));
		Position.MultiplyInPlace(0.5f);
		Position.MultiplyInPlace(MAP_SIZE);
		
		return Position;
	}
	
	public Vector2Float GetRandomWalkablePoint(Random Random)
	{
		Vector2Float NewDestination = new Vector2Float();
		
		do
		{
			NewDestination.X = Random.nextFloat() * (MAP_SIZE - 1);
			NewDestination.Y = Random.nextFloat() * (MAP_SIZE - 1);
		} while(!IsWalkable(NewDestination));
		
		return NewDestination;
	}
	
	public boolean IsWalkable(Vector2Float Position)
	{
		return IsWalkable(new Vector2Int(Position));
	}
	
	public boolean IsWalkable(Vector2Int Position)
	{
		return (Map[Position.X][Position.Y] != -1);
	}
	
	public boolean IsMyColor(Vector2Int Position)
	{
		return (gameSocket.getBoard(Position.X, Position.Y) == (gameSocket.getMyPlayerNumber() + 1));
	}
	
	public void PrintMap()
	{
		PrintMap(Map);
	}
	
	private void PrintMap(int[][] Map)
	{
		String zeile = "    ";
		for (int i = 0; i < MAP_SIZE; i++) {
			zeile += " " + String.format("%2d", i);
		}
		System.out.println(zeile);
		
		for (int i = MAP_SIZE - 1; i >= 0; i--) {
			zeile = String.format("%2d", i) + " |";
			for (int j = 0; j < MAP_SIZE; j++) {
				if(Map[j][i] < 0) {
					zeile += " -1";
				} else {
					zeile += "  0";
				}
			}
			zeile += "| " + String.format("%2d", i);
			System.out.println(zeile);
		}
		
		zeile = "    ";
		for (int i = 0; i < MAP_SIZE; i++) {
			zeile += " " + String.format("%2d", i);
		}
		System.out.println(zeile);
	}
}
