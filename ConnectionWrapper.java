import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
/**
 * File Name: ConnectionWrapper.java 
 * Author: Joshua Whiting, 040913631
 * Course: CST8221 - JAP, 
 * Lab Section: 311 
 * Assignment: 2, Part 2 Date: Dec 6th, 2019 
 * Professor: Daniel Cormier 
 * Purpose: handles input and output
 * Class list: ConnectionWrapper
 */



/**
* The ConnectionWrapper class handles the input and output streams
* 
* @author Joshua Whiting
* @version 1.0r
* @since 1.8.0_1
*/
public class ConnectionWrapper {

	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private Socket socket;
/**Default Constructor, creates connection wrapper
 * @param Socket, port
 */
	public ConnectionWrapper(Socket socket) {
		this.socket = socket;
	}
/**
 * returns socket
 * @return Socket
 */
	public Socket getSocket() {
		return socket;
	}
/**
 * returns output stream
 * @return outputStream
 */
	public ObjectOutputStream getOutputStream() {
		return outputStream;

	}
/**
 * Returns input stream
 * @return inputStream
 */
	public ObjectInputStream getInputStream() {
		return inputStream;

	}
/**
 * creates Input stream object
 * @return inputStream
 * @throws IOException
 */
	ObjectInputStream createObjectIStreams() throws IOException {
		inputStream = new ObjectInputStream(socket.getInputStream());
		return inputStream;
	}
/**
 * creates Output Stream object
 * @return outputStream
 * @throws IOException
 */
	ObjectOutputStream createObjectOStreams() throws IOException {
		outputStream = new ObjectOutputStream(socket.getOutputStream());
		return outputStream;

	}
/**
 * Creates both input and output objects
 * @throws IOException
 */
	void createStreams() throws IOException {
		createObjectOStreams();
		createObjectIStreams();
	}
/**
 * closes the connections
 * @throws IOException
 */
	public void closeConnection() throws IOException {
		if (inputStream != null && !socket.isClosed())
			inputStream.close();

		if (socket != null && !socket.isClosed() && outputStream != null)
			outputStream.close();

		if (socket != null && !socket.isClosed())
			socket.close();
		
		outputStream = null;
		inputStream = null;
		socket = null;
	}

}
