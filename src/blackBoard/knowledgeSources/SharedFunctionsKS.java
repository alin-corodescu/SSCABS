// ----------------------------------------------------------------------------------------
//  SharedFunctionsKS.java 
//     Not really a knowledge source but a collection of common methods for some of the 
//     other knowledge sources.  It may not be good SOLID design but this is for another day
// ----------------------------------------------------------------------------------------
package blackBoard.knowledgeSources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import blackBoard.Blackboard;
import blackBoard.blackboardObjects.Counter;
import blackBoard.blackboardObjects.TextObject;

public class SharedFunctionsKS extends KnowledgeSource {
	protected TextObject plainText;
	protected Counter unresolved;
	protected String alphabet = "abcdefghijklmnopqrstuvwxyz";

	public SharedFunctionsKS(Blackboard blackboard, String name) {
		super(blackboard, name);
		plainText = (TextObject) blackboard.layer("plainText");
		unresolved = (Counter) blackboard.layer("unresolved");
	}

	// generates a word code from the string 
	public String getWordPattern(String word) {
		int nextNum = 0;
		word = word.toLowerCase();
		Map<Character, String> letterNums = new HashMap<Character, String>();
		List<String> wordPattern = new ArrayList<String>();
		for (int i = 0; i < word.length(); i++) {
			Character letter = word.charAt(i);
			if (!letterNums.containsKey(letter)) {
				letterNums.put(letter, ""+nextNum);
				nextNum++;
			}
			wordPattern.add(letterNums.get(letter)); 			
		}
		return String.join(".", wordPattern);
	}
	
	// removes characters from inStr that occur in remStr
	public String removeMatches(String inStr, String remvStr) {
		String newStr = "";
		for (int i = 0; i < inStr.length(); i++) {
			String ch = "";
			try {
				ch = inStr.substring(i,i+1);
			}  catch (IndexOutOfBoundsException e) {
				
			}
			if (!remvStr.contains(ch)) {
				newStr += ch;
			}
		}
		return newStr;
	}

	// removes duplicate characters from a string
	public String removeDupes(String myStr) {
		String newStr = "";
		for (int i = 0; i < myStr.length(); i++) {
			String ch = myStr.substring(i,i+1);
			if (!newStr.contains(ch)) {
				newStr += ch;
			}
		}
		return newStr;
	}
	
	// if there are still letters yet to be deciphered, _ means no match 
	public boolean stuffToFix() {
		return plainText.count('_') != unresolved.toInt(); 
	}
}
