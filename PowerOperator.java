import java.util.LinkedList;

/**
 * Created by rgw3d on 8/15/2014.
 */
public class PowerOperator extends Operator {

    /**
     * Default Constructor
     */
    public PowerOperator() {
        super();
        Terms = new LinkedList<EquationNode>();
    }

    /**
     * This canEval() method is actually important.  determins if there is a variable in the exponent
     * @return boolean
     * @throws CanNotEval
     */
    @Override
    public boolean canEval() throws CanNotEval {
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

    /**
     * exponent must be an int.
     * @return LinkedList of EquationNode type.
     * @throws CanNotEval  If there is a variable in the exponent, throws and error.  if canEval() is false throws this
     */
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
                int expnt = (int)((NumberStructure) Terms.getLast().getList().getFirst()).getTop().getFirst().getNum();
                //get exponent.  will be cast to an int.
                if(expnt ==0) {
                    LinkedList<EquationNode> toReturn = new LinkedList<EquationNode>();
                    toReturn.add(Nominal.One);
                    return toReturn;
                }
                else if(expnt <0){//negative exponent
                    Fraction flipped = new Fraction(Nominal.One,Terms.getFirst().getList());
                    expnt = Math.abs(expnt);

                    if(expnt-1==0){//not multiplied
                        LinkedList<EquationNode> toReturn = new LinkedList<EquationNode>();
                        toReturn.add(flipped);
                        return toReturn;
                    }

                    MultiplicationOperator[] raisedPowers = new MultiplicationOperator[expnt-1];
                    for(int i = raisedPowers.length-1; i>=0; i--)
                    {
                        if(i == raisedPowers.length-1){
                            raisedPowers[i] = new MultiplicationOperator();
                            raisedPowers[i].addTerm(flipped);
                            raisedPowers[i].addTerm(flipped);
                        }
                        else{
                            raisedPowers[i] = new MultiplicationOperator();
                            raisedPowers[i].addTerm(raisedPowers[i+1]);
                            raisedPowers[i].addTerm(flipped);
                        }
                    }

                    LinkedList<EquationNode> allRaised = raisedPowers[0].getList();
                    return allRaised;

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
