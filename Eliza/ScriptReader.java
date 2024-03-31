//imports
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;

/*

this class is used to read and store information from the script

*/
public class ScriptReader {

    /* the information that will be stored */
        // the welcome and goodbye messages
        private String welcomeMessage;
        private String finalMessage;

        // a HasSet of quit commands
        private Set<String> quitCommands = new HashSet<>();

        // an list of the keywords
        private List<Keyword> keywords = new ArrayList<>();

        //HashMaps to store pre and post substitution rules
        //the key is the word to be replaced and the value is the replacement word
        private Map<String, String> preSubstitutionRules = new HashMap<>();
        private Map<String, String> postSubstitutionRules = new HashMap<>();


    /* the constructor takes in file name, reads in data from file and stores it in variables */
    //there is little to no error handling
    public ScriptReader(String fileName) throws Exception {

    /* read all lines of the file into an arrayList */
        //creates a path object from the file name given
        Path filePath = Path.of(fileName);

        //if the file either does not exist or is not readable
        if (!Files.exists(filePath) || !Files.isReadable(filePath)){

            //if so, throw exception
            throw new Exception("file does not exist/cannot read file");

        }

        //Read all lines of the file and store it in a list
        List<String> scriptFile = Files.readAllLines(Path.of(fileName));


    /* process the file information stored in arrayList */

        //create an iterator to loop through each line of script
        Iterator<String> scriptIterator = scriptFile.iterator();


        // The first 2 lines of the script will always be the welcome and goodbye message so assign them accordingly
        welcomeMessage = scriptIterator.next();
        finalMessage = scriptIterator.next();


        /* initialise variables used to loop through the rest of the file */

            //the current line being used from the file
            String currentLine;
            //the first char of each line, denotes what information the line stores
            char denoter;

            //NOTE: both of these are only initialised here to avoid "may not be initialised" errors
            Keyword tempKeyword = new Keyword(); //holds the most recent keyword read
            DecompositionRule tempDecompRule = new DecompositionRule(); //holds the most recent decomposition rule read

        
        /*

        the script file is structured like:
        <keyword>
            <decomp rule>
                <reassemble rule>
                <reassemble rule>
            <decomp rule>
                <reassemble rule>

        
        each keyword is followed immediately by its decomp rules, each of which
        are followed by their recomp rules

        when a recomp rule is seen, the string is stored within the most recent decomp rule object
        when a decomp rule is seen, a new decomp object is created and it is stored in the most recent keyword object
        when a keyword is seen, a new keyword object is created

        */
        

        //loop through each line of the file
        while(scriptIterator.hasNext()){

            //store the next line into variable, also reduce to lower case and remove trailing spaces
            currentLine = scriptIterator.next().trim().toLowerCase();

                //ignores empty lines
                if(currentLine.length() != 0){

                //seperate the denoter from the rest of string
                denoter = currentLine.charAt(0); //describes information stored in the string
                currentLine = currentLine.substring(1, currentLine.length()).trim(); //removes first char, trims again


                //use the denoter to decide what to do with the line
                switch(denoter){

                    //@ is our denoter for the quit commands in the script
                    case '@':
                        //add the current string to quit commands set
                        quitCommands.add(currentLine);
                        break;

                    //& is our denoter for the keywords in the script
                    case '&':
                        //create a new keyword object, pass in the keywords and add to keywords object array
                        tempKeyword = new Keyword(currentLine.split(" *, *"));
                        keywords.add(tempKeyword);
                        break; 
                    
                    //& is our denoter for the decomposition rule in the script
                    case '~':
                        //create a new decomposition rule object, pass in the rule and add to most recent keyword
                        tempDecompRule = new DecompositionRule(currentLine);
                        tempKeyword.addDecompositionRule(tempDecompRule);
                        break;

                    //& is our denoter for the recomposition rule in the script
                    case '#':
                        //add the recomposition rule to most recent decomposition rule
                        tempDecompRule.addRecompositionRule(currentLine);
                        break;   
                        
                    //£ is our denoter for a pair of pre-substitution words
                    case '£':
                        //pass in string split into array by ',' delimiter (and trim spaces)
                        addPreSubstitutionRule(currentLine.split(" *, *"));
                        break;

                    //% is our denoter for a pair of post-substitution words
                    case '%':
                        //put the words before and after the coma into the HashMap as key and value respectively
                        addPostSubstitutionRule(currentLine.split(" *, *"));
                        break;
                }      
                
            }


        }
        
    } //end of constructor


/* methods for welcome message */
    public String getWelcomeMessage(){
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage){
        this.welcomeMessage = welcomeMessage;
    }


/* methods for final message */
    public String getFinalMessage(){
        return finalMessage;
    }

    public void setFinalMessage(String finalMessage){
        this.finalMessage = finalMessage;
    }


/* methods for quit commands set */
    public Set<String> getQuitCommands(){
        return quitCommands;
    }

    public void setQuitCommands(Set<String> quitCommands){
        this.quitCommands = quitCommands;
    }

    public void addQuitCommand(String quitCommand){
        quitCommands.add(quitCommand);
    }


/* methods for keywords List */
    public List<Keyword> getKeywords(){
        return keywords;
    }

    public void setKeywords(List<Keyword> keywords){
        this.keywords = keywords;
    }

    public void addKeyword(Keyword keyword){
        keywords.add(keyword);
    }


/* methods for presubstitution hashMap */
    public Map<String, String> getPreSubstitutionRules(){
        return preSubstitutionRules;
    }
    
    public void setPreSubstitutionRules(Map<String, String> preSubstitutionRules){
        this.preSubstitutionRules = preSubstitutionRules;
    }
    
    //takes in the 2 args as seperate strings
    public void addPreSubstitutionRule(String toReplace, String replacement){
        preSubstitutionRules.put(toReplace, replacement);
    }

    //takes in the 2 args as an array
    public void addPreSubstitutionRule(String[] arrayString){
        
        //pass first 2 strings into other method with same name
        if(arrayString.length >= 2){
            addPreSubstitutionRule(arrayString[0], arrayString[1]);
        }

    }


/* methods for postsubstitution hashMap */
    public Map<String, String> getPostSubstitutionRules(){
        return postSubstitutionRules;
    }
    
    public void setPostSubstitutionRules(Map<String, String> postSubstitutionRules){
        this.postSubstitutionRules = postSubstitutionRules;
    }
    
    public void addPostSubstitutionRule(String toReplace, String replacement){
        postSubstitutionRules.put(toReplace, replacement);
    }

     //takes in string in form of array rather than 2 seperate strings
     public void addPostSubstitutionRule(String[] arrayString){
        
        //pass first 2 strings into other method with same name
        if(arrayString.length >= 2){
            addPostSubstitutionRule(arrayString[0], arrayString[1]);
        }

    }




/* method to generate respone to user input */
    public String generateResponse(String userInput){
        
        //format the users input before processing it
        userInput = userInput.toLowerCase().trim(); //convert to lower case and trim
        userInput = applyPresubstitution(userInput); //apply pre-substitution rules


        //identify and store the correct keyword and decomposition rule for the input
        Keyword identifiedKeyword = getUserKeyword(userInput);
        DecompositionRule identifiedDecomp = identifiedKeyword.findDecomp(userInput);
        //Get a recomposition rule from the decomposition rule
        String recompositionRule = identifiedDecomp.getRandomRecompositionRule();

        

        //remove asterisks (and trailing spaces) from decomposition rule
        String decompWithoutAsterisk = identifiedDecomp.getDecompositionRule().replaceAll("\\*", "").trim();

        //split user input by decomp rule to get values of asterisks at start and end of decomp rule
        String[] asteriskValues = userInput.split(decompWithoutAsterisk);


        //loop through each asterisk value
        for(int i = 0; i < asteriskValues.length; i++){

            //apply post-substitution rules to the string
            asteriskValues[i] = applyPostsubstitution(asteriskValues[i]);

            //replace placeholder numbers with their actual values
            //0 means * at start of decomp rule
            //1 means * at end of decomp rule
            recompositionRule = recompositionRule.replaceAll(String.valueOf(i) , asteriskValues[i]);

        }

        
        //return eliza's response to the user
        return recompositionRule;

        


    } //end of generateResponse method
    

    /* Method to find the keyword in the user input and since the highest priority keywords will be closer to top of the file than others
    then it will quit as soon as they find the first matching keyword */
    public Keyword getUserKeyword(String userInput){

        //split input into array of individual words
        String[] splitInput = userInput.split(" ");

        // Loop through each keyword in the list of keywords 
        for(int i = 0; i< keywords.size(); i++){
        
            //Loop through the each word in the user input
            for(String word : splitInput){

                //loop through all the keywords in that keyword object
                for(String keyword : keywords.get(i).getKeywords()){

                    //if the keyword
                    if(keyword.equals(word)){
                        return keywords.get(i);
                    }

                }
                
    
            }

        }

        //If no keyword is found return the final keyword which will be a general one
        return keywords.get(keywords.size()-1);
    }


    /*
    takes in a string and subs in words for other words based on script
    */
    public String applyPresubstitution(String stringInput){

        //split the input into an array of individual words
        String[] splitInput = stringInput.split(" ");

        //this will store the string produced from pre-substitution
        String output = "";

        //loop through each word in the input
        for(String word : splitInput){
            
            if(preSubstitutionRules.get(word) != null){
                output += preSubstitutionRules.get(word);
            }

            else{
                output += word;
            }

            output += " ";

        }

        //return the output (trimmed as there is an extra space at the end)
        return output.trim();
        
    }

    /*
    takes in a string and subs in words for other words based on script
    */
    public String applyPostsubstitution(String stringInput){

        //split the string into an array of individual words
        String[] splitInput = stringInput.split(" ");

        //this will store the string produced by the pre-substitution
        String output = "";

        //loop through each word in the input
        for(String word : splitInput){
            
            if(postSubstitutionRules.get(word) != null){
                output += postSubstitutionRules.get(word);
            }

            else{
                output += word;
            }

            output += " ";

        }

        //return the output (trimmed as there is an extra space at the end)
        return output.trim();
        
    }






}
