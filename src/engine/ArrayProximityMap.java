package engine;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import math.*;

public class ArrayProximityMap implements ProximityMapIF{
	private BBox bound;
	private float cellsize;
	private float invcellsize;
	private int   cellx;
	private int   celly;
	private Object[] array;
	private static int _rayUID = 13;

	private class ArrayRaytrace implements Raytrace{
		private float px,py;
		private float dx,dy;
		private float maxDist;
		private int   cellIndex;
		private int   rayUID = _rayUID++;
		
		public ArrayRaytrace(Vec2 pos, Vec2 dir, float maxDist){
			this.px = pos.x;
			this.py = pos.y;
			this.dx = dir.x;
			this.dy = dir.y;
			this.maxDist = maxDist;
			this.cellIndex = (int)((pos.x - bound.l())*invcellsize) % cellx;

		}
		@Override
		public Ent next(Vec2 colStart, Vec2 colEnd) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Vec2 getPos() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Vec2 getDir() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public float getMaxDist() {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}
	
	public ArrayProximityMap(BBox bound, float cellsize){
		this.bound = bound.dup();
		this.cellsize = cellsize;
		this.invcellsize = 1.0f/cellsize;
		this.cellx = (int)(bound.getSizeX() * invcellsize);
		this.celly = (int)(bound.getSizeY() * invcellsize);
		this.array = new Object[cellx*celly];
	}
	@Override
	public void reset(){
		this.array = new Object[cellx*celly];
	}
	@Override
	public void addEnt(Ent e) {
		int pl = (int)((e.l() - bound.l())*invcellsize);
		int pr = (int)((e.r() - bound.l())*invcellsize);
		int pd = (int)((e.d() - bound.d())*invcellsize);
		int pu = (int)((e.u() - bound.d())*invcellsize);
		for(int x = pl; x<= pr; x++){
			for(int y = pd; y <= pu; y++){
				int px = x % cellx;
				if  (px < 0){ px = cellx+px; }
				int py = y % celly;
				if  (py < 0){ py = celly+py; }
				int index = py*cellx+px;
				if(array[index] == null){
					array[index] = new ArrayList<Ent>();
				}
				((ArrayList<Ent>)array[index]).add(e);
			}
		}
	}

	@Override
	public Set<Ent> getCloseEnt(BoundIF e) {
		Set<Ent> c = new HashSet<Ent>();
		int pl = (int)((e.l() - bound.l())*invcellsize);
		int pr = (int)((e.r() - bound.l())*invcellsize);
		int pd = (int)((e.d() - bound.d())*invcellsize);
		int pu = (int)((e.u() - bound.d())*invcellsize);
		for(int x = pl; x<= pr; x++){
			for(int y = pd; y <= pu; y++){
				int px = x % cellx;
				if  (px < 0){ px = cellx+px; }
				int py = y % celly;
				if  (py < 0){ py = celly+py; }
				int index = py*cellx+px;
				if(array[index] != null){
					c.addAll(((ArrayList<Ent>)array[index]));
				}
			}
		}
		return c;
	}

	@Override
	public Set<Ent> getCloseEnt(Vec2 p) {
		Set<Ent> c = new HashSet<Ent>();
		int px = (int)((p.x - bound.l())*invcellsize) % cellx;
		if  (px < 0){ px = cellx+px; }
		int py = (int)((p.y - bound.d())*invcellsize) % celly;
		if  (py < 0){ py = celly+py; }
		int index = py*cellx+px;
		if(array[index]!=null){
			c.addAll(((ArrayList<Ent>)array[index]));
		}
		return c;
	}

}
