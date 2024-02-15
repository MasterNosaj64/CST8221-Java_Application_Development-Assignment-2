import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;


/**
 * File Name: Client.java 
 * Author: Jason Waid, 040912687 
 * Course: CST8221 - JAP,
 * Lab Section: 311 Assignment: 2, Part 2 
 * Date: Dec 5th, 2019 
 * Professor: Daniel Cormier 
 * Purpose: launches the application. Class list: ClientChatUI, Client
 */
public class Client {

	public static void main(String[] args) {

		
		ClientChatUI chatUI = new ClientChatUI("Jason's ClientChatUI");
		
		
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				chatUI.setSize(new Dimension(588, 500));
				chatUI.setResizable(false);
				chatUI.setLocationByPlatform(true);
				chatUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				chatUI.setVisible(true);
			}
		});
	}
}