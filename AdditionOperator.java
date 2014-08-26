import java.util.*;

/**
 * Created by rgw3d on 8/11/2014.
 */
public class AdditionOperator extends Operator {

    public AdditionOperator() {
        super();
        Terms = new LinkedList<EquationNode>();
    }

    public AdditionOperator(LinkedList<EquationNode> Terms) {
        super(Terms);
        this.Terms = Terms;
    }

    public AdditionOperator(String left, String right){
        Terms.add(new Nominal(Double.parseDouble(left),0));
        Terms.add(new Nominal(Double.parseDouble(right),0));
    }

    public double getNum() throws CanNotEval {
       // if(canEval()) {
            double Value = 0;
            for (EquationNode x : Terms) {
                Value += x.getNum();
            }
            return Value;
        //}
        //else
           // throw new CanNotEval("Can Not Evaluate Expression");
    }

    public double getVar() throws CanNotEval {
        //if(canEval()) {
            double Var = Terms.getFirst().getVar();//var should be the same if parsed correctly
            return Var;
        //}
        //else throw new CanNotEval("Can Not Evaluate Expression");
    }

    public boolean canEval() throws CanNotEval {//test to see if first everything is a nomial, then if their var values are the same
        return MathOperations.canAddEasy(Terms);
    }

    public Nominal getNominal() throws CanNotEval {
        if(canEval())
            return MathOperations.combineAll(Terms);
        else
            throw new CanNotEval("Can Not Evaluate Expression");
    }

    public String toString() {
        try {
            if(canEval())
                return (" " + getNum() + " " + getVar() + " " + new Nominal(getNum(), getVar()).toString());//this is why there are excepitons here
        } catch (CanNotEval canNotEval) {
            canNotEval.printStackTrace();
        }
        return Terms.getFirst().toString()+"+"+Terms.getLast().toString();
    }

    public LinkedList<EquationNode> getList() throws CanNotEval {
        if (canEval()) {// if things are easy to add together
            LinkedList<EquationNode> result = new LinkedList<EquationNode>();
            result.add(MathOperations.combineAll(Terms));

            return result;
        }
        else {
            return MathOperations.addControl(Terms);
        }
    }


}
