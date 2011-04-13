package math;

import org.lwjgl.opengl.GL11;

public class BBox{
	/** The position of the center of the BBox, in world coordinates */
	public Vec2 p;		//position
	/** The size of the BBox in world coordinates */
	public Vec2 s;		//size
	private Vec2 hs;	//half size
	/** Instantiates a new BBox, with size (sx,sy) and position (0,0) */
	public BBox(float sx, float sy){
		p = new Vec2();
		s = new Vec2(sx,sy);
		hs = new Vec2(sx*0.5f,sy*0.5f);
	}
	/** Instantiates a new BBox with size (size.x,size.y) and position (0,0) */
	public BBox(Vec2 size){
		p = new Vec2();
		s = size.dup();
		hs = new Vec2(s.x*0.5f,s.y*0.5f);
	}
	/** Instantiates a new BBox with size (sx,sy) and position (pos.x,pos.y) */
	public BBox(Vec2 pos, float sx, float sy){
		p = new Vec2(pos.x,pos.y);
		s = new Vec2(sx,sy);
		hs = new Vec2(sx*0.5f,sy*0.5f);
	}
	/** Instantiates a new BBox with size (size.x,size.y) and position (pos.x,pos.y) */
	public BBox(Vec2 pos, Vec2 size){
		p = new Vec2(pos.x,pos.y);
		s = size.dup();
		hs = new Vec2(s.x*0.5f,s.y*0.5f);
	}
	/** Instantiates a new BBox with size (sx,sy) and position (px,py) */
	public BBox(float px, float py, float sx, float sy){
		p = new Vec2(px,py);
		s = new Vec2(sx,sy);
		hs = new Vec2(sx*0.5f,sy*0.5f);
	}
	/** Returns a new BBox that is a duplicate of this one */
	public BBox dup(){
		return new BBox(p,s);
	}
	/** Returns a new BBox with the same size as this, but with position (dest.x,dest.y) */
	public BBox dupAt(Vec2 dest){
		return new BBox(dest,s);
	}
	/** Changes the size of this BBox. WARNING: do not change the size of the BBox by changing ".s" ! */
	public void resize(Vec2 newsize){
		s = newsize.dup();
		hs = new Vec2(newsize.x,newsize.y);
	}
	/** Multiplies the size of this BBox by factor. WARNING: do not change the size of the BBox by changing ".s" ! */
	public void scale(float factor){
		s.scale(factor);
		hs.scale(factor);
	}
	/** Multiplies .s.x by factor.x and .s.y by factor.x. WARNING: do not change the size of the BBox by changing ".s" ! */
	public void scale(Vec2 factor){
		s.mult(factor);
		hs.mult(factor);
	}
	/** Sets the position of the BBox to pos */
	public void position(Vec2 pos){
		p = pos.dup();
	}
	/** Moves the BBox by (disp.x,disp.y) */
	public void move(Vec2 disp){
		p.add(disp);
	}
	/** Returns the x coord of 'left' side of the bbox, that is the minimum x coord in the BBox */
	public float l(){
		return p.x - hs.x;
	}
	/** Returns the x coord of 'right' side of the bbox, that is the maximum x coord in the BBox */

	public float r(){
		return p.x + hs.x;
	}
	/** Returns the y coord of 'up' side of the bbox, that is the maximum y coord in the BBox */
	public float u(){
		return p.y + hs.y;
	}
	/** Returns the y coord of 'down' side of the bbox, that is the minimum y coord in the BBox */
	public float d(){
		return p.y - hs.y;
	}
	/** Returns true if the position v is inside (boundaries included) of the BBox */
	public boolean isIn(Vec2 v){
		return 		v.x >= l() && v.x <= r()
				&&	v.y >= d() && v.y <= u() ; 
	}
	/** Returns true if the BBox b is inside (boundaries included) of the BBox. (isIn(this) == true) */
	public boolean isIn(BBox b){
		return  	b.l() >= l()  && b.r() <= r()
				&&  b.d() >= d() && b.u() <= u();
	}
	/** Returns true if the BBox b collides (boundaries excluded) with the BBox */
	public boolean collides(BBox b){
		boolean xcol = false;
		boolean ycol = false;
		xcol = (	( b.l() <= r() && b.r() > r()) 
				|| 	( b.r() >= l() && b.l() < l())
				||  ( b.l() >= l() && b.r() <= r()) );
		
		ycol = (	( b.d() <= u() && b.u() > u()) 
				|| 	( b.u() >= d() && b.d() < d())
				||  ( b.d() >= d() && b.u() <= u()) );
		
		return xcol && ycol;
	}
	/** Returns the vector (x,y) ;
	 * 	- x is the minimum x coord displacement of this BBox to avoid collision with b.
	 *  - y is the minimum y coord displacement of this BBox to avoid collision with b 
	 *  Displacing the BBox by (x,0) or (0,y) is sufficient to avoid the collision.
	 *  If x or y is 0.0, b does not collides with the BBox */
	public Vec2 collisionVector(BBox b){
		float xdist = 0.0f;
		float ydist = 0.0f;
		if(b.p.x > p.x){	// b is on the right
			if(b.l() < r()){
				xdist = - (r() - b.l());
			}
		}else{
			if(b.r() > l()){
				xdist = b.r() - l();
			}
		}
		if(b.p.y > p.y){	// b is on the right
			if(b.d() < u()){
				ydist = - (u() - b.d());
			}
		}else{
			if(b.u() > d()){
				ydist = b.u() - d();
			}
		}
		return new Vec2(xdist,ydist);
	}
	public String toString(){
		return "["+p.x+","+p.y+"|"+s.x+","+s.y+"]";
	}
	/** Draws a quad of position and dimensions of the BBox, at z coord 0.0 */
	public void glDraw(){
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f((float)l(), (float)d());
			GL11.glVertex2f((float)r(), (float)d());
			GL11.glVertex2f((float)r(), (float)u());
			GL11.glVertex2f((float)l(), (float)u());
		GL11.glEnd();
	}
	/** Draws a quad of position and dimensions of the BBox, at z coord "z" */
	public void glDraw(float z){
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3f((float)l(), (float)d(),z);
			GL11.glVertex3f((float)r(), (float)d(),z);
			GL11.glVertex3f((float)r(), (float)u(),z);
			GL11.glVertex3f((float)l(), (float)u(),z);
		GL11.glEnd();
	}
}
