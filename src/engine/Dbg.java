package engine;

public class Dbg {
	public static boolean debug = true;
	public static void Log(String s){
		if(debug){
			System.out.println(s);
		}
	}
	public static void Warning(String s){
		if(debug){
			System.out.println("Warning:"+s);
		}
	}
	public static void Error(String name, String reason){
		Error(name+"\nReason:"+reason);
	}
	public static void Error(String s){
		if(debug){
			System.out.println("\nERROR:"+s);
			Throwable t = new Throwable();
			t.printStackTrace(System.out);
			System.out.println("\n");
		}
	}
	public static void FatalError(String name, String reason){
		FatalError(name+"\nReason:"+reason);
	}
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
}
