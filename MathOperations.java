import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

/**
 * Created by rgw3d on 8/16/2014.
 */
public class MathOperations {

    public static LinkedList<EquationNode> multiplyControl(LinkedList<EquationNode> Terms) throws CanNotEval{


        LinkedList<EquationNode> numberStructures = new LinkedList<EquationNode>();
        numberStructures.add(new Nominal(1, 0));//set group to have a default value

        LinkedList<LinkedList<EquationNode>> groups = new LinkedList<LinkedList<EquationNode>>();
        groups.add(new Nominal(1,0).getList());//set groups to have a default value

        for (EquationNode node : Terms) {//tests to see if instance of NumberStructure
            if (node instanceof NumberStructure)
                numberStructures.add(node);
            else
                groups.add(node.getList());//this is what calls everything else down the chain/ up the tree
        }


        NumberStructure simplifiedNominal = multiplyNumberStructures(numberStructures);//multiplyControl the numberStructures together
        LinkedList<EquationNode> simplifiedLinkedList = multiplyLists(groups);//multiplyControl the lists together
        LinkedList<EquationNode> result = multiplyFoil(simplifiedNominal, simplifiedLinkedList);//multiplyControl everything together

        return result;
    }
    public static NumberStructure multiplyNumberStructures(LinkedList<EquationNode> preConverted) throws CanNotEval{

        LinkedList<NumberStructure> numberStructures = new LinkedList<NumberStructure>();
        for(EquationNode x: preConverted){
            numberStructures.add((NumberStructure)x);//this just converts everything to NumberStructure class
        }

        NumberStructure combine = new Nominal(1,0);//set default value
        for(NumberStructure cycle: numberStructures){
            if(combine instanceof Fraction && !(cycle instanceof Fraction)){//cycle is not a fraciton and combine is
                combine = new Fraction(multiplyFoil((NumberStructure)cycle, combine.getTop()), combine.getBottom());
                //multiplyControl the top together, and then add the bottom back on to it.  make combine a fraction.
            }
            if(!(combine instanceof Fraction) && (cycle instanceof Fraction)){//cycle is a fraction and combine is not
                combine = new Fraction(multiplyFoil((NumberStructure)combine, cycle.getTop()), cycle.getBottom());
            }
            if((combine instanceof Fraction) && (cycle instanceof Fraction)){//both are fractions
                LinkedList<LinkedList<EquationNode>> top = new LinkedList<LinkedList<EquationNode>>();
                LinkedList<LinkedList<EquationNode>> bot = new LinkedList<LinkedList<EquationNode>>();

                top.add(combine.getTop());
                top.add(cycle.getTop());

                bot.add(combine.getBottom());
                bot.add(cycle.getBottom());

                combine = new Fraction(multiplyLists(top),multiplyLists(bot));
            }
            if(!(combine instanceof Fraction) && !(cycle instanceof Fraction))
                combine  =  new Nominal(combine.getNum() * cycle.getNum(), combine.getVar() + cycle.getVar());
        }//combines everything together

        return combine;
    }

    public static LinkedList<EquationNode> multiplyLists(LinkedList<LinkedList<EquationNode>> groups) throws CanNotEval{
        LinkedList<EquationNode> result = new LinkedList<EquationNode>();
        result.add(new Nominal(1, 0));//set default value

        for(LinkedList<EquationNode> groupList: groups){
            for(EquationNode insideNominal: groupList) {
                LinkedList<EquationNode> tmpResult = new LinkedList<EquationNode>();
                for (EquationNode tmp : result) {
                    tmpResult.add(new Nominal(insideNominal.getNum()*tmp.getNum(), tmp.getVar()+ insideNominal.getVar()));
                }
                result = tmpResult;//switch the two lists
            }
        }
        return result;
    }

    public static LinkedList<EquationNode> multiplyFoil(NumberStructure numberStructure, LinkedList<EquationNode> list) throws CanNotEval{

        LinkedList<EquationNode> finalList = new LinkedList<EquationNode>();

        for(EquationNode x: list){
            finalList.add(new Nominal((numberStructure.getNum() * x.getNum()),(numberStructure.getVar() + x.getVar())));
        }

        return finalList;
    }

    public static LinkedList<EquationNode> addControl(LinkedList<EquationNode> Terms) throws CanNotEval{
        // if things are not so easy to add together

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

        if (canAddEasy(nominals)) {//if they are all the same nominal type.  its faster
            Nominal tmp = combineAll(nominals); //since nominals is used later we just clear it
            nominals.clear();
            nominals.add(tmp);
        }

        for(LinkedList<EquationNode> groupList: groups){
            nominals.addAll(groupList);//adds everything from the group to nominals
        }
        sortSimplifyNominals(nominals);//simplifies everything within nominals
        return nominals;
    }

    public static Nominal combineAll(LinkedList<EquationNode> toCombine) throws CanNotEval {
        Nominal result = new Nominal(0,0);

        for (EquationNode x : toCombine) {//add everything together
            result = new Nominal(x.getNum() + result.getNum(), x.getVar() );
        }
        return result;
    }

    public static void sortSimplifyNominals(LinkedList<EquationNode> nominalGroup) throws CanNotEval{

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

    public static boolean canAddEasy(LinkedList<EquationNode> Parts) throws CanNotEval {
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

    public static LinkedList<EquationNode> divideControl(EquationNode a, EquationNode b){
        return  new LinkedList<EquationNode>();
    }
    public static LinkedList<EquationNode> nominalDivision(LinkedList<EquationNode> Terms) throws CanNotEval{
        LinkedList<EquationNode> dividedTerms = new LinkedList<EquationNode>();

        Nominal tmp = new Nominal(Math.pow(Terms.getFirst().getNum(),2),0);
        for(EquationNode nom: Terms){
            dividedTerms.add(new Nominal(nom.))
        }
    }



    public static LinkedList<EquationNode> power(EquationNode a, EquationNode b){
        return  new LinkedList<EquationNode>();
    }

}
