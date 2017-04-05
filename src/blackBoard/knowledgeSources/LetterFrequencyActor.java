package blackBoard.knowledgeSources;

import akka.actor.UntypedActor;
import blackBoard.blackboardObjects.TextObject;

import java.util.*;

import static blackBoard.blackboardObjects.CipherLetter.getBlankMapping;
import static blackBoard.knowledgeSources.TextUtils.removeMatches;
import static blackBoard.knowledgeSources.TextUtils.removeNonLetters;

/**
 * Created by alin on 4/5/17.
 * Actor used to create a cipher based on the letter frequency
 * in the english language, it works best if the whole ciphered text is passed
 * as a message
 */
public class LetterFrequencyActor extends UntypedActor implements Contributor {
    private String frequentCharacters = "etaoinshrdlu";

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof TextObject) {
            // compute the cipher candidate
            Map<Character, List<Character>> cipher = computeCipher((TextObject) message);
            // return the cipher candidate to the sender in order to
            getSender().tell(cipher,getSelf());
        }
        else {
            unhandled(message);
        }
    }

    @Override
    public boolean canDecipher(TextObject text) {
        // LetterFrequency KS doesn't have any restrictions
       return true;
    }

    /**
     * counts the letter frequency in the text passed as parameter
     * @param text text for which to count letter frequency;
     * @return a map containing the frequency of every character in the text
     */
    public Map<Character, Float> letterFrequency(String text) {
        Map<Character, Float> letterCount = new HashMap<Character, Float>();
        text = removeMatches(text, " ");  // remove spaces for the calculation
        float total = text.length();
        for (int i = 0; i < text.length(); i++) {
            Character ch = text.charAt(i);
            letterCount.put(ch, letterCount.getOrDefault(ch, (float) 0) + 1);
        }
        for (Character ch : letterCount.keySet()) {
            letterCount.put(ch, letterCount.getOrDefault(ch, (float) 0) / total);
        }
        return letterCount;
    }

    public Map<Character, List<Character>> computeCipher(TextObject cipherText) {
        // unwraps the text to decipher
        String cipherString = cipherText.toString();

        cipherString = removeNonLetters(cipherString);

        // compute the letter frequency in the cipherString
        Map<Character, Float> letterFrequency = letterFrequency(cipherString);
        List<Map.Entry<Character, Float>> lfEntryList = new ArrayList<Map.Entry<Character, Float>>();
        // create the entry set of the frequency (as a list)
        lfEntryList.addAll(letterFrequency.entrySet());

        // sort them descending - probably - too lazy to think
        lfEntryList.sort(new Comparator<Map.Entry<Character, Float>>() {
            public int compare(Map.Entry<Character, Float> lhs, Map.Entry<Character, Float> rhs) {
                return lhs.getValue() > rhs.getValue() ? -1 : (lhs.getValue() < rhs.getValue() ? 1 : 0);
            }
        });

        // create the cipher with which to update
        Map<Character, List<Character>> cipher = getBlankMapping();
        // first 6 characters?
        for (int p = 0; p < 5; p++) {
            if (Math.abs(lfEntryList.get(p).getValue() - lfEntryList.get(p+1).getValue()) < 0.000001)
                break;
            List<Character> arr =  new ArrayList<Character>();
            // add the p most common letter in english ?
            arr.add(frequentCharacters.charAt(p));
            // put the mapping in the cipher
            cipher.put(lfEntryList.get(p).getKey(), arr);
        }

        return cipher;
    }
}
