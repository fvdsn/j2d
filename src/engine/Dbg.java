package engine;

import org.lwjgl.opengl.GL11;

import math.Color;
import math.Vec2;

/** This class is used to print debug message to the standard output */
public class Dbg {
	/** Turns on or off the debug messages */
	public static boolean debug = true;
	/** Prints the string to the standard output, if debug is activated */
	public static void Log(String s){
		if(debug){
			System.out.println(s);
		}
	}
	/** Prints the object to the standard output, if debug is activated */
	public static void Log(Object s){
		if(debug){
			System.out.println(s.toString());
		}
	}
	/** Prints the int to the standard output, if debug is activated */
	public static void Log(int i){
		if(debug){
			System.out.println(i);
		}
	}
	/** Prints the long to the standard output, if debug is activated */
	public static void Log(long l){
		if(debug){
			System.out.println(l);
		}
	}
	/** Prints the double to the standard output, if debug is activated */
	public static void Log(double v){
		if(debug){
			System.out.println(v);
		}
	}
	/** Prints the float to the standard output, if debug is activated */
	public static void Log(float f){
		if(debug){
			System.out.println(f);
		}
	}
	/** Prints a warning message to the standard output, if debug is activated */
	public static void Warning(String s){
		if(debug){
			System.out.println("Warning:"+s);
		}
	}
	/** Prints an error message and a backtrace to the standard output, if debug is activated */
	public static void Error(String name, String reason){
		Error(name+"\nReason:"+reason);
	}
	/** Prints an error message and a backtrance to the standard output, if debug is activated */
	public static void Error(String s){
		if(debug){
			System.out.println("\nERROR:"+s);
			Throwable t = new Throwable();
			t.printStackTrace(System.out);
			System.out.println("\n");
		}
	}
	/** Prints a fatal error message and a backtrace the standard output then exits the game, if debug is activated */
	public static void FatalError(String name, String reason){
		FatalError(name+"\nReason:"+reason);
	}
	/** Prints a fatal error message and a backtrace the standard output then exits the game, if debug is activated */
	public static void FatalError(String s){
		if(debug){
			System.out.println("\n********************************************");
			System.out.println("FATAL ERROR:"+s);
			Throwable t = new Throwable();
			t.printStackTrace(System.out);
			System.out.println("EXITING PROGRAM");
			System.out.println("********************************************");

			System.exit(1);
		}
	}
	/** Draws a 1px point of color c at position pos, if debug is activated */
	public static void drawPoint(Vec2 pos, Color c){
		if(debug){
			GL11.glColor4f(c.r, c.g, c.b, c.a);
			Draw.point(pos);
		}
	}
	/** Draws a 1px line of color c from start to end, if debug is activated */
	public static void drawLine(Vec2 start, Vec2 end, Color c){
		if(debug){
			GL11.glColor4f(c.r,c.g,c.b,c.a);
			Draw.line(start, end);
		}
	}
	/** Draws a 10px x/y red/green axis at position pos, if debug is activated */
	public static void drawRef(Vec2 pos){
		drawRef(pos,10);
	}
	/** Draws a scale px x/y red/green axis at position pos, if debug is activated */
	public static void drawRef(Vec2 pos,float scale){
		drawLine(pos,pos.addn(new Vec2(scale,0)),Color.red);
		drawLine(pos,pos.addn(new Vec2(0,scale)),Color.green);
	}
}
