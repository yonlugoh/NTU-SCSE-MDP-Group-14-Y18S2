import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class FastestPath {

    int fpMap[][];
    ArrayList<Point> closedList;
    ArrayList<Node> openList;

    public FastestPath(int[][] exploredMap){

        fpMap = new int[20][15];
        for(int i = 0; i < 20; i++){
            for(int j = 0; j < 15; j++){
                fpMap[i][j] = exploredMap[i][j];
            }
        }
        for(int i = 0; i < 15; i++){
            fpMap[0][i] = 0;
            fpMap[19][i] = 0;
        }
        for(int i = 0; i < 20; i++){
            fpMap[i][0] = 0;
            fpMap[i][14] = 0;
        }
        for(int i = 1; i < 19; i++){
            for(int j = 1; j < 14; j++){
                if(exploredMap[i][j] == 1){
                    if(!(exploredMap[i+1][j-1] == 1 &&
                            exploredMap[i+1][j] == 1 &&
                            exploredMap[i+1][j+1] == 1 &&
                            exploredMap[i][j-1] == 1 &&
                            exploredMap[i][j+1] == 1 &&
                            exploredMap[i-1][j-1] == 1 &&
                            exploredMap[i-1][j] == 1 &&
                            exploredMap[i-1][j+1] == 1)){
                        fpMap[i][j] = 0;
                    }
                }
            }
        }
    }

    public ArrayList<Point> getFastestPoints(Point start, Point end){
        ArrayList<Point> path = new ArrayList<Point>();
        closedList = new ArrayList<Point>();
        openList = new ArrayList<Node>();
        openList.add(new Node(start.x, start.y, 0, Math.abs(end.x - start.x) + Math.abs(end.y - start.y)));
        Node current;
        int currentIndex;

        while(!openList.isEmpty()){
            currentIndex = getSmallestIndex();
            current = openList.get(currentIndex);

            if(current.x == end.x && current.y == end.y){

                while(current.parent != null){
                    path.add(0, new Point(current.x, current.y));
                    current = current.parent;
                }
                path.add(0, new Point(start.x, start.y));
                return path;

            } else{
                closedList.add(new Point(current.x, current.y));
                openList.remove(currentIndex);
                int posX = current.x;
                int posY = current.y;
                boolean isClosed = false;

                if(fpMap[posX+1][posY] == 1){
                    isClosed = false;
                    for(int i = 0; i < closedList.size(); i++){
                        if(closedList.get(i).x == posX+1 && closedList.get(i).y == posY){
                            isClosed = true;
                            break;
                        }
                    }
                    if(isClosed) continue;

                    int tempIndex = -1;
                    for(int i = 0; i < openList.size(); i++){
                        if(openList.get(i).x == posX+1 && openList.get(i).y == posY){
                            tempIndex = i;
                            break;
                        }
                    }
                    if(tempIndex == -1){
                        Node newNode = new Node(posX+1, posY, current.g+1, Math.abs(end.x - (posX+1)) + Math.abs(end.y - posY));
                        newNode.parent = current;
                        openList.add(newNode);
                    }
                }

                if(fpMap[posX][posY+1] == 1){
                    isClosed = false;
                    for(int i = 0; i < closedList.size(); i++){
                        if(closedList.get(i).x == posX && closedList.get(i).y == posY+1){
                            isClosed = true;
                            break;
                        }
                    }
                    if(isClosed) continue;

                    int tempIndex = -1;
                    for(int i = 0; i < openList.size(); i++){
                        if(openList.get(i).x == posX && openList.get(i).y == posY+1){
                            tempIndex = i;
                            break;
                        }
                    }
                    if(tempIndex == -1){
                        Node newNode = new Node(posX, posY+1, current.g+1, Math.abs(end.x - posX) + Math.abs(end.y - (posY+1)));
                        newNode.parent = current;
                        openList.add(newNode);
                    }
                }

                if(fpMap[posX-1][posY] == 1){
                    isClosed = false;
                    for(int i = 0; i < closedList.size(); i++){
                        if(closedList.get(i).x == posX-1 && closedList.get(i).y == posY){
                            isClosed = true;
                            break;
                        }
                    }
                    if(isClosed) continue;

                    int tempIndex = -1;
                    for(int i = 0; i < openList.size(); i++){
                        if(openList.get(i).x == posX-1 && openList.get(i).y == posY){
                            tempIndex = i;
                            break;
                        }
                    }
                    if(tempIndex == -1){
                        Node newNode = new Node(posX-1, posY, current.g+1, Math.abs(end.x - (posX-1)) + Math.abs(end.y - posY));
                        newNode.parent = current;
                        openList.add(newNode);
                    }
                }

                if(fpMap[posX][posY-1] == 1){
                    isClosed = false;
                    for(int i = 0; i < closedList.size(); i++){
                        if(closedList.get(i).x == posX && closedList.get(i).y == posY-1){
                            isClosed = true;
                            break;
                        }
                    }
                    if(isClosed) continue;

                    int tempIndex = -1;
                    for(int i = 0; i < openList.size(); i++){
                        if(openList.get(i).x == posX && openList.get(i).y == posY-1){
                            tempIndex = i;
                            break;
                        }
                    }
                    if(tempIndex == -1){
                        Node newNode = new Node(posX, posY-1, current.g+1, Math.abs(end.x - posX) + Math.abs(end.y - (posY-1)));
                        newNode.parent = current;
                        openList.add(newNode);
                    }
                }

            }
        }
        System.out.println("No valid path!");
        return path;
    }

    private int getSmallestIndex(){
        int res = 0;
        int currentCost = 1000;
        for(int i = 0; i < openList.size(); i++){
            if(openList.get(i).cost < currentCost){
                currentCost = openList.get(i).cost;
                res = i;
            }
        }
        return res;
    }

    public char[] getFastestPath(Point start, Point end, char dir){
        StringBuilder moves = new StringBuilder();
        char curD = dir;

        ArrayList<Point> fp = getFastestPoints(start, end);
        for(int i = 1; i < fp.size(); i++){
            if(fp.get(i).x - fp.get(i-1).x == 1){
                if(curD == 'N'){
                    moves.append('M');
                } else if(curD == 'S'){
                    moves.append('B');
                    moves.append('M');
                } else if(curD == 'E'){
                    moves.append('L');
                    moves.append('M');
                } else if(curD == 'W'){
                    moves.append('R');
                    moves.append('M');
                }
                curD = 'N';
            } else if(fp.get(i).x - fp.get(i-1).x == -1){
                if(curD == 'N'){
                    moves.append('B');
                    moves.append('M');
                } else if(curD == 'S'){
                    moves.append('M');
                } else if(curD == 'E'){
                    moves.append('R');
                    moves.append('M');
                } else if(curD == 'W'){
                    moves.append('L');
                    moves.append('M');
                }
                curD = 'S';
            } else if(fp.get(i).y - fp.get(i-1).y == 1){
                if(curD == 'N'){
                    moves.append('R');
                    moves.append('M');
                } else if(curD == 'S'){
                    moves.append('L');
                    moves.append('M');
                } else if(curD == 'E'){
                    moves.append('M');
                } else if(curD == 'W'){
                    moves.append('B');
                    moves.append('M');
                }
                curD = 'E';
            } else if(fp.get(i).y - fp.get(i-1).y == -1){
                if(curD == 'N'){
                    moves.append('L');
                    moves.append('M');
                } else if(curD == 'S'){
                    moves.append('R');
                    moves.append('M');
                } else if(curD == 'E'){
                    moves.append('B');
                    moves.append('M');
                } else if(curD == 'W'){
                    moves.append('M');
                }
                curD = 'W';
            }
        }
        return moves.toString().toCharArray();
    }

    public char[] getFastestPathWaypoint(Point start, Point end, char dir, Point waypoint){
        StringBuilder moves = new StringBuilder();
        char curD = dir;

        ArrayList<Point> fp = getFastestPoints(start, waypoint);
        for(int i = 1; i < fp.size(); i++){
            if(fp.get(i).x - fp.get(i-1).x == 1){
                if(curD == 'N'){
                    moves.append('M');
                } else if(curD == 'S'){
                    moves.append('B');
                    moves.append('M');
                } else if(curD == 'E'){
                    moves.append('L');
                    moves.append('M');
                } else if(curD == 'W'){
                    moves.append('R');
                    moves.append('M');
                }
                curD = 'N';
            } else if(fp.get(i).x - fp.get(i-1).x == -1){
                if(curD == 'N'){
                    moves.append('B');
                    moves.append('M');
                } else if(curD == 'S'){
                    moves.append('M');
                } else if(curD == 'E'){
                    moves.append('R');
                    moves.append('M');
                } else if(curD == 'W'){
                    moves.append('L');
                    moves.append('M');
                }
                curD = 'S';
            } else if(fp.get(i).y - fp.get(i-1).y == 1){
                if(curD == 'N'){
                    moves.append('R');
                    moves.append('M');
                } else if(curD == 'S'){
                    moves.append('L');
                    moves.append('M');
                } else if(curD == 'E'){
                    moves.append('M');
                } else if(curD == 'W'){
                    moves.append('B');
                    moves.append('M');
                }
                curD = 'E';
            } else if(fp.get(i).y - fp.get(i-1).y == -1){
                if(curD == 'N'){
                    moves.append('L');
                    moves.append('M');
                } else if(curD == 'S'){
                    moves.append('R');
                    moves.append('M');
                } else if(curD == 'E'){
                    moves.append('B');
                    moves.append('M');
                } else if(curD == 'W'){
                    moves.append('M');
                }
                curD = 'W';
            }
        }

        fp = getFastestPoints(waypoint, end);
        for(int i = 1; i < fp.size(); i++){
            if(fp.get(i).x - fp.get(i-1).x == 1){
                if(curD == 'N'){
                    moves.append('M');
                } else if(curD == 'S'){
                    moves.append('B');
                    moves.append('M');
                } else if(curD == 'E'){
                    moves.append('L');
                    moves.append('M');
                } else if(curD == 'W'){
                    moves.append('R');
                    moves.append('M');
                }
                curD = 'N';
            } else if(fp.get(i).x - fp.get(i-1).x == -1){
                if(curD == 'N'){
                    moves.append('B');
                    moves.append('M');
                } else if(curD == 'S'){
                    moves.append('M');
                } else if(curD == 'E'){
                    moves.append('R');
                    moves.append('M');
                } else if(curD == 'W'){
                    moves.append('L');
                    moves.append('M');
                }
                curD = 'S';
            } else if(fp.get(i).y - fp.get(i-1).y == 1){
                if(curD == 'N'){
                    moves.append('R');
                    moves.append('M');
                } else if(curD == 'S'){
                    moves.append('L');
                    moves.append('M');
                } else if(curD == 'E'){
                    moves.append('M');
                } else if(curD == 'W'){
                    moves.append('B');
                    moves.append('M');
                }
                curD = 'E';
            } else if(fp.get(i).y - fp.get(i-1).y == -1){
                if(curD == 'N'){
                    moves.append('L');
                    moves.append('M');
                } else if(curD == 'S'){
                    moves.append('R');
                    moves.append('M');
                } else if(curD == 'E'){
                    moves.append('B');
                    moves.append('M');
                } else if(curD == 'W'){
                    moves.append('M');
                }
                curD = 'W';
            }
        }
        return moves.toString().toCharArray();
    }

}
