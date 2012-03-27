package engine;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import math.BBox;
import math.Color;
import math.Vec2;

public class Camera {
	/** The center of the camera, in world space */
	protected Vec2 	p;
	/** The pixel to world unit ratio: 1 => 1px = 1wu, 10 => 1px = 10wu */
	protected float  	scale;
	/** The near clip distance */
	protected float   zNear = 100;
	/** The far  clip distance */
	protected float 	zFar  = -100;
	/** The BBox of the viewed region in world space */
	protected BBox	   	viewPort;
	/** The background color of the viewport */
	protected Color	bgColor = Color.black;
	
	/** The resolution in pixel of the camera, same as Main.resolutionX, Main.resolutionY */
	protected static Vec2 	resolution;
	
	public static float  	widthToHeight;
	public static float  	heightToWidth;
	
	public Camera(Vec2 pos){
		this.p = pos;
		scale = 1.0f;
		setResolution(new Vec2(Main.resolutionX,Main.resolutionY));

	}
	
	/** Changes the position of the center of the camera in world coordinates */
	public void setPos(Vec2 pos){
		this.p = pos;
		viewPort = new BBox(p.x,
							p.y,
							resolution.x * scale,
							resolution.y * scale);
	}
	/** Returns the position of the center of the camera in world coordinates */
	public Vec2 getPos(){
		return p.dup();
	}
	/** Changes the resolution of the camera. WARNING: this should only be done by the game engine */
	public void setResolution(Vec2 res){
		resolution = res.dup();
		widthToHeight = resolution.y / resolution.x;
		heightToWidth = resolution.x / resolution.y;
		viewPort = new BBox(p.x,
							p.y,
							resolution.x * scale,
							resolution.y * scale);
	}
	/** Returns the resolution of the camera in screen pixels, same as in (Main.resolutionX, Main.resolutionY) */
	public Vec2 getResolution(){
		return resolution.dup();
	}
	/** Changes the scale of the camera: The pixel to world unit: 1 => 1px = 1wu, 10 => 1px = 10wu */
	public void setScale(float scale){
		this.scale = scale;
		viewPort = new BBox(p.x,
							p.y,
							resolution.x * scale,
							resolution.y * scale);
	}
	/** Returns the scale of the camera: The pixel to world unit: 1 => 1px = 1wu, 10 => 1px = 10wu */
	public float getScale(){
		return scale;
	}
	/** Returns a BBox representing the viewed part of the world by the camera, in world coordinates */
	public BBox getViewPort(){
		return viewPort.dup();
	}
	/** Modifies the color of the camera background */
	public void setBgColor(Color c){
		bgColor = c;
	}
	/** Returns the color of the camera's background */
	public Color getBgColor(){
		return bgColor;
	}
	/** Modifies the z buffer near plane distance */
	public void setZNear(float zNear){
		this.zNear = zNear;
	}
	/** Returns the z buffer near plane distance */
	public float getZNear(){
		return zNear;
	}
	/** Modifies the z buffer far plane distance */
	public void setZFar(float zFar){
		this.zFar = zFar;
	}
	/** Returns the z buffer far plane distance */
	public float getZFar(){
		return zFar;
	}
	/** Updates the OpenGL projection state of the camera */
	public void projectionSetup(){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(viewPort.l(), viewPort.r(), viewPort.d(),viewPort.u(), zFar, zNear);
	}
	/** Clears the OpenGL background */
	public void backgroundSetup(){
		GL11.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);	
		//GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	public String toString(){
		return "Camera{ pos:"+p+" scale:"+scale+" zNear:"+zNear+" zFar:"+zFar+" resolution:"+resolution+" viewPort:"+viewPort+" }";
	}
	public Vec2   getMousePos(){
		Vec2 pos = new Vec2(Mouse.getX(),Mouse.getY());
		pos.sub(resolution.scalen(0.5f));
		pos.scale(scale);
		pos.add(p);
		return pos;
	}
	/** This method is called just before the background and projection setup is done */
	public void OnRenderSetup(){}
	/** This method is called after all entities have been updated, and before they are rendered */
	public void OnRenderStart(){}
	/** This method is called after all entities have been rendered and before the display is updated */
	public void OnRenderEnd(){}
}
