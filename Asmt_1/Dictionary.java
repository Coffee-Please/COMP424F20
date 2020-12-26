// Dictionary.java
// Source [1]: https://github.com/eat41899/CipherText 
//*******************************************************************************************************************************************

import java.util.ArrayList;
import java.io.IOException;
import java.io.*;
import java.util.List;
import java.util.Scanner;

public class Dictionary{
    private ArrayList<String> dictionary;

    public Dictionary() throws IOException{

        dictionary = new ArrayList<>();
        readInDictionaryWords();
    }//end Dictionary


    private void readInDictionaryWords() throws FileNotFoundException{
        //create new file
        File dictionaryFile = new File("Dictionary.txt");
        
        //if file not found
        if(!dictionaryFile.exists()) {
            System.out.println("*** Error *** \n" +
                    "Your dictionary file has the wrong name or is " +
                    "in the wrong directory.  \n" +
                    "Aborting program...\n\n" + System.getProperty("user.dir"));
            System.exit( -1);    // Terminate the program
        }//end if

         //add words to internal dictionary using opened file
        Scanner inputFile = new Scanner(dictionaryFile);
        while(inputFile.hasNext()) {
            dictionary.add( inputFile.nextLine().toUpperCase() );
        }//end while
    }//end readInDictionaryWords


    public boolean wordExists(String wordToLookup){
        if( dictionary.contains(wordToLookup)) {
            return true;// words was found in dictionary
        }//end if
        else {
            return false;// word was not found in dictionary
        }//end else
    }//end wordExist
}//end class Dictionary
