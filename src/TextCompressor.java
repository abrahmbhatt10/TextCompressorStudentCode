/******************************************************************************
 *  Compilation:  javac TextCompressor.java
 *  Execution:    java TextCompressor - < input.txt   (compress)
 *  Execution:    java TextCompressor + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   abra.txt
 *                jabberwocky.txt
 *                shakespeare.txt
 *                virus.txt
 *
 *  % java DumpBinary 0 < abra.txt
 *  136 bits
 *
 *  % java TextCompressor - < abra.txt | java DumpBinary 0
 *  104 bits    (when using 8-bit codes)
 *
 *  % java DumpBinary 0 < alice.txt
 *  1104064 bits
 *  % java TextCompressor - < alice.txt | java DumpBinary 0
 *  480760 bits
 *  = 43.54% compression ratio!
 ******************************************************************************/

import java.util.Hashtable;

/**
 *  The {@code TextCompressor} class provides static methods for compressing
 *  and expanding natural language through textfile input.
 *
 *  @author Zach Blick, Agastya Brahmbhatt
 */
public class TextCompressor {
private static Character startDict = 0;
private static String punctuationString = " .,;!?:\"";

    private static void compress() {
        Hashtable<String,Character> my_dict = new Hashtable<String,Character>();
        Character wordCount = 0;
        String inputWord = "";
        Character currentLetter = 0;
        boolean dictFlag = false;
        for(int i = 0; i < punctuationString.length(); i++)
        {
            wordCount++;
            currentLetter = punctuationString.charAt(i);
            inputWord = currentLetter.toString();
            my_dict.put(inputWord,wordCount);
        }
        inputWord = "";
        while(!BinaryStdIn.isEmpty()){
            currentLetter = BinaryStdIn.readChar();
            if(punctuationString.contains(currentLetter.toString()))
            {
                if(my_dict.containsKey(inputWord)){
                    if(!dictFlag){
                        BinaryStdOut.write(startDict);
                        dictFlag = true;
                    }
                    if(inputWord.length() > 0)
                        BinaryStdOut.write(my_dict.get(inputWord));
                }
                else{
                    if(inputWord.length() > 0) {
                        wordCount++;
                        my_dict.put(inputWord, wordCount);
                        if(dictFlag){
                            BinaryStdOut.write(startDict);
                            dictFlag = false;
                        }
                        BinaryStdOut.write(inputWord);
                    }
                }
                inputWord = "";
                if(dictFlag)
                {
                    BinaryStdOut.write(my_dict.get(currentLetter.toString()));
                } else {
                    BinaryStdOut.write(currentLetter);
                }
            }
            else{
                inputWord += currentLetter.toString();
            }
        }
        BinaryStdOut.close();
    }

    private static void expand() {
        Hashtable<Character, String> my_dict = new Hashtable<Character, String>();
        Character wordCount = 0;
        String inputWord = "";
        Character currentLetter = 0;
        boolean dictFlag = false;
        for(int i = 0; i < punctuationString.length(); i++)
        {
            wordCount++;
            currentLetter = punctuationString.charAt(i);
            inputWord = currentLetter.toString();
            my_dict.put(wordCount,inputWord);
        }
        inputWord = "";
        while (!BinaryStdIn.isEmpty()) {
            currentLetter = BinaryStdIn.readChar();
            if(currentLetter == startDict){
                dictFlag = !dictFlag;
            }
            if(dictFlag){
                BinaryStdOut.write(my_dict.get(currentLetter));
            }
            else if(punctuationString.contains(currentLetter.toString())) {
                BinaryStdOut.write(inputWord);
                wordCount++;
                my_dict.put(wordCount, inputWord);
                inputWord = "";
                BinaryStdOut.write(currentLetter);
            }
            else{
                inputWord += currentLetter.toString();
            }
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
