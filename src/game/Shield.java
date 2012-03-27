package game;

import org.lwjgl.opengl.GL11;

import engine.*;
import math.*;
public class Shield extends Ent{
	public Shield(Vec2 pos){
		super();
		setPos(pos);
		setBound(new BBox(50,50));
		destroy(2.0f);
		this.collisionBehaviour = CollisionBehaviour.Receiver;
		Main.scene.addEntity(this);
	}
	public Shield(Vec2 pos, float duration){
		super();
		setPos(pos);
		setBound(new BBox(50,50));
		destroy(duration);
		this.collisionBehaviour = CollisionBehaviour.Receiver;
		Main.scene.addEntity(this);
	}
	public void OnUpdate(){
		Vec2 scale = this.getScale();
		scale.add(new Vec2(2.0f,2.0f).scalen(Main.deltaTime));
		this.setLocalScale(scale);
	}
	public void OnDraw(){
		Color.blue.glSet();
		//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		//GL11.glEnable(GL11.GL_BLEND);
		Draw.box(new BBox(50,50));
		Draw.box(getBound().dupAtZero(),5);
		//GL11.glLineWidth(10);
		//GL11.glDisable(GL11.GL_BLEND);

	}
}
