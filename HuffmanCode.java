// HuffmanCode can compress text, creating huffman codes which are binary sequences
// corresponding to each unique character in the text. HuffmanCode can also be used
// to translate huffman codes into the original text.

import java.io.*;
import java.util.*;

public class HuffmanCode {
    
    private HuffmanNode overallRoot;
    
    // post: Initializes a HuffmanCode with the given frequencies of each character,
    //       creating a huffman code used to compress the text.
    public HuffmanCode(int[] frequencies) {
        Queue<HuffmanNode> frequencyQ = new PriorityQueue<>();
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                frequencyQ.add(new HuffmanNode(frequencies[i], i));
            }
        }
        while (frequencyQ.size() > 1) {
            HuffmanNode first = frequencyQ.remove();
            HuffmanNode second = frequencyQ.remove();
            HuffmanNode sum = new HuffmanNode(first.frequency + second.frequency, -1, first, second);
            frequencyQ.add(sum);
        }
        overallRoot = frequencyQ.remove();
    }
    
    // pre: Assumes the input is in standard format, which consists of line pairs.
    //      The first line in each pair is the ASCII value of the character,
    //      and the second line respresents the huffman code for that specific character.
    // post: Initializes a HuffmanCode taking the input from a code that was already made,
    //       using the given input from a code file.
    public HuffmanCode(Scanner input) {
        while (input.hasNextLine()) {
            int asciiValue = Integer.parseInt(input.nextLine());
            String code = input.nextLine();
            overallRoot = HuffmanCodeHelper(overallRoot, asciiValue, code);
        }
    }
    
    // Takes given asciiValue and given code to form to recreate the huffman code.
    // Used to translate an already compressed file.
    private HuffmanNode HuffmanCodeHelper(HuffmanNode root, int asciiValue, String code) {
        if (root == null) {
            root = new HuffmanNode(0, -1);
        }
        if (code.length() == 1) {
            if (code.equals("0")) {
                root.left = new HuffmanNode(0, asciiValue);
            } else {
                root.right = new HuffmanNode(0, asciiValue);
            }
        } else {
            if (code.charAt(0) == '0') {
                root.left = HuffmanCodeHelper(root.left, asciiValue, code.substring(1));
            } else {
                root.right = HuffmanCodeHelper(root.right, asciiValue, code.substring(1));
            }
        }
        return root;
    }
    
    // post: Stores the current huffman codes to given output in standard format.
    public void save(PrintStream output) {
        save(output, overallRoot, "");
    }
    
    // Saves huffman codes to given output
    private void save(PrintStream output, HuffmanNode root, String code) {
        if (root.left == null && root.right == null) {
            output.println(root.letter);
            output.println(code);
        } else {
            save(output, root.left, code + "0");
            save(output, root.right, code + "1");
        }
    }
    
    // pre: Assumes that the bits from input are legal for this huffman code
    // post: Reads the bits individually from the input, and writes to the output the
    //       corresponding characters
    public void translate(BitInputStream input, PrintStream output) {
        HuffmanNode current = overallRoot;
        while (input.hasNextBit()) {
            int bit = input.nextBit();
            if (bit == 0) {
                current = current.left;
            } else {
                current = current.right;
            }
            if (current.left == null && current.right == null) {
                output.write(current.letter);
                current = overallRoot;
            }
        }
    }
    
    // HuffmanNode forms the structure of building the huffman code used to compress data
    private class HuffmanNode implements Comparable<HuffmanNode> {
        public int frequency;
        public int letter;
        public HuffmanNode left;
        public HuffmanNode right;
        
        // Initializes a HuffmanNode with the given frequency and letter value
        public HuffmanNode(int frequency, int letter) {
            this(frequency, letter, null, null);
        }
        
        // Initializes a HuffmanNode with the given frequenct and letter value,
        // and connects the given left and right HuffmanNodes to it
        public HuffmanNode(int frequency, int letter, HuffmanNode left, HuffmanNode right) {
            this.frequency = frequency;
            this.letter = letter;
            this.left = left;
            this.right = right;
        }
        
        // returns the difference of the frequencies of the current HuffmanNode
        // and the given other HuffmanNode
        public int compareTo(HuffmanNode other) {
            return this.frequency - other.frequency;
        }
    }
    
}