
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

public class SokoBot {

    ArrayList<OrderedPair> mapDataAL = new ArrayList();
    ArrayList<OrderedPair> itemsDataAL = new ArrayList();

    ArrayList<OrderedPair> walls = new ArrayList();
    ArrayList<OrderedPair> goals = new ArrayList();

    OrderedPair player;
    ArrayList<OrderedPair> crates = new ArrayList();

    
    // Arraylist itemsData (1-wall, 2-goal, 3-blankspace)
    public void convertMapData(char[][] mapData)
    {
        for(int i=0;i< mapData.length;i++)
            for (int j=0;j<mapData[i].length;j++)
            {
                if (mapData[i][j] == '#') 
                {
                    mapDataAL.add(new OrderedPair(i, j)); // is a walls
                    walls.add(new OrderedPair(i, j));
                }
                else if (mapData[i][j] == '.') 
                {
                    mapDataAL.add(new OrderedPair(i, j)); //  is a goal
                    goals.add(new OrderedPair(i, j));
                }
                else
                    mapDataAL.add(new OrderedPair(i, j)); // is a blank space
            }
    }

    // Arraylist itemsData (4-player, 5-crate, 3-blankspace)
    public void convertItemsData(char[][] itemsData)
    {
        for(int i=0;i< itemsData.length;i++)
            for (int j=0;j<itemsData[i].length;j++)
            {
                if (itemsData[i][j] == '@') 
                {
                    mapDataAL.add(new OrderedPair(i, j)); // is a player
                    player = new OrderedPair(i, j);
                }
                    
                else if (itemsData[i][j] == '$') 
                {
                    mapDataAL.add(new OrderedPair(i, j)); //  is a crate
                    crates.add(new OrderedPair(i, j));
                }
                else
                    mapDataAL.add(new OrderedPair(i, j)); // is a blank space
            }
    }

    public int cost(ArrayList<Action> actionsTaken)
    {
        int count = 0;
        for(int i=0;i<actionsTaken.size();i++)
            if (!actionsTaken.get(i).getIsPush())
                count++;
        return count;
    }

    public ArrayList<Action> legalActions(OrderedPair player, ArrayList<OrderedPair> Boxes)
    {
        
        
        ArrayList<Action> possibleActions = new ArrayList<>();
        possibleActions.add(new Action(-1, 0, null, 0)); // up
        possibleActions.add(new Action(1, 0, null, 0)); // down
        possibleActions.add(new Action(0, -1, null, 0)); // left
        possibleActions.add(new Action(0, 1, null, 0)); // right
        
        ArrayList<Action> legalActions = new ArrayList<>();

        for (int i=0; i<4; i++)
        {
            int x1 = player.getX() + possibleActions.get(i).getColChange(); // possible location of box X
            int y1 = player.gety() + possibleActions.get(i).getRowChange(); // possible location of box Y

            Boolean pushing = false;
            Boolean isWall = false;
            Boolean isDeadlock = false;

            OrderedPair tempCrate = new OrderedPair(x1, y1); 

            // checks if there's a crate after the move
            if(crates.contains(tempCrate)) 
            {
                pushing = true; 
                tempCrate.setX(player.getX() + possibleActions.get(i).getColChange() * 2);
                tempCrate.setX(player.getX() + possibleActions.get(i).getColChange() * 2);
            }
            else if (!walls.contains(tempCrate)) // checks if there's a wall after the move
                legalActions.add(possibleActions.get(i));
            
            // this will only be checked if there is pushing***
            if (pushing)
            {
                if(!walls.contains(tempCrate))
                {
                    if(!crates.contains(tempCrate))
                    {
                        OrderedPair checkNextSpot = new OrderedPair(0, 0);
                        if ()
                        // up
                        // down
                        // left
                        // right
                    
                        if (!isDeadlock)
                        {
                            possibleActions.get(i).setIsPush(true);
                            legalActions.add(possibleActions.get(i));
                        }
                        
                    }
                }  
            }
            
                
        }

        return legalActions;

    } 

    public ArrayList<OrderedPair> updateGameState(OrderedPair player, ArrayList<OrderedPair> Boxes, Action action, OrderedPair newPosPlayer)
    {
        ArrayList<OrderedPair> tempBox = Boxes;

        int xPrev = player.getX();
        int yPrev = player.gety();

        if (action.getIsPush())
        {
            tempBox.remove(newPosPlayer);

            OrderedPair temp = new OrderedPair(xPrev + action.getColChange()* 2, yPrev + action.getRowChange()* 2, 5)
            tempBox.add(temp);
        }   

        return tempBox;       
    }

    public Boolean checkEndState(ArrayList<OrderedPair> posBox)
    {
        int count = 0;

        for(OrderedPair od: goals)
            for(OrderedPair op: posBox)
                if (od.getX() == op.getX() && od.gety() == op.gety())
                    count++;

        if (count == goals.size())
            return true;
        else
            return false;
    }
    
    public int theHueristic(OrderedPair player, ArrayList<OrderedPair> posBox)
    {
        int distance = 0;
        ArrayList<OrderedPair> intersection = new ArrayList<>();
        ArrayList<OrderedPair> boxesNotInGoals = new ArrayList<>();
        ArrayList<OrderedPair> emptyGoals = new ArrayList<>();
        
        
            if (od.getX() == op.getX() && od.gety() == op.gety())
            {
                OrderedPair temp = new OrderedPair(od.getX(), od.gety(), 5);
                intersection.add(temp);
            }

        for(OrderedPair op: posBox)
            for(OrderedPair pair: intersection)
            {
                if (op != pair) // this means that the box is not in the goal
                {
                    boxesNotInGoals.add(pair);
                    break;
                }
            }
        return 0;
    }

    public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {
    
    convertMapData(mapData); // converts the walls and the targets to int, map them to ordered pair
    convertItemsData(itemsData);  // converts the player and the crates to int, map them to ordered pair

    PriorityQueue<State> frontier = new PriorityQueue<>();

    State startState = new State(player,crates,0);

    // States look like [(0,1), (0,2)]
    frontier.add(startState);

    PriorityQueue<Action> actions = new PriorityQueue<>();
    // action looks like (-1,0,false,18)

    actions.add(new Action(0,0,false,0)); // no movement, no hueristic

    ArrayList<State> exploredSet = new ArrayList();
    ArrayList<Action> node_actions = new ArrayList<>();

    while(!frontier.isEmpty())
    {
        State node = frontier.poll();
        Action action = actions.poll();

        node_actions.add(action);

        if (checkEndState(node.getPosBox()))

        if (!exploredSet.contains(node))
        {
            exploredSet.add(node);
            int cost = cost(node_actions);

            ArrayList<Action> legalActions = legalActions(player, crates);

            for(Action i: legalActions)
            {
                int xPrev = player.getX();
                int yPrev = player.gety();

                OrderedPair newPosPlayer = new OrderedPair(xPrev + action.getColChange(), yPrev + action.getRowChange()); 
                ArrayList<OrderedPair> newPosBox = updateGameState(player,crates, i, newPosPlayer);
                
                State newTemp = new State (newPosPlayer,newPosBox, 0);
                frontier.add(node);

            }
            
        }

    }


     
     


    /*  1. check possible moves. ( u,d,l,r )
     * 
     * EACH NODE HAS TO GO THROUGH THIS TO CHECK WHICH IS POSSIBLE TO ADD
     * 
     * 1. remap each 
     *
     */
    





    /*
     * mapData Contains
     * - wall
     * - target
     * 
     * itemsData contains
     * - the player
     * - the crates
     */

  



    try {
      Thread.sleep(3000);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return "lrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlr";
  }

}
