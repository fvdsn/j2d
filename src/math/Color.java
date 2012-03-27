package math;

import org.lwjgl.opengl.GL11;

public final class Color {
	public static final Color red 		= new Color(1,0,0);
	public static final Color green 	= new Color(0,1,0);
	public static final Color blue 		= new Color(0,0,1);
	public static final Color white 	= new Color(1,1,1);
	public static final Color black 	= new Color(0,0,0);
	public static final Color gray  	= new Color(0.5f,0.5f,0.5f);
	public static final Color darkgray  = new Color(0.25f,0.25f,0.25f);
	public static final Color lightgray = new Color(0.75f,0.75f,0.75f);
	public static final Color orange 	= new Color(1,0.5f,0);
	public static final Color yellow 	= new Color(1,1,0);
	public static final Color purple 	= new Color(1,0,1);
	public static final Color bluegreen = new Color(0,1,1);
	public static final Color darkred	= new Color(0.5f,0,0);
	public static final Color darkgreen	= new Color(0,0.5f,0);
	public static final Color darkblue	= new Color(0,0,0.5f);
	public static final Color lightred	= new Color(1,0.5f,0.5f);
	public static final Color lightgreen= new Color(0.5f,1,0.5f);
	public static final Color lightblue	= new Color(0.5f,0.5f,1);

	public final float r;
	public final float g;
	public final float b;
	public final float a;
	public Color(){
		this.r = 0.0f;
		this.g = 0.0f;
		this.b = 0.0f;
		this.a = 1.0f;
	}
	public Color(float r, float g, float b){
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = 1.0f;
	}
	public Color(float r, float g, float b, float a){
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	public void glSet(){
		GL11.glColor4f(r,g,b,a);
	}
}
