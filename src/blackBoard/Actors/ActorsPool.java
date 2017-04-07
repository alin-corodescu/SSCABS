package blackBoard.Actors;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import javax.xml.ws.Service;
import java.util.*;

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

    private ActorSystem actorSystem;

    /**
     * Mapping between services and the actors which offer them
     * using a List instead of a single ActorRef enables us to distribute the
     * workload between the actors
     */
    private Map<ServiceType, List<ActorRef>> actors;


    public ActorsPool(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
        actors = new HashMap<>();
        createActors();
    }

    private void createActors() {
        List<ActorRef> currentActors;

        currentActors = new ArrayList<>();
        currentActors.add(actorSystem.actorOf(LetterFrequencyActor.props()));
        actors.put(ServiceType.LETTER_FREQUENCY, currentActors);

        currentActors = new ArrayList<>();
        currentActors.add(actorSystem.actorOf(SingleLetterWordsActor.props()));
        actors.put(ServiceType.SINGLE_LETTER, currentActors);

        currentActors = new ArrayList<>();
        currentActors.add(actorSystem.actorOf(CommonWordsActor.props()));
        actors.put(ServiceType.COMMON_WORDS, currentActors);

        currentActors = new ArrayList<>();
        currentActors.add(actorSystem.actorOf(ReworkActor.props()));
        actors.put(ServiceType.REWORK, currentActors);

        currentActors = new ArrayList<>();
        currentActors.add(actorSystem.actorOf(TextSplittingActor.props()));
        actors.put(ServiceType.SPLIT, currentActors);


        // Decrypt actor creation is postponed until the cipher is received

    }

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
        ActorRef destination;

        // just run it in the first actor
        // simple method
        destination = actors.get(service).get(0);

        destination.tell(message,sender);
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
       List<ActorRef> destinationActors = actors.get(service);
       numberOfActors = destinationActors.size();
       for (ActorRef actor : destinationActors) {
           actor.tell(message, sender);
       }
       return numberOfActors;
    }

    public void setUpDecryptor(Map<Character, List<Character>> cipherKey) {
        // create as many Decryptors as you need with the given cipher
        isDecryptorSet = true;

        List<ActorRef> decryptors;

        decryptors = new ArrayList<>();
        decryptors.add(actorSystem.actorOf(DecryptActor.props(cipherKey)));
        actors.put(ServiceType.DECRYPT, decryptors);

    }
}
