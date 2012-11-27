package com.manus.roldan.fastblockin.pieces;

import org.apache.http.util.ByteArrayBuffer;

import android.graphics.Point;

public class Piece {

	public ByteArrayBuffer mPieceGrid = new ByteArrayBuffer(16);

	public Point getPoint(int i)
	{
		return new Point(i / 4, i % 4);
	}
	public int getTextureId(int i)
	{
		return mPieceGrid.byteAt(i);
	}
	public int getTextureId(int x,int y)
	{
		return mPieceGrid.byteAt(x + (4 * y));
	}
	
}
