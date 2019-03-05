import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;

public class Map {

    final Point startPoint = new Point(1, 1);
    final Point endPoint = new Point(18, 13);
    int[][] map;
    int[][] simulatedMap;
    boolean isBackAtStart = false;

    public Map(){
        init();
    }

    public Map(String path){
        init();
        simulatedMap = new int[20][15];

        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader(path));
            String line;
            int i = 0;
            do{
                line = reader.readLine();
                for(int j = 0; j < 15; j++){
                    simulatedMap[19-i][j] = Character.getNumericValue(line.charAt(j));
                }
                i++;
            }
            while(line != null && i < 20);
        } catch(IOException e){
            e.printStackTrace();
        }

        for(int i = 0; i < 20; i++){
            for(int j = 0; j < 15; j++){
                System.out.print(simulatedMap[i][j]);
            }
            System.out.println("");
        }
    }

    private void init(){
        map = new int[20][15];

        for(int i = 19; i > -1; i--){
            for(int j = 0; j < 15; j++){
                map[i][j] = 0;
            }
        }

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                map[i][j] = 1;
            }
        }
    }

    public double getPercentageExplored(){
        int explored = 0;
        for(int i = 0; i < 20; i++){
            for(int j = 0; j < 15; j++){
                if(map[i][j] == 1 || map[i][j] == 2){
                    explored++;
                }
            }
        }

        double res = ((double)explored / 300) * 100;
        return res;
    }

    public String getMDFExplored(){
        StringBuilder sb = new StringBuilder("11");
        for(int i = 0; i < 20; i++){
            for(int j = 0; j < 15; j++){
                if(map[i][j] == 2) sb.append("1");
                else sb.append(map[i][j]);
            }
        }
        sb.append("11");

        StringBuilder res = new StringBuilder();
        for(int i = 0; i < 304; i+=4){
            res.append(bin2Hex(sb.substring(i, i+4)));
        }
        System.out.println(res);
        return res.substring(0);
    }

    public String getMDFObstacle(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 20; i++){
            for(int j = 0; j < 15; j++){
                if(map[i][j] == 1) sb.append("0");
                else if(map[i][j] == 2) sb.append("1");
            }
        }
        int padNum = 8 - (sb.length() % 8);
        for(int i = 0; i < padNum; i++)
            sb.append("0");

        System.out.println(sb);
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < sb.length(); i+=4){
            res.append(bin2Hex(sb.substring(i, i+4)));
        }
        System.out.println(res);
        return res.substring(0);
    }

    private String bin2Hex(String input){
        switch (input) {
            case "0000":
                return "0";
            case "0001":
                return "1";
            case "0010":
                return "2";
            case "0011":
                return "3";
            case "0100":
                return "4";
            case "0101":
                return "5";
            case "0110":
                return "6";
            case "0111":
                return "7";
            case "1000":
                return "8";
            case "1001":
                return "9";
            case "1010":
                return "A";
            case "1011":
                return "B";
            case "1100":
                return "C";
            case "1101":
                return "D";
            case "1110":
                return "E";
            case "1111":
                return "F";
            default:
                return "0";
        }
    }

    public void printMap(){
        for(int i = 19; i > -1; i--){
            for(int j = 0; j < 15; j++){
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }

}
