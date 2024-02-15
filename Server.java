import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
/**
 * File Name: Server.java 
 * Author: Joshua Whiting, 040913631
 * Course: CST8221 - JAP, 
 * Lab Section: 311 
 * Assignment: 2, Part 2 Date: Dec 6th, 2019 
 * Professor: Daniel Cormier 
 * Purpose: launches the application. 
 * Class list: Server
 */



/**
* The Server class starts the program
* 
* @author Joshua Whiting
* @version 1.0
* @see calculator
* @since 1.8.0_1
*/
public class Server {
/**
 * main() the main program of the server
 * @param args, if a port was selected, use that port, otherwise use default
 */
	public static void main(String[] args) {
		int port = 65535;
		if (args.length > 0) { // checks if any arguments has been passed
			port = Integer.parseInt(args[0]);
			System.out.println("Using port: " + port);
		} else {
			System.out.println("Using default port: " + port); //default port
		}
		
		
		ServerSocket serverSocket = null;
		try { // tries to create server port
			serverSocket = new ServerSocket(port);
			int friend = 0;

			while (true) {
				Socket socket = serverSocket.accept();

				if (socket.getSoLinger() != -1)
					socket.setSoLinger(true, 5);
				if (!socket.getTcpNoDelay())
					socket.setTcpNoDelay(true);

				System.out.printf("Connecting to a client Socket[addr=%s, port=%d, localport=%d]\n",
						socket.getInetAddress(), socket.getPort(), socket.getLocalPort());

				friend++; //how many users are connected
				final String title = "Josh's Friend " + friend;
				launchClient(socket, title);

			}
		} catch (IOException event) {
			System.out.println("Failed to connect to port: " + port);
		}

	}


	/**
	 * The launchClient launches the server.
	 * @author Joshua Whiting
	 * @version 1.0
	 * @param Socket, port of server
	 * @param String, name of the user
	 * @since 1.8.0_1
	 */

	public static void launchClient(Socket port, String title) {
		

		EventQueue.invokeLater(new Runnable() {
			@Override
			
			/**
			 * run(), runs the server and ends later
			 */
			public void run() { // launchs server GUI
				ServerChatUI server = new ServerChatUI(port);
				server.setTitle(title);
				server.setMinimumSize(new Dimension(588, 500));
				server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				server.setVisible(true);
				server.setResizable(false);
				server.setLocationRelativeTo(null);
			}
		});
	}
}
