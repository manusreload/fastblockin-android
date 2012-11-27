package com.manus.roldan.fastblockin.pieces;

import java.nio.CharBuffer;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

public class PieceGrid extends Piece {
	
	
	int GRID_WIDTH = 4;
	int GRID_HEIGHT = 4;
	int SCREEN_WIDTH = 10;
	int SCREEN_HEIGHT = 20;
	public int x;
	public int y;
		
	public PieceGrid() {
		// TODO Auto-generated constructor stub
	}

	public void draw(GL10 gl, int SQUARE_WIDTH, int SQUARE_HEIGHT
			,FloatBuffer mVertexBuffer,FloatBuffer mTexCoordBuffer,CharBuffer mIndexBuffer) throws PieceException
	{
		if(x < 0 || x > SCREEN_WIDTH)
			throw new PieceException("X has exceeded the screen bounds!");
		if(y < 0 || y > SCREEN_HEIGHT)
			throw new PieceException("Y has exceeded the screen bounds!");

		for(int i = 0; i < GRID_WIDTH; i++)
		{
			for(int j = 0 ; j < GRID_HEIGHT; j ++)
			{
				if(getTextureId(i,j) != 0)
				{
					gl.glBindTexture(GL10.GL_TEXTURE_2D, getTextureId(i,j));
						
					gl.glPushMatrix();
					gl.glLoadIdentity();
					
					gl.glTranslatef(((x - i + 1) * SQUARE_WIDTH ) - (SQUARE_WIDTH / 2), ((y - j) * SQUARE_HEIGHT ) - (SQUARE_HEIGHT / 2), 0);
								
					gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
					gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexCoordBuffer);
					gl.glDrawElements(GL10.GL_TRIANGLES, mIndexBuffer.length(), GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
					gl.glPopMatrix();
				}
			}
		}
	}
}
