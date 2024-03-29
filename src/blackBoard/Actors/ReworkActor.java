package blackBoard.Actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import blackBoard.WordServerInterface;
import blackBoard.blackboardObjects.CipherLetter;
import blackBoard.blackboardObjects.Decryption;
import blackBoard.blackboardObjects.TextObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static blackBoard.Actors.ControlMessage.Types.DONE;
import static blackBoard.blackboardObjects.CipherLetter.getBlankMapping;

/**
 * Created by alin on 4/5/17.
 * Actor used to provide cipher candidates for letters which have not been solved by the other
 * knowledge sources. It should be used only when the DispatcherActor is in the REWORK phase
 * @see blackBoard.DispatcherActor
 */
public class ReworkActor extends KnowledgeSourceActor {
    private WordServerInterface serverInterface;

    /**
     * Constructor which takes a WordServerInterface as parameter, to be used when communicating with the server
     * The WordServerInterface needs to have the connection to the server already set up
     * @param serverInterface - WordServerInterface to be used
     */
    public ReworkActor(WordServerInterface serverInterface) {
        this.serverInterface = serverInterface;
    }

    /**
     * Creates a Props of the ReworkActor which uses the WordServerInterface passed as parameter
     * @param serverInterface - WordServerInterface to be used by this actor
     * @return Props of ReworkActor
     */
    public static Props props(WordServerInterface serverInterface) {
        return Props.create(ReworkActor.class, serverInterface);
    }

    /**
     * Cannot compute a cipher based on a TextObject, it needs to be a Decryption
     * @see Decryption
     * @see TextObject
     */
    @Override
    protected Map<Character, List<Character>> computeCipher(TextObject text) {
        // This one cannot compute a cipher based on a TextObject
        return null;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Decryption) {
            // received a decryption to rework
            if (((Decryption) message).decrypted.length() >= 3) {
                Map<Character, List<Character>> cipher = computeCipher((Decryption) message);
                CipherLetter cipherLetter = new CipherLetter();
                cipherLetter.update(cipher);
                getSender().tell(cipherLetter, getSelf());
            }
        } else if (message instanceof ControlMessage) {
            ControlMessage controlMessage = (ControlMessage) message;
            // if any other actor is waiting on this one
            if (controlMessage.getType() == ControlMessage.Types.WAITING)
            {
                // send back a message that we are done processing
                // otherwise the Waiting message wouldn't have been processed
                getSender().tell(new ControlMessage().setType(DONE),getSelf());
            }
        }
        else {unhandled(message);}
    }

    /**
     * Computes the best match of the pattern with the words array,
     * by comparing the number of similar letters
     * @param pattern word for which to find the best match in the list
     * @param words array of words from which to select the best match
     * @return the best matching word from the array
     */
    private String bestMatch(String pattern, String[] words) {
        String bestWord = "";
        int count;
        int similarity = 0;
        for (String word : words) {
            word = word.toLowerCase();
            count = 0;
            for (int c = 0; c < pattern.length(); c++) {
                if (word.charAt(c) == pattern.charAt(c)) {
                    count++;
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
     *
     * @param decryption
     * @return
     */
    private Map<Character, List<Character>> computeCipher(Decryption decryption) {
        String plaintext = decryption.decrypted.toString();
        Map<Character, List<Character>> cipher = getBlankMapping();

        String cipherword = decryption.encrypted.toString();
        // ask the server about some suggestions
        String pattern = TextUtils.getWordPattern(cipherword);
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
