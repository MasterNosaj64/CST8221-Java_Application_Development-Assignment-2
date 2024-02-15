import javax.swing.JTextArea;
/**
 * File Name: Accessible.java 
 * Author: Joshua Whiting, 040913631
 * Course: CST8221 - JAP, 
 * Lab Section: 311 
 * Assignment: 2, Part 2 Date: Dec 6th, 2019 
 * Professor: Daniel Cormier 
 * Purpose: Server and Client's interface
 * Class list: 
 */

/**
* Interface for server and client  gui
* 
* @author Joshua Whiting
* @version 1.0
* @since 1.8.0_1
*/
public interface Accessible {

	
	/**
	 * Servers and  clients chat box
	 * @return JTextArea
	 */
	public JTextArea getDisplay();
/**
 * Closes server or clients gui
 */
	public void closeChat();
		
	
}
