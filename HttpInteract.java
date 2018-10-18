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
						  "Host: " + hostname + CRLF);
		//System.out.print(requestMessage + "\n" + hostname + "\n" + pathname);
		

		//Ensure server closes connection after one response.
		/*if (response.startsWith("220")) {
			connection.close();
		}*/

		return;
	}


	/* 	Send Http request
		parse response and return requested object as a String (if no errors),
	 	otherwise return meaningful error message.
	 	Don't catch Exceptions. EmailClient will handle them. */

	public String send() throws IOException {

		char[] buf = new char[BUF_SIZE];		//Buffer to read in 4kB chunks
		char[] body = new char[MAX_OBJECT_SIZE];//Max size of object

		String statusLine = "";				
		int status;								// Status code
		String headers = "";	
		int bodyLength = (-1);

		String[] tmp;
		Socket connection;	//Socket to server

		/* Streams for reading from and writing to socket */
		BufferedReader fromServer;
		DataOutputStream toServer;

		System.out.println("Connecting server: " + hostname + CRLF);

		/* Connect to http server on port 80.
		 * Assign input and output streams to connection. */
		connection = new Socket("cgi.csc.liv.ac.uk", HTTP_PORT);
		//fromServer = new BufferedReader(isr);
		//toServer = new DataOutputStream(	connection.getOutputStream()	);

		System.out.println("Send request:\n" + requestMessage);


		/* Send requestMessage to http server */


		/* Read the status line from response message */
		//statusLine= reponse.readLine();
		System.out.println("Status Line:\n"+statusLine+CRLF);

		/* Extract status code from status line. If status code is not 200,
		 * close connection and return an error message.
		 * Do NOT throw an exception */

		if (!statusLine.contains("200")){
				System.out.println("Error, connection closed");
				connection.close();
		}

		/* Read header lines from response message, convert to a string,
 		 * and assign to "headers" variable.
		 * Recall that an empty line indicates end of headers.
		 * Extract length  from "Content-Length:" (or "Content-length:")
		 * header line, if present, and assign to "bodyLength" variable.
		*/
		/* Fill in */ 		// requires about 10 lines of code
		System.out.println("Headers:\n"+headers+CRLF);


		/* If object is larger than MAX_OBJECT_SIZE, close the connection and
		 * return meaningful message. */
		/*if () {
			
			return( + bodyLength);
		}*/

		/* Read the body in chunks of BUF_SIZE using buf[] and copy the chunk
		 * into body[]. Stop when either we have
		 * read Content-Length bytes or when the connection is
		 * closed (when there is no Content-Length in the response).
		 * Use one of the read() methods of BufferedReader here, NOT readLine().
		 * Make sure not to read more than MAX_OBJECT_SIZE characters.
		 */
		int bytesRead = 0;

		/* Fill in */   // Requires 10-20 lines of code

		/* At this points body[] should hold to body of the downloaded object and
		 * bytesRead should hold the number of bytes read from the BufferedReader
		 */

		/* Close connection and return object as String. */
		System.out.println("Done reading file. Closing connection.");
		connection.close();
		return(new String(body, 0, bytesRead));
	}
}
