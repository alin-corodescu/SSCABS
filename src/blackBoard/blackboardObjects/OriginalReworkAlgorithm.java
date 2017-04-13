package blackBoard.blackboardObjects;

import java.util.List;
import java.util.Map;

/**
 * Un modified implementation of the rework algorithm
 * Created by alin on 4/13/17.
 */
public class OriginalReworkAlgorithm extends ReworkAlgorithm {
    @Override
    public void addNewCipher(Map<Character, List<Character>> cipher) {

        // overwrite the existing values (that was the logic in the original version)
        for (Map.Entry<Character, List<Character>> entry : cipher.entrySet()) {
            // if the actual mapping has some significance
            if (entry.getValue().size() > 0)
                constructedCipher.put(entry.getKey(),entry.getValue());
        }
    }

}
