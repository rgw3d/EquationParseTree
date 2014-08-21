import java.util.LinkedList;

/**
 * Created by rgw3d on 8/16/2014.
 */
public class MathOperations {

    public static LinkedList<EquationNode> multiply(LinkedList<EquationNode> Terms) throws CanNotEval{

        LinkedList<EquationNode> toMultiply = new LinkedList<EquationNode>();

        

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


        NumberStructure simplifiedNominal = multiplyNumberStructs(numberStructures);//multiply the numberStructures together
        LinkedList<EquationNode> simplifiedLinkedList = multiplyLists(groups);//multiply the lists together
        LinkedList<EquationNode> result = multiplyFinal(simplifiedNominal, simplifiedLinkedList);//multiply everything together

        return result;
    }
    public static NumberStructure multiplyNumberStructs(LinkedList<EquationNode> numberStructs) throws CanNotEval{

        NumberStructure combine = new Nominal(1,0);//set default value
        for(EquationNode cycle: numberStructs){
            if(combine instanceof Fraction && !(cycle instanceof Fraction)){
                combine = new Fraction(multiplyFinal(cycle,(combine.getTop()),combine.getBottom());
            }
            if(!(combine instanceof Fraction) && (cycle instanceof Fraction)){

            }
            if((combine instanceof Fraction) && (cycle instanceof Fraction)){

            }
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

    public static LinkedList<EquationNode> multiplyFinal(NumberStructure numberStructure, LinkedList<EquationNode> list) throws CanNotEval{

        LinkedList<EquationNode> finalList = new LinkedList<EquationNode>();

        for(EquationNode x: list){
            finalList.add(new Nominal((numberStructure.getNum() * x.getNum()),(numberStructure.getVar() + x.getVar())));
        }

        return finalList;
    }

    public static LinkedList<EquationNode> divide(EquationNode a, EquationNode b){
    }


    public static LinkedList<EquationNode> power(EquationNode a, EquationNode b){
    }


    public static LinkedList<EquationNode> add(EquationNode a, EquationNode b){
    }


}
