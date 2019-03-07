public class Main {

    //static JButton[][] map = new JButton[15][20];

    public static void main(String[] args) {
        //Simulator simulator = new Simulator();
        ActualRun actualRun = new ActualRun();
        actualRun.run();

        //TCPConnection connect = new TCPConnection("192.168.59.130", 5003);
        //int[] result = connect.getSensorReadings();
        //System.out.println("Response: " + result);
        //connect.sendMessage("bB");
        //connect.getMessage();
        //int[] temp = connect.getSensorReadings2();
        //System.out.println(temp[0] + " " + temp[1] + " " + temp[2] + " " + temp[3] + " " + temp[4] + " " + temp[5]);
    }
}
