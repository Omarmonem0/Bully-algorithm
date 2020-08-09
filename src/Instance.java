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
            System.out.println("[" + member.getPid() + "] is running on port: " + member.getPort());
            printLine();
            while(true) {
                Socket socket = serverSocket.accept();
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                Message message = (Message) input.readObject();
                String messageType = message.getType();;
                switch (messageType){
                    case "PING":
                        if(message.getPayload().equals("From Detector")){
                            Message response = new Ping(member.getIsDown() ? "no" : "yes");
                            output.writeObject(response);
                        } else {
                            member.startElection();
                            output.writeObject("started");
                        }
                        break;
                    case "EXIT" :
                        member.setIsDown(true);
                        output.writeObject("done");
                        break;
                    case "UPDATE":
                        member.removeKilledMember(Integer.parseInt(message.getPayload()));
                        output.writeObject("done");
                        break;
                    case "ELECTION":
                        System.out.println("[" + member.getPid() + "] I will start new election now");
                        printLine();
                        member.startElection();
                        break;
                    case "WINNER":
                        System.out.println("Winner message");
                        member.setLeaderId(message.getPayload());
                        System.out.println("[" + member.getPid() + "]: All hail to the new king");
                        printLine();
                        break;
                    default:
                        break;
                }
                input.close();
                output.close();
                socket.close();
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void printLine(){
        System.out.println("--------------------");
    }
}
