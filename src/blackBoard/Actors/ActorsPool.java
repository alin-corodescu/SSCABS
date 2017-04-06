package blackBoard.Actors;

import akka.actor.ActorRef;

import javax.xml.ws.Service;

/**
 * Created by alin on 4/6/17.
 * Serves as an interface between the Knowledge Source actors and the Dispatcher
 * This abstractions allows for easier scalability (by distributing the workload between
 * multiple actors of the same type - where possible)
 */
public class ActorsPool {
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
}
