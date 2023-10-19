package solver;
import java.util.*;

public class SokoBot {

    //Accomplished OrderedPair (OrderedPair)
    ArrayList<OrderedPair> walls = new ArrayList();
    ArrayList<OrderedPair> goals = new ArrayList();
    ArrayList<OrderedPair> crates = new ArrayList();
    OrderedPair player;
    private class OrderedPair{
        private int x;
        private int y;

        public OrderedPair(int x, int y){
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

    }

    private class Action{

        private int rowChange;
        private int colChange;
        private boolean push;
        private int heuristic;
        private final char letter;


        public Action(int rowChange, int colChange, char letter) {
            this.rowChange = rowChange;
            this.colChange = colChange;
            this.heuristic = 0;
            this.push = false;
            this.letter = letter;
        }

        public int getRowChange() {
            return rowChange;
        }
        public int getColChange() {
            return colChange;
        }
        public int getHeuristic(){
            return heuristic;
        }

        public char getLetter() {
            return letter;
        }

        public boolean getPush() {
            return push;
        }
        public void setHeuristic(int heuristic){
            this.heuristic = heuristic;
        }
        public void setPush(boolean push) {
            this.push = push;
        }
    }


    private class State{
        OrderedPair player;
        ArrayList<OrderedPair> crates;

        public State(OrderedPair player, ArrayList<OrderedPair> crates){
            this.player = player;
            this.crates = crates;
        }

        public OrderedPair getPlayer() {
            return player;
        }

        public ArrayList<OrderedPair> getCrates() {
            return crates;
        }
    }
    public void convertMapData(char[][] mapData)
    {
        for(int i=0;i< mapData.length;i++)
            for (int j=0;j<mapData[i].length;j++)
            {
                if (mapData[i][j] == '#')
                {
                    walls.add(new OrderedPair(i, j));
                }
                else if (mapData[i][j] == '.')
                {
                    goals.add(new OrderedPair(i, j));
                }
            }
    }
    public void convertItemsData(char[][] itemsData)
    {
        for(int i=0;i< itemsData.length;i++)
            for (int j=0;j<itemsData[i].length;j++)
            {
                if (itemsData[i][j] == '@')
                {
                    player = new OrderedPair(i, j);
                }
                else if (itemsData[i][j] == '$')
                {
                    crates.add(new OrderedPair(i, j));
                }
            }
    }
    //Comparator
    public class OrderedPairComparator implements Comparator<OrderedPair> {
        @Override
        public int compare(OrderedPair p1, OrderedPair p2) {
            if (p1.x != p2.x) {
                return Integer.compare(p1.x, p2.x); // Sort by x
            }
            return Integer.compare(p1.y, p2.y); // If x is the same, sort by y
        }
    }

    public boolean contains(OrderedPair p, ArrayList<OrderedPair> arr){
        boolean bool = false;
        for(OrderedPair x: arr){
            if((p.getX() == x.getX()) && (p.getY() == x.getY())){
                bool = true;
            }
        }
        return bool;
    }

    //Overload for Checking Set
    public boolean containset(State s, Set<State> set){
        boolean bool = false;
        //System.out.println(s.getCrates().size());
        for(State x: set){

            //Compare Player
            if((s.getPlayer().getX() == x.getPlayer().getX()) && (s.getPlayer().getY() == x.getPlayer().getY())){
                bool = true;
            }else{
                bool = false;
            }

            int count = 0;
            if(bool) {
                //Compare Boxes
                for (OrderedPair j : s.getCrates()) { //check crates one by one

                    for (OrderedPair y : x.getCrates()) {
                        //System.out.println(s.getCrates().size());
                        //System.out.println(j.getX() + ":" + j.getY() + "--" + y.getX() + ":" + y.getY());
                        if ((j.getX() == y.getX() && j.getY() == y.getY())) {
                            count++;
                        }
                    }
                    //System.out.println();
                }
                if (count == x.getCrates().size()) { //if the number of true is equals
                    bool = true;
                    //System.out.println(true);
                    break;
                } else {
                    bool = false;
                }
            }
        }
        return bool;
    }

    public Boolean checkEndState(ArrayList<OrderedPair> crates)
    {
        ArrayList<OrderedPair> sortedCrates = new ArrayList<>();
        ArrayList<OrderedPair> sortedGoals = new ArrayList<>();

        for(OrderedPair i: crates){
            sortedCrates.add(new OrderedPair(i.getX(),i.getY()));
        }
        for(OrderedPair i: goals){
            sortedGoals.add(new OrderedPair(i.getX(),i.getY()));
        }
        sortedCrates.sort(new OrderedPairComparator());
        sortedGoals.sort(new OrderedPairComparator());
        /*
        for(int i = 0 ; i< sortedCrates.size()-1;i++){
            System.out.println( sortedCrates.get(i).getX()+":"+sortedCrates.get(i).getY() + " :: " + sortedGoals.get(i).getX()+":"+sortedGoals.get(i).getY());
        }
        System.out.println(" ");
        */
        return sortedCrates.equals(sortedGoals);
    }
    public Boolean isActionLegal(Action action, OrderedPair player,ArrayList<OrderedPair> crates){
        int player_x = player.getX();
        int player_y = player.getY();

        if(action.getPush()){
            //if it touches a crate
            //+2 since it advances to double check if it hits another crate or not
            player_x += 2* action.getRowChange();
            player_y += 2* action.getColChange();
        }else{
            player_x += action.getRowChange();
            player_y += action.getColChange();
        }
        //check if move hits crate or wall
        return !((contains(new OrderedPair(player_x,player_y),crates) || (contains(new OrderedPair(player_x,player_y),walls))));
    }
    public ArrayList<Action> legalAction(OrderedPair player, ArrayList<OrderedPair> crates){
        Action[] allActions = {
                new Action(1,0,'d'),
                new Action(-1,0,'u'),
                new Action(0,-1,'l'),
                new Action(0,1,'r')
        };
        int player_x = player.getX();
        int player_y = player.getY();

        ArrayList<Action> possibleActions = new ArrayList<>();
        for (Action action: allActions){
            int action_x = player_x + action.getRowChange();
            int action_y = player_y + action.getColChange();

            if(contains(new OrderedPair(action_x,action_y),crates)){
                action.setPush(true);
            }
            //else default is false
            //check if action is legal or not
            if(isActionLegal(action,new OrderedPair(player_x,player_y),crates)){
                possibleActions.add(action);
            }
        }
        return possibleActions;
    }

    public State updateState(Action action, OrderedPair player, ArrayList<OrderedPair> crates){

        //New Player Position
        OrderedPair newPosPlayer = new OrderedPair(action.getRowChange()+player.getX(), action.getColChange()+player.getY());
        //System.out.println(newPosPlayer.getX()+":"+newPosPlayer.getY());
        ArrayList<OrderedPair> tempCrates = new ArrayList<>();

        for(OrderedPair i: crates){
            tempCrates.add(new OrderedPair(i.getX(),i.getY()));
        }

        if(action.getPush()){ //if it is a push action
            //remove ordered pair tuple
            int count = 0;
            for(OrderedPair i:tempCrates){
                if(i.getX() == newPosPlayer.getX() && i.getY() == newPosPlayer.getY()){
                    break;
                }else{
                    count++;
                }
            }
            tempCrates.remove(count);
            tempCrates.add(new OrderedPair(player.getX()+2* action.getRowChange(),player.getY()+2* action.getColChange()));
        }
        //return new Player and Crates Position
        State newState = new State(newPosPlayer,tempCrates);

        return newState;
    }

    public boolean isFailed(ArrayList<OrderedPair> crates){
        int[][] allPattern = {
                {0,1,2,3,4,5,6,7,8}, //0 degrees
                {2,5,8,1,4,7,0,3,6}, //90 degrees
                {8,7,6,5,4,3,2,1,0}, //180 degrees
                {6,3,0,7,4,1,8,5,2}, //270 degrees
                //Flip Pattern
                {2,1,0,5,4,3,8,7,6}, //Horizontal flip
                {0,3,6,1,4,7,2,5,8}, //Vertical flip
                {6,7,8,3,4,5,0,1,2}, //Horizontal flip followed by 180-degree rotation
                {8,5,2,7,4,1,6,3,0} //Vertical flip followed by 180-degree rotation
        };

        for(OrderedPair crate: crates){
            if(!contains(crate,goals)) {
                OrderedPair[] board = {
                        new OrderedPair(crate.getX() - 1, crate.getY() - 1), new OrderedPair(crate.getX() - 1, crate.getY()), new OrderedPair(crate.getX() - 1, crate.getY() + 1),
                        new OrderedPair(crate.getX(), crate.getY() - 1), new OrderedPair(crate.getX(), crate.getY()), new OrderedPair(crate.getX(), crate.getY() + 1),
                        new OrderedPair(crate.getX() + 1, crate.getY() - 1), new OrderedPair(crate.getX() + 1, crate.getY()), new OrderedPair(crate.getX() + 1, crate.getY() + 1)};


                for (int[] pattern : allPattern) {
                    OrderedPair[] newboard = new OrderedPair[9];
                    int count = 0;
                    for (int i : pattern) {
                        newboard[count] = board[i];
                        count++;
                    }



                    if(
                            //Pruning Patterns
                            /*
                            _#_
                            __#
                            ___
                             */
                            (contains(newboard[1],walls) && contains(newboard[5],walls)) ||
                            /*
                            _$#
                            __#
                            ___
                             */
                            (contains(newboard[1],crates) && contains(newboard[2],walls) && contains(newboard[5],walls)) ||
                            /*
                            _$#
                            __$
                            ___
                             */
                            (contains(newboard[1],crates) && contains(newboard[2],walls) && contains(newboard[5],crates))||
                            /*
                            _$$
                            __$
                            ___
                             */
                            //elif newBoard[1] in posBox and newBoard[2] in posBox and newBoard[5] in posBox: return True
                            (contains(newboard[1],crates) && contains(newboard[2],crates) && contains(newboard[3],crates))||
                            /*
                            _$#
                            #__
                            _$#
*/
                            //elif newBoard[1] in posBox and newBoard[6] in posBox and newBoard[2] in posWalls and newBoard[3] in posWalls and newBoard[8] in posWalls: return True
                            (contains(newboard[1],crates) && contains(newboard[7],crates) && contains(newboard[2],walls) && contains(newboard[3],walls)&& contains(newboard[8],walls)))
                    {
                        return true;
                    }
                }

            }
        }

        return false;
    }

    //DFS
    public String SokobanSolver(){
        /*
        //Convert it to local
        OrderedPair player= new OrderedPair(this.player.getX(),this.player.getY());

        ArrayList<OrderedPair> crates = new ArrayList<>();

        for(OrderedPair i: this.crates){
            crates.add(new OrderedPair(i.getX(),i.getY()));
        }
        */

        State startState = new State(player,crates);
        Queue<List<State>> frontier = new LinkedList<>();
        frontier.add(Arrays.asList(startState));

        Queue<List<Action>> actions = new LinkedList<>();
        actions.add(Arrays.asList(new Action(0,0,' '))); //start with no moves

        Set<State> exploredSet = new HashSet<>();
        while(!frontier.isEmpty()){

            List<State> node = frontier.poll(); //head , left pop, assigns and deletes
            List<Action> nodeAction = actions.poll(); //head , left pop,

            State currentState = node.get(node.size()-1);
            //System.out.println(currentState.getPlayer().getX()+":"+currentState.getPlayer().getY());
            if(checkEndState(currentState.getCrates())){ //check if crates align with goals
                //convert nodes to string
                StringBuilder s = new StringBuilder();
                if(nodeAction != null){
                    for(Action a: nodeAction){
                        s.append(a.getLetter());
                    }
                    return s.toString(); //return solution
                }
                return ""; //return none, problem solved
            }
            //check if the current node is explored or not
            //!containset(currentState,exploredSet)
            if(!containset(currentState,exploredSet)){
                exploredSet.add(currentState);
                for(Action action: legalAction(currentState.getPlayer(),currentState.getCrates())){
                    State newState = updateState(action,currentState.getPlayer(),currentState.getCrates());
                    //find deadlocks
                    if(isFailed(newState.getCrates())){ //if crates meet a deadlock
                        continue;
                    }

                    List<State> newNode = new ArrayList<>(node); //parent to state node
                    newNode.add(newState);
                    List<Action> newAction = new ArrayList<>(nodeAction);//parent to actions node
                    newAction.add(action);

                    //System.out.println(currentState.getPlayer().getX() + ": "+currentState.getPlayer().getY());
                    frontier.add(newNode);
                    actions.add(newAction);

                    //System.out.println(newState.getPlayer().getX()+":"+newState.getPlayer().getY());

                }
                System.out.println();
            }

        }
        return "";
    }
    public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {

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


    //Convert Map
    convertItemsData(itemsData);
    convertMapData(mapData);
    return SokobanSolver();
  }





}
