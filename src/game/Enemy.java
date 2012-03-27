package game;

import java.util.ArrayList;

import engine.Dbg;
import engine.Draw;
import engine.Ent;
import engine.Main;
import engine.PhysEnt;
import engine.Raytrace;
import math.BBox;
import math.Color;
import math.Vec2;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Enemy extends Ent{
	
	public enum Type{
		Basic,				//RED
		Lasers,			//Orange
		Teleporters,		//Purple
		MissileLauncher,	//Green
		LongRange,			//Pink
		Homing,				//Blue
		Boss,				//Change Color
	}
	public final Type type;
	
	protected float life = 1.0f;
	private boolean firing = false;
	private Vec2 moveDir = new Vec2(0,1);
	private Vec2 fireDir = new Vec2(0,1);		/* The missile fire direction */
	private float fireLastTime = -1;			/* Last time the ship fired a missile */
	private final float fireInterval = 0.33f;	/* The delay (sec) between fires */
	private final float fireSpeed	= 100.0f;	/* The speed of the missile in pixel/sec */
	private final float fireDuration = 3.0f;		/* The time the missiles are alive */
	private final float thinkTime	= 1.0f;
	private final float busyThinkTime = 0.2f;
	private float	lastThinkTime = -10.0f;
	public final float mspeed = 50.0f;			/* The speed of the ship in pixel per seconds */
	protected Player player;
	protected float spawnTime = 0;
	private ArrayList<Missile> fireList = new ArrayList<Missile>();
	private boolean paralysed = false;
	private float laserAngle  = Main.rng.nextFloat()*360;
	private float laserRandRotation = (Main.rng.nextInt(2)*2) -1;
	private Vec2  laserDir    = new Vec2(1,0);
	private float laserAngleSpeed = 120;
	private float laserLength	  = 10;
	
	public Enemy(Player p, Type type){
		super();
		this.player = p;
		this.collisionBehaviour = PhysEnt.CollisionBehaviour.Both;
		this.spawnTime = Main.time;
		this.type = type;
		if(type == Type.Basic){
			this.setBound(new BBox(16,16));
			this.life = 1.0f;
		}else if(type == Type.MissileLauncher){
			this.setBound(new BBox(20,20));
			this.life  = 5.0f;
		}else if(type == Type.Lasers){
			this.setBound(new BBox(20,20));
			this.life = 3;
		}

	}
	private void randomDirection(){
		moveDir = Vec2.randomn();
		moveDir.normalise();
		Vec2 toPlayer = distVec(player);	//.getPos().subn(getPos());
		toPlayer.normalise();
		toPlayer.scale(0.5f);
		moveDir.add(toPlayer);
		moveDir.normalise();
	}
	public void damage(){
		damage(1);
	}
	public void damage(float d){
		life -= d;
		if(life <= 0.0f){
			destroy();
		}else{
			for(int i = 0; i < 3; i++){
				Explosion e = new Explosion(getPos(),new Vec2(), Explosion.Type.Enemy);
				Main.scene.addEntity(e);
			}
		}
	}
	public void OnUpdate(){
		if(paralysed){
			paralysed = false;
			return;
		}
		float pdist = getPos().dist(player.getPos());
		if(pdist > 2000){
			destroy();
		}else if(pdist < 200 && Main.time > spawnTime + 3.0f){
			firing = true;
			if(lastThinkTime + busyThinkTime <= Main.time){
				fireDir = player.getPos().subn(getPos()).normalisen();
				lastThinkTime = Main.time;
			}
		}else{
			firing = false;
			if(lastThinkTime + thinkTime <= Main.time){
				randomDirection();
				lastThinkTime = Main.time;
			}
			
		}
		
		if(type == Type.Lasers){
			Raytrace rt = Main.scene.raytrace(getPos(), laserDir, 1000);
			Vec2 hit = getPos().addn(laserDir.setLengthn(1000));
			Ent  hitent = rt.next(hit, null);
			while(hitent != null && hitent instanceof Enemy){
					hitent = rt.next(hit,null);
			}
			boolean touched = false;
			
			float length = getPos().dist(hit);
			if(laserLength < length){
				if(firing){
				laserLength += 300 *Main.deltaTime;
				}else{
					laserLength += 100 *Main.deltaTime;

				}
				if(laserLength > length){
					laserLength = length;
					touched = true;
				}
			}else{
				laserLength = length;
				touched = true;
			}
			if(touched){
				if(hitent instanceof Obstacle){
					if(firing){
						((Obstacle) hitent).damage(200*Main.deltaTime);
					}else{
						((Obstacle) hitent).damage(20*Main.deltaTime);
					}
				}else if(hitent instanceof Player){
					((Player) hitent).damage();
				}
				Explosion e = new Explosion(hit,new Vec2(), Explosion.Type.PlayerFire);
				Main.scene.addEntity(e);			
			}
			if(firing){
			
				laserDir.setPolarDeg(1, laserAngle);
				float aimAngle = fireDir.angleSignedDeg(laserDir);
				float angleAdjust = laserAngleSpeed *Main.deltaTime *0.15f*(float)Math.sqrt(Math.abs(aimAngle));
				if(aimAngle > 0){
					laserAngle -= Math.min(angleAdjust,aimAngle);
				}else{
					laserAngle += Math.min(angleAdjust,-aimAngle);
				}
				return;
			}else{
				if(!moveDir.isZero()){
					translate(moveDir.setLengthn(mspeed*Main.deltaTime));
				}
				laserAngle += laserAngleSpeed *0.3f * laserRandRotation * Main.deltaTime;
				laserDir.setPolarDeg(1, laserAngle);
				return;
			}
		}
		/* Moving the ship ! */
		if(!moveDir.isZero()){
			translate(moveDir.setLengthn(mspeed*Main.deltaTime));
		}
		/* Firing the Missiles ! */
		if(!fireDir.isZero() && firing && Main.time > fireLastTime + fireInterval){
			fireLastTime = Main.time;
			float spdFact = 1.0f; // + Main.time * 0.01f;
			Missile m = new Missile(this, fireDir, fireSpeed * spdFact, fireDuration);
			m.setPos(this.getPos());	// we set the missile's position to the ship's current world position
			Main.scene.addEntity(m);	// we add the missile to the current's scene so that it is rendered
			fireList.add(m);
		}
		
	}
	public void OnCollisionEmit(Ent e, Vec2 colVec){
		if(e instanceof Obstacle){
			translate(colVec);
			randomDirection();
		}else if(e instanceof Shield){
			paralysed = true;
		}
	}

	public void OnDraw(){
		if(type == Type.Basic){
			Color.red.glSet();
			Draw.circle(new Vec2(), 8);
			Draw.line(new Vec2(), fireDir.setLengthn(10));
		}else if(type == Type.MissileLauncher){
			Color.green.glSet();
			Draw.arc(10, -90, life/5.0f*360.0f);
			Draw.circle(8);
			Draw.line(new Vec2(), fireDir.setLengthn(12));
		}else if(type == Type.Lasers){
			Color.yellow.glSet();
			Draw.circle(new Vec2(), 8);
			Draw.arc(10, laserAngle - 15, 30);
			Draw.arc(12, laserAngle - 15, 30);
			Draw.arc(6, laserAngle - 15, 30);
			if(firing){
				GL11.glLineWidth(Main.rng.nextFloat()*10);
			}
			Draw.line(new Vec2(), laserDir.setLengthn(laserLength));
			GL11.glLineWidth(1);

		}else{
			Dbg.drawRef(new Vec2());
		}
	}
	public void OnDestroyed(){
		for(int i = 0; i < 15; i++){
			Explosion e = new Explosion(getPos(),new Vec2(), Explosion.Type.Enemy);
			Main.scene.addEntity(e);
		}
		if(type == Type.Basic){
			int firedestroyed = 0;
			for(Missile m: fireList){
				if(!m.isDestroyed()){
					m.destroy();
					firedestroyed++;
					for(int i = 0; i < 3; i++){
						Explosion e = new Explosion(m.getPos(),m.getSpeed(), Explosion.Type.Enemy);
						Main.scene.addEntity(e);
					}
				}
			}
			((GameMain)Main.game).multiplier+= 1+firedestroyed;
			((GameMain)Main.game).score += 10*((GameMain)Main.game).multiplier * firedestroyed;
		}else if(type == Type.MissileLauncher){
			((GameMain)Main.game).multiplier+= 5;
			((GameMain)Main.game).score += 50*((GameMain)Main.game).multiplier;
		}
	}
}
