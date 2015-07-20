package client.agent;

import lenz.htw.zaip.net.NetworkClient;

public interface IAgent {
	void Setup(NetworkClient GameSocket);
	void Tick();
}
