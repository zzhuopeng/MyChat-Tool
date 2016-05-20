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

	TextField textField = new TextField();	//文本编辑区
	TextArea textArea = new TextArea();   	//文本显示区
	//新建一个线程，用于接收服务端发过来的数据。
	Thread tRecv = new Thread(new RecvThread());
	
	/**
	 * Note:新建一个ChatClient，并加载客户端界面。
	 */
	public static void main(String[] args) {
		ChatClient myChatClient = new ChatClient();
		myChatClient.launchFrame();
	}

	/**
	 * Note:加载客户端界面。
	 */
	public void launchFrame() {
		setLocation(450, 350);
		this.setSize(400, 400);
		add(textField, BorderLayout.SOUTH);
		add(textArea, BorderLayout.NORTH);
		pack();//调整此窗口的大小，以适合其子组件的首选大小和布局。
		//窗口关闭处理。使用匿名类。
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				disconnect();//清除资源
				System.exit(0);//关闭系统
			}
		});
		
		//设置消息编辑区的监听事件。
		textField.addActionListener(new TextFListener());
		setVisible(true);
		//连接服务器
		connect();

		tRecv.start();
	}
	
	public void connect() {
		try {
			//创建一个流套接字并将其连接到指定主机上的指定端口号。 需要捕获未知异常和IO异常。
			s = new Socket("127.0.0.1", 6666);
			//获得输入输出流。
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
			System.out.println("我已经连上了服务器。");
			Connected = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//当连接断开时，清空资源。
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
	 * Note:文本编辑区监听事件
	 */
	private class TextFListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String str = textField.getText().trim();//返回字符串的副本，忽略前导空白和尾部空白。 
			textField.setText("");

			try {
				//以与机器无关方式使用 UTF-8 修改版编码将一个字符串写入基础输出流。 需要捕获IO异常。
				dos.writeUTF(str);
				dos.flush();//清空此数据输出流。这迫使所有缓冲的输出字节被写出到流中。需要捕获IO异常。
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * Note：新建线程，专门用来接收消息，并打印出来。
	 */
	private class RecvThread implements Runnable {

		public void run() {
			try {
				while (Connected) {
					String str = dis.readUTF();//堵塞线程。
					//在显示窗口显示接收到的数据。
					textArea.setText(textArea.getText() + str + '\n');
				}
			} catch (SocketException e) {
				System.out.println("退出了。");
			} catch (EOFException e) {
				System.out.println("我退出了。");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
