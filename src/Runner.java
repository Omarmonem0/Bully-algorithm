import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class Runner {
    public static void main(String args[]) {
        try {
            System.out.println("Main process id: " + ProcessHandle.current().pid());
            int initialPort = 3000;
            int numberOfProcess = 0;
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter number of process you want: ");
            numberOfProcess = sc.nextInt();
            for (int i = 1 ; i <= numberOfProcess ; i++){
              Runner.startNewProcess(numberOfProcess, initialPort + i);
            }
            System.out.println("Enter port of the process you want to kill: ");
            int toBeKilledProcessPort = sc.nextInt();
            Runner.terminateRunningProcess(toBeKilledProcessPort);
            Runner.notifyAll(numberOfProcess, initialPort, toBeKilledProcessPort);

            // Runner.startFailureDetector(numberOfProcess);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void startFailureDetector(int numberOfProcess) throws IOException, InterruptedException {
        int detectorPort = 3000;
        LinkedList parameters = new LinkedList<String>();
        parameters.add(Integer.toString(numberOfProcess));
        parameters.add(Integer.toString(detectorPort));
        JavaProcess.exec(Detector.class, parameters);
    }

    public static void startNewProcess(int numberOfProcesses, int port) throws IOException, InterruptedException {
        LinkedList parameters = new LinkedList<String>();
        parameters.add(Integer.toString(numberOfProcesses));
        parameters.add(Integer.toString(port));
        JavaProcess.exec(Instance.class, parameters);
    }

    public static void terminateRunningProcess(int port) throws IOException, InterruptedException, ClassNotFoundException {
        Message message = new Terminate("");
        System.out.println("I'm Runner thread and I'm Sending termination message to process running on port " + port);
        Socket socket = new Socket("127.0.0.1" , port);
        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        output.writeObject(message);
        input.close();
        output.close();
        Thread.sleep(100);
    }

    public static void notifyAll(int numberOfMembers, int initialPort, int killedProcess) throws IOException, ClassNotFoundException, InterruptedException {
        for(int i = 1; i <= numberOfMembers; i++ ){
            int targetPort = initialPort + i;
            if(targetPort != killedProcess) {
                Message message = new Update(Integer.toString(killedProcess));
                System.out.println("Sending notification" + " to port " + targetPort + " to delete " + killedProcess);
                Socket socket = new Socket("127.0.0.1" , targetPort);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                output.writeObject(message);
                input.close();
                output.close();
                Thread.sleep(100);
            }

        }
    }
}
