import java.awt.*;

public class ActualRun {
    private Map m;
    private Robot robot;
    Point start = new Point(1, 1);
    Point end = new Point(18, 13);
    Point waypoint;
    FastestPath fp;
    char path[];
    int time = 0;
    TCPConnection connection;

    public ActualRun(){
        connection = new TCPConnection("192.168.5.158", 5003);

        m = new Map();
        robot = new Robot();
    }

    public void run(){
        while(true){
            String option = connection.getMessage();

            if(option.equals("start_e")){
                explore();
                String wpString = connection.getMessage();
                waypoint = new Point(wpString.charAt(6), wpString.charAt(9));

            } else if(option.equals("start_f")){
                if(waypoint != null){
                    path = fp.getFastestPathWaypoint(start, end, robot.currentDirection, waypoint);
                } else{
                    path = fp.getFastestPath(start, end, robot.currentDirection);
                }
                fastestPath();
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
        connection.sendMessage("TURN LEFT");
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
        connection.sendMessage("TURN RIGHT");
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
        connection.sendMessage("MOVE");
    }

    public void scan(){
        int results[] = connection.getSensorReadings();
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
                        System.out.println(results[0]);
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

    }

    private void explore(){

        while(!m.isBackAtStart) {
            m.printMap();
            scan();
            if (checkLeft()) {
                turnLeft();
                move();
            } else if (checkFront()) {
                move();
            } else if (checkRight()) {
                turnRight();
                move();
            } else {
                turnRight();
            }
            if(robot.currentPosition.x == 1 && robot.currentPosition.y == 1){
                m.isBackAtStart = true;
            }
        }

        if(robot.currentDirection == 'W'){
            turnRight();
        } else if(robot.currentDirection == 'S'){
            turnBack();
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
        for(int i = 0; i < path.length; i++){
            switch(path[i]) {
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
        }
    }

    private void updateAndroid(){
        String robotDirection = "";
        if(robot.currentDirection == 'N') robotDirection = "u";
        else if(robot.currentDirection == 'S') robotDirection = "d";
        else if(robot.currentDirection == 'E') robotDirection = "r";
        else if(robot.currentDirection == 'W') robotDirection = "l";
        connection.sendMessage("loc_{" + robot.currentPosition.x + "," + robot.currentPosition.y + "," + robotDirection + "}");

        connection.sendMessage("loc_{" + m.getMDFExplored() + "," + m.getMDFObstacle() + "}");
    }

}