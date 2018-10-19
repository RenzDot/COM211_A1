/*************************************
 * 	Filename:  HttpInteract.java
 * 	Name: 			Lorenzo Jumilla
 		Student-ID: 201202403
 *  Name:				Daniel Stevens
 		Student-ID:	201270161
 * 	Date:				16/10/18
 *************************************/

import java.net.*;
import java.io.*;
import java.util.*;

/*
 * Class for downloading one object from http server.
 */

/* 
The EmailClient constructs a HttpInteract class object to hold the request message.
The EmailClient sends the request using the function HttpInteract.send().
This function returns a String containing the requested object, or an error message, if one occured.
The EmailClient updates the message field to the returned String.
*/

public class HttpInteract {
	private String hostname;
	private String pathname;
	private String requestMessage;


	private static final int HTTP_PORT = 80;
	private static final String CRLF = "\r\n";
	private static final int BUF_SIZE = 4096;
	private static final int MAX_OBJECT_SIZE = 102400;

 	/* Create a HttpInteract object. */
	public HttpInteract(String url) {
		
		/* 
		Split url into pathname & hostname
		If URL is only hostname, use "/" for path
		*/
		
		//Splits URL into hostname & pathname
		if (url.indexOf('/') > (-1)	) {
			String[] arrOfUrl = url.split("/", 0);
			//Host Name
			hostname = (arrOfUrl[0]);
			//Path Name
			pathname = (arrOfUrl[1]);
			
			for (int i = 2; i < arrOfUrl.length; i++) {
				pathname += "/" + arrOfUrl[i];
			};
			
		} else {
			hostname = url;
			pathname = "";
			
		};
		
		/*
		Construct requestMessage
		Add a header line
		*/
		//We pass this into the send function
		/*
		GET /~gairing/test.txt HTTP/1.1
		Host: cgi.csc.liv.ac.uk
		*/
		requestMessage = ("GET " + "/" + pathname + " HTTP/1.1\r\n" +
						  "Host: " + hostname + CRLF + CRLF);
		
		System.out.print(requestMessage);
		
		//Ensure server closes connection after one response.
		/*if (response.startsWith("220")) {
			connection.close();
		};*/
		
		return;
	}


	/* 	Send Http request
		parse response and return requested object as a String (if no errors),
	 	otherwise return meaningful error message.
	 	Don't catch Exceptions. EmailClient will handle them. 
	*/
	
	/*
	Open a TCP connection ./
	Assign input and output streams to the connection ./
	Request the object by sending the requestMessage into the OuputStream ./
	Read the status line from the InputStream and extract the status code ./
	If the request was successful (status code = 200)  ./
	*/

	public String send() throws IOException {

		char[] buf = new char[BUF_SIZE];		//Buffer to read in 4kB chunks
		char[] body = new char[MAX_OBJECT_SIZE];//Max size of object
		
		String[] tmp;
		String headers = "";
		int bodyLength = (-1);
		int bytesRead = 0;
		
		String statusLine = "";
		int status;				//Status code
			
		Socket connection;		//Socket to server

		//Streams for reading from and writing to socket
		BufferedReader fromServer;
		DataOutputStream toServer;

		System.out.println("Connecting server: " + hostname + CRLF);

		//Connect to HTTP server on port 80
		connection = new Socket(hostname, HTTP_PORT);
		
		//Assign input and output streams to connection
		InputStream is = connection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		fromServer = new BufferedReader(isr);
		
		toServer = new DataOutputStream(	connection.getOutputStream()	);
		System.out.println("Send request:\n" + requestMessage);
		
		//Send requestMessage to http server
		toServer.writeBytes(requestMessage);

		//Read the status line from response message
		statusLine = fromServer.readLine();
		System.out.println("Status Line:\n" + statusLine + CRLF);

	
		/*
		Read the headers ./
		Extract the length of the body from "Content-Length:" ./
		Read status line using BufferedReader readLine() ./
		Read header line using BufferedReader readLine() ./
		*/
		
		//Extract status code
		status = Integer.valueOf(statusLine.substring(9, 12));
		
		//Check status code
		if (status != 200){
			connection.close();
			return("Error, connection closed");
			
		} else {
			String response = fromServer.readLine();
			System.out.println(response);
			
			//Read Headers
			while (response.length() > 0) {
				headers += response + CRLF;
				
				//Read Content-Length
				if (	(response.toLowerCase()).contains("content-length:")	) {
					String[] responseArray = response.split(" ");
					bodyLength = Integer.valueOf(responseArray[1]);//Assign bodylength
				
				};
				
				//System.out.println("Content length is: " + bodyLength);
				
				response = fromServer.readLine();
				
			};
			
			System.out.println("Headers:\n" + headers + CRLF);
			
			//Close connection when object is larger than MAX_OBJECT_SIZE
			if (bodyLength > MAX_OBJECT_SIZE) {
				connection.close();
				return("Requested object is too large\nBody Length = " + bodyLength);
			
			//Read Body
			} else {
				/*int bodyPos = 0;
				bytesRead = fromServer.read(buf, 0, BUF_SIZE);
				for (char c : buf) {
					body[bodyPos] = c;
					bodyPos++;
					if (c != 0) {
						System.out.println(body[bodyPos]);
					};
				};
				
				//Read body in chunks
				System.out.println(bodyLength + " , " + bytesRead);
				System.out.println((bodyLength < bytesRead) + " && " + (bytesRead > (-1)));
				while (	bodyLength < bytesRead && bytesRead > (-1)	) {
					
					//Copy buffered chunks into body
					for (char c : buf) {
						body[bodyPos] = c;
						bodyPos++;
						if (c != 0) {
							System.out.println(body[bodyPos]);
						}
					};
					
					//Reset buffer
					buf = new char[BUF_SIZE];
					bytesRead = fromServer.read(buf, 0, BUF_SIZE);
					
				};*/
				int bodyPos = 0;
				
				do {
					buf = new char[BUF_SIZE];						//Empty Buffer
					bytesRead = fromServer.read(buf, 0, BUF_SIZE);	//Place object in buffer
					
					//Copy buffer to body
					for (char c : buf) {
						if (c != 0) {			//Add char to body if not null
							body[bodyPos] = c;
							bodyPos++;
						};
					};
					
				} while (	bodyLength < bytesRead && bytesRead > (-1)	);
				
			};
			
			
		};
		
		System.out.println("Done reading file. Closing connection.");
		connection.close();
		if (bytesRead > 0) {
			return(new String(body, 0, bytesRead));
		} else {
			return("Error");
		}
		
	}
}
