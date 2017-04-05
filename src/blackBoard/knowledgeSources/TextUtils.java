package blackBoard.knowledgeSources;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import blackBoard.blackboardObjects.TextObject;

import java.util.*;

/**
 * Created by alin on 4/4/17.
 * Actor class used to split text into words
 * could be renamed
 */
public class TextUtils extends UntypedActor {
    protected static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof TextObject) {
            System.out.println("Text splitter received some text");
            List<String> words = splitIntoWords((TextObject) message);
            for (String word : words) {
                sendWord(word,getSender());
            }
        }
        else {
            unhandled(message);
        }
    }

    /**
     * Splits the given TextObject into tokens (separated by spaces)
     * @param text TextObject to split
     * @return List of words
     */
    public static List<String> splitIntoWords(TextObject text) {
        List<String> wordList = new ArrayList<>();

        String nonletter;
        // unwraps the text to decipher
        String cipherText = text.toString();

        cipherText = removeNonLetters(cipherText);

        wordList = Arrays.asList(cipherText.split(" "));
        return wordList;
    }

    /**
     * removes the non-letters characters from the text
     * ignores the spaces
     * @param text text to remove symbols from
     * @return processed String
     */
    public static String removeNonLetters(String text) {
        String nonletter;
        text = text.toLowerCase();
        //remove the letters from the text
        nonletter = removeMatches(text, alphabet);
        //clean up
        nonletter = removeDupes(nonletter);
        // let only letters in the ciphertext
        text = removeMatches(text, nonletter.trim());
        return text;
    }

    /**
     * method used to send words extracted from the text to the listener
     * @param word word to be sent to the listener
     * @param destination ActorRef of the listener
     */
    private void sendWord(String word, ActorRef destination) {
        // do the necessary wrapping here
        destination.tell(word,getSelf());
    }


    /**
     * See definition of word code in the RFC
     * @param word - string to turn into word code
     * @return the word code
     */
    public static String getWordPattern(String word) {
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
     * removes characters of the second string from the first string
     * @param inStr string to remove characters from
     * @param remvStr character to remove
     * @return newly created string
     */
    // removes characters from inStr that occur in remStr
    public static String removeMatches(String inStr,String remvStr) {
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

    // removes duplicate characters from a string
    public static String removeDupes(String myStr) {
        String newStr = "";
        for (int i = 0; i < myStr.length(); i++) {
            String ch = myStr.substring(i,i+1);
            if (!newStr.contains(ch)) {
                newStr += ch;
            }
        }
        return newStr;
    }
}
