//http://blog.csdn.net/jia20003/article/details/8195226
package tcp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Service extends Thread {
	private ServerSocket serverSocket;

	/**
	 * Note：HelloSevice类构造函数，port为服务器监听端口号。
	 */
	public Service(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}

	/**
	 * Note:Thread子类在执行start()方法的时候，自动运行类中的run方法。
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
			System.out.println("服务器run方法出现异常！");
			// e.printStackTrace();
		}
	}

	/**
	 * Note：启动一个Server，端口号为50000。
	 */
	public static void main(String[] args) {
		try {
			Service service = new Service(50000);
			service.start();
		} catch (IOException e) {
			System.out.println("创建服务器出现异常！");
			// e.printStackTrace();
		}
	}
}
