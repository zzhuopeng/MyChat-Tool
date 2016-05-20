package MaShiBing.TCP;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;

public class ChatClient extends Frame {
	Socket s = null;
	DataOutputStream dos = null;
	DataInputStream dis = null;
	private boolean Connected = false;

	TextField textField = new TextField();	//�ı��༭��
	TextArea textArea = new TextArea();   	//�ı���ʾ��
	//�½�һ���̣߳����ڽ��շ���˷����������ݡ�
	Thread tRecv = new Thread(new RecvThread());
	
	/**
	 * Note:�½�һ��ChatClient�������ؿͻ��˽��档
	 */
	public static void main(String[] args) {
		ChatClient myChatClient = new ChatClient();
		myChatClient.launchFrame();
	}

	/**
	 * Note:���ؿͻ��˽��档
	 */
	public void launchFrame() {
		setLocation(450, 350);
		this.setSize(400, 400);
		add(textField, BorderLayout.SOUTH);
		add(textArea, BorderLayout.NORTH);
		pack();//�����˴��ڵĴ�С�����ʺ������������ѡ��С�Ͳ��֡�
		//���ڹرմ���ʹ�������ࡣ
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				disconnect();//�����Դ
				System.exit(0);//�ر�ϵͳ
			}
		});
		
		//������Ϣ�༭���ļ����¼���
		textField.addActionListener(new TextFListener());
		setVisible(true);
		//���ӷ�����
		connect();

		tRecv.start();
	}
	
	public void connect() {
		try {
			//����һ�����׽��ֲ��������ӵ�ָ�������ϵ�ָ���˿ںš� ��Ҫ����δ֪�쳣��IO�쳣��
			s = new Socket("127.0.0.1", 6666);
			//��������������
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
			System.out.println("���Ѿ������˷�������");
			Connected = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//�����ӶϿ�ʱ�������Դ��
	public void disconnect() {
		try {
			dos.close();
			dis.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Note:�ı��༭�������¼�
	 */
	private class TextFListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String str = textField.getText().trim();//�����ַ����ĸ���������ǰ���հ׺�β���հס� 
			textField.setText("");

			try {
				//��������޹ط�ʽʹ�� UTF-8 �޸İ���뽫һ���ַ���д������������ ��Ҫ����IO�쳣��
				dos.writeUTF(str);
				dos.flush();//��մ����������������ʹ���л��������ֽڱ�д�������С���Ҫ����IO�쳣��
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * Note���½��̣߳�ר������������Ϣ������ӡ������
	 */
	private class RecvThread implements Runnable {

		public void run() {
			try {
				while (Connected) {
					String str = dis.readUTF();//�����̡߳�
					//����ʾ������ʾ���յ������ݡ�
					textArea.setText(textArea.getText() + str + '\n');
				}
			} catch (SocketException e) {
				System.out.println("�˳��ˡ�");
			} catch (EOFException e) {
				System.out.println("���˳��ˡ�");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
