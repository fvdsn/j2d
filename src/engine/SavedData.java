package engine;

import java.io.*;
import java.util.Hashtable;

import math.BoundIF;
import math.Vec2;

/** Saved Data is used to save various user data on the disk  between games.
 * @author fred
 */
public class SavedData{
	public static String userdatapath = "j2d_data";
	private static Hashtable<String,Serializable> library = null;
	
	private static Hashtable<String,Serializable> readLibrary(){
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		Hashtable<String,Serializable> lib = null;

		try{
			fis = new FileInputStream(userdatapath);
			ois = new ObjectInputStream(fis);
			lib = (Hashtable<String,Serializable>)ois.readObject();
			ois.close();
			fis.close();
		}catch(IOException e){
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return lib;
	}
	private static void writeLibrary(Hashtable<String,Serializable> lib){
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try{
			fos = new FileOutputStream(userdatapath);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(lib);
			oos.close();
			fos.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public static void write(String key, Serializable value){
		if(library == null){
			library = readLibrary();
			if(library == null){
				library = new Hashtable<String,Serializable>();
				Dbg.Log("Saved Data file not found, creating a new one");
			}
		}
		library.put(key, value);
		writeLibrary(library);
	}
	public static Object read(String key){
		if(library == null){
			library = readLibrary();
			if(library == null){
				return null;
			}
		}
		return library.get(key);
	}
	public static String readString(String key){
		Object r = read(key);
		if(r != null && r instanceof String){
			return (String)r;
		}
		return null;
	}
	public static int readInt(String key, int def_value){
		Object r = read(key);
		if(r == null){
			return def_value;
		}else if(r instanceof Integer){
			return (Integer)r;
		}else{
			return def_value;
		}
	}
	public static Ent readEnt(String key){
		Object r = read(key);
		if(r != null && r instanceof Ent){
			return (Ent)r;
		}
		return null;
	}
	public static Vec2 readVec2(String key){
		Object r = read(key);
		if(r != null && r instanceof Vec2){
			return (Vec2)r;
		}
		return null;
	}
	public static BoundIF readBound(String key){
		Object r = read(key);
		if(r != null && r instanceof BoundIF){
			return (BoundIF)r;
		}
		return null;
	}
	
}
