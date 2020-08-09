## Bully Algorithm implementation

### Assumptions:
1- Each process has a unique id and listen to a certain port and the intial port is 3000 ex: if you have 4 running proccess process 1 will listen to port 
3001 and process 2 will listen to port 3002..etc.

2-Each process knows the total number of running processes and the intial port.

3- User should enter the total number of process he wants to run.

4- User shouled enter the port number of the process he wants to kill to run the election algorithm

5- A killed or terminated process is not stopped phyiscally it raises a flag tells the other process and the failure detector process that it's down.

### Proccess
1- Runner process : The main process which is responsible for forking other processes and terminating other process.

2- Detector process: A process which keeps sending ping message to all other process to check if there is down process or not and when it detectes down process
it starts the election algorithm throgh sending an election message to process listening to port 3001 (minimum process)

3- Member process : A process which listen to a certain port

### Messages
1- Ping:To check if the process is up or not

2- Update:This message is sent to all up process when another process is terminated to tell the process to update it's list of up and running processes

3- Exit:This message is sent to a process to tell it to raise a flag indicated that it's down or not available

4- Winner:The newly elected process sends this type of message to tell other processes that it's elected and election is over

5- Election:This message is sent by failure detector process to the process with minimum id to tell it to start election process

### Workflow
1- User enters the total number of processes he wants to run through console

2- Processes created and info about the created processes logged to the console (process id , port,..etc)

3- User enters the port of the process he wantsto terminate.

4- Process terminated and Update message sent to all running process to update their lists of running process.

5- Failure detector process started.

6- Failure detector process detects the terminated process and sends Election message to process runs on port 3001

7- Election begins.

### Interprocess communication
Process communication is done through sockets programming 

### Environment
OS : Ubuntu 18.04.4 LTS
IDE : IntelliJ Ultimate Edition
