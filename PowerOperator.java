import java.util.LinkedList;

/**
 * Created by rgw3d on 8/15/2014.
 */
public class PowerOperator extends Operator {

    private boolean RaiseVar = false;

    public PowerOperator() {
        super();
        Terms = new LinkedList<EquationNode>();
    }

    public PowerOperator(LinkedList<EquationNode> Terms) {
        super(Terms);
        this.Terms = Terms;
    }

    public PowerOperator(LinkedList<EquationNode> Terms, boolean raiseVar) {
        super(Terms);
        this.Terms = Terms;
        RaiseVar = raiseVar;
    }

    public double getNum() {
        if (!RaiseVar)
            return Math.pow(Terms.getFirst().getNum(), Terms.getLast().getNum());
        else
            return Terms.getFirst().getNum();
    }

    public double getVar() {
        if (RaiseVar)
            return Terms.getFirst().getVar() * Terms.getLast().getVar();
        else
            return Terms.getFirst().getVar();
    }

    public Nominal getNominal() throws CanNotEval {
        if (canEval()) {
            return new Nominal(getNum(), getVar());
        } else
            throw new CanNotEval("Can Not Evaluate Expression");
    }

    public boolean canEval() throws CanNotEval {
        //justs tests to see if the power has a variable or not.  hopefully not
        //okay so the rules are that the exponent must be a nominal without any variables.  must jsut be a number
        //or the exponent must just simplify into a number.  ahhh.
        //this will only test the exponent.

        if (Terms.getLast() instanceof Nominal && Terms.getLast().getVar() == 0) {
            return true;
        } else {
            LinkedList<EquationNode> list = Terms.getLast().getList();//get the list for the exponent
            if (list.size() == 1 && list.getFirst() instanceof Nominal && list.getFirst().getVar() == 0) {
                return true;//if it simplifies to a nominal without a variable
            } else if (list.size() == 1 && list.getFirst() instanceof Fraction) {
                MathOperations.simplifyFractionsFromList(list);//if it simplifies to a fraction that can be simplified (something over 1)
                return (((NumberStructure) list.getFirst()).getTop().size() == 1 && ((NumberStructure) list.getFirst()).getTop().getFirst() instanceof Nominal && ((NumberStructure) list.getFirst()).getTop().getFirst().getVar() == 0 &&
                        ((NumberStructure) list.getFirst()).getBottom().size() == 1 && ((NumberStructure) list.getFirst()).getBottom().getFirst().equals(Nominal.One));
                //this returns true if the fraction is something like (3/1)
            }
            return false;
        }

    }

    public LinkedList<EquationNode> getList() throws CanNotEval {
        if (canEval()) {
            if (Terms.getFirst() instanceof Nominal) {//meaning that the first term is actually something else. like a multiplicaiton operator or something.
                if (Terms.getFirst().getVar() == 0) {//meaning that there is no variable in the base
                    Nominal simplified = new Nominal(Math.pow(Terms.getFirst().getNum(),
                            ((NumberStructure) Terms.getLast().getList().getFirst()).getTop().getFirst().getNum()), 0);
                    LinkedList<EquationNode> result = new LinkedList<EquationNode>();
                    result.add(simplified);
                    return result;
                } else {//with a variable in the base
                    Nominal simplified = new Nominal(Terms.getFirst().getNum(),
                            Terms.getFirst().getVar() * ((NumberStructure) Terms.getLast().getList().getFirst()).getTop().getFirst().getNum());
                    LinkedList<EquationNode> result = new LinkedList<EquationNode>();
                    result.add(simplified);
                    return result;
                }
            } else {//this means that the first bit equals something else.
                //determine if it needs to be flipped (negative exponent)
                //determine how many times it is multiplied against itself.
                LinkedList<EquationNode> orig = Terms.getFirst().getList();
                int expnt = (int)((NumberStructure) Terms.getLast().getList().getFirst()).getTop().getFirst().getNum();
                //get exponent
                if(expnt ==0) {
                    LinkedList<EquationNode> toReturn = new LinkedList<EquationNode>();
                    toReturn.add(Nominal.One);
                    return toReturn;
                }
                else if(expnt <0){//negative exponent

                }
                else
                {
                    if(expnt-1==0){//not multiplied
                        return Terms.getFirst().getList();
                    }

                    MultiplicationOperator[] raisedPowers = new MultiplicationOperator[expnt-1];
                    for(int i = raisedPowers.length-1; i>=0; i--)
                    {
                        if(i == raisedPowers.length-1){
                            raisedPowers[i] = new MultiplicationOperator();
                            raisedPowers[i].addTerm(Terms.getFirst());
                            raisedPowers[i].addTerm(Terms.getFirst());
                        }
                        else{
                            raisedPowers[i] = new MultiplicationOperator();
                            raisedPowers[i].addTerm(raisedPowers[i+1]);
                            raisedPowers[i].addTerm(Terms.getFirst());
                        }
                    }

                    LinkedList<EquationNode> allRaised = raisedPowers[0].getList();
                    return allRaised;
                }


            }
        }
        throw new CanNotEval("Variable in the Power");

    }


}
