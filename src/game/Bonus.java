package game;

import engine.*;
import math.*;

public class Bonus extends Ent{
	public enum Type{
		Shield,
		Phantom,
		Bounce,
		AutoAim,
		ExtraFire,
		HiPower,
		None
	}
	public Color color = Color.black;
	public Color altcolor = Color.white;
	public Type type = Type.None;
	private float duration = 14.0f;
	public Bonus(Vec2 pos, Type type){
		super();
		this.type = type;
		this.setPos(pos);
		this.setBound(new BBox(20,20));
		this.collisionBehaviour = CollisionBehaviour.Receiver;
		this.destroy(60);
		switch(type){
		case Shield:
			color = Color.lightblue;
			altcolor = Color.white;
			break;
		case Phantom:
			color = new Color(0.5f,0,1);
			altcolor = new Color(0.5f,0,1);
			break;
		case Bounce:
			color = new Color(0.1f,0.1f,1.0f);
			altcolor = new Color(1f,0,0.1f);
			break;
		case AutoAim:
			color = new Color(1,0.5f,1);
			altcolor = 	new Color(1,0.5f,1);
			break;
		case ExtraFire:
			color = new Color(1,1,0);
			altcolor = new Color(1,1,0);
			break;
		case HiPower:
			color = new Color(1,0.5f,0);
			altcolor = new Color(1,0,0);
			break;
		}
	}
	public void OnCollisionReceive(Ent col, Vec2 colVec){
		if(col instanceof Player){
			((Player) col).setBonus(this, duration);
			destroy();
		}
	}
	public void OnDraw(){
		switch(type){
		case Shield:
			Color.lightblue.glSet();
			Draw.disc(10);
			Color.white.glSet();
			Draw.circle(9);
			Color.blue.glSet();
			Draw.lcd(new Vec2(-5,-9), 0, 16, "S");
			Color.white.glSet();
			Draw.lcd(new Vec2(-6,-8), 0, 16, "S");
			break;
		case ExtraFire:
			new Color(1,1,0).glSet();
			Draw.disc(10);
			break;
		case Phantom:
			new Color(0.5f,0,1).glSet();
			Draw.disc(10);
			break;
		case AutoAim:
			new Color(1,0.5f,1).glSet();
			Draw.disc(10);
			break;
		case Bounce:
			new Color(0.1f,0.1f,1.0f).glSet();
			Draw.disc(10);
			new Color(1f,0,0.1f).glSet();
			Draw.circle(10);
			break;
		case HiPower:
			color.glSet();
			Draw.disc(10);
			altcolor.glSet();
			Draw.circle(10);
			break;
		default:
			Color.blue.glSet();
			Draw.disc(10);
			break;
				
		}
	}
}
