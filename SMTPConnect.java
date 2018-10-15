/*************************************
 * Filename: SMTPConnect.java
 * Names:
 * Student-IDs:
 * Date:
 *************************************/
import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Open SMTP connection to mailserver & send one mail.
 *
 */

 /*
 The first task is to program the SMTP interaction between the MUA and the local SMTP
 server. The client provides a graphical user interface containing fields for
 entering the sender and recipient addresses, the subject of the message and the
 message itself.
 */
 
 public class SMTPConnect {
	
	private Socket connection;//Socket to server
	
	//Read & Write streams
	private BufferedReader fromServer;
	private DataOutputStream toServer;
	
	private boolean isConnected = false;// Used in close()
	private static final String CRLF = "\r\n";
	/*
	Create SMTPConnect object 
	Create socket
	Associate streams
	Initialize SMTP connection	
	*/
	
	public SMTPConnect(EmailMessage mailmessage) throws IOException {
		
		//Open TCP client socket
		connection = new Socket(mailmessage.DestHost, mailmessage.DestHostPort);
		
		//Allow fromServer to read socket
		InputStream is = connection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);//Decodes bytes into char
		fromServer = new BufferedReader(isr);

		//Allow toServer to write to socket
		toServer = new DataOutputStream(	connection.getOutputStream() );
		
		//Check server's reply is 220
		String reply = fromServer.readLine();
		System.out.println("1)" + reply);
		if (!reply.startsWith("220")) {
			throw new IOException("220 reply not received from server.");
		}
		
		String localHost = InetAddress.getLocalHost().getHostName();
		//sendCommand("DATA", );
		
		isConnected = true;
	}
	

	
	public void send(EmailMessage mailmessage) throws IOException {
		
		
    }
	
	
	//Close SMTP connection
    public void close() {
	
    }
	
	
	private void sendCommand(String command, int rc) throws IOException {
	
	
    }

	
 }