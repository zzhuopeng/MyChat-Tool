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
		try {
			while (true) {
				System.out.println("服务器已经启动，服务器监听端口为：" + serverSocket.getLocalPort() + "\n等待连接......");
				//accep()方法，等待客户端socket连接，此方法会堵塞线程。
				Socket client = serverSocket.accept();
				System.out.println("已经连接" + client.getRemoteSocketAddress());//Remote远程的。
				DataOutputStream dataOS = new DataOutputStream(client.getOutputStream());
				byte[] hello = "Hello, Java Socket".getBytes();
				dataOS.write(hello, 0, hello.length);
				dataOS.close();
				client.close();
			}
		} catch (Exception e) {
			System.out.println("服务器run方法出现异常！");
//			e.printStackTrace();
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
//			e.printStackTrace();
		}
	}
}
	