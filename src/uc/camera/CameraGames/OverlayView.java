package uc.camera.CameraGames;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;



public class OverlayView extends View {
	private class UpdateTimerTask extends TimerTask{
		@Override
		public void run() {
			update();
			postInvalidate();
		}
		
	}
	
	long step=1;
	GameLogic myGameLogic;
	Timer timer;
	SensorsOutput mySensors;

	
	//I'd rather keep this closer to game logic, but needs to be able to load images from resources
	//Into mEnemies. Maybe pass a context to GameLogic instead?
	EnemyPositions mEnemies; 
	Bitmap bmp;
	public void LoadLevel(){ //For the time being do some static initializing
		Log.d("LoadLevel", "Starting LoadLevel, in OverlayView");
		mEnemies = new EnemyPositions(mySensors);
		Log.d("LoadLevel", "Loading bitmaps");
		
		bmp = BitmapFactory.decodeResource(getResources(),R.drawable.skull);
		Log.d("LoadLevel", "Loaded bmp is null?"+bmp.equals(null));
		
		Log.d("LoadLevel", "Creating enemies");
		Enemy enemyA= new Enemy(10,0,10,999,bmp);
		Enemy enemyB= new Enemy(0,0,-10,999,bmp);
		Log.d("LoadLevel", "Got enemy? " + enemyA.equals(null));
		mEnemies.add(enemyA);//mEnemies.add(enemyB);
		Log.d("LoadLevel", "End of LoadLevel, in OverlayView");
	}
	
	
	public OverlayView(Context c,SensorsOutput so){
		super(c);
		mySensors=so;
		
		//TODO move this to some level loader
		LoadLevel();
		
		
		
		
		myGameLogic=new GameLogic(mySensors,mEnemies);
		timer = new Timer();
		timer.schedule(new  UpdateTimerTask(), 0, step);
		
		this.invalidate();	
		

		
	}
	public void pause(){
		timer.cancel();
	}
	public void resume(){
		timer = new Timer();
		timer.schedule(new  UpdateTimerTask(), 0, step);		
	}
	
	
	
	public void update(){
		myGameLogic.advance(step);
		//this.invalidate();
	}
	
	public void setSensorsOutput(SensorsOutput so){ //WHY DO I NEED TO DO THIS TWICE? (CONST NOT ENOUGH!)
		mySensors=so;
		myGameLogic=new GameLogic(mySensors,mEnemies);
	}
	
	
	
	protected void onDraw(Canvas c){
		this.requestLayout();
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		
		//c.drawText("View dimension:"+this.getWidth()+"x"+this.getHeight(), 0, 50, paint);
		//c.drawText("View mesured dimension:"+this.getMeasuredWidth()+"x"+this.getMeasuredHeight(), 00, 60, paint);
		//c.setViewport(this.getWidth(), this.getWidth());
		//c.drawText("Canvas dimension in view:"+c.getWidth()+"x"+c.getHeight(), 0, 60, paint);
		/*super.onDraw(c);
		c.drawText("Rotation x:"+mySensors.rotation_x+"\t y:"+mySensors.rotation_y+"\t z:"+mySensors.rotation_z, 5, 50, p);
		c.drawText("Acceleration x:"+mySensors.acceleration_x+"\t y:"+mySensors.acceleration_y+"\t z:"+mySensors.acceleration_z, 5, 70, p);
		*/
		
		myGameLogic.draw(c);
	}

}
