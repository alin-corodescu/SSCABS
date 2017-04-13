package blackBoard;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import blackBoard.Actors.ActorsPool;
import blackBoard.Actors.ControlMessage;
import blackBoard.blackboardObjects.TextObject;
import blackBoard.knowledgeSources.*;

import java.io.*;

/**
 * Created by alin on 4/7/17.
 * Main class used to start the interpret command line arguments and start the dispatcher actor
 * (responsible with the decryption process)
 */
public class ActorMain {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String message = "";
        PrintWriter outputWriter = null;
        if (args.length >= 1){
            try {
                FileReader fileReader = new FileReader(args[1]);
                BufferedReader reader = new BufferedReader(fileReader);
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!message.isEmpty())
                        message.concat("\n");
                    message.concat(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (args.length == 2) {
                try {
                    outputWriter = new PrintWriter(new FileOutputStream(args[2]));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else
                outputWriter = new PrintWriter(System.out);
        }
        else {
            // example decryption for your instruction
            message = "Sy l nlx sr pyyacao l ylwj eiswi upar lulsxrj isr sxrjsxwjr, ia " +
                    "esmm rwctjsxsza sj wmpramh, lxo txmarr jia aqsoaxwa sr pqaceiamn" +
                    "sxu, ia esmm caytra jp famsaqa sj. Sy, px jia pjiac ilxo, ia sr " +
                    "pyyacao rpnajisxu eiswi lyypcor l calrpx ypc lwjsxu sx lwwpcolxw" +
                    "a jp isr sxrjsxwjr, ia esmm lwwabj sj aqax px jia rmsuijarj aqso" +
                    "axwa. Jia pcsusx py nhjir sr agbmlsxao sx jisr elh. -Facjclxo Ct" +
                    "rramm";

            System.out.println("Decrytping default message");
            outputWriter = new PrintWriter(System.out);

        }

        ActorSystem system = ActorSystem.create("decipherator");

        ActorsPool pool = new ActorsPool(system);
        ActorRef dispathcer = system.actorOf(DispatcherActor.props(outputWriter, pool));

        ControlMessage startMessage = new ControlMessage().setType(ControlMessage.Types.START);
        startMessage.setData(message);

        dispathcer.tell(startMessage, ActorRef.noSender());
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (args.length == 0) {
            System.out.println("\nTo decrypt from a file pass the filename as a command line arguement. Eg:");
            System.out.println("java sscabs cipherFile.txt\nor\njava sscabs cipherText.txt plainText.txt");
        }
    }
}
