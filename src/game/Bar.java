package game;

import math.*;
import engine.*;

public class Bar extends PhysEnt {
	public Bar(){
		super();
		this.useAccel = false;
		this.gravity  = new Vec2(0,-10);
	}
	public void draw(){
		Color.purple.glSet();
		Draw.circle(new Vec2(), 3);
	}

}
