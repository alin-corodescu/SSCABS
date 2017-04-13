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

    public abstract void addNewCipher(Map<Character, List<Character>> cipher);

    public Map<Character, List<Character>> getCipher() {
        return constructedCipher;
    }

    public void newReworkPhase() {
        constructedCipher = getBlankMapping();
    }
}
