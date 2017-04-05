package blackBoard.knowledgeSources;

import akka.actor.UntypedActor;
import blackBoard.WordServerInterface;
import blackBoard.blackboardObjects.TextObject;

import java.util.List;
import java.util.Map;

import static blackBoard.blackboardObjects.CipherLetter.addLettersToMapping;
import static blackBoard.blackboardObjects.CipherLetter.getBlankMapping;
import static blackBoard.knowledgeSources.TextUtils.getWordPattern;

/**
 * Created by alin on 4/5/17.
 */
public class CommonWordsActor extends UntypedActor implements Contributor {
    WordServerInterface serverInterface;

    /**
     * constructor which initializes connection with the server
     */
    public CommonWordsActor() {
        serverInterface = new WordServerInterface();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof TextObject) {
            //TODO this function looks the same probably for most KS
            System.out.println("CommonWords received a text object");
            // compute the cipher
            Map<Character, List<Character>> cipher = computeCipher((TextObject) message);
            //respond with the newly generated cipher
            getSender().tell(cipher,getSelf());
        }
    }

    @Override
    public boolean canDecipher(TextObject text) {
        return false;
    }

    public Map<Character, List<Character>> computeCipher(TextObject textObject) {
        Map<Character, List<Character>> cipher = getBlankMapping(); // make an empty cipher map
        String word = textObject.toString();
        String pattern = getWordPattern(word);     // calculate the word code
        String[] words = serverInterface.allPatterns(pattern); // get the array of strings from the word server
        if (words != null) {                       // check that we have data
            for (String candidate : words) {       // add each word to the cipher map
                cipher = addLettersToMapping(cipher, word, candidate.toLowerCase());
            }
                  // add the cipher map to the blackboard
        }
        return cipher;
    }
}
