package math;

import engine.Draw;
import engine.Main;

/** The base implementation of BoundIF. This implementation represents a simple point,
 * and has little practical use beyond a base class for new type of BoundIF.
 * See BBox for an axis aligned Bounding Box 
 *
 */
public class Bound implements BoundIF {
	protected BoundIF parent = null;
	protected float px = 0.0f;
	protected float py = 0.0f;
	protected float sx = 1.0f;
	protected float sy = 1.0f;

	public Bound(){
	}
	public Bound(float px, float py){
		this.px = px;
		this.py = py;
	}
	public Bound(Vec2 pos){
		px = pos.x;
		py = pos.y;
	}
	@Override 
	public void setParent(BoundIF parent){
		this.parent = parent;
	}
	@Override
	public BoundIF getParent(){
		return parent;
	}
	@Override
	public boolean isRoot(){
		return parent == null;
	}
	@Override
	public Vec2 getPos() {
		if(parent == null){
		return new Vec2(px,py);
		}else{
			return new Vec2(parent.getPosX()+px,
							parent.getPosY()+py);
		}
	}
	@Override
	public float getPosX() {
		if(parent == null){
			return px;
		}else{
			return parent.getPosX() + px * parent.getScaleX();
		}
	}
	@Override
	public float getPosY(){
		if(parent == null){
			return py;
		}else{
			return parent.getPosY() + py * parent.getScaleY();
		}
	}
	@Override
	public void setPos(Vec2 pos) {
		if(parent == null){
			px = pos.x;
			py = pos.y;
		}else{
			float ppx = parent.getPosX();
			float ppy = parent.getPosY();
			px = (pos.x - ppx) / parent.getScaleX();
			py = (pos.y - ppy) / parent.getScaleY();
		}
	}
	@Override
	public void setPos(float posx, float posy) {
		if(parent == null){
			px = posx;
			py = posy;
		}else{
			float ppx = parent.getPosX();
			float ppy = parent.getPosY();
			px = (posx - ppx) / parent.getScaleX();
			py = (posy - ppy) / parent.getScaleY();
		}
	}
	@Override
	public void setLocalPos(Vec2 pos){
		px = pos.x;
		py = pos.y;
	}
	@Override
	public void setLocalPos(float posx, float posy){
		px = posx;
		py = posy;
	}
	@Override
	public float getLocalPosX(){
		return px;
	}
	@Override
	public float getLocalPosY(){
		return py;
	}
	@Override
	public Vec2 getLocalPos(){
		return new Vec2(px,py);
	}
	@Override
	public float dist(BoundIF b){
		return getPos().dist(b.getPos());
	}
	@Override
	public float dist(Vec2 b){
		return getPos().dist(b);
	}
	@Override
	public Vec2 distVec(BoundIF b){
		return getPos().distVecn(b.getPos());
	}
	@Override
	public Vec2 distVec(Vec2 b){
		return getPos().distVecn(b);
	}

	@Override
	public float l() {
		return getPosX();
	}

	@Override
	public float r() {
		return getPosX();
	}

	@Override
	public float u() {
		return getPosY();
	}

	@Override
	public float d() {
		return getPosY();
	}

	@Override
	public boolean contains(Vec2 pos) {
		return 		pos.x >= l() && pos.x <= r()
		&&	pos.y >= d() && pos.y <= u() ; 
	}
	@Override
	public boolean contains(BoundIF b) {
		return  	b.l() >= l()  && b.r() <= r()
		&&  b.d() >= d() && b.u() <= u();
	}
	
	@Override
	public boolean collides(BoundIF b) {
		if(!boundCollides(l(),r(),b.l(),b.r())){
			return false;
		}
		if(!boundCollides(d(),u(),b.d(),b.u())){
			return false;
		}
		return true;
	}
	@Override
	public Vec2 collisionVector(BoundIF b) {
		float dx = boundEscapeDist(l(), r(), b.l(), b.r());
		float dy = boundEscapeDist(d(),u(), b.d(), b.u());
		if(Math.abs(dx) < Math.abs(dy)){
			return new Vec2(dx,0);
		}else{
			return new Vec2(0,dy);
		}
	}
	@Override
	public Vec2 collisionAxis(BoundIF b) {
		float dx = boundEscapeDist(l(), r(), b.l(), b.r());
		float dy = boundEscapeDist(d(),u(), b.d(), b.u());
		return new Vec2(dx,dy);
	}
	@Override
	public void translate(Vec2 disp) {
		px += disp.x;
		py += disp.y;
	}
	@Override
	public void translate(float dx, float dy) {
		px += dx;
		py += dy;
	}
	@Override
	public void scale(float factor){
		sx *= factor;
		sy *= factor;
	}
	@Override
	public void scale(Vec2 factor){
		sx *= factor.x;
		sy *= factor.y;
	}
	@Override
	public void scale(float fx, float fy){
		sx *= fx;
		sy *= fy;
	}
	@Override
	public void setLocalScale(Vec2 scale){
		sx = scale.x;
		sy = scale.y;
	}
	@Override
	public void setLocalScale(float scalex, float scaley){
		sx = scalex;
		sy = scaley;
	}
	@Override
	public Vec2 getScale(){
		if(parent == null){
			return new Vec2(sx,sy);
		}else{
			return new Vec2(sx*parent.getScaleX(), 
							sy*parent.getScaleY());
		}
	}
	@Override 
	public float getScaleX(){
		if(parent == null){
			return sx;
		}else{
			return sx*parent.getScaleX();
		}
	}
	@Override
	public float getScaleY(){
		if(parent == null){
			return sy;
		}else{
			return sx*parent.getScaleY();
		}
	}
	@Override
	public Vec2 getLocalScale(){
		return new Vec2(sx,sy);
	}
	@Override
	public float getLocalScaleX(){
		return sx;
	}
	@Override
	public float getLocalScaleY(){
		return sy;
	}
	@Override
	public BoundIF dup() {
		return new Bound(px,py);
	}

	@Override
	public BoundIF dupAt(Vec2 pos) {
		return new Bound(pos);
	}
	@Override
	public BoundIF dupAtZero() {
		return new Bound();
	}
	public String toString(){
		return "["+getPosX()+","+getPosY()+"]";
	}
	protected static boolean boundCollides(float amin, float amax, float bmin, float bmax){
		if(amin + amax < bmin + bmax){
			return amax > bmin;
		}else{
			return amin < bmax;
		}
	}
	/* The distance to displace amin to escape to amax. */
	protected static float boundEscapeDist(float amin, float amax, float bmin, float bmax){
		if(amin + amax < bmin + bmax){
			float disp = bmin - amax;
			if(disp >= 0){
				return 0;
			}else{
				return disp;
			}
		}else{
			float disp = bmax - amin;
			if(disp <= 0){
				return 0;
			}else{
				return disp;
			}
		}
		
	}
	@Override
	public Vec2 getRandomPos(){
		float fx = Main.rng.nextFloat();
		float fy = Main.rng.nextFloat();
		return new Vec2(fx * l() + (1-fx) * r(),
						fy * d() + (1-fy) * u());
	}
	private float rayColX(Vec2 rayStart, Vec2 rayDir, float y){
		/* X = rs.x + rd.x*t
		 * Y = rs.y + rd.y*t
		 * t = (Y - rs.y)/rd.y
		 * X = rs.x + rd.x*(...)
		 */
		return rayStart.x + rayDir.x *( (y-rayStart.y)/rayDir.y );
	}
	private float rayColY(Vec2 rayStart, Vec2 rayDir, float x){
		return rayStart.y + rayDir.y *( (x-rayStart.x)/rayDir.x);
	}
	public boolean rayCollides(Vec2 rs, Vec2 rd, float endDist){
		float cxd, cxu, cyl, cyr;
		Vec2	vcol;
		Vec2	tmp = new Vec2();
		float l = l();
		float r = r();
		float u = u();
		float d = d();
		if(			rs.x > r && rd.x > 0 || rs.x < l && rd.x < 0 
				||  rs.y > u && rd.y > 0 || rs.y < d && rd.y < 0){
			return false;
		}
		endDist *= endDist;
		if(rd.x != 0){
			cyl = rayColY(rs,rd,l);
			if(cyl >= d && cyl <= u){
				vcol = new Vec2(l,cyl);
				tmp.copy(vcol);
				tmp.sub(rs);
				if(tmp.x * rd.x > 0){	//tmp.x * rd.x > 0
					float dist = tmp.lengthSquared();	//		rs.distSquared(vcol);
					if(dist <= endDist){
						return true;
					}
				}
			}
			cyr = rayColY(rs,rd,r);
			if(cyr >= d && cyr <= u){
				vcol = new Vec2(r,cyr);
				tmp.copy(vcol);
				tmp.sub(rs);
				if(tmp.x * rd.x > 0){
					float dist = tmp.lengthSquared();	//		rs.distSquared(vcol);
					if(dist <= endDist){
						return true;
					}
				}
			}
		}
		if(rd.y != 0){
			cxd = rayColX(rs,rd,d);
			if(cxd >= l && cxd <= r){
				vcol = new Vec2(cxd,d);
				tmp.copy(vcol);
				tmp.sub(rs);
				if(tmp.y * rd.y > 0){
					float dist = tmp.lengthSquared();	//		rs.distSquared(vcol);
					if(dist <= endDist){
						return true;
					}
				}
			}
			cxu = rayColX(rs,rd,u);
			if(cxu >= l && cxu <= r){
				vcol = new Vec2(cxu,u);
				tmp.copy(vcol);
				tmp.sub(rs);
				if(tmp.y * rd.y > 0){
					float dist = tmp.lengthSquared();	//		rs.distSquared(vcol);
					if(dist <= endDist){
						return true;
					}
				}
			}
		}
		return false;
	}
	public boolean rayCollides(Vec2 rs, Vec2 rd, float endDist, Vec2 colStart, Vec2 colEnd){
		float cxd, cxu, cyl, cyr, mindist = Float.MAX_VALUE, maxdist = Float.MIN_VALUE;
		boolean col = false;
		Vec2	vcol,vclose = null,vfar = null;
		Vec2	tmp = new Vec2();
		float l = l();
		float r = r();
		float u = u();
		float d = d();
		if(			rs.x > r && rd.x > 0 || rs.x < l && rd.x < 0 
				||  rs.y > u && rd.y > 0 || rs.y < d && rd.y < 0){
			return false;
		}
		endDist *= endDist;
		if(rd.x != 0){
			cyl = rayColY(rs,rd,l);
			if(cyl >= d && cyl <= u){
				vcol = new Vec2(l,cyl);
				tmp.copy(vcol);
				tmp.sub(rs);
				if(tmp.x * rd.x > 0){	//tmp.x * rd.x > 0
					float dist = tmp.lengthSquared();	//		rs.distSquared(vcol);
					if(dist <= endDist){
						col = true;
						if(dist < mindist){
							mindist = dist;
							vclose = vcol;
						}
						if(dist > maxdist){
							maxdist = dist;
							vfar = vcol;
						}
					}
				}
			}
			cyr = rayColY(rs,rd,r);
			if(cyr >= d && cyr <= u){
				vcol = new Vec2(r,cyr);
				tmp.copy(vcol);
				tmp.sub(rs);
				if(tmp.x * rd.x > 0){
					float dist = tmp.lengthSquared();	//		rs.distSquared(vcol);
					if(dist <= endDist){
						col = true;
						if(dist < mindist){
							mindist = dist;
							vclose = vcol;
						}
						if(dist > maxdist){
							maxdist = dist;
							vfar = vcol;
						}
					}
				}
			}
		}
		if(rd.y != 0){
			cxd = rayColX(rs,rd,d);
			if(cxd >= l && cxd <= r){
				vcol = new Vec2(cxd,d);
				tmp.copy(vcol);
				tmp.sub(rs);
				if(tmp.y * rd.y > 0){
					float dist = tmp.lengthSquared();	//		rs.distSquared(vcol);
					if(dist <= endDist){
						col = true;
						if(dist < mindist){
							mindist = dist;
							vclose = vcol;
						}
						if(dist > maxdist){
							maxdist = dist;
							vfar = vcol;
						}
					}
				}
			}
			cxu = rayColX(rs,rd,u);
			if(cxu >= l && cxu <= r){
				vcol = new Vec2(cxu,u);
				tmp.copy(vcol);
				tmp.sub(rs);
				if(tmp.y * rd.y > 0){
					float dist = tmp.lengthSquared();	//		rs.distSquared(vcol);
					if(dist <= endDist){
						col = true;
						if(dist < mindist){
							mindist = dist;
							vclose = vcol;
						}
						if(dist > maxdist){
							maxdist = dist;
							vfar = vcol;
						}
					}
				}
			}
		}
		if(col){
			if(colStart != null && vclose != null){
				colStart.copy(vclose);
			}
			if(colEnd != null && vfar != null){
				colEnd.copy(vfar);
			}
			return true;
		}
		return false;
	}



}
