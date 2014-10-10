import java.util.LinkedList;

/**
 * Created by rgw3d on 8/11/2014.
 */
public class AdditionOperator extends Operator {

    public AdditionOperator() {
        super();
        //Terms = new LinkedList<EquationNode>();
    }

    public AdditionOperator(LinkedList<EquationNode> Terms) {
        super(Terms);
        //this.Terms = Terms;
    }

    /**
     * "first term" + "second term"
     * @return String value of operator
     */
    public String toString() {
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
