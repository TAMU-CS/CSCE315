import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
 
    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
        //io stuff
        Scanner scanObj = new Scanner(System.in);
        
        String inputLine;
        
        //1. welcome message read in
        inputLine = in.readLine();
        System.out.println(inputLine);
        //2. setup board configuration
        inputLine = in.readLine();
        System.out.println(inputLine);
        
        //3. acknowledge that input was received
        out.println("READY");
        
        while((inputLine = in.readLine()) != null) {
        	System.out.println(inputLine);
        	
        	String resp = scanObj.nextLine();
        	if(resp.length() > 0) {
        		out.println(scanObj.nextLine()); //response
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