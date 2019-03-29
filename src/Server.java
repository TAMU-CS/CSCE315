import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Server {
	//socket to receive info from client sockets
    private ServerSocket serverSocket;
    private Board board;
    private Player players[];
    public boolean plrJoin[];
    
    //constructor
    public Server(Board _board) {
    	board = _board;
    }
 
    //starts server to listen in an infinite loop to handle client handler threads
    public void start(int port) throws IOException {
    	
    	//server socket setup with plr id
        serverSocket = new ServerSocket(port);
        int tid = 0;
        players = new Player[2]; 
        
        //loop through client handlers and set up their sockets
        while (true) {
        	ClientHandler ch;
        	ch = new ClientHandler(serverSocket.accept(), tid, this);        	
        	Player plr = new Player(ch);
        	players[tid] = plr;
    		ch.setPlr(plr);
        	ch.run();
            tid++;
            
            //print that a player has joined
            System.out.println("Player Has Joined: " + tid);
        }
    }
    
    public void initGame() {
    	//create the player objects
    	board.StartGame(players[0], players[1]);
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
    	private Server server;
    	
    	//create a player to represent this client
    	private Player plr;
    	
    	public ClientHandler(Socket socket, int tid, Server serve) {
    		this.clientSocket = socket;
    		id = tid;
    		server = serve;
    	}
    	
    	public void setPlr(Player tplr) {
    		plr = tplr;
    	}
    	
    	public void run() {
    		try {
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(
						  new InputStreamReader(clientSocket.getInputStream()));
				
				//print out board initiation
				out.println("Welcome");
				
				//respond with board configurations
				out.println("BoardConfigurations");
				
				//user responds with ready
	            //String inputLine = in.readLine();

	            //update player status
	            server.plrJoin[id] = true;
	            
	            //check if both players are ready
	            if(server.plrJoin[0] && server.plrJoin[1]) {
	            	//initiate the board in server class
	            	server.initGame();
	            }
	            
//				in.close();
//	            out.close();
//				clientSocket.close();
    		}catch(IOException e) {
    			e.printStackTrace();
    		}


    	}
    	
    	public int getMove(int timeForMove) {
    		//call read line from in buffer
    		try {
    			out.println("Your Move:");
    			String inputLine = in.readLine();
        		return Integer.parseInt(inputLine);
    		}catch(IOException e) {
    			e.printStackTrace();
    		}
    		
    		return 0;
    	}
    }
        
    public static void main(String[] args) throws IOException {
    	/*
    	//must get options for server configuration
    	//figure out how many players will be client
    	//or run on the server input.
    	System.out.println("[Server Started]");
    	System.out.println("Configure game setup...\n");
		Scanner scanObj = new Scanner(System.in);
    	
		//game configuration
		System.out.println("How many seeds per house?");
		int seedsPerHouse = scanObj.nextInt();
		System.out.println("How many houses in a row?");
		int housesPerRow = scanObj.nextInt();
		System.out.println("Randomize seeds? [Y/N]");
		boolean randomizeSeeds = scanObj.next().charAt(0) == 'y';
		System.out.println("Time per moves (ms)? (0- no time limit)");
		int timePerMove = scanObj.nextInt();

		//get input for player 0
		System.out.println("\nConfiguring Player 0:");
    	System.out.println("Player 0 configuration, run on server? [Y/N]");
    	boolean plr0RunOnServer = scanObj.next().charAt(0) == 'y';
    	System.out.println("Player 0 AI? [Y/N]");
    	char plr0AI = scanObj.next().charAt(0);
    	
		//get input for player 1
		System.out.println("\nConfiguring Player 1:");
    	System.out.println("Player 1 configuration, run on server? [Y/N]");
    	boolean plr1RunOnServer =scanObj.next().charAt(0) == 'y';
    	System.out.println("Player 1 AI? [Y/N]");
    	boolean plr1AI = scanObj.next().charAt(0) == 'y';
    	*/
    	
    	//create player objects
    	
    	//create board
    	Board board = new Board(6, 4, false, 0);
    	//new Board(housesPerRow, seedsPerHouse, randomizeSeeds);
    	
    	//set up server object
    	Server server=new Server(board);
    	int port = 6666;
    	
    	//depending on which players are remote, set up the connection
    	server.plrJoin = new boolean[2];
    	server.plrJoin[0] = false;
    	server.plrJoin[1] = false;
    	
        server.start(port);
    }
}