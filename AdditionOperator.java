import java.util.LinkedList;

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
