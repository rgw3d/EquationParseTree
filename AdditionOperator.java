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
        if(canEval()) {
            double Value = 0;
            for (EquationNode x : Terms) {
                Value += x.getNum();
            }
            return Value;
        }
        else
            throw new CanNotEval("Can Not Evaluate Expression");
    }

    public double getVar() throws CanNotEval {
        if(canEval()) {
            double Var = Terms.getFirst().getVar();//var should be the same if parsed correctly
            return Var;
        }
        else throw new CanNotEval("Can Not Evaluate Expression");
    }

    public boolean canEval() throws CanNotEval {//test to see if first everything is a nomial, then if their var values are the same
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


    public Nominal getNominal() throws CanNotEval {
        if(canEval())
            return combineAll(Terms);
        else
            throw new CanNotEval("Can Not Evaluate Expression");
    }

    public Nominal combineAll(LinkedList<EquationNode> toCombine) throws CanNotEval {
        Nominal result = new Nominal();

        for (EquationNode x : toCombine) {//add everything together
            result = new Nominal(x.getNum() + result.getNum(), x.getVar() + result.getVar());
        }
        return result;
    }

    public String toString() {
        try {
            if(canEval())
                return (" " + getNum() + " " + getVar() + " " + new Nominal(getNum(), getVar()).toString());
        } catch (CanNotEval canNotEval) {
            canNotEval.printStackTrace();
        }
        return "didnt work lol";
    }


    public LinkedList<EquationNode> getList() throws CanNotEval {
        if (canEval()) {// if things are easy to add together
            LinkedList<EquationNode> result = new LinkedList<EquationNode>();
            result.add(combineAll(Terms));
            return result;
        } else // if things are not so easy to add together
        {
            LinkedList<EquationNode> result = new LinkedList<EquationNode>();

            LinkedList<EquationNode> nominals = new LinkedList<EquationNode>();
            nominals.add(new Nominal());//set group to have a default value
            LinkedList<LinkedList<EquationNode>> groups = new LinkedList<LinkedList<EquationNode>>();
            groups.add(new Nominal().getList());//set groups to have a default value

            for (EquationNode searchThrough : Terms) {//tests to see if instance of nominal
                if (searchThrough instanceof Nominal)
                    nominals.add(searchThrough);//puts it with all the other nominals
                else
                    groups.add(searchThrough.getList());//puts all the lists asside
            }

            if (evaluate(nominals)) {//if they are all the same nominal type.  its faster
                Nominal tmp = combineAll(nominals); //since nominals is used later we just clear it
                nominals.clear();
                nominals.add(tmp);
            }

            for(LinkedList<EquationNode> groupList: groups){
                nominals.addAll(groupList);//adds everything from the group to nominals
            }
            sortSimplifyNominals(nominals);//simplifies everything within nominals
        }

        return Terms;
    }

    public void sortSimplifyNominals(LinkedList<EquationNode> nominalGroup) throws CanNotEval{

    Hashtable<Double, LinkedList<EquationNode>> SortedNominals = new Hashtable<Double, LinkedList<EquationNode>>();
    ArrayList<Double> varsAdded = new ArrayList<Double>();

    for (EquationNode nom : nominalGroup) {//add everyone to their respectieve gropus
        double var = nom.getVar();
        try {
            SortedNominals.get(var).add(nom);
        } catch (NullPointerException E) {//key was not mapped to a value
            LinkedList<EquationNode> tmp = new LinkedList<EquationNode>();
            tmp.add(nom);
            SortedNominals.put(var, tmp);
            varsAdded.add(var);
        }
    }

    LinkedList<EquationNode> finalNomials = new LinkedList<EquationNode>();
    for (Double x : varsAdded) {
        Nominal simplifed = combineAll(SortedNominals.get(x));
        finalNomials.add(simplifed);
    }

    nominalGroup.clear();
    nominalGroup.addAll(finalNomials);

    }
}
