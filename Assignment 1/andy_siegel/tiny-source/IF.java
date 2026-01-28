public class IF extends STAT {
    public String label;
    public EXPR expr;
    public IF(String label, EXPR expr) {this.label = label; this.expr = expr;}
    public String toString() { return "IF " + expr + " GOTO " + label;}
}
