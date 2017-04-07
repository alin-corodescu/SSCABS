package blackBoard;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import blackBoard.Actors.ActorsPool;
import blackBoard.Actors.ControlMessage;
import blackBoard.blackboardObjects.CipherLetter;
import blackBoard.blackboardObjects.Decryption;
import blackBoard.blackboardObjects.TextObject;
import scala.Char;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static blackBoard.Actors.ControlMessage.Types.DONE;
import static blackBoard.Actors.ControlMessage.Types.WAITING;


/**
 * Created by alin on 4/6/17.
 * Class used to organize the tasks between the other actors,
 * as well as handle the responses received from them.
 */
public class DispatcherActor extends UntypedActor {
    private int waitingFor = 0;
    private boolean unresolvedLetters = false;
    private String plainText;
    private Map<Character, List<Character>> reworkedCipher;
    private enum Phase {
        WORD_SPLITTING,
        BUILDING_CIPHER,
        DECRYPT,
        DECRYPT_SPLITTING,
        REWORK
    }

    private Phase currentPhase;
    private CipherLetter mainCipher;
    private ActorsPool actorsPool;
    private TextObject cipherText;

    public DispatcherActor() {
        mainCipher = new CipherLetter();
        currentPhase = Phase.WORD_SPLITTING;
        // TODO probably pass the actorPool as parameter for the constructor
        actorsPool = new ActorsPool();
        cipherText = new TextObject();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof CipherLetter) {
            switch (currentPhase) {
                case BUILDING_CIPHER:
                    // In case I receive a CipherLetter during decyphering phase
                    // just update the current mainCipher;
                    mainCipher.update(((CipherLetter) message).getData());
                    break;
                case REWORK:
                    // In case I receive a CipherLetter during the Rework phase
                    // postpone the update until all the ciphers are in
                    updateReworkedCipher((CipherLetter) message);
                    break;
            }
        } else {
            // New cipher word received from the text splitter
            if (message instanceof String) {
                TextObject outboundMessage = new TextObject();

                outboundMessage.add((String) message);

                // tell both common words and single letter actors to decipher the given word
                // the check if it can be decyphered is done before the computation, inside the actor
                // so we will recieve a cipher only if the actor was able to handle the word
                actorsPool.run(ActorsPool.ServiceType.COMMON_WORDS, getSelf(), outboundMessage);
                actorsPool.run(ActorsPool.ServiceType.SINGLE_LETTER, getSelf(), outboundMessage);

            } else if (message instanceof ControlMessage) {
                ControlMessage m = (ControlMessage) message;
                handleControlMessage(m);
            }
            // phase == DECRYPT
            else if (message instanceof Decryption) {
                Decryption decryption = (Decryption) message;
                if (decryption.toString().contains("_")) {
                    unresolvedLetters = true;
                    // if there is an unsolved letter, send it to the rework actor
                    actorsPool.run(ActorsPool.ServiceType.REWORK, getSelf(), decryption);
                }
            }
        }

    }

    private void updateReworkedCipher(CipherLetter newCipher) {
        // keep the exact same logic as in the original program
        Map<Character, List<Character>> mapping = newCipher.getData();

        // overwrite the existing values (that was the logic in the original version)
        for (Map.Entry<Character, List<Character>> entry : mapping.entrySet()) {
            reworkedCipher.put(entry.getKey(),entry.getValue());
        }
    }

    private void handleControlMessage(ControlMessage m) {
        switch (m.getType()) {
            case START:
                String data = m.getData();
                // store the cipher text for later use
                cipherText.add(data);
                // Split the ciphertext into separate words
                actorsPool.run(ActorsPool.ServiceType.SPLIT, getSelf(), cipherText);
                // Compute the cipher based on letter frequency
                actorsPool.run(ActorsPool.ServiceType.LETTER_FREQUENCY, getSelf(), cipherText);
                break;

            case DONE:
                // The word splitting is finished, now we need to let the KS'es know we are waiting
                switch (currentPhase) {
                    case WORD_SPLITTING:
                        // Enter the BUILDING_CIPHER state
                        currentPhase = Phase.BUILDING_CIPHER;

                        ControlMessage waiting = new ControlMessage().setType(WAITING);
                        // No problem with the atomicity of the operations because of the message queue style.
                        // Before processing the DONE message, it will modify the waitingFor integer
                        // Count the number of actors we are waiting a response from
                        waitingFor = 1;
                        actorsPool.broadcast(ActorsPool.ServiceType.LETTER_FREQUENCY, getSelf(), waiting);
                        waitingFor += actorsPool.broadcast(ActorsPool.ServiceType.SINGLE_LETTER, getSelf(), waiting);
                        waitingFor += actorsPool.broadcast(ActorsPool.ServiceType.COMMON_WORDS, getSelf(), waiting);
                        break;

                    case BUILDING_CIPHER:
                        waitingFor--;
                        // if all the knowledge sources are done contributing
                        if (waitingFor == 0) {
                            // Enter decryption phase
                            currentPhase = Phase.DECRYPT;
                            // move on to decrypting
                            actorsPool.setUpDecryptor(mainCipher.getData());
                            // decipher the whole cipherText
                            // TODO : implement load balancing method for decryptors
                            actorsPool.run(ActorsPool.ServiceType.DECRYPT, getSelf(), cipherText);
                        }

                        break;
                    case DECRYPT:
                        // Decryption is done
                        currentPhase = Phase.DECRYPT_SPLITTING;

                        // assume for the moment the decryption fully solved the cipher text
                        // this flag will be set to true in case there is a decryption containing unresolved
                        // letters, in the Decryption handling procedure inside this actor
                        unresolvedLetters = false;

                        String decrypted = m.getData();
                        TextObject decryptedText = new TextObject();
                        decryptedText.add(decrypted);
                        Decryption decryption = new Decryption();
                        decryption.encrypted = cipherText;
                        decryption.decrypted = decryptedText;
                        // Store the plain text
                        plainText = decryptedText.toString();
                        // will trigger the decryption-separating function inside the TextsplttingActor
                        actorsPool.run(ActorsPool.ServiceType.SPLIT, getSelf(), decryption);
                        break;

                    case DECRYPT_SPLITTING:
                        // received a DONE message from the decryption splitter, means we are ready
                        // notify the rework actor(s) that we are waiting
                        // they will respond with a done
                        if (unresolvedLetters) {
                            // Enter REWORKING PHASE
                            currentPhase = Phase.REWORK;
                            // reset the reworked cipher
                            reworkedCipher = new HashMap<>();
                            // Tell the reworkers we are waiting for their responses
                            waitingFor = actorsPool.broadcast(ActorsPool.ServiceType.REWORK, getSelf(), new ControlMessage().setType(WAITING));
                        }
                        else{
                            // return the plain  text somehow
                        }
                        break;
                    case REWORK:
                        // one more reworker finished
                        waitingFor--;
                        // if all the reworkers finished
                        if (waitingFor == 0) {
                            // update the mainCipher with the reworked one
                            mainCipher.update(reworkedCipher);

                            // this block will trigger a new decryption sequence
                            currentPhase = Phase.BUILDING_CIPHER;
                            waitingFor = 1;
                            getSelf().tell(new ControlMessage().setType(DONE),ActorRef.noSender());
                        }
                        break;
                }

        }
    }
}
