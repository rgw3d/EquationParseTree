
import java.util.LinkedList;

/**
 * Created by rgw3d on 8/11/2014.
 */
public class MultiplicationOperator extends Operator {

    public MultiplicationOperator(){
        super();
        Terms = new LinkedList<EquationNode>();
    }

    public MultiplicationOperator(LinkedList<EquationNode> Terms){
        super(Terms);
        this.Terms = Terms;
    }

    public double getNum() {
            float value = 1;
            for (EquationNode tmp : Terms) {
                value *= tmp.getNum();
            }
            return value;
    }

    public double getVar(){
            float value = 1;
            for (EquationNode tmp : Terms) {
                value *= tmp.getVar();
            }
            return value;

    }

    public Nominal getNominal() throws CanNotEval {
        if(canEval()) {
            return new Nominal(getNum(), getVar());
        }
        else
            throw new CanNotEval("Can Not Evaluate Expression");
    }

    public boolean canEval() throws CanNotEval{
        boolean canEval = true;

        for(EquationNode tmp : Terms){
            canEval &= tmp instanceof  Nominal;//tests to see if instance of nominal.
        }

        return canEval;
    }

    public LinkedList<EquationNode> getList() throws CanNotEval{
        LinkedList<EquationNode> multiply = MathOperations.multiplyControl(Terms);
        return multiply;

    }
}
