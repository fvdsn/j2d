package engine;

import java.util.Random;

import math.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

/**
 * This class represents the main loop of the game.
 * 
 * You start the game by calling Main.runLoop()
 * Before calling that function, you must assign game, camera, and scene
 * to instances that you defined.
 * 
 * @author Frédéric van der Essen.
 *
 */
public class Main {
	public static Game game;
	public static Scene  scene;
	public static Camera camera;
	public static Random rng = new Random();
		
	public static boolean running = false;
	private static float	restartTime = -1.0f;
	/** The current frame being rendered, starts at 0 */
	public static long    frame   = 0;	
	/** The time since the start of the game, pauses included, in seconds */
	public static float	  time	  = 0.0f;
	/** The time since the start of the game, pauses included, in milliseconds */
	public static long    timeMillis = 0;
	/** The unix time of the game start, milliseconds */
	public static long	  startTime	 = 0;
	/** The number of frames update per second */
	public static float   fps	   = 60.0f;
	/** The duration of a frame in seconds */
	public static float   deltaTime = 1.0f / fps;
	
	public static int	  resolutionX = 800;
	public static int	  resolutionY = 600;
	
	public static void setFPS(float _fps){
		fps = _fps;
		deltaTime = 1.0f/fps;
	
	}
	/** Exits the game at the end of the current frame */
	public static void exit(){
		running = false;
	}
	protected static void runInit(){
		try{
			Display.setDisplayMode(new DisplayMode(resolutionX,resolutionY));
			Display.setFullscreen(true);
			Display.create();
		}catch (LWJGLException e){
			Dbg.FatalError("Could Not Create Display",e.toString());
		  	e.printStackTrace();
		  	System.exit(0);
		}
		running = true;
	}
	/** This method is called when the game is started before the first frame,
	 *  everything that must be done before the first frame is done here */
	protected static void runStart(){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, resolutionX, resolutionY, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		running = true;
		startTime = System.currentTimeMillis();
		time = 0.0f;
		timeMillis = 0;
		restartTime = -1.0f;
		frame = 0;
		if(game != null){
			game.OnGameStart();
		}
	}
	/** This method is called each frame. Everything that happens in a frame starts
	 * from this method.
	 */
	protected static void runFrame(){
		long sTime,eTime,fsTime;
		if(camera == null ){
			return;
		}
		fsTime = System.currentTimeMillis();
		timeMillis = System.currentTimeMillis() - startTime;
		time = (float)(timeMillis / 1000.0f);
		Statistics.resetFrameStats();
		game.OnFrameStart();
		
		camera.OnRenderSetup();
		camera.backgroundSetup();
		camera.projectionSetup();
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		sTime = System.currentTimeMillis();
		if(scene != null){
			scene.update();
		}
		eTime = System.currentTimeMillis();
		Statistics.updateTime = (int)(eTime - sTime);
		
		sTime = System.currentTimeMillis();
		camera.OnRenderStart();
		Dbg.drawRef(camera.getMousePos());
		if(scene != null){
			scene.draw();
		}
		camera.OnRenderEnd();
		eTime = System.currentTimeMillis();
		Statistics.drawTime = (int)(eTime - sTime);
		Statistics.frameTime = (int)(eTime - fsTime);
		Statistics.fps = 1000/Statistics.frameTime;
		Statistics.waitTime = (int)(deltaTime*1000) - Statistics.frameTime;
		game.OnFrameEnd();
		Display.update();
		frame++;
	}
	/** This method is called after the last frame before the game exits. Everything
	 * that must be done before the game exits must be called from there 
	 */
	protected static void runEnd(){
		if(game != null){
			game.OnGameEnd();
		}
	}
	protected static void runExit(){
		Display.destroy();
	}
	/** This methods starts and runs the game. It returns only when the game exits */
	public static void run(){
		runInit();
		while(running){
			if(game == null){
				System.err.println("FATAL ERROR: Main.runLoop(): no game provided");
				System.exit(1);
			}
			runStart();
			while(running && (restartTime < 0.0f || time < restartTime)){
				runFrame();
				if(Display.isCloseRequested()){
					Dbg.Log("Closing Main Window and exiting game.\nGood Bye!\n");
					running = false;
					continue;
				}
				tick(deltaTime);
			}
			runEnd();
		}
		runExit();
	}
	public static void restart(){
		restartTime = time;
	}
	public static void restart(float delay){
		restartTime = time + delay;
	}
	private static long sysTime = 0;
	private static void tick(double time){
		long tickDuration = (long)(time * 1000.0);
		long currTime = System.currentTimeMillis();
		long nextTime = sysTime + tickDuration;
		if(currTime >= nextTime){
			sysTime = currTime;
			return;
		}else{
			while(currTime < nextTime){
				try {
					Thread.currentThread().sleep(nextTime - currTime);
					currTime = System.currentTimeMillis();
				} catch (InterruptedException e) {
					System.err.print("ERROR: Time->tick() : interrupted..");
					currTime = System.currentTimeMillis();
				}
			}
		}
		sysTime = currTime;
		return;
	}
}
