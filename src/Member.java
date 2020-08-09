import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Member {
    private long pid;
    private boolean leader;
    private boolean isDown;
    private int port;
    private int totalNumberOfMembers;
    private LinkedList liveMembers;

    public long getPid() {
        return  this.pid;
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
        System.out.println("process with id " + this.getPid() + " terminated");
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
        System.out.println("Active members: " + Arrays.toString(this.liveMembers.toArray()));
    }
    public LinkedList getMembersPorts() {
        LinkedList ports = new LinkedList<>();
        int maximumPort = 3000 + this.totalNumberOfMembers;
        for (int i = 1; i + 3000 <= maximumPort; i++) {
            if (this.port != i + 3000) {
                ports.add(i + 3000);
            }
        }
        return ports;
    }

    public void removeKilledMember(int port) {
        this.liveMembers.removeFirstOccurrence(port);
        System.out.println("I'm [" + this.pid + "] process with port: " + port + " is removed from my list my new list : " + Arrays.toString(this.liveMembers.toArray()));
    }

}
