package blackBoard.Actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import blackBoard.blackboardObjects.TextObject;
import scala.Char;

import java.util.*;

/**
 * Created by alin on 4/4/17.
 * Class containing methods which help working with texts
 */
public abstract class TextUtils {
    static final String alphabet = "abcdefghijklmnopqrstuvwxyz";

    /**
     * Splits the given TextObject into tokens (separated by spaces)
     * @param text TextObject to split
     * @return List of words
     */
    static List<String> splitIntoWords(TextObject text) {
        List<String> wordList = new ArrayList<>();
        // unwraps the text to decipher
        String cipherText = text.toString();

        cipherText = removeNonLetters(cipherText);

        wordList = Arrays.asList(cipherText.split(" "));
        return wordList;
    }

    /**
     * Removes the non-letters characters from the text.
     * ignores the spaces
     * @param text text to remove symbols from
     * @return processed String
     */
    static String removeNonLetters(String text) {
        String nonletter;
        text = text.toLowerCase();
        //remove the letters from the text
        nonletter = removeMatches(text, alphabet);
        //clean up
        nonletter = removeDupes(nonletter);
        // let only letters in the ciphertext
        text = removeMatches(text, nonletter.replace(" ", ""));
        return text;
    }


    /**
     * See definition of word code in the RFC
     * @param word - string to turn into word code
     * @return the word code
     */
    static String getWordPattern(String word) {
        int nextNum = 0;
        word = word.toLowerCase();
        Map<Character, String> letterNums = new HashMap<Character, String>();
        List<String> wordPattern = new ArrayList<String>();
        for (int i = 0; i < word.length(); i++) {
            Character letter = word.charAt(i);
            if (!letterNums.containsKey(letter)) {
                letterNums.put(letter, ""+nextNum);
                nextNum++;
            }
            wordPattern.add(letterNums.get(letter));
        }
        return String.join(".", wordPattern);
    }

    /**
     * Removes characters of the second string from the first string
     * @param inStr string to remove characters from
     * @param remvStr character to remove
     * @return newly created string
     */
    // removes characters from inStr that occur in remStr
    static String removeMatches(String inStr, String remvStr) {
        String newStr = "";
        for (int i = 0; i < inStr.length(); i++) {
            String ch = "";
            try {
                ch = inStr.substring(i,i+1);
            }  catch (IndexOutOfBoundsException e) {

            }
            if (!remvStr.contains(ch)) {
                newStr += ch;
            }
        }
        return newStr;
    }

    /**
     * Removes duplicate characters from a string
     */
    private static String removeDupes(String myStr) {
        String newStr = "";
        for (int i = 0; i < myStr.length(); i++) {
            String ch = myStr.substring(i,i+1);
            if (!newStr.contains(ch)) {
                newStr += ch;
            }
        }
        return newStr;
    }

    /**
     * Adds spaces after each non-letter symbol to facilitate word splitting
     * e.g : Without this, abc.def would be considered a single word "abcdef"
     * @param text text to modify
     * @return the modifed text
     */
    public static String addSpacesAfterSymbols(String text) {
        StringBuilder builder = new StringBuilder();
        text = text.toLowerCase();
        for (int i = 0; i < text.length(); i++)
        {
            Character c = text.charAt(i);
            builder.append(c);
            if (! (c >= 'a' && c <= 'z')
                    && !("' ".contains(Character.toString(c)))
                // add or remove symbols in the hardcoded string
                    ) {
                builder.append(' ');
            }
        }
        return builder.toString();
    }
}
