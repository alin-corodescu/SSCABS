// ----------------------------------------------------------------------------------------
//  BlackboardMain.java 
//     Creates the blackboard, knowledge sources and controller objects.  The cipher text
//     message is then added to the cipher text layer of the blackboard and the controller  
//     is run to decrypt the contents of the message.
//
//     You may need to add code and edit this file as part of your solution. 
// ----------------------------------------------------------------------------------------
package blackBoard;

import blackBoard.knowledgeSources.AreWeDoneYet;
import blackBoard.knowledgeSources.CommonWordsKS;
import blackBoard.knowledgeSources.DecryptKS;
import blackBoard.knowledgeSources.LetterFrequencyKS;
import blackBoard.knowledgeSources.ReworkKS;
import blackBoard.knowledgeSources.SingleLetterWordKS;
import blackBoard.blackboardObjects.TextObject;

import static blackBoard.Actors.TextUtils.addSpacesAfterSymbols;

public class BlackboardMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// create the blackboard
		Blackboard blackboard = new Blackboard();

		// add the knowledge sources
		// think i documented it
        blackboard.add_expert(new LetterFrequencyKS(blackboard, "Freq"));
        // easily documented
        blackboard.add_expert(new SingleLetterWordKS(blackboard, "1Letter"));
        // communicates with the server
        blackboard.add_expert(new CommonWordsKS(blackboard, "Words"));
        // decrypts the words into plain text
        blackboard.add_expert(new DecryptKS(blackboard, "decrypt"));
        // uses plain text and tries to fix the unmatched characters
        blackboard.add_expert(new ReworkKS(blackboard, "rework"));
		//
        blackboard.add_expert(new AreWeDoneYet(blackboard, "doneYet"));

        // create the controller 
		Controller ctrl = new Controller(blackboard);

        if (args.length > 0){
        	// Add code here to read and decrypt the contents of a cipher file (print plain 
        	// text to the screen) when a second argument is provided create a file of that 
        	// name write the plain text to that file. 
        }
        else {
        	// example decryption for your instruction
		    String message = "\"Wifv cuixb’g mihgm, cht,\" mub Mbdhx ghyz.\"Gi ym yg,\" Hvmfvi ghyz.\"Afbgg wif’z ebmmbv xim xbbz kb mubx.\"Hzh’g cuixb ohg xim mihgm.Yx mub qhv, ub ptyccbz ym icbx hxz guiobz ym uyg ehzab mubx ohymbz h kikbxm ouytb ym nbvypybz uyg yzbxmymw oymu mub Giqyht Uhvkixw evhyxg.Ixqb ym uhz, ym gcyttbz ymg afmg.Gub’z qhttbz mub thgm BdqfgbQtfe xfkebv h kixmu ebpivb hxz ub’z uhz ym zygqixxbqmbz.H obbs thmbv, gub ohg qhttyxa mub xbo xfkebv, moyqb kivb ebpivb ub qhfaum ubv.Gikboubvb yx muhm obbs, gub’z khzb qixmhqm oymu gikbixb oui’z aynbx ubv mub xbo xfkebv.Ym qiftz uhnb ebbx h pvybxz hm gquiit" +
					" mitz ubv phqb-mi-phqb, efm yp ub ohg tfqsw, ym ohg ew cuixb.";

            message = "Sy l nlx sr pyyacao l ylwj eiswi upar lulsxrj isr sxrjsxwjr, ia " +
                    "esmm rwctjsxsza sj wmpramh, lxo txmarr jia aqsoaxwa sr pqaceiamn" +
                    "sxu, ia esmm caytra jp famsaqa sj. Sy, px jia pjiac ilxo, ia sr " +
                    "pyyacao rpnajisxu eiswi lyypcor l calrpx ypc lwjsxu sx lwwpcolxw" +
                    "a jp isr sxrjsxwjr, ia esmm lwwabj sj aqax px jia rmsuijarj aqso" +
                    "axwa. Jia pcsusx py nhjir sr agbmlsxao sx jisr elh. -Facjclxo Ct" +
                    "rramm";

			TextObject cipher = (TextObject) blackboard.layer("cipherText");
			message = addSpacesAfterSymbols(message);
			cipher.add(message);
			
			// launch the controller to decrypt the message 
			String plainText = ctrl.runLoop();
			
			// useful for debugging
//			System.out.println("cipherLetters: " + ((CipherLetter)blackboard.layer("cipherLetter")).getData() );			
			System.out.println("Example decryption:\n" + message + "\nbecomes:\n" + plainText);
			System.out.println("\nTo decrypt from a file pass the filename as a command line arguement. Eg:");
			System.out.println("java sscabs cipherFile.txt\nor\njava sscabs cipherText.txt plainText.txt");


			System.out.println("THIS IS THE MAIN CLASS FOR THE IMPLEMENTATION WITHOUT ACTORS.");
			System.out.println("FOR THE ACTOR IMPLEMENTATION, USE ActorMain class");
        }
	}
}
