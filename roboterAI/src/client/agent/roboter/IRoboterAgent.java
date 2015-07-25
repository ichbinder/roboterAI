package client.agent.roboter;

import lenz.htw.zaip.net.NetworkClient;
import client.game.Map;
import client.utils.Vector2Float;

public interface IRoboterAgent {
	static enum RoboterType { Normal, BigOne, Plotter };
	
	void Setup(NetworkClient GameSocket, Map Map, int RoboterID);
	void GoTo(Vector2Float Destination);
	boolean IsIdle();
	void Tick();
	RoboterType GetRoboterType();

}
