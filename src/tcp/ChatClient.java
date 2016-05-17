package tcp;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient extends JFrame implements ActionListener {
	public final static String CONNECT_CMD = "Connect";
	public final static String DISCONNECT_CMD = "Disconnect";
	public final static String SEND_CMD = "Send";
	public final static String END_FLAG = "EOF";

	/** 
     *  
     */
	private static final long serialVersionUID = 5837742337463099673L;
	private String winTitle;
	private JLabel userLabel;
	private JLabel passwordLabel;
	private JLabel ipLabel;
	private JLabel portLabel;

	// 文本域
	private JTextField userField;
	private JPasswordField passwordField;
	private JTextField ipField;
	private JTextField portField;

	private JList friendList;
	private JTextArea historyRecordArea;
	private JTextArea chatContentArea;

	// 按钮
	private JButton connectBtn;
	private JButton disConnectBtn;
	private JButton sendBtn;
	private JCheckBox send2AllBtn;

	// socket
	private Socket mSocket;
	private SocketAddress address;
	private ChatClientThread m_client;

	public ChatClient() {
		super("Chat Client");
		initComponents();
		setupListener();
	}

	/**
	 * Note:界面部件初始化。
	 */
	private void initComponents() {
		JPanel settingsPanel = new JPanel();
		JPanel chatPanel = new JPanel();
		GridLayout gy = new GridLayout(1, 2, 10, 2);
		getContentPane().setLayout(gy);
		getContentPane().add(settingsPanel);
		getContentPane().add(chatPanel);

		// set up settings info
		settingsPanel.setLayout(new BorderLayout());
		settingsPanel.setOpaque(false);
		JPanel gridPanel = new JPanel(new GridLayout(4, 2));
		gridPanel.setBorder(BorderFactory.createTitledBorder("Server Settings & User Info"));
		gridPanel.setOpaque(false);
		userLabel = new JLabel("User Name:");
		passwordLabel = new JLabel("User Password:");
		ipLabel = new JLabel("Server IP Address:");
		portLabel = new JLabel("Server Port");
		userLabel.setOpaque(false);
		passwordLabel.setOpaque(false);
		ipLabel.setOpaque(false);
		portLabel.setOpaque(false);
		userField = new JTextField();
		passwordField = new JPasswordField();
		ipField = new JTextField();
		portField = new JTextField();
		connectBtn = new JButton(CONNECT_CMD);
		disConnectBtn = new JButton(DISCONNECT_CMD);
		JPanel btnPanel = new JPanel();
		btnPanel.setOpaque(false);
		btnPanel.setLayout(new FlowLayout());
		btnPanel.add(connectBtn);
		btnPanel.add(disConnectBtn);

		gridPanel.add(userLabel);
		gridPanel.add(userField);
		gridPanel.add(passwordLabel);
		gridPanel.add(passwordField);
		gridPanel.add(ipLabel);
		gridPanel.add(ipField);
		gridPanel.add(portLabel);
		gridPanel.add(portField);
		friendList = new JList();
		JScrollPane friendPanel = new JScrollPane(friendList);
		friendPanel.setOpaque(false);
		friendPanel.setBorder(BorderFactory.createTitledBorder("Friend List:"));
		settingsPanel.add(btnPanel, BorderLayout.SOUTH);
		settingsPanel.add(gridPanel, BorderLayout.NORTH);
		settingsPanel.add(friendPanel, BorderLayout.CENTER);

		chatPanel.setLayout(new GridLayout(3, 1));
		chatPanel.setOpaque(false);
		historyRecordArea = new JTextArea();
		JScrollPane histroyPanel = new JScrollPane(historyRecordArea);
		histroyPanel.setBorder(BorderFactory.createTitledBorder("Chat History Record:"));
		histroyPanel.setOpaque(false);
		chatContentArea = new JTextArea();
		JScrollPane messagePanel = new JScrollPane(chatContentArea);
		messagePanel.setBorder(BorderFactory.createTitledBorder("Message:"));
		messagePanel.setOpaque(false);
		// chatPanel.add(friendPanel);
		chatPanel.add(histroyPanel);
		chatPanel.add(messagePanel);
		sendBtn = new JButton(SEND_CMD);
		send2AllBtn = new JCheckBox("Send to All online Users");
		send2AllBtn.setOpaque(false);
		JPanel sendbtnPanel = new JPanel();
		sendbtnPanel.setOpaque(false);
		sendbtnPanel.setLayout(new FlowLayout());
		sendbtnPanel.add(sendBtn);
		sendbtnPanel.add(send2AllBtn);
		chatPanel.add(sendbtnPanel);
	}

	/**
	 * Note：开始按键的监听事件。
	 */
	private void setupListener() {
		connectBtn.addActionListener(this);
		disConnectBtn.addActionListener(this);
		sendBtn.addActionListener(this);
		disConnectBtn.setEnabled(false);
	}

	/**
	 * @param content
	 *            - byte array
	 * @param bsize
	 *            - the size of bytes
	 */
	public synchronized void handleMessage(char[] content, int bsize) {
		// char[] inputMessage = convertByteToChar(content, bsize);
		String receivedContent = String.valueOf(content);
		int endFlag = receivedContent.indexOf(END_FLAG);
		receivedContent = receivedContent.substring(0, endFlag);
		System.out.println("Client " + userField.getText() + " Message:" + receivedContent);
		if (receivedContent.contains("#")) {
			String[] onlineUserList = receivedContent.split("#");
			friendList.setListData(onlineUserList);
		} else {
			// just append to chat history record...
			appendHistoryRecord(receivedContent + "\r\n");
		}
	}

	public synchronized void appendHistoryRecord(String record) {
		historyRecordArea.append(record);
	}

	private String getSelectedUser() {
		int index = friendList.getSelectedIndex();
		if (index >= 0) {
			String user = (String) friendList.getSelectedValue();
			return user;
		} else {
			return "Server";
		}
	}

	// private char[] convertByteToChar(byte[] cbuff, int size) {
	// char[] charBuff = new char[size];
	// for(int i=0; i<size; i++) {
	// charBuff[i] = (char)cbuff[i];
	// }
	// return charBuff;
	// }

	public void setTitle(String title) {
		winTitle = title;
		super.setTitle(winTitle);
	}

	public String getTitle() {
		return super.getTitle();
	}

	public static void main(String[] args) {
		ChatClient client = new ChatClient();
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.pack();
		client.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (SEND_CMD.equals(e.getActionCommand())) {
			String chatContent = chatContentArea.getText();
			if (checkNull(chatContent)) {
				JOptionPane.showMessageDialog(this, "Please enter the message at least 6 characters!");
				return;
			} else if (chatContent.getBytes().length > 200) {
				JOptionPane.showMessageDialog(this, "The length of the message must be less than 200 characters!");
				return;
			}
			try {
				m_client.dispatchMessage(getSelectedUser() + "#" + chatContent);
				m_client.dispatchMessage(END_FLAG);
				appendHistoryRecord("me :" + chatContent + "\r\n");
				chatContentArea.setText(""); // try to clear user enter......
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if (DISCONNECT_CMD.equals(e.getActionCommand())) {
			enableSettingsUI(true);
		} else if (CONNECT_CMD.equals(e.getActionCommand())) {
			String serverHostName = ipField.getText();
			String portStr = portField.getText();
			String userName = userField.getText();
			char[] password = passwordField.getPassword();
			System.out.println("Password = " + password.length);
			if (checkNull(serverHostName) || checkNull(portStr) || checkNull(userName)) {
				JOptionPane.showMessageDialog(this, "Please enter user name, server host name, server port!");
				return;
			}
			setTitle("Chat Client-" + userName);
			address = new InetSocketAddress(serverHostName, Integer.parseInt(portStr));
			mSocket = new Socket();
			try {
				mSocket.connect(address);
				m_client = new ChatClientThread(this, mSocket);
				m_client.dispatchMessage(userName); // send user name
				// m_client.dispatchMessage(END_FLAG); // send end flag
				m_client.start();
				enableSettingsUI(false);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	/**
	 * Note：设置UI部件的可编辑性。
	 */
	private void enableSettingsUI(boolean enable) {
		ipField.setEditable(enable);
		portField.setEnabled(enable);
		userField.setEditable(enable);
		passwordField.setEnabled(enable);
		connectBtn.setEnabled(enable);
		disConnectBtn.setEnabled(!enable);
	}

	/**
	 * Note：判断inputString字符串是否为空。
	 */
	private boolean checkNull(String inputString) {
		if (inputString == null || inputString.length() == 0) {
			return true;
		} else {
			return false;
		}
	}

}