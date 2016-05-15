package com.zzhuopeng.socketclient;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.activity.ActivityCompletedException;


public class TcpSocketClient{

	public static void main(String[] args) {
		try {
			// 创建一个Socket对象，指定服务器端的IP地址和端口号
			Socket socket = new Socket("100.114.43.230", 4567);
			//使用InputStream对象读取硬盘上的文件
			InputStream inputStream = new FileInputStream("C://haha.txt");
			//从Socket对象中得到OutputStream对象，准备往OutputStream对象里写入数据
			OutputStream outputStream = socket.getOutputStream();
			byte data[] = new byte[1024*4];
			int i = 0;//字节长度
			while ((i = inputStream.read(data)) != 1) {
				//从InputStream对象中读取数据并写入到 OutputStream对象当中
				outputStream.write(data, 0, i);
			}
			outputStream.flush();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

}
