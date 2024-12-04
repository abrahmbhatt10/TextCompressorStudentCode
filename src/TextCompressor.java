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
private static int count = 8;
private static int MAX_COUNT_SIZE = 255;
private static Character startDict = 0;
private static String punctuationString = ".,;!?:\"";

    private static void compress() {
        Hashtable<String,Character> my_dict = new Hashtable<String,Character>();
        Character wordCount = 0;
        String inputWord = "";
        Character inputWordLen = 0;
        Character currentLetter = 0;
        boolean dictFlag = false;
        while(!BinaryStdIn.isEmpty()){
            currentLetter = BinaryStdIn.readChar();
            if(currentLetter == ' ') {

            }
            else if(punctuationString.contains(currentLetter.toString()))
            {
                if(my_dict.containsKey(inputWord)){
                    if(!dictFlag){
                        BinaryStdOut.write(startDict);
                        dictFlag = true;
                    }
                    BinaryStdOut.write(my_dict.get(inputWord));
                }
                else{
                    wordCount++;
                    my_dict.put(inputWord, wordCount);
                }
                BinaryStdOut.write(currentLetter);
            }
            else{
                inputWord += currentLetter.toString();
            }
            BinaryStdOut.close();
        }
    }

    private static void expand() {

        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
