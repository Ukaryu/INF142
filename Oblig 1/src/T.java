/**
 * Created by Martin on 22.02.2016.
 */

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class T {
    public static void main(String[] args) throws Exception{
        String commandFromUser;
        String responseFromServer = null;
        int v = 0;
        ArrayList<String> history = new ArrayList<String>();
        DateFormat dateFormat;
        Date date;

        ServerSocket welcomeSocket = new ServerSocket(6789);
        Socket connectionSocket = welcomeSocket.accept();
        BufferedReader inFromClient =  new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

        boolean closed = false;
        while(!closed){
            commandFromUser = inFromClient.readLine();
            if (!(commandFromUser == null)) {

                commandFromUser = commandFromUser.toLowerCase();
                System.out.println("Received: " + commandFromUser);

                String commands [] = commandFromUser.split(" ");
                commandFromUser = commands[0];
                int value = 0;

                if (3 > commands.length && commands.length > 1){

                    try {
                        v += value;
                        value = Integer.parseInt(commands[1]);
                    }catch (NumberFormatException e){

                    }

                }


                UserCommand userCommand = null;
                try {
                    userCommand = UserCommand.valueOf(commandFromUser);

                }catch (IllegalArgumentException e){
                    userCommand = UserCommand.wrongCommand;
                }finally {

                    dateFormat = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
                    date = new Date();
                    String time = dateFormat.format(date);
                    String address = connectionSocket.getRemoteSocketAddress().toString();

                    switch (userCommand) {
                        case add:
                            if (value != 0){
                                v += value;

                                responseFromServer = "Value added to V";
                                history.add(" IP: " + address + " DATE: " + time + " CHANGE: " + "Subtracted " + value + " from V");
                            } else {
                                responseFromServer = "No value given!";
                            }
                            break;
                        case sub:
                                if (value != 0){
                                    v -= value;

                                    responseFromServer = "Value subtracted from V";
                                    history.add(" IP: " + address + " DATE: " + time + " CHANGE: " + "Subtracted " + value + " from V");
                                } else {
                                    responseFromServer = "No value given!";
                                }
                            break;
                        case get:
                            responseFromServer = "Value of V: " + v;

                            break;
                        case history:
                            String info = "";

                            for (String elem : history){
                                info += elem + "-";
                            }
                            responseFromServer = info;
                            break;
                        case close:
                            responseFromServer = "CLOSED";
                            closed = true;
                            break;

                        case wrongCommand:
                            responseFromServer = "You have not entered a valid request";
                            break;

                    }
                }


                if (!(responseFromServer == null)){
                    outToClient.writeBytes(responseFromServer  + '\n');
                }

            }

        }
        outToClient.close();
        inFromClient.close();
        welcomeSocket.close();

    }

    private enum UserCommand {
        add, sub, get, history, close, wrongCommand
    }
}

