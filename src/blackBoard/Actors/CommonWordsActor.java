package blackBoard.Actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import blackBoard.WordServerInterface;
import blackBoard.blackboardObjects.TextObject;

import java.util.List;
import java.util.Map;

import static blackBoard.blackboardObjects.CipherLetter.addLettersToMapping;
import static blackBoard.blackboardObjects.CipherLetter.getBlankMapping;

/**
 * Created by alin on 4/5/17.
 * Class used to create ciphers by communicating with the word server
 */
public class CommonWordsActor extends KnowledgeSourceActor {
    private WordServerInterface serverInterface;

    /**
     * Constructor with a already connected WordServerInterface, to be used when communicating with the server
     */
    public CommonWordsActor(WordServerInterface serverInterface) {
        this.serverInterface = serverInterface;
    }

    /**
     * Function used to build a Props of the actor
     * @param serverInterface - already initialised (connected) WordServerInterface
     * @return Props of this actor
     */
    public static Props props(WordServerInterface serverInterface) {
        return Props.create(CommonWordsActor.class, serverInterface);
    }

    @Override
    public Map<Character, List<Character>> computeCipher(TextObject textObject) {
        Map<Character, List<Character>> cipher = getBlankMapping(); // make an empty cipher map
        String word = textObject.toString();
        String pattern = TextUtils.getWordPattern(word);     // calculate the word code
        String[] words = serverInterface.allPatterns(pattern); // get the array of strings from the word server
        if (words != null) {                       // check that we have data
            for (String candidate : words) {       // add each word to the cipher map
                cipher = addLettersToMapping(cipher, word, candidate.toLowerCase());
            }
                  // add the cipher map to the blackboard
        }
        return cipher;
    }

    @Override
    protected boolean canDecypher(TextObject text) {
        return text.length() >= 3 // This here is >= 3 because there was a mistake in the original version
                && !text.toString().contains(" ");
    }
}
