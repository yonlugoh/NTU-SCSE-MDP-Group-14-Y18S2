import java.net.*;
import java.io.*;

public class TCPConnection {

    Socket clientSocket;
    DataOutputStream outToServer;
    BufferedReader inFromServer;

    public TCPConnection(String ipAddress, int port){
        try{
            clientSocket = new Socket(ipAddress, port);
            clientSocket.setKeepAlive(true);
        } catch(Exception e){
            System.out.println("Error...");
        }
    }

    public void sendMessage(String msg){
        try{
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes(msg);
        } catch(Exception e){
            System.out.println("Error...send");
        }
    }

    public String getMessage(){
        String result = "";
        try{
            while(true){
                inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                result = inFromServer.readLine();
                if(result != null) break;
            }
        } catch(Exception e){
            System.out.println("Error...receive");
        }
        return result;
    }

    public int[] getSensorReadings(){
        sendMessage("GIVE ME READINGS");

        String res = getMessage();
        int[] result = new int[6];
        result[0] = Character.digit(res.charAt(0), 10);
        result[1] = Character.digit(res.charAt(1), 10);
        result[2] = Character.digit(res.charAt(2), 10);
        result[3] = Character.digit(res.charAt(3), 10);
        result[4] = Character.digit(res.charAt(4), 10);
        result[5] = Character.digit(res.charAt(5), 10);

        return result;
    }
}
