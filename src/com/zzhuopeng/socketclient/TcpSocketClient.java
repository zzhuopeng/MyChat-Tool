package com.zzhuopeng.socketclient;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.activity.ActivityCompletedException;


public class TcpSocketClient{

	public static void main(String[] args) {
		try {
			// ����һ��Socket����ָ���������˵�IP��ַ�Ͷ˿ں�
			Socket socket = new Socket("100.114.43.230", 4567);
			//ʹ��InputStream�����ȡӲ���ϵ��ļ�
			InputStream inputStream = new FileInputStream("C://haha.txt");
			//��Socket�����еõ�OutputStream����׼����OutputStream������д������
			OutputStream outputStream = socket.getOutputStream();
			byte data[] = new byte[1024*4];
			int i = 0;//�ֽڳ���
			while ((i = inputStream.read(data)) != 1) {
				//��InputStream�����ж�ȡ���ݲ�д�뵽 OutputStream������
				outputStream.write(data, 0, i);
			}
			outputStream.flush();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

}
