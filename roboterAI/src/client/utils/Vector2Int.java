package client.utils;

public class Vector2Int {
	
	public static Vector2Int Up = new Vector2Int(0,1);
	public static Vector2Int Down = new Vector2Int(0,-1);
	public static Vector2Int Left = new Vector2Int(-1,0);
	public static Vector2Int Right = new Vector2Int(1,0);
	
	public int X;
	public int Y;
	
	public Vector2Int()
	{
		this(0, 0);
	}
	
	public Vector2Int(int X, int Y)
	{
		this.X = X;
		this.Y = Y;
	}
	
	public boolean equals(Object other)
	{
		if(other == this)
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
		
		Vector2Int otherVector = (Vector2Int)other;
		
		return ((this.X == otherVector.X) && (this.Y == otherVector.Y));
	}
	
	public Vector2Int Add(Vector2Int other)
	{
		return new Vector2Int(this.X + other.X, this.Y + other.Y);
	}
}
