// ----------------------------------------------------------------------------------------
//  Controller.java 
//     Iterates through the knowledge sources to see if they have are able to contribute
//     Also cleans the cipherList part of the blackboard and return the plain text when
//     processing is done.
// ----------------------------------------------------------------------------------------
package blackBoard;

import blackBoard.knowledgeSources.KnowledgeSource;

public class Controller {
	private Blackboard blackboard;
	public Controller(Blackboard blackboard) {
		this.blackboard = blackboard;		
	}
	
	public String runLoop() {
		while (blackboard.layer("progress").toInt() < 100) {  // keep global flag on blackboard
			int count = 0;
			for (KnowledgeSource ks : blackboard.experts()) { // step through all knowledge sources
				if (ks.is_eager_to_contribute()) {       // can the ks solve this kind of problem
//					System.out.println("using: " + ks);  // useful when debugging
					ks.contribute();                     // then do so
                    //popping happens inside the contribute() function;
				}
				else {
					count++;
				}
			}
			// if none of the knowledge sources want to contribute
			if (count == blackboard.experts().size()) {  // have we looped through everything
				blackboard.layer("cipherList").pop();    // move on to the next word
			}			
		}
		return blackboard.layer("plainText").toString(); // return the plain text solution
	}
}
