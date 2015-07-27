package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import client.Client;
import client.IClient;
import client.agent.IAgent;
import client.agent.RandomAgent;
import client.agent.SteadyAgent;
import client.agent.TacticsAgent;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

public class CmdTest
{

	public static void main(String[] args) throws IOException
	{
		String text;
		List<String> arge = null;
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		
		do
		{
			arge = new ArrayList<String>();
			System.out.println("Eingabe :");
			text = input.readLine();
			Collections.addAll(arge, text.split(" "));
			
			if (arge.get(0).equals("client")) {
				ClientOptions opt = new ClientOptions();
				JCommander cmd = CmdTest.cmdPaser(arge, opt);
				if (opt.getStart() == true) {
					IClient NewClient = null;
					NewClient = new Client(opt.getClientName(), new TacticsAgent(), true);
					NewClient.Connect(opt.getGameServerIP());
					System.out.println("Client Starded!");
				} if (opt.getHelp() == true)
					cmd.usage();
			} else if (arge.get(0).equals("server")) {
				ServerOptions so = new ServerOptions();
				JCommander cmd = CmdTest.cmdPaser(arge, so);
				if (so.getStart() == true) {
					// to do ...
					System.out.println("Server Starded!");
				}	if (so.getHelp() == true)
					cmd.usage();
			} else if(arge.get(0).equals("test"))
			{
				ClientOptions opt = new ClientOptions();
				JCommander cmd = CmdTest.cmdPaser(arge, opt);
				
				IClient NewClient = null;
				
				NewClient = new Client("Tactics 1", new TacticsAgent(), true);
				NewClient.ConnectToLocalhost();
				NewClient = new Client("Random 1", new RandomAgent());
				NewClient.ConnectToLocalhost();
				NewClient = new Client("Random 2", new RandomAgent());
				NewClient.ConnectToLocalhost();
				NewClient = new Client("Random 3", new RandomAgent());
				NewClient.ConnectToLocalhost();
			}
			
		} while (!arge.get(0).equals("end"));
		
	}
	
	static JCommander cmdPaser(List<String> arg, Object opt) {
		JCommander cmd = new JCommander(opt);
		if (arg.size() != 1) {
			arg.remove(0);
			try
			{
				cmd.parse(arg.toArray(new String[arg.size()]));
			} catch (ParameterException ex)
			{
				cmd.usage();
			}
		} else 
			cmd.usage();
		return cmd;
	}
}
