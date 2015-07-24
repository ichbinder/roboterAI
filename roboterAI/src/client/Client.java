package client;

import lenz.htw.zaip.net.NetworkClient;
import client.agent.IAgent;
import client.game.Map;

public class Client implements IClient, Runnable {
	
	private String Name = "";
	private String Server = null;
	private NetworkClient GameSocket = null;
	private Thread ClientThread = null;
	private int PlayerID = 0;
	private Map map;
	
	private IAgent Agent;
	
	private boolean Verbose = false;
	
	public Client(String Name, IAgent Agent, boolean Verbose)
	{
		this(Name, Agent);
		this.Verbose = Verbose;
	}
	
	public Client(String Name, IAgent Agent)
	{
		this.Name = Name;
		this.Agent = Agent;
	}
	
	public void ConnectToLocalhost() {
		Connect("192.168.1.199");
	}

	public void Connect(String Server) {
		this.Server = Server;
		ClientThread = new Thread(this);
		ClientThread.start();
	}
	
	public void run() {
		Setup();
		Loop();
		CleanUp();
	}
	
	private void Setup()
	{
		GameSocket = new NetworkClient(Server, Name);
		PlayerID = GameSocket.getMyPlayerNumber();
		
		Agent.Setup(GameSocket);
		this.map = new Map(GameSocket);
		
		map.Setup();
		
		if(Verbose)
		{
			map.PrintMap();
		}
	}
	
	private void Loop()
	{
		while(GameSocket.isAlive())
		{
			try {
				//Update Everything
				Agent.Tick();
			} catch (Exception e) {
				System.err.println("Error: " + Name);
				e.printStackTrace(System.err);
				break;
			}
		}
	}
	
	private void CleanUp()
	{
		
	}

}
