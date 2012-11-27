package com.manus.roldan.fastblockin;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.manus.roldan.fastblockin.pieces.GameGrid;
import com.manus.roldan.fastblockin.pieces.PieceException;
import com.manus.roldan.fastblockin.pieces.PieceGrid;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

public class OpenGLRenderer implements Renderer {
	Activity context;
	long loops = 0;
	long timer = 0;
	int SQUARE_WIDTH;
	int SQUARE_HEIGHT;
	float factor_width;
	float factor_height;

	PieceGrid mPieceGrid = null;
	GameGrid mGameGrid = null;
	
	public OpenGLRenderer(Activity context) {
		// TODO Auto-generated constructor stub
		
		
		this.context = context;
//		View surface = context.findViewById(R.id.container_relative);
//		surface.measure(0, 0);
		
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
	
	
	
	
	gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
	gl.glDisable(GL10.GL_DEPTH_TEST);
	gl.glDisable(GL10.GL_DITHER);
	gl.glDisable(GL10.GL_LIGHTING);

	gl.glEnable(GL10.GL_TEXTURE_2D);
	gl.glClearColor(0, 0, 0, 1);
	gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

	final int FLOAT_SIZE = 4;
	final int CHAR_SIZE = 2;
	mTexCoordBuffer = ByteBuffer.allocateDirect(FLOAT_SIZE * sTexCoordValues.length)
		.order(ByteOrder.nativeOrder()).asFloatBuffer();
	mIndexBuffer = ByteBuffer.allocateDirect(CHAR_SIZE * sIndexValues.length)
		.order(ByteOrder.nativeOrder()).asCharBuffer();

	for (int i = 0; i < sTexCoordValues.length; ++i)
		mTexCoordBuffer.put(i, sTexCoordValues[i]);
	for (int i = 0; i < sIndexValues.length; ++i)
		mIndexBuffer.put(i, sIndexValues[i]);

	
	
	gl.glGenTextures(sTextureIDWorkspace.length, sTextureIDWorkspace, 0);

	loadTexture(gl, R.drawable.block_blue);
	loadTexture(gl, R.drawable.block_green);
	loadTexture(gl, R.drawable.block_orange);
	loadTexture(gl, R.drawable.block_purple);
	loadTexture(gl, R.drawable.block_red);
	loadTexture(gl, R.drawable.block_sea);
	loadTexture(gl, R.drawable.block_yellow);
	
	
	Runtime r = Runtime.getRuntime();
	r.gc();

	//mTextureID = textureID;
}

public void onSurfaceChanged(GL10 gl, int width, int height) {

	factor_width = (float) width / (10.0f * 16f);
	factor_height = (float) height / (20.0f * 16f);
	
	SQUARE_WIDTH = (int) (factor_width * 16f);
	SQUARE_HEIGHT = (int) (factor_height * 16f);
	
	sVertexValues = new float[] {
			
			-SQUARE_WIDTH/2, -SQUARE_HEIGHT/2, 0,
			 SQUARE_WIDTH/2, -SQUARE_HEIGHT/2, 0,
			-SQUARE_WIDTH/2,  SQUARE_HEIGHT/2, 0,
			 SQUARE_WIDTH/2,  SQUARE_HEIGHT/2, 0,
		};

	
	mVertexBuffer = ByteBuffer.allocateDirect(4 * sVertexValues.length)
		.order(ByteOrder.nativeOrder()).asFloatBuffer();
	for (int i = 0; i < sVertexValues.length; ++i)
		mVertexBuffer.put(i, sVertexValues[i]);
	
	
	//line = new PieceI(SQUARE_WIDTH, SQUARE_HEIGHT,mVertexBuffer,mTexCoordBuffer, mIndexBuffer);
	
	gl.glViewport(0, 0, width, height);
	gl.glMatrixMode(GL10.GL_PROJECTION);
	gl.glLoadIdentity();
	gl.glOrthof(0, width, 0, height, 0, 1);
	gl.glShadeModel(GL10.GL_FLAT);
	gl.glEnable(GL10.GL_BLEND);
	gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
	gl.glEnable(GL10.GL_TEXTURE_2D);
}

public void onDrawFrame(GL10 gl) {
	
	//boolean rand = ((CheckBox)context.findViewById(R.id.random_cbx)).isChecked();
	gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	gl.glMatrixMode(GL10.GL_MODELVIEW);
	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

	try {
		if(mGameGrid != null)
		{
			mGameGrid.draw(gl,SQUARE_WIDTH,SQUARE_HEIGHT,mVertexBuffer,mTexCoordBuffer,mIndexBuffer);
		}
	} catch (PieceException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	try {
		if(mPieceGrid != null)
		{
			mPieceGrid.draw(gl,SQUARE_WIDTH,SQUARE_HEIGHT,mVertexBuffer,mTexCoordBuffer,mIndexBuffer);
		}
		
		
	} catch (PieceException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	
	loops++;
	if(SystemClock.uptimeMillis() - timer > 1000)
	{
		timer = SystemClock.uptimeMillis();
		//final int fps = (int) loops;
		context.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub

				((TextView)context.findViewById(R.id.fps_lbl)).setText(loops + " FPS");
			}
		});
		loops = 0;
		
	}
	
}
void loadTexture(GL10 gl, int resource)
{
	int textureID = sTextureIDWorkspace[mTextureID++];
	gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID);

	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
	gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);

	Bitmap bitmap;
	InputStream is = context.getResources().openRawResource(resource);
	try {
		bitmap = BitmapFactory.decodeStream(is, null, sBitmapOptions);
	} finally {
		try {
			is.close();
		} catch (IOException e) {
			// Ignore
		}
	}

	GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
	bitmap.recycle();

	int error = gl.glGetError();
	if (error != GL10.GL_NO_ERROR)
		Log.e(TAG, "Error loading textures: " + error);

}

private static final String TAG = "OpenGLRenderer";
private float[] sVertexValues;
private static final float[] sTexCoordValues = new float[] {
	0, 1,
	1, 1,
	0, 0,
	1, 0,
};
private static final char[] sIndexValues = new char[] {
	0, 1, 2,
	1, 2, 3,
};
private static final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
private static final int[] sTextureIDWorkspace = new int[7];

private int mTextureID = 0;
private FloatBuffer mVertexBuffer;
private FloatBuffer mTexCoordBuffer;
private CharBuffer mIndexBuffer;
}
