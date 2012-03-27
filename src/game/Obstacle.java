package game;

import engine.*;
import math.*;

public class Obstacle extends Ent{
	private float lastCollisionTime = -10;
	private float maxLife = 100;
	private float life = maxLife;
	public boolean destroyed = false;
	public boolean destructible = true;
	Color c = Color.blue;
	
	public Obstacle(float px, float py, float sx, float sy){
		super();
		this.setPos(px, py);
		this.setBound(new BBox(sx,sy));
		this.collisionBehaviour = PhysEnt.CollisionBehaviour.Receiver;
		maxLife = Math.max(sx,sy)*10;
		life = maxLife;		
	}
	public void damage(float damage){
		if(destructible){
			life -= damage;
			if(life <= 0.0f && !destroyed){
				destroyed = true;
				destroy(0.15f);
			}
		}
	}
	public void OnDraw(){
		if(destroyed){
			Color.white.glSet();
			Draw.boxFilled(getBound().dupAt(new Vec2()),-0.1f);
			return;
		}
		float lf = life / maxLife;
		new Color(0.0f,0.0f,0.0f).glSet();
		Draw.boxFilled(getBound().dupAt(new Vec2(5,-5)),-0.2f);
		new Color(0.2f*lf,0.2f*lf,0.2f*lf).glSet();
		Draw.boxFilled(getBound().dupAt(new Vec2()),-0.1f);
		if(lastCollisionTime >= Main.time - 0.25f){
			if(life < 10){
				new Color(1,0,0).glSet();
			}else{
				new Color(0.5f,0.5f,0.5f).glSet();
			}
			Draw.box(getBound().dupAtZero());
		}else{
			new Color(0.35f,0.35f,0.35f).glSet();
			Draw.box(getBound().dupAtZero());
		}
	}
	@Override
	public void OnCollisionReceive(Ent e, Vec2 colVec){
		//e.translate(colVec.negaten());
		lastCollisionTime = Main.time;
	}
	public void OnDestroyed(){
		for(int i = 0; i < 100; i++){
			Explosion e = new Explosion(getBound().getRandomPos(),new Vec2(), Explosion.Type.Building);
			Main.scene.addEntity(e);
		}
	}
}
