package game;

import java.util.ArrayList;
import java.util.List;

import math.*;

import engine.Dbg;
import engine.Draw;
import engine.Effect;
import engine.Ent;
import engine.Main;
import engine.PhysEnt;
import engine.Raytrace;
import engine.SavedData;
import game.Bonus.Type;

import org.lwjgl.input.*;
import org.lwjgl.opengl.GL11;
public class Player extends Ent {
	private Vec2 fireDir = new Vec2(0,1);		/* The missile fire direction */
	private float fireLastTime = -1;			/* Last time the ship fired a missile */
	public final float fireInterval = 0.05f;	/* The delay (sec) between fires */
	public final float fireSpeed	= 500.0f;	/* The speed of the missile in pixel/sec */
	public final float fireDuration = 3.0f;		/* The time the missiles are alive */
	public final float mspeed = 170.0f;			/* The speed of the ship in pixel per seconds */
	public final float shockWaveInterval = 0.5f;
	private float	shockWaveLastTime = -1.0f;
	
	private Bonus.Type  bonus = Bonus.Type.None;
	private Color bonusColor = Color.black;
	private float bonusExpirationTime = 0.0f;
	private float invulnTime		  = 0.0f;
	private boolean hasShield = true;
	public Player(){
		super();
		this.setBound(new BBox(17,17));
		this.collisionBehaviour = PhysEnt.CollisionBehaviour.Both;
	}
	public void setBonus(Bonus bonus, float duration){
		invulnTime = Main.time + 0.25f;
		if(bonus.type == Bonus.Type.Shield){
			if(hasShield){
				new Shield(getPos(),7);
			}else{
				new Shield(getPos());
			}
			hasShield = true;
		}else{
			this.bonus = bonus.type;
			this.bonusColor = bonus.color;
			bonusExpirationTime = Math.max( Main.time + duration,
											bonusExpirationTime + duration);
		}
	}
	public void damage(){
		if(Main.time < invulnTime){
			return;
		}else if(bonus != Bonus.Type.None && Main.time < bonusExpirationTime){
			bonus = Bonus.Type.None;
			bonusExpirationTime = Main.time;
			invulnTime = Main.time + 0.5f;
			new Shield(getPos());
		}else if(hasShield){
			hasShield = false;
			new Shield(getPos());
			invulnTime = Main.time + 2.0f;
			for(int i = 0; i < 90; i++){
				Explosion e = new Explosion(getPos(),new Vec2(), Explosion.Type.Player);
				Main.scene.addEntity(e);
			}
		}else{
			destroy();
		}
	}
	public void OnUpdate(){
		/* Moving the ship ! */
		Vec2 dir = new Vec2();	// The direction of the ship
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			dir.add(-1,0);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			dir.add(1,0);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			dir.add(0,-1);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			dir.add(0,1);
		}
		if(!dir.isZero()){
			this.translate(dir.setLengthn(mspeed*Main.deltaTime));
		}
		if(Mouse.isButtonDown(1) && Main.time > shockWaveLastTime + shockWaveInterval){
			float duration = Math.min((float)(Math.sqrt(Main.time - shockWaveLastTime))*0.1f,1.5f);
			Dbg.Log(duration);
			new ShockWave(getPos(),duration);
			shockWaveLastTime = Main.time;
		}

		/* Firing the Missiles ! */

		fireDir = Main.camera.getMousePos().subn(getPos()).normalisen();
		if( (Keyboard.isKeyDown(Keyboard.KEY_SPACE) || Mouse.isButtonDown(0)) && Main.time > fireLastTime + fireInterval && !fireDir.isZero()){
			fireLastTime = Main.time;
			if(Main.time < bonusExpirationTime){
				switch(bonus){
				case ExtraFire:
					float spread = 0.1f;
					for(int i = 0; i < 5; i++){
						Missile m = new Missile(
								this,
								(fireDir.addn(Vec2.randomn().scalen(spread))).normalisen(), 
								fireSpeed, 
								fireDuration*0.2f);
						m.setBonus(bonus);
						m.setPos(this.getPos());	// we set the missile's position to the ship's current world position
						Main.scene.addEntity(m);	// we add the missile to the current's scene so that it is rendered
						spread += 0.2f;
					}
					break;
				case Phantom:
					Missile m = new Missile(
							this,
							(fireDir.addn(Vec2.randomn().scalen(0.15f))).normalisen(), 
							fireSpeed, 
							fireDuration);
					m.setBonus(bonus);
					m.setPos(this.getPos());	// we set the missile's position to the ship's current world position
					Main.scene.addEntity(m);	// we add the missile to the current's scene so that it is rendered
					break;
				case HiPower:
					m = new Missile(
							this,
							(fireDir.addn(Vec2.randomn().scalen(0.1f))).normalisen(), 
							fireSpeed, 
							fireDuration);
					m.setBonus(bonus);
					m.setPos(this.getPos());	// we set the missile's position to the ship's current world position
					Main.scene.addEntity(m);	// we add the missile to the current's scene so that it is rendered
					break;
				case Bounce:
					m = new Missile(
							this,
							(fireDir.addn(Vec2.randomn().scalen(0.05f))).normalisen(), 
							fireSpeed, 
							fireDuration*0.6f);
					m.setBonus(bonus);
					m.setPos(this.getPos());	// we set the missile's position to the ship's current world position
					Main.scene.addEntity(m);	// we add the missile to the current's scene so that it is rendered
					break;
				case AutoAim:
					m = new Missile(
							this,
							(fireDir.addn(Vec2.randomn().scalen(0.01f))).normalisen(), 
							fireSpeed, 
							fireDuration);
					m.setPos(this.getPos());	// we set the missile's position to the ship's current world position
					Main.scene.addEntity(m);	// we add the missile to the current's scene so that it is rendered
					m.setBonus(Type.AutoAim);
					List<Ent> el = new ArrayList<Ent>();
					for(Ent e : Main.scene.getAllEnt()){
						if(e instanceof Enemy && e.getPos().dist(this.getPos()) < 400){
							el.add(e);
						}
					}
					if(el.size() > 0){
						
						Ent re = el.get(Main.rng.nextInt(el.size()));
						for(int i = 0; i < 10; i++){
							Raytrace rt = Main.scene.raytrace(getPos(), re.getPos().subn(getPos()), 500);
							if(rt.next(null, null)!= re){
								re = el.get(Main.rng.nextInt(el.size()));
							}else{
								break;
							}
						}
						Vec2 rdir = re.getPos().subn(this.getPos()).normalisen();
						m = new Missile(
								this,
								rdir, 
								fireSpeed, 
								fireDuration);
						m.setPos(this.getPos());	// we set the missile's position to the ship's current world position
						Main.scene.addEntity(m);	// we add the missile to the current's scene so that it is rendered
						m.setBonus(Type.AutoAim);
					}
					break;
				}
			}else{
				Missile m = new Missile(
									this,
									(fireDir.addn(Vec2.randomn().scalen(0.1f))).normalisen(), 
									fireSpeed, 
									fireDuration);
				m.setPos(this.getPos());	// we set the missile's position to the ship's current world position
				Main.scene.addEntity(m);	// we add the missile to the current's scene so that it is rendered
			}
		}
		
		/* The camera tracks the player */
		//we move the camera towards the ship by 1% of their distance
		Main.camera.setPos(Main.camera.getPos().lerpn(getPos(), 0.02f));	
		
	}
	public void OnDraw(){
		if(Main.time < invulnTime){
			if((Main.timeMillis/200)%2 == 0){
				return;
			}
		}
		Color.white.glSet();
		Draw.circle(new Vec2(), 10);
		Draw.line(new Vec2(), fireDir.setLengthn(15));
		if(hasShield){
			Color.blue.glSet();
			Draw.circle(new Vec2(), 15);
		}
		for(Ent e: Main.scene.getAllEnt()){
			if(e instanceof Bonus){
				Vec2 dir = e.getPos().subn(getPos());
				Color.white.glSet();
				Draw.line(dir.setLengthn(50), dir.setLengthn(60));
				Draw.lcd(dir.setLengthn(50), 0, 6, Integer.toString((int)(dir.length()/10)));
			}/*else if(e instanceof Enemy){
				Vec2 dir = e.getPos().subn(getPos());
				Color.darkred.glSet();
				Draw.point(dir.setLengthn(10+(float)Math.sqrt(dir.length())));
			}*/
		}
		if(bonus != Bonus.Type.None && Main.time < bonusExpirationTime){
			bonusColor.glSet();
			float standardTime = 7.0f;
			float time = bonusExpirationTime - Main.time;
			float radius = 20;
			float radiusint = 3;
			while(time > standardTime){
				Draw.circle(radius);
				radius += radiusint;
				time -= standardTime;
			}
			float angle = (time/standardTime)*360.0f;
			Draw.arc(radius, 90.0f, angle);
		}
	}
	public void OnCollisionEmit(Ent e, Vec2 colVec){
		if(e instanceof Obstacle){
			if(!((Obstacle) e).destroyed){
				translate(colVec);
			}
		}
	}
	public void OnDestroyed(){
		int bestscore = SavedData.readInt("MaxScore", -1);
		final int score = ((GameMain)Main.game).score;
		if  (score > bestscore){
			SavedData.write("MaxScore", score);
			Main.scene.addEffect(0.5f, 2, new Effect(){public void effect(){ 
				GL11.glLineWidth(5);
				Color.red.glSet();
				Draw.lcd(Main.camera.getPos().addn(-100,-10), 5, 32, "New Record!\n"+Integer.toString(score));
				GL11.glLineWidth(1);
			}});
			Main.scene.addEffect(2.5f, new Effect(){public void effect(){Main.restart();}});
		}else{
			Main.scene.addEffect(0.5f, new Effect(){public void effect(){Main.restart();}});
		}
		//Main.restart(0.5f);
		for(int i = 0; i < 90; i++){
			Explosion e = new Explosion(getPos(),new Vec2(),Explosion.Type.Player);
			Main.scene.addEntity(e);
		}
	}
}
