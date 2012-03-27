package math;

import java.io.Serializable;

public interface BoundIF extends Serializable{
	/** Changes the parent of this bound. The child bound will appear scaled by it's parent
	 *  scale, and its world position will be relative to its parent world position, scaled by its parent
	 *  scale.
	 *  If parent is null, then it removes any parent child relationship.
	 * @param parent
	 */
	public void		setParent(BoundIF parent);
	/** Returns the parent bound of this bound. returns null if there is no parent */
	public BoundIF	getParent();
	/** Returns true if this bound has no parent */
	public boolean	isRoot();
	/** Returns the x coordinate of the bound's center in world coordinate*/
	public float	getPosX();
	/** Returns the y coordinate of the bound's center in world coordinate*/
	public float	getPosY();
	/** Returns the bound's center position in world coordinates*/
	public Vec2 	getPos();
	/** Returns the x coordinate of the bound's center relative to its parent center in local coordinates */
	public float	getLocalPosX();
	/** Returns the y coordinate of the bound's center relative to its parent center in local coordinates */
	public float	getLocalPosY();
	/** Return the bound's center position relative to itś parent center in local coordinates */
	public Vec2		getLocalPos();
	/** Changes the bound's center position relative to its parent center, in local coordinates, pos cannot be null */
	public void		setLocalPos(Vec2 pos);
	/** Changes the bound's center position relative to its parent center, in local coordinates */
	public void		setLocalPos(float lposx, float lposy);
	/** Changes the bound's center position in world coordinate to 'pos', 'pos' cannot be null */
	public void 	setPos(Vec2 pos);
	/** Changes the bound's center position in world coordinates to (posx,posy) */
	public void 	setPos(float posx, float posy);
	/** Translates the bound by the vector 'disp' in local coordinates. 'disp' cannot be null */
	public void 	translate(Vec2 disp);
	/** Translates the bound by the vector (dx,dy) in local coordinates.*/
	public void 	translate(float dx, float dy);
	/** Scales the bound around its center by 'factor'. 'factor' must be positive */
	public void		scale(float factor);
	/** Scales the bound around its center by 'factor.x' on the x axis, 
	 * and 'factor.y' on the y axis. 'factor' must not be null and its component
	 * must be positive. 
	 * Some bounds implmentations may not be able to be scaled differently on different axes,
	 * in this case they must scale uniformly by the largest component.
	 */
	public void 	scale(Vec2 factor);
	/** Scales the bound around its center by 'fx' on the x axis, 
	 * and 'fy' on the y axis. fx and fy must be positive
	 * Some bounds implmentations may not be able to be scaled differently on different axes,
	 * in this case they must scale uniformly by the largest component.
	 */
	public void 	scale(float fx, float fy);
	/** Sets the local scale of the bound to the component of 'scale'. 
	 * 'scale' cannot be null and its components must be positive.
	 */
	public void		setLocalScale(Vec2 scale);
	/** Sets the local scale of the bound to (sx,sy). 
	 * 'sx' and 'sy' must be positive.
	 */
	public void		setLocalScale(float sx, float sy);
	/** Returns the world scale of the bound */
	public Vec2		getScale();
	/** Return the world scale of the bound on the X axis (horizontal) */
	public float	getScaleX();
	/** Return the world scale of the bound on the Y axis (vertical) */
	public float	getScaleY();
	/** Return the local scale of the bound */
	public Vec2		getLocalScale();
	/** Return the local scale of the bound on the X axis (horizontal) */
	public float	getLocalScaleX();
	/** Return the local scale of the bound on the Y axis (vertical) */
	public float	getLocalScaleY();

	
	
	/** Returns a duplicate of the bound. 
	 * Pparent/child relationship are lost, 
	 * Local properties become world properties */
	public BoundIF	dup();
	/** Returns a duplicated of the bound centered at 'pos'. 'pos' must not be null.
	 * Parent/child relationship are lost 
	 * Local properties become world properties */
	public BoundIF	dupAt(Vec2 pos);
	/** Returns a duplicated of the bound centered on (0,0)
	 * Parent/child relationship are lost 
	 * Local properties become world properties */
	public BoundIF	dupAtZero();
	/** Returns the minimum x coordinate contained in the bound */
	public float 	l();
	/** Returns the maximum x coordinate contained in the bound */
	public float 	r();
	/** Returns the maximum y coordinate contained in the bound */
	public float 	u();
	/** Returns the minimum y coordinate contained in the bound */
	public float 	d();
	/** Returns true if pos is inside of this bound. (Boundaries excluded) */
	public boolean 	contains(Vec2 pos);
	/** Returns true if the Bound b is inside of this bound (Boundaries excluded for both bounds) */
	public boolean 	contains(BoundIF b);
	/** Returns true if the Bound b collides with this bound */
	public boolean 	collides(BoundIF b);
	/** Returns the smallest vector such that translating the bound by this vector prevents
	 * collision with the Bound b
	 */
	public Vec2	   	collisionVector(BoundIF b);
	/** Returns the vector (x,y) ;
	 * 	- x is the minimum x coord displacement of this BBox to avoid collision with b.
	 *  - y is the minimum y coord displacement of this BBox to avoid collision with b 
	 *  Displacing the BBox by (x,0) or (0,y) is sufficient to avoid the collision.
	 *  If x or y is 0.0, b does not collides with the BBox */
	public Vec2		collisionAxis(BoundIF b);
	
	/** Returns a position inside the box chosen with a random uniform distribution */
	public Vec2		getRandomPos();
	public boolean rayCollides(Vec2 rayStart, Vec2 rayDir, float maxDist, Vec2 colStart, Vec2 colEnd);
	public boolean rayCollides(Vec2 rayStart, Vec2 rayDir, float maxDist);
	
	/** Returns the world distance between the world position of this Bound's center
	 * to the world position of b's center 
	 */
	public float dist(BoundIF b);
	/** Returns the world distance between the world position of this Bound's center
	 * to the world position b
	 */
	public float dist(Vec2 b);
	/** Returns a vector from this Bound's world center to b's world center */
	public Vec2  distVec(BoundIF b);
	/** Returns a vector from this Bound's world center to v */
	public Vec2  distVec(Vec2 v);
	/** 
	 * public boolean rayCollides(Vec2 rayStart, Vec2 rayDir, float maxDist);
	 * public boolean rayCollides(Vec2 rayStart, Vec2 rayDir, float maxDist, Vec2 colStart, Vec2 colEnd);
	 */
	
	
}
