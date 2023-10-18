import java.util.ArrayList;

public class State implements Comparable<State>{

    OrderedPair player = new OrderedPair(0,0);
    ArrayList<OrderedPair> posBox = new ArrayList<>();
    int hueristic;

    public State(OrderedPair player, ArrayList<OrderedPair> posBox, int i)
    {
        this.player= player;
        this.posBox = posBox;
        hueristic = i;
    }
    
    public OrderedPair getPlayer() {
        return player;
    }

   public ArrayList<OrderedPair> getPosBox() {
       return posBox;
   }

    public void setHueristic(int hueristic) {
        this.hueristic = hueristic;
    }

    @Override
    public int compareTo(State o) {
        if (hueristic < o.hueristic)
            return -1;
        else if (hueristic > o.hueristic)  
            return 1;
        return 0;
    }

    
}
