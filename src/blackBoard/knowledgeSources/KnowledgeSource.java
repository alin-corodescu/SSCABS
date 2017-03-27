// ----------------------------------------------------------------------------------------
//  KnowledgeSource.java 
//     All knowledge source objects inherit from this class.  The child object must implement
//     the is_eager_to_contribute and contribute methods to be used within the blackboard.
//     The contribute method may then call the update method with a cipher map to update
//     the central cipher map held on the blackboard.
// ----------------------------------------------------------------------------------------
package blackBoard.knowledgeSources;

import java.util.List;
import java.util.Map;

import blackBoard.Blackboard;
import blackBoard.blackboardObjects.CipherLetter;
import blackBoard.blackboardObjects.CipherList;

/**
 * each of the knowledge source will maybe offer a cipher key
 */
public class KnowledgeSource {
	private String name;
	protected Blackboard blackboard;
	// actual key of the cipher
	protected CipherLetter cipherLetter;
	// list of words to decipher - guess
	protected CipherList cipherList;
	
	public KnowledgeSource(Blackboard blackboard, String name) {
		this.blackboard = blackboard;
		this.name = name;
		this.cipherLetter = (CipherLetter) this.blackboard.layer("cipherLetter");
		this.cipherList = (CipherList) blackboard.layer("cipherList");
	}

	public void update(Map<Character, List<Character>> cipher) {
		cipherLetter.update(cipher);		
	}

	// needs to be overridden

	/**
	 * function used to check if the knowledge source can contribute to the
	 * cipher
	 * @return
	 */
	public boolean is_eager_to_contribute() {
		return false;
	}
	
	// needs to be overridden

	/**
	 * functions used to actually contribute to the cipher
	 */
	public void contribute() {
		System.out.println("Error in:" + this + " contribute() must be implemented in subclass.");
	}
	
	public String toString() {
		return "<K-" + name + ">";
	}
}
