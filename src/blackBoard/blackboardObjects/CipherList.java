// ----------------------------------------------------------------------------------------
//  CipherList.java 
//     Holds the array of words to be deciphered. Acts as a non-destructive stack or queue.
// ----------------------------------------------------------------------------------------
package blackBoard.blackboardObjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for a list of words to be deciphered??
 */
public class CipherList extends BlackboardObject {
	private List<String> data = new ArrayList<String>();
	private int pos = 0;             // top of the stack
	
	public void add(List<String> data) {
		this.data.addAll(data);
		pos = 0;
	}
	
	public String peek() {           // look at top item
		if (pos >= data.size()){
			return "";			
		}
		return data.get(pos);
	}
	
	public String pop() {            // removes top item
		return data.get(pos++);
	}

	public boolean done() {          // flag the end of the queue
		return pos == data.size();
	}

	public int length() {
		return data.size();
	}

	public List<String> toList() {
		return data;
	}
}
