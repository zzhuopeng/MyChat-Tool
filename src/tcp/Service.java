package tcp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Service extends Thread {
	private ServerSocket serverSocket;
	
	/**
	 * Note��HelloSevice�๹�캯����portΪ�����������˿ںš�
	 */
	public Service(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}
	
	/**
	 * Note:Thread������ִ��start()������ʱ���Զ��������е�run������
	 */
	public void run() {
		try {
			while (true) {
				System.out.println("�������Ѿ������������������˿�Ϊ��" + serverSocket.getLocalPort() + "\n�ȴ�����......");
				//accep()�������ȴ��ͻ���socket���ӣ��˷���������̡߳�
				Socket client = serverSocket.accept();
				System.out.println("�Ѿ�����" + client.getRemoteSocketAddress());//RemoteԶ�̵ġ�
				DataOutputStream dataOS = new DataOutputStream(client.getOutputStream());
				byte[] hello = "Hello, Java Socket".getBytes();
				dataOS.write(hello, 0, hello.length);
				dataOS.close();
				client.close();
			}
		} catch (Exception e) {
			System.out.println("������run���������쳣��");
//			e.printStackTrace();
		}
	}

	/**
	 * Note������һ��Server���˿ں�Ϊ50000��
	 */
	public static void main(String[] args) {
		try {
			Service service = new Service(50000);
			service.start();
		} catch (IOException e) {
			System.out.println("���������������쳣��");
//			e.printStackTrace();
		}
	}
}
	