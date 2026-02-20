public class LABEL extends STAT {
    public String label;
    public LABEL(String label) { this.label = label; }
    public String toString() { return "(LABEL " + label + ")"; }
    public String getName() { return this.label; }
}
