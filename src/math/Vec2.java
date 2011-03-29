package math;


public class Vec2 implements Comparable<Vec2>{
	public double x = 0.0;
	public double y = 0.0;
	public Vec2(){}
	public Vec2(double x, double y){
		this.x = x;
		this.y = y;
	}
	public double length(){
		return Math.sqrt(x*x + y*y);
	}
	public double sqrLength(){
		return x*x + y*y;
	}
	public double dist(Vec2 b){
		double dx = x - b.x;
		double dy = y - b.y;
		return Math.sqrt(dx*dx + dy*dy);
	}
	public double sqrDist(Vec2 b){
		double dx = x - b.x;
		double dy = y - b.y;
		return dx*dx + dy*dy;
	}
	public double dot(Vec2 b){
		return x*b.x + y*b.y;
	}
	public Vec2 dup(){
		return new Vec2(x,y);
	}
	public void zero(){
		x = 0.0;
		y = 0.0;
	}
	public void copy(Vec2 b){
		x = b.x;
		y = b.y;
	}
	public void add(Vec2 b){
		x += b.x;
		y += b.y;
	}
	public Vec2 addn(Vec2 b){
		return new Vec2(x+b.x, y+b.y);
	}
	public void sub(Vec2 b){
		x -= b.x;
		y -= b.y;
	}
	public Vec2 subn(Vec2 b){
		return new Vec2(x-b.x,y-b.y);
	}
	public void scale(double a){
		x *= a;
		y *= a;
	}
	public Vec2 scalen(double a){
		return new Vec2(x*a,y*a);
	}
	public void mult(Vec2 b){
		x *= b.x;
		y *= b.y;
	}
	public Vec2 multn(Vec2 b){
		return new Vec2(x*b.x,y*b.y);
	}
	public void normalize(){
		double norm = this.length();
		if(norm != 0.0){
			double invnorm = 1.0/norm;
			x *= invnorm;
			y *= invnorm;
		}else{
			x = 1.0;
			y = 1.0;
		}
	}
	public Vec2 normalizen(){
		Vec2 n = this.dup();
		n.normalize();
		return n;
	}
	public void setLength(float v){
		this.normalize();
		this.scale(v);
	}
	public Vec2 setLengthn(float v){
		Vec2 n = this.normalizen();
		n.scale(v);
		return n;
	}
	public void biggest(Vec2 b){
		if(this.sqrLength() < b.sqrLength()){
			x = b.x;
			y = b.y;
		}
	}
	public Vec2 biggestn(Vec2 b){
		if(this.sqrLength() < b.sqrLength()){
			return b;
		}else{
			return this;
		}
	}
	public void smallest(Vec2 b){
		if(this.sqrLength() > b.sqrLength()){
			x = b.x;
			y = b.y;
		}
	}
	public Vec2 smallestn(Vec2 b){
		if(this.sqrLength() > b.sqrLength()){
			return b;
		}else{
			return this;
		}
	}
	public boolean bigger(double v){
		return this.sqrLength() > v*v;
	}
	public boolean smaller(double v){
		return this.sqrLength() < v*v;
	}
	public void project(Vec2 onthisv){
		double d = this.dot(onthisv);
		this.copy(onthisv);
		this.normalize();
		this.scale(d);
	}
	public Vec2 projectn(Vec2 onthisv){
		Vec2 n = dup();
		n.project(onthisv);
		return n;
	}
	/*public void rotate(float radAngle){
		double c  = Math.cos(radAngle);
		double s  = Math.sin(radAngle);
		double nx =  
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
			double l = this.sqrLength();
			double vl = v.sqrLength();
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
