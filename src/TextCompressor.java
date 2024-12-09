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
        Below code taken from Sedgewick and Wayne's Algorithms 4th edition.
     */
    private static final int WIDTH = 12;
    private static final int R = 256;
    private static final int EOF = 128;
    private static final int L = 4096;

    private static void compress() {
        String input;
        TST dict = new TST();
        for(int i = 0; i < R; i++){
            dict.insert("" + (char)i, i);
        }
        int code = R+1;
        while(!BinaryStdIn.isEmpty()){
            input = BinaryStdIn.readString();
            while(input.length() > 0){
                String s = dict.getLongestPrefix(input);
                BinaryStdOut.write(dict.lookup(s), WIDTH);
                int t = s.length();
                if(t < input.length() && code < L){
                    dict.insert(input.substring(0, t + 1), code++);
                }
                input = input.substring(t);
            }
        }
        BinaryStdOut.write(EOF, WIDTH);
        BinaryStdOut.close();
    }

    private static void expand() {
        /*
            Below code taken from Sedgewick & Wayne Algortihms 4th Edition
         */
        int i;
        String[] st = new String[L];
        for(i = 0; i < R; i++){
            st[i] = "" + (char)i;
        }
        st[i++] = "";

        int codeword = BinaryStdIn.readInt(WIDTH);
        String val = st[codeword];
        while(true)
        {
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(WIDTH);
            if((codeword == R) || (codeword == EOF)){
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
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
