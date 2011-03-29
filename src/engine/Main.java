package engine;

import java.util.ArrayList;

public class Main {
	public static GameIF game;
	public static Scene  scene;
	public static Camera camera;
		
	public static boolean running = false;

	public static long    frame   = 0;
	public static double  fps	   = 60.0;
	public static double  deltaTime = 1.0 / 60.0;
	
	public static void setFPS(double _fps){
		fps = _fps;
		deltaTime = 1.0/fps;
	}
	public static void exit(){
		running = false;
	}
	public static void runStart(){
		running = true;
		if(game != null){
			game.OnGameStart();
		}
	}
	public static void runFrame(){
		if(game != null){
			game.OnFrameStart();
			if(scene != null){
				scene.runFrame();
			}
			game.OnFrameEnd();
			frame++;
		}
	}
	public static void runExit(){
		if(game != null){
			game.OnGameEnd();
		}
	}
	public static void runLoop(){
		runStart();
		while(running){
			if(game != null){
				Dbg.Log("Frame:"+frame);
				runFrame();
				tick(deltaTime);
			}
		}
		runExit();
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
		game = new GameDefault();
		runLoop();
	}
}
