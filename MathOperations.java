import java.util.LinkedList;

/**
 * Created by rgw3d on 8/16/2014.
 */
public class MathOperations {

    public static LinkedList<EquationNode> multiply(LinkedList<EquationNode> Terms) throws CanNotEval{


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


        NumberStructure simplifiedNominal = multiplyNumberStructures(numberStructures);//multiply the numberStructures together
        LinkedList<EquationNode> simplifiedLinkedList = multiplyLists(groups);//multiply the lists together
        LinkedList<EquationNode> result = multiplyFoil(simplifiedNominal, simplifiedLinkedList);//multiply everything together

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
                //multiply the top together, and then add the bottom back on to it.  make combine a fraction.
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

    public static LinkedList<EquationNode> divide(EquationNode a, EquationNode b){
        return  new LinkedList<EquationNode>();
    }


    public static LinkedList<EquationNode> power(EquationNode a, EquationNode b){
        return  new LinkedList<EquationNode>();
    }


    public static LinkedList<EquationNode> add(EquationNode a, EquationNode b){
        return  new LinkedList<EquationNode>();
    }


}
