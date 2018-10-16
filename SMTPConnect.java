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
	private String response;
	private BufferedReader fromServer;
	private DataOutputStream toServer;
	
	private boolean isConnected = false;// Used in close()
	private static final String CRLF = "\r\n";
	
	
	public SMTPConnect(EmailMessage mailmessage) throws IOException {
		
		/*
		Create SMTPConnect object ./
		Create socket ./
		Associate streams ./
		Initialize SMTP connection ./
		*/
		
		//Open TCP client socket
		connection = new Socket(mailmessage.DestHost, mailmessage.DestHostPort);
		
		//Allow fromServer to read socket
		InputStream is = connection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);//Decodes bytes into char
		fromServer = new BufferedReader(isr);

		//Allow toServer to write to socket
		toServer = new DataOutputStream(	connection.getOutputStream() );
		
		//Check server's response is 220
		response = fromServer.readLine();
		System.out.println("1)" + response);
		if (!response.startsWith("220")) {
			throw new IOException("220 response not received from server.");
		}
		
		String localHost = InetAddress.getLocalHost().getHostName();//Name of client's computer
		sendCommand("HELO " + localHost);
		test(); // Remove before submitting project

		
		isConnected = true;
	}
	
	public void test() throws IOException {
		sendCommand("HELO localHost", 250);
		sendCommand("MAIL FROM: <a@a>", 250);
		sendCommand("RCPT TO: <b@b>", 250);
		sendCommand("DATA", 354);
		sendCommand("Hi Bob, How's the weather? Alice." + CRLF + ".", 250);
		sendCommand("QUIT", 221);
		
	}
	
	/*
	Send SMTP commands in order
	Call sendCommand
	*/
	public void send(EmailMessage mailmessage) throws IOException {
		
		
    }
	
	
	//Close SMTP connection
    public void close() {
	
    }
	
	
	/*
	Send SMTP command to server
	Check reply follows RFC 821
	*/
	private void sendCommand(String command, int rc) throws IOException {
		
		toServer.writeBytes(command + CRLF);//Write command to server
		
		System.out.print("Client: " + command);
		
		//Read server reply
		response = fromServer.readLine();
		System.out.println("Server: " + response);
		if (!response.startsWith("" + rc)) {// Check server response is equal to rc
			throw new IOException(rc + " response not received from server");
		}
		
		//rc reply codes
		/*
		DATA	354
		HELO	250
		MAIL FROM	250
		QUIT	221
		RCPT TO	250
		*/
		
		
    }
	
	
}