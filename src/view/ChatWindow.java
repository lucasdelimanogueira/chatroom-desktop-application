package view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.Client;

import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;


public class ChatWindow extends JFrame {

	private JPanel contentPane;
	private JTextArea taMessageBox = new JTextArea();
	JButton buttonSendMessage = new JButton("Enviar");
	static JTextArea taChatBox = new JTextArea();
	JScrollPane scrollPaneChat = new JScrollPane(taChatBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	private static Client client = new Client();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatWindow frame = new ChatWindow();
					frame.setVisible(true);
					frame.taMessageBox.requestFocus();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		//nome de login
		String name = JOptionPane.showInputDialog("Digite seu nome");
		try{			
			client.connectToServer(); //conecta com o servidor
			client.sendMessage(name); //envia nome
			
		}catch(IOException e) {
			System.err.println("Conexão com o servidor perdida");
		}
		
		//escuta mensagens
		while(true) {
			try {
				taChatBox.append(client.getMessages()); //recebe mensagens do servidor
			}catch(IOException e){
				System.err.println("Conexão com o servidor perdida");
			}			
		}
	}

	public ChatWindow() {
		//JPanel
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//JTextArea MessageBox
		taMessageBox.setRequestFocusEnabled(true);
		taMessageBox.setBounds(5, 234, 326, 22);
		taMessageBox.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		contentPane.add(taMessageBox);
		
		//JButton sendMessage
		buttonSendMessage.setBounds(341, 235, 89, 23);
		contentPane.add(buttonSendMessage);
		
		//Chat
		taChatBox.setEditable(false);
		taChatBox.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPaneChat.setBounds(5, 5, 425, 218);
		contentPane.add(scrollPaneChat);
		
		//listeners
		//clique botao de enviar mensagens
		buttonSendMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				taMessageBox.requestFocus(); //devolve foco para a caixa de texto
				
				String msg = taMessageBox.getText(); //recupera texto da caixa de texto
				if(!msg.isEmpty()) {
					try {
						client.sendMessage(msg); //envia mensagem para o servidor
						taMessageBox.setText("");  //limpa caixa de texto
					}catch(IOException IOException) {
						System.err.println("Conexão com o servidor perdida");
					}
				}
			}
		});
		
		//tecla enter
		taMessageBox.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					e.consume(); //evitar quebra de linha
					buttonSendMessage.doClick(); //aciona clique no botao de envio de mensagens
				}
			}
		});
	}
}
