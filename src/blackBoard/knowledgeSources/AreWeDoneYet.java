// ----------------------------------------------------------------------------------------
//  AreWeDoneYet.java 
//     Examines the values of various blackboard objects, when there is nothing on the 
//     cipherList, content in the plainText and nothing left to fix, The progress counter 
//     is set to 100 to indicate the end of processing.
// ----------------------------------------------------------------------------------------
package blackBoard.knowledgeSources;

import blackBoard.Blackboard;
import blackBoard.blackboardObjects.Counter;

public class AreWeDoneYet extends SharedFunctionsKS {

	public AreWeDoneYet(Blackboard blackboard, String name) {
		super(blackboard, name);
	}
	
	public boolean is_eager_to_contribute() {
		return cipherList.done() && plainText.length() != 0 && !stuffToFix();
	}
	
	public void contribute() {
		Counter counter = (Counter) this.blackbord.layer("progress").get();
		counter.set(100);
		
	}
}
