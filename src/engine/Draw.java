package engine;

import math.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL11.*;
/**
 * This class provides simple OpenGL drawing primitives that can be used to render the
 * game entities. The color, styling, etc. usually depend on the OpenGL rendering Context.
 * For example you should use Color.glSet() to set the drawing color.
 * 
 * Drawing in the Ent.draw() method will draw in the entity local space (0,0) is centered
 * on the entity, etc.
 * 
 * Drawing outside will usually draw in world space.
 *
 */
public class Draw {
	/*
	 * LCD Bar Coords index :
	 *  12-13-14
	 *  |   |
	 *  9 10 11
	 *  |   |
	 *  6-7-8
	 *  |   |
	 *  3 4 5
	 *  |   |
	 *  0-1-2
	 */
	/** Position of the lcd Coord index */
	private static final float[][] LCD_COORDS = {
			{0,0},		{0.25f,0},		{0.5f,0},
			{0,0.25f},	{0.25f,0.25f},	{0.5f,0.25f},
			{0,0.5f},	{0.25f,0.5f},	{0.5f,0.5f},
			{0,0.75f},	{0.25f,0.75f},	{0.5f,0.75f},
			{0,1},		{0.25f,1},		{0.5f,1}

		};
	/** LCD glyphs of symbols. A glyph is A list of segments defined by pair of lcd coord indexes */
	private static final int[][][] LCD_SYMBOLS = {	
			{{13,7},{4,1}},										//!			//0
			{{13,10},{14,11}},									//"
			{{0,6},{6,12},{2,8},{8,14},{3,5},{9,11}},			//#
			{{0,2},{2,8},{8,6},{6,12},{12,14},{1,7},{7,13}},	//$
			{{1,2},{2,5},{5,4},{4,1},{0,7},{7,14},{9,10},{10,13},{13,12},{12,9}},//%
			{{2,6},{6,12},{12,13},{13,7},{7,3},{3,0},{0,8}},	//&
			{{13,10}},											//'
			{{13,9},{9,6},{6,3},{3,1}},							//(
			{{13,11},{11,8},{8,5},{5,1}},						//)
			{{10,4},{9,5},{11,3}},								// *
			{{10,4},{6,8}},										//+
			{{4,0}},											//,
			{{6,8}},											//-
			{{0,3},{3,4},{4,1},{1,0}},							//.
			{{0,7},{7,14}},										///			//14

			{{0,3},{3,4},{4,1},{1,0},{9,10},{10,7},{7,6},{6,9}},//:			//15
			{{4,0},{9,10},{10,7},{7,6},{6,9}},					//;
			{{11,6},{6,5}},										//<
			{{9,11},{3,5}},										//=
			{{9,8},{8,3}},										//>
			{{9,12},{12,14},{14,11},{11,7},{4,1}},				//?
			{{8,7},{7,4},{4,5},{5,8},{8,14},{14,12},{12,6},{6,0},{0,2}},		 //@	//21

			{{13,12},{12,6},{6,0},{0,1}},						//[			//22
			{{12,7},{7,2}},										//\
			{{13,14},{14,8},{8,2},{2,1}},						//]
			{{9,13},{13,11}},									//^
			{{0,2}},											//_			//26

			{{13,9},{9,7},{7,6},{7,3},{3,1}},					//{			//27
			{{13,10},{4,1}},									//| 
			{{13,11},{11,7},{7,8},{7,5},{5,1}},					//}
			{{6,10},{4,8}},										//~			//30
			
			{{12,10}},											//`			//31

	};
	/** LCD glyphs of capital letters. A glyph is A list of segments defined by pair of lcd coord indexes */
	private static final int[][][] LCD_CAPITALS = {	
			{{0,6},{6,13},{13,8},{8,2},{6,8}}, 					//A
			{{0,6},{6,12},{12,11},{11,8},{8,2},{2,0},{6,8}},	//B
			{{0,6},{6,12},{12,14},{0,2}},						//C
			{{0,6},{6,12},{12,11},{11,8},{8,2},{2,0}},			//D
			{{0,6},{6,12},{12,14},{6,7},{0,2}},					//E
			{{0,6},{6,12},{12,14},{6,7}},						//F
			{{0,6},{6,12},{12,14},{0,2},{2,8},{8,7}},			//G
			{{0,6},{6,12},{6,8},{2,8},{8,14}},					//H
			{{1,7},{7,13}},										//I
			{{14,8},{8,2},{2,0},{0,3}},							//J
			{{0,6},{6,12},{6,8},{8,2},{7,14}},					//K
			{{0,6},{6,12},{0,2}},								//L
			{{0,6},{6,12},{12,10},{10,14},{14,8},{8,2}},		//M
			{{0,6},{6,12},{12,7},{7,2},{2,8},{8,14}},			//N
			{{0,6},{6,12},{12,14},{14,8},{8,2},{2,0}},			//O
			{{0,6},{6,12},{12,14},{14,8},{8,6}},				//P
			{{0,6},{6,12},{12,14},{14,8},{8,2},{2,0},{1,4}},	//Q
			{{0,6},{6,12},{12,14},{14,8},{8,6},{6,2}},			//R
			{{0,2},{2,8},{8,6},{6,12},{12,14}},					//S
			{{12,14},{13,7},{7,1}},								//T
			{{0,6},{6,12},{14,8},{8,2},{2,0}},					//U
			{{12,6},{6,1},{1,8},{8,14}},						//V
			{{12,6},{6,0},{0,4},{4,2},{2,8},{8,14}},			//W
			{{12,7},{7,14},{0,7},{7,2}},						//X
			{{12,7},{14,7},{7,1}},								//Y
			{{12,14},{14,7},{7,0},{0,2}},						//Z
	};
	/** LCD glyphs of numbers. A glyph is A list of segments defined by pair of lcd coord indexes */

	private static final int[][][] LCD_NUMBERS  = {
			{{0,6},{6,12},{12,14},{14,8},{8,2},{2,0}},			//0
			{{2,8},{8,14}},										//1
			{{12,14},{14,8},{8,6},{6,0},{0,2}},					//2
			{{12,14},{14,8},{8,6},{8,2},{2,0}},					//3
			{{12,6},{6,8},{14,8},{8,2}},						//4
			{{14,12},{12,6},{6,8},{8,2},{2,0}},					//5
			{{14,12},{12,6},{6,8},{8,2},{2,0},{0,6}},			//6
			{{2,8},{8,14},{14,12}},								//7
			{{14,12},{12,6},{6,8},{8,2},{2,0},{0,6},{8,14}},	//8
			{{14,12},{12,6},{6,8},{8,2},{2,0},{8,14}}			//9
	};
	/** Return the LCD Glyph corresponding to a ANSI character. 
	 *  if the character is not supported it will return the code for '#' */
	private static int[][] lcdGetCode(char c){
		//[!0  /14]  [:15 @21]  [[22  _26]  [{27  ~30] [`31]
		if(c >= 'a' && c <= 'z'){
			return LCD_CAPITALS[c-'a'];
		}else if(c >= 'A' && c <= 'Z'){
			return LCD_CAPITALS[c - 'A'];
		}else if(c >= '0' && c <= '9'){
			return LCD_NUMBERS[c - '0'];
		}else if(c >= '!' && c <= '/'){
			return LCD_SYMBOLS[c - '!'];
		}else if(c >= ':' && c <= '@'){
			return LCD_SYMBOLS[c - ':' + 15];
		}else if(c >= '[' && c <= '_'){
			return LCD_SYMBOLS[c - '[' + 22];
		}else if(c >= '{' && c <= '~'){
			return LCD_SYMBOLS[c - '{' + 27];
		}else if(c == '`'){
			return LCD_SYMBOLS[31];
		}else{
			return LCD_SYMBOLS[2];	//#
		}
	}
	/** Draws a single Glyph with (px,py) as lower left corner world position,
	 *  z as depth, size as the world height of the glyph, c as the ansi code of the character */
	private static void lcdPrintChar(float px, float py, float z, float size, char c){
		int[][] code = lcdGetCode(c);
		for(int[] pair: code){
			GL11.glBegin(GL11.GL_LINE);
				GL11.glVertex3f(LCD_COORDS[pair[0]][0]*size+px,LCD_COORDS[pair[0]][1]*size+py,z);
				GL11.glVertex3f(LCD_COORDS[pair[1]][0]*size+px,LCD_COORDS[pair[1]][1]*size+py,z);
			GL11.glEnd();
		}
	}
	/** Draws a complete string of text on screen with lcd glyphes.
	 *  Supports new lines, spaces, and tabs (4 spaces)
	 *  Unsupported characters are drawn as '#'
	 *  Line width, color, and blending are provided by the OpenGL context.
	 *  
	 * @param pos The world position of the lower left corner of the first line of text 
	 * @param z The opengl depth
	 * @param size The world height of the characters
	 * @param str The string to draw 
	 */
	public static void lcd(Vec2 pos, float z, float size, String str){
		float px = pos.x;
		float py = pos.y;
		float Dx = size * 0.75f;
		float Dy = size * 1.25f;
		int i = 0;
		while(i < str.length()){
			char c = str.charAt(i);
			if(c == ' '){
				px += Dx;
			}else if(c == '\t'){
				px += Dx * 4;
			}else if(c == '\n'){
				px = pos.x;
				py -= Dy;
			}else{
				lcdPrintChar(px,py,z,size,c);
				px += Dx;
			}
			i++;
		}
	}
	/** Draw a point at (0,0) with depth 0
	 */
	public static void point(){
		GL11.glBegin(GL11.GL_POINT);
			GL11.glVertex2f(0.0f, 0.0f);
		GL11.glEnd();
	}
	/** Draw a point at pos with depth 0*/
	public static void point(Vec2 pos){
		GL11.glBegin(GL11.GL_POINT);
			GL11.glVertex2f(pos.x, pos.y);
		GL11.glEnd();
	}
	/** Draw a point at (px,py) with depth 0*/
	public static void point(float px, float py){
		GL11.glBegin(GL11.GL_POINT);
			GL11.glVertex2f(px, py);
		GL11.glEnd();
	}
	/** Draw a point at pos, with depth z */
	public static void point(Vec2 pos,float z){
		GL11.glBegin(GL11.GL_POINT);
			GL11.glVertex3f(pos.x, pos.y, z);
		GL11.glEnd();
	}
	/** Draw a point at (px,py) with depth z */
	public static void point(float px, float py, float z){
		GL11.glBegin(GL11.GL_POINT);
			GL11.glVertex3f(px, py, z);
		GL11.glEnd();
	}
	/** Draw a line from start to end with depth 0 */
	public static void line(Vec2 start, Vec2 end){
		GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex3f(start.x, start.y, 0.0f);
			GL11.glVertex3f(end.x, end.y, 0.0f);
		GL11.glEnd();
	}
	/** Draw a line from start to end with depth z */
	public static void line(Vec2 start, Vec2 end,float z){
		GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex3f(start.x, start.y, z);
			GL11.glVertex3f(end.x, end.y, z);
		GL11.glEnd();
	}
	/** Draw a box outline with the size and position of box */
	public static void box(BoundIF box){
		GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2f(box.l(), box.d());
			GL11.glVertex2f(box.r(), box.d());
			GL11.glVertex2f(box.r(), box.u());
			GL11.glVertex2f(box.l(), box.u());
		GL11.glEnd();
	}
	/** Draw a box outline with the size and position of box at depth z */
	public static void box(BoundIF box,float z){
		GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex3f(box.l(), box.d(),z);
			GL11.glVertex3f(box.r(), box.d(),z);
			GL11.glVertex3f(box.r(), box.u(),z);
			GL11.glVertex3f(box.l(), box.u(),z);
		GL11.glEnd();
	}
	/** Draw a color filled box with the size and position of box at depth 0 */
	public static void boxFilled(BoundIF box){
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(box.l(), box.d());
			GL11.glVertex2f(box.r(), box.d());
			GL11.glVertex2f(box.r(), box.u());
			GL11.glVertex2f(box.l(), box.u());
		GL11.glEnd();
	}
	/** Draw a color filled box with the size and position of box at depth z */
	public static void boxFilled(BoundIF box,float z){
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3f(box.l(), box.d(),z);
			GL11.glVertex3f(box.r(), box.d(),z);
			GL11.glVertex3f(box.r(), box.u(),z);
			GL11.glVertex3f(box.l(), box.u(),z);
		GL11.glEnd();
	}
	/** Draw a box textured with a solid image (img) the size and position of box, at depth 0 */
	public static void boxTextured(BoundIF box, Image img){
		boxTextured(box,0,img);
	}
	/** Draw a box textured with a solid image (img) the size and position of box, at depth z */
	public static void boxTextured(BoundIF box, float z, Image img){
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		img.glTex();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0,1);
		GL11.glVertex3f(box.l(), box.d(),z);
		GL11.glTexCoord2f(1,1);
		GL11.glVertex3f(box.r(), box.d(),z);
		GL11.glTexCoord2f(1,0);
		GL11.glVertex3f(box.r(), box.u(),z);
		GL11.glTexCoord2f(0,0);
		GL11.glVertex3f(box.l(), box.u(),z);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}
	/** Draw a box textured with a alpha blended image (img) the size and position of box */
	public static void boxAlphaTextured(BoundIF box, Image img){
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		boxTextured(box,0,img);
		GL11.glDisable(GL11.GL_BLEND);
	}
	/** Draw a box textured with a additive blended image (img) the size and position of box */
	public static void boxAdditiveTextured(BoundIF box, Image img){
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glEnable(GL11.GL_BLEND);
		boxTextured(box,0,img);
		GL11.glDisable(GL11.GL_BLEND);
	}
	/** Draw a circle outline of radius 'radius' centered on (0,0) at depth 0 */
	public static void circle(float radius){
		circle(0.0f,0.0f,radius,0.0f);
	}
	/** Draw a circle outline of radius 'radius' centered on pos at depth 0 */

	public static void circle(Vec2 pos, float radius){
		circle(pos.x,pos.y,radius,0.0f);
	}
	/** Draw a circle outline of radius 'radius' centered on pos at depth z */

	public static void circle(Vec2 pos, float radius, float z){
		circle(pos.x,pos.y,radius,z);
	}
	/** Draw a circle outline of radius 'radius' centered on (cx,cy) at depth 0 */
	public static void circle(float cx, float cy, float radius){
		circle(cx,cy,radius,0.0f);
	}
	/** Draw a circle outline of radius 'radius' centered on (cx,cy) at depth z */
	public static void circle(float cx, float cy, float radius,float z){
		int segments = 32;
		float stepangle = (float)(2*Math.PI/segments);
		float angle = 0;
		GL11.glBegin(GL11.GL_LINE_LOOP);
		for(int i = 0; i< segments; i++){
			float px = (float)Math.cos(angle);
			float py = (float)Math.sin(angle);
			GL11.glVertex3f(cx + px*radius, cy + py*radius, z);
			angle += stepangle;
		}
		GL11.glEnd();
	}
	/** Draw an arc of radius 'radius' from 'startangle' to 'startangle+arcangle' centered on (0,0), at depth 0 */
	public static void arc(float radius, float startangle, float arcangle){
		arc(0.0f,0.0f,radius,startangle,arcangle,0.0f);
	}
	/** Draw an arc of radius 'radius' from 'startangle' to 'startangle+arcangle' centered on pos, at depth 0 */
	public static void arc(Vec2 pos, float radius, float startangle, float arcangle){
		arc(pos.x,pos.y,radius,startangle,arcangle,0.0f);
	}
	/** Draw an arc of radius 'radius' from 'startangle' to 'startangle+arcangle' centered on pos, at depth z */
	public static void arc(Vec2 pos, float radius, float startangle, float arcangle, float z){
		arc(pos.x,pos.y,radius,startangle,arcangle,z);
	}
	/** Draw an arc of radius 'radius' from 'startangle' to 'startangle+arcangle' centered on (cx,cy), at depth 0 */
	public static void arc(float cx, float cy, float radius, float startangle, float arcangle, float z){
		int segments = 32;
		float stepangle = (float)(2*Math.PI/(segments-1)*(arcangle/360.0f));
		float angle = (float)(Math.PI*startangle/180.0f);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		for(int i = 0; i< segments; i++){
			float px = (float)Math.cos(angle);
			float py = (float)Math.sin(angle);
			GL11.glVertex3f(cx + px*radius, cy + py*radius, z);
			angle += stepangle;
		}
		GL11.glEnd();
	}
	/** Draw a color filled disc of radius 'radius' centerd on (0,0) at depth 0 */
	public static void disc(float radius){
		disc(new Vec2(),radius,0.0f);
	}
	/** Draw a color filled disc of radius 'radius' centerd on pos at depth 0 */
	public static void disc(Vec2 pos, float radius){
		disc(pos,radius,0.0f);
	}
	/** Draw a color filled disc of radius 'radius' centerd on pos at depth z */
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
