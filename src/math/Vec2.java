package math;
import java.io.Serializable;

import org.lwjgl.util.vector.*;

import engine.Main;

public class Vec2 extends Vector2f implements Comparable<Vec2>, Serializable{
	private static final long serialVersionUID = -7795624606328972986L;
	/** Multiply by this value to convert a radiant angle to a degree angle */
	public final float radToDeg = 180.0f / (float)(Math.PI);
	/** Multiply by this value to convert a degree angle to a radiant angle */
	public final float degToRad = (float)(Math.PI) / 180.0f;
	
	/** Creates a new vector uniformly chosen in the [-1,1] square */
	public static Vec2 randomn(){
		return new Vec2(Main.rng.nextFloat()*2f - 1f,
						Main.rng.nextFloat()*2f - 1f);
	}
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
	public void setPolar(float length, float angle){
		x = length;
		y = 0;
		rotate(angle);
	}
	public void setPolarDeg(float length, float angleDeg){
		x = length;
		y = 0;
		rotateDeg(angleDeg);
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
	/** Return the perpendicular dot product of this vector with b */
	public float perpDot(Vec2 b){
		return x*b.y - y*b.x;
	}
	/** Return the perpendicular dot product of this vector with (px,py) */
	public float perpDot(float px,float py){
		return x*py - y*px;
	}
	/** Return the angle between this vector and b in radians*/
	public float angle(Vec2 b){
		return (float)Math.acos(dot(b)/length()*b.length());
	}
	/** Return the angle between this vector and b in degrees*/
	public float angleDeg(Vec2 b){
		return angle(b) * radToDeg;
	}
	/** Return the signed angle between this vector and b in radians */
	public float angleSigned(Vec2 b){
		return (float)Math.atan2(perpDot(b), dot(b));
	}
	/** Return the signed angle between this vector and b in degrees */
	public float angleSignedDeg(Vec2 b){
		return (float)Math.atan2(perpDot(b), dot(b)) * radToDeg;
	}
	
	
	/** Linear interpolation between this vector and target. if
	 * alpha is zero : this vector stays the same, if alpha is one,
	 * this vector becomes target */
	public void lerp(Vec2 target,float alpha){
		x = alpha*target.x + (1.0f-alpha)*x;
		y = alpha*target.y + (1.0f-alpha)*y;
	}
	/** Linear interpolation between this vector and target. if
	 * alpha is zero : returns this vector. if alpha is one,
	 * returns target */
	public Vec2 lerpn(Vec2 target,float alpha){
		Vec2 ret = this.dup();
		ret.lerp(target, alpha);
		return ret;
	}
	/** Rotates the vector by angle radians */
	public void rotate(float angle){
		float c = (float)Math.cos(angle);
		float s = (float)Math.sin(angle);
		float tmpx = x;
		float tmpy = y;
		x = tmpx * c - tmpy * s;
		y = tmpx * s + tmpy * c;
	}
	/** Rotates the vector by angle degrees */
	public void rotateDeg(float angle){
		rotate(angle * degToRad);
	}
	/** Returns a rotated vector by angle radians */
	public Vec2 rotaten(float angle){
		Vec2 ret = this.dup();
		ret.rotate(angle);
		return ret;
	}
	/** Returns a rotated vector by angle degrees */
	public Vec2 rotateDegn(float angle){
		return rotaten(angle * degToRad);
	}
	/** Reflect the vector around a normal */
	public void reflect(Vec2 normal){
		Vec2 n = normal.normalisen();
		float d = x*n.x + y*n.y;
		x = x - 2.0f*n.x*d;
		y = y - 2.0f*n.y*d;
	}
	/** Returns a new vector that is the reflection of this vector around the normal */
	public void reflectn(Vec2 normal){
		Vec2 r = this.dup();
		r.reflect(normal);
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
	/** Adds the components of the vector (px,py) to the components of this vector, and returns the result as a new vector, leaving this vector intact */
	public Vec2 addn(float px, float py){
		return new Vec2(x+px, y+py);

	}
	/** Substracts the components of the vector b to the components of this vector */
	public void sub(Vec2 b){
		x -= b.x;
		y -= b.y;
	}
	/** Substracts the components of the vector (px,py) to the components of this vector */
	public void sub(float px, float py){
		x -= px;
		y -= py;
	}
	/** Substracts the components of the vector b to the components of this vector, and returns the result as a new vector, leaving this vector intact */
	public Vec2 subn(Vec2 b){
		return new Vec2(x-b.x,y-b.y);
	}
	/** Substracts the components of the vector (px,py) to the components of this vector, and returns the result as a new vector, leaving this vector intact */
	public Vec2 subn(float px, float py){
		return new Vec2(x-px,y-py);
	}
	/** Sets the vector to the vector from this vector to dest.
	 */
	public void distVec(Vec2 dest){
		x = dest.x - x;
		y = dest.y - y;
	}
	/** Sets the vector to the vector from this vector to (destx,desty).
	 */
	public void distVec(float destx, float desty){
		x = destx - x;
		y = desty - y;
	}
	/** Returns a new vector from this vector to dest.
	 */
	public Vec2 distVecn(Vec2 dest){
		return new Vec2(dest.x-x, dest.y-y);
	}
	/** Returns a new vector from this vector to (destx,desty).
	 */
	public Vec2 distVecn(float destx, float desty){
		return new Vec2(destx-x, desty-y);
	}
	/** Multiplies the components of the vector by a, and returns the result as a new vector, leaving this vector intact */
	public Vec2 scalen(float a){
		return new Vec2(x*a,y*a);
	}
	/** Negates the components of the vector, and returns the result as a new vector, leaving this vector intact */
	public Vec2 negaten(){
		return new Vec2(-x,-y);
	}
	/** Multiplies the components of this vector by the components of the vector b*/
	public void mult(Vec2 b){
		x *= b.x;
		y *= b.y;
	}
	/** Multiplies the components of this vector by the components of the vector (px,py)*/
	public void mult(float px, float py){
		x *= px;
		y *= py;
	}
	/** Multiplies the components of this vector by the components of the vector b,
	  * and returns the result as a new vector, leaving this vector intact */
	public Vec2 multn(Vec2 b){
		return new Vec2(x*b.x,y*b.y);
	}

	/** Multiplies the components of this vector by the components of the vector (px,py),
	  * and returns the result as a new vector, leaving this vector intact */
	public Vec2 multn(float px, float py){
		return new Vec2(x*px,y*py);
	}
	/** Returns a normalized version of this vector. This vector cannot be zero */
	public Vec2 normalisen(){
		Vec2 n = this.dup();
		n.normalise();
		return n;
	}
	/** Sets the length of this vector to v, This vector cannot be zero */
	public void setLength(float v){
		this.normalise();
		this.scale(v);
	}
	/** Returns a vector of the same direction as this vector but of length v, 
	 * This vector cannot be zero */
	public Vec2 setLengthn(float v){
		Vec2 n = this.normalisen();
		n.scale(v);
		return n;
	}
	/** Sets this vector to b if b is bigger than this vector */
	public void biggest(Vec2 b){
		if(this.lengthSquared() < b.lengthSquared()){
			x = b.x;
			y = b.y;
		}
	}
	/** Returns the biggest vector between this vector and b */
	public Vec2 biggestn(Vec2 b){
		if(this.lengthSquared() < b.lengthSquared()){
			return b;
		}else{
			return this;
		}
	}
	/** Sets this vector to b if b is smaller than this vector */
	public void smallest(Vec2 b){
		if(this.lengthSquared() > b.lengthSquared()){
			x = b.x;
			y = b.y;
		}
	}
	/** Returns the smallest vector between this vector and b */
	public Vec2 smallestn(Vec2 b){
		if(this.lengthSquared() > b.lengthSquared()){
			return b;
		}else{
			return this;
		}
	}
	/** Return true if this vector has a length greater than v */
	public boolean bigger(float v){
		return this.lengthSquared() > v*v;
	}
	/** Return true if this vector has a length smaller than v */
	public boolean smaller(float v){
		return this.lengthSquared() < v*v;
	}
	/** Return true if this vector has a length close enough to zero */
	public boolean isZero(){
		return this.lengthSquared() < 0.00001f;
	}
	/** Project this vector on the vector 'onthisv' */
	public void project(Vec2 onthisv){
		float d = this.dot(onthisv);
		this.copy(onthisv);
		this.normalise();
		this.scale(d);
	}
	/** Returns a projection of this vector on 'onthisv' */
	public Vec2 projectn(Vec2 onthisv){
		Vec2 n = dup();
		n.project(onthisv);
		return n;
	}
	/** Sets this vector to the next ccw perpendicular vector */
	public void perpendicular(){
		float tmp = x;
		this.x = -y;
		this.y = tmp;
	}
	/** Returns the next ccw perpendicular vector to this one*/
	public Vec2 perpendicularn(){
		return new Vec2(-y,x);
	}
	/** Returns true if the two vector are equal */
	public boolean equals(Vec2 b){
		return this == b || x == b.x && y == b.y;
	}
	/** Returns a string representation of this vector */
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
