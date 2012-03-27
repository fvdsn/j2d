package game;

import java.util.ArrayList;


import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import engine.*;
import math.*;

/**
 * ex = esx + t*X;
 * ey = esy + t*Y;
 * mx = msx + t*mvx;
 * my = msy + t*mvy;
 * sqrt(x²+y²) = S;
 * 
 * esx + t*X = msx + t*mvx;
 * 
 * esx - msx = t*(mvx -X)
 * (esx - msx)/(mvx - X) = t
 * 
 * (esx - msx) / (mvx - X) = (esy - msy) / (mvy - Y)
 * 
 * S² = x² + y²
 * y = sqrt(S² - X²)
 * 
 * @author fred
 *
 */

public class GameMain extends Game{
	private BBox world = new BBox(2000,2000);
	private float enemySpawnInterval = 0.2f;
	private float enemyLastSpawn = 0;
	private Player player;
	public  int  score = 0;
	public  int  multiplier = 1;
	private  float  lastScoreUpdateTime = 0.0f;
	private  float  bonusSpawnInterval  = 10f;
	private  float  bonusLastSpawn	   = 0.0f;
	private ArrayList<Obstacle> obstacleList = new ArrayList<Obstacle>();
	private static Bonus.Type[] bonusList = {
			Bonus.Type.AutoAim, Bonus.Type.AutoAim,
			Bonus.Type.Bounce, Bonus.Type.Bounce,
			Bonus.Type.ExtraFire, Bonus.Type.ExtraFire,
			Bonus.Type.Phantom, Bonus.Type.Phantom,
			//Bonus.Type.HiPower, Bonus.Type.HiPower,
			Bonus.Type.Shield, Bonus.Type.Shield
	};
	private static int nextBonusIndex = bonusList.length;
	
	private static Bonus.Type nextBonus(){
		if(nextBonusIndex >= bonusList.length){
			for(int i = 0; i < 100; i++){
				int a = Main.rng.nextInt(bonusList.length);
				int b = Main.rng.nextInt(bonusList.length);
				Bonus.Type tmp = bonusList[a];
				bonusList[a] = bonusList[b];
				bonusList[b] = tmp;
			}
			nextBonusIndex = 0;
		}
		return bonusList[nextBonusIndex++];
	}
	
	public void spawnBonus(){
		Bonus b = new Bonus(world.getRandomPos(),nextBonus());	
		boolean ok = false;
		while(!ok){
			ok = true;
			for(Obstacle o : obstacleList){
				if(b.collides(o)){
					ok = false;
					b.setPos(world.getRandomPos());
					continue;
				}
			}
		}
		Main.scene.addEntity(b);
	}
	public static void spawnBonus(Vec2 pos){
		Bonus b = new Bonus(pos,nextBonus());
		Main.scene.addEntity(b);
	}
	public void buildObstacles(int count){
		obstacleList = new ArrayList<Obstacle>();
		for(int i = 0; i < count; i++){
			boolean ok = false;
			while(!ok){
				Vec2 p = world.getRandomPos();
				float sx = Main.rng.nextFloat() * 100 + 10.0f;
				float sy = Main.rng.nextFloat() * 100 + 10.0f;
				BBox b = new BBox(p,sx,sy);
				ok = true;
				for(Obstacle o: obstacleList ){
					if(o.collides(b)){
						ok = false;
						break;
					}
				}
				if(ok){
					Obstacle o = new Obstacle(p.x,p.y,sx,sy);
					Main.scene.addEntity(o);
					obstacleList.add(o);
				}
			}
		}
		buildObstacleWall(new Vec2(world.l(),world.u()), new Vec2(world.r(),world.u()),50);
		buildObstacleWall(new Vec2(world.l(),world.u()), new Vec2(world.l(),world.d()),50);
		buildObstacleWall(new Vec2(world.l(),world.d()), new Vec2(world.r(),world.d()),50);
		buildObstacleWall(new Vec2(world.r(),world.u()), new Vec2(world.r(),world.d()),50);
	}
	public void buildObstacleWall(Vec2 start, Vec2 end, int count){
		Vec2 distvec = start.distVecn(end);
		float minx = Math.max(distvec.x / count,10);
		float miny = Math.max(distvec.y / count,10);
		Vec2 interval = distvec.setLengthn(distvec.length()/count);
		Vec2 pos = start;
		BBox randpos = new BBox(10,10);
		for(int i = 0; i < count; i++){
			Vec2 rpos = randpos.getRandomPos();
			float sx = Main.rng.nextFloat() * 100 + minx;
			float sy = Main.rng.nextFloat() * 100 + miny;
			Obstacle o = new Obstacle(pos.x + rpos.x, pos.y + rpos.y, sx, sy);
			o.destructible = false;
			Main.scene.addEntity(o);
			pos = pos.addn(interval);
		}
	}
	public void spawnEnemy(){
		Vec2 pos = world.getRandomPos();
		while(pos.dist(player.getPos()) < 150  && pos.dist(player.getPos()) > 1000 ){
			pos = world.getRandomPos();
		}
		Enemy e = null;
		float f = Main.rng.nextFloat();
		if(Main.time < 15){
			if(f > 0.5f){
				e = new Enemy(player,Enemy.Type.Basic);
			}
		}else if(Main.time < 30){
			e = new Enemy(player,Enemy.Type.Basic);
		}else if(Main.time < 60){
			if(f > 0.1f){
				e = new Enemy(player,Enemy.Type.Basic);
			}else{
				e = new Enemy(player,Enemy.Type.MissileLauncher);
			}
		}else if(Main.time < 90){
			if(f > 0.2f){
				e = new Enemy(player,Enemy.Type.Basic);
			}else if (f > 0.18f){
				e = new Enemy(player,Enemy.Type.MissileLauncher);
			}else{
				e = new Enemy(player,Enemy.Type.Lasers);
			}
		}else if(Main.time < 120){
			if(f > 0.2f){
				e = new Enemy(player,Enemy.Type.Basic);
			}else if (f > 0.1f){
				e = new Enemy(player,Enemy.Type.MissileLauncher);
			}else{
				e = new Enemy(player,Enemy.Type.Lasers);
			}
		}else if (Main.time < 180){
			if(f > 0.3f){
				e = new Enemy(player,Enemy.Type.Basic);
			}else if (f > 0.2f){
				e = new Enemy(player,Enemy.Type.MissileLauncher);
			}else if (f > 0.05f){
				e = new Enemy(player,Enemy.Type.Lasers);
			}else{
				e = new Boss(player,3);
			}
		}else if(Main.time < 210){
			e = new Boss(player,3);
		}else if(Main.time < 220){
			e = new Boss(player,4);
		}else if(Main.time < 230){
			e = new Boss(player,5);
		}else if(Main.time < 240){
			e = new Boss(player,6);
		}else if(Main.time < 250){
			e = new Boss(player,7);
		}else{
			if(f > 0.75f){
				e = new Enemy(player,Enemy.Type.Basic);
			}else if(f > 0.5f){
				e = new Enemy(player,Enemy.Type.MissileLauncher);
			}else if(f > 0.25f){
				e = new Enemy(player,Enemy.Type.Lasers);
			}else{
				e = new Boss(player,10);
			}
		}
		if(e != null){
			e.setPos(pos);
			Main.scene.addEntity(e);
		}
	}
	public void spawnEnemies(int count){
		for(int i = 0; i < count; i++){
			spawnEnemy();
		}
	}
	public static void main(String[] args){
		Main.game = new GameMain();
		Main.run();
	}
	@Override
	public void OnGameStart() {
		try {
			Display.setFullscreen(false);
			Dbg.Log("is FullScreen ?:"+Display.isFullscreen());
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		Main.scene = new Scene();
		Main.camera = new Camera(new Vec2());
		Main.camera.setBgColor(new Color(0.1f,0.1f,0.1f));
		player = new Player();
		Main.scene.addEntity(player);
		Main.scene.setProximityMap(new ArrayProximityMap(world,32));
		//proxmap = new QTreeProximityMap(world,2,100);
		//Main.scene.setProximityMap(proxmap);
		buildObstacles(300);
		spawnEnemies(50);		
		score = 0;
		multiplier = 1;
		lastScoreUpdateTime = Main.time;
		enemyLastSpawn		= Main.time;
		bonusLastSpawn	   	= Main.time;
		Image img = new Image("art/smiley.png");
		img.register("Smiley");
	}
	@Override
	public void OnFrameStart() {		
	}
	@Override
	public void OnFrameEnd() {
		Main.scene.setActiveRegion(new BBox(player.getPos(),1400,900));
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			Main.exit();
		}
		if(Main.time > enemyLastSpawn + enemySpawnInterval){
			spawnEnemy();
			enemyLastSpawn = Main.time;
		}
		if(Main.time > this.lastScoreUpdateTime + 0.1f){
			score += multiplier;
			lastScoreUpdateTime = Main.time;
		}
		if(Main.time > this.bonusLastSpawn + this.bonusSpawnInterval){
			spawnBonus();
			bonusLastSpawn = Main.time;
		}
		Vec2 cp = Main.camera.getPos();
		float size = 32;
		if(score < 1000){
			size = 32;
		}else if(score < 10000){
			size = 36;
		}else if(score < 100000){
			size = 40;
		}else if(score < 500000){
			size = 44;
		}else if(score < 1000000){
			size = 48;	
		}else if(score < 5000000){
			size = 52;
		}else{
			size = 56;
		}
		Color.blue.glSet();
		GL11.glLineWidth(size/8);
		Draw.lcd(Main.camera.getPos().addn(-380,275-size), 0.5f, size, Integer.toString(score));
		GL11.glLineWidth(1);
		Draw.lcd(Main.camera.getPos().addn(-380,275-size-26), 0.5f, 16, Integer.toString(multiplier));
		if(score < SavedData.readInt("MaxScore", -1)){
			Color.white.glSet();
		}else{
			Color.red.glSet();
		}
		GL11.glLineWidth(size/8);
		Draw.lcd(Main.camera.getPos().addn(-382,277-size), 0.6f, size, Integer.toString(score));
		GL11.glLineWidth(1);
		Draw.lcd(Main.camera.getPos().addn(-382,277-size-26), 0.6f, 16, Integer.toString(multiplier));
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glEnable(GL11.GL_BLEND);
		Color.darkgray.glSet();
		Statistics.drawStats(Main.camera.getPos().addn(240,280));
		GL11.glDisable(GL11.GL_BLEND);
		Color.white.glSet();
		/*Raytrace rt = Main.scene.raytrace(Main.camera.getPos(), 
										  Main.camera.getMousePos().subn(Main.camera.getPos()), 
										  300);
		Color.white.glSet();
		Draw.line(rt.getPos(), rt.getPos().addn(rt.getDir().setLengthn(300)));
		Vec2 cols = new Vec2();
		Vec2 cole = new Vec2();
		Ent  col  = rt.next(cols, cole);
		while(col != null){
			GL11.glLineWidth(3);
			Color.white.glSet();
			Draw.line(cols, cole);
			GL11.glLineWidth(1);
			Color.green.glSet();
			Draw.disc(cols, 4);
			Color.red.glSet();
			Draw.disc(cole,4);
			col  = rt.next(cols, cole);
		}*/
		//Image img = Image.load("Smiley");
		//Draw.boxAdditiveTextured(new BBox(img.getSize()), img);
		
	}
	@Override
	public void OnGameEnd() {
		int bestscore = SavedData.readInt("MaxScore", -1);
		Dbg.Log("Score: "+score+" Best Score: "+bestscore);
	}
}
