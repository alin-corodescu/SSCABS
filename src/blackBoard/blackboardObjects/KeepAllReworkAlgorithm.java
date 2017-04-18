package blackBoard.blackboardObjects;

import com.sun.org.apache.regexp.internal.RE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by alin on 4/13/17.
 * Rework algorithm which will keep all the distinct choices given
 */
public class KeepAllReworkAlgorithm extends ReworkAlgorithm {
    @Override
    public void addNewCipher(Map<Character, List<Character>> cipher) {
        for (Map.Entry<Character, List<Character>> entry : cipher.entrySet()) {
            Character key = entry.getKey();
            List<Character> candidates = entry.getValue();

            if (constructedCipher.containsKey(key)) {
                List<Character> currentCandidates = constructedCipher.get(key);
                constructedCipher.put(key,union(currentCandidates,candidates));
            }
        }
    }

    /**
     * Computes the distinct reunion of the 2 lists passed as parameters
     */
    private List<Character> union(List<Character> a, List<Character> b) {
        List<Character> result = new ArrayList<>();
        result.addAll(a);
        for (Character c : b) {
            if (!result.contains(c))
                result.add(c);
        }
        return result;
    }
}
