// ----------------------------------------------------------------------------------------
//  WordServerInterface.java 
//     This file is incomplete. 
//     This file should oversee connection to the word server and retrieval of candidate 
//     words from the word server. It will need to convert them into a string array and
//     return the data to blackboard.
//
//     You will need to add code and edit this file as part of your solution. 
// ----------------------------------------------------------------------------------------
package blackBoard;

import java.io.*;
import java.net.Socket;

public class WordServerInterface {
    private static final String hostname = "localhost";
    private static final Integer port = 18877;
    private static boolean connected = false;
    private BufferedWriter bufferedWriter;
    private BufferedReader socketReader;
    private Socket serverSocket = null;

    // initialise the connection and logging in
    public WordServerInterface() {
        String token;

        try {
            serverSocket = new Socket(hostname, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        socketReader = null;
        try {
            InputStream socketStream = serverSocket.getInputStream();
            socketReader = new BufferedReader(new InputStreamReader(socketStream));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            OutputStream outputStream = serverSocket.getOutputStream();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            if (!connected) {
                String line = socketReader.readLine();
                String[] tokens = line.split("\\+");
                System.out.println("Line received : " + tokens[0] + " " + tokens[1]);
                token = tokens[1];
                token = token.substring(0, token.length() - 1);
                String connectionString = "<" + " 1700331" + "*" + token + ">" + "\n";
                bufferedWriter.write(connectionString);
                bufferedWriter.flush(); //TODO mark this flush as "bug"
                System.out.println(socketReader.readLine());
                connected = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // receives a word code and returns an array of strings into the blackboard
    public String[] allPatterns(String pattern) {
        String[] words = null;
        char[] test = new char[100000];
        try {
            bufferedWriter.write(pattern + "\n");
            bufferedWriter.flush();
            socketReader.read(test);
            String line = new String(test);
            //System.out.println(line);
            words = line.split(",");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }
}
