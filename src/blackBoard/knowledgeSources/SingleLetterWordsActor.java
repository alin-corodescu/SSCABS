package blackBoard.knowledgeSources;

import akka.actor.UntypedActor;
import blackBoard.blackboardObjects.TextObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static blackBoard.blackboardObjects.CipherLetter.getBlankMapping;

/**
 * Created by alin on 4/5/17.
 */
public class SingleLetterWordsActor extends UntypedActor {
    private List<Character> words;
    public SingleLetterWordsActor(){
        Character [] wordArray = {'a', 'i'};
        words = new ArrayList<Character>(Arrays.asList(wordArray));
    }
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof TextObject) {
            Map<Character, List<Character>> cipher = computeCipher((TextObject) message);
            getSender().tell(cipher,getSelf());
        }
        else {
            unhandled(message);
        }
    }

    public Map<Character, List<Character>> computeCipher(TextObject text) {
        Character word = text.toString().charAt(0);
        Map<Character, List<Character>> cipher = getBlankMapping();
        cipher.put(word, words);
        return cipher;

    }
}
