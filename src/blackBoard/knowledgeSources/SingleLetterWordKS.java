// ----------------------------------------------------------------------------------------
//  SingleLetterWordKS.java 
//     Knowledge source, used for single letter words. 
//     this ks can contribute if we have a single letter word.  
//     There are only two single letter words in English "a" and "I" so we map them both
//     to the corresponding cipher letter and update the blackboard.   
// ----------------------------------------------------------------------------------------
package blackBoard.knowledgeSources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import blackBoard.Blackboard;

public class SingleLetterWordKS extends KnowledgeSource {
	private List<Character> words;

	public SingleLetterWordKS(Blackboard blackboard, String name) {
		super(blackboard, name);
		// the only 1 letter words in english
		Character [] wordArray = {'a', 'i'};
		words = new ArrayList<Character>(Arrays.asList(wordArray));
	}

	/**
	 * contribute only if the top of the cipherList is a single letter word
	 * @return
	 */
	public boolean is_eager_to_contribute() {
		return cipherList.peek().length() == 1;
	}

	/**
	 * update the cipher by adding the only 2 1-letter words in english to the mapping
	 */
	public void contribute() {
		Character word = cipherList.pop().charAt(0);
		Map<Character, List<Character>> cipher = cipherLetter.getBlankMapping();
		cipher.put(word, words);
		update(cipher);
	}
}
