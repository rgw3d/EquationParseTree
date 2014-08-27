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
            NumberStructure tmp = combineAll(nominals); //since nominals is used later we just clear it
            nominals.clear();
            nominals.add(tmp);
        }

        for(LinkedList<EquationNode> groupList: groups){
            nominals.addAll(groupList);//adds everything from the group to nominals
        }
        sortSimplifyNumberStructures(nominals);//simplifies everything within nominals
        return nominals;
    }

    public static NumberStructure combineAll(LinkedList<EquationNode> toCombine) throws CanNotEval {

        if(toCombine.getFirst() instanceof Nominal) {
            NumberStructure result = new Nominal(0, 0);

            for (EquationNode x : toCombine) {//add everything together
                result = new Nominal(x.getNum() + result.getNum(), x.getVar());
            }
            return result;
        }
        else{//instance of fraction
            Fraction bottom = (Fraction)toCombine.getFirst();
            NumberStructure result  = new Fraction(new LinkedList<EquationNode>(),bottom.getBottom());
            //give it a default fraction with the same bottom
            LinkedList<NumberStructure> numberStructures  = new LinkedList<NumberStructure>();
            for(EquationNode x: toCombine){
                    numberStructures.add((NumberStructure)x);
            }
            for (NumberStructure x : numberStructures) {//add everything together
                LinkedList<EquationNode> tmp = new LinkedList<EquationNode>();
                tmp.addAll(x.getTop());
                tmp.addAll(result.getTop());
                result = new Fraction(addControl(tmp), bottom.getBottom());
            }
            return result;
        }
    }

    public static void sortSimplifyNumberStructures(LinkedList<EquationNode> preConverted) throws CanNotEval{
        //first sortSimpify the nominals only.  fractions later if there are any.
        LinkedList<EquationNode> result = new LinkedList<EquationNode>();

        LinkedList<NumberStructure> nominalsOnly  = new LinkedList<NumberStructure>();
        for(EquationNode x: preConverted){
            if(x instanceof  Nominal)
                nominalsOnly.add((NumberStructure)x);
        }
        if(nominalsOnly.size()>0){//if there is any nominals, add them
            sortSimplifyNominals(result, nominalsOnly);
        }

        //now check for fractions
        LinkedList<NumberStructure> fractionsOnly = new LinkedList<NumberStructure>();
        for(EquationNode x: preConverted){
            if(x instanceof Fraction)
                fractionsOnly.add((NumberStructure) x);//this just converts everything to NumberStructure class
        }
        if(fractionsOnly.size()>0) {
            sortSimplifyFractions(result, fractionsOnly);
        }

        preConverted.clear();
        preConverted.addAll(result);

    }

    private static void sortSimplifyFractions(LinkedList<EquationNode> result, LinkedList<NumberStructure> fractionsOnly) throws CanNotEval {
        Hashtable<LinkedList<EquationNode>, LinkedList<EquationNode>> SortedNominals = new Hashtable<LinkedList<EquationNode>, LinkedList<EquationNode>>();
        ArrayList<LinkedList<EquationNode>> varsAdded = new ArrayList<LinkedList<EquationNode>>();

        for (NumberStructure nom : fractionsOnly) {//add everyone to their respectieve gropus
            LinkedList<EquationNode> nomBottom = nom.getBottom();
            /*
            boolean addedValue = false;
            for(LinkedList<EquationNode> addedVars: varsAdded){
                if(addedVars.equals(nom.getBottom())){
                    SortedNominals.get(addedVars).add(nom);//add the fraction value to the hashtable
                    addedValue = true;
                    break;//break out of the for loop because we have added what is necessary to the hashtable
                }
            }
            if(!addedValue){//did not add value to the hash table
                //make a new key
                LinkedList<EquationNode> tmp = new LinkedList<EquationNode>();
                tmp.add(nom);
                SortedNominals.put(nomBottom, tmp);
                varsAdded.add(nomBottom);
            }
            */

            try {
                SortedNominals.get(nomBottom).add(nom);
            } catch (NullPointerException E) {//key was not mapped to a value
                LinkedList<EquationNode> tmp = new LinkedList<EquationNode>();
                tmp.add(nom);
                SortedNominals.put(nomBottom, tmp);
                varsAdded.add(nomBottom);
            }

        }

        LinkedList<EquationNode> finalNomials = new LinkedList<EquationNode>();
        for (LinkedList<EquationNode> x : varsAdded) {
            NumberStructure simplifed = combineAll(SortedNominals.get(x));
            finalNomials.add(simplifed);
        }



        result.addAll(finalNomials);
    }

    private static void sortSimplifyNominals(LinkedList<EquationNode> result, LinkedList<NumberStructure> nominalsOnly) throws CanNotEval {
        Hashtable<Double, LinkedList<EquationNode>> SortedNominals = new Hashtable<Double, LinkedList<EquationNode>>();
        ArrayList<Double> varsAdded = new ArrayList<Double>();

        for (EquationNode nom : nominalsOnly) {//add everyone to their respectieve gropus
            Double nomBottom = nom.getVar();
            try {
                SortedNominals.get(nomBottom).add(nom);
            } catch (NullPointerException E) {//key was not mapped to a value
                LinkedList<EquationNode> tmp = new LinkedList<EquationNode>();
                tmp.add(nom);
                SortedNominals.put(nomBottom, tmp);
                varsAdded.add(nomBottom);
            }
        }

        LinkedList<EquationNode> finalNomials = new LinkedList<EquationNode>();
        for (Double x : varsAdded) {
            NumberStructure simplifed = combineAll(SortedNominals.get(x));
            finalNomials.add(simplifed);
        }
        result.addAll(finalNomials);
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



    public static LinkedList<EquationNode> divideControl(LinkedList<EquationNode> Terms) throws CanNotEval{
        Fraction toDivide = new Fraction(Terms.getFirst().getList(),Terms.getLast().getList());

        LinkedList<EquationNode> toReturn = new LinkedList<EquationNode>();
        toReturn.add(toDivide);

        return toReturn;
    }
    public static boolean canDivide(){
        return true;
    }
    public static LinkedList<EquationNode> nominalDivision(LinkedList<EquationNode> Terms) throws CanNotEval{
        LinkedList<EquationNode> dividedTerm = new LinkedList<EquationNode>();

        Nominal tmp = new Nominal(Math.pow(Terms.getFirst().getNum(),2),0);
        for(EquationNode nom: Terms){
            tmp = new Nominal(tmp.getNum()/nom.getNum(),0);
        }
        dividedTerm.add(tmp);
        return  dividedTerm;
    }



    public static LinkedList<EquationNode> power(EquationNode a, EquationNode b){
        return  new LinkedList<EquationNode>();
    }

}
