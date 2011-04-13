package math;
import org.lwjgl.util.vector.*;

public class Vec2 extends Vector2f implements Comparable<Vec2>{
	private static final long serialVersionUID = -7795624606328972986L;
	/** Creates a new zero Vector */
	public Vec2(){
		this.x = 0.0f;
		this.y = 0.0f;
	}
	/** Creates a new vector (x,y) */
	public Vec2(float x, float y){
		this.x = x;
		this.y = y;
	}
	/** Returns the distance from this vector to b */
	public float dist(Vec2 b){
		float dx = x - b.x;
		float dy = y - b.y;
		return (float)Math.sqrt(dx*dx + dy*dy);
	}
	/** Returns the distance from this vector to (px,py) */
	public float dist(float px, float py){
		float dx = x - px;
		float dy = y - py;
		return (float)Math.sqrt(dx*dx + dy*dy);
	}
	/** Returns the squared distance from this vector to b */
	public float distSquared(Vec2 b){
		float dx = x - b.x;
		float dy = y - b.y;
		return dx*dx + dy*dy;
	}
	/** Returns the squared distance from this vector to (px,py) */
	public float distSquared(float px, float py){
		float dx = x - px;
		float dy = y - py;
		return dx*dx + dy*dy;
	}
	/** Returns the dot product of this vector with b */
	public float dot(Vec2 b){
		return x*b.x + y*b.y;
	}
	/** Returns the dot product of this vector with (px,py) */
	public float dot(float px, float py){
		return x*px + y*py;
	}
	/** Returns a duplicate of this vector (same as clone()) */
	public Vec2 dup(){
		return new Vec2(x,y);
	}
	/** Returns a duplicate of this vector (same as dup()) */
	public Vec2 clone(){
		return new Vec2(x,y);
	}
	/** Sets the vector to (0,0) */
	public void zero(){
		x = 0.0f;
		y = 0.0f;
	}
	/** Copies the content of vector b into this vector */
	public void copy(Vec2 b){
		x = b.x;
		y = b.y;
	}
	/** Adds the components of the vector b to the components of this vector */
	public void add(Vec2 b){
		x += b.x;
		y += b.y;
	}
	/** Adds the components of the vector (px,py) to the components of this vector */
	public void add(float px, float py){
		x += px;
		y += py;
	}
	/** Adds the components of the vector b to the components of this vector, and returns the result as a new vector, leaving this vector intact */
	public Vec2 addn(Vec2 b){
		return new Vec2(x+b.x, y+b.y);
	}
	public Vec2 addn(float px, float py){
		return new Vec2(x+px, y+py);

	}
	public void sub(Vec2 b){
		x -= b.x;
		y -= b.y;
	}
	public void sub(float px, float py){
		x -= px;
		y -= py;
	}
	public Vec2 subn(Vec2 b){
		return new Vec2(x-b.x,y-b.y);
	}
	public Vec2 subn(float px, float py){
		return new Vec2(x-px,y-py);
	}
	public Vec2 scalen(float a){
		return new Vec2(x*a,y*a);
	}
	public Vec2 negaten(){
		return new Vec2(-x,-y);
	}
	public void mult(Vec2 b){
		x *= b.x;
		y *= b.y;
	}
	public void mult(float px, float py){
		x *= px;
		y *= py;
	}
	public Vec2 multn(Vec2 b){
		return new Vec2(x*b.x,y*b.y);
	}
	public Vec2 multn(float px, float py){
		return new Vec2(x*px,y*py);
	}
	public Vec2 normalisen(){
		Vec2 n = this.dup();
		n.normalise();
		return n;
	}
	public void setLength(float v){
		this.normalise();
		this.scale(v);
	}
	public Vec2 setLengthn(float v){
		Vec2 n = this.normalisen();
		n.scale(v);
		return n;
	}
	public void biggest(Vec2 b){
		if(this.lengthSquared() < b.lengthSquared()){
			x = b.x;
			y = b.y;
		}
	}
	public Vec2 biggestn(Vec2 b){
		if(this.lengthSquared() < b.lengthSquared()){
			return b;
		}else{
			return this;
		}
	}
	public void smallest(Vec2 b){
		if(this.lengthSquared() > b.lengthSquared()){
			x = b.x;
			y = b.y;
		}
	}
	public Vec2 smallestn(Vec2 b){
		if(this.lengthSquared() > b.lengthSquared()){
			return b;
		}else{
			return this;
		}
	}
	public boolean bigger(float v){
		return this.lengthSquared() > v*v;
	}
	public boolean smaller(float v){
		return this.lengthSquared() < v*v;
	}
	public void project(Vec2 onthisv){
		float d = this.dot(onthisv);
		this.copy(onthisv);
		this.normalise();
		this.scale(d);
	}
	public Vec2 projectn(Vec2 onthisv){
		Vec2 n = dup();
		n.project(onthisv);
		return n;
	}
	/*public void rotate(float radAngle){
		float c  = Math.cos(radAngle);
		float s  = Math.sin(radAngle);
		float nx =  
	}*/
	public boolean equals(Vec2 b){
		return this == b || x == b.x && y == b.y;
	}
	public String toString(){
		return "["+x+","+y+"]";
	}
	public int hashCode(){
		int i = (int)(x*1000.0);
		int j = (int)(y*1000.0);
		return i + j*1000;
	}
	@Override
	public int compareTo(Vec2 v) {
		if(this == v){
			return 0;
		}else{
			float l = this.lengthSquared();
			float vl = v.lengthSquared();
			if(l < vl){
				return -1;
			}else if(l == vl){
				return 0;
			}else{
				return 1;
			}
		}
	}
}
