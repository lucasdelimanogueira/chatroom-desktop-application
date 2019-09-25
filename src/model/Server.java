package model;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
	
	private ServerSocket serverSocket;
	private Map<Socket, Client> clientsMap = new HashMap<>();
	private int clientsNumber = 0;
	
	//criar socket do servidor
	public void createServerSocket(int port) throws IOException{
		serverSocket = new ServerSocket(port);
	}
	
	//fecha socket do servidor
	public void closeServerSocket(ServerSocket serverSocket) throws IOException{
		if(serverSocket != null) {
			serverSocket.close();
		}
	}
	
	//aguarda novas conexões de clientes e aceita quando houver
	public Socket waitConnection() throws IOException{
		Socket clientSocket = serverSocket.accept();
		
		return clientSocket;
	}
	
	//configura comunicação com novo client
	public void configNewClient(Socket clientSocket) throws IOException{
		new Thread() {
			@Override
			public void run() {
				try {					
					//cria e configura novo cliente com seu respectivo socket
					Client client = new Client();
					client.setClientSocket(clientSocket);
					
					//cria streams de entrada e saída
					
					/*do ponto de vista do servidor, output envia para o cliente e 
					 * input recebe dado do cliente
					 */
					client.setOutput(new ObjectOutputStream(clientSocket.getOutputStream()));
					client.setInput(new ObjectInputStream(clientSocket.getInputStream()));

					//primeira mensagem é o nome do cliente
					client.setClientName(client.getInput().readUTF());
					
					//adiciona o cliente na lista de clientes
					clientsMap.put(clientSocket, client);

					//le mensagens recebidas
					String msg;
					while(true) {
						msg = client.getInput().readUTF(); //
						System.out.println(client.getClientName() + " disse: " + msg);
							
						//escreve mensagens para os usuários
						for(Client clients : clientsMap.values()) {
							clients.getOutput().writeUTF(client.getClientName() + ": " + msg + "\n");
							clients.getOutput().flush();
						}
					}
					
				}catch(IOException e) {
					System.err.println("Cliente fechou a conexão.");
				}
			}
		}.start();
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	public Map<Socket, Client> getClientsMap() {
		return clientsMap;
	}

}
