package engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import math.*;

public class Ent implements BoundIF, Comparable{
	/** The name of the entity. It is by default the entity's class */
	private String name = "UnnamedEnt";
	
	private static long lastuid = 0;
	public final long uid;
	
	public enum EntState{
		New,
		Alive,
		Destroyed
	}
	
	public enum CollisionBehaviour{
		Receiver,
		Emitter,
		Both,
		None
	}
	public CollisionBehaviour collisionBehaviour = CollisionBehaviour.Receiver;
	
	/** The current running state of the entity */
	public EntState entState 	= EntState.New;
	/** The last frame the entity has been updated */
	public long entCurrentFrame = 0;
	/** If true, this entity's OnUpdate, OnFirstUpdate are called */ 
	public boolean active 		= true;	//TODO
	/** If true, this entity is rendered */
	public boolean renderActive = true; //TODO
	/** If true this entity's childs are rendered */
	public boolean renderChildsActive =  true;	// TODO
	
	/** If the current game time is larger than destroyTime, the entity is destroyed */
	public float   destroyTime	= Float.MAX_VALUE;
	
	/** Set of the scenes this entity is in */
	private Set<Scene> sceneList = new HashSet<Scene>();
	
	/** The bound contains the entity's position, size and parent information */
	private BoundIF bound = new Bound();

	/** The entity depth position. smaller z are further away from the camera */
	private float z = 0.0f;		
	/** The entity's parent entity */
	private ArrayList<Ent> childList = null;
	
	public Ent(){
		this.uid = lastuid++;
		this.name = this.getClass().getName();
	}
	/** Returns this entity's name */
	public String getName(){
		return this.name;
	}
	/** Changes this entity's name if not null */
	public void   setName(String name){
		if(name != null){
			this.name = name;
		}
	}
	public void setBound(BoundIF b){
		if(! (b instanceof Ent)){
			BoundIF b2 = b.dupAt(bound.getLocalPos());
			b2.setParent(bound.getParent());
			b2.setLocalScale(bound.getLocalScale());
			bound = b2;
		}
	}
	public BoundIF getBound(){
		BoundIF b = bound.dup();
		b.setParent(null);
		return b;
	}
	/* BoundIF methods, See BoundIF for documentation */
	public Vec2 	getPos(){ 					return bound.getPos();		}
	public float 	getPosX(){ 					return bound.getPosX(); 	}
	public float 	getPosY(){ 					return bound.getPosY();		}
	public Vec2 	getLocalPos(){	 			return bound.getLocalPos();	}
	public float 	getLocalPosX(){	 			return bound.getLocalPosX();	}
	public float 	getLocalPosY(){	 			return bound.getLocalPosY();	}
	public float 	dist(BoundIF b){			return bound.dist(b);		}
	public float 	dist(Vec2 b){				return bound.dist(b);		}
	public Vec2		distVec(BoundIF b){			return bound.distVec(b);	}
	public Vec2		distVec(Vec2 b){			return bound.distVec(b);	}
	public void 	setPos(Vec2 p){				bound.setPos(p); 			}
	public void 	setPos(float px, float py){ bound.setPos(px,py); 		}
	public void 	setLocalPos(Vec2 pos){		bound.setLocalPos(pos); 	}
	public void 	setLocalPos(float px, float py){bound.setLocalPos(px,py); 	}
	public void 	translate(Vec2 disp){		bound.translate(disp);		}
	public void 	translate(float px, float py){	bound.translate(px,py); 	}
	public void		scale(float factor){		bound.scale(factor);		}
	public void		scale(Vec2 factor){			bound.scale(factor);		}
	public void		scale(float fx, float fy){	bound.scale(fx,fy);			}
	public Vec2 	getScale(){					return bound.getScale();	}
	public float 	getScaleX(){				return bound.getScaleX();	}
	public float 	getScaleY(){				return bound.getScaleY();	}
	public Vec2 	getLocalScale(){			return bound.getLocalScale();	}
	public float 	getLocalScaleX(){			return bound.getLocalScaleX();	}
	public float 	getLocalScaleY(){			return bound.getLocalScaleY();	}
	public void 	setLocalScale(Vec2 scale){	bound.setLocalScale(scale);	}
	public void 	setLocalScale(float sx, float sy){	bound.setLocalScale(sx, sy);}
	public BoundIF	dup(){ 						return null;	/*TODO*/	}
	public BoundIF	dupAt(Vec2 pos){ 			return null;	/*TODO*/	}
	public BoundIF	dupAtZero(){ 				return null;	/*TODO*/	}
	public float 	l(){						return bound.l(); 			}
	public float 	r(){						return bound.r(); 			}
	public float 	u(){						return bound.u(); 			}
	public float 	d(){						return bound.d(); 			}
	public boolean  contains(Vec2 pos){			return bound.contains(pos);	}
	public boolean  contains(BoundIF b){		return bound.contains(b);	}
	public boolean	collides(BoundIF b){		return bound.collides(b);	}
	public Vec2		collisionVector(BoundIF b){	return bound.collisionVector(b);	} 
	public Vec2		collisionAxis(BoundIF b){	return bound.collisionAxis(b);	} 
	public boolean  rayCollides(Vec2 rs, Vec2 rd, float endDist, Vec2 colStart, Vec2 colEnd){
		return bound.rayCollides(rs,rd,endDist,colStart,colEnd);
		}
	public boolean rayCollides(Vec2 rs, Vec2 rd, float endDist){
		return bound.rayCollides(rs,rd,endDist);
	}
	public Vec2		getRandomPos(){
		return bound.getRandomPos();
	}
	
	/** Returns the entity's z buffer position relative to its parent */
	public float getLocalZ(){
		return this.z;
	}
	/** Sets the entity's z buffer position relative to its parent */
	public void setLocalZ(float z){
		this.z = z;
	}
	/** Returns the entity's absolute z buffer position */
	public float getZ(){
		if(!isRoot()){
			return getParent().getZ() + z;
		}else{
			return z;
		}
	}
	/** Returns the entity's parent. May be null */
	public Ent getParent(){
		return (Ent)bound.getParent();
	}
	public void setParent(BoundIF parent){
		if(parent instanceof Ent){
			Ent eparent = (Ent)parent;
			eparent.addChild(this);
		}
	}
	/** Return true if the entity has no parents */
	public boolean isRoot(){
		return bound.isRoot();
	}
	/** Return true if the entity has no childs */
	public boolean isLeaf(){
		return (childList == null || childList.size() == 0);
	}
	/** Returns an iterator over the entity's childs. May be null if entity isLeaf() */
	public List<Ent> getChildList(){
		if(this.childList != null){
			return Collections.unmodifiableList(this.childList);
		}else{
			return new ArrayList<Ent>();
		}
	}
	/** Adds a a child to this entity : TODO document this more */
	private void addChild(Ent e){
		if(e.getParent() == this){
			return;
		}else if(e.getParent() != null){
			e.removeFromParent();
		}
		if(e instanceof PhysEnt){
			Dbg.Error("Cannot make PhysEnt a child : PhysEnt must be root entities.");
			return;
		}
		e.bound.setParent(this);
		if(childList == null){
			childList = new ArrayList<Ent>();
		}
		childList.add(e);
		for(Scene s:sceneList){
			s.addEntity(e);
		}
	}
	/** Remove this entity from it's parent child list: TODO document this more */
	public void removeFromParent(){
		if(getParent() != null){
			if(getParent().childList != null){
				getParent().childList.remove(this);
			}
			bound.setParent(null);
			for(Scene s:sceneList){
				s.rootEntityList.add(this);
			}
		}
	}
	/** Returns this entity's scene list */
	public Set<Scene> getSceneList(){
		return this.sceneList;
	}
	/** Returns true if this entity is active : OnUpdate,OnFirstUpdate are called */
	public boolean isActive(){
		return active;
	}
	public void setActive(boolean _active){
		active = _active;
	}
	public void setActiveRecursively(boolean _active){
		active = _active;
		if(childList != null){
			for (Ent e: childList){
				e.setActiveRecursively(_active);
			}
		}
	}
	/** The entity will be removed from all scenes at the end of this frame */
	public void destroy(){
		if(entState != EntState.Destroyed){
			entState = EntState.Destroyed;
			if(childList != null){
				for (Ent e : childList){
					e.destroy();
				}
			}
		}
	}
	/** The entity will be removed from all scenes after delay seconds, or less
	 *  if it has already been destroyed by something else */
	public void destroy(float delay){
		destroyTime = Math.min(destroyTime, Main.time + delay);
	}
	/** Return the time in seconds before the entity is automatically destroyed,
	 *  Returns Float.MAX_VALUE if the entity will not be destroyed */
	public float timeBeforeDestruction(){
		if(destroyTime < Float.MAX_VALUE){
			return destroyTime - Main.time;
		}else{
			return Float.MAX_VALUE;
		}
	}
	/** Returns true if the entity is destroyed (it will not be drawn this frame,
	 *  and will be removed from all scenes on the next frame 
	 */
	public boolean isDestroyed(){
		return entState == EntState.Destroyed;
	}
	/** Returns true if the mouse is over the entity's bound */
	public boolean isMouseOver(){
		return this.contains(Main.camera.getMousePos());
	}
	@Override
	public int compareTo(Object obj) {
		if(obj instanceof Ent){
			Ent e = (Ent)obj;
			float z = getZ();
			float ze = e.getZ();
			if(z < ze){
				return -1;
			}else if(z == ze){
				return 0;
			}else{
				return 1;
			}
		}
		return -1;
	}
	/** Sorts the child list recursively by increasing z values */
	public void sortChildsZ(){
		if(childList != null && childList.size() > 0){
			Collections.sort(childList);
			for(Ent e: childList){
				e.sortChildsZ();
			}
		}
	}
	public void OnFirstUpdate(){}
	public void OnUpdate(){}
	public void OnDestroyed(){}
	public void	OnDraw(){}
	public void OnWorldCollision(){}
	public void OnCollisionEmit(Ent e, Vec2 colVec){}
	public void OnCollisionReceive(Ent e, Vec2 colVec){}
	
}
