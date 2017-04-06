// ----------------------------------------------------------------------------------------
//  CipherLetter.java 
//     Maintains the cipher map a hash table used to map cipher text letters to a list 
//     candidate plain text letters.  Each candidate list is refined throughout the process
//     by intersection (i.e. corroboration).  When there is one letter in the list it is 
//     likely to be the plain text mapping corresponding to the cipher letter.
// ----------------------------------------------------------------------------------------
package blackBoard.blackboardObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class used to represnt the key of the cipher
 * in the form of Map<Character, List<Character>>
 */
public class CipherLetter extends BlackboardObject {
	private Map<Character, List<Character>> data = getBlankMapping();
	private String alphabet = "abcdefghijklmnopqrstuvwxyz";
	
	// generate a new empty cipher map
	public static Map<Character, List<Character>> getBlankMapping(){
		Map<Character, List<Character>> data = new HashMap<Character, List<Character>>();
		for (int i = 0; i < 26; i++) {
			data.put(new Character((char)('a'+i)), new ArrayList<Character>());
		}
		return data;		
	}

	public Map<Character, List<Character>> getData(){
		return data;
	}

	// use the cipherword letters to map the candidate letters in the letterMapping   
	public static Map<Character, List<Character>> addLettersToMapping(Map<Character, List<Character>> letterMapping, String cipherword, String candidate) {
		Map<Character, List<Character>> lm = new HashMap<Character, List<Character>>();
		for (Map.Entry<Character, List<Character>> e : letterMapping.entrySet()) {
			if (!lm.containsKey(e.getKey()))
				lm.put(e.getKey(), e.getValue());
		}
		for (int i = 0; i < cipherword.length(); i++) {
			Character key = cipherword.charAt(i);
			Character value = '_';
			try {
				value = candidate.charAt(i);
			}
			catch (StringIndexOutOfBoundsException ex){
					ex.printStackTrace();
			}
			if (!lm.get(key).contains(value)){
				List<Character> valueList = lm.get(key);
				valueList.add(value);
				lm.put(key, valueList);
			}
		}
		return lm;				
	}
	
	// if a candidate letter is the only one on a list remove it from the other candidate lists (as they likely to be wrong)
	public Map<Character, List<Character>> removeSolvedLettersFromMapping(Map<Character, List<Character>> letterMapping) {
		Map<Character, List<Character>> lm = new HashMap<Character, List<Character>>();
		for (Map.Entry<Character, List<Character>> e : letterMapping.entrySet()) {
			if (!lm.containsKey(e.getKey()))
				lm.put(e.getKey(), e.getValue());
		}
		List<Character> solvedLetters;
		boolean loopAgain = true;
		while (loopAgain) {
			loopAgain = false;
			solvedLetters = new ArrayList<Character>();
			for (int i = 0; i < alphabet.length(); i++) {
				Character cipherletter = alphabet.charAt(i);
				if (letterMapping.get(cipherletter).size() == 1) {
					solvedLetters.add(letterMapping.get(cipherletter).get(0));
				}				
			}
			for (int i = 0; i < alphabet.length(); i++) {
				Character cipherletter = alphabet.charAt(i);
				for (Character s : solvedLetters) {
					if (letterMapping.get(cipherletter).size() != 1 && letterMapping.get(cipherletter).contains(s)) {
						letterMapping.get(cipherletter).remove(s);
						if (letterMapping.get(cipherletter).size() == 1) {
							loopAgain = true;
						}
					}
				}
			}
		}
		return letterMapping;
	}

	public Map<Character, List<Character>> intersectMappings(
			Map<Character, List<Character>> mapA,
			Map<Character, List<Character>> mapB
			) {
		Map<Character, List<Character>> intersectedMapping = getBlankMapping();
		for (int i = 0; i < alphabet.length(); i++) {
			Character letter = alphabet.charAt(i);
			if (mapA.get(letter).isEmpty()) {      // if there are no plain text candidates
				intersectedMapping.put(letter, new ArrayList<Character>(mapB.get(letter))); // copy from the other map
			}
			else if (mapB.get(letter).isEmpty()) { // if there are no plain text candidates
				intersectedMapping.put(letter, new ArrayList<Character>(mapA.get(letter))); // copy from the other map
			}
			else {
				for (Character mappedLetter : mapA.get(letter)) {  // step through both lists and retain only 
					if (mapB.get(letter).contains(mappedLetter)) { // letters that are common to both
						List<Character> tempList = intersectedMapping.get(letter); 
						tempList.add(mappedLetter);
						intersectedMapping.put(letter, tempList);
					}
				}
			}
		}
		return removeSolvedLettersFromMapping(intersectedMapping);
	}

	// for interface with the blackboard intersect a foreign cipher map with ours
	public void update(Map<Character, List<Character>> mapA) {
		data = intersectMappings(mapA, data);
	}
}
