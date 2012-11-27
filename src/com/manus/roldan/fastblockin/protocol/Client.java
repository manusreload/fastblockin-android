package com.manus.roldan.fastblockin.protocol;

import com.manus.roldan.fastblockin.pieces.GameGrid;
import com.manus.roldan.fastblockin.pieces.PieceGrid;
import com.manus.roldan.fastblockin.protocol.ByteTypes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;
public class Client implements Runnable
{
	private Socket mSocket;
	
	private String mIp;
	private int mPort;
	
	public static Client mInstance;
	private static final String TAG = "Client";

	private final ClientEvents event;
	private boolean mConected;
	//public void onDataArrive(Byte data[]);
	
	public Client(ClientEvents event) throws UnknownHostException, IOException {
		// TODO Auto-generated constructor stub
		//super("83.52.33.147", 3553);
		mIp = "85.53.66.42";
		mPort = 3553;
		this.event = event;
		mInstance = this;
		
		//startConection();
	}
	
	public void close()
	{
		try {
			mSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mConected = false;
		
	}
	public void startConection(String ip,int port) throws IOException
	{
		mIp = ip;
		mPort = port;
		startConection();
	}
	public void startConection() throws IOException
	{
		mSocket = new Socket(mIp,mPort);
		mSocket.getInputStream();
		mConected = true;
		new Thread(this).start();
		
	}
	public void sendString(String string)
	{
		try {
			sendByte(ByteTypes.BYTE_STRING);
			mSocket.getOutputStream().write(string.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void sendByte(byte data)
	{
		try {
			byte[] buffer = new byte[] {data};
			mSocket.getOutputStream().write(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void sendByte(byte[] data)
	{
		try {
			mSocket.getOutputStream().write(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void processMessage(ByteArrayBuffer data[]) throws ProtocolException
	{
		//event.onDataArrival(data);
		while(true)
		{
			int[] offset = {0};
			try {
				byte message = readByte(data, offset);
				if(message > ByteTypes.BYTE_MAX)
				{
					throw new ProtocolException("Message type undefined");
				}
				switch(message)
				{
				case ByteTypes.BYTE_PIECE_GRID:
					PieceGrid mPieceGrid = new PieceGrid();
					mPieceGrid.x = readByte(data, offset)-10;
					mPieceGrid.y = 20 - readByte(data, offset);
					
					for(int i = 0; i < 16; i++)
					{
						mPieceGrid.mGrid.append(readByte(data, offset));
					}
					
					if(mPieceGrid.y > 0)
						event.onPieceGrid(mPieceGrid);
					
					remove(data, offset);
					break;
				case ByteTypes.BYTE_GAME_GRID:

					GameGrid mGameGrid = new GameGrid(readByte(data, offset),readByte(data, offset));
					
					for(int i = 0; i < 10 * 20; i++)
					{
						mGameGrid.mGrid.append(readByte(data, offset));
					}

					event.onGameGrid(mGameGrid);
					
					remove(data, offset);
					break;
				case ByteTypes.BYTE_KICK:
					String str = readString(data, offset);
					event.onKick(str);
					remove(data, offset);
				default:
					throw new ProtocolException("Message type undefined {" + message + "}");
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			InputStream inputStream = mSocket.getInputStream();
			int leng;
			byte[] buffer = new byte[1024];
			ByteArrayBuffer[] data = {new ByteArrayBuffer(1024 * 32)};
			while((leng = inputStream.read(buffer)) != -1 && mConected)
			{
				if(mConected)
				{
					data[0].append(buffer, data[0].length(), leng);
					processMessage(data);
				}
				else
				{
					break;
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "Cannot set InputStream\nNetwork error");
			event.connectionLost();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			event.connectionLost();
		}
	}
	public static void remove(ByteArrayBuffer data[],int[] offset)
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		os.write(data[0].toByteArray(), offset[0] + 1, data[0].length() - offset[0] - 1);
		data[0] = new ByteArrayBuffer(1024 * 32);
		data[0].append(os.toByteArray(),0,os.size());
	}
	public static byte readByte(ByteArrayBuffer data[], int[] offset) throws Exception
	{
		
		if(data[0].length() < offset[0] || data[0].length() == 0)
			throw new Exception("Offset out of bounds");
		return (byte) data[0].byteAt(offset[0] ++);
	}
	public static short readShort(ByteArrayBuffer data[], int[] offset) throws Exception
	{
		int value = readByte(data, offset);
		
		return (short) (value + readByte(data, offset) * 256);
	}
	public static String readString(ByteArrayBuffer data[], int[] offset) throws Exception
	{
		if(readByte(data, offset) == ByteTypes.BYTE_STRING)
		{
			StringBuilder b = new StringBuilder();
			while(true)
			{
				int value = readByte(data, offset);
				if(value == ByteTypes.BYTE_NULL)
				{
					break;
				}
				b.append((char)value);
			}
			return b.toString();
		}
		else
		{
			return "";
		}
	}

	
}
