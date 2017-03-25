// ----------------------------------------------------------------------------------------
//  ReworkKS.java 
//     Knowledge source, used as a second pass tries to resolve any unresolved letters. 
//     this ks can contribute if we have finished the cipher list but have unresolved letters.  
//     The process looks for words containing underscores "_" as these are unresolved letters 
//     it then uses the word server again to find the best matching word and suggests the 
//     plain text letter as a solution. When finished it clears the plain text so decryption
//     can occur again.
// ----------------------------------------------------------------------------------------
package blackBoard.knowledgeSources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import blackBoard.Blackboard;
import blackBoard.WordServerInterface;

public class ReworkKS extends SharedFunctionsKS {
	private WordServerInterface wp;

	public ReworkKS(Blackboard blackboard, String name) {
		super(blackboard, name);
		wp = new WordServerInterface();
	}

	public String bestMatch(String pattern, String [] words) {
		String bestWord = "";
		int count;
		int similarity = 0;
		for (String word : words) {
			word = word.toLowerCase();
			count = 0;
			for (int c = 0; c < pattern.length(); c++) {
				if (word.charAt(c) == pattern.charAt(c)) {
					count++;
				}
				else if (pattern.charAt(c) == '_') {
					continue;
				}
				else {
					count = 0;
					break;
				}				
			}
			if (count > similarity) {
				bestWord = word;
				similarity = count;
			}
		}
		return bestWord;
	}
	
	public boolean is_eager_to_contribute() {
		return cipherList.done() && stuffToFix();
	}

	public void contribute() {
		String plaintext = plainText.toString();
		String nonletters = removeMatches(plaintext, alphabet + "_");
		nonletters = removeDupes(nonletters);
		plaintext = removeMatches(plaintext, nonletters.trim());
		String [] plainList = plaintext.split(" ");
		Map<Character, List<Character>> cipher = cipherLetter.getBlankMapping();
		for (int index = 0; index < plainList.length; index++){
			String word = plainList[index];
			if (word.length() <= 3) {
				continue;
			}
			if (word.contains("_")) {
				String cipherword = cipherList.toList().get(index);
				String pattern = getWordPattern(cipherword);
				String [] words = wp.allPatterns(pattern);
				String candidate = bestMatch(word, words);
				if (candidate != null && !candidate.isEmpty()) {
					int pos = word.indexOf("_");
					List<Character> temp = new ArrayList<Character>();
					temp.add(candidate.charAt(pos));
					cipher.put(cipherword.charAt(pos), temp);
				}
			}
		}
		update(cipher);
		plainText.add("");
	}
}
