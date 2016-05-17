package applet;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
//import com.borland.jbcl.layout.*;
import javax.swing.*;
import java.io.*;
import java.net.*;

public class ChatClientApplet extends Applet implements Runnable {
	private boolean isStandalone = false;
	JLabel jLabel1 = new JLabel();
	JTextField jTextField1 = new JTextField();
	JLabel jLabel2 = new JLabel();
	JButton jButton1 = new JButton();
	DataInputStream m_in; // ��Ϣ������
	DataOutputStream m_out; // ��Ϣ�����
	JScrollPane jScrollPane1 = new JScrollPane();
	JTextArea jTextArea1 = new JTextArea();
	JLabel jLabel3 = new JLabel();

	// Get a parameter value
	public String getParameter(String key, String def) {
		return isStandalone ? System.getProperty(key, def) : (getParameter(key) != null ? getParameter(key) : def);
	}

	// Construct the applet
	public ChatClientApplet() {
	}

	// Initialize the applet
	public void init() {
		m_in = null;
		m_out = null;
		try {
			URL url = getCodeBase(); // ��ȡapplet ��URL ֵ��
			// ��ȡ������IP��ַ
			InetAddress inetaddr = InetAddress.getByName(url.getHost());
			Socket m_socket;
			// ��Ļ��ʾ������IP��ַ��ͨѶЭ��
			System.out.println("������:" + inetaddr + " " + url.getHost() + " " + url.getProtocol());
			m_socket = new Socket(inetaddr, 8080); // �����������IP��ַ���ӵ��׽ӿ�
			// ���׽ӿ��Ͻ���������
			m_in = new DataInputStream(m_socket.getInputStream());
			// ���׽ӿ��Ͻ��������
			m_out = new DataOutputStream(m_socket.getOutputStream());
		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Component initialization
	private void jbInit() throws Exception {
		jLabel1.setFont(new java.awt.Font("DialogInput", 1, 20));
		jLabel1.setForeground(Color.red);
		jLabel1.setToolTipText("���ߣ�����ɭ  Email:yanglinsen@126.com");
		jLabel1.setText("һ�Զ��������Java Applet�ͻ���");
		jLabel1.setBounds(new Rectangle(81, 22, 358, 38));
		this.setBackground(UIManager.getColor("ComboBox.selectionBackground"));
		this.setLayout(null);
		jLabel2.setFont(new java.awt.Font("Dialog", 1, 16));
		jLabel2.setForeground(Color.blue);
		jLabel2.setText("���뷢����Ϣ��");
		jLabel2.setBounds(new Rectangle(35, 78, 124, 37));
		jButton1.setBackground(UIManager.getColor("OptionPane.questionDialog.titlePane.background"));
		jButton1.setBounds(new Rectangle(413, 77, 70, 39));
		jButton1.setFont(new java.awt.Font("Dialog", 1, 16));
		jButton1.setForeground(Color.blue);
		jButton1.setToolTipText("������Ͱ�ť���ɷ����ı������������Ϣ");
		jButton1.setText("����");
		jButton1.addActionListener(new ChatClientApplet_jButton1_actionAdapter(this));
		jTextField1.setBackground(new Color(182, 231, 223));
		jTextField1.setFont(new java.awt.Font("Dialog", 0, 17));
		jTextField1.setToolTipText("���ı����������뷢�͵���Ϣ");
		jTextField1.setBounds(new Rectangle(153, 80, 248, 35));
		jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane1.getViewport().setBackground(UIManager.getColor("MenuItem.acceleratorForeground"));
		jScrollPane1.setBounds(new Rectangle(54, 130, 410, 231));
		jTextArea1.setBackground(UIManager.getColor("Desktop.background"));
		jTextArea1.setFont(new java.awt.Font("Dialog", 0, 18));
		jTextArea1.setForeground(Color.black);
		jLabel3.setFont(new java.awt.Font("Dialog", 0, 16));
		jLabel3.setForeground(Color.blue);
		jLabel3.setText("�����뿪�����ң����ڷ��Ϳ��������ַ���leave������");
		jLabel3.setBounds(new Rectangle(62, 367, 394, 24));
		this.add(jLabel1, null);
		this.add(jLabel2, null);
		this.add(jTextField1, null);
		this.add(jButton1, null);
		this.add(jScrollPane1, null);
		this.add(jLabel3, null);
		jScrollPane1.getViewport().add(jTextArea1, null);
		// ���������߳�
		new Thread(this).start();
	}

	// Get Applet information
	public String getAppletInfo() {
		return "Applet Information";
	}

	// Get parameter info
	public String[][] getParameterInfo() {
		return null;
	}

	public void run() {
		try {
			while (true) {
				// ���������߷�������Ϣ���߳̽������ڸ�����У�ֱ����Ϣ������
				String s = m_in.readUTF(); // ��һ��UTF��ʽ�ַ�����
				if (s != null)
					// ����Ϣ��ʾ����Ϣ��ʾ�����С�
					jTextArea1.append(s + "\n");
			}
		} catch (Exception e) {
			jTextArea1.append("Network problem or Sever down.\n");
			jTextArea1.setVisible(false);
		}
	}

	public void stop() {
		try {
			m_out.writeUTF("leave");
			// �����뿪�����ң����ڷ��Ϳ��������ַ���leave
		} catch (IOException e) {
		}
	}

	void jButton1_actionPerformed(ActionEvent e) {
		String b = jTextField1.getText();
		jTextField1.setText("");
		// ���û��������Ϣ���͸� Chat Server
		try {
			m_out.writeUTF(b); // ������߷���һUTF��ʽ�ַ�����
		} catch (IOException g) {
		}
	}
}

class ChatClientApplet_jButton1_actionAdapter implements java.awt.event.ActionListener {
	ChatClientApplet adaptee;

	ChatClientApplet_jButton1_actionAdapter(ChatClientApplet adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jButton1_actionPerformed(e);
	}
}
