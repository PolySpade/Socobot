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
        private final char letter;


        public Action(int rowChange, int colChange, char letter) {
            this.rowChange = rowChange;
            this.colChange = colChange;
            this.push = false;
            this.letter = letter;
        }

        public int getRowChange() {
            return rowChange;
        }
        public int getColChange() {
            return colChange;
        }

        public char getLetter() {
            return letter;
        }

        public boolean getPush() {
            return push;
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
    public ArrayList<OrderedPair> sortPairList(ArrayList<OrderedPair> list){
        ArrayList<OrderedPair> tmp = new ArrayList<>();
        for(OrderedPair i: list){
            tmp.add(new OrderedPair(i.getX(),i.getY()));
        }
        tmp.sort(new OrderedPairComparator());
        return tmp;
    }

    public Boolean checkEndState(ArrayList<OrderedPair> crates)
    {

        ArrayList<OrderedPair> sortedCrates = sortPairList(crates);
        ArrayList<OrderedPair> sortedGoals = sortPairList(goals);

        StringBuilder crates_string = new StringBuilder();
        StringBuilder goals_string = new StringBuilder();
        for(OrderedPair p: sortedCrates){
            crates_string.append(p.getX());
            crates_string.append(p.getY());
        }

        for(OrderedPair p: sortedGoals){
            goals_string.append(p.getX());
            goals_string.append(p.getY());
        }

        return (goals_string.toString()).equals(crates_string.toString());
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

    public String StatetoString(State state){
        StringBuilder s = new StringBuilder();
        s.append(state.getPlayer().getX());
        s.append(state.getPlayer().getY());
        ArrayList<OrderedPair> tmp = sortPairList(state.getCrates());
        for(OrderedPair p: tmp){
            s.append(p.getX());
            s.append(p.getY());
        }
        return s.toString();
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

    public boolean isDeadlock(ArrayList<OrderedPair> crates){
        int[][] allPattern = {
                {0,1,2,3,4,5,6,7,8}, //0 degrees
                {2,5,8,1,4,7,0,3,6}, //90 degrees
                {8,7,6,5,4,3,2,1,0}, //180 degrees
                {6,3,0,7,4,1,8,5,2}, //270 degrees
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
                            _X#
                            ___
                             */
                            (contains(newboard[1],walls) && contains(newboard[5],walls)) ||
                            /*
                            _$#
                            _X#
                            ___
                             */
                                    (contains(newboard[1],crates) && contains(newboard[2],walls) && contains(newboard[5],walls)) ||
                            /*
                            _#$
                            _X$
                            ___
                             */
                                    (contains(newboard[1],walls) && contains(newboard[2],crates) && contains(newboard[5],crates)) ||
                            /*
                            _$#
                            _X$
                            ___
                             */
                                    (contains(newboard[1],crates) && contains(newboard[2],walls) && contains(newboard[5],crates)) ||
                            /*
                            _$$
                            _X$
                            ___
                             */
                                    (contains(newboard[1],crates) && contains(newboard[2],crates) && contains(newboard[5],crates)) ||
                            /*
                            _$#
                            #X_
                            _$#
                            */
                                    (contains(newboard[1],crates) && contains(newboard[7],crates) && contains(newboard[2],walls) && contains(newboard[3],walls)&& contains(newboard[8],walls)) ||
                            /*
                            _#_
                            $X_
                            #__
                            */
                                    (contains(newboard[1],walls) && contains(newboard[6],walls) && contains(newboard[3],crates)))
                    {
                        return true;
                    }
                }

            }
        }

        return false;
    }
    public int heuristic(State s) { //manhattan distance
        List<OrderedPair> crates = s.getCrates();
        int distance = 0;

        // Use a set for faster lookups
        HashSet<String> matchedGoals = new HashSet<>();

        // Calculate the Manhattan distance between unmatched crates and goals
        for (OrderedPair crate : crates) {
            //if crate is in goal already, skip
            if (goals.contains(crate)) {
                String crateString = crate.getX() +""+ crate.getY();
                matchedGoals.add(crateString);
                continue;
            }
            //check other crates
            int minDistance = 1000;
            OrderedPair closestGoal = null;
            for (OrderedPair goal : goals) {
                String goalString = goal.getX() +""+ goal.getY();
                if (!matchedGoals.contains(goalString)) {
                    int currentDistance = Math.abs(crate.getX() - goal.getX()) + Math.abs(crate.getY() - goal.getY());
                    if (currentDistance < minDistance) {
                        minDistance = currentDistance;
                        closestGoal = goal;
                    }
                }
            }
            if (closestGoal != null) {
                String closestString = closestGoal.getX() +""+ closestGoal.getY();
                matchedGoals.add(closestString);
            }
            distance += minDistance;
        }

        return distance;
    }
    //GBFS
    public String SokobanSolver(){
        State startState = new State(player,crates);

        // Use a priority queue for the frontier
        PriorityQueue<SearchNode> frontier = new PriorityQueue<>(Comparator.comparingInt(SearchNode::getHeuristic));
        frontier.add(new SearchNode(startState, Arrays.asList(new Action(0,0,' ')), 0));

        HashSet<String> exploredSet = new HashSet<>();

        while(!frontier.isEmpty()){
            SearchNode currentNode = frontier.poll();
            State currentState = currentNode.state;
            List<Action> nodeAction = currentNode.actions;

            String currentStateString = StatetoString(currentState);

            if(checkEndState(currentState.getCrates())){
                StringBuilder s = new StringBuilder();
                for(Action a: nodeAction){
                    s.append(a.getLetter());
                }
                return s.toString();
            }

            if(!exploredSet.contains(currentStateString)){
                exploredSet.add(currentStateString);

                for(Action action: legalAction(currentState.getPlayer(),currentState.getCrates())){
                    State newState = updateState(action,currentState.getPlayer(),currentState.getCrates());

                    if(isDeadlock(newState.getCrates())){
                        continue;
                    }
                    List<Action> newAction = new ArrayList<>(nodeAction);
                    newAction.add(action);

                    int newHeuristic;
                    //only change heuristic if it is true
                    if(action.getPush()){
                        newHeuristic = heuristic(newState);
                    }else{
                        newHeuristic = currentNode.getHeuristic();
                    }

                    frontier.add(new SearchNode(newState, newAction, newHeuristic));
                }
            }
        }
        return "";
    }



    private class SearchNode {
        State state;
        List<Action> actions;
        int heuristic;

        SearchNode(State state, List<Action> actions, int heuristic) {
            this.state = state;
            this.actions = actions;
            this.heuristic = heuristic;
        }

        int getHeuristic() {
            return heuristic;
        }
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
