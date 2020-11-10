//*******************************************************************************************************************************************
// 424	Assignment #1
// Created by Priya Singh |	Fall 2020
// Utility.java
// Contains all methods needed by Main.java to decipher a given ciphertext
// using frequency analysis, shift substitutions and columnar transposition
//*******************************************************************************************************************************************

import java.util.*;
import java.util.Map;
import java.util.Map.Entry;
import java.io.IOException;


public class Utility{
//Class Variables
   static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";//used to reference English alphabet to symbol


//*******************************************************************************************************************************************
// Frequency Counter
// Counts the number of occurances of a symbol in the ciphertext
// Source [1]: https://stackoverflow.com/questions/60093999/using-hash-maps-in-java-to-find-frequency-of-characters-in-a-string; https://stackoverflow.com/a/60094876
//*******************************************************************************************************************************************
   static Map countFrequency(String cipherText){
   //[1] (Ln 26-39) Code from https://stackoverflow.com/questions/60093999/using-hash-maps-in-java-to-find-frequency-of-characters-in-a-string; https://stackoverflow.com/a/60094876
      Map<Character, Integer> map = new HashMap<Character, Integer>();//create new hashmap object to map symbol to frequency
   
   //for all symbols in the ciphertext        
      for (int i = 0; i < cipherText.length(); i++){
      //if the hashmap already contains a mapping for the symbol at cipherText[i] 
         if (map.containsKey(cipherText.charAt(i))){
            int n = map.get(cipherText.charAt(i)) + 1;//add 1 to the total frequency count   
            map.put(cipherText.charAt(i), n);//update frequency count in the hashmap
         }//end if
         //Otherwise, add the symbol to the hasmap with the frequncy of 1 
         else {
            map.put(cipherText.charAt(i), 1);
         }//end else
      }//end for
      
      return (Map) map;//return hashmap of frequencies
   }//end countFrequency


//*******************************************************************************************************************************************
//Frequency Sorter
//Sorts the occurances of a unique symbol in the ciphertext decreasing order of frequency of occurance
//Changed it to use ArrayList and Map since HashMap wasn't converting into a list.
//Source [1]: https://stackoverflow.com/questions/8119366/sorting-hashmap-by-values; https://stackoverflow.com/a/13913206
//Source [2]: https://stackoverflow.com/questions/10462819/get-keys-from-hashmap-in-java; https://stackoverflow.com/a/10462838
//*******************************************************************************************************************************************
   static ArrayList sortFrequencyList(Map frequencyMap){
   //[1] (Ln 57-72) Code from https://stackoverflow.com/questions/8119366/sorting-hashmap-by-values; https://stackoverflow.com/a/13913206
      List<Entry<Character, Integer>> list = new ArrayList<Entry<Character, Integer>>(frequencyMap.entrySet());
   
      Collections.sort(list, 
         new Comparator<Entry<Character, Integer>>()
         {
            public int compare(Entry<Character, Integer> o1,
                    Entry<Character, Integer> o2){
               return o2.getValue().compareTo(o1.getValue());
            }
         });
        
      Map<Character, Integer> sortedMap = new LinkedHashMap<Character, Integer>();
      for (Entry<Character, Integer> entry : list)
      {
         sortedMap.put(entry.getKey(), entry.getValue());
      }
   
   //[2] (Ln 75) Code from https://stackoverflow.com/questions/10462819/get-keys-from-hashmap-in-java; https://stackoverflow.com/a/10462838
      List sortedList = new ArrayList(sortedMap.keySet());//store map keys into list
   
      return (ArrayList) sortedList;
   }//end sortFrequencyList
   
   
//*******************************************************************************************************************************************
//Frequency Analyzer
//Determines the number of possible shifts needed to find the common charatcter in the alphabet for the 
//corresponding ciphertext symbol
//Source [1]: https://en.wikipedia.org/wiki/Frequency_analysis
//Source [2]: https://github.com/eat41899/CipherText 
//*******************************************************************************************************************************************
   static Set analyzeFrequencies(List<Character> sortedList){
      //Local Variables
      char[] commonLetters = {'E', 'T', 'A', 'O', 'I', 'N', 'S', 'H', 'R', 'D', 'L', 'U'};//[1] (Ln 89) Most common English symbols according to Wikipedia
      
      //[2] (Ln 93-132) Code from https://github.com/eat41899/CipherText
      Set<Integer> possibleShifts = new HashSet<Integer>();
      char shiftedChar;
      
      //For each symbol in the sortedList, locate it's position in the alphabet.
      for(int i = 0; i < sortedList.size(); i++){
         //Scope Variables
         int alphaIndex = ALPHABET.indexOf(sortedList.get(i));
         boolean done = false;
         int shift = 1;
         
         do{
         //Going from Z to A, but starting with the cipher symbol in the sortedList
         //if we are not at the beginning of the alphabet (0==A)
            if((alphaIndex - shift) >= 0){
               shiftedChar = ALPHABET.charAt(alphaIndex - shift);//get the next letter before
            }//end if
            //otherwise we are at the beginning of the alphabet (0==A)
            else{
               int shiftRmdr = Math.abs(alphaIndex - shift) % 25;//wrap around to the end of the alphabet (25==Z)              
               shiftedChar = ALPHABET.charAt(26 - shiftRmdr);//get the next letter before
            }//end else          
            
            //Check if the chosen letter is in the list of common letters            
            for(int j = 0; j < commonLetters.length; j++){
               char ch = commonLetters[j];
               
               //if it is, save the number of shifts it took in the list to find the common character
               if(shiftedChar == ch){
                  possibleShifts.add(shift);
                  done = true;
               }//end if
            
            }//end for
         
            //if not, then add a shift
            shift++;
         
         }while(!done);//end do-while
      
      }//end for
      
      return possibleShifts;
   }//end analyzeFrequencies
   
   
//*******************************************************************************************************************************************
//Shift Substitution
//Decrypts ciphertext using possible shift substitutions
//Source [1]: https://github.com/eat41899/CipherText 
//*******************************************************************************************************************************************
   static Set shiftSubstitute(Set<Integer> possibleShifts, String ciphertext){
   //Code (Ln 143-175) from [1] https://github.com/eat41899/CipherText
      Set<String> possibleStrings = new HashSet<>();
           
   //Go through all possible shifts 
      for(Integer shift : possibleShifts){
      
         String plainText = "";
         char[] shiftText = ciphertext.toCharArray();
      
        //For each character in the ciphertext
         for(int i = 0; i < shiftText.length; i++){
         //get the index of the ciphertext char based on the alphabet
            int alphaIndex = ALPHABET.indexOf(shiftText[i]);
         
            //If the shift count requires to wrap around the alphabet
            if((alphaIndex - shift) >= 0){
               plainText += ALPHABET.charAt(alphaIndex - shift);
            }//end if
            // else get the char located on the alphabet after shifting
            else{
               int shiftRmdr = Math.abs(alphaIndex - shift) % 25;
               plainText += ALPHABET.charAt(26 - shiftRmdr);
            }//end else
         }//end for
      
         possibleStrings.add(plainText);//gives shift substitute decrypted strings based on the # of shifts possible
      }//end for
   
      return possibleStrings;
   }//end shiftSubstitute


//*******************************************************************************************************************************************
//Key Generator
//Generates all possible keys based on given key length
//Source [1]: https://github.com/eat41899/CipherText 
//*******************************************************************************************************************************************
   static List<String> generateKeys(){
    //Code (Ln 184-201) Source [1]: https://github.com/eat41899/CipherText 
    //Add each permutation to the keyList
      List<String> keyList = new ArrayList<>();
      
      //Slowly work way up to key length of 10
      //starts lagging at 1 <= 8, so adjust range accordingly
      for(int keyLength = 7; keyLength <= 7; keyLength++){
         //key is generated from Alphabet starting at index 0 to i-1
         String key = ALPHABET.substring(0, keyLength);
         
         //Add each permutation generated from the current key
         for(String str : permutation(key)){
            keyList.add(str);
         }//end for
      }//end for
      return keyList;
   }//end generateKeys
   
   
//*******************************************************************************************************************************************
//Key Permutations
//Used by generateKeys() to permutate all possible keys for given string.
//Source [1]: https://stackoverflow.com/questions/4240080/generating-all-permutations-of-a-given-string; https://stackoverflow.com/a/20614037
//*******************************************************************************************************************************************
   static Set<String> permutation(String input){
      //Code (Ln 207-233) Source [1]: https://stackoverflow.com/questions/4240080/generating-all-permutations-of-a-given-string; https://stackoverflow.com/a/20614037
      Set<String> set = new HashSet<String>();
      
      //if there is no string or we are at the end of the string
      if(input == ""){
         return set;
      }//end if
   
      char a = input.charAt(0);
   
      //if there is a string
      if(input.length() > 1){
      //Local variables
         input = input.substring(1);
         Set<String> permSet = permutation(input);
      
         for(String x : permSet){
            for(int i = 0; i <= x.length(); i++){
               set.add(x.substring(0, i) + a + x.substring(i));
            }//end for
         }//end for
      }//end if
      //else we are at the end of the string
      else{
         set.add(a + "");
      }//end else
      
      return set;
   }//end permutation
   

//*******************************************************************************************************************************************
//Columnar Transposition
//Performs columnar transposition on passed in possibleStrings using the possible keys in the keyList
//Source [1]: https://github.com/eat41899/CipherText 
//Source [2]: https://programmingcode4life.blogspot.com/2015/09/columnar-transposition-cipher.html
//*******************************************************************************************************************************************
   static void columnarTranspose(Set<String> possibleStrings, List<String> keyList) throws IOException{
   //Code (Ln 247-265) Source [1]: https://github.com/eat41899/CipherText 
      Dictionary dictionary = new Dictionary();
      int count = 1;
         
      //for each possible string
      for(String strCandidate : possibleStrings){
      //use every possible key on the string
         for(int i = 0; i < keyList.size(); i++){
         //Local Variables
            String strKeys = keyList.get(i);
            String key = strKeys;  
            int keyLength = key.length();
            String str = strCandidate;
            int remainder = str.length() % keyLength;
            int flag = remainder;
            int amountOfRows = (int) Math.ceil((double) str.length() / key.length());
            char[][] grid = new char[amountOfRows][keyLength];
            String newStr = "";
         
         
         //Code (Ln 268-280) Source [2]: https://programmingcode4life.blogspot.com/2015/09/columnar-transposition-cipher.html
            //Converting the alphabetic characters to numbers for the grid
            String[] keys = key.split("");//split each character in the string
            Arrays.sort(keys);//sort the split string
            int[] num = new int[key.length()];//create a new array for the numbered key
            
            for(int x = 0; x < keys.length; x++){//for the characters in the sorted key
               for(int y = 0; y < key.length(); y++){//for the characters in the initial key
               //if the character in keys[x] == key.charAt(y)
                  if(keys[x].equals(key.charAt(y) + "")){
                     num[y] = x;//find which position the numeric equivalent is EX. A=0, B=1,... etc.
                     break;
                  }//end if
               }//end for
            }//end for
            
            int[] arrangedKeyArr = num;//key in numeric form
            
            
         //Code (Ln 282-350) Source [1]: https://github.com/eat41899/CipherText 
         //finds each col & match it against the int from arrangedKeyArr
            for(int col = 0; col < keyLength; col++){//starting in the first column
               for(int keyInt : arrangedKeyArr){//for each number/column in the key
                  if(keyInt == col){
                  
                    //First, account for message that is not padded
                    //Which means empty spaces in the grid if string length isn't the multiple of the key length
                    //if there is empty space in the column
                     if(flag <= keyLength && flag != 0){//flag == 0 means there is no empty space, flag <= keyLength means there is
                        //This loop starts at the last columns since they will
                        // be the ones requiring padding if needed, if padding
                        // is needed, it won't be for more columns than the
                        // remainder count.
                        for(int p = keyLength - 1; p > remainder - 1; p--){//pad the empty cells
                           if(arrangedKeyArr[p] == col){//if the padding is for this column, starting from the last column
                              newStr = str.substring(0, (amountOfRows - 1));//get the string in the column
                              newStr = newStr + "X";//add the padding "X" to the end
                              flag++;//reduce the remaining spaces
                              str = str.substring((amountOfRows - 1), str.length());//update the string
                           }//end if
                        }//end for
                     }//end if
                     
                    //No padding required for this col, take substring of row
                    // length to populate the column in grid
                     if(newStr == ""){
                        newStr = str.substring(0, amountOfRows);//get portion of string candidate string for column
                        str = str.substring(amountOfRows, str.length());// update string candidate by removing portion taken
                     }//end for
                     
                    //Populate grid at the given column
                     for(int row = 0; row < amountOfRows; row++){
                        grid[row][col] = newStr.charAt(row);//add string portion characters to grid at (row,col)
                     }//end for
                     
                     newStr = "";//cleaning variable for next loop in case it was used
                  }//end if
               }//end for
            }//end for
            
            
         //Populate the message with the all of the characters
         // in order of the arrangedKey array since the keys now
         // correspond to the column indexes of the grid. Begins
         // with the first row at zero.
            String message = "";//hold message
            for(int r = 0; r < amountOfRows; r++){//for each row
               for(int keyInt : arrangedKeyArr){//for each num of the key
                  message = message + grid[r][keyInt];//get the char of the message.
               }//end for
            }//end for
            
         //System.out.println(message);
            String deciphered = message;
            
         //print possible candidates
            if(segmentString(deciphered, dictionary) != null){//if deciphered string has a dictionary word
               System.out.println("Candidate: [" + strCandidate + "] key = " + strKeys);
               System.out.println("Deciphered: #" + count + " [" + segmentString(deciphered, dictionary) + "]");
               System.out.println();
               count++;
            }//end if
         }//end for
      }//end for
   }//end columnarTranspose
   
   
//*******************************************************************************************************************************************
//String Segmentation
//Checks the given string for words that are present in the dictionary.
//Source [1]: https://github.com/eat41899/CipherText 
//Source [2]: http://thenoisychannel.com/2011/08/08/retiring-a-great-interview-problem
//*******************************************************************************************************************************************
    //Segment a String based on dictionary words
   private static String segmentString(String newString, Dictionary dictionary){
      for(int i = 1; i < newString.length(); i++){
         String prefix = newString.substring(0, i);//check a portion of the start of the string
         
            //If the dictionary file contains the word taken from index 0
            // to i (prefix) return it with a space and the remaining string
            // which will perform a recursive call on that remaining string
            //finds all possible substrings using prefix and suffix search
         if(dictionary.wordExists(prefix)){//if there is a word in the prefix
            String suffix = newString.substring(i, newString.length());//get the rest of the string
            String segSuffix = segmentString(suffix, dictionary);//check the rest of the string for words
                        
            if(segSuffix != null){//if there are other words
               return prefix + " " + segSuffix;//go back up a level
            }//end if
            else{
               return suffix;//complete recursion
            }//end else
         }//end if
      }//end for
      
        //if no word was able to be found, will return null
        // helpful when attempting to filter a final decrypted
        // message
      return null;
   }//end segmentString
}//end Utility