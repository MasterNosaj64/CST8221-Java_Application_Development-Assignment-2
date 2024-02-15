/**
 * File Name: ClientChatUI.java 
 * Author: Jason Waid, 040912687 
 * Course: CST8221 - JAP, 
 * Lab Section: 311 Assignment: 2, Part 2 
 * Date: Dec 5th, 2019 
 * Professor: Daniel Cormier 
 * Purpose: Creates the Java Client application and handled user interfacing
 */


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;


/**
 * Creates the Java Client application and handled user interfacing
 * 
 * @author Jason Waid
 * @version 2
 * @see ClientChatUI
 * @since 1.8.0
 *
 */
public class ClientChatUI extends JFrame implements Accessible{

	private static final long serialVersionUID = 4825180218508220458L;
	private Controller controller = new Controller();
	
	
	private JPanel mainPanel = new JPanel();
	//holds connectionBox & messageBox
	private JPanel mainTop;
	//holds connectionBoxNORTH & connectionBOXSOUTH
	private JPanel connectionBox;
	//holds hostLabel & hostTxtField
	private JPanel connectionBoxNORTH;
	//holds portLabel, portDropDown & connectButton
	private JPanel connectionBoxSOUTH;
	private JLabel hostLabel;
	private JTextField hostTxtField;
	
	private JLabel portLabel;
	private JComboBox <String> portDropDown;
	private JButton connectButton;
	//holds message & send button
	private JPanel messageBox;
	private JTextField message;
	private JButton sendButton;
	//holds chatDisplay area
	private JPanel chatBox;
	private JTextArea chatDisplay;
	
	
	//New for Part 2 
	private ObjectOutputStream outputStream;
	private Socket socket;
	private ConnectionWrapper connection;
	
	
	/**
	 * default constructor sets the duration
	 * 
	 * @param title
	 *            - set the title
	 */
	public ClientChatUI(String title) {
		setTitle(title);
		runClient();
	}

	/**
	 * Creates Client UI
	 * @return
	 * 		- JFrame
	 */
	JPanel createClientUI() {

		mainPanel.setLayout(new BorderLayout());
		
		/* ************** Connection Area *************************************/
		String[] portNums = { "", "8089", "65000", "65535" };

		
		
		connectionBox = new JPanel();

		connectionBox.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED, 10), "CONNECTION"));

		connectionBoxNORTH = new JPanel();
		connectionBoxSOUTH = new JPanel();

		connectionBoxNORTH.setLayout(new FlowLayout(FlowLayout.LEFT));
		connectionBoxSOUTH.setLayout(new FlowLayout(FlowLayout.LEFT));
		connectionBox.setLayout(new BorderLayout());

		hostLabel = new JLabel();
		hostTxtField = new JTextField();
		hostTxtField.setText("localhost");
		hostTxtField.setColumns(46);
		hostTxtField.setMargin(new Insets(0,5,0,0));

		hostLabel.setText("Host:");
		hostLabel.setSize(new Dimension(35, 30));
		hostLabel.setLabelFor(hostTxtField);
		hostLabel.setDisplayedMnemonic('H');

		portLabel = new JLabel();
		portDropDown = new JComboBox<String>(portNums);
		portDropDown.setBackground(Color.WHITE);
		portDropDown.setEditable(true);

		connectButton = new JButton();

		portLabel.setText("Port:");
		portLabel.setSize(new Dimension(35, 30));
		portLabel.setLabelFor(portDropDown);
		portLabel.setDisplayedMnemonic('P');
		
		connectButton.setText("Connect");
		connectButton.setBackground(Color.RED);
		connectButton.setMnemonic('C');
		connectButton.addActionListener(controller);
		connectButton.setPreferredSize(new Dimension(125,25));

		/* Creation of the connection box */
		connectionBoxNORTH.add(hostLabel);
		connectionBoxNORTH.add(hostTxtField);
		connectionBoxNORTH.setBorder(new EmptyBorder(5, 0, 5, 0));
		connectionBoxSOUTH.add(portLabel);
		connectionBoxSOUTH.add(portDropDown);
		connectionBoxSOUTH.add(connectButton);
		connectionBoxSOUTH.setBorder(new EmptyBorder(5, 0, 5, 0));

		connectionBox.add(connectionBoxNORTH, BorderLayout.NORTH);
		connectionBox.add(connectionBoxSOUTH, BorderLayout.SOUTH);

		/* ************** Message Area *************************************/

		messageBox = new JPanel();
		messageBox.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 10), "MESSAGE"));
		messageBox.setLayout(new FlowLayout(FlowLayout.LEFT));

		message = new JTextField();
		message.setText("Type message");
		message.setColumns(41);

		sendButton = new JButton();
		sendButton.setText("Send");
		sendButton.setMnemonic('S');
		sendButton.setEnabled(false);
		sendButton.setPreferredSize(new Dimension(80,20));
		sendButton.addActionListener(controller);
		
		messageBox.add(message);
		messageBox.add(sendButton);

		/* ************** Chat Display Area *************************************/

		chatBox = new JPanel();
		chatBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE, 10), "CHAT DISPLAY",
				TitledBorder.CENTER, TitledBorder.CENTER));
		
		chatBox.setLayout(new BorderLayout());
		chatDisplay = new JTextArea();
		chatDisplay.setColumns(45);
		chatDisplay.setRows(30);
		chatDisplay.setEditable(false);
		JScrollPane sp = new JScrollPane(chatDisplay);

		chatBox.add(sp);

		/* *********************** Main Pane ***********************************************/

		/* Pane creation for CONNECTION and MESSAGE boxes */
		mainTop = new JPanel();
		mainTop.setLayout(new BorderLayout());
		mainTop.add(connectionBox, BorderLayout.NORTH);
		mainTop.add(messageBox, BorderLayout.SOUTH);

		/* Main Pane setup */
		mainPanel.add(mainTop, BorderLayout.NORTH);
		mainPanel.add(chatBox);
		
		return mainPanel;
	}
	
	/**
	 * Handles all actions taken by the user
	 * 
	 * @author Jason Waid
	 * @version 1
	 * @see Controller
	 * @since 1.8.0
	 *
	 */
	private class Controller implements ActionListener {
		
		/**
		 * handles action performed
		 * @param event
		 *            - the action the user made
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			boolean connected = false;
		
			switch(event.getActionCommand()) {
				
			case "Connect":
				String host = hostTxtField.getText();			
				int port;
				//this will help avoid attempting a connection when a host hasnt been set
				if(host.isEmpty()) {
					return;
				}
				
				try {
				
				port = Integer.parseInt(portDropDown.getSelectedItem().toString());
				}catch(NumberFormatException num) {
					return;
				}
				
				connected = connect(host, port);
				
				if(connected) {
					connectButton.setEnabled(false);
					connectButton.setBackground(Color.BLUE);
					sendButton.setEnabled(true);
					message.requestFocus();
					
					Runnable session = new ChatRunnable<ClientChatUI>(ClientChatUI.this, connection);		
					Thread newThread = new Thread(session);
					newThread.start();
					
				}
				else {
					return;
				}
				break;
				
			case "Send":
					send();
					break;
			default:
				break;
				}
			}
		}

	/**
	 * handles action performed
	 * @param host
	 *            - the host we're connecting to
	 * @param port
	 *            - the port we're connecting through
	 *            
	 * @return
	 * 			- boolean, false on failure, true on success
	 */
		boolean connect(String host, int port) {
			
			try {
				socket = new Socket();
				socket.connect(new InetSocketAddress(InetAddress.getByName(host), port), 60);
			
				if(socket.getSoLinger() != -1) {
					socket.setSoLinger(true, 5);
				}
				if(!socket.getTcpNoDelay()) {
					socket.setTcpNoDelay(true);
				}
				
				chatDisplay.append("Connected to " + socket.toString() + "\n");
				connection = new ConnectionWrapper(socket);
				connection.createStreams();
				outputStream = connection.getOutputStream();
				
			} catch (IOException e) {
				
				chatDisplay.append(e.toString() + "\n");
				return false;
				}
			
			return true;
		}
		/**
		 * send user message to server
		 * 
		 */
		void send(){
			
			String sendMessage = message.getText();
			chatDisplay.append(sendMessage + "\n");
			
			try {
				outputStream.writeObject(ChatProtocolConstants.DISPLACMENT
				+ sendMessage
				+ ChatProtocolConstants.LINE_TERMINATOR
						);
			} catch (IOException e) {

				chatDisplay.append(e.toString() + "\n");
			}
			
			return;
		}
		
		/**
		 * handles window actions, such as closing the window
		 * 
		 * @author Jason Waid
		 * @version 1
		 * @see WindowController
		 * @since 1.8.0
		 *
		 */
	class WindowController extends WindowAdapter {
		void windowClosing() {
			
			try {
				outputStream.writeObject(ChatProtocolConstants.CHAT_TERMINATOR);
			} catch (IOException e) {
				System.exit(0);
			}
			
			System.exit(0);
		}
	}
	/**
	 * runs the client
	 * 
	 */
	private void runClient() {
		
		addWindowListener(new WindowController());
		setContentPane(createClientUI());
	
	}
	/**
	 * gets chat display
	 * 
	 * @return
	 * 			-chatDisplay in the clientUI
	 */
	public JTextArea getDisplay() {
		return chatDisplay;
		
	}
	
	/**
	 * closes chat session so user can start another one
	 * 
	 */
	public void closeChat(){

		
		if(!socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException e) {
				
				chatDisplay.append(e.toString() + "\n");
			}
			
		}
		
		enableConnectButton();
		
	}
	/**
	 * enables connect button what a session isn't active
	 * 
	 */
	void enableConnectButton() {

		connectButton.setEnabled(true);
		connectButton.setBackground(Color.RED);	
		sendButton.setEnabled(false);
		hostTxtField.requestFocus(true);
	}
	
	
}
