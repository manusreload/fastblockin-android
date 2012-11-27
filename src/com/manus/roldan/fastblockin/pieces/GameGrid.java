package com.manus.roldan.fastblockin.pieces;

public class GameGrid extends PieceGrid{

	public GameGrid(int GRID_WIDTH,int GRID_HEIGHT) {
		// TODO Auto-generated constructor stub
		super(GRID_WIDTH);
		this.GRID_WIDTH = GRID_WIDTH;
		this.GRID_HEIGHT = GRID_HEIGHT;
		x = 0;
		y = GRID_HEIGHT;
	}
}
