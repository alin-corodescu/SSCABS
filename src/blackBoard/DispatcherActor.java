package blackBoard;

import akka.actor.UntypedActor;
import blackBoard.Actors.ActorsPool;
import blackBoard.Actors.ControlMessage;
import blackBoard.blackboardObjects.CipherLetter;
import blackBoard.blackboardObjects.Decryption;
import blackBoard.blackboardObjects.TextObject;

import static blackBoard.Actors.ControlMessage.Types.DONE;
import static blackBoard.Actors.ControlMessage.Types.WAITING;


/**
 * Created by alin on 4/6/17.
 * Class used to organize the tasks between the other actors,
 * as well as handle the responses received from them.
 */
public class DispatcherActor extends UntypedActor {
    private enum Phase {BUILDING_CIPHER, DECRYPT, REWORK}

    ;
    private Phase currentPhase;
    private CipherLetter mainCipher;
    private ActorsPool actorsPool;
    private TextObject cipherText;

    public DispatcherActor() {
        mainCipher = new CipherLetter();
        currentPhase = Phase.BUILDING_CIPHER;
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

                    // when we receive a done ControlMessage, proceed to the next state
                    case DONE:
//                  if we were decyphering, move on to the rework phase
                        if (currentPhase == Phase.BUILDING_CIPHER) {
                            currentPhase = Phase.DECRYPT;
                            // move on to decrypting
                            actorsPool.setUpDecryptor(mainCipher.getData());
                            // decipher the whole cipherText
                            // TODO : implement load balancing method for decryptors
                            actorsPool.run(ActorsPool.ServiceType.DECRYPT, getSelf(), cipherText);
                        }
                        if (currentPhase == Phase.DECRYPT) {
                            currentPhase = Phase.REWORK;
                            String decrypted = m.getData();
                            TextObject decryptedText = new TextObject();
                            decryptedText.add(decrypted);
                            Decryption decryption = new Decryption();
                            decryption.encrypted = cipherText;
                            decryption.decrypted = decryptedText;

                            // will trigger the decryption-separating function inside the TextsplttingActor
                            actorsPool.run(ActorsPool.ServiceType.SPLIT, getSelf(), decryption);
                        }
                        if (currentPhase == Phase.REWORK) {
                            // received a DONE message from the decryption splitter, means we are ready
                            // notify the rework actor(s) that we are waiting
                            actorsPool.run(ActorsPool.ServiceType.REWORK, getSelf(), new ControlMessage().setType(WAITING));
                        }
                }
            }
            // phase == DECRYPT
            else if (message instanceof Decryption) {
                Decryption decryption = (Decryption) message;
                if (decryption.toString().contains("_")) {
                    // if there is an unsolved letter, send it to the rework actor
                    actorsPool.run(ActorsPool.ServiceType.REWORK, getSelf(), decryption);
                }
            }
        }

    }
}
