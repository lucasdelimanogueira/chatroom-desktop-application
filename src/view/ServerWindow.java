package view;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import model.Server;

import javax.swing.JTextArea;
import java.awt.Component;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class ServerWindow extends JFrame {

	private JPanel contentPane;
	private JTextArea taLogBox = new JTextArea();
	private JScrollPane scrollPaneChat = new JScrollPane(taLogBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JTextPane txtpnLog = new JTextPane();
	private JTextPane textPane_1 = new JTextPane();
	private JTextPane tpStatus = new JTextPane();
	private Server server = null;
	private String flagStatus = "OFFLINE";
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerWindow frame = new ServerWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ServerWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//LogBox
		taLogBox.setEditable(false);
		taLogBox.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPaneChat.setBounds(5, 62, 425, 188);
		contentPane.add(scrollPaneChat);
		
		//textos
		txtpnLog.setText("Log");
		txtpnLog.setBounds(5, 42, 24, 22);
		contentPane.add(txtpnLog);
		
		textPane_1.setText("Status:");
		textPane_1.setBounds(5, 11, 54, 20);
		contentPane.add(textPane_1);
		
		tpStatus.setText("Offline");
		tpStatus.setBounds(56, 11, 54, 20);
		contentPane.add(tpStatus);
		
		//botao
		JButton button = new JButton("Conectar");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread online = null;
				
				switch(flagStatus) {
				case "OFFLINE":
					flagStatus = "ONLINE";
					button.setText("Desconectar");
					server = new Server();
					
					online = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								server.createServerSocket(9999); //cria servidor
								
								taLogBox.append("Servidor criado!\n"); //log
								tpStatus.setText("Online");
								taLogBox.append("Aguardando conexões...\n\n"); //log
								
								while(true) {
									Socket clientSocket = server.waitConnection(); //espera por novas conexões (travado aqui até novo cliente se conectar)
									server.configNewClient(clientSocket); //cria um gerenciador para o respectivo cliente conectado (em uma nova thread)
									
									//log
									taLogBox.append("Novo cliente conectado. \n"
													
													+ "client. IP: " + clientSocket.getInetAddress().getHostAddress()
													+ "\n\n");

									System.out.println("Cliente conectado.");
								}
							}catch(IOException IOException) {
								tpStatus.setText("Offline");
							}
						}
					});
					online.start();
					break;
					
				case "ONLINE":
					try {
						server.closeServerSocket(server.getServerSocket());;
						flagStatus = "OFFLINE";
						taLogBox.append("\nServidor desconectado.\n\n");
						button.setText("Conectar");
						break;
					}catch(IOException IOException) {
						System.err.println(IOException.getMessage());
					}
					
				}	
			}
		});
		button.setBounds(269, 11, 161, 23);
		contentPane.add(button);
	}
}
