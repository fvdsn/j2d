package math;

import org.lwjgl.opengl.GL11;
/**
 * A BBox is an axis aligned bounding box. It is defined by its size and its center.
 *
 */
public class BBox extends Bound{
	/** The size of the BBox in world coordinates */
	protected  float sizex = 1;		
	protected  float sizey = 1;
	/** Half the size of the BBox in world coordinates */
	protected  float hsx   = 0.5f;
	protected  float hsy   = 0.5f;

	/** Instantiates a new BBox, with size (sx,sy) and position (0,0) */
	public BBox(float sx, float sy){
		super();
		sizex = sx;
		sizey = sy;
		hsx = sizex * 0.5f;
		hsy = sizey * 0.5f;
	}
	/** Instantiates a new BBox with size (sizex,sizey) and position (0,0) */
	public BBox(Vec2 size){
		super();	
		sizex = size.x;
		sizey = size.y;
		hsx = sizex * 0.5f;
		hsy = sizey * 0.5f;
	}
	/** Instantiates a new BBox with size (sx,sy) and position (pos.x,pos.y) */
	public BBox(Vec2 pos, float sx, float sy){
		super(pos.x,pos.y);
		sizex = sx;
		sizey = sy;
		hsx = sizex * 0.5f;
		hsy = sizey * 0.5f;
	}
	/** Instantiates a new BBox with size (sizex,sizey) and position (pos.x,pos.y) */
	public BBox(Vec2 pos, Vec2 size){
		super(pos.x,pos.y);
		sizex = sizex;
		sizey = sizey;
		hsx = sizex * 0.5f;
		hsy = sizey * 0.5f;
	}
	/** Instantiates a new BBox with size (sx,sy) and position (px,py) */
	public BBox(float px, float py, float sx, float sy){
		super(px,py);
		sizex = sx;
		sizey = sy;
		hsx = sizex * 0.5f;
		hsy = sizey * 0.5f;
	}
	/** Returns a new BBox that is a duplicate of this one. 
	 * All parent/child relationships are lost */
	public BBox dup(){
		return new BBox(px,py,sizex,sizey);
	}
	/** Returns a new BBox with the same size as this, but with position (dest.x,dest.y).
	 *  All parent/child relationships are lost */
	public BBox dupAt(Vec2 dest){
		return new BBox(dest.x,dest.y,sizex,sizey);
	}
	/** Changes the local size of this BBox to newsize */
	public void setSize(Vec2 newsize){
		sizex = newsize.x;
		sizey = newsize.y;
		hsx = newsize.x;
		hsy = newsize.y;
	}
	/** Returns the local size of this BBox */
	public Vec2 getSize(){
		return new Vec2(sizex,sizey);
	}
	/** Return the local size of this BBox on the X axis (width) */
	public float getSizeX(){
		return sizex;
	}
	/** Return the local size of this BBox on the Y axis (height) */
	public float getSizeY(){
		return sizey;
	}
	/** Returns the x coord of 'left' side of the bbox, that is the minimum x coord in the BBox */
	@Override
	public float l(){
		return getPosX() - hsx * getScaleX();
	}
	/** Returns the x coord of 'right' side of the bbox, that is the maximum x coord in the BBox */
	@Override
	public float r(){
		return getPosX() + hsx * getScaleX();
	}
	/** Returns the y coord of 'up' side of the bbox, that is the maximum y coord in the BBox */
	@Override
	public float u(){
		return getPosY() + hsy * getScaleY();
	}
	/** Returns the y coord of 'down' side of the bbox, that is the minimum y coord in the BBox */
	@Override
	public float d(){
		return getPosY() - hsy * getScaleY();
	}
	/** Returns a string representation of this BBox  */
	public String toString(){
		return "["+getPosX()+","+getPosY()+"|"+sizex+","+sizey+"]";
	}
}
