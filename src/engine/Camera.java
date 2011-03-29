package engine;

import math.BBox;
import math.Vec2;

public class Camera {
	public Vec2 	pos;
	public double  	scale;
	public BBox	   	viewPort;

	public static Vec2 	resolution;
	public static double  	widthToHeight;
	public static double  	heightToWidth;
	public static boolean	fullScreen = false;
	
	public void OnRenderFirstStart(){}	//TODO
	public void OnRenderStart(){}
	public void OnRenderEnd(){}

}
