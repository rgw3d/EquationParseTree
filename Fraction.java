import java.util.LinkedList;

/**
 * Created by rgw3d on 8/16/2014.
 */
public class Fraction extends NumberStructure {
    public LinkedList<EquationNode> Top = new LinkedList<EquationNode>();
    public LinkedList<EquationNode> Bottom = new LinkedList<EquationNode>();

    public Fraction(LinkedList<EquationNode> top, LinkedList<EquationNode> bottom) {
        Top = top;
        Bottom = bottom;

    }
    public Fraction(LinkedList<EquationNode> top, NumberStructure bottom) {
        Top = top;
        Bottom.add(bottom);
    }
    public Fraction(NumberStructure top, LinkedList<EquationNode> bottom) {
        Top.add(top);
        Bottom = bottom;
    }
    public Fraction(NumberStructure top, NumberStructure bottom) {
        Top.add(top);
        Bottom.add(bottom);
    }

    public double getNum() throws CanNotEval {
        return 0;
    }

    public double getVar() throws CanNotEval {
        return 0;
    }

    public Nominal getNominal() throws CanNotEval {
        return null;
    }

    public boolean canEval() throws CanNotEval {
        return false;
    }

    public LinkedList<EquationNode> getList() throws CanNotEval {
        return null;
    }

    public String toString(){
        String toReturn = "(";
        for(EquationNode fract: Top){
            toReturn+=fract.toString();
        }
        toReturn+="/";
        for(EquationNode fract: Bottom){
            toReturn+=fract.toString();
        }
        toReturn+=")";
        return toReturn;
    }

    public LinkedList<EquationNode> getTop(){
        return Top;
    }
    public LinkedList<EquationNode> getBottom(){
        return  Bottom;
    }
    @Override public int hashCode() {
        System.out.print("wow");
        return 42; }
}
