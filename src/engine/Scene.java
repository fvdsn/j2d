package engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import math.BBox;
import math.BoundIF;
import math.Vec2;

import org.lwjgl.opengl.GL11;

import engine.Ent.EntState;
import engine.Ent.CollisionBehaviour;

public class Scene {
	protected String name = "Unnamed Scene";
	protected ArrayList<Ent> entityList = new ArrayList<Ent>();
	protected ArrayList<Ent> rootEntityList = new ArrayList<Ent>();
	protected ArrayList<Ent> newEntityList = new ArrayList<Ent>();
	protected ArrayList<PhysEnt> physEntityList = new ArrayList<PhysEnt>();
	protected ArrayList<Ent> destroyedEntityList = new ArrayList<Ent>();
	protected BoundIF activeRegion = null;
	
	protected ProximityMapIF proximityMap = null;
	
	protected Hashtable<Long,Ent> entityByUid = new Hashtable<Long,Ent>();
	protected Hashtable<String,Set<Ent>> entityByName = new Hashtable<String,Set<Ent>>();
	
	protected ArrayList<Effect> effectList = new ArrayList<Effect>();
	
	public class SceneRaytrace implements Raytrace{
		private ArrayList<Ent> entityList;
		private float maxDist;
		private Vec2 pos;
		private Vec2 dir;
		private Vec2 epsilon;
		public SceneRaytrace(ArrayList<Ent> entityList, Vec2 start, Vec2 dir, float maxDist){
			this.entityList = entityList;
			this.maxDist = maxDist;
			this.pos = start;
			this.dir = dir;
			this.epsilon = dir.setLengthn(0.0001f);
		}
		@Override
		public Ent next(Vec2 colStart, Vec2 colEnd) {
			ArrayList<Ent> colliders = new ArrayList<Ent>();
			Ent closest = null;
			float closestDist = Float.MAX_VALUE;
			Vec2 cols = new Vec2();
			Vec2 cole = new Vec2();
			Vec2 colS = new Vec2();
			Vec2 colE = new Vec2();
			if(entityList.size() == 0){
				return null;
			}
			for(Ent e: entityList){
				if(	(e.collisionBehaviour == Ent.CollisionBehaviour.Receiver ||
						e.collisionBehaviour == Ent.CollisionBehaviour.Both )
						&& !e.contains(pos)
						&& e.rayCollides(pos, dir, maxDist, cols, cole)){
					if(!cols.equals(cole)){
						colliders.add(e);
						float dist = pos.distSquared(cols);
						if(dist < closestDist){
							closestDist = dist;
							closest = e;
							colS.copy(cols);
							colE.copy(cole);
						}
					}
				}
			}
			if(colliders.size() > 0){
				colliders.remove(closest);
				pos.copy(colS);
				pos.sub(epsilon);
				maxDist -= Math.sqrt(closestDist);
				entityList = colliders;
				if(colStart != null){
					colStart.copy(colS);
				}
				if(colEnd != null){
					colEnd.copy(colE);
				}
				return closest;
			}
			return null;
		}
		public Vec2 getPos(){
			return pos.dup();
		}
		public Vec2 getDir(){
			return dir.dup();
		}
		public float getMaxDist(){
			return maxDist;
		}
		
	}
	
	public void addEffect(float delay, Effect effect){
		effect.time  = Main.time + delay;
		effect.duration = 0.0f;
		for(int i = 0; i < effectList.size(); i++){
			if(effect.time < effectList.get(i).time ){
				effectList.add(i, effect);
				return;
			}
		}
		effectList.add(effect);
	}
	public void addEffect(float delay, float duration, Effect effect){
		addEffect(delay, effect);
		effect.duration = duration;
	}
	protected void processEffects(){
		int index = 0;
		while(effectList.size() > index && Main.time >= effectList.get(index).time){
			Effect e = effectList.get(index);
			e.effect();
			if(e.duration > 0.0f && Main.time < e.time + e.duration){
				index++;
			}else{
				effectList.remove(0);
			}
		}
	}
	
	public void setProximityMap(ProximityMapIF map){
		this.proximityMap = map;
	}
	public void setActiveRegion(BoundIF b){
		activeRegion = b;
	}
	
	private boolean isInActiveRegion(Ent e){
		if(activeRegion != null){
			return activeRegion.collides(e);
		}else{
			return true;
		}
	}
	/** Adds an Entity and all its childs to the scene : TODO document this more */
	public void addEntity(Ent e){
		if(!e.getSceneList().contains(this)){
			newEntityList.add(e);
			e.getSceneList().add(this);
			entityByUid.put(e.uid, e);
			if(!entityByName.containsKey(e.getName())){
				Set<Ent> s = new HashSet<Ent>();
				s.add(e);
				entityByName.put(e.getName(), s);
			}else{
				entityByName.get(e.getName()).add(e);
			}
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
			entityByUid.remove(e.uid);
			Set s = entityByName.get(e.getName());
			s.remove(e);
			if(s.isEmpty()){
				entityByName.remove(e.getName());
			}
			if(e.isRoot()){
				rootEntityList.remove(e);
			}
			e.getSceneList().remove(this);
		}
	}
	public Ent getEntByUid(long uid){
		return entityByUid.get(uid);
	}
	public Ent getEntByName(String name){
		Set<Ent> s = entityByName.get(name);
		if(s != null){
			for(Ent e : s){
				return e;
			}
			return null;
		}else{
			return null;
		}
	}
	public Set<Ent> getAllEntByName(String name){
		Set<Ent> s = entityByName.get(name);
		if(s != null && !s.isEmpty()){
			Set<Ent> s2 = new HashSet<Ent>();
			s2.addAll(s);
			return s2;
		}else{
			return null;
		}
	}
	public List<Ent> getAllEnt(){
		return Collections.unmodifiableList(entityList);
	}
	public List<Ent> getAllRootEnt(){
		return Collections.unmodifiableList(rootEntityList);
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
		long startTime,endTime;
		/** Processing events */
		processEffects();
		/* Adding new entities to the entity List */
		
		Statistics.entNewCount = newEntityList.size();
		
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
				//e.OnFirstUpdate(); FIXME adding entities in this modifies newEntityList that is currently being iterated...
				//e.OnUpdate();
			}
		}
		/* Calling update to the entity list */
		newEntityList.clear();
		startTime = System.currentTimeMillis();
		Statistics.entCount    = entityList.size();
		for(Ent e : entityList){
			if(isInActiveRegion(e)){
				Statistics.entUpdatedCount++;
				entityUpdate(e);
			}
			if( e.destroyTime <= Main.time){
				e.destroy();
			}
		}
		endTime = System.currentTimeMillis();
		Statistics.updateTime = (int)(endTime - startTime);
		
		/* Applying Physics */
		startTime = System.currentTimeMillis();
		for(PhysEnt e : physEntityList){
			if(isInActiveRegion(e)){
				Statistics.entPhysUpCount++;
				e.doPhysics();
			}
		}
		endTime = System.currentTimeMillis();
		Statistics.physicsTime = (int)(endTime - startTime);
		
		/* Applying Collisions */
		startTime = System.currentTimeMillis();
		if(proximityMap == null){
			for(Ent e: rootEntityList){
				if(!isInActiveRegion(e)){
					continue;
				}
				if(	e.collisionBehaviour == CollisionBehaviour.Emitter ||
						e.collisionBehaviour == CollisionBehaviour.Both){
					Statistics.entCEmitterCount++;
					for(Ent r: rootEntityList){
						if(!isInActiveRegion(r)){
							continue;
						}
						if( (r.collisionBehaviour == CollisionBehaviour.Receiver ||
								r.collisionBehaviour == CollisionBehaviour.Both )
								&& e != r){
							Statistics.entCReceiverCount++;
							if( e.collides(r)){
								Statistics.entCollisionCount++;
								Vec2 colVec = e.collisionVector(r);
								e.OnCollisionEmit(r,colVec);
								r.OnCollisionReceive(e,colVec.negaten());
							}
						}
					}
				}
			}
		}else{
			/*Building the map */
			proximityMap.reset();
			for(Ent e: rootEntityList){
				if(isInActiveRegion(e)){
					if(e.collisionBehaviour == CollisionBehaviour.Receiver ||
							e.collisionBehaviour == CollisionBehaviour.Both){
						Statistics.entCEmitterCount++;
						proximityMap.addEnt(e);
					}
				}
			}
			/*Checking for collisions with help of the map*/
			for(Ent e: rootEntityList){
				if(!isInActiveRegion(e)){
					continue;
				}
				if(e.collisionBehaviour == CollisionBehaviour.Emitter ||
						e.collisionBehaviour == CollisionBehaviour.Both){
					for(Ent r: proximityMap.getCloseEnt(e)){
						Statistics.entCReceiverCount++;
						if(e != r && e.collides(r)){
							Statistics.entCollisionCount++;
							Vec2 colVec = e.collisionVector(r);
							e.OnCollisionEmit(r,colVec);
							r.OnCollisionReceive(e,colVec.negaten());
						}
					}
				}
			}
		}
		endTime = System.currentTimeMillis();
		Statistics.collisionTime = (int)(endTime - startTime);
		
		/* Destroying entities */
		for(Ent e : entityList){
			if(e.entState == EntState.Destroyed){
				destroyedEntityList.add(e);
			}
		}
		Statistics.entDestroyedCount = destroyedEntityList.size();
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
		GL11.glTranslatef(e.getLocalPosX(), e.getLocalPosY(), e.getLocalZ());
		GL11.glScalef(e.getLocalScaleX(), e.getLocalScaleY(), 1.0f);
		if(e.renderActive){
			e.OnDraw();
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
		Collections.sort(rootEntityList);
		for(Ent e: rootEntityList){
			if(this.isInActiveRegion(e)){
				e.sortChildsZ();
				drawEntity(e);
			}
		}
	}
	public Raytrace raytrace(Vec2 start, Vec2 dir, float maxDist){
		return new SceneRaytrace(rootEntityList,start,dir,maxDist);
	}
}
