import java.util.*;
import java.util.regex.*;
/*
stores information and methods for keywords
contains a list of decomposition rules, and an array of
keywords that those decomposition rules apply to
*/
public class Keyword {
    
    //an array of string keywords, all of which share the same decomposition rules
    //eg ["belive", "belives"]
    private String[] keywords;

    //a list of all the decomposition rules (in order of priority, highest to lowest)
    private List<DecompositionRule> decompositionRules;

/* constructors */
    /* constructor taking in array of keywords */
    public Keyword(String[] keywords){

        //set the keyword
        setKeywords(keywords);

        //initialise the decomposition rules list
        decompositionRules = new ArrayList<>();

    }

    /* constructor takes in single keyword */
    public Keyword(String keyword){

        //put into 1 length array and call constructor with array parameters
        this(keyword.split("$")); //hoping to not split the string
        

    }

    /* blank constructor takes in no keywords*/
    public Keyword(){
        decompositionRules = new ArrayList<>();
        keywords = null;
    }

/* methods for keyword */
    public String[] getKeywords() {
        return keywords;
    }

    //takes in array of keywords
    public void setKeywords(String[] keywords) {
        //split the string into an array to store, by commas
        //also trims trailing spaces
        this.keywords = keywords;
    }


/* methods for decomposition rules */
    public List<DecompositionRule> getDecompositionRules(){
        return decompositionRules;
    }

    public void setDecompositionRules(List<DecompositionRule> decompositionRules) {
        this.decompositionRules = decompositionRules;
    }

    public void addDecompositionRule(DecompositionRule decompositionRule){
        decompositionRules.add(decompositionRule);
    }

    /* Method to find the appropriate decomposition rule object for a given string
    it is assumed that the string passed in has been checked and does contains a keyword from this object

    as decomposition rules are ordered by highest priority, the method returns after it finds the first
    decomposition rule that fits the input
    */
    public DecompositionRule findDecomp(String userInput){

        /*

          the following sources were used when writing this method:
              https://www.keycdn.com/support/regex-cheatsheet , last accessed 2021/02/19
              https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/regex/Pattern.html , last accessed 2021/02/19
              https://stackoverflow.com/questions/9341379/check-string-contains-whitespace-along-with-some-other-char-sequence-using-regex , last accessed 2021/02/19

        */

        //stores the string decomposition rule
        String tempDecompRule;

        // Loop through each decomposition rule
        //does not include the last one, as it is always a catch all '*' used if no others match
        for(int i = 0; i< decompositionRules.size()-1; i++){

            //assign value to temp decomposition rule
            tempDecompRule = decompositionRules.get(i).getDecompositionRule();

            
            //replaces an * at start with a wildcard that allows words to come before it
            tempDecompRule = tempDecompRule.replaceAll("\\* ", "(.* )?");
            //replaces the * at end with a wildcard that allows words to come after it
            tempDecompRule = tempDecompRule.replaceAll(" \\*", "( .*)?");

            //if the user input matches the decomp rule, then return the decomp rule object
            if( Pattern.matches(tempDecompRule, userInput)){

                //return the decomposition rule object
                return decompositionRules.get(i);
            }

        }


        //If no decomposition rule is found return the last decomposition rule
        // (the last one is always just '*', so will match anything)
        return decompositionRules.get( decompositionRules.size()-1 );
    }

}
