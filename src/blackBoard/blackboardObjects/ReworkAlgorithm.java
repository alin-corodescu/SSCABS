package blackBoard.blackboardObjects;

import java.util.List;
import java.util.Map;

import static blackBoard.blackboardObjects.CipherLetter.getBlankMapping;

/**
 * Created by alin on 4/13/17.
 * abstract class used to easily swap between different Rework implementation
 */
public abstract class ReworkAlgorithm {

    protected Map<Character,List<Character>> constructedCipher;

    /**
     * Add a new cipher to be processed by the algorithm
    * @param cipher - the cipher to be processed
     */
    public abstract void addNewCipher(Map<Character, List<Character>> cipher);

    /**
     * Method used to query the algorithm for the cipher, can be overriden
     * @return - the cipher computed by the algorithm
     */
    public Map<Character, List<Character>> getCipher() {
        return constructedCipher;
    }

    /**
     * Method called when a new reworking phase begins
     */
    public void newReworkPhase() {
        constructedCipher = getBlankMapping();
    }
}
