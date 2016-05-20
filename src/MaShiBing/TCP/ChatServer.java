package MaShiBing.TCP;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
	boolean started = false;
	ServerSocket ss = null;
	//�����������ӵĿͻ��ˣ����ڹ㲥��Ϣ��
	List<Client> clients = new ArrayList<Client>();

	/**
	 * Note:�½�һ��ChatServer����������
	 */
	public static void main(String[] args) {
		ChatServer myServer = new ChatServer();
		myServer.start();
	}

	public void start() {
		int id=1;
		try {
			// �����������˿�6666
			ss = new ServerSocket(6666);
			started = true;
			System.out.println("�������Ѿ�������");
		} catch (BindException e) {
			System.out.println("�½�ServerSocket��ʧ�ܣ�����˿��Ƿ�ռ�á�");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			while (started) {
				Socket s = ss.accept();// �˷���������̡߳��ȴ��ͻ���Socket���ӡ�
				Client c = new Client(s);
				c.setID(id++);
				System.out.println(c.getID()+"��"+"�ͻ��������ӡ�");
				new Thread(c).start();//���߳�startʱ��ʵ����Runnable�ӿڵ�Client����c�е�run����������
				clients.add(c);//��c��ӵ�Client�б�
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ss.close();//�رտͻ���socket
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Note:Runnable �ӿ�Ӧ������Щ����ͨ��ĳһ�߳�ִ����ʵ��������ʵ�֡�����붨��һ����Ϊ run ���޲���������
	 */
	class Client implements Runnable {
		private Socket s;
		private DataInputStream dis = null;
		private DataOutputStream dos = null;
		private boolean Connected = false;
		private int ID;//Ϊÿ���ͻ��˼�һ��ID
		
		/**
		 * Note:���socket�������������
		 */
		public Client(Socket s) {
			this.s = s;
			try {
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				Connected = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//��ÿͻ���ID
		public int getID(){
			return ID;
		}
		
		//���ÿͻ���ID
		public void setID(int id){
			this.ID = id;
		}
		

		public void send(String str) {
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				clients.remove(this);
				System.out.println(getID()+"��"+"�ͻ����˳��ˣ��ѽ����List��ɾ����");
			}
		}

		public void run() {
			try {
				while (Connected) {
					//��������dis �ж�ȡ�� UTF-8 �޸İ��ʽ����� Unicode �ַ���ʽ���ַ�����Ȼ���� String ��ʽ���ش��ַ��������ܻ���������쳣��
					String str = dis.readUTF();//�����߳�
					//System.out.println(getID()+"��˵��"+str);
					//��Clients�б������е�Client�㲥��Ϣ��
					for (int i = 0; i < clients.size(); i++) {
						Client c = clients.get(i);
						c.send(getID()+"��˵��"+str);
					}
				}
			} catch (EOFException e) {
				System.out.println(getID()+"��"+"�ͻ��˹رա�");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				//��������쳣���������������socket�ر�
				try {
					if (dis != null)
						dis.close();
					if (dos != null)
						dos.close();
					if (s != null) {
						s.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
