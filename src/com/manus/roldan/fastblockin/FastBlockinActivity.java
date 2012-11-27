package com.manus.roldan.fastblockin;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.http.util.ByteArrayBuffer;

import com.manus.roldan.fastblockin.pieces.GameGrid;
import com.manus.roldan.fastblockin.pieces.PieceGrid;
import com.manus.roldan.fastblockin.protocol.ByteTypes;
import com.manus.roldan.fastblockin.protocol.Client;
import com.manus.roldan.fastblockin.protocol.ClientEvents;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.SystemClock;

import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

public class FastBlockinActivity extends Activity {
	Client client;
    OpenGLRenderer glRenderer;

    GestureDetector gestureDetector;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // (NEW)
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//        WindowManager.LayoutParams.FLAG_FULLSCREEN); // (NEW)
        gestureDetector = new GestureDetector(this, new GestureListener());

        glRenderer = new OpenGLRenderer(this);
        
        glRenderer.mGameGrid = new GameGrid(8,2);
        byte[] t = new byte[]
        		{
        		3,3,0,0,0,0,0,0,
        		3,3,0,0,1,1,1,1
        		};
       //glRenderer.mGameGrid.y = t.length / 10;
        glRenderer.mGameGrid.mGrid = new ByteArrayBuffer(t.length);
        glRenderer.mGameGrid.mGrid.append(t,0,t.length);
        
        setContentView(R.layout.game);
        GLSurfaceView view = (GLSurfaceView) findViewById(R.id.surface);
   		view.setRenderer(glRenderer);
   		//setContentView(view);
			try {
				client = new Client(new ClientEvents() {
					
					@Override
					public void onDataArrival(ByteArrayBuffer buffer) {
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub

								// TODO Auto-generated method stub
								Builder b = new Builder(FastBlockinActivity.this);
								b.setMessage("Nuevo mensaje!!!!!!");
								b.show();
							}
						});
					}

					@Override
					public void onPieceGrid(PieceGrid pieceGrid) {
						// TODO Auto-generated method stub
						glRenderer.mPieceGrid = pieceGrid;
					}

					@Override
					public void onGameGrid(GameGrid gameGrid) {
						// TODO Auto-generated method stub
						glRenderer.mGameGrid = gameGrid;
						
					}
					@Override
					public void onKick(final String message) {
						// TODO Auto-generated method stub
						runOnUiThread(new Runnable() {
						
								@Override
								public void run() {
								if(message != "")
								{
									Builder b = new Builder(FastBlockinActivity.this);
									b.setMessage(message);
									b.setOnCancelListener(new OnCancelListener() {
										
										@Override
										public void onCancel(DialogInterface dialog) {
											// TODO Auto-generated method stub
											finish();
										}
									});
									b.create().show();
								}
								else
								{
									finish();
								}
							}
						});
						client.close();
						
					}
					@Override
					public void connectionLost() {
						// TODO Auto-generated method stub
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub

								FastBlockinActivity.this.connectionLost();
							}
						});
					}

				});
				if(getIntent().hasExtra("ip"))
				{
					client.startConection(getIntent().getStringExtra("ip"), 3553);
				}
				else
				{
					client.startConection();
				}
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block

				connectionLost();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				connectionLost();

			}
			
			//client.sendString("Hola servidor!");
   		//client.sendString("Hola!!");
   		
    }
    void connectionLost()
    {

		Builder b = new Builder(FastBlockinActivity.this);
		b.setMessage("No hay conexion con el servidor!");
		b.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		b.create().show();
    }
    int offsetX,offsetY;
    boolean moved = false;
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	// TODO Auto-generated method stub
    	gestureDetector.onTouchEvent(event);
    	
//    	Display display = getWindowManager().getDefaultDisplay(); 
//    	int width = display.getWidth();
//    	int height = display.getHeight();
    	if(event.getAction() == MotionEvent.ACTION_MOVE)
    	{
    		//event.offsetLocation(-offsetX, 0);
    		if(true)
    		{
    			
		    	if (offsetX - event.getX() < -glRenderer.SQUARE_WIDTH)
		    	{
		    		if(glRenderer.mPieceGrid.x < 9)
		    		{
		    			//glRenderer.mPieceGrid.x++;
			    		move(ByteTypes.BYTE_MOVE_RIGHT);
			    		setTimer();
			    		offsetX = (int) event.getX();
		    		}
		    		//move(ByteTypes.BYTE_MOVE_RIGHT);
		    	}
		    	else if (offsetX - event.getX()  > glRenderer.SQUARE_WIDTH)
		    	{
		    		if(glRenderer.mPieceGrid.x > -1)
		    		{
		    			//glRenderer.mPieceGrid.x--;
			    		move(ByteTypes.BYTE_MOVE_LEFT);
			    		offsetX = (int) event.getX();
			    		setTimer();
		    		}
		    		//offsetX = (int) event.getX();
	
		    	}
		    	moved = true;
    		}
//	    	if(offsetX == 0)
//	    	{
//	    	}
    	}
    	if(event.getAction() == MotionEvent.ACTION_DOWN)
    	{
    		offsetX = (int) event.getX();
    	}
    	((TextView)findViewById(R.id.textView1)).setText(" X = " + event.getX() + " O = " + (offsetX - event.getX()));
    	return super.onTouchEvent(event);
    }

    void move(byte loaction)
    {
    	byte[] buffer = new byte[]
    		{
    			ByteTypes.BYTE_MOVE,
    			loaction,
    			ByteTypes.BYTE_END
    		};
    	
    	client.sendByte(buffer);
    	
    }
    void rotate(byte loaction)
    {
    	byte[] buffer = new byte[]
    		{
    			ByteTypes.BYTE_ROTATE,
    			loaction,
    			ByteTypes.BYTE_END
    		};
    	
    	client.sendByte(buffer);
    	
    }
    @Override
    protected void finalize() throws Throwable {
    	// TODO Auto-generated method stub
    	
    	super.finalize();
    	client.close();
    }
    
    
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

    	boolean press;
    	@Override
    	public boolean onSingleTapUp(MotionEvent e) {
    		// TODO Auto-generated method stub
    		return super.onSingleTapUp(e);
    	}
    	
        @Override
        public boolean onDown(MotionEvent e) {
        	press = false;
            return true;
        }
        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
//            float x = e.getX();
//            float y = e.getY();

    		//rotate(ByteTypes.BYTE_MOVE_LEFT);
        	move(ByteTypes.BYTE_MOVE_DOWN);
            return true;
        }
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
        		float distanceX, float distanceY) {
        	// TODO Auto-generated method stub
        	
        	if(!press)
        	{
	        	if(distanceY < -40)
	        	{
	
	        		rotate(ByteTypes.BYTE_MOVE_LEFT);
	        		press = true;
	        		return true;
	        	}
	        	if(distanceY > 40)
	        	{

	        		rotate(ByteTypes.BYTE_MOVE_RIGHT);
	        		press = true;
	        		return true;
	        	}
	        	
        	}
        	return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }
    
    long mTimer;
    boolean timer(int time)
    {
    	if(SystemClock.currentThreadTimeMillis() - mTimer > time)
    	{
    		//mTimer = SystemClock.currentThreadTimeMillis();
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    void setTimer()
    {
		mTimer = SystemClock.currentThreadTimeMillis();
    }

}