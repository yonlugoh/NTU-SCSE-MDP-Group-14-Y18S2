import java.awt.*;

public class Robot {

    Point currentPosition;
    char currentDirection;

    public Robot(){
        currentPosition = new Point(1, 1);
        currentDirection = 'N';
    }

    public int[] getSensorReadings(int[][] simulatedMap){

        int[] res = new int[]{0,0,0,0,0,0};
        int i = 0;

        if(this.currentDirection == 'N'){

            //Sensor 1
            try{
                for(i = 1; i < 4; i++){
                    if(simulatedMap[this.currentPosition.x - 1][this.currentPosition.y - 1 - i] == 1){
                        res[0] = i;
                        break;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                res[0] = i;
            }

            //Sensor 2
            try{
                for(i = 1; i < 4; i++){
                    if(simulatedMap[this.currentPosition.x + 1][this.currentPosition.y - 1 - i] == 1){
                        res[1] = i;
                        break;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                res[1] = i;
            }

            //Sensor 3
            try{
                for(i = 1; i < 4; i++){
                    if(simulatedMap[this.currentPosition.x + 1 + i][this.currentPosition.y - 1] == 1){
                        res[2] = i;
                        break;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                res[2] = i;
            }

            //Sensor 4
            try{
                for(i = 1; i < 4; i++){
                    if(simulatedMap[this.currentPosition.x + 1 + i][this.currentPosition.y] == 1){
                        res[3] = i;
                        break;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                res[3] = i;
            }

            //Sensor 5
            try{
                for(i = 1; i < 4; i++){
                    if(simulatedMap[this.currentPosition.x + 1 + i][this.currentPosition.y + 1] == 1){
                        res[4] = i;
                        break;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                res[4] = i;
            }

            //Sensor 6
            try{
                for(i = 1; i < 5; i++){
                    if(simulatedMap[this.currentPosition.x + 1][this.currentPosition.y + 1 + i] == 1){
                        res[5] = i;
                        break;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                res[5] = i;
            }

        } else if(this.currentDirection == 'S'){

            //Sensor 1
            try{
                for(i = 1; i < 4; i++){
                    if(simulatedMap[this.currentPosition.x + 1][this.currentPosition.y + 1 + i] == 1){
                        res[0] = i;
                        break;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                res[0] = i;
            }

            //Sensor 2
            try{
                for(i = 1; i < 4; i++){
                    if(simulatedMap[this.currentPosition.x - 1][this.currentPosition.y + 1 + i] == 1){
                        res[1] = i;
                        break;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                res[1] = i;
            }

            //Sensor 3
            try{
                for(i = 1; i < 4; i++){
                    if(simulatedMap[this.currentPosition.x - 1 - i][this.currentPosition.y + 1] == 1){
                        res[2] = i;
                        break;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                res[2] = i;
            }

            //Sensor 4
            try{
                for(i = 1; i < 4; i++){
                    if(simulatedMap[this.currentPosition.x - 1 - i][this.currentPosition.y] == 1){
                        res[3] = i;
                        break;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                res[3] = i;
            }

            //Sensor 5
            try{
                for(i = 1; i < 4; i++){
                    if(simulatedMap[this.currentPosition.x - 1 - i][this.currentPosition.y - 1] == 1){
                        res[4] = i;
                        break;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                res[4] = i;
            }

            //Sensor 6
            try{
                for(i = 1; i < 5; i++){
                    if(simulatedMap[this.currentPosition.x - 1][this.currentPosition.y - 1 - i] == 1){
                        res[5] = i;
                        break;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                res[5] = i;
            }

        } else if(this.currentDirection == 'E'){

            //Sensor 1
            try{
                for(i = 1; i < 4; i++){
                    if(simulatedMap[this.currentPosition.x + 1 + i][this.currentPosition.y - 1] == 1){
                        res[0] = i;
                        break;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                res[0] = i;
            }

            //Sensor 2
            try{
                for(i = 1; i < 4; i++){
                    if(simulatedMap[this.currentPosition.x + 1 + i][this.currentPosition.y + 1] == 1){
                        res[1] = i;
                        break;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                res[1] = i;
            }

            //Sensor 3
            try{
                for(i = 1; i < 4; i++){
                    if(simulatedMap[this.currentPosition.x + 1][this.currentPosition.y + 1 + i] == 1){
                        res[2] = i;
                        break;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                res[2] = i;
            }

            //Sensor 4
            try{
                for(i = 1; i < 4; i++){
                    if(simulatedMap[this.currentPosition.x][this.currentPosition.y + 1 + i] == 1){
                        res[3] = i;
                        break;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                res[3] = i;
            }

            //Sensor 5
            try{
                for(i = 1; i < 4; i++){
                    if(simulatedMap[this.currentPosition.x - 1][this.currentPosition.y + 1 + i] == 1){
                        res[4] = i;
                        break;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                res[4] = i;
            }

            //Sensor 6
            try{
                for(i = 1; i < 5; i++){
                    if(simulatedMap[this.currentPosition.x - 1 - i][this.currentPosition.y + 1] == 1){
                        res[5] = i;
                        break;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                res[5] = i;
            }

        } else if(this.currentDirection == 'W'){

            //Sensor 1
            try{
                for(i = 1; i < 4; i++){
                    if(simulatedMap[this.currentPosition.x - 1 - i][this.currentPosition.y + 1] == 1){
                        res[0] = i;
                        break;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                res[0] = i;
            }

            //Sensor 2
            try{
                for(i = 1; i < 4; i++){
                    if(simulatedMap[this.currentPosition.x - 1 - i][this.currentPosition.y - 1] == 1){
                        res[1] = i;
                        break;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                res[1] = i;
            }

            //Sensor 3
            try{
                for(i = 1; i < 4; i++){
                    if(simulatedMap[this.currentPosition.x - 1][this.currentPosition.y - 1 - i] == 1){
                        res[2] = i;
                        break;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                res[2] = i;
            }

            //Sensor 4
            try{
                for(i = 1; i < 4; i++){
                    if(simulatedMap[this.currentPosition.x][this.currentPosition.y - 1 - i] == 1){
                        res[3] = i;
                        break;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                res[3] = i;
            }

            //Sensor 5
            try{
                for(i = 1; i < 4; i++){
                    if(simulatedMap[this.currentPosition.x + 1][this.currentPosition.y - 1 - i] == 1){
                        res[4] = i;
                        break;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                res[4] = i;
            }

            //Sensor 6
            try{
                for(i = 1; i < 5; i++){
                    if(simulatedMap[this.currentPosition.x + 1 + i][this.currentPosition.y - 1] == 1){
                        res[5] = i;
                        break;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                res[5] = i;
            }

        }

        return res;
    }

}
