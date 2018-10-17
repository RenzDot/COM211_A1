/*************************************
 * Filename:  HttpInteract.java
 * Names:
 * Student-IDs:
 * Date:
 *************************************/

import java.net.*;
import java.io.*;
import java.util.*;


/**
 * HttpInteract downloads an object from a given http server
 *
 */
 
 public class HttpInteract {
	private String host;
	private String path;
	private String requestMessage;
	private static final String CRLF = "\r\n";
	
	private static final int HTTP_PORT = 80;
	private static final int BUF_SIZE = 4096;			//Size of chunks (kB)
	private static final int MAX_OBJECT_SIZE = 102400;	//Max size of object (kB)
	
	
	//Constructor
	public HttpInteract(String url) {
		
		return;
	}
	
	/*send() To Do
	Send Http request
	parse response
	Check for errors
	return requested object as String if no error
	*/
	public String send() throws IOException {
		int bodyLength = (-1);
		int status;
		String[] tmp;
		String headers = "";
		String statusLine = ""; 
		
		char[] buf = new char[BUF_SIZE];		//Buffer to read objects in chunks
		char[] body = new char[MAX_OBJECT_SIZE];
		
		//Open socket & assign read/write streams
		System.out.println("Connecting server" + host + CRLF);
		
		Socket connection;
		BufferedReader fromServer;	//Allow fromServer to read from socket
		DataOutputStream toServer;	//Allow toServer to write to socket
		
		System.out.println("Send request:\n" + requestMessage);
		
		statusLine = "";
		System.out.println("Status Line:\n" + statusLine + CRLF);
		
		/*
		Extract status int from statusline
		If status = 200, close connection & return error msg
		Convert response message to string
		Assign headers variable (empty line indicates end of header)
		Assign Content-Length
		Assign bodyLength if header line present
		*/
		System.out.println("Headers:\n" + headers + CRLF);
		
		//Close connection if object size is greater than MAX_OBJECT_SIZE
		if (true) {
			return "";
		};
		
		/*
		Read body in using buf
		Copy chunks into body[]
		Stop copying when Content-Length bytes are read or when connection is closed
		
		Try to use read(), not readline()
		*/
		int bytesRead = 0;
		
		/*
		Close connection
		Return object as string
		*/
		
		System.out.println("Done reading file. Closing connection.");
		connection.close();
		return(	new String(body, 0, bytesRead)	);
		
	}
	
	
 }