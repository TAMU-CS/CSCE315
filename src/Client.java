import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Player plr;
 
    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
        //io
        Scanner scanObj = new Scanner(System.in);
        String inputLine;
        
        //1. welcome message read in
        inputLine = in.readLine();
        System.out.println(inputLine);
        //2. setup board configuration
        inputLine = in.readLine();
        System.out.println(inputLine);
    	String confTokens[] = inputLine.split(" ");

        //create player object
        plr = new Player(Integer.parseInt(confTokens[4]), false);  
        System.out.println("Hello Player " + plr.side + "!");
        
        //3. acknowledge that input was received
        out.println("READY");
        
        while((inputLine = in.readLine()) != null) {
        	//parse through input line and create board information
        	String tokens[] = inputLine.split(" ");
        	
        	//check for different cases
        	//System.out.println(inputLine);
        	
        	//display board and update state
    		Player oplr;
    		Board board;
    		int opt = Integer.parseInt(tokens[0]);
        	if(opt == 1 || opt == 2) {
        		int oplrId = plr.side == 1 ? 0 : 1;
        		oplr = new Player(oplrId,false);
        		board = new Board(tokens);
        		System.out.println("Board State:");
        		board.printBoard();
        	}
        	
        	String resp = plr.getMove(0, opt) + "";
        	
        	if(resp.length() > 0) {
        		out.println(resp); //response
        	}
        }
    }
 
    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        String resp = in.readLine();
        return resp;
    }
 
    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.startConnection("127.0.0.1", 6666);
        String response = client.sendMessage("hello server");
        System.out.println(response);
    }
}