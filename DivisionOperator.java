import java.util.LinkedList;

/**
 * Created by rgw3d on 8/11/2014.
 */
public class DivisionOperator extends Operator{
    public DivisionOperator(){
        super();
        Terms = new LinkedList<EquationNode>();
    }

    public DivisionOperator(LinkedList<EquationNode> Terms){
        super(Terms);
        this.Terms = Terms;
    }

    public double getNum() {
        double value =0;
        for(EquationNode tmp: Terms){
            if(tmp == Terms.getFirst()){
                value = tmp.getNum();
            }
            else
            {
                value /= tmp.getNum();
            }
        }
        return value;
    }

    public Nominal getNominal() throws CanNotEval {
        if(canEval())
            return new Nominal(getNum(), getVar());
        else
            throw new CanNotEval("Can Not Evaluate Expression");
    }

    public double getVar() {
        double value=  0;
        for(EquationNode tmp: Terms){
            if(tmp == Terms.getFirst()){
                value = tmp.getVar();
            }
            else
                value -= tmp.getVar();
        }
        return value;
    }

    public boolean canEval() throws CanNotEval{
        boolean canEval = true;

        for(EquationNode tmp : Terms){
            canEval &= tmp instanceof  Nominal && tmp.getVar() ==0;
            //tests to see if instance of nominal and have a var of 0
        }
        return canEval;
    }

    public LinkedList<EquationNode> getList() throws CanNotEval{

        if(canEval()){
            return MathOperations.nominalDivision(Terms);
        }
        else{
            return MathOperations.divideControl(Terms);
        }


    }



}
