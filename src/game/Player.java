package game;

import math.*;

import engine.Dbg;
import engine.Draw;
import engine.Ent;
import engine.Main;

import org.lwjgl.input.*;
public class Player extends Ent {
	public float mspeed = 100.0f;
	public Player(){
		super();
		setScale(new Vec2(3.0f,1.0f));
		Ent l = new Foo(Color.blue);
		l.setPos(-10,-5);
		Ent r = new Foo(Color.red);
		r.setPos(10,-5);
		addChild(l);
		addChild(r);
	}
	public void OnUpdate(){
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			translate(-mspeed*Main.deltaTime,0);
		}else if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			translate(mspeed*Main.deltaTime,0);
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			translate(0,-mspeed*Main.deltaTime);
		}else if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			translate(0,mspeed*Main.deltaTime);
		}
		Vec2 dist = Main.camera.getPosition().subn(getWPos());
		dist.scale(0.99f);
		Main.camera.setPosition(getWPos().addn(dist));
		for(Ent e: getChildList()){
			Dbg.drawRef(e.getWPos());
		}
	}
	public void draw(){
		Color.orange.glSet();
		Draw.circle(new Vec2(), 10);
	}
}
