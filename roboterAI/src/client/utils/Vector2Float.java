package client.utils;

public class Vector2Float {
	
	public static Vector2Float Up = new Vector2Float(0,1);
	public static Vector2Float Down = new Vector2Float(0,-1);
	public static Vector2Float Left = new Vector2Float(-1,0);
	public static Vector2Float Right = new Vector2Float(1,0);
	
	public float X;
	public float Y;
	
	public Vector2Float()
	{
		this(0, 0);
	}
	
	public Vector2Float(float X, float Y)
	{
		this.X = X;
		this.Y = Y;
	}

	public Vector2Float(Vector2Float other)
	{
		this.X = other.X;
		this.Y = other.Y;
	}
	
	public Vector2Float(Vector2Int other)
	{
		this.X = (float)other.X;
		this.Y = (float)other.Y;
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
		
		Vector2Float otherVector = (Vector2Float)other;
		
		return ((this.X == otherVector.X) && (this.Y == otherVector.Y));
	}

	
	public Vector2Float Add(Vector2Float other)
	{
		return new Vector2Float(this.X + other.X, this.Y + other.Y);
	}

	public Vector2Float AddInPlace(Vector2Float other)
	{
		this.X += other.X;
		this.Y += other.Y;
		return this;
	}
	
	public Vector2Float Subtract(Vector2Float other)
	{
		return new Vector2Float(this.X - other.X, this.Y - other.Y);
	}
	
	public Vector2Float SubtractInPlace(Vector2Float other)
	{
		this.X -= other.X;
		this.Y -= other.Y;
		return this;
	}
	
	public Vector2Float Multiply(float Factor)
	{
		return new Vector2Float(this.X * Factor, this.Y * Factor);
	}

	public Vector2Float MultiplyInPlace(float Factor)
	{
		this.X *= Factor;
		this.Y *= Factor;
		return this;
	}
	
	public float Length()
	{
		return (float) Math.sqrt(X*X + Y*Y);
	}
	
	public String toString()
	{
		return "(" + X + ", " + Y + ")";
	}
	
	public Vector2Float Normalize()
	{
		this.MultiplyInPlace(1/this.Length());
		
		return this;
	}

	public Vector2Float RotateRight()
	{
		float tmp = this.X;
		this.X = this.Y;
		this.Y = -tmp;

		return this;
	}
	
	public Vector2Float RightRotation()
	{
		return new Vector2Float(this.Y, -this.X);
	}

	public Vector2Float RotateLeft()
	{
		float tmp = this.Y;
		this.Y = this.X;
		this.X = -tmp;

		return this;
	}

	public Vector2Float LeftRotation()
	{
		return new Vector2Float(-this.Y, this.X);
	}
}
