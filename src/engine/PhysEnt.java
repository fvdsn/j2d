package engine;
import math.*;

/** A PhysEnt is an Entity that has a speed, gravity and acceleration. The movement of
 * such entities is automatically performed each frame by the engine in the 
 * Physic Update Pass. The Physic Update Pass is performed after all entities have
 * been updated and before collision and rendering are performed.
 */
public class PhysEnt extends Ent {
	/** The speed of the entity in worldunits/sec */
	protected Vec2 speed = new Vec2(0,0);
	/** if true the speed of this entity is affected by it's accleration*/
	public boolean useAccel = true;
	/** The accleration of the entity in worldunits/sec/sec */
	protected Vec2 accel = new Vec2(0,0);
	/** if true the speed of this entity is affected by it's gravity */
	public boolean useGravity = true;
	/** the gravity affecting this entity in worldunits/sec/sec */
	protected Vec2 gravity   = new Vec2(0,0);
	
	/** Returns the speed (wu/sec) of this entity */
	public Vec2 getSpeed(){
		return speed.dup();
	}
	/** Changes the speed (wu/sec) of this entity */
	public void setSpeed(Vec2 speed){
		this.speed = speed.dup();
	}
	/** Returns the acceleration (wu/sec/sec) of this entity */
	public Vec2 getAccel(){
		return accel.dup();
	}
	/** Changes the acceleration (wu/sec/sec) of this entity.
	 *  SeeAlso: useAccel */
	public void setAccel(Vec2 accel){
		this.accel = accel;
	}
	/** Returns the gravity (wu/sec/sec) affecting this entity */
	public Vec2 getGravity(){
		return gravity.dup();
	}
	/** Sets the gravity (wu/sec/sec) affecting this entity 
	 *  SeeAlso: useGravity */
	public void setGravity(Vec2 gravity){
		this.gravity = gravity.dup();
	}
	/** Applies the speed and acceleration effect of a frame's duration.
	 *  This is called on all PhysEnt each frame in the Physics Pass.
	 */
	public void doPhysics(){
		if(useAccel){
			speed.add(accel.scalen(Main.deltaTime));
		}
		if(useGravity){
			speed.add(gravity.scalen(Main.deltaTime));
		}
		translate(speed.scalen(Main.deltaTime));
	}
	
}
