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

/**
 *  The {@code TextCompressor} class provides static methods for compressing
 *  and expanding natural language through textfile input.
 *
 *  @author Zach Blick, Agastya Brahmbhatt
 */
public class TextCompressor {
    /*
        Sedgewick and Wayne's Algorithms 4th edition helped me write the below code.
     */
    private static final int WIDTH = 12; // Number of bits used to store a character (A, B, ...) in the compressed format.
    private static final int R = 256; // Used for end of file
    private static final int L = 4096; // 2^12, so code word can go up to 0 to 4095.

    private static void compress() {
        String input; // Variable used to save data from the input file.
        TST dict = new TST(); // Variable used to store string code pairs.
        // the for loop below is used to initialize 0 to 255 characters with their codes.
        for(int i = 0; i < R; i++){
            dict.insert("" + (char)i, i);
        }
        // The string matching lookup code from the input text will get R + 1 code (stored as the prefix nodes in the trie).
        int code = R+1;
        String s; // Variable to hold longest prefix
        int t; // Variable for the length of the string.
        // Get input file strings one at a time till end of file.
        while(!BinaryStdIn.isEmpty()){
            // Reading the input file string
            input = BinaryStdIn.readString();
            // Compress with codes till input length becomes 0.
            while(input.length() > 0){
                // Get the longest matching prefix.
                s = dict.getLongestPrefix(input);
                BinaryStdOut.write(dict.lookup(s), WIDTH);
                t = s.length();
                if(t < input.length() && code < L){
                    dict.insert(input.substring(0, t + 1), code++);
                }
                input = input.substring(t);
            }
        }
        BinaryStdOut.write(R, WIDTH);
        BinaryStdOut.close();
    }

    private static void expand() {
        /*
            Sedgewick & Wayne Algortihms 4th Edition helped me write the below code
         */
        int i;
        String[] st = new String[L];
        for(i = 0; i < R; i++){
            st[i] = "" + (char)i;
        }
        st[i++] = "";
        int codeword;
        while(!BinaryStdIn.isEmpty()){
            codeword = BinaryStdIn.readInt(WIDTH);
            String val = st[codeword];
            while(true)
            {
                BinaryStdOut.write(val);
                codeword = BinaryStdIn.readInt(WIDTH);
                if(codeword == R){
                    break;
                }
                String s = st[codeword];
                if(i == codeword){
                    s = val + val.charAt(0);
                }
                if(i < L){
                    st[i++] = val + s.charAt(0);
                }
                val = s;
            }
            if(codeword == R){
                break;
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
