import javax.swing.plaf.TableHeaderUI;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;

public class Member {
    private long pid;
    private boolean leader;
    private boolean isDown;
    private int port;
    private int totalNumberOfMembers;
    private LinkedList liveMembers;
    private String leaderId;

    public long getPid() {
        return  this.pid;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public boolean getLeader() {
        return leader;
    }

    public void setLeader(boolean leader) {
        this.leader = leader;
    }

    public boolean getIsDown() {
        return isDown;
    }

    public void setIsDown(boolean isDown) {
        this.isDown = isDown;
        System.out.println("[" + this.getPid() + "] terminated");
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return this.port;
    }
    public Member(String  port, String totalNumberOfMembers) {
        this.pid = ProcessHandle.current().pid();
        this.isDown = false;
        this.leader = false;
        this.port = Integer.parseInt(port);
        this.totalNumberOfMembers = Integer.parseInt(totalNumberOfMembers);
        this.liveMembers = this.getMembersPorts();
    }
    public void printInfo() {
        System.out.println("Process id: " + this.pid);
        System.out.println("Leader: " + this.leader);
        System.out.println("Is down: " + this.isDown);
        System.out.println("Port: " + this.port);
    }

    public LinkedList getMembersPorts() {
        LinkedList ports = new LinkedList<>();
        int maximumPort = 3000 + this.totalNumberOfMembers;
        for (int i = 1; i + 3000 <= maximumPort; i++) {
            if (i + 3000  != this.port) {
                ports.add(i + 3000);
            }
        }
        return ports;
    }

    public void startElection() throws IOException, InterruptedException {
        if(this.checkForMaximumProcessId()){
            System.out.println("Process with id: " + this.pid + " is the new leader");
            this.setLeader(true);
            this.setLeaderId("-");
            this.sendWinnerMessage();
            return;
        }
        this.pingAll();
    }

    public void send(String messageType,int port) throws IOException, InterruptedException {
        Message message = null;
        switch (messageType){
            case Constants.ping:
                message = new Ping("");
                break;
            case Constants.winner:
                message = new Winner(Long.toString(this.getPid()));
                break;
            default:
                break;
        }
        Socket socket = new Socket(Constants.host, port);
        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        output.writeObject(message);
        socket.close();
        input.close();
        output.close();
        Thread.sleep(100);
    }

    public boolean checkForMaximumProcessId() {
        int max = this.port;
        for (Object port: this.liveMembers) {
            if(Integer.parseInt(port.toString()) > max){
                max = Integer.parseInt(port.toString());
            }
        }
        return max == this.port ? true : false;
    }

    public void removeKilledMember(int port) {
        this.liveMembers.removeFirstOccurrence(port);
        System.out.println("[" + this.pid + "] process with port: " + port + " is removed from my list my new list : " + Arrays.toString(this.liveMembers.toArray()));
    }

    public void sendWinnerMessage() throws IOException, InterruptedException {
        for (Object port: this.liveMembers) {
            System.out.println("[" + this.pid + "] send winner message to " + Integer.parseInt(port.toString()));
            send(Constants.winner, Integer.parseInt(port.toString()));
            Thread.sleep(100);
        }
    }

    public void pingAll() throws IOException, InterruptedException {
        for (Object port: this.liveMembers.toArray()) {
            if(Integer.parseInt(port.toString()) > this.port) {
                System.out.println("[" + this.pid + "] sending PING to process on port " + Integer.parseInt(port.toString()));
                this.send(Constants.ping, Integer.parseInt(port.toString()));
                Thread.sleep(100);
            }
        }
    }

}
