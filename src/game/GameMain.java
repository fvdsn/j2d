package game;

import engine.*;
import math.*;

public class GameMain {
	public static void main(String[] args){
		Main.game = new GameDefault();
		Main.scene = new Scene();
		Main.camera = new Camera(new Vec2());
		Ent p = new Player();
		Main.scene.addEntity(p);
		Main.scene.addEntity(new Foo(Color.red));
		PhysEnt pe = new Bar();
		pe.setPos(10,30);
		Main.scene.addEntity(pe);
		Main.runLoop();
	}
}
