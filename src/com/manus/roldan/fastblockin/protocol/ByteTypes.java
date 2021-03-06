package com.manus.roldan.fastblockin.protocol;

public class ByteTypes 
{
	public static final byte BYTE_NULL = 0x00;
	public static final byte BYTE_SEPARATOR = 0x01;
	public static final byte BYTE_STRING = 0x02;
	public static final byte BYTE_ENDCONDITIONAL = 0x03;
	public static final byte BYTE_END = 0x04;
	public static final byte BYTE_CANCEL = 0x05;
	public static final byte BYTE_MAX = 0x7F;

	public static final byte BYTE_PIECE_GRID = 	0x10;
	public static final byte BYTE_GAME_GRID = 	0x11;
	public static final byte BYTE_MOVE =		0x12;
	public static final byte BYTE_ROTATE =		0x13;
	public static final byte BYTE_GAME_END =	0x14;

	public static final byte BYTE_MOVE_DOWN =		0x00;
	public static final byte BYTE_MOVE_UP =			0x01;
	public static final byte BYTE_MOVE_LEFT =		0x02;
	public static final byte BYTE_MOVE_RIGHT =		0x03;
	
	public static final byte BYTE_KICK = 0x30;
}
