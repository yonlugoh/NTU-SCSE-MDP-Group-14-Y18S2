import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Simulator {

    final int timestep = 100;

    private JFrame frame;
    private JPanel mapPanel;
    private JPanel botPanel;
    private JButton[][] mapButtons = new JButton[20][15];
    private JButton back = new JButton("back");
    private JButton left = new JButton("left");
    private JButton right = new JButton("right");
    private JButton move = new JButton("move");
    private JLabel exploreLabel = new JLabel("Exploration Cutoff: ");
    private JLabel timeLabel = new JLabel("Time Limit (seconds): ");
    private JTextField exploreText = new JTextField("100");
    private JTextField timeText = new JTextField("60");
    private JButton loadMap = new JButton("Load Map");
    private JButton explore = new JButton("Explore");
    private JButton fastestPath = new JButton("Fastest Path");
    private JLabel percentageLabel = new JLabel("Explored Area (%): ");
    private JLabel timeElapsedLabel = new JLabel("Time Elapsed (seconds): ");
    private JLabel percentage = new JLabel("0%");
    private JLabel timeElapsed = new JLabel("0");
    private Map m;
    private Robot robot;
    boolean isBackAtStart = false;
    Timer exploreTimer;
    Timer exploreFPTimer;
    Timer fpTimer;
    Timer stopwatch;
    Point start = new Point(1, 1);
    Point end = new Point(18, 13);
    Point waypoint;
    FastestPath fp;
    char path[];
    int time = 0;
    int pathMove = 0;

    public Simulator(){
        init();
        updateMap();
    }

    private void init(){
        frame = new JFrame("Simulator");
        frame.setSize(400, 700);

        mapPanel = new JPanel();
        mapPanel.setBounds(50, 50, 300, 400);
        mapPanel.setLayout(new GridLayout(20,15));

        botPanel = new JPanel();
        botPanel.setBounds(50, 450, 300, 200);
        botPanel.setLayout(null);

        m = new Map();
        //m = new Map("src/map1");
        robot = new Robot();

        for(int i = 19; i > -1; i--){
            for(int j = 0; j < 15; j++){
                mapButtons[i][j] = new JButton();
                mapButtons[i][j].setToolTipText("(" + i + ", " + j + ")");
                mapButtons[i][j].setBounds(i * 20, j * 20, 20, 20);
                mapButtons[i][j].setBackground(Color.DARK_GRAY);
                final int ti = i;
                final int tj = j;
                mapButtons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(fp.fpMap[ti][tj] == 1){
                            waypoint = new Point(ti, tj);
                            System.out.println("Waypoint set: (" + ti + ", " + tj + ")");
                        } else{
                            System.out.println("invalid waypoint");
                        }
                    }
                });
                mapPanel.add(mapButtons[i][j]);
            }
        }

        back.setBounds(20, 0, 80, 20);
        left.setBounds(100, 0, 80, 20);
        right.setBounds(180, 0, 80, 20);
        move.setBounds(100, 20, 80, 20);
        exploreLabel.setBounds(60, 40, 160, 20);
        timeLabel.setBounds(60, 60, 160, 20);
        exploreText.setBounds(200, 40, 40, 20);
        timeText.setBounds(200, 60, 40, 20);
        loadMap.setBounds(60, 80, 180, 20);
        explore.setBounds(60, 100, 180, 20);
        fastestPath.setBounds(60, 120, 180, 20);
        percentageLabel.setBounds(40, 140, 100, 20);
        percentage.setBounds(140, 140, 60, 20);
        timeElapsedLabel.setBounds(40, 160, 160, 20);
        timeElapsed.setBounds(200, 160, 60, 20);

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                turnBack();
            }
        });

        left.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                turnLeft();
            }
        });

        right.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                turnRight();
            }
        });

        move.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                move();
            }
        });

        loadMap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
                fileChooser.showOpenDialog(botPanel);
                m = new Map(fileChooser.getSelectedFile().getPath());
            }
        });

        explore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scan();
                updateMap();

                stopwatch = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        time++;
                        timeElapsed.setText(time + "");
                    }
                });
                stopwatch.start();

                exploreTimer = new Timer(timestep, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        percentage.setText(String.format("%.2f", m.getPercentageExplored()));
                        explore();
                    }
                });
                exploreTimer.start();
            }
        });

        fastestPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pathMove = 0;
                if(waypoint != null){
                    path = fp.getFastestPathWaypoint(start, end, robot.currentDirection, waypoint);
                } else{
                    path = fp.getFastestPath(start, end, robot.currentDirection);
                }
                fpTimer = new Timer(timestep, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        fastestPath();
                    }
                });
                fpTimer.start();
            }
        });

        botPanel.add(back);
        botPanel.add(left);
        botPanel.add(right);
        botPanel.add(move);
        botPanel.add(exploreLabel);
        botPanel.add(timeLabel);
        botPanel.add(exploreText);
        botPanel.add(timeText);
        botPanel.add(loadMap);
        botPanel.add(explore);
        botPanel.add(fastestPath);
        botPanel.add(percentageLabel);
        botPanel.add(percentage);
        botPanel.add(timeElapsedLabel);
        botPanel.add(timeElapsed);

        frame.add(mapPanel);
        frame.add(botPanel);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void updateMap(){

        for(int i = 0; i < 20; i++){
            for(int j = 0; j < 15; j++){
                if(m.map[i][j] == 0)
                    mapButtons[i][j].setBackground(Color.DARK_GRAY);
                else if(m.map[i][j] == 1)
                    mapButtons[i][j].setBackground(Color.WHITE);
                else if(m.map[i][j] == 2)
                    mapButtons[i][j].setBackground(Color.RED);
            }
        }

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                mapButtons[i][j].setBackground(Color.BLUE);
            }
        }

        for(int i = 17; i < 20; i++){
            for(int j = 12; j < 15; j++){
                mapButtons[i][j].setBackground(Color.BLUE);
            }
        }

        mapButtons[robot.currentPosition.x][robot.currentPosition.y].setBackground(Color.GREEN);
        mapButtons[robot.currentPosition.x+1][robot.currentPosition.y].setBackground(Color.GREEN);
        mapButtons[robot.currentPosition.x][robot.currentPosition.y+1].setBackground(Color.GREEN);
        mapButtons[robot.currentPosition.x+1][robot.currentPosition.y+1].setBackground(Color.GREEN);
        mapButtons[robot.currentPosition.x-1][robot.currentPosition.y].setBackground(Color.GREEN);
        mapButtons[robot.currentPosition.x][robot.currentPosition.y-1].setBackground(Color.GREEN);
        mapButtons[robot.currentPosition.x-1][robot.currentPosition.y-1].setBackground(Color.GREEN);
        mapButtons[robot.currentPosition.x+1][robot.currentPosition.y-1].setBackground(Color.GREEN);
        mapButtons[robot.currentPosition.x-1][robot.currentPosition.y+1].setBackground(Color.GREEN);

        if(robot.currentDirection == 'N'){
            mapButtons[robot.currentPosition.x+1][robot.currentPosition.y].setBackground(Color.YELLOW);
        } else if(robot.currentDirection == 'S') {
            mapButtons[robot.currentPosition.x-1][robot.currentPosition.y].setBackground(Color.YELLOW);
        } else if(robot.currentDirection == 'E') {
            mapButtons[robot.currentPosition.x][robot.currentPosition.y+1].setBackground(Color.YELLOW);
        } else if(robot.currentDirection == 'W'){
            mapButtons[robot.currentPosition.x][robot.currentPosition.y-1].setBackground(Color.YELLOW);
        }
    }

    private void turnLeft(){
        if(robot.currentDirection == 'N'){
            robot.currentDirection = 'W';
        } else if(robot.currentDirection == 'S') {
            robot.currentDirection = 'E';
        } else if(robot.currentDirection == 'E') {
            robot.currentDirection = 'N';
        } else if(robot.currentDirection == 'W'){
            robot.currentDirection = 'S';
        }
    }

    private void turnRight(){
        if(robot.currentDirection == 'N'){
            robot.currentDirection = 'E';
        } else if(robot.currentDirection == 'S') {
            robot.currentDirection = 'W';
        } else if(robot.currentDirection == 'E') {
            robot.currentDirection = 'S';
        } else if(robot.currentDirection == 'W'){
            robot.currentDirection = 'N';
        }
    }

    private void turnBack(){
        turnRight();
        turnRight();
    }

    public void move(){
        if(robot.currentDirection == 'N'){
            robot.currentPosition.x++;
        } else if(robot.currentDirection == 'S') {
            robot.currentPosition.x--;
        } else if(robot.currentDirection == 'E') {
            robot.currentPosition.y++;
        } else if(robot.currentDirection == 'W'){
            robot.currentPosition.y--;
        }
    }

    public void scan(){
        int results[] = robot.getSensorReadings(m.simulatedMap);
        //System.out.println(results[0] + " " + results[1] + " " + results[2] + " " + results[3] + " " + results[4] + " " + results[5]);

        if(robot.currentDirection == 'N'){

            //Sensor 1
            if(results[0] > 0){
                try{
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 1 - results[0]] = 2;
                    results[0]--;
                    for(; results[0] > 0; results[0]--){
                        m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 1 - results[0]] = 1;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    for(int i = 1; i < results[0]; i++){
                        m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 1 - i] = 1;
                    }
                }
            } else{
                m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 2] = 1;
                m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 3] = 1;
                m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 4] = 1;
            }

            //Sensor 2
            if(results[1] > 0){
                try{
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y - 1 - results[1]] = 2;
                    results[1]--;
                    for(; results[1] > 0; results[1]--){
                        m.map[robot.currentPosition.x + 1][robot.currentPosition.y - 1 - results[1]] = 1;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    for(int i = 1; i < results[1]; i++){
                        m.map[robot.currentPosition.x + 1][robot.currentPosition.y - 1 - i] = 1;
                    }
                }
            } else{
                m.map[robot.currentPosition.x + 1][robot.currentPosition.y - 2] = 1;
                m.map[robot.currentPosition.x + 1][robot.currentPosition.y - 3] = 1;
                m.map[robot.currentPosition.x + 1][robot.currentPosition.y - 4] = 1;
            }

            //Sensor 3
            if(results[2] > 0){
                try{
                    m.map[robot.currentPosition.x + 1 + results[2]][robot.currentPosition.y - 1] = 2;
                    results[2]--;
                    for(; results[2] > 0; results[2]--){
                        m.map[robot.currentPosition.x + 1 + results[2]][robot.currentPosition.y - 1] = 1;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    for(int i = 1; i < results[2]; i++){
                        m.map[robot.currentPosition.x + 1 + i][robot.currentPosition.y - 1] = 1;
                    }
                }
            } else{
                m.map[robot.currentPosition.x + 2][robot.currentPosition.y - 1] = 1;
                m.map[robot.currentPosition.x + 3][robot.currentPosition.y - 1] = 1;
                m.map[robot.currentPosition.x + 4][robot.currentPosition.y - 1] = 1;
            }

            //Sensor 4
            if(results[3] > 0){
                try{
                    m.map[robot.currentPosition.x + 1 + results[3]][robot.currentPosition.y] = 2;
                    results[3]--;
                    for(; results[3] > 0; results[3]--){
                        m.map[robot.currentPosition.x + 1 + results[3]][robot.currentPosition.y] = 1;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    for(int i = 1; i < results[3]; i++){
                        m.map[robot.currentPosition.x + 1 + i][robot.currentPosition.y] = 1;
                    }
                }
            } else{
                m.map[robot.currentPosition.x + 2][robot.currentPosition.y] = 1;
                m.map[robot.currentPosition.x + 3][robot.currentPosition.y] = 1;
                m.map[robot.currentPosition.x + 4][robot.currentPosition.y] = 1;
            }

            //Sensor 5
            if(results[4] > 0){
                try{
                    m.map[robot.currentPosition.x + 1 + results[4]][robot.currentPosition.y + 1] = 2;
                    results[4]--;
                    for(; results[4] > 0; results[4]--){
                        m.map[robot.currentPosition.x + 1 + results[4]][robot.currentPosition.y + 1] = 1;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    for(int i = 1; i < results[4]; i++){
                        m.map[robot.currentPosition.x + 1 + i][robot.currentPosition.y + 1] = 1;
                    }
                }
            } else{
                m.map[robot.currentPosition.x + 2][robot.currentPosition.y + 1] = 1;
                m.map[robot.currentPosition.x + 3][robot.currentPosition.y + 1] = 1;
                m.map[robot.currentPosition.x + 4][robot.currentPosition.y + 1] = 1;
            }

            //Sensor 6
            if(results[5] > 0){
                try{
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 1 + results[5]] = 2;
                    results[5]--;
                    for(; results[5] > 0; results[5]--){
                        m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 1 + results[5]] = 1;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    for(int i = 1; i < results[5]; i++){
                        m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 1 + i] = 1;
                    }
                }
            } else{
                m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 2] = 1;
                m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 3] = 1;
                m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 4] = 1;
                m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 5] = 1;
            }

        } else if(robot.currentDirection == 'S'){

            //Sensor 1
            if(results[0] > 0){
                try{
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 1 + results[0]] = 2;
                    results[0]--;
                    for(; results[0] > 0; results[0]--){
                        m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 1 + results[0]] = 1;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    for(int i = 1; i < results[0]; i++){
                        m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 1 + i] = 1;
                    }
                }
            } else{
                m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 2] = 1;
                m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 3] = 1;
                m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 4] = 1;
            }

            //Sensor 2
            if(results[1] > 0){
                try{
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y + 1 + results[1]] = 2;
                    results[1]--;
                    for(; results[1] > 0; results[1]--){
                        m.map[robot.currentPosition.x - 1][robot.currentPosition.y + 1 + results[1]] = 1;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    for(int i = 1; i < results[1]; i++){
                        m.map[robot.currentPosition.x - 1][robot.currentPosition.y + 1 + i] = 1;
                    }
                }
            } else{
                m.map[robot.currentPosition.x - 1][robot.currentPosition.y + 2] = 1;
                m.map[robot.currentPosition.x - 1][robot.currentPosition.y + 3] = 1;
                m.map[robot.currentPosition.x - 1][robot.currentPosition.y + 4] = 1;
            }

            //Sensor 3
            if(results[2] > 0){
                try{
                    m.map[robot.currentPosition.x - 1 - results[2]][robot.currentPosition.y + 1] = 2;
                    results[2]--;
                    for(; results[2] > 0; results[2]--){
                        m.map[robot.currentPosition.x - 1 - results[2]][robot.currentPosition.y + 1] = 1;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    for(int i = 1; i < results[2]; i++){
                        m.map[robot.currentPosition.x - 1 - i][robot.currentPosition.y + 1] = 1;
                    }
                }
            } else{
                m.map[robot.currentPosition.x - 2][robot.currentPosition.y + 1] = 1;
                m.map[robot.currentPosition.x - 3][robot.currentPosition.y + 1] = 1;
                m.map[robot.currentPosition.x - 4][robot.currentPosition.y + 1] = 1;
            }

            //Sensor 4
            if(results[3] > 0){
                try{
                    m.map[robot.currentPosition.x - 1 - results[3]][robot.currentPosition.y] = 2;
                    results[3]--;
                    for(; results[3] > 0; results[3]--){
                        m.map[robot.currentPosition.x - 1 - results[3]][robot.currentPosition.y] = 1;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    for(int i = 1; i < results[3]; i++){
                        m.map[robot.currentPosition.x - 1 - i][robot.currentPosition.y] = 1;
                    }
                }
            } else{
                m.map[robot.currentPosition.x - 2][robot.currentPosition.y] = 1;
                m.map[robot.currentPosition.x - 3][robot.currentPosition.y] = 1;
                m.map[robot.currentPosition.x - 4][robot.currentPosition.y] = 1;
            }

            //Sensor 5
            if(results[4] > 0){
                try{
                    m.map[robot.currentPosition.x - 1 - results[4]][robot.currentPosition.y - 1] = 2;
                    results[4]--;
                    for(; results[4] > 0; results[4]--){
                        m.map[robot.currentPosition.x - 1 - results[4]][robot.currentPosition.y - 1] = 1;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    for(int i = 1; i < results[4]; i++){
                        m.map[robot.currentPosition.x - 1 - i][robot.currentPosition.y - 1] = 1;
                    }
                }
            } else{
                m.map[robot.currentPosition.x - 2][robot.currentPosition.y - 1] = 1;
                m.map[robot.currentPosition.x - 3][robot.currentPosition.y - 1] = 1;
                m.map[robot.currentPosition.x - 4][robot.currentPosition.y - 1] = 1;
            }

            //Sensor 6
            if(results[5] > 0){
                try{
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 1 - results[5]] = 2;
                    results[5]--;
                    for(; results[5] > 0; results[5]--){
                        m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 1 - results[5]] = 1;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    for(int i = 1; i < results[5]; i++){
                        m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 1 - i] = 1;
                    }
                }
            } else{
                m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 2] = 1;
                m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 3] = 1;
                m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 4] = 1;
                m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 5] = 1;
            }

        }else if(robot.currentDirection == 'E'){

            //Sensor 1
            if(results[0] > 0){
                try{
                    m.map[robot.currentPosition.x + 1 + results[0]][robot.currentPosition.y - 1] = 2;
                    results[0]--;
                    for(; results[0] > 0; results[0]--){
                        m.map[robot.currentPosition.x + 1 + results[0]][robot.currentPosition.y - 1] = 1;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    for(int i = 1; i < results[0]; i++){
                        m.map[robot.currentPosition.x + 1 + i][robot.currentPosition.y - 1] = 1;
                    }
                }
            } else{
                m.map[robot.currentPosition.x + 2][robot.currentPosition.y - 1] = 1;
                m.map[robot.currentPosition.x + 3][robot.currentPosition.y - 1] = 1;
                m.map[robot.currentPosition.x + 4][robot.currentPosition.y - 1] = 1;
            }

            //Sensor 2
            if(results[1] > 0){
                try{
                    m.map[robot.currentPosition.x + 1 + results[1]][robot.currentPosition.y + 1] = 2;
                    results[1]--;
                    for(; results[1] > 0; results[1]--){
                        m.map[robot.currentPosition.x + 1 + results[1]][robot.currentPosition.y + 1] = 1;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    for(int i = 1; i < results[1]; i++){
                        m.map[robot.currentPosition.x + 1 + i][robot.currentPosition.y + 1] = 1;
                    }
                }
            } else{
                m.map[robot.currentPosition.x + 2][robot.currentPosition.y + 1] = 1;
                m.map[robot.currentPosition.x + 3][robot.currentPosition.y + 1] = 1;
                m.map[robot.currentPosition.x + 4][robot.currentPosition.y + 1] = 1;
            }

            //Sensor 3
            if(results[2] > 0){
                try{
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 1 + results[2]] = 2;
                    results[2]--;
                    for(; results[2] > 0; results[2]--){
                        m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 1 + results[2]] = 1;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    for(int i = 1; i < results[2]; i++){
                        m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 1 + i] = 1;
                    }
                }
            } else{
                m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 2] = 1;
                m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 3] = 1;
                m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 4] = 1;
            }

            //Sensor 4
            if(results[3] > 0){
                try{
                    m.map[robot.currentPosition.x][robot.currentPosition.y + 1 + results[3]] = 2;
                    results[3]--;
                    for(; results[3] > 0; results[3]--){
                        m.map[robot.currentPosition.x][robot.currentPosition.y + 1 + results[3]] = 1;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    for(int i = 1; i < results[3]; i++){
                        m.map[robot.currentPosition.x][robot.currentPosition.y + 1 + i] = 1;
                    }
                }
            } else{
                m.map[robot.currentPosition.x][robot.currentPosition.y + 2] = 1;
                m.map[robot.currentPosition.x][robot.currentPosition.y + 3] = 1;
                m.map[robot.currentPosition.x][robot.currentPosition.y + 4] = 1;
            }

            //Sensor 5
            if(results[4] > 0){
                try{
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y + 1 + results[4]] = 2;
                    results[4]--;
                    for(; results[4] > 0; results[4]--){
                        m.map[robot.currentPosition.x - 1][robot.currentPosition.y + 1 + results[4]] = 1;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    for(int i = 1; i < results[3]; i++){
                        m.map[robot.currentPosition.x - 1][robot.currentPosition.y + 1 + i] = 1;
                    }
                }
            } else{
                m.map[robot.currentPosition.x - 1][robot.currentPosition.y + 2] = 1;
                m.map[robot.currentPosition.x - 1][robot.currentPosition.y + 3] = 1;
                m.map[robot.currentPosition.x - 1][robot.currentPosition.y + 4] = 1;
            }

            //Sensor 6
            if(results[5] > 0){
                try{
                    m.map[robot.currentPosition.x - 1 - results[5]][robot.currentPosition.y + 1] = 2;
                    results[5]--;
                    for(; results[5] > 0; results[5]--){
                        m.map[robot.currentPosition.x - 1 - results[5]][robot.currentPosition.y + 1] = 1;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    for(int i = 1; i < results[5]; i++){
                        m.map[robot.currentPosition.x - 1 - i][robot.currentPosition.y + 1] = 1;
                    }
                }
            } else{
                m.map[robot.currentPosition.x - 2][robot.currentPosition.y + 1] = 1;
                m.map[robot.currentPosition.x - 3][robot.currentPosition.y + 1] = 1;
                m.map[robot.currentPosition.x - 4][robot.currentPosition.y + 1] = 1;
                m.map[robot.currentPosition.x - 5][robot.currentPosition.y + 1] = 1;
            }

        }else if(robot.currentDirection == 'W'){

            //Sensor 1
            if(results[0] > 0){
                try{
                    m.map[robot.currentPosition.x - 1 - results[0]][robot.currentPosition.y + 1] = 2;
                    results[0]--;
                    for(; results[0] > 0; results[0]--){
                        m.map[robot.currentPosition.x - 1 - results[0]][robot.currentPosition.y + 1] = 1;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    for(int i = 1; i < results[0]; i++){
                        m.map[robot.currentPosition.x - 1 - i][robot.currentPosition.y + 1] = 1;
                    }
                }
            } else{
                m.map[robot.currentPosition.x - 2][robot.currentPosition.y + 1] = 1;
                m.map[robot.currentPosition.x - 3][robot.currentPosition.y + 1] = 1;
                m.map[robot.currentPosition.x - 4][robot.currentPosition.y + 1] = 1;
            }

            //Sensor 2
            if(results[1] > 0){
                try{
                    m.map[robot.currentPosition.x - 1 - results[1]][robot.currentPosition.y - 1] = 2;
                    results[1]--;
                    for(; results[1] > 0; results[1]--){
                        m.map[robot.currentPosition.x - 1 - results[1]][robot.currentPosition.y - 1] = 1;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    for(int i = 1; i < results[1]; i++){
                        m.map[robot.currentPosition.x - 1 - i][robot.currentPosition.y - 1] = 1;
                    }
                }
            } else{
                m.map[robot.currentPosition.x - 2][robot.currentPosition.y - 1] = 1;
                m.map[robot.currentPosition.x - 3][robot.currentPosition.y - 1] = 1;
                m.map[robot.currentPosition.x - 4][robot.currentPosition.y - 1] = 1;
            }

            //Sensor 3
            if(results[2] > 0){
                try{
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 1 - results[2]] = 2;
                    results[2]--;
                    for(; results[2] > 0; results[2]--){
                        m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 1 - results[2]] = 1;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    for(int i = 1; i < results[2]; i++){
                        m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 1 - i] = 1;
                    }
                }
            } else{
                m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 2] = 1;
                m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 3] = 1;
                m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 4] = 1;
            }

            //Sensor 4
            if(results[3] > 0){
                try{
                    m.map[robot.currentPosition.x][robot.currentPosition.y - 1 - results[3]] = 2;
                    results[3]--;
                    for(; results[3] > 0; results[3]--){
                        m.map[robot.currentPosition.x][robot.currentPosition.y - 1 - results[3]] = 1;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    for(int i = 1; i < results[3]; i++){
                        m.map[robot.currentPosition.x][robot.currentPosition.y - 1 - i] = 1;
                    }
                }
            } else{
                m.map[robot.currentPosition.x][robot.currentPosition.y - 2] = 1;
                m.map[robot.currentPosition.x][robot.currentPosition.y - 3] = 1;
                m.map[robot.currentPosition.x][robot.currentPosition.y - 4] = 1;
            }

            //Sensor 5
            if(results[4] > 0){
                try{
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y - 1 - results[4]] = 2;
                    results[4]--;
                    for(; results[4] > 0; results[4]--){
                        m.map[robot.currentPosition.x + 1][robot.currentPosition.y - 1 - results[4]] = 1;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    for(int i = 1; i < results[3]; i++){
                        m.map[robot.currentPosition.x + 1][robot.currentPosition.y - 1 - i] = 1;
                    }
                }
            } else{
                m.map[robot.currentPosition.x + 1][robot.currentPosition.y - 2] = 1;
                m.map[robot.currentPosition.x + 1][robot.currentPosition.y - 3] = 1;
                m.map[robot.currentPosition.x + 1][robot.currentPosition.y - 4] = 1;
            }

            //Sensor 6
            if(results[5] > 0){
                try{
                    m.map[robot.currentPosition.x + 1 + results[5]][robot.currentPosition.y - 1] = 2;
                    results[5]--;
                    for(; results[5] > 0; results[5]--){
                        m.map[robot.currentPosition.x + 1 + results[5]][robot.currentPosition.y - 1] = 1;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    for(int i = 1; i < results[5]; i++){
                        m.map[robot.currentPosition.x + 1 + i][robot.currentPosition.y - 1] = 1;
                    }
                }
            } else{
                m.map[robot.currentPosition.x + 2][robot.currentPosition.y - 1] = 1;
                m.map[robot.currentPosition.x + 3][robot.currentPosition.y - 1] = 1;
                m.map[robot.currentPosition.x + 4][robot.currentPosition.y - 1] = 1;
                m.map[robot.currentPosition.x + 5][robot.currentPosition.y - 1] = 1;
            }
        }

        m.getMDFExplored();
        m.getMDFObstacle();
    }

    private void explore(){

        scan();
        updateMap();
        if(checkLeft()){
            turnLeft();
            updateMap();
            move();
            updateMap();
        } else if(checkFront()){
            move();
            updateMap();
        } else if(checkRight()){
            turnRight();
            updateMap();
            move();
            updateMap();
        } else{
            turnRight();
            updateMap();
        }

        if(m.map[17][12] == 1 && m.map[17][13] == 1 && m.map[17][14] == 1 && m.map[18][12] == 1 && m.map[19][12] == 1)
            m.isGoalFound = true;
        checkFinishExploration();
    }

    private void checkFinishExploration(){
        if(Integer.parseInt(timeElapsed.getText()) >= Integer.parseInt(timeText.getText()) || Double.parseDouble(percentage.getText()) >= Double.parseDouble(exploreText.getText())){
        //if(robot.currentPosition.x == 1 && robot.currentPosition.y == 1 && m.isGoalFound){
            isBackAtStart = true;
            exploreTimer.stop();
            stopwatch.stop();
            fp = new FastestPath(m.map);

            pathMove = 0;
            path = fp.getFastestPath(robot.currentPosition, start, robot.currentDirection);
            exploreFPTimer = new Timer(timestep, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    goBackToStart();
                }
            });
            exploreFPTimer.start();
        }
    }

    private boolean checkLeft(){
        boolean res = false;

        try {
            if(robot.currentDirection == 'N'){
                if(m.map[robot.currentPosition.x+1][robot.currentPosition.y-2] == 1
                        && m.map[robot.currentPosition.x][robot.currentPosition.y-2] == 1
                        && m.map[robot.currentPosition.x-1][robot.currentPosition.y-2] == 1)
                    res = true;
            } else if(robot.currentDirection == 'S'){
                if(m.map[robot.currentPosition.x+1][robot.currentPosition.y+2] == 1
                        && m.map[robot.currentPosition.x][robot.currentPosition.y+2] == 1
                        && m.map[robot.currentPosition.x-1][robot.currentPosition.y+2] == 1)
                    res = true;
            } else if(robot.currentDirection == 'E'){
                if(m.map[robot.currentPosition.x+2][robot.currentPosition.y-1] == 1
                        && m.map[robot.currentPosition.x+2][robot.currentPosition.y] == 1
                        && m.map[robot.currentPosition.x+2][robot.currentPosition.y+1] == 1)
                    res = true;
            } else if(robot.currentDirection == 'W'){
                if(m.map[robot.currentPosition.x-2][robot.currentPosition.y-1] == 1
                        && m.map[robot.currentPosition.x-2][robot.currentPosition.y] == 1
                        && m.map[robot.currentPosition.x-2][robot.currentPosition.y+1] == 1)
                    res = true;
            }
        } catch(ArrayIndexOutOfBoundsException e){ }

        return res;
    }

    private boolean checkFront(){
        boolean res = false;

        try {
            if(robot.currentDirection == 'N'){
                if(m.map[robot.currentPosition.x+2][robot.currentPosition.y-1] == 1
                        && m.map[robot.currentPosition.x+2][robot.currentPosition.y] == 1
                        && m.map[robot.currentPosition.x+2][robot.currentPosition.y+1] == 1)
                    res = true;
            } else if(robot.currentDirection == 'S'){
                if(m.map[robot.currentPosition.x-2][robot.currentPosition.y-1] == 1
                        && m.map[robot.currentPosition.x-2][robot.currentPosition.y] == 1
                        && m.map[robot.currentPosition.x-2][robot.currentPosition.y+1] == 1)
                    res = true;
            } else if(robot.currentDirection == 'E'){
                if(m.map[robot.currentPosition.x-1][robot.currentPosition.y+2] == 1
                        && m.map[robot.currentPosition.x][robot.currentPosition.y+2] == 1
                        && m.map[robot.currentPosition.x+1][robot.currentPosition.y+2] == 1)
                    res = true;
            } else if(robot.currentDirection == 'W'){
                if(m.map[robot.currentPosition.x-1][robot.currentPosition.y-2] == 1
                        && m.map[robot.currentPosition.x][robot.currentPosition.y-2] == 1
                        && m.map[robot.currentPosition.x+1][robot.currentPosition.y-2] == 1)
                    res = true;
            }
        } catch(ArrayIndexOutOfBoundsException e){ }

        return res;
    }

    private boolean checkRight(){
        boolean res = false;

        try {
            if(robot.currentDirection == 'N'){
                if(m.map[robot.currentPosition.x-1][robot.currentPosition.y+2] == 1
                        && m.map[robot.currentPosition.x][robot.currentPosition.y+2] == 1
                        && m.map[robot.currentPosition.x+1][robot.currentPosition.y+2] == 1)
                    res = true;
            } else if(robot.currentDirection == 'S'){
                if(m.map[robot.currentPosition.x-1][robot.currentPosition.y-2] == 1
                        && m.map[robot.currentPosition.x][robot.currentPosition.y-2] == 1
                        && m.map[robot.currentPosition.x+1][robot.currentPosition.y-2] == 1)
                    res = true;
            } else if(robot.currentDirection == 'E'){
                if(m.map[robot.currentPosition.x-2][robot.currentPosition.y-1] == 1
                        && m.map[robot.currentPosition.x-2][robot.currentPosition.y] == 1
                        && m.map[robot.currentPosition.x-2][robot.currentPosition.y+1] == 1)
                    res = true;
            } else if(robot.currentDirection == 'W'){
                if(m.map[robot.currentPosition.x+2][robot.currentPosition.y-1] == 1
                        && m.map[robot.currentPosition.x+2][robot.currentPosition.y] == 1
                        && m.map[robot.currentPosition.x+2][robot.currentPosition.y+1] == 1)
                    res = true;
            }
        } catch(ArrayIndexOutOfBoundsException e){ }

        return res;
    }

    private void fastestPath(){
        switch(path[pathMove]) {
            case 'M':
                move();
                System.out.println("Moved");
                break;
            case 'L':
                turnLeft();
                System.out.println("TL");
                break;
            case 'R':
                turnRight();
                System.out.println("TR");
                break;
            case 'B':
                turnBack();
                System.out.println("TB");
        }
        updateMap();
        pathMove++;
        if(pathMove == path.length) fpTimer.stop();
    }

    private void goBackToStart(){
        switch(path[pathMove]) {
            case 'M':
                move();
                System.out.println("Moved");
                break;
            case 'L':
                turnLeft();
                System.out.println("TL");
                break;
            case 'R':
                turnRight();
                System.out.println("TR");
                break;
            case 'B':
                turnBack();
                System.out.println("TB");
        }
        updateMap();
        pathMove++;
        if(pathMove == path.length) exploreFPTimer.stop();
    }

}