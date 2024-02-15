import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
/**
 * File Name: ServerChatUI.java 
 * Author: Joshua Whiting, 040913631
 * Course: CST8221 - JAP, 
 * Lab Section: 311 
 * Assignment: 2, Part 2 Date: Dec 6th, 2019 
 * Professor: Daniel Cormier 
 * Purpose: Server's GUI and controls the buttons
 * Class list: ServerChatUI,Controller,WindowControllor
 */

/**
* The ServerChatUI creates the GUI
* 
* @author Joshua Whiting
* @version 1.0
* @see calculator
* @since 1.8.0_1
*/
public class ServerChatUI extends JFrame implements Accessible {
/**
 * {@value #serialVersionUID} serialversionUID
 */
	private static final long serialVersionUID = -4201023477458632984L;
	private Socket socket;
	private JTextArea chatDisplay;
	private JTextField messText;
	private JButton sendButton;
	private ObjectOutputStream outputStream;
	private ConnectionWrapper connection;

	public ServerChatUI(Socket socket) {
		this.socket = socket;
		setFrame(createUI());
		runClient();
	}
/**
 * Creates the programs GUI
 * @return JPanel, main Jpanel to create GUI
 */
	public JPanel createUI() { // creates GUI
		JPanel main = new JPanel(new BorderLayout());
		JPanel message = new JPanel(new FlowLayout(FlowLayout.LEADING));
		JPanel chat = new JPanel(new BorderLayout());
		Controller controller = new Controller();

		message.setPreferredSize(new Dimension(580, 60));
		chat.setPreferredSize(new Dimension(580, 412));

		message.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 10), "MESSAGE"));
		chat.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE, 10), "CHAT DISPLAY",
				TitledBorder.CENTER, TitledBorder.CENTER));

		messText = new JTextField(); // text box for send button
		messText.setBackground(Color.WHITE);
		messText.setPreferredSize(new Dimension(461, 20));
		messText.setText("Type message");

		sendButton = new JButton("Send");
		sendButton.setPreferredSize(new Dimension(80, 20));
		sendButton.setEnabled(true);
		sendButton.setMnemonic(KeyEvent.VK_S);
		sendButton.addActionListener(controller);
		message.add(messText);
		message.add(sendButton);

		chatDisplay = new JTextArea(); //big text box for messages
		chatDisplay.setPreferredSize(new Dimension(chatDisplay.getPreferredSize().width, 480));
		chatDisplay.setBackground(Color.WHITE);
		chatDisplay.setEditable(false);
		chat.add(chatDisplay);

		JScrollPane scroll = new JScrollPane(chatDisplay);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		chat.add(scroll);

		main.add(message, BorderLayout.PAGE_START);
		main.add(chat, BorderLayout.PAGE_END);
		;
		return main;
	}
/**
 * sets the created gui to the frame
 * @param panel, adds the main panel to create the gui
 */
	public final void setFrame(JPanel panel) {
		setContentPane(panel);
		addWindowListener(new WindowController());
	}
/**
 * Creates a connection from the client
 */
	private void runClient() {
		connection = new ConnectionWrapper(socket);

		try {
			connection.createStreams();
			outputStream = connection.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Runnable run = new ChatRunnable<ServerChatUI>(this, connection);
		Thread thread = new Thread(run);
		thread.start();
	}
/** returns the text display
 * @return JTextArea
 */
	public JTextArea getDisplay() {
		return chatDisplay;
	}
/**
 * closeChat, closes the chat
 */
	public void closeChat() {
		try {
			connection.closeConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		dispose();
	}

	private class WindowController extends WindowAdapter {
		public void windowClosing(WindowEvent event) {

			super.windowClosing(event);
			System.out.println("ServerUI Window closing!");
			try {
				outputStream.writeObject(ChatProtocolConstants.DISPLACMENT + ChatProtocolConstants.CHAT_TERMINATOR
						+ ChatProtocolConstants.LINE_TERMINATOR);
				System.out.println("Closing Chat!");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				dispose();
			}
			System.out.println("Chat closed!");
			System.exit(0);
		}

		public void windowClosed(WindowEvent event) {
			super.windowClosed(event);
			System.out.println("Server UI Closed!");
		}
	}
/**
 * The Controller class controls all of the buttons action events
 * @author Josh
 *
 */
	private class Controller implements ActionListener {
/**When a button is pressed, it comes here to do the next task
 * @param ActionEvent
 */
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == sendButton) {
				send();
			}
		}
/**
 * sends the message into the text field
 */
		private void send() {
			String sendMessage = messText.getText();
			getDisplay().append(sendMessage + ChatProtocolConstants.LINE_TERMINATOR);

			try {
				outputStream.writeObject(
						ChatProtocolConstants.DISPLACMENT + sendMessage + ChatProtocolConstants.LINE_TERMINATOR);
			} catch (IOException e) {
				getDisplay().append(e.getMessage());
			}
		}

	}
}
