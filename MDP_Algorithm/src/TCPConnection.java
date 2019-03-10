import java.net.*;
import java.io.*;
import java.util.Arrays;

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
                if(result != null) {
                    break;
                }
            }
        } catch(Exception e){
            System.out.println("Error...receive");
        }
        return result;
    }

    public int[] getSensorReadings(){
        sendMessage("sS");

        String res = getMessage();
        int[] result = processReadings(res);

        /*for(int i = 0; i < 5; i++){
            if(result[i] > 3) result[i] = 0;
        }
        if(result[5] > 4) result[5] = 0;*/

        System.out.println(result[0] + " " + result[1] + " " + result[2] + " " + result[3] + " " + result[4] + " " + result[5]);

        return result;
    }

    public int[] getSensorReadings2(){

        String res = getMessage();
        int[] result = processReadings(res);

        /*for(int i = 0; i < 5; i++){
            if(result[i] > 3) result[i] = 0;
        }
        if(result[5] > 4) result[5] = 0;*/

        System.out.println(result[0] + " " + result[1] + " " + result[2] + " " + result[3] + " " + result[4] + " " + result[5]);

        return result;
    }

    public int[] processReadings(String readings){
        int[] results = new int[6];
        int numSets = readings.length() / 6;
        int[][] interim = new int[6][numSets];
        for(int i = 0; i < numSets; i++){
            interim[0][i] = Character.getNumericValue(readings.charAt(i * 6));
            interim[1][i] = Character.getNumericValue(readings.charAt((i * 6) + 1));
            interim[2][i] = Character.getNumericValue(readings.charAt((i * 6) + 2));
            interim[3][i] = Character.getNumericValue(readings.charAt((i * 6) + 3));
            interim[4][i] = Character.getNumericValue(readings.charAt((i * 6) + 4));
            interim[5][i] = Character.getNumericValue(readings.charAt((i * 6) + 5));
        }
        Arrays.sort(interim[0]);
        Arrays.sort(interim[1]);
        Arrays.sort(interim[2]);
        Arrays.sort(interim[3]);
        Arrays.sort(interim[4]);
        Arrays.sort(interim[5]);

        results[0] = interim[0][numSets/2];
        results[1] = interim[1][numSets/2];
        results[2] = interim[2][numSets/2];
        results[3] = interim[3][numSets/2];
        results[4] = interim[4][numSets/2];
        results[5] = interim[5][numSets/2];

        return results;
    }
}
