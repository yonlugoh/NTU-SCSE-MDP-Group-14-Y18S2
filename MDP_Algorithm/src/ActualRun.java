import java.awt.*;

public class ActualRun {
    private Map m;
    private Robot robot;
    Point start = new Point(1, 1);
    Point end = new Point(18, 13);
    Point waypoint;
    FastestPath fp;
    String fastestMoves = "F";
    char path[];
    TCPConnection connection;

    public ActualRun(){
        connection = new TCPConnection("192.168.14.14", 5003);

        m = new Map();
        robot = new Robot();
    }

    public void run(){
        while(true){
            String option = connection.getMessage();

            if(option.equals("start_e")){
                System.out.println("Starting exploration");
                scan(true);
                explore();
                if(waypoint != null){
                    path = fp.getFastestPathWaypoint(start, end, robot.currentDirection, waypoint);
                } else{
                    path = fp.getFastestPath(start, end, robot.currentDirection);
                }
                processFastestPath();
            } else if(option.equals("start_f")){
                fastestPath();
            } else if(option.substring(0,5).equals("set_w")){
                waypoint = new Point(option.charAt(6), option.charAt(9));
                System.out.println("Waypoint : " + waypoint.x + ", " + waypoint.y);
            }
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
        connection.sendMessage("sL");
        scan(false);
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
        connection.sendMessage("sR");
        scan(false);
    }

    private void turnBack(){
        if(robot.currentDirection == 'N'){
            robot.currentDirection = 'S';
        } else if(robot.currentDirection == 'S') {
            robot.currentDirection = 'N';
        } else if(robot.currentDirection == 'E') {
            robot.currentDirection = 'W';
        } else if(robot.currentDirection == 'W'){
            robot.currentDirection = 'E';
        }
        connection.sendMessage("sB");
        scan(false);
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
        connection.sendMessage("sM");
        scan(false);
    }

    public void scan(boolean firstScan){
        int results[];
        if(firstScan){
            results = connection.getSensorReadings();
        } else{
            results = connection.getSensorReadings2();
        }

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
                    try{
                        for(int i = 1; i < results[0]; i++){
                            m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 1 - i] = 1;
                        }
                    } catch(Exception e2){}
                }
            } else{
                try{
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 2] = 1;
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 3] = 1;
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 4] = 1;
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 5] = 1;
                } catch(Exception e){}
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
                    try{
                        for(int i = 1; i < results[1]; i++){
                            m.map[robot.currentPosition.x + 1][robot.currentPosition.y - 1 - i] = 1;
                        }
                    } catch(Exception e2){}
                }
            } else{
                try{
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y - 2] = 1;
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y - 3] = 1;
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y - 4] = 1;
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y - 5] = 1;
                } catch(Exception e){}
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
                    try{
                        for(int i = 1; i < results[2]; i++){
                            m.map[robot.currentPosition.x + 1 + i][robot.currentPosition.y - 1] = 1;
                        }
                    } catch(Exception e2){}
                }
            } else{
                try{
                    m.map[robot.currentPosition.x + 2][robot.currentPosition.y - 1] = 1;
                    m.map[robot.currentPosition.x + 3][robot.currentPosition.y - 1] = 1;
                    m.map[robot.currentPosition.x + 4][robot.currentPosition.y - 1] = 1;
                    m.map[robot.currentPosition.x + 5][robot.currentPosition.y - 1] = 1;
                } catch(Exception e){}
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
                    try{
                        for(int i = 1; i < results[3]; i++){
                            m.map[robot.currentPosition.x + 1 + i][robot.currentPosition.y] = 1;
                        }
                    } catch(Exception e2){}
                }
            } else{
                try{
                    m.map[robot.currentPosition.x + 2][robot.currentPosition.y] = 1;
                    m.map[robot.currentPosition.x + 3][robot.currentPosition.y] = 1;
                    m.map[robot.currentPosition.x + 4][robot.currentPosition.y] = 1;
                    m.map[robot.currentPosition.x + 5][robot.currentPosition.y] = 1;
                } catch(Exception e){}
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
                    try{
                        for(int i = 1; i < results[4]; i++){
                            m.map[robot.currentPosition.x + 1 + i][robot.currentPosition.y + 1] = 1;
                        }
                    } catch(Exception e2){}
                }
            } else{
                try{
                    m.map[robot.currentPosition.x + 2][robot.currentPosition.y + 1] = 1;
                    m.map[robot.currentPosition.x + 3][robot.currentPosition.y + 1] = 1;
                    m.map[robot.currentPosition.x + 4][robot.currentPosition.y + 1] = 1;
                    m.map[robot.currentPosition.x + 5][robot.currentPosition.y + 1] = 1;
                } catch(Exception e){}
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
                    try{
                        for(int i = 1; i < results[5]; i++){
                            m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 1 + i] = 1;
                        }
                    } catch(Exception e2){}
                }
            } else{
                try{
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 2] = 1;
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 3] = 1;
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 4] = 1;
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 5] = 1;
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 6] = 1;
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 7] = 1;
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 8] = 1;
                } catch(Exception e){}
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
                    try{
                        for(int i = 1; i < results[0]; i++){
                            m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 1 + i] = 1;
                        }
                    } catch(Exception e2){}
                }
            } else{
                try{
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 2] = 1;
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 3] = 1;
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 4] = 1;
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 5] = 1;
                } catch(Exception e){}
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
                    try{
                        for(int i = 1; i < results[1]; i++){
                            m.map[robot.currentPosition.x - 1][robot.currentPosition.y + 1 + i] = 1;
                        }
                    } catch(Exception e2){}
                }
            } else{
                try{
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y + 2] = 1;
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y + 3] = 1;
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y + 4] = 1;
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y + 5] = 1;
                } catch(Exception e){}
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
                    try{
                        for(int i = 1; i < results[2]; i++){
                            m.map[robot.currentPosition.x - 1 - i][robot.currentPosition.y + 1] = 1;
                        }
                    } catch(Exception e2){}
                }
            } else{
                try{
                    m.map[robot.currentPosition.x - 2][robot.currentPosition.y + 1] = 1;
                    m.map[robot.currentPosition.x - 3][robot.currentPosition.y + 1] = 1;
                    m.map[robot.currentPosition.x - 4][robot.currentPosition.y + 1] = 1;
                    m.map[robot.currentPosition.x - 5][robot.currentPosition.y + 1] = 1;
                } catch(Exception e){}
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
                    try{
                        for(int i = 1; i < results[3]; i++){
                            m.map[robot.currentPosition.x - 1 - i][robot.currentPosition.y] = 1;
                        }
                    } catch(Exception e2){}
                }
            } else{
                try{
                    m.map[robot.currentPosition.x - 2][robot.currentPosition.y] = 1;
                    m.map[robot.currentPosition.x - 3][robot.currentPosition.y] = 1;
                    m.map[robot.currentPosition.x - 4][robot.currentPosition.y] = 1;
                    m.map[robot.currentPosition.x - 5][robot.currentPosition.y] = 1;
                } catch(Exception e){}
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
                    try{
                        for(int i = 1; i < results[4]; i++){
                            m.map[robot.currentPosition.x - 1 - i][robot.currentPosition.y - 1] = 1;
                        }
                    } catch(Exception e2){}
                }
            } else{
                try{
                    m.map[robot.currentPosition.x - 2][robot.currentPosition.y - 1] = 1;
                    m.map[robot.currentPosition.x - 3][robot.currentPosition.y - 1] = 1;
                    m.map[robot.currentPosition.x - 4][robot.currentPosition.y - 1] = 1;
                    m.map[robot.currentPosition.x - 5][robot.currentPosition.y - 1] = 1;
                } catch(Exception e){}
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
                    try{
                        for(int i = 1; i < results[5]; i++){
                            m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 1 - i] = 1;
                        }
                    } catch(Exception e2){}
                }
            } else{
                try{
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 2] = 1;
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 3] = 1;
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 4] = 1;
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 5] = 1;
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 6] = 1;
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 7] = 1;
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 8] = 1;
                } catch(Exception e){}
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
                    try{
                        for(int i = 1; i < results[0]; i++){
                            m.map[robot.currentPosition.x + 1 + i][robot.currentPosition.y - 1] = 1;
                        }
                    } catch(Exception e2){}
                }
            } else{
                try{
                    m.map[robot.currentPosition.x + 2][robot.currentPosition.y - 1] = 1;
                    m.map[robot.currentPosition.x + 3][robot.currentPosition.y - 1] = 1;
                    m.map[robot.currentPosition.x + 4][robot.currentPosition.y - 1] = 1;
                    m.map[robot.currentPosition.x + 5][robot.currentPosition.y - 1] = 1;
                } catch(Exception e){}
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
                    try{
                        for(int i = 1; i < results[1]; i++){
                            m.map[robot.currentPosition.x + 1 + i][robot.currentPosition.y + 1] = 1;
                        }
                    } catch(Exception e2){}
                }
            } else{
                try{
                    m.map[robot.currentPosition.x + 2][robot.currentPosition.y + 1] = 1;
                    m.map[robot.currentPosition.x + 3][robot.currentPosition.y + 1] = 1;
                    m.map[robot.currentPosition.x + 4][robot.currentPosition.y + 1] = 1;
                    m.map[robot.currentPosition.x + 5][robot.currentPosition.y + 1] = 1;
                } catch(Exception e){}
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
                    try{
                        for(int i = 1; i < results[2]; i++){
                            m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 1 + i] = 1;
                        }
                    } catch(Exception e2){}
                }
            } else{
                try{
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 2] = 1;
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 3] = 1;
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 4] = 1;
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 5] = 1;
                } catch(Exception e){}
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
                    try{
                        for(int i = 1; i < results[3]; i++){
                            m.map[robot.currentPosition.x][robot.currentPosition.y + 1 + i] = 1;
                        }
                    } catch(Exception e2){}
                }
            } else{
                try{
                    m.map[robot.currentPosition.x][robot.currentPosition.y + 2] = 1;
                    m.map[robot.currentPosition.x][robot.currentPosition.y + 3] = 1;
                    m.map[robot.currentPosition.x][robot.currentPosition.y + 4] = 1;
                    m.map[robot.currentPosition.x][robot.currentPosition.y + 5] = 1;
                } catch(Exception e){}
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
                    try{
                        for(int i = 1; i < results[3]; i++){
                            m.map[robot.currentPosition.x - 1][robot.currentPosition.y + 1 + i] = 1;
                        }
                    } catch(Exception e2){}
                }
            } else{
                try{
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y + 2] = 1;
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y + 3] = 1;
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y + 4] = 1;
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y + 5] = 1;
                } catch(Exception e){}
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
                    try{
                        for(int i = 1; i < results[5]; i++){
                            m.map[robot.currentPosition.x - i][robot.currentPosition.y + 1] = 1;
                        }
                    } catch(Exception e2){}
                }
            } else{
                try{
                    m.map[robot.currentPosition.x - 2][robot.currentPosition.y + 1] = 1;
                    m.map[robot.currentPosition.x - 3][robot.currentPosition.y + 1] = 1;
                    m.map[robot.currentPosition.x - 4][robot.currentPosition.y + 1] = 1;
                    m.map[robot.currentPosition.x - 5][robot.currentPosition.y + 1] = 1;
                    m.map[robot.currentPosition.x - 6][robot.currentPosition.y + 1] = 1;
                    m.map[robot.currentPosition.x - 7][robot.currentPosition.y + 1] = 1;
                    m.map[robot.currentPosition.x - 8][robot.currentPosition.y + 1] = 1;
                } catch(Exception e){}
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
                    try{
                        for(int i = 1; i < results[0]; i++){
                            m.map[robot.currentPosition.x - 1 - i][robot.currentPosition.y + 1] = 1;
                        }
                    } catch(Exception e2){}
                }
            } else{
                try{
                    m.map[robot.currentPosition.x - 2][robot.currentPosition.y + 1] = 1;
                    m.map[robot.currentPosition.x - 3][robot.currentPosition.y + 1] = 1;
                    m.map[robot.currentPosition.x - 4][robot.currentPosition.y + 1] = 1;
                    m.map[robot.currentPosition.x - 5][robot.currentPosition.y + 1] = 1;
                } catch(Exception e){}
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
                    try{
                        for(int i = 1; i < results[1]; i++){
                            m.map[robot.currentPosition.x - 1 - i][robot.currentPosition.y - 1] = 1;
                        }
                    } catch(Exception e2){}
                }
            } else{
                try{
                    m.map[robot.currentPosition.x - 2][robot.currentPosition.y - 1] = 1;
                    m.map[robot.currentPosition.x - 3][robot.currentPosition.y - 1] = 1;
                    m.map[robot.currentPosition.x - 4][robot.currentPosition.y - 1] = 1;
                    m.map[robot.currentPosition.x - 5][robot.currentPosition.y - 1] = 1;
                } catch(Exception e){}
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
                    try{
                        for(int i = 1; i < results[2]; i++){
                            m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 1 - i] = 1;
                        }
                    } catch(Exception e2){}
                }
            } else{
                try{
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 2] = 1;
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 3] = 1;
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 4] = 1;
                    m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 5] = 1;
                } catch(Exception e){}
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
                    try{
                        for(int i = 1; i < results[3]; i++){
                            m.map[robot.currentPosition.x][robot.currentPosition.y - 1 - i] = 1;
                        }
                    } catch(Exception e2){}
                }
            } else{
                try{
                    m.map[robot.currentPosition.x][robot.currentPosition.y - 2] = 1;
                    m.map[robot.currentPosition.x][robot.currentPosition.y - 3] = 1;
                    m.map[robot.currentPosition.x][robot.currentPosition.y - 4] = 1;
                    m.map[robot.currentPosition.x][robot.currentPosition.y - 5] = 1;
                } catch(Exception e){}
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
                    try{
                        for(int i = 1; i < results[3]; i++){
                            m.map[robot.currentPosition.x + 1][robot.currentPosition.y - 1 - i] = 1;
                        }
                    } catch(Exception e2){}
                }
            } else{
                try{
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y - 2] = 1;
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y - 3] = 1;
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y - 4] = 1;
                    m.map[robot.currentPosition.x + 1][robot.currentPosition.y - 5] = 1;
                } catch(Exception e){}
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
                    try{
                        for(int i = 1; i < results[5]; i++){
                            m.map[robot.currentPosition.x + 1 + i][robot.currentPosition.y - 1] = 1;
                        }
                    } catch(Exception e2){}
                }
            } else{
                try{
                    m.map[robot.currentPosition.x + 2][robot.currentPosition.y - 1] = 1;
                    m.map[robot.currentPosition.x + 3][robot.currentPosition.y - 1] = 1;
                    m.map[robot.currentPosition.x + 4][robot.currentPosition.y - 1] = 1;
                    m.map[robot.currentPosition.x + 5][robot.currentPosition.y - 1] = 1;
                    m.map[robot.currentPosition.x + 6][robot.currentPosition.y - 1] = 1;
                    m.map[robot.currentPosition.x + 7][robot.currentPosition.y - 1] = 1;
                    m.map[robot.currentPosition.x + 8][robot.currentPosition.y - 1] = 1;
                } catch(Exception e){}
            }
        }

    }

    private void explore(){

        while(!m.isBackAtStart) {
            //updateAndroid();
            if(checkCalibration()){
                connection.sendMessage("sC");
                connection.getMessage();
            }
            if (checkLeft()) {
                turnLeft();
                move();
            } else if (checkFront()) {
                move();
            } else if (checkRight()) {
                turnRight();
                move();
            } else {
                turnBack();
            }
            updateAndroid();
            if(robot.currentPosition.x == 1 && robot.currentPosition.y == 1){
                m.isBackAtStart = true;
            }
        }

        if(robot.currentDirection == 'W'){
            turnRight();
        } else if(robot.currentDirection == 'S'){
            turnBack();
        }
        updateAndroid();
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

    private void processFastestPath(){
        for(int i = 0; i < path.length; i++){
            switch(path[i]) {
                case 'M':
                    int j = 1;
                    for(; i < path.length; i++){
                        if(i+1 < path.length && path[i+1] == 'M'){
                            j++;
                        } else break;
                    }
                    fastestMoves += 'M';
                    fastestMoves += j;
                    break;
                case 'L':
                    fastestMoves += 'L';
                    break;
                case 'R':
                    fastestMoves += 'R';
                    break;
                case 'B':
                    fastestMoves += 'B';
            }
        }
    }

    private void fastestPath(){
        connection.sendMessage(fastestMoves);
    }

    private void updateAndroid(){
        String robotDirection = "";
        if(robot.currentDirection == 'N') robotDirection = "u";
        else if(robot.currentDirection == 'S') robotDirection = "d";
        else if(robot.currentDirection == 'E') robotDirection = "r";
        else if(robot.currentDirection == 'W') robotDirection = "l";
        /*connection.sendMessage("bloc_{" + robot.currentPosition.x + "," + robot.currentPosition.y + "," + robotDirection + "}");
        connection.getMessage();
        connection.sendMessage("bmap_{" + m.getMDFExplored() + "," + m.getMDFObstacle() + "}");
        connection.getMessage();*/
        connection.sendMessage("bmap_{" + robot.currentPosition.x + "," + robot.currentPosition.y + "," + robotDirection + "," + m.getMDFExplored() + "," + m.getMDFObstacle() + "}");
    }

    private boolean checkCalibration(){
        boolean result = false;
        if(robot.currentDirection == 'N'){
            try{
                if(m.map[robot.currentPosition.x + 1][robot.currentPosition.y - 2] == 1
                        && m.map[robot.currentPosition.x][robot.currentPosition.y - 2] == 1
                        && m.map[robot.currentPosition.x - 1][robot.currentPosition.y - 2] == 1){
                    result = true;
                }
            } catch(Exception e){
                result = true;
            }
        } else if(robot.currentDirection == 'S'){
            try{
                if(m.map[robot.currentPosition.x + 1][robot.currentPosition.y + 2] == 1
                        && m.map[robot.currentPosition.x][robot.currentPosition.y + 2] == 1
                        && m.map[robot.currentPosition.x - 1][robot.currentPosition.y + 2] == 1){
                    result = true;
                }
            } catch(Exception e){
                result = true;
            }
        } else if(robot.currentDirection == 'E'){
            try{
                if(m.map[robot.currentPosition.x + 2][robot.currentPosition.y - 1] == 1
                        && m.map[robot.currentPosition.x + 2][robot.currentPosition.y] == 1
                        && m.map[robot.currentPosition.x + 2][robot.currentPosition.y + 1] == 1){
                    result = true;
                }
            } catch(Exception e){
                result = true;
            }
        } else if(robot.currentDirection == 'W'){
            try{
                if(m.map[robot.currentPosition.x - 2][robot.currentPosition.y - 1] == 1
                        && m.map[robot.currentPosition.x - 2][robot.currentPosition.y] == 1
                        && m.map[robot.currentPosition.x - 2][robot.currentPosition.y + 1] == 1){
                    result = true;
                }
            } catch(Exception e){
                result = true;
            }
        }
        return result;
    }

}