//http://blog.csdn.net/jia20003/article/details/8195226
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
		System.out.println("start user = " + userName);
		try {
			DataInputStream bufferedReader = new DataInputStream(userSocket.getInputStream());
			byte[] cbuff = new byte[256];
			char[] tbuff = new char[256];
			int size = 0;
			int byteCount = 0;
			int length = 0;
			while (true) {
				if ((size = bufferedReader.read(cbuff)) > 0) {
					char[] temp = convertByteToChar(cbuff, size);
					length = temp.length;
					if ((length + byteCount) > 256) {
						length = 256 - byteCount;
					}
					System.arraycopy(temp, 0, tbuff, byteCount, length);
					byteCount += size;
					if (String.valueOf(tbuff).indexOf(ChatServer.END_FLAG) > 0) {
						String receivedContent = String.valueOf(tbuff);
						int endFlag = receivedContent.indexOf(ChatServer.END_FLAG);
						receivedContent = receivedContent.substring(0, endFlag);
						String[] keyValue = receivedContent.split("#");
						if (keyValue.length > 1) {
							server.dispatchMessage(keyValue, userName);
						}
						byteCount = 0;
						clearTempBuffer(tbuff);
					}
				}
			}
		} catch (IOException e) {
			System.out.println("������run���������쳣��");
			// e.printStackTrace();
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
			// e.printStackTrace();
		}
	}
}
