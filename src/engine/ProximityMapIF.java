package engine;

import java.util.Set;

import math.BoundIF;
import math.Vec2;
/** 
 * A proximity Map maps allows to efficiently list the entities that are
 * close to another entity and is used to accelerate collision detection.
 * 
 * Proximity Maps are currently entirely rebuilt on each frame. A possible
 * improvement would be to make them updatable, where only the entity that changed
 * shape the previous frame are moved in the map. 
 * 
 * A proposal of API is seen commented below. However, current ArrayProximityMap
 * provides sufficient performances for it to be a non-issue. Feel free to improve :)
 *
 */
public interface ProximityMapIF {
	/** Adds an entity in the ProximityMap.
	 *  Modifying an entity after it has been added to the map has unspecified effects
	 *  and getCloseEnt() may not return a correct value */
	public void addEnt(Ent e);
	/** Returns the set of entities that are 'close' to a Bound e. 'close' is
	 *  a loose definition based on what gives the most performances. The only 
	 *  restriction is that colliding entities are most certainly 'close'.
	 *  An entity should not be added multiple times to the same map.
	 */
	public Set<Ent> getCloseEnt(BoundIF e);
	/** Returns the set of entities that are close to a position. 'close' is
	 *  a loose definition based on what gives the most performances. The only
	 *  restriction is that the entities that contains the position are most certainly
	 *  'close' to it.
	 */
	public Set<Ent> getCloseEnt(Vec2 p);
	/** Removes all entities from the map. Must be at least as efficient as creating
	 *  a new ProximityMap from scratch.
	 */
	public void reset();
	
	/*
	 *  // returns true if the map implements the updateEnt() and removeEnt() methods 
	 *  // Scene collision behaviour should use this method to use the best collision 
	 *  // detection algorithm available.
	 * public boolean isUpdatable();
	 *  // The Ent e is an entity already in the Map, whose shape or position has changed
	 *  // from the last update / time it was added. Applying this method to the entity
	 *  // ensure that the map is coherent relative to this entity
	 * public void updateEnt(Ent e);
	 *  // removes an entity from the map. The entity's shape must not have been modified
	 *  // since it was last updated / added to the map
	 * public void removeEnt(Ent e);
	 * 
	 */
	
	/**
	 * public boolean isRaytracable();
	 * public Raytrace raytrace(Vec2 start, Vec2 dir, float maxDist)
	 */
}
