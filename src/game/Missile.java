package game;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import math.*;
import engine.*;
import game.Enemy.Type;

public class Missile extends PhysEnt {
	public Ent launcher;
	private float damage = 1.0f;
	private Bonus.Type bonus = Bonus.Type.None;
	
	private boolean additive = false;
	private Color color = Color.red;
	private Color altColor = Color.red;
	public enum DrawShape{
		Circle,
		Disc,
		Arrow,
		Line,
		Rectangle,
	}
	private DrawShape drawShape;
	private float     drawShapeSize;
	public Missile(Ent launcher, Vec2 dir, float speed, float duration){
		super();
		this.speed = dir.setLengthn(speed);
		this.destroy(duration);
		this.launcher = launcher;
		this.setBound(new BBox(10,10));
		this.collisionBehaviour = PhysEnt.CollisionBehaviour.Emitter;
	}
	public Missile(Scene s, Ent launcher, Vec2 dir, float speed, float duration, Color color){
		super();
		this.speed = dir.setLengthn(speed);
		this.destroy(duration);
		this.launcher = launcher;
		this.setBound(new BBox(10,10));
		this.collisionBehaviour = PhysEnt.CollisionBehaviour.Emitter;
		s.addEntity(this);
		this.setPos(launcher.getPos());
		this.color = color;
	}
	public void OnDraw(){
		if(launcher instanceof Player){
			if(bonus == Bonus.Type.ExtraFire){
				Color.yellow.glSet();
				Draw.disc(3);
			}else if(bonus == Bonus.Type.Phantom){
				new Color(0.5f,0.01f,0.95f).glSet();
				Draw.circle(5);
			}else if(bonus == Bonus.Type.AutoAim){
				new Color(1,0,1).glSet();
				Draw.line(speed.setLengthn(-25), speed.setLengthn(25));
			}else if(bonus == Bonus.Type.Bounce){
				new Color(0.1f,0.1f,1.0f).glSet();
				Draw.disc(4);
				new Color(1f,0,0.1f).glSet();
				Draw.circle(4);
			}else if(bonus == Bonus.Type.HiPower){
				new Color(1,0.5f,0).glSet();
				Draw.disc(7);
				Draw.circle(15);
			}else{
				new Color(0,0,1.0f).glSet();
				Draw.circle(4);
				Color.white.glSet();
				Draw.circle(3);
			}
		}else{
			Enemy e = (Enemy)launcher;
			if(e.type == Type.MissileLauncher){
				Color.green.glSet();
				Draw.disc(new Vec2(), 4);
				Draw.circle(speed.scalen(-0.05f), 4);
				Draw.circle(speed.scalen(-0.10f), 4);
				Draw.circle(speed.scalen(-0.15f), 4);
			}else{
				color.glSet();
				if(this.timeBeforeDestruction() > 0.5f){
					Draw.disc(4);
				}else{
					Draw.circle(4);
				}
			}
		}
	}
	public void setBonus(Bonus.Type type){
		bonus = type;
		if(bonus == Bonus.Type.HiPower){
			this.setBound(new BBox(30,30));
		}
	}
	public Bonus.Type getBonus(){
		return bonus;
	}
	public void OnCollisionEmit(Ent e, Vec2 colVec){
		if(launcher instanceof Player){
			if(e instanceof Enemy){
				if(bonus == Bonus.Type.HiPower){
					((Enemy)e).damage();
				}else{
					((Enemy)e).damage();
					destroy();
				}
			}else if(e instanceof Obstacle){
				if(((Obstacle) e).destroyed){
					return;
				}
				if(bonus == Bonus.Type.Phantom){
					return;
				}else if(bonus == Bonus.Type.ExtraFire){
					((Obstacle)e).damage(5);
					destroy();
				}else if(bonus == Bonus.Type.HiPower){
					((Obstacle)e).damage(20);
					destroy();
				}else if(bonus == Bonus.Type.Bounce){
					translate(colVec.scalen(1.1f));
					speed.reflect(colVec);
					((Obstacle)e).damage(2);
				}else{
					((Obstacle)e).damage(3);
					destroy();
				}
			}
		}else{
			if(e instanceof Player){
				((Player) e).damage();
				destroy();
			}else if(e instanceof Shield){
				destroy();
			}else if(e instanceof Obstacle){
				((Obstacle) e).damage(10);
				destroy();
			}
		}
	}
	public void OnDestroyed(){
		if(launcher instanceof Player){
			for(int i = 0; i < 3; i++){
				Explosion e = new Explosion(getPos(),new Vec2(), Explosion.Type.PlayerFire);
				Main.scene.addEntity(e);
			}
		}
	}
}
