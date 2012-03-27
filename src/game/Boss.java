package game;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import engine.Draw;
import engine.Ent;
import engine.Main;
import game.Enemy.Type;
import math.BBox;
import math.Color;
import math.Vec2;

public class Boss extends Enemy{
	private int level = 0;
	private int[] leveltypes;
	private int maxlife = 30;
	private Vec2 moveDir = new Vec2(0,1);
	private Vec2 fireDir = new Vec2(0,1);
	
	private final float thinkTime	= 1.0f;
	private final float busyThinkTime = 0.2f;
	private float	lastThinkTime = -10.0f;
	
	private float fireLastTime = 0.0f;
	private float fireInterval = 0.3f;
	private int   fireSequence = 0;
	
	private ArrayList<Missile> fireList = new ArrayList<Missile>();

	private final int LVL_RUNAFTERPLAYER0 = 0;
	private final int LVL_RUNAFTERPLAYER1 = 1;
	private final int LVL_RANDMOVE0 = 2;
	private final int LVL_RANDMOVE1 = 3;
	private final int LVL_STATIONARY0 = 4;
	private final int LVL_STATIONARY1 = 5;
	private boolean paralysed;

	private final Color[] lvlcolor = {
			Color.red,
			Color.orange,
			Color.green,
			Color.bluegreen,
			Color.blue,
			Color.purple
	};

	public Boss(Player p, int level) {
		super(p, Type.Boss);
		this.setBound(new BBox(40,40));
		this.life = maxlife;
		this.level = level;
		this.leveltypes = makeLevels();
	}
	private int[] makeLevels(){
		int[] l = {0,0,1,1,2,2,3,3,4,4,5,5};
		for(int i = 0; i < 100; i++){
			int a = Main.rng.nextInt(l.length);
			int b = Main.rng.nextInt(l.length);
			int tmp = l[a];
			l[a] = l[b];
			l[b] = tmp;
		}
		return l;
	}
	public void damage(){
		life--;
		if(life <= 0){
			((GameMain)Main.game).multiplier+= 10;
			for(Missile m: fireList){
				if(!m.isDestroyed()){
					m.destroy();
				}
			}
			level--;
			life = maxlife;
			if(level == 0){
				destroy();
			}
		}
	}
	private int  getLevelType(){
		if(level > 0){
			return leveltypes[level-1];
		}else{
			return 0;
		}
	}
	private void randomDirection(){
		moveDir = Vec2.randomn();
		moveDir.normalise();
		Vec2 toPlayer = distVec(player);
		toPlayer.normalise();
		toPlayer.scale(0.5f);
		moveDir.add(toPlayer);
		moveDir.normalise();
	}
	private void chasePlayer(){
		moveDir = Vec2.randomn();
		moveDir.normalise();
		Vec2 toPlayer = distVec(player);
		toPlayer.normalise();
		toPlayer.scale(1.5f);
		moveDir.add(toPlayer);
		moveDir.normalise();
	}
	
	public void OnUpdate(){
		boolean firing = false;
		if(paralysed){
			paralysed = false;
			return;
		}
		float pdist =  dist(player); 
		if(pdist > 2000){
			destroy();
		}else if(pdist < 400 && Main.time > spawnTime + 1.0f){
			firing = true;
			if(lastThinkTime + busyThinkTime <= Main.time){
				fireDir = distVec(player).normalisen();
				lastThinkTime = Main.time;
			}
		}else{
			firing = false;
			if(lastThinkTime + thinkTime <= Main.time){
				if(getLevelType() == this.LVL_RANDMOVE0 || getLevelType() == this.LVL_RANDMOVE1){
					randomDirection();
				}else{
					chasePlayer();
				}
				lastThinkTime = Main.time;
			}

		}
		/* Moving the ship ! */
		if(!moveDir.isZero()){
			if(getLevelType() == this.LVL_STATIONARY0 || getLevelType() == this.LVL_STATIONARY1){
				translate(moveDir.setLengthn(0.5f*mspeed*Main.deltaTime));
			}else if (getLevelType() == this.LVL_RUNAFTERPLAYER0 || getLevelType() == this.LVL_RUNAFTERPLAYER1){ 
				translate(moveDir.setLengthn(1.25f*mspeed*Main.deltaTime));
			}else{
				translate(moveDir.setLengthn(mspeed*Main.deltaTime));
			}
		}
		/* Firing the Missiles ! */
		if(!fireDir.isZero() && firing && Main.time > fireLastTime + fireInterval){
			fireLastTime = Main.time;
			switch(getLevelType()){
			case LVL_RANDMOVE0:
				if(fireSequence >= 6){
					fireSequence = 0;
					return;
				}else if(fireSequence < 3){
					fireArc(fireDir,30,0,3,true);			
				}
				fireSequence++;
				break;
			case LVL_RANDMOVE1:
				if(fireSequence >= 18){
					fireSequence = 0;
					return;
				}else if(fireSequence < 12){
					fireArc(fireDir,Math.abs((fireSequence-6)*3),0,2,true);			
				}
				fireSequence++;
				break;
			case LVL_RUNAFTERPLAYER0:
				if(fireSequence >= 6){
					fireSequence = 0;
					return;
				}else if(fireSequence < 3){
					fireArc(fireDir,(fireSequence+1)*10,0,fireSequence+1,true);			
				}
				fireSequence++;
				break;
			case LVL_RUNAFTERPLAYER1:
				if(fireSequence >= 6){
					fireSequence = 0;
					return;
				}else if(fireSequence < 3){
					fireArc(fireDir,30,(fireSequence+1)*5,3,true);			
				}
				fireSequence++;
				break;
			case LVL_STATIONARY0:
				if(fireSequence >= 24){
					fireSequence = 0;
					return;
				}else if(fireSequence < 18){
					fireArc(fireDir,300,0,(fireSequence+1),true);			
				}
				fireSequence++;
				break;
			case LVL_STATIONARY1:
				if(fireSequence >= 24){
					fireSequence = 0;
					return;
				}else if(fireSequence < 18){
					fireArc(fireDir.rotateDegn(fireSequence*30),30,0,3,true);	
					fireArc(fireDir.rotateDegn(90-fireSequence*45),30,0,3,true);			
				}
				fireSequence++;
				break;
			default:
			}
		}

	}
	public void fireArc(Vec2 dir, float angle, float randAngle, int count, boolean addToList){
		Vec2 sdir = dir.rotateDegn(-angle*0.5f);
		float sangle = 0;
		float dangle = 0;
		if(count > 0){
			dangle = angle/(count-1);
			for(int i = 0; i < count; i++){
				Missile m = new Missile(Main.scene,
						this,
						sdir.rotateDegn(sangle + (Main.rng.nextFloat() - 0.5f)*2*randAngle),
						100,
						5,
						lvlcolor[getLevelType()]);
				if(addToList){
					fireList.add(m);
				}
				sangle += dangle;
			}
		}else{
			Missile m = new Missile(Main.scene,
					this,
					dir.rotateDegn((Main.rng.nextFloat() - 0.5f)*2*randAngle),
					100,
					5,
					lvlcolor[getLevelType()]);
			if(addToList){
				fireList.add(m);
			}
		}
	}

	public void OnCollisionEmit(Ent e, Vec2 colVec){
		if(e instanceof Obstacle){
			translate(colVec);
			randomDirection();
			((Obstacle) e).damage(500);
		}else if(e instanceof Shield){
			paralysed = true;
		}
	}
	public void OnDestroyed(){
		for(int i = 0; i < 50; i++){
			Explosion e = new Explosion(getPos(),new Vec2(), Explosion.Type.Enemy);
			Main.scene.addEntity(e);
		}
		((GameMain)Main.game).multiplier+= 50;
		((GameMain)Main.game).score += 100*((GameMain)Main.game).multiplier;
	}
	public void OnDraw(){
		Color.red.glSet();
		GL11.glLineWidth(3);
		Draw.circle(18);
		Draw.line(fireDir.setLengthn(5), fireDir.setLengthn(25));
		GL11.glLineWidth(1);
		if(level > 0){
			lvlcolor[leveltypes[level-1]].glSet();
			Draw.disc(10);
			Draw.arc(16, 60 + Main.time*10, 120);
			Draw.arc(14, - Main.time*20, 120);
			Draw.arc(12,  Main.time*5, 120);
		}
		float rad = 30;
		float radint = 5;
		for(int i = 0; i < level; i++){
			lvlcolor[leveltypes[i]].glSet();
			if(i == level -1){
				Draw.arc(rad, 0, life/maxlife*360.0f);
			}else{
				Draw.circle(rad);
			}
			rad += radint;
		}
	}

}
