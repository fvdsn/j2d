package engine;
import math.*;

public class PhysEnt extends Ent {
	
	public boolean collidesWorld = true;
	
	public enum SelfCollision{
		Both,
		OnlySelf,
		OnlyOthers
	}
	public SelfCollision selfCollisionEmit 	  = SelfCollision.OnlyOthers;
	public SelfCollision selfCollisionReceive = SelfCollision.OnlyOthers;
	public int			 selfCollisionClass = 0;
	
	public enum CollisionBehaviour{
		Receiver,
		Emitter,
		Both,
		None
	}
	public CollisionBehaviour collisionBehaviour = CollisionBehaviour.Receiver;
	
	protected Vec2 size;
	protected float mass = 1.0f;
	protected Vec2 speed = new Vec2(0,0);
	public boolean useAccel = true;
	protected Vec2 accel = new Vec2(0,0);
	public boolean useGravity = true;
	protected Vec2 gravity   = new Vec2(0,0);
	
	public BBox getBounds(){
		return new BBox(getPos(),size);
	}
	public Vec2 getSpeed(){
		return speed.dup();
	}
	public void setSpeed(Vec2 speed){
		this.speed = speed.dup();
	}
	public Vec2 getAccel(){
		return accel.dup();
	}
	public void setAccel(Vec2 accel){
		this.accel = accel;
	}
	public float getMass(){
		return mass;
	}
	public void setMass(float mass){
		this.mass = mass;
	}
	public Vec2 getGravity(){
		return gravity.dup();
	}
	public void setGravity(Vec2 gravity){
		this.gravity = gravity.dup();
	}
	public void doPhysics(){
		if(useAccel){
			speed.add(accel.scalen(Main.deltaTime));
		}
		if(useGravity){
			speed.add(gravity.scalen(Main.deltaTime));
		}
		translate(speed.scalen(Main.deltaTime));
	}
	public void OnWorldCollision(){}
	public void OnCollisionEmit(PhysEnt e, Vec2 colVec){}
	public void OnCollisionReceive(PhysEnt e, Vec2 colVec){}
}
