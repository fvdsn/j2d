package math;

public class BBox{
	public Vec2 p;		//position
	public Vec2 s;		//size
	private Vec2 hs;	//half size
	public BBox(double sx, double sy){
		p = new Vec2();
		s = new Vec2(sx,sy);
		hs = new Vec2(sx*0.5,sy*0.5);
	}
	public BBox(Vec2 size){
		p = new Vec2();
		s = size.dup();
		hs = new Vec2(s.x*0.5,s.y*0.5);
	}
	public BBox(Vec2 pos, double sx, double sy){
		p = new Vec2(pos.x,pos.y);
		s = new Vec2(sx,sy);
		hs = new Vec2(sx*0.5,sy*0.5);
	}
	public BBox(Vec2 pos, Vec2 size){
		p = new Vec2(pos.x,pos.y);
		s = size.dup();
		hs = new Vec2(s.x*0.5,s.y*0.5);
	}
	public BBox(double px, double py, double sx, double sy){
		p = new Vec2(px,py);
		s = new Vec2(sx,sy);
		hs = new Vec2(sx*0.5,sy*0.5);
	}
	public BBox dup(){
		return new BBox(p,s);
	}
	public BBox dupAt(Vec2 dest){
		return new BBox(dest,s);
	}
	public void resize(Vec2 newsize){
		s = newsize.dup();
		hs = new Vec2(newsize.x,newsize.y);
	}
	public void scale(double factor){
		s.scale(factor);
		hs.scale(factor);
	}
	public void scale(Vec2 factor){
		s.mult(factor);
		hs.mult(factor);
	}
	public void position(Vec2 pos){
		p = pos.dup();
	}
	public void move(Vec2 disp){
		p.add(disp);
	}
	private double l(){
		return p.x - hs.x;
	}
	private double r(){
		return p.x + hs.x;
	}
	private double u(){
		return p.y + hs.y;
	}
	private double d(){
		return p.y - hs.y;
	}
	public boolean isIn(Vec2 v){
		return 		v.x >= l() && v.x <= r()
				&&	v.y >= d() && v.y <= u() ; 
	}
	public boolean isIn(BBox b){
		return  	b.l() >= l()  && b.r() <= r()
				&&  b.d() >= d() && b.u() <= u();
	}
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
	public Vec2 collisionVector(BBox b){
		double xdist = 0.0;
		double ydist = 0.0;
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
}
