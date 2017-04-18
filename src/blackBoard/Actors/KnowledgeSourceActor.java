package blackBoard.Actors;

import akka.actor.UntypedActor;
import blackBoard.blackboardObjects.CipherLetter;
import blackBoard.blackboardObjects.TextObject;

import javax.xml.soap.Text;
import java.util.List;
import java.util.Map;

import static blackBoard.Actors.ControlMessage.Types.DONE;

/**
 * Created by alin on 4/6/17.
 * Abstract class representing the base for Knowledge Source actors
 */
public abstract class KnowledgeSourceActor extends UntypedActor {

    /**
     * Computes with a new candidate cipher based on the TextObject passed as parameter
     * @param text - TextObject to base the cipher creation on
     * @return - newly created cipher
     */
    protected abstract Map<Character, List<Character>> computeCipher(TextObject text);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof TextObject) {
            // handle only if you can decipher the message
            if (canDecypher( (TextObject) message)) {
                Map<Character, List<Character>> cipher = computeCipher((TextObject) message);
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
        else {
            unhandled(message);
        }
    }

    /**
     * Checks whether or not the actor can decypher the given text
     * @param text text to be checked
     * @return true - if the text can be deciphered, false otherwise
     */
    protected boolean canDecypher(TextObject text) {
        return true;
    }
}
