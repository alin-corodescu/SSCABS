package blackBoard.Actors;

import akka.actor.Actor;
import akka.actor.ActorRef;

import java.util.List;
import java.util.Map;

/**
 * Created by alin on 4/6/17.
 * Serves as an interface between the Knowledge Source actors and the Dispatcher
 * This abstractions allows for easier scalability (by distributing the workload between
 * multiple actors of the same type - where possible)
 */
public class ActorsPool {
    public boolean isDecryptorSet() {
        return isDecryptorSet;
    }

    public void setDecryptorSet(boolean decryptorSet) {
        this.isDecryptorSet = decryptorSet;
    }

    private boolean isDecryptorSet = false;

    public enum ServiceType {
        LETTER_FREQUENCY,
        SINGLE_LETTER,
        COMMON_WORDS,
        REWORK,
        DECRYPT,
        SPLIT;
    }

    /**
     * Sends an appropriate actor a message, on behalf of the sender
     * @param service type of service, used to determine the actor to use
     * @param sender on whose behalf to send the message
     * @param message content of the message
     */
    public void run(ServiceType service, ActorRef sender, Object message) {
        switch (service) {
            case SPLIT:
            case LETTER_FREQUENCY:
            case COMMON_WORDS:
            case SINGLE_LETTER:
            case DECRYPT:
            case REWORK:
        }
    }

    /**
     * Broadcasts a message to all the actors which can provide the type of service specified.
     * Used when broadcasting ControlMessages (WAITING) especially.
     * @see ControlMessage.Types
     * @param service type of service
     * @param sender on whose behalf to send the broadcast
     * @param message the message to be broadcasted
     * @return the number of actors to which the message was broadcasted
     * - the return could be replaced with a list of ActorRef for more flexibility
     */
    public int broadcast(ServiceType service, ActorRef sender, Object message) {
       int numberOfActors = 0;

       return numberOfActors;
    }

    public void setUpDecryptor(Map<Character, List<Character>> cipherKey) {
        // create as many Decryptors as you need with the given cipher
        isDecryptorSet = true;
    }
}
