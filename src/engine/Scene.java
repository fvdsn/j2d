package engine;

import java.util.ArrayList;

import math.BBox;
import math.Vec2;

import org.lwjgl.opengl.GL11;

import engine.Ent.EntState;
import engine.PhysEnt.CollisionBehaviour;

public class Scene {
	public String name = "Unnamed Scene";
	public ArrayList<Ent> entityList = new ArrayList<Ent>();
	public ArrayList<Ent> rootEntityList = new ArrayList<Ent>();
	public ArrayList<Ent> newEntityList = new ArrayList<Ent>();
	public ArrayList<PhysEnt> physEntityList = new ArrayList<PhysEnt>();
	public ArrayList<Ent> destroyedEntityList = new ArrayList<Ent>();
	
	/** Adds an Entity and all its childs to the scene : TODO document this more */
	public void addEntity(Ent e){
		if(!e.getSceneList().contains(this)){
			newEntityList.add(e);
			e.getSceneList().add(this);
		}
		if(!e.isLeaf()){
			for (Ent ec : e.getChildList()){
				addEntity(ec);
			}
		}
	}
	/** Removes an entity and all its childs from the scene : TODO document this more */
	public void removeEntity(Ent e){
		if(!e.isLeaf()){
			for(Ent e2 : e.getChildList()){
				removeEntity(e2);
			}
		}if(e.getSceneList().contains(this)){
			newEntityList.remove(e);
			entityList.remove(e);
			if(e.isRoot()){
				rootEntityList.remove(e);
			}
			e.getSceneList().remove(this);
		}
	}
	private void entityUpdate(Ent e){
		if(e.active){
			if(e.entState == EntState.New){
				e.entState = EntState.Alive;
				e.entCurrentFrame = Main.frame;
				e.OnFirstUpdate();
				e.OnUpdate();
			}else if(e.entCurrentFrame != Main.frame){
				e.entCurrentFrame = Main.frame;
				e.OnUpdate();
			}
		}
		if(!e.isLeaf()){
			for(Ent e2: e.getChildList()){
				entityUpdate(e2);
			}
		}
	}
	public void update(){
		/* Adding new entities to the entity List */
		for(Ent e : newEntityList){
			entityList.add(e);
			if(e.isRoot()){
				rootEntityList.add(e);
			}
			if(e instanceof PhysEnt){
				physEntityList.add((PhysEnt)e);
			}
			if(e.entState == EntState.New && e.active){
				e.entState = EntState.Alive;
				e.entCurrentFrame = Main.frame;
				e.OnFirstUpdate();
				e.OnUpdate();
			}
		}
		/* Calling update to the entity list */
		newEntityList.clear();
		for(Ent e : entityList){
			entityUpdate(e);
			if(e.entState == EntState.Destroyed){
				destroyedEntityList.add(e);
			}
		}
		/* Applying Physics */
		for(PhysEnt e : physEntityList){
			e.doPhysics();
		}
		/* Applying Collisions */
		for(PhysEnt e: physEntityList){
			if(	e.collisionBehaviour == CollisionBehaviour.Emitter ||
				e.collisionBehaviour == CollisionBehaviour.Both){
				for(PhysEnt r: physEntityList){
					if( (e.collisionBehaviour == CollisionBehaviour.Receiver ||
						 e.collisionBehaviour == CollisionBehaviour.Both )
						 && e != r){
						//TODO self Collisions ...
						BBox eb = e.getBounds();
						BBox rb = r.getBounds();
						if( eb.collides(rb)){
							Vec2 colVec = eb.collisionVector(rb);
							e.OnCollisionEmit(r,colVec);
							r.OnCollisionReceive(e,colVec.negaten());
						}
					}
				}
			}
		}
		/* Destroying entities */
		for(Ent e : destroyedEntityList){
			entityList.remove(e);
			if(e.isRoot()){
				rootEntityList.remove(e);
			}
			if(e instanceof PhysEnt){
				physEntityList.remove(e);
			}
			e.OnDestroyed();
		}
		destroyedEntityList.clear();
	}
	private void drawEntity(Ent e){
		GL11.glPushMatrix();
		GL11.glTranslatef(e.getPos().x, e.getPos().y, e.getZ());
		GL11.glScalef(e.getScale().x, e.getScale().y, 1.0f);
		if(e.renderActive){
			e.draw();
		}
		if(e.renderChildsActive){
			if(!e.isLeaf()){
				for(Ent ec: e.getChildList()){
					drawEntity(ec);
				}
			}
		}
		GL11.glPopMatrix();
	}
	public void draw(){
		/* Displaying entities : */
		for(Ent e: rootEntityList){
			drawEntity(e);
		}
	}
}
