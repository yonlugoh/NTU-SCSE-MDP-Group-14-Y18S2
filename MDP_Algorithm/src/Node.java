import java.awt.*;

public class Node{
    Node parent;
    int x, y;
    int g, h, cost;

    public Node(int inputX, int inputY, int inputG, int inputH){
        x = inputX;
        y = inputY;
        g = inputG;
        h = inputH;
        cost = g + h;
        parent = null;
    }
}
