import java.util.*;

/*
The eliza class,
runs the conversation with the user
from the main method

*/
public class Eliza {

    // main method to run eliza
    public static void main(String[] args) throws Exception {

        //check only 1 arg passed in
        if(args.length != 1){
            throw new Exception("expected 1 argument but got " + args.length);
        }
        
        /* INITIALISE VARIABLES */
            boolean hasQuit = false;

            Scanner reader = new Scanner(System.in);
            String userInput;
            String response;



        /* READ FILE */

            //argument passed in is the file location
            ScriptReader script = new ScriptReader(args[0]);


        /* LOOP THROUGH CONVERSATION WITH USER */

            //display welcome message
            System.out.println(script.getWelcomeMessage());

            //keep responding until quit command
            while(!hasQuit){

                //get the next from the user
                userInput = reader.nextLine();

                //if the user entered a quit command
                if( script.getQuitCommands().contains(userInput)){
                    System.out.println(script.getFinalMessage());
                    hasQuit = true;
                    reader.close();
                }


                //process the input from the user
                else{
                    response = script.generateResponse(userInput); //generate a response
                    System.out.println("Eliza: " + response); //display the response
                }

            }




    } //end of main method


} //end of class
