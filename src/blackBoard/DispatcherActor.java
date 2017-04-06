package blackBoard;

import akka.actor.UntypedActor;
import blackBoard.Actors.ControlMessage;
import blackBoard.Actors.ActorsPool;
import blackBoard.blackboardObjects.CipherLetter;
import blackBoard.blackboardObjects.TextObject;


/**
 * Created by alin on 4/6/17.
 * Class used to organize the tasks between the other actors,
 * as well as handle the responses received from them.
 */
public class DispatcherActor extends UntypedActor {
    private enum Phase{DECYPHER, REWORK};
    private Phase currentPhase;
    private CipherLetter mainCipher;
    private ActorsPool actorsPool;

    public DispatcherActor() {
        mainCipher = new CipherLetter();
        currentPhase = Phase.DECYPHER;
        actorsPool = new ActorsPool();
    }
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof CipherLetter) {
            switch (currentPhase)
            {
                case DECYPHER:
                    // In case I receive a CipherLetter during decyphering phase
                    // just update the current mainCipher;
                    mainCipher.update(((CipherLetter) message).getData());
                    break;
                case REWORK:
                    // In case I receive a CipherLetter during the Rework phase
                    // postpone the update until all the ciphers are in

                    break;
            }
        } else {
            // New cipher word received from the text splitter
            if (message instanceof String)
            {
                // pass it on to one of the Knowledge Sources
            }
            else if (message instanceof ControlMessage)
            {
                ControlMessage m = (ControlMessage) message;
                switch (m.getType()) {
                    case START:
                    String data = m.getData();
                    TextObject textObject = new TextObject();
                    textObject.add(data);
                    // Split the ciphertext into separate words
                    actorsPool.run(ActorsPool.ServiceType.SPLIT, getSelf(), textObject);
                    // Compute the cipher based on letter frequency
                    actorsPool.run(ActorsPool.ServiceType.LETTER_FREQUENCY, getSelf(), textObject);
            }
            }
        }

    }
}
