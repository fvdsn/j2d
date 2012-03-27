package game;

import org.lwjgl.opengl.GL11;

import engine.*;
import math.*;

public class Explosion extends PhysEnt{
	Vec2 p1;
	Vec2 p2;
	public enum Type{
		Enemy,
		Player,
		PlayerFire,
		Building,
	}
	Type type = Type.Enemy;
	public Explosion(Vec2 pos, Vec2 speed, Type type){
		super();
		this.setPos(pos);
		this.type = type;
		if(type == Type.Enemy){ 
			p1 = Vec2.randomn().scalen(5);
			p2 = Vec2.randomn().scalen(5);
			speed = Vec2.randomn().scalen(200);
			destroy(1.0f + Main.rng.nextFloat());
		}else if(type == Type.PlayerFire){
			p1 = Vec2.randomn().scalen(2);
			p2 = Vec2.randomn().scalen(2);
			speed = Vec2.randomn().scalen(100);
			destroy(0.1f + Main.rng.nextFloat()*0.5f);
		}else if(type == Type.Player){
			p1 = Vec2.randomn().scalen(7);
			p2 = Vec2.randomn().scalen(7);
			speed = Vec2.randomn().scalen(400);
			destroy(1.0f + Main.rng.nextFloat());
		}else if(type == Type.Building){
			p1 = Vec2.randomn().scalen(7);
			p2 = Vec2.randomn().scalen(7);
			speed = Vec2.randomn().scalen(100);
			destroy(1.0f + Main.rng.nextFloat());
		}
		this.speed.add(speed);
	}
	public void OnDraw(){
		if(type == Type.Enemy){
			GL11.glLineWidth(1);
			Color.red.glSet();
			Draw.line(p1, p2);
		}else if(type == Type.PlayerFire || type == Type.Player){
			Color.white.glSet();
			Draw.line(p1, p2);
		}else if(type == Type.Building){
			Color.black.glSet();
			Draw.line(p1, p2);

		}
		Draw.line(p1, p2);
	}
	public void OnUpdate(){
		if(speed.bigger(50))
		speed.scale(0.95f);
	}

}
