package engine;

import java.util.ArrayList;

import engine.Ent.EntState;

public class Scene {
	public String name = "Unnamed Scene";
	public ArrayList<Ent> entityList = new ArrayList<Ent>();
	public ArrayList<Ent> newEntityList = new ArrayList<Ent>();
	public ArrayList<Ent> destroyedEntityList = new ArrayList<Ent>();
	
	public void addEntity(Ent e){
		newEntityList.add(e);
		if(e.childList != null){
			for (Ent ec : e.childList){
				addEntity(ec);
			}
		}
	}
	public void removeEntity(Ent e){
		if(e.childList != null){
			for(Ent e2 : e.childList){
				removeEntity(e2);
			}
		}
		newEntityList.remove(e);
		entityList.remove(e);
	}
	private void entityUpdate(Ent e){
		if(e.entState == EntState.New){
			e.entState = EntState.Alive;
			e.entCurrentFrame = Main.frame;
			e.OnFirstUpdate();
			e.OnUpdate();
		}else if(e.entCurrentFrame != Main.frame){
			e.entCurrentFrame = Main.frame;
			e.OnUpdate();
		}
		if(e.childList != null){
			for(Ent e2: e.childList){
				entityUpdate(e2);
			}
		}
	}
	public void runFrame(){
		/* Adding new entities to the entity List */
		for(Ent e : newEntityList){
			entityList.add(e);
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
			if(e.entCurrentFrame < Main.frame){
				e.entCurrentFrame = Main.frame;
				entityUpdate(e);
			}
			if(e.entState == EntState.Destroyed){
				destroyedEntityList.add(e);
			}
		}
		/* Destroying entities */
		for(Ent e : destroyedEntityList){
			entityList.remove(e);
			e.OnDestroyed();
		}
		destroyedEntityList.clear();
		/* Postprocessing entities : applying fieldsNext to fields */
		for(Ent e: entityList){
			e.updatePostProcess();
		}
		/* Displaying entities : */
		for(Ent e: entityList){
			e.draw();
		}
	}
}
