/**
 * File Name: ChatRunnable.java 
 * Author: Joshua Whiting, 040912687 
 * Course: CST8221 - JAP, 
 * Lab Section: 312 Assignment: 2, Part 2 
 * Date: Dec 5th, 2019 
 * Professor: Daniel Cormier 
 * Purpose: 
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;
import javax.swing.JTextArea;
/**
 * Handles the chat session elements
 * 
 * @author Joshua Whiting
 * @version 1
 * @see ChatRunnable
 * @since 1.8.0
 *
 */
public class ChatRunnable<T extends JFrame & Accessible> implements Runnable {

	private final T ui;
	private final Socket socket;
	private final ObjectInputStream inputStream;
	private final ObjectOutputStream outputStream;
	private final JTextArea display;

	/**
	 * default constructor sets the duration
	 * 
	 * @param ui
	 *            - The client UI
	 * @param connection
	 * 				- the connection to the server
	 */
	public ChatRunnable(T ui, ConnectionWrapper connection) {
		this.ui = ui;
		socket = connection.getSocket();
		inputStream = connection.getInputStream();
		outputStream = connection.getOutputStream();
		display = ui.getDisplay();
	}
	/**
	 * runs the process handling the session elements
	 * 
	 */
	public void run() {
		String strin = "";

		while (true) {
			if (!socket.isClosed()) {
				try {
					strin = (String) inputStream.readObject();

					LocalDateTime localDate = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, HH:mm a");
					String curTime = formatter.format(localDate);

					if (strin.trim().equals(ChatProtocolConstants.CHAT_TERMINATOR)) {
						final String terminate;
						terminate = ChatProtocolConstants.DISPLACMENT+ curTime+ ChatProtocolConstants.LINE_TERMINATOR+ strin;
						display.append(terminate);
						break;
					} else {
						final String append;
						append = ChatProtocolConstants.DISPLACMENT+ curTime +ChatProtocolConstants.LINE_TERMINATOR+ strin;
			            display.append(append); 
					}

				} catch (IOException | ClassNotFoundException e) {

					e.printStackTrace();
					break;
				}
			} else {
				break;
			}
		}
		if(!socket.isClosed()) {
		       try {
				outputStream.writeBytes(ChatProtocolConstants.DISPLACMENT+
				           ChatProtocolConstants.CHAT_TERMINATOR+
				           ChatProtocolConstants.LINE_TERMINATOR);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ui.closeChat();
	}

}
