
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Detector {
    public static void main (String args[]) throws InterruptedException, IOException, ClassNotFoundException {
        int numberOfMembers = Integer.parseInt(args[0]);
        int initialPort = Integer.parseInt(args [1]);
        Detector.monitorProcesses(numberOfMembers, initialPort);
    }

    public static void monitorProcesses(int numberOfMembers, int initialPort) throws IOException, InterruptedException, ClassNotFoundException {
        while (true) {
            for(int i = 1; i <= numberOfMembers; i++ ){
                int targetPort = initialPort + i;
                Ping pingMessage = new Ping("From Detector");
                System.out.println("[Detector]: Hi " + targetPort + " Are you alive?");
                Socket socket = new Socket(Constants.host , targetPort);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                output.writeObject(pingMessage);
                Message response = (Message) input.readObject();
                System.out.println("[" + targetPort + "]:" + response.getPayload());
                if(response.getPayload() == "no") {
                    break;
                }
                input.close();
                output.close();
                Thread.sleep(100);
            }
            System.out.println("--------------------");
            break;
        }
        Detector.sendStartElectionMessage();
    }

    public static void sendStartElectionMessage() throws IOException, ClassNotFoundException, InterruptedException {
        Message message = new Election("");
        Socket socket = new Socket(Constants.host, 3001);
        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        output.writeObject(message);
        socket.close();
        input.close();
        output.close();
        Thread.sleep(100);
    }
}
