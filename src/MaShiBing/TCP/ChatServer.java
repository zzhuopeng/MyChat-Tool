package MaShiBing.TCP;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
	boolean started = false;
	ServerSocket ss = null;
	//保存所有连接的客户端，便于广播消息。
	List<Client> clients = new ArrayList<Client>();

	/**
	 * Note:新建一个ChatServer，并启动。
	 */
	public static void main(String[] args) {
		ChatServer myServer = new ChatServer();
		myServer.start();
	}

	public void start() {
		int id=1;
		try {
			// 服务器监听端口6666
			ss = new ServerSocket(6666);
			started = true;
			System.out.println("服务器已经开启。");
		} catch (BindException e) {
			System.out.println("新建ServerSocket类失败，请检查端口是否被占用。");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			while (started) {
				Socket s = ss.accept();// 此方法会堵塞线程。等待客户端Socket连接。
				Client c = new Client(s);
				c.setID(id++);
				System.out.println(c.getID()+"号"+"客户端已连接。");
				new Thread(c).start();//当线程start时，实现了Runnable接口的Client对象c中的run方法启动。
				clients.add(c);//将c添加到Client列表
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ss.close();//关闭客户端socket
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Note:Runnable 接口应该由那些打算通过某一线程执行其实例的类来实现。类必须定义一个称为 run 的无参数方法。
	 */
	class Client implements Runnable {
		private Socket s;
		private DataInputStream dis = null;
		private DataOutputStream dos = null;
		private boolean Connected = false;
		private int ID;//为每个客户端加一个ID
		
		/**
		 * Note:获得socket和输入输出流。
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
		
		//获得客户端ID
		public int getID(){
			return ID;
		}
		
		//设置客户端ID
		public void setID(int id){
			this.ID = id;
		}
		

		public void send(String str) {
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				clients.remove(this);
				System.out.println(getID()+"号"+"客户端退出了，已将其从List中删除。");
			}
		}

		public void run() {
			try {
				while (Connected) {
					//从输入流dis 中读取用 UTF-8 修改版格式编码的 Unicode 字符格式的字符串；然后以 String 形式返回此字符串。可能会产生三种异常。
					String str = dis.readUTF();//堵塞线程
					//System.out.println(getID()+"号说："+str);
					//给Clients列表中所有的Client广播消息。
					for (int i = 0; i < clients.size(); i++) {
						Client c = clients.get(i);
						c.send(getID()+"号说："+str);
					}
				}
			} catch (EOFException e) {
				System.out.println(getID()+"号"+"客户端关闭。");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				//如果出现异常，把输入输出流和socket关闭
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
