package com.manus.roldan.fastblockin;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.http.util.ByteArrayBuffer;

import com.manus.roldan.fastblockin.pieces.PieceGrid;
import com.manus.roldan.fastblockin.protocol.Client;
import com.manus.roldan.fastblockin.protocol.ClientEvents;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;

import android.opengl.GLSurfaceView;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.TextView;

public class FastBlockinActivity extends Activity {
	Client client;
    OpenGLRenderer glRenderer;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // (NEW)
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//        WindowManager.LayoutParams.FLAG_FULLSCREEN); // (NEW)
        glRenderer = new OpenGLRenderer(this);
        glRenderer.mPieceGrid = new PieceGrid();
        glRenderer.mPieceGrid.x = 0;
        glRenderer.mPieceGrid.y = 20;
        byte[] t = new byte[]
        		{
        			1, 0, 0, 0,
        			1, 0, 0, 0,
        			1, 0, 0, 0,
        			1, 0, 0, 0,
        		};
        glRenderer.mPieceGrid.mPieceGrid = new ByteArrayBuffer(16);
        glRenderer.mPieceGrid.mPieceGrid.append(t,0,t.length);
        
        setContentView(R.layout.main);
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
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block

				connectionLost();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				connectionLost();

			}
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
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	// TODO Auto-generated method stub
    	Display display = getWindowManager().getDefaultDisplay(); 
    	int width = display.getWidth();
    	int height = display.getHeight();

    	if (event.getX() > width / 2)
    	{
    		
    	}
    	((TextView)findViewById(R.id.textView1)).setText(" X = " + event.getX() + " Y = " + event.getY());
    	return super.onTouchEvent(event);
    }
}