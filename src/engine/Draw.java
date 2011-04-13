package engine;

import math.BBox;
import math.Vec2;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL11.*;

public class Draw {
	public static void point(Vec2 pos){
		GL11.glBegin(GL11.GL_POINT);
			GL11.glVertex3f(pos.x, pos.y, 0.0f);
		GL11.glEnd();
	}
	public static void point(Vec2 pos,float z){
		GL11.glBegin(GL11.GL_POINT);
			GL11.glVertex3f(pos.x, pos.y, z);
		GL11.glEnd();
	}
	public static void line(Vec2 start, Vec2 end){
		GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex3f(start.x, start.y, 0.0f);
			GL11.glVertex3f(end.x, end.y, 0.0f);
		GL11.glEnd();
	}
	public static void line(Vec2 start, Vec2 end,float z){
		GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex3f(start.x, start.y, z);
			GL11.glVertex3f(end.x, end.y, z);
		GL11.glEnd();
	}
	public static void box(BBox box){
		GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2f(box.l(), box.d());
			GL11.glVertex2f(box.r(), box.d());
			GL11.glVertex2f(box.r(), box.u());
			GL11.glVertex2f(box.l(), box.u());
		GL11.glEnd();
	}
	public static void box(BBox box,float z){
		GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex3f(box.l(), box.d(),z);
			GL11.glVertex3f(box.r(), box.d(),z);
			GL11.glVertex3f(box.r(), box.u(),z);
			GL11.glVertex3f(box.l(), box.u(),z);
		GL11.glEnd();
	}
	public static void boxFilled(BBox box){
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(box.l(), box.d());
			GL11.glVertex2f(box.r(), box.d());
			GL11.glVertex2f(box.r(), box.u());
			GL11.glVertex2f(box.l(), box.u());
		GL11.glEnd();
	}
	public static void boxFilled(BBox box,float z){
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3f(box.l(), box.d(),z);
			GL11.glVertex3f(box.r(), box.d(),z);
			GL11.glVertex3f(box.r(), box.u(),z);
			GL11.glVertex3f(box.l(), box.u(),z);
		GL11.glEnd();
	}
	public static void circle(Vec2 pos, float radius){
		circle(pos,radius,0.0f);
	}
	public static void circle(Vec2 pos, float radius,float z){
		int segments = 32;
		float stepangle = (float)(2*Math.PI/segments);
		float angle = 0;
		GL11.glBegin(GL11.GL_LINE_LOOP);
		for(int i = 0; i< segments; i++){
			float px = (float)Math.cos(angle);
			float py = (float)Math.sin(angle);
			GL11.glVertex3f(pos.x + px*radius, pos.y + py*radius, z);
			angle += stepangle;
		}
		GL11.glEnd();
	}
	public static void disc(Vec2 pos, float radius){
		disc(pos,radius,0.0f);
	}
	public static void disc(Vec2 pos, float radius,float z){
		int segments = 32;
		float stepangle = (float)(2*Math.PI/segments);
		float angle = 0;
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex3f(pos.x, pos.y, z);
		for(int i = 0; i< segments + 1; i++){
			float px = (float)Math.cos(angle);
			float py = (float)Math.sin(angle);
			GL11.glVertex3f(pos.x + px*radius, pos.y + py*radius, z);
			angle += stepangle;
		}
		GL11.glEnd();
	}
}
