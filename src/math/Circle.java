package math;

import engine.Draw;
/** WIP */
public class Circle extends Bound {
	private float radius;

	public Circle(float px, float py, float radius){
		super(px,py);
		this.radius = radius;
	}
	public Circle(Vec2 pos, float radius){
		super(pos.x,pos.y);
		this.radius = radius;
	}
		
	public float getRadius(){
		return radius;
	}
	
	public void setRadius(float radius){
		this.radius = radius;
	}

	@Override
	public float l() {
		return getPosX() - radius * getScaleX();
	}

	@Override
	public float r() {
		return getPosX() + radius * getScaleX();
	}

	@Override
	public float u() {
		return getPosY() + radius * getScaleY();
	}

	@Override
	public float d() {
		return getPosY() - radius * getScaleY();
	}

	@Override
	public boolean contains(Vec2 pos) {
		return pos.distSquared(px,py) < radius * radius;
	}

	@Override
	public boolean contains(BoundIF b) {
		if(b instanceof Bound){
			return contains(b.getPos());
		}else if(b instanceof Circle){
			Circle c = (Circle)b;
			float dist = getPos().dist(c.getPos());
			return dist + c.getRadius() <= radius;
		}else{
			return 	contains(new Vec2(b.l(),b.d())) &&
					contains(new Vec2(b.l(),b.u())) &&
					contains(new Vec2(b.r(),b.d())) &&
					contains(new Vec2(b.r(),b.u()));
		}
	}

	@Override
	public boolean collides(BoundIF b) {
		if(b instanceof Bound){
			return contains(b.getPos());
		}else if(b instanceof Circle){
			Circle c = (Circle)b;
			float dist = getPos().dist(c.getPos());
			return dist < radius + c.getRadius();
		}else{
			return boundCollides(b.l(), b.r(), px - radius, px + radius) &&
				   boundCollides(b.d(), b.u(), py - radius, py + radius);
		}
	}
	
	private Vec2 collisionVectorWithPoint(float bx, float by){
		Vec2 disp = new Vec2(px,py);
		disp.sub(bx,by);
		float dist = disp.length();
		if(dist >= radius){
			return new Vec2();
		}else if(disp.isZero()){
			return new Vec2(radius,0);
		}else{
			disp.setLength(radius - dist);
			return disp;
		}
	}
	@Override
	public Vec2 collisionVector(BoundIF b) {
		if(b instanceof Bound){
			return this.collisionVectorWithPoint(b.getPosX(), b.getPosY());
		}else if(b instanceof Circle){
			Circle c = (Circle)b;
			Vec2 disp = new Vec2(px,py);
			disp.sub(b.getPosX(),b.getPosY());
			float dist = disp.length();
			if(dist >= radius + c.getRadius()){
				return new Vec2();
			}else if(disp.isZero()){
				return new Vec2(radius + c.getRadius(),0);
			}else{
				disp.setLength(radius + c.getRadius() - dist);
				return disp;
			}
		}else{
			if(px > b.r()){
				if(py > b.u()){
					return collisionVectorWithPoint(b.r(), b.u());
				}else if(py < b.d()){
					return collisionVectorWithPoint(b.r(), b.d());
				}
			}else if(px < b.l()){
				if(py > b.u()){
					return collisionVectorWithPoint(b.l(), b.u());
				}else if(py < b.d()){
					return collisionVectorWithPoint(b.l(), b.d());
				}
			}
			float xdisp = boundEscapeDist(px - radius, px + radius, b.l(), b.r());
			float ydisp = boundEscapeDist(py - radius, py + radius, b.d(), b.u());
			if(Math.abs(xdisp) < Math.abs(ydisp)){
				return new Vec2(xdisp,0);
			}else{
				return new Vec2(0,ydisp);
			}
		}
	}
	@Override
	public BoundIF dup() {
		return new Circle(px,py,radius);
	}

	@Override
	public BoundIF dupAt(Vec2 pos) {
		return new Circle(pos.x,pos.y,radius);
	}
	@Override
	public BoundIF dupAtZero() {
		return new Circle(0,0,radius);
	}
	

	@Override
	public Vec2 collisionAxis(BoundIF b) {
		// TODO Auto-generated method stub
		return new Vec2();
	}

}
