package com.manus.roldan.fastblockin.protocol;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Protocol {

	public Socket mSocket;
	private String mIp;
	private int mPort;
	
	public Protocol(String ip, int port) throws UnknownHostException, IOException {
		// TODO Auto-generated constructor stub
		mIp = ip;
		mPort = port;
		startConextion();
	}

	void startConextion() throws UnknownHostException, IOException
	{
		mSocket = new Socket(mIp,mPort);
	}
	void startConextion(String ip, int port) throws UnknownHostException, IOException
	{
		mIp = ip;
		mPort = port;
		startConextion();
	}
}
