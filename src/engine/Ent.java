package engine;

import java.util.ArrayList;

import math.*;

public class Ent {
	public Ent  next = new Ent();	//all the content of this entity are copied into this entity

	public String name = "Unnamed Entity";

	public enum EntState{
		New,
		Alive,
		Destroyed
	}
	public EntState entState 	= EntState.New;
	public long entCurrentFrame = 0;
	public boolean active 		= true;	//Todo
	
	public Vec2 p = new Vec2();	// the entity position

	public enum CollisionMode{
		None,	// does not receive nor sends collision events 
		Receiver,		// only receive collision events (recieveCollision ent do not collide each other)
		Sender,		// only sends collision events (SendCollisions do not collide each other)
		Full			// sends and receive collision events
	}
	public CollisionMode collisionMode = CollisionMode.None;
	
	public enum CollisionSelf{
		None,	// does not collides with entity of the same class
		Only,	// only collides with entities of the same class
		Both	// collides with same and different classes
	}
	
	public CollisionSelf  collisionSelf = CollisionSelf.Both;
	
	public int  collisionClass = 0;	// for self collisions
	
	public int  collisionPriority = 0;	// higher priorities collide first
	
	public BBox collisionBox;
	
	public BBox touchBox;
	
	public BBox boundingBox;

	public Ent parent = null;
	public ArrayList<Ent> childList = null;
	
	public boolean isLeaf(){
		return (childList == null || childList.size() == 0);
	}
	public void addChild(Ent e){
		if(e.parent == this){
			return;
		}else if(e.parent != null){
			e.removeFromParent();
		}
		if(childList == null){
			childList = new ArrayList<Ent>();
		}
		childList.add(e);
	}
	public void removeFromParent(){
		if(parent != null){
			if(parent.childList != null){
				parent.childList.remove(this);
			}
			parent = null;
		}
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
	public void destroy(){
		entState = EntState.Destroyed;
		if(childList != null){
			for (Ent e : childList){
				e.destroy();
			}
		}
	}
	public void updatePostProcess(){
	}
	public void updateBBox(){
		if(isLeaf()){
		}else{
			for(Ent e: childList){
			}
		}
	}
	public void OnFirstUpdate(){}
	public void OnUpdate(){}
	public void OnDestroyed(){}
	public void OnCollision(Ent e){}
	public void draw(){}
}
