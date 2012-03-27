package engine;

import math.Vec2;
/** 
 * A raytrace represents the result of a raytrace on entities in the scene. It
 * allows to iterate over the entities colliding with the ray from closest to furthest
 * from the source of the ray, and know where the ray entered and exited the entities.
 * 
 * A Raytrace instance is only valid during the frame it was produced, and should not
 * be reused between frames. Behaviour in such cases is undefined. 
 * 
 * The raytrace will ignore an entity if the ray starts or ends (because it reached it's
 * maximum trace distance) inside the entity. 
 * 
 * The raytrace will ignore all entities that do not have a CollisionBehaviour set to 
 * 'Reciever' or 'Both'
 */

public interface Raytrace {
	/** Returns the closest entity from the current position (initially the start position provided by the constructor)
	 *  fully colliding with the ray (the ray enters and leaves the entity). 
	 *  if not null, colStart will be set to the position where the ray enters the entity. 
	 *  if not null, colEnd will be set to the position where the ray leaves the entity.
	 *  The next trace will start at 'colStart+epsilon' and will ignore the last entity returned, and
	 *  may or may not ignore entities with the same boundaries as the last entity returned.
	 *   
	 *  If there is no colliding entity, it returns null, and leaves colStart and colEnd untouched.
	 */
	public Ent next(Vec2 colStart, Vec2 colEnd);
	/** Returns the current start position of the ray */ 
	public Vec2 getPos();
	/** Returns the direction of the ray */
	public Vec2 getDir();
	/** Returns the distance from the current start position to the end of the ray */
	public float getMaxDist();
}
