package com.manus.roldan.fastblockin.pieces;

import org.apache.http.util.ByteArrayBuffer;

import android.graphics.Point;

public class Piece {

	private int width;
	
	public ByteArrayBuffer mGrid = new ByteArrayBuffer(1024);
	
	public Piece(int cell_width) {
		// TODO Auto-generated constructor stub
		width = cell_width;
	}
	public Point getPoint(int i)
	{
		return new Point(i / width, i % width);
	}
	public int getTextureId(int i)
	{
		if(i >= mGrid.length())
			return 0;
		else
			return mGrid.byteAt(i);
	}
	public int getTextureId(int x,int y)
	{
		return getTextureId(x + (width * y));
	}
	
}
