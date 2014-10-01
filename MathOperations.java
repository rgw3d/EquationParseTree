import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;

import static java.util.Arrays.*;

/**
 * Created by rgw3d on 8/16/2014.
 */
public class MathOperations {

    public static LinkedList<EquationNode> multiplyControl(LinkedList<EquationNode> Terms) throws CanNotEval{


        LinkedList<EquationNode> nominals = new LinkedList<EquationNode>();
        nominals.add(new Nominal(1, 0));//set group to have a default value

        LinkedList<LinkedList<EquationNode>> groups = new LinkedList<LinkedList<EquationNode>>();
        groups.add(new Nominal(1,0).getList());//set groups to have a default value

        for (EquationNode node : Terms) {//tests to see if instance of NumberStructure
          // if (node instanceof Nominal)
            //    nominals.add(node);
           //else
                groups.add(node.getList());//this is what calls jeverything else down the chain/ up the tree
        }

        /*
        multiply nominals.  they are easy to do and pick out.
        however, with the addition of fractions, now lists of fractions must be dealt with.
        so, the new strategy is to multiply nominals first, then multiply lists together seperatly.
        then combine them at the end.
        needs to be able to do recursively (fractions in fractions in fractions)
        */

       // Nominal multipliedNominal = multiplyNominals(nominals);//multiply the list of nominals together.



        //NumberStructure simplifiedNominal = multiplyNumberStructures(nominals);//multiplyControl the numberStructures together
        LinkedList<EquationNode> simplifiedList = multiplyLists(groups);//multiplyControl the lists together
        //LinkedList<EquationNode> result = multiplyFoil(simplifiedNominal, simplifiedLinkedList);//multiplyControl everything together

        //return result;
        return simplifiedList;
    }
    public static Nominal multiplyNominals(LinkedList<EquationNode> nodes) throws CanNotEval{

        Nominal result = Nominal.One;

        for(EquationNode node: nodes){
            result = new Nominal(result.getNum() *node.getNum(), result.getVar() + node.getVar());
        }

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
                combine = new Fraction(multiplyFoil(cycle, combine.getTop()), combine.getBottom());
                //multiplyControl the top together, and then add the bottom back on to it.  make combine a fraction.
                continue;
            }
            if(!(combine instanceof Fraction) && (cycle instanceof Fraction)){//cycle is a fraction and combine is not
                combine = new Fraction(multiplyFoil(combine, cycle.getTop()), cycle.getBottom());
                continue;
            }
            if((combine instanceof Fraction) && (cycle instanceof Fraction)){//both are fractions
                LinkedList<LinkedList<EquationNode>> top = new LinkedList<LinkedList<EquationNode>>();
                LinkedList<LinkedList<EquationNode>> bot = new LinkedList<LinkedList<EquationNode>>();

                top.add(combine.getTop());
                top.add(cycle.getTop());

                bot.add(combine.getBottom());
                bot.add(cycle.getBottom());

                combine = new Fraction(multiplyLists(top),multiplyLists(bot));
                continue;
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
            LinkedList<EquationNode> tmpResult = new LinkedList<EquationNode>();
            for(EquationNode insideNominal: groupList) {//uses the top for loops list

                for (EquationNode tmp : result) {//multiplies against result list
                    LinkedList<EquationNode> multiplyList = new LinkedList<EquationNode>();
                    multiplyList.add(insideNominal);
                    multiplyList.add(tmp);
                    tmpResult.add(multiplyNumberStructures(multiplyList));
                }

            }
            result = tmpResult;//switch the two lists
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

        if(Terms.getLast().getNum()==0){
            throw new IllegalArgumentException("Error: Divisor is 0");
        }


        Nominal tmp = new Nominal(Math.pow(Terms.getFirst().getNum(),2),0);//raise the first one to the second power.  we will divide it by itself
        for(EquationNode nom: Terms){

            tmp = new Nominal(tmp.getNum()/nom.getNum(),0);
        }
        dividedTerm.add(tmp);
        return  dividedTerm;
    }



    public static LinkedList<EquationNode> power(EquationNode a, EquationNode b){
        return  new LinkedList<EquationNode>();
    }

    public static void removeZeros(LinkedList<EquationNode> list) {
        LinkedList<NumberStructure> numberStructures = new LinkedList<NumberStructure>();
        for(EquationNode x: list){
            numberStructures.add((NumberStructure)x);//this just converts everything to NumberStructure class
        }

        LinkedList<EquationNode> simplifiedStructures = new LinkedList<EquationNode>();//the final list that if the node passes it is added to this.
        for(NumberStructure node: numberStructures){
            if(!(node.equals(new Nominal(0,0))||node.getNum() == 0) && node instanceof Nominal ){//if not zero, and a nominal
                //numberStructures.remove(node);//so this actually causes errors. so i wont use it
                simplifiedStructures.add(node);
            }

            if(node instanceof Fraction){
                LinkedList<EquationNode> top = node.getTop();
                removeZeros(top);
                LinkedList<EquationNode> bot = node.getBottom();
                removeZeros(bot);
                if(bot.size() ==0){//if it is zero, then the zero has been removed and it is a divide by zero error
                    throw new IllegalArgumentException("Error: Divisor is 0");
                }
                simplifiedStructures.add(new Fraction(top,bot));
            }

        }

        list.clear();
        list.addAll(simplifiedStructures);
    }

    public static void simplifyFractionsFromList(LinkedList<EquationNode> nodes) {

        LinkedList<EquationNode> returnList = new LinkedList<EquationNode>();

        for(EquationNode x: nodes){
            if(x instanceof Fraction){

                returnList.add(simplifyFractions((Fraction) x));
            }
            else {
                returnList.add(x);
            }
        }

        nodes.clear();
        nodes.addAll(returnList);

    }
    public static EquationNode simplifyFractions(Fraction fraction) {

        for(EquationNode top: fraction.getTop()){//simplify underliying fractions
            if(top instanceof Fraction){//could be a fraction in a fraction
                int tmpIndx = fraction.getTop().indexOf(top);//get indx of fraction
                fraction.getTop().remove(top);//remove the object from the list
                fraction.getTop().add(tmpIndx,simplifyFractions((Fraction)top));//add the "simplifed" object back in
            }

        }
        for(EquationNode bot: fraction.getBottom()){//simplify underlying fractions
            if(bot instanceof Fraction){//could be a fraction in a fraction
                int tmpIndx = fraction.getTop().indexOf(bot);//get indx of fraction
                fraction.getBottom().remove(bot);//remove the object from the list
                fraction.getBottom().add(tmpIndx,simplifyFractions((Fraction)bot));//add the "simplifed" object back in
            }

        }

        boolean canDivide = true;

        ArrayList<Integer> topDivisors = new ArrayList<Integer>();
        outerTopLoop:
        for(EquationNode outside: fraction.getTop()){//these should all be nominals.  if not, clear list and break
            int possibleOutsideGCD = 0;
            for(int i = 0; i<fraction.getTop().size(); i++){
            //for(EquationNode inside: fraction.getTop()){//
                if(fraction.getTop().get(i) instanceof Nominal){
                    double outsideNum = outside.getNum();
                    double insideNum = fraction.getTop().get(i).getNum();

                    if(outsideNum == Math.round(outsideNum) && insideNum == Math.round(insideNum)){//if both are ints
                        if(possibleOutsideGCD == 0){
                            possibleOutsideGCD=GCD((int)outsideNum,(int)insideNum);
                        }
                        else{
                            if(!((int)insideNum%possibleOutsideGCD==0)){//so the factor doesnt work
                                possibleOutsideGCD = GCD((int)outsideNum,(int)insideNum);
                                i=0;//reseting the loop so that this new gcd value is tested.  if the value is 1, then it will work (no infinite loop)
                                continue;
                                //reset the loop and then continue; to loop through again

                            }//if false, then its good -- do nothing because this factor works.
                        }
                    }
                    else
                    {
                        canDivide =false;
                        topDivisors.clear();
                        break outerTopLoop;
                    }

                }
                else{
                    canDivide = false;
                    topDivisors.clear();
                    break outerTopLoop;
                }
            }
            //this is where we add the possibleGCD value to the arraylist if it has survived
            if(possibleOutsideGCD!=1 && possibleOutsideGCD!=0){
                topDivisors.add(possibleOutsideGCD);
            }

        }

        ArrayList<Integer> botDivisors = new ArrayList<Integer>();
        outerBotLoop:
        for(EquationNode outside: fraction.getBottom()){//these should all be nominals.  if not, clear list and break
            int possibleOutsideGCD = 0;
            for(int i = 0; i<fraction.getBottom().size(); i++){
                //for(EquationNode inside: fraction.getTop()){//
                if(fraction.getBottom().get(i) instanceof Nominal){
                    double outsideNum = outside.getNum();
                    double insideNum = fraction.getBottom().get(i).getNum();

                    if(outsideNum == Math.round(outsideNum) && insideNum == Math.round(insideNum)){//if both are ints
                        if(possibleOutsideGCD == 0){
                            possibleOutsideGCD=GCD((int)outsideNum,(int)insideNum);
                        }
                        else{
                            if(!((int)insideNum%possibleOutsideGCD==0)){//so the factor doesnt work
                                possibleOutsideGCD = GCD((int)outsideNum,(int)insideNum);
                                i=0;//reseting the loop so that this new gcd value is tested.  if the value is 1, then it will work (no infinite loop)
                                continue;
                                //reset the loop and then continue; to loop through again

                            }//if false, then its good -- do nothing because this factor works.
                        }
                    }
                    else
                    {
                        canDivide = false;
                        botDivisors.clear();
                        break outerBotLoop;
                    }

                }
                else{
                    canDivide = false;
                    botDivisors.clear();
                    break outerBotLoop;
                }
            }
            //this is where we add the possibleGCD value to the arraylist if it has survived
            if(possibleOutsideGCD!=1 && possibleOutsideGCD!=0){
                botDivisors.add(possibleOutsideGCD);
            }

        }

        //now both of the lists have the GCD and can be used to find something to divide by.

        if(canDivide) {
            Integer[] topDiv = topDivisors.toArray(new Integer[topDivisors.size()]);//its late
            Integer[] botDiv = botDivisors.toArray(new Integer[botDivisors.size()]);//im tired and this might work

            Arrays.sort(topDiv);//yay sorting
            Arrays.sort(botDiv);//ascending order

            bottomFractLoop:
            for (int x = botDiv.length-1; x >= 0; x--) {
                for (int y = topDiv.length-1; y >= 0; y--) {
                    if (topDiv[y] % botDiv[x] == 0) {//yaya it actually works. this means that the greatist divisor has been found.
                        LinkedList<EquationNode> tmpTop = new LinkedList<EquationNode>();
                        LinkedList<EquationNode> tmpBot = new LinkedList<EquationNode>();

                        for (EquationNode node: fraction.getTop()){
                            tmpTop.add(new Nominal((node.getNum()/botDiv[x]),node.getVar()));
                        }
                        for (EquationNode node: fraction.getBottom()){
                            tmpBot.add(new Nominal((node.getNum()/botDiv[x]),node.getVar()));
                        }

                        fraction.getTop().clear();
                        fraction.getTop().addAll(tmpTop);

                        fraction.getBottom().clear();
                        fraction.getBottom().addAll(tmpBot);



                            break bottomFractLoop;
                    }
                }
            }
        }

        /*
        above loops divided shit.  now its time to remove the variables.  yay dividing by variables.
        how to do this.  we will just get a int that says the lowest var exponent that each have.
        then compare the two and whicever is the smallest we will then just remove that many number of exponents from all of them
        negative exponents do not count.
        wait, we can just do Math.abs(and use those values.  maybe.  i will think about it.
        nope. no negative exponents so far
        */

        boolean canReduceVars = true;
        int topSmallestVar = -1;//non innitialized values
        int botSmallestVar = -1;

        for(EquationNode x: fraction.getTop()){
            if(x instanceof Nominal){
                if(topSmallestVar == -1) {
                    topSmallestVar = (int) x.getVar();
                }
                else if(topSmallestVar>x.getVar() && x.getVar()>=0) {
                    topSmallestVar = (int) x.getVar();
                }
            }
            else{
                canReduceVars = false;
            }
        }

        for(EquationNode x: fraction.getBottom()){
            if(x instanceof Nominal){
                if(botSmallestVar == -1) {

                    botSmallestVar = (int) x.getVar();
                }
                else if(botSmallestVar>x.getVar() && x.getVar()>=0) {
                    botSmallestVar = (int) x.getVar();
                }
            }
            else{
                canReduceVars = false;
            }
        }

        if(canReduceVars){
            int reduceValue;
            if(topSmallestVar>=botSmallestVar)
                reduceValue = botSmallestVar;
            else
                reduceValue = topSmallestVar;

            LinkedList<EquationNode> tmpTop = new LinkedList<EquationNode>();
            LinkedList<EquationNode> tmpBot = new LinkedList<EquationNode>();

            for (EquationNode node: fraction.getTop()){
                tmpTop.add(new Nominal((node.getNum()),node.getVar()-reduceValue));
            }
            for (EquationNode node: fraction.getBottom()){
                tmpBot.add(new Nominal((node.getNum()),node.getVar()-reduceValue));
            }

            fraction.getTop().clear();
            fraction.getTop().addAll(tmpTop);

            fraction.getBottom().clear();
            fraction.getBottom().addAll(tmpBot);
        }





        /* test at end to see if divided by 1 on the bottom.  if so, return nominal
        jk.  we will always return the fraction, even with the 1 denominator
        */
        return fraction;

    }


    public static int GCD(int a, int b) {//greatest common divisor
        if (b==0) return a;
        return GCD(b,a%b);
    }
}
