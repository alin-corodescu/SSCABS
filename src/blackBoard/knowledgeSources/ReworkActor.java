package blackBoard.knowledgeSources;

import akka.actor.UntypedActor;
import blackBoard.WordServerInterface;
import blackBoard.blackboardObjects.Decryption;
import scala.Char;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static blackBoard.blackboardObjects.CipherLetter.getBlankMapping;
import static blackBoard.knowledgeSources.TextUtils.getWordPattern;

/**
 * Created by alin on 4/5/17.
 */
public class ReworkActor extends UntypedActor {
    private WordServerInterface serverInterface;

    public ReworkActor() {
        serverInterface = new WordServerInterface();
    }
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Decryption) {
            // received a decryption to rework
            Map<Character,List<Character>> cipher = computeCipher((Decryption) message);
            getSender().tell(cipher,getSelf());
        }
    }

    public String bestMatch(String pattern, String[] words) {
        String bestWord = "";
        int count;
        int similarity = 0;
        for (String word : words) {
            word = word.toLowerCase();
            count = 0;
            for (int c = 0; c < pattern.length(); c++) {
                if (word.charAt(c) == pattern.charAt(c)) {
                    count++;
                } else if (pattern.charAt(c) == '_') {
                    continue;
                } else {
                    count = 0;
                    break;
                }
            }
            if (count > similarity) {
                bestWord = word;
                similarity = count;
            }
        }
        return bestWord;
    }

    /**
     * this one uses the plain text, not the encrypted one
     */
    public Map<Character, List<Character>> computeCipher(Decryption decryption) {
        String plaintext = decryption.decrypted.toString();
        Map<Character, List<Character>> cipher = getBlankMapping();

        String cipherword = decryption.encrypted.toString();
        // ask the server about some suggestions
        String pattern = getWordPattern(cipherword);
        // get the response
        String[] words = serverInterface.allPatterns(pattern);
        // find the best match
        String candidate = bestMatch(plaintext, words);
        if (candidate != null && !candidate.isEmpty()) {
            int pos = plaintext.indexOf("_");
            List<Character> temp = new ArrayList<Character>();
            temp.add(candidate.charAt(pos));
            // add the new match to cipher mapping
            cipher.put(cipherword.charAt(pos), temp);
        }
        return cipher;
    }
}
