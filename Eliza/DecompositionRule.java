import java.util.*;

/*
stores info and methods for decompsition rules
contains a decomposition rule, and an array of recomposition
rules that apply to it
*/
public class DecompositionRule {

    //the regex decomposition rule
    private String decompositionRule;

    //array of regex reconstruction rules
    private List<String> recompositionRules;



    //constructor takes in decomposition rule
    public DecompositionRule(String decompositionRule){
            
        //set decomp rule
        setDecompositionRule(decompositionRule);

        //initialise recomp rules list
        recompositionRules = new ArrayList<>();
    }

    //constructor takes in decomposition rule
    public DecompositionRule(){
    }

    /* decomposition rule methods */
    public String getDecompositionRule(){
        return decompositionRule;
    }

    public void setDecompositionRule(String decompositionRule){
        this.decompositionRule = decompositionRule;
    }


    /* recomposition rule methods */
    public List<String> getRecompositionRules(){
        return recompositionRules;
    }

    public void setRecompositionRules(List<String> recompositionRules){
        this.recompositionRules = recompositionRules;
    }

    public void addRecompositionRule(String recompositionRule){
        recompositionRules.add(recompositionRule);
    }


    //this method will select a random recomposition rule from the List
    public String getRandomRecompositionRule(){

        //this source was used to generate a random number
        //https://docs.oracle.com/javase/8/docs/api/java/util/Random.html , last accessed 2021/02/15

        //generates a random integer between 0 and size of List-1
        //ie returns a random recomposition rule from the list
        Random rand = new Random();
        int randomInt = rand.nextInt(recompositionRules.size());

        //return the random rule
        return recompositionRules.get(randomInt);
    }








}
