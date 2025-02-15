package org.example;

/*
Name: Lucas Tan
Due Date: 2/19/25
Assignment: Caesar Cipher Decoder
Description:
A program that can decode Caesar ciphers using
a brute force attack and a dictionary.
 */

// === IMPORTS ===
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileReader;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {

        // === LOCAL VARIABLES ===

        // ArrayList used to store words read from the file
        ArrayList<String> ciphertexts = new ArrayList<>();
        // ArrayList used to store split apart ciphertexts
        ArrayList<char[]> splitTexts = new ArrayList<>();
        // Storing words from the dictionary file
        ArrayList<String> dictionary = new ArrayList<>();
        // String[] used to store shifted text
        String shiftText = null;
        // int that stores correct shift number
        int shift = 0;
        // Counts the number of words in the shifted text contained in the dictionary
        int count;
        // Returns true if over 75% of the words in the ciphertext are in the dictionary
        boolean flag = false;


        // === PRE-LOADING DICTIONARY ===

        // Reading in and storing the dictionary
        FileReader dr = new FileReader("src/main/resources/en-US.dic");
        Scanner ds = new Scanner (dr);
        while (ds.hasNextLine()) {
            dictionary.add(ds.nextLine().trim().toLowerCase());
        }
        dr.close();


        // === FILE READING ===

        System.out.println("""
        =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        Welcome to the Caesar Cipher decoder!
        =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=""");

        // Prompting user to enter filename
        Scanner in = new Scanner (System.in);
        System.out.print("Please enter the name of the file you want to decode: ");
        String filename = in.nextLine().trim();
        // This is the file the program will write to
        // (Note: I used ChatGPT to get this regex to change the filename)
        String outputFilename = filename.replaceFirst("(\\.\\w+)?$", "-solution.txt");

        try {
            FileReader fr = new FileReader(filename);
            Scanner fs = new Scanner(fr);

            // Reading in ciphertexts
            while (fs.hasNextLine()) {
                ciphertexts.add(fs.nextLine().trim().toLowerCase());
            }
            fr.close();

        } catch (Exception e) { // Catches any errors from reading the file
            System.out.println(filename + " could not be read, closing program");
            System.exit(1);
        }


        // === BRUTE FORCING ===

        // Splitting ciphertexts into character arrays
        for (String text : ciphertexts) {
            splitTexts.add(text.toCharArray());
        }

        // Preparing to write to file
        try {
            FileWriter fw = new FileWriter(outputFilename);

            // Brute forcing all possible ciphers and testing against the dictionary
            for (char[] text : splitTexts) {
                // Testing once per possible letter
                for (int i = 1; i <= 26; i++) {
                    // Shifting letters
                    for (int j = 0; j < text.length; j++) {
                        // If c = 'z' go to 'a', else shift one to the right
                        if (text[j] != ' ') {
                            if (text[j] == 'z') {
                                text[j] = 'a';
                            } else {
                                text[j] += 1;
                            }
                        }
                    }
                    // Testing to see if words are in the dictionary
                    shiftText = new String(text);
                    count = 0;
                    flag = false;
                    for (String word : shiftText.split(" ")) {
                        if (dictionary.contains(word)) {
                            count++;
                        }
                    }
                    // If over 75% of the words in the shifted text are in the
                    // dictionary, saves the shift and breaks.
                    if (count > ((shiftText.split(" ").length) / 4) * 3) {
                        if (i == 26) {
                            shift = 0;
                        } else {
                            shift = i;
                        }
                        flag = true;
                        break;
                    }
                }

                // === FILE WRITING ===

                // If a shifted text where over 75% of the words were in
                // the dictionary was found, shows that text and the shift amount,
                // then writes the results to a new file.
                // If not, displays an error message.
                if (flag) {
                    System.out.println(shift + ";" + shiftText);
                    fw.write(shift + ";" + shiftText + "\n");
                } else {
                    System.out.println("No decoded text could be found.");
                    fw.write("No decoded text could be found.\n");
                }



            }

            fw.close();

        } catch (Exception e) { // Catches errors with writing to the file
            System.out.println(outputFilename + " could not be written to, closing program");
            System.exit(1);
        }
    }
}