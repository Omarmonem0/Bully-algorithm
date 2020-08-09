
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
                Ping pingMessage = new Ping("");
                System.out.println("Sending " + pingMessage.getType() + " to port " + targetPort);
                Socket socket = new Socket("127.0.0.1" , targetPort);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                output.writeObject(pingMessage);
                Message message = (Message) input.readObject();
                System.out.println(message.getPayload());
                input.close();
                output.close();
                Thread.sleep(100);
            }
        }
        }
}
