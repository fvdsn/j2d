package engine;

import java.util.HashSet;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import math.BBox;
import math.Bound;
import math.BoundIF;
import math.Vec2;

public class QTreeProximityMap implements ProximityMapIF{
	
	private class Node extends BBox{
		public Node tl = null ,tr = null,bl = null,br = null;
		public Set<Ent> contents = null;
		public int depth = 0;
		public float minx, miny,maxx,maxy,cx,cy;
		public Node(float minx, float miny, float maxx, float maxy){			
			super(  (minx+maxx)*0.5f,
					(miny+maxy)*0.5f,
					maxx - minx,
					maxy - miny);
			this.minx = minx;
			this.miny = miny;
			this.maxx = maxx;
			this.maxy = maxy;
			this.cx   = (maxx+minx)*0.5f;
			this.cy   = (maxy+miny)*0.5f;
		}
		public boolean isLeaf(){
			return tl == null && tr == null && bl == null && br == null;
		}
		public Node makeTL(){
			return new Node(minx,cy,cx,maxy);
		}
		public Node makeTR(){
			return new Node(cx,cy,maxx,maxy);
		}
		public Node makeBL(){
			return new Node(minx,miny,cx,cy);
		}
		public Node makeBR(){
			return new Node(cx,miny,maxx,cy);
		}
		public void getEntities(BoundIF bound, Set<Ent> ents){
			if(!bound.collides(this)){
				return;
			}else if(bound.contains(this)){
				if(contents != null){
					ents.addAll(contents);
				}
			}else if(isLeaf()){
				if(contents != null){
					ents.addAll(contents);
				}
			}else{
				if(tl != null){ tl.getEntities(bound,ents); }
				if(tr != null){ tr.getEntities(bound,ents); }
				if(bl != null){ bl.getEntities(bound,ents); }
				if(br != null){ br.getEntities(bound,ents); }
			}
		}
		public void addEntity(int maxdepth, int maxSize, Ent ent){
			if(contents == null){
				contents = new HashSet<Ent>();
				contents.add(ent);
				return;
			}else if(contents.size() < maxSize){
				contents.add(ent);
				return;
			}else if(depth >= maxdepth){
				contents.add(ent);
				return;
			}else{
				contents.add(ent);
				Node ntl = makeTL();
				Node ntr = makeTR();
				Node nbl = makeBL();
				Node nbr = makeBR();
				for(Ent e : contents){
					if(ntl.collides(e)){
						if(tl == null){
							tl = ntl;
							tl.depth = depth +1;
						}
						tl.addEntity(maxdepth, maxSize, e);
					}
					if(ntr.collides(e)){
						if(tr == null){
							tr = ntr;
							tr.depth = depth + 1;
						}
						tr.addEntity(maxdepth, maxSize, e);
					}
					if(nbl.collides(e)){
						if(bl == null){
							bl = nbl;
							bl.depth = depth + 1;
						}
						bl.addEntity(maxdepth, maxSize, e);
					}
					if(nbr.collides(e)){
						if(br == null){
							br = nbr;
							br.depth = depth + 1;
						}
						br.addEntity(maxdepth, maxSize, e);
					}
				}
				//contents.add(ent);
			}
		}
		public void draw(int maxDepth){
			int size = maxDepth - depth + 1;
			if(size > 0){
				GL11.glLineWidth(size);
				Draw.box(this);
				if(tl != null){ tl.draw(maxDepth); }
				if(tr != null){ tr.draw(maxDepth); }
				if(bl != null){ bl.draw(maxDepth); }
				if(br != null){ br.draw(maxDepth); }
				GL11.glLineWidth(1);
			}
		}
	}
	private Vec2 min;
	private Vec2 max;
	private Node root;
	private int  maxDepth = 10;
	private int  maxNodeSize = 5;
	
	public QTreeProximityMap(BBox bound, int maxDepth, int maxNodeSize){
		min = new Vec2(bound.l(),bound.d());
		max = new Vec2(bound.r(),bound.u());
		this.maxDepth = maxDepth;
		this.maxNodeSize = maxNodeSize;
		this.root = new Node(min.x,min.y,max.x,max.y);
	}

	@Override
	public void addEnt(Ent e) {
		if(e.collides(root)){
			root.addEntity(maxDepth, maxNodeSize, e);
		}
	}

	@Override
	public Set<Ent> getCloseEnt(BoundIF e) {
		HashSet<Ent> set = new HashSet<Ent>();
		root.getEntities(e,set);
		return set;
	}

	@Override
	public Set<Ent> getCloseEnt(Vec2 p) {
		HashSet<Ent> set = new HashSet<Ent>();
		root.getEntities(new Bound(p),set);
		return set;
	}

	@Override
	public void reset() {
		this.root = new Node(min.x,min.y,max.x,max.y);
	}
	public void draw(){
		this.root.draw(maxDepth);
	}

}
