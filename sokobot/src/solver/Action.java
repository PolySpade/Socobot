public class Action {

    private int rowChange;
    private int colChange;
    private int hueristic;
    private Boolean isPush;

    Action(int rowChange, int colChange, Boolean isPush, int hueristic) {
        this.rowChange = rowChange;
        this.colChange = colChange;
        this.hueristic = hueristic;
        this.isPush = false;
    }

    public int getRowChange() {
        return rowChange;
    }

    public int getColChange() {
        return colChange;
    }

    public Boolean getIsPush() {
        return isPush;
    }

    public void setIsPush(Boolean isPush) {
        this.isPush = isPush;
    }

}