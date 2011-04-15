package engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import math.*;

public class Ent {
	/** The name of the entity. It is by default the entity's class */
	private String name = "UnnamedEnt";

	public enum EntState{
		New,
		Alive,
		Destroyed
	}
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
	
	/** Set of the scenes this entity is in */
	private Set<Scene> sceneList = new HashSet<Scene>();
	
	/** The entity position, never null*/
	private Vec2 p = new Vec2();	
	/** The entity's scale, never null */
	private Vec2 s = new Vec2(1.0f,1.0f);
	/** The entity depth position. smaller z are further away from the camera */
	private float z = 0.0f;		
	/** The entity's parent entity */
	private Ent parent = null;
	/** A list of this entity's childs, may be null */
	private ArrayList<Ent> childList = null;
	
	public Ent(){
		this.p = new Vec2();
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
	/** Returns the entity's current position relative to its parent */
	public Vec2 getPos(){
		return p.dup();
	}
	/** Modifies the entity's current position relative to its parent */
	public void setPos(Vec2 p){
		this.p = p.dup();
	}
	/** Modifies the entity's current position relative to its parent */
	public void setPos(float px, float py){
		this.p.x = px;
		this.p.y = py;
	}
	/** Returns the entity's world position */
	public Vec2 getWPos(){	//TODO FIXME scale is wrong....
		if(parent != null){
			Vec2 pos = parent.getWPos();
			pos.add(p.multn(parent.s));
			return pos;
		}else{
			return p.dup();
		}
	}
	/** Sets the entity current position to the world position pos */
	public void setWPos(Vec2 pos){ 
		if(parent != null){
			Vec2 ppos = parent.getWPos();
			setPos(pos.subn(ppos).multn(1.0f/parent.s.x, 1.0f/parent.s.y ));
		}else{
			setPos(pos);
		}
	}
	/** Sets the entity current position to the world position pos */
	public void setWPos(float px, float py){
		setWPos(new Vec2(px,py));	//TODO make that a bit more efficient ...
	}
	/** Translates the entity's world position by disp */
	public void translate(Vec2 disp){
		this.p.add(disp);
	}
	/** Translates the entity's world position by (px,py) */
	public void translate(float px, float py){
		this.p.add(px,py);
	}
	/** Returns the entity's local scale */
	public Vec2 getScale(){
		return this.s.dup();
	}
	/** Sets the entity's local scale */
	public void setScale(Vec2 scale){
		this.s = scale.dup();
	}
	/** Sets the entity's local scale */
	public void setScale(float sx, float sy){
		this.s.x = sx;
		this.s.y = sy; 
	}
	/** Returns the entity's z buffer position relative to its parent */
	public float getZ(){
		return this.z;
	}
	/** Sets the entity's z buffer position relative to its parent */
	public void setZ(float z){
		this.z = z;
	}
	/** Returns the entity's absolute z buffer position */
	public float getWZ(){
		if(parent != null){
			return parent.getWZ() + z;
		}else{
			return z;
		}
	}
	/** Returns the entity's parent. May be null */
	public Ent getParent(){
		return parent;
	}
	/** Return true if the entity has no parents */
	public boolean isRoot(){
		return parent == null;
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
			return null;
		}
	}
	/** Adds a a child to this entity : TODO document this more */
	public void addChild(Ent e){
		if(e.parent == this){
			return;
		}else if(e.parent != null){
			e.removeFromParent();
		}
		if(e instanceof PhysEnt){
			Dbg.Error("Cannot make PhysEnt a child : PhysEnt must be root entities.");
			return;
		}
		e.parent = this;
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
		if(parent != null){
			if(parent.childList != null){
				parent.childList.remove(this);
			}
			parent = null;
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
	public void destroy(){
		entState = EntState.Destroyed;
		if(childList != null){
			for (Ent e : childList){
				e.destroy();
			}
		}
	}
	public void OnFirstUpdate(){}
	public void OnUpdate(){}
	public void OnDestroyed(){}
	public void draw(){}
}
