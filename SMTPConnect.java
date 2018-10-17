/*************************************
 * 	Filename:  HttpInteract.java
 * 	Name: 			Lorenzo Jumilla
 		Student-ID:
 *  Name:				Daniel Stevens
 		Student-ID:	201270161
 * 	Date:				16/10/18
 *************************************/
import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Open SMTP connection to mailserver & send one mail.
 */

 public class SMTPConnect {

	private Socket connection;//Socket to server
	private String response;

	//Read & Write streams
	private BufferedReader fromServer;
	private DataOutputStream toServer;

	private boolean isConnected = false;		//Checks connection, used in close()
	private static final String CRLF = "\r\n";	//Used to separate commands

	public SMTPConnect(EmailMessage mailmessage) throws IOException {
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

		//Name of client's computer
		String localHost = InetAddress.getLocalHost().getHostName();

		//Send handshake
		sendCommand("HELO " + localHost, 250);

		//test(); // Debug only, remove before submitting project

		isConnected = true;
	}

	//Debug only, remove before submitting project
	public void test() throws IOException {
		sendCommand("HELO localHost", 250);
		sendCommand("MAIL FROM: <a@a>", 250);
		sendCommand("RCPT TO: <b@b>", 250);
		sendCommand("DATA", 354);
		sendCommand("Hi Bob, How's the weather? Alice." + CRLF + ".", 250);
		sendCommand("QUIT", 221);

	}

	//Send an SMTP email
	public void send(EmailMessage mailmessage) throws IOException {
		sendCommand("MAIL FROM: " + mailmessage.Sender, 250);
		sendCommand("RCPT TO: " + mailmessage.Recipient, 250);
		sendCommand("DATA", 354);
		/*sendCommand(mailmessage.toString() + CRLF
					 + ".", 250);*/
		sendCommand( mailmessage.Headers + mailmessage.Body + CRLF
					 + ".", 250);

		/* Example Format
		MAIL FROM:	sender@a.com
		RCPT TO:	receiver@b.com
		DATA
		Hi, How are you?
		.
		QUIT
		*/


    }


	//Quit SMTP connection
    public void close() {
		isConnected = false;
		try {
			sendCommand("QUIT", 221);	//Close SMTP connection
			connection.close();			//Close socket

		} catch (IOException e) {
			System.out.println("Unable to close connection: " + e);
			isConnected = true;

		}
    }



	//sendCommand (<command to send>, <expected response number>)
	//Sends SMTP commands to server
	private void sendCommand(String command, int rc) throws IOException {
		toServer.writeBytes(command + CRLF);	//Write command to server

		//Read server's response
		response = fromServer.readLine();
		System.out.println(command + "	--> " + response);
		if (!response.startsWith("" + rc)	) {	//Check if response followed RFC 821
			throw new IOException(rc + " response not received from server");
		}

		/* RFC 821
			DATA		354
			HELO		250
			MAIL FROM	250
			QUIT		221
			RCPT TO		250
		*/
    }


	//Abort connection if something bad happens
	/*protected void finalize() throws Throwable {
		if ( isConnected ) {
			close();
		};

		super.finalize();
    }*/


}
