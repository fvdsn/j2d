package engine;

import math.BBox;


public class WorldCell {
	public int  px;
	public int  pz;
	public int  py;
	public int  sx;
	public int  sy;
	
	/* Class type */
	public static String type = "DefaultCell";
	public static boolean solid = true;
	
	/* Solid cells properties */
	/** The friction of an object sliding on the ground */
	public static float groundFriction = 1.0f;	
	/** The friction used for acceleration on the ground */
	public static float groundCtrlFriction = 1.0f;
	/** The friction of an object sliding on the walls */
	public static float wallFriction = 1.0f;	
	/** The friction used for acceleration on the walls */
	public static float wallCtrlFriction = 1.0f;
	/** The friction of an object sliding on the walls */
	public static float ceilFriction = 1.0f;	
	/** The friction used for acceleration on the walls */
	public static float ceilCtrlFriction = 1.0f;

	
	public BBox getBounds(){
		return new BBox(px*World.cellSize,py*World.cellSize,World.cellSize,World.cellSize);
	}

}
