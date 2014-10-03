import java.util.LinkedList;

/**
 * Created by rgw3d on 8/16/2014.
 */
public class Fraction extends NumberStructure {
    public LinkedList<EquationNode> Top = new LinkedList<EquationNode>();
    public LinkedList<EquationNode> Bottom = new LinkedList<EquationNode>();

    public Fraction(LinkedList<EquationNode> top, LinkedList<EquationNode> bottom) {

        MathOperations.removeZeros(top);
        if(top.size() == 0){
            top.add(new Nominal(0,0));
        }
        Top = top;
        MathOperations.removeZeros(bottom);
        if(bottom.size() ==0){//if it is zero, then the zero has been removed and it is a divide by zero error
            throw new IllegalArgumentException("Error: Divisor is 0");
        }
        Bottom = bottom;

    }
    public Fraction(LinkedList<EquationNode> top, NumberStructure bottom) {
        Top = top;
        if(bottom.equals(new Nominal())){
            throw new IllegalArgumentException("Error: Divisor is 0");
        }
        Bottom.add(bottom);
    }
    public Fraction(NumberStructure top, LinkedList<EquationNode> bottom) {
        Top.add(top);
        if(bottom.size() ==0){//if it is zero, then the zero has been removed and it is a divide by zero error
            throw new IllegalArgumentException("Error: Divisor is 0");
        }
        Bottom = bottom;
    }
    public Fraction(NumberStructure top, NumberStructure bottom) {
        Top.add(top);
        if(bottom.equals(new Nominal())){
            throw new IllegalArgumentException("Error: Divisor is 0");
        }
        Bottom.add(bottom);
    }

    public double getNum() {
        return -1;
    }

    public double getVar(){
        return -1;
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
        String toReturn ="";
        String top = "";
        String bot = "";

        if(Bottom.size() ==1 && Bottom.getFirst().equals(Nominal.One)) {//if denominator is 1
            for (EquationNode fract : Top) {
                if (top.equals("")) {
                    top += fract.toString();
                } else {
                    top += "+" + fract.toString();
                }
            }
        }
        else {
            top = "((";
            for (EquationNode fract : Top) {
                if(top.equals("((")){
                    top += fract.toString();
                }
                else{
                   top+= "+"+fract.toString();
                }
            }
            top+=")/(";
            for (EquationNode fract : Bottom) {
                if(bot.equals("")){
                    bot += fract.toString();
                }
                else{
                    bot+= "+"+fract.toString();
                }
            }
            bot += "))";

        }
        toReturn+=top;
        toReturn+=bot;
        return toReturn;
    }

    public LinkedList<EquationNode> getTop(){
        return Top;
    }
    public LinkedList<EquationNode> getBottom(){
        return  Bottom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fraction fraction = (Fraction) o;

        if (!Bottom.equals(fraction.Bottom)) return false;
        if (!Top.equals(fraction.Top)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = Top.hashCode();
        result = 31 * result + Bottom.hashCode();
        return result;
    }
}
