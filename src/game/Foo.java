package game;

import engine.*;
import math.*;

public class Foo extends Ent{
	Color c = Color.blue;
	
	public Foo(Color c){
		super();
		this.c = c;
	}
	
	public void draw(){
		c.glSet();
		Draw.circle(new Vec2(), 5);
	}
}
