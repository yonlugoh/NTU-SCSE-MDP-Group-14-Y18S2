package Interface;

import javax.swing.*;
import java.awt.*;

public class Simulator {

    static int[] robotPosition = new int[2];
    static char robotDirection;
    private JFrame frame;
    private JPanel mapPanel;
    private JButton[][] map = new JButton[20][15];

    public Simulator(){
        init();
        robotPosition[0] = 1;
        robotPosition[1] = 1;
        robotDirection = 'S';
        updateMap();
        turnBack();
    }

    private void init(){
        frame = new JFrame("Simulator");
        frame.setSize(400, 700);

        mapPanel = new JPanel();
        mapPanel.setBounds(50, 50, 300, 400);
        mapPanel.setLayout(new GridLayout(20,15));

        for(int i = 19; i > -1; i--){
            for(int j = 0; j < 15; j++){
                map[i][j] = new JButton();
                map[i][j].setToolTipText("(" + i + ", " + j + ")");
                map[i][j].setBounds(i * 20, j * 20, 20, 20);
                map[i][j].setBackground(Color.DARK_GRAY);
                mapPanel.add(map[i][j]);
            }
        }

        frame.add(mapPanel);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void updateMap(){
        map[robotPosition[0]][robotPosition[1]].setBackground(Color.GREEN);
        map[robotPosition[0]+1][robotPosition[1]].setBackground(Color.GREEN);
        map[robotPosition[0]][robotPosition[1]+1].setBackground(Color.GREEN);
        map[robotPosition[0]+1][robotPosition[1]+1].setBackground(Color.GREEN);
        map[robotPosition[0]-1][robotPosition[1]].setBackground(Color.GREEN);
        map[robotPosition[0]][robotPosition[1]-1].setBackground(Color.GREEN);
        map[robotPosition[0]-1][robotPosition[1]-1].setBackground(Color.GREEN);
        map[robotPosition[0]+1][robotPosition[1]-1].setBackground(Color.GREEN);
        map[robotPosition[0]-1][robotPosition[1]+1].setBackground(Color.GREEN);

        if(robotDirection == 'N'){
            map[robotPosition[0]+1][robotPosition[1]].setBackground(Color.YELLOW);
        } else if(robotDirection == 'S') {
            map[robotPosition[0]-1][robotPosition[1]].setBackground(Color.YELLOW);
        } else if(robotDirection == 'E') {
            map[robotPosition[0]][robotPosition[1]+1].setBackground(Color.YELLOW);
        } else if(robotDirection == 'W'){
            map[robotPosition[0]][robotPosition[1]-1].setBackground(Color.YELLOW);
        }
    }

    private void turnLeft(){
        if(robotDirection == 'N'){
            map[robotPosition[0]+1][robotPosition[1]].setBackground(Color.GREEN);
            map[robotPosition[0]][robotPosition[1]-1].setBackground(Color.YELLOW);
            robotDirection = 'W';
        } else if(robotDirection == 'S') {
            map[robotPosition[0]-1][robotPosition[1]].setBackground(Color.GREEN);
            map[robotPosition[0]][robotPosition[1]+1].setBackground(Color.YELLOW);
            robotDirection = 'E';
        } else if(robotDirection == 'E') {
            map[robotPosition[0]][robotPosition[1]+1].setBackground(Color.GREEN);
            map[robotPosition[0]+1][robotPosition[1]].setBackground(Color.YELLOW);
            robotDirection = 'N';
        } else if(robotDirection == 'W'){
            map[robotPosition[0]][robotPosition[1]-1].setBackground(Color.GREEN);
            map[robotPosition[0]-1][robotPosition[1]].setBackground(Color.YELLOW);
            robotDirection = 'S';
        }
    }

    private void turnRight(){
        if(robotDirection == 'N'){
            map[robotPosition[0]+1][robotPosition[1]].setBackground(Color.GREEN);
            map[robotPosition[0]][robotPosition[1]+1].setBackground(Color.YELLOW);
            robotDirection = 'E';
        } else if(robotDirection == 'S') {
            map[robotPosition[0]-1][robotPosition[1]].setBackground(Color.GREEN);
            map[robotPosition[0]][robotPosition[1]-1].setBackground(Color.YELLOW);
            robotDirection = 'W';
        } else if(robotDirection == 'E') {
            map[robotPosition[0]][robotPosition[1]+1].setBackground(Color.GREEN);
            map[robotPosition[0]-1][robotPosition[1]].setBackground(Color.YELLOW);
            robotDirection = 'S';
        } else if(robotDirection == 'W'){
            map[robotPosition[0]][robotPosition[1]-1].setBackground(Color.GREEN);
            map[robotPosition[0]+1][robotPosition[1]].setBackground(Color.YELLOW);
            robotDirection = 'N';
        }
    }

    private void turnBack(){
        turnRight();
        turnRight();
    }

}
