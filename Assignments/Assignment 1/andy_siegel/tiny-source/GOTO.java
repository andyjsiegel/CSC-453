public class GOTO extends STAT {
    public String label;
    public GOTO(String label) { this.label = label; }
    public String toString() { return "(GOTO" + label + ")"; }
}
