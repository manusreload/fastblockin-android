package com.manus.roldan.fastblockin.protocol;

import org.apache.http.util.ByteArrayBuffer;

import com.manus.roldan.fastblockin.pieces.PieceGrid;

public interface ClientEvents {

	public void onDataArrival(ByteArrayBuffer buffer);
	public void onPieceGrid(PieceGrid pieceGrid);
	public void onKick(String message);
	public void connectionLost();
}
