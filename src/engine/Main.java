package engine;

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
	public static GameIF game;
	public static Scene  scene;
	public static Camera camera;
		
	public static boolean running = false;

	public static long    frame   = 0;
	public static float   fps	   = 60.0f;
	public static float   deltaTime = 1.0f / fps;
	
	public static int	  resolutionX = 800;
	public static int	  resolutionY = 600;
	
	public static void setFPS(float _fps){
		fps = _fps;
		deltaTime = 1.0f/fps;
	
	}
	public static void exit(){
		running = false;
	}
	public static void runStart(){
		try{
			Display.setDisplayMode(new DisplayMode(resolutionX,resolutionY));
			Display.create();
		}catch (LWJGLException e){
			Dbg.FatalError("Could Not Create Display",e.toString());
		  	e.printStackTrace();
		  	System.exit(0);
		}
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, resolutionX, resolutionY, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		running = true;
		if(game != null){
			game.OnGameStart();
		}
	}
	public static void runFrame(){
		if(camera == null ){
			return;
		}
		game.OnFrameStart();
		
		camera.OnRenderSetup();
		camera.backgroundSetup();
		camera.projectionSetup();
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		if(scene != null){
			scene.update();
		}
		
		camera.OnRenderStart();
		
		if(scene != null){
			scene.draw();
		}
		
		camera.OnRenderEnd();
		
		Dbg.drawRef(new Vec2(0,0),100);
		
		Color.blue.glSet();
		Draw.line(new Vec2(0,0), new Vec2(100,50));
		Draw.point(new Vec2(30,30));
		Draw.box(new BBox(100,50,45,30));
		Draw.boxFilled(new BBox(100,50,40,25));
		Draw.disc(new Vec2(-100,50), 25);
		Draw.circle(new Vec2(-100,50), 35);
		Draw.circle(new Vec2(-100,50), 45);

		game.OnFrameEnd();
		Display.update();
		frame++;
	}
	public static void runExit(){
		if(game != null){
			game.OnGameEnd();
		}
	}
	public static void runLoop(){
		if(game == null){
			System.err.println("FATAL ERROR: Main.runLoop(): no game provided");
			System.exit(1);
		}
		runStart();
		while(running){
			runFrame();
			if(Display.isCloseRequested()){
				Dbg.Log("Closing Main Window and exiting game.\nGood Bye!\n");
				running = false;
				continue;
			}
			tick(deltaTime);
		}
		runExit();
		Display.destroy();
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
	public static void main(String[] args){
		camera = new Camera(new Vec2()); /*{
			public void OnRenderSetup(){
				this.setScale(1.0f + Main.frame * Main.deltaTime);
			}
		};*/
		Dbg.Log(camera);
		game = new GameDefault();
		scene = new Scene();
		Ent e = new Ent();
		Ent e2 = new Ent();
		e.addChild(e2);
		scene.addEntity(e);

		runLoop();
	}
}
