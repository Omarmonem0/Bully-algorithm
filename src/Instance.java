import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Instance {

    public static void main (String args[]) throws IOException {
        String totalNumberOfProcesses = args[0];
        String processPort = args[1];
        Member member = new Member(processPort, totalNumberOfProcesses);
        member.printInfo();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(member.getPort());
            System.out.println("process with id: " + member.getPid() + "is running on port: " + member.getPort());
            System.out.println("------------------------------------");
            while(true) {
                Socket socket = serverSocket.accept();
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                Message message = (Message) input.readObject();
                String messageType = message.getType();;
                switch (messageType){
                    case "PING":
                        Message response = new Ping(member.getIsDown() ? "yes" : "no");
                        output.writeObject(response);
                        break;
                    case "EXIT" :
                        member.setIsDown(true);
                        output.writeObject("done");
                        break;
                    case "UPDATE":
                        member.removeKilledMember(Integer.parseInt(message.getPayload()));
                        output.writeObject("done");
                    default:
                        break;
                }
                input.close();
                output.close();
                socket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
