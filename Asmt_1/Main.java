//*******************************************************************************************************************************************
//424	Assignment #1
//Created by Priya Singh |	Fall 2020
//Main.java
//Contains all method call to Utility.java needed to decipher a given ciphertext
//using frequency analysis, shift substitutions and columnar transposition
//*******************************************************************************************************************************************

import java.util.*;
import java.io.IOException;

public class Main extends Utility {
   public static void main(String args[]) throws IOException {
   //Local Variables
      Map frequencyMap;
      ArrayList<Character> sortedList;
      Set<Integer> possibleShifts;
      Set<String>possibleStrings = new HashSet<>();
      List<String> keyList = new ArrayList<>();
   
   
   //get	encrypted string
      String ciphertext = "KUHPVIBQKVOSHWHXBPOFUXHRPVLLDDWVOSKWPREDDVVIDWQRBHBGLLBBPKQUNRVOHQEIRLWOKKRDD";
   
   //get	frequency count of each	character
      frequencyMap = Utility.countFrequency(ciphertext);
      // System.out.println("Frequency Map: " + frequencyMap);
            
   //sort the frequency list
      sortedList = Utility.sortFrequencyList(frequencyMap);
      //System.out.println("Sorted List: " + sortedList);
      
   //analyze the frequencies to associate most common letters
      possibleShifts = Utility.analyzeFrequencies(sortedList);
      // System.out.println("Possible Shifts: " + possibleShifts);
      
   //find all possible shifts based on the most occuring frequencies
      possibleStrings = Utility.shiftSubstitute(possibleShifts, ciphertext);
      // System.out.println("Possible strings: " + possibleStrings);
   
   //generate all possible keys
      keyList = Utility.generateKeys();
      // System.out.println("Key List: " + keyList);//all based on only possible shifts
   
   //apply columnar transposition to decrypt all strings using all possible keys with the set range
   //Uncomment to see the deciphered strings of all possible candidates and keys
      Utility.columnarTranspose(possibleStrings, keyList);   
      
     //Best match based on previous OUTPUT which segments the string based on dictionary words.
        /*Candidate: [HREMSFYNHSLPETEUYMLCRUEOMSIIAATSLPHTMOBAASSFATNOYEYDIIYYMHNRKOSLENBFOITLHHOAA]
          key = GCAEDBF
        Deciphered: #555 [BE HAPPY FOR THE MOMENT THIS MOMENT IS YOUR LIFE BY KHAY YAMOH AND ALSO THIS CLASS IS REALLY FUN]*/
      System.out.println("====================================================================================================");
      System.out.println("Results:\n");

        Set<String> candidate = new HashSet<>();
        String candidateStr = "HREMSFYNHSLPETEUYMLCRUEOMSIIAATSLPHTMOBAASSFATNOYEYDIIYYMHNRKOSLENBFOITLHHOAA";
        candidate.add(candidateStr);
        List<String> keyCand  = new ArrayList<>();
        String key = "GCAEDBF";
        keyCand.add(key);
        columnarTranspose(candidate, keyCand);
       
   }//end main
}//end Decrypter