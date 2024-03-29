// ----------------------------------------------------------------------------------------
//  AreWeDoneYet.java 
//     Examines the values of various blackboard objects, when there is nothing on the 
//     cipherList, content in the plainText and nothing left to fix, The progress counter 
//     is set to 100 to indicate the end of processing.
// ----------------------------------------------------------------------------------------
package blackBoard.knowledgeSources;

import blackBoard.Blackboard;
import blackBoard.blackboardObjects.Counter;

/**
 * Class used to check progress, but wrapped as a SharedFunctionKS
 */
public class AreWeDoneYet extends SharedFunctionsKS {

	public AreWeDoneYet(Blackboard blackboard, String name) {
		super(blackboard, name);
	}

    /**
     * only returns true when the progress is done, there is nothing left to do
     * @return
     */
	public boolean is_eager_to_contribute() {
		return cipherList.done() && plainText.length() != 0 && !stuffToFix();
	}

    /**
     * sets the progress counter to 100 - Controller will stop
     */
	public void contribute() {
		Counter counter = (Counter) this.blackboard.layer("progress").get();
		counter.set(100);
		
	}
}
