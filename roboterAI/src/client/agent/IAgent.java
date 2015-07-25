package client.agent;

import client.game.Map;
import lenz.htw.zaip.net.NetworkClient;

public interface IAgent {
	void Setup(NetworkClient GameSocket, Map Map);
	void Tick();
}
