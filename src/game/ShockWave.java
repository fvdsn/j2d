package game;

import org.lwjgl.opengl.GL11;

import math.*;
import engine.*;

public class ShockWave extends Ent{
	private float duration = 0.6f;
	private float fireDestroyRadius = 0.8f;
	private float fireDisperseSpeed = 50.0f;
	private float enemyDamageRadius = 0.6f;
	private float enemyDamageRate   = 300.0f;
	private float enemyDisperseSpeed = 600.0f;
	private float radius = 30;
	private float strength;
	public ShockWave(Vec2 pos,float duration){
		this.duration = duration;
		this.setBound(new BBox(radius*2,radius*2));
		this.setPos(pos);
		this.setLocalZ(10);
		this.collisionBehaviour = Ent.CollisionBehaviour.Receiver;
		Main.scene.addEntity(this);
		this.destroy(duration);
	}
	public void OnUpdate(){
		this.radius += 700*Main.deltaTime;
		this.setBound(new BBox(radius *2, radius *2));
	}
	public void OnCollisionReceive(Ent e, Vec2 colVec){
		
		Vec2 vdist = e.getPos().subn(getPos());
		float dist = vdist.length();
		
		if(e instanceof Enemy){
			if(dist < enemyDamageRadius*radius){
				((Enemy)e).damage(Main.deltaTime*enemyDamageRate*(1-dist/(enemyDamageRadius*radius)));
			}
			e.translate(vdist.setLengthn(Main.deltaTime*enemyDisperseSpeed*(1-dist/radius)));
		}else if(e instanceof Missile && ((Missile)e).launcher instanceof Enemy){
			if(dist < fireDestroyRadius*radius){
				e.destroy();
			}else{
				e.translate(vdist.setLengthn(Main.deltaTime*fireDisperseSpeed*(1-dist/radius)));
			}
		}
	}
	public void OnDraw(){
		Color.blue.glSet();
		Draw.circle(radius);
		Draw.circle(radius*0.95f);
		Draw.circle(radius*0.8f);
		Draw.circle(radius*0.6f);
		new Color(0,0,0.1f).glSet();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glEnable(GL11.GL_BLEND);
		Draw.disc(radius*0.65f);
		GL11.glDisable(GL11.GL_BLEND);

	}
}
