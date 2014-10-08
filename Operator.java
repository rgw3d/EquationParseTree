import java.util.LinkedList;

/**
 * This is the abstract class that implements EquationNode, but doesnt need to define its methods.
 * Parent to all *Operator classes.
 * Created by rgw3d on 8/11/2014.
 */
public abstract class Operator implements EquationNode{

    public LinkedList<EquationNode> Terms;

    /**
     * Default constructor
     */
    public Operator(){
        Terms  = new LinkedList<EquationNode>();
    }

    /**
     * Secondary constructor
     */
    public Operator(LinkedList<EquationNode> terms){
        this.Terms = terms;
    }

    /**
     * Extend by all of the Operator classes. default action for adding a term to the list
     * @param node
     */
    public void addTerm(EquationNode node){
        this.Terms.add(node);
    }

    /**
     * By default, returns -1
     * Assumes canEval(), as this is the "simple" or "easy" action of the operator, adding 2+2
     * or multiplying 3*2x.  Not multiplying by 4*(3x+2), or adding 2x+2.
     * @return double -1
     */
    public double getNum() {
        return -1;
    }

    /**
     * By default, just returns -1
     * Assumes canEval(), as this is the "simple" or "easy" action of the operator, adding 2+2
     * or multiplying 3*2x.  Not multiplying by 4*(3x+2), or adding 2x+2.
     * @return double -1
     */
    public double getVar() {
        return -1;
    }

    /**
     * By default returns false
     * should evaluate if the problem can be easily calculated this is the "simple" or "easy" action of the operator, adding 2+2
     * or multiplying 3*2x.  Not multiplying by 4*(3x+2), or adding 2x+2.
     * @return boolean
     * @throws CanNotEval
     */
    public boolean canEval() throws CanNotEval {//test to see if first everything is a nomial, then if their var values are the same
        return false;
    }


}
