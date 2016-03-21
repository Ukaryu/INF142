/**
 * Created by Martin on 22.02.2016.
 */

import java.io.*;
import java.net.*;

public class K {
    public static void main(String[] args) throws Exception{
        String commandFromUser;
        String responseFromServer;
        boolean closed = false;

        BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));

        Socket clientSocket = new Socket("localhost", 6789);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        while (!closed){

            commandFromUser = inFromUser.readLine();
            commandFromUser = commandFromUser.toLowerCase();

            outToServer.writeBytes(commandFromUser + '\n');

            responseFromServer = inFromServer.readLine();

            if (commandFromUser.equals("history")){
                String historyList [] = responseFromServer.split("-");

                for (String elem : historyList){
                    System.out.println(elem);
                }
            } else {
                System.out.println("FROM SERVER: " + responseFromServer);
            }

            if (responseFromServer.equals("CLOSED")){
                closed = true;
            }

        }

        outToServer.close();
        inFromServer.close();
        clientSocket.close();

        System.out.println("Connection closed");
    }
}

