import java.net.*;
import java.io.*;

public class Server {
	//socket to receive info from client sockets
    private ServerSocket serverSocket;
 
    //starts server to listen in an infinite loop to handle client handler threads
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        int tid = 0;
        while (true) {
        	System.out.println("new socket");
            new ClientHandler(serverSocket.accept(), tid).start();
            
            tid++;
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }
    
    public static class ClientHandler extends Thread{
    	private Socket clientSocket;
    	private PrintWriter out;
    	private BufferedReader in;
    	private int id;
    	
    	private int curMove;
    	
    	//create a player to represent this client
    	private Player plr;
    	
    	public ClientHandler(Socket socket, int tid) {
    		this.clientSocket = socket;
    		id = tid;
    	}
    	
    	public void run() {
            try {
				out = new PrintWriter(clientSocket.getOutputStream(), true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				in = new BufferedReader(
				  new InputStreamReader(clientSocket.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             
            String inputLine;
            try {
				while ((inputLine = in.readLine()) != null) {
				    System.out.println(inputLine);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
            try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            out.close();
            try {
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	public int getMove(int timeForMove) {
    		return 0;
    	}
    }
    
    public static void main(String[] args) throws IOException {
        Server server=new Server();
        server.start(6666);
    }
}