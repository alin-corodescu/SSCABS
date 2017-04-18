package blackBoard.Actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import blackBoard.Actors.KnowledgeSourceActor;
import blackBoard.blackboardObjects.TextObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static blackBoard.blackboardObjects.CipherLetter.getBlankMapping;

/**
 * Created by alin on 4/5/17.
 * Actor providing ciphers for single-letter words
 */
public class SingleLetterWordsActor extends KnowledgeSourceActor {
    private List<Character> words;

    public SingleLetterWordsActor(){
        Character [] wordArray = {'a', 'i'};
        words = new ArrayList<Character>(Arrays.asList(wordArray));
    }

    public static Props props() {
        return Props.create(SingleLetterWordsActor.class);
    }

    @Override
    public Map<Character, List<Character>> computeCipher(TextObject text) {
        Character word = text.toString().charAt(0);
        Map<Character, List<Character>> cipher = getBlankMapping();
        cipher.put(word, words);
        return cipher;
    }

    @Override
    protected boolean canDecypher(TextObject text) {
        String string = text.toString();
        return string.length() == 1;
    }
}
