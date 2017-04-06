package blackBoard.Actors;

import akka.actor.UntypedActor;
import blackBoard.blackboardObjects.TextObject;

import javax.xml.soap.Text;
import java.util.List;
import java.util.Map;

/**
 * Created by alin on 4/6/17.
 * Abstract class representing the base for Knowledge Source actors
 */
public abstract class KnowledgeSourceActor extends UntypedActor {

    protected abstract Map<Character, List<Character>> computeCipher(TextObject text);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof TextObject) {
            // handle only if you can decipher the message
            if (canDecypher( (TextObject) message)) {
                Map<Character, List<Character>> cipher = computeCipher((TextObject) message);
                getSender().tell(cipher, getSelf());
            }
        } else {
            unhandled(message);
        }
    }

    /**
     * checks whether or not the actor can decypher the give text
     * @param text text to be checked
     * @return true - if the text can be deciphered, false otherwise
     */
    protected boolean canDecypher(TextObject text) {
        return true;
    }
}
