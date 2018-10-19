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
public class HttpInteract {
	
	private String host;
	private String path;
	private String requestMessage;

	private static final int HTTP_PORT = 80;
	private static final String CRLF = "\r\n";
	private static final int BUF_SIZE = 4096;
	private static final int MAX_OBJECT_SIZE = 102400;

 	//HttpInteract Constructor
	public HttpInteract(String url) {
		
		//Splits URL into host & path
		if (url.indexOf('/') > (-1)	) {
			String[] arrOfUrl = url.split("/", 0);
			host = (arrOfUrl[0]);//Host Name
			path = (arrOfUrl[1]);//Path Name
			
			for (int i = 2; i < arrOfUrl.length; i++) {
				path += "/" + arrOfUrl[i];
			};
			
		} else {
			host = url;
			path = "";
			
		};
		
		
		//Construct requestMessage
		requestMessage = ("GET " + "/" + path + " HTTP/1.1\r\n" +
						  "Host: " + host + CRLF + CRLF);
		/*
		Example requestMessage:
		GET /~gairing/test.txt HTTP/1.1
		Host: cgi.csc.liv.ac.uk
		*/
		
		System.out.print(requestMessage);
		return;
	}
	
	

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

		System.out.println("Connecting server: " + host + CRLF);

		//Connect to HTTP server on port 80
		connection = new Socket(host, HTTP_PORT);
		
		//Assign input to connection
		InputStream is = connection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		fromServer = new BufferedReader(isr);
		
		//Assign output to connection
		toServer = new DataOutputStream(	connection.getOutputStream()	);
		System.out.println("Send request:\n" + requestMessage);
		
		//Send requestMessage to Http server
		toServer.writeBytes(requestMessage);

		//Extract status line from server's response message
		statusLine = fromServer.readLine();
		System.out.println("Status Line:\n" + statusLine + CRLF);
		
		//Extract status code
		status = Integer.valueOf(statusLine.substring(9, 12));
		
		//Check status code
		if (status != 200){
			connection.close();
			return("Error, connection closed");
		
		//Read Headers & Body, when status code is acceptable
		} else {
			String response = fromServer.readLine();
			
			//Read Headers
			while (response.length() > 0) {
				headers += response + CRLF;
				
				//Read Content-Length
				if (	(response.toLowerCase()).contains("content-length:")	) {
					String[] responseArray = response.split(" ");
					bodyLength = Integer.valueOf(responseArray[1]);//Assign bodylength
				
				};
				
				response = fromServer.readLine();				
			};
			System.out.println("Headers:\n" + headers + CRLF);
			
			
			//Close connection when object is larger than MAX_OBJECT_SIZE
			if (bodyLength > MAX_OBJECT_SIZE) {
				connection.close();
				return("Requested object is too large\nBody Length = " + bodyLength);
			
			//Read Body
			} else {
				
				//Read Body in chunks using Buffer
				int bodyPos = 0;
				do {
					buf = new char[BUF_SIZE];						//Empty Buffer
					bytesRead += fromServer.read(buf, 0, BUF_SIZE);	//Object to Buffer
					
					//Copy Buffer to body
					for (char c : buf) {
						if (c != 0) {		//Add char to body if not null
							body[bodyPos] = c;
							bodyPos++;
						};
						
					};
					System.out.println(bytesRead + " , " + bodyLength);
				} while (	bytesRead < bodyLength && bytesRead > (-1)	);
				
			};
			
		};
		
		//Close connection & return object as String
		System.out.println("Done reading file. Closing connection.");
		connection.close();
		return(new String(body, 0, bytesRead));
		
		
	}
}
