package model;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	Socket clientSocket;
	String clientName = "Lucas";
	ObjectOutputStream output;	
	ObjectInputStream input;

	public void connectToServer() {
		try {
			//cria conexão entre cliente e o servidor localhost:5555
			this.setClientSocket(new Socket("localhost", 9999));
			
			//inicia stream de saída
			this.setOutput(new ObjectOutputStream(clientSocket.getOutputStream()));
			 
			//stream de entrada
			this.setInput(new ObjectInputStream(clientSocket.getInputStream()));	
					
		}catch(IOException e) {
			System.err.println("Conexão com o servidor perdida");
		}
		
	}
	
	//recebe mensagens do servidor
	public String getMessages() throws IOException{
		return this.getInput().readUTF();
	}
	
	//envia mensagens para o servidor
	public void sendMessage(String msg) throws IOException{
		this.getOutput().writeUTF(msg);
		this.getOutput().flush();
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public ObjectOutputStream getOutput() {
		return output;
	}

	public void setOutput(ObjectOutputStream output) {
		this.output = output;
	}

	public ObjectInputStream getInput() {
		return input;
	}

	public void setInput(ObjectInputStream input) {
		this.input = input;
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}	
}
