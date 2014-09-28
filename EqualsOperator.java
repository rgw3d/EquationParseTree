import java.util.LinkedList;

/**
 * Created by rgw3d on 8/12/2014.
 * Essentially the equals operator is another additionOperator but it does subtraciton
 */
public class EqualsOperator extends Operator {

    public EqualsOperator() {
        super();
        Terms = new LinkedList<EquationNode>();
    }

    public EqualsOperator(LinkedList<EquationNode> Terms) {
        super(Terms);
        this.Terms = Terms;
    }

    public double getNum() {
            double Value = Terms.getFirst().getNum();//subtracts rightside from left side
            Value -= Terms.getLast().getNum();
            return Value;

    }

    public double getVar() {
            double Value = Terms.getFirst().getVar();//subtracts rightside from left side
            Value -= Terms.getLast().getVar();
            return Value;

    }

    public Nominal getNominal() {
            return new Nominal(getNum(), getVar());

    }

    public boolean canEval() throws CanNotEval {
        return evaluate(Terms);
    }

    private boolean evaluate(LinkedList<EquationNode> Parts) throws CanNotEval {
        boolean allNominals = true;
        boolean allCompatable = true;
        for (EquationNode searchThrough : Parts) {//tests to see if instance of nominal
            allNominals &= searchThrough instanceof Nominal;
        }
        if (allNominals) {
            double firstVarValue = Parts.getFirst().getVar();
            for (EquationNode searchThrough : Parts) {//tests to see if instance of nominal
                allCompatable &= searchThrough.getVar() == firstVarValue;//
            }
            //combining all is possible so it returns true. all other paths lead to false;
            if (allCompatable) return true;
        }
        return false;
    }

    /*

    Will return a list of objects that can be solved in some other formula.
    these objects will all be on one side of the equation

    */

    public LinkedList<EquationNode> getList() throws CanNotEval{

        LinkedList<EquationNode> finalList = new LinkedList<EquationNode>();

        finalList.addAll(Terms.getFirst().getList());//get the left side of the equation and store it in the

        LinkedList<EquationNode> tmpRightSide = new LinkedList<EquationNode>();
        for (EquationNode tmp : Terms.getLast().getList()) {
            tmpRightSide.add(new Nominal(tmp.getNum() * -1, tmp.getVar()));  //multiplyControl num by -1
        }

        finalList.addAll(tmpRightSide);

        MathOperations.sortSimplifyNumberStructures(finalList);//now that everything is on one side, add them all together.
        MathOperations.simplifyFractionsFromList(finalList);

        MathOperations.removeZeros(finalList);

        //now we are going to run the whole thing through again to simplify more things.

        /*LinkedList<EquationNode> newTerm = new LinkedList<EquationNode>();
        newTerm.addAll(finalList);
        finalList.clear();
        finalList.addAll(newTerm.getFirst().getList());
        MathOperations.removeZeros(finalList);
        */

        return finalList;
    }

}


