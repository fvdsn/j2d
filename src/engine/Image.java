package engine;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Hashtable;

import math.Vec2;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class Image {
	private static Hashtable<String,Image> library = new Hashtable<String,Image>();
	boolean registered = false;
	
	private String path;
	private String name = "Unnamed Image";
	private int sizex = 0;
	private int sizey = 0;
	private ByteBuffer data;
	
	private int texName = 0;
	private boolean loaded = false;
	
	public Image(String path){
		try{
		InputStream in = new FileInputStream(path);
		PNGDecoder decoder = new PNGDecoder(in);
		sizex = decoder.getWidth();
		sizey = decoder.getHeight();
		
		data = ByteBuffer.allocateDirect(4*sizex*sizey);
		decoder.decode(data, sizex*4, Format.RGBA);
		data.flip();

		in.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public int getSizeX(){
		return sizex;
	}
	public int getSizeY(){
		return sizey;
	}
	public Vec2 getSize(){
		return new Vec2(sizex,sizey);
	}
	public void glLoad(){
		if(!loaded){
			texName = GL11.glGenTextures();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texName);
			GL11.glTexImage2D(
				GL11.GL_TEXTURE_2D,	//target 
				0, //level, 
				GL11.GL_RGBA, // internalformat, 
				sizex, //width, 
				sizey, // height, 
				0, //border, 
				GL11.GL_RGBA, //format, 
				GL11.GL_UNSIGNED_BYTE, //type, 
				data // pixels
			);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			loaded = true;
		}
	}
	public void glTex(){
		if(!loaded){
			glLoad();
		}
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texName);
	}
	
	public void register(String name){
		this.name = name;
		library.put(name, this);
		registered = true;
	}
	public void unregister(){
		if(registered){
			library.remove(name);
		}
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		if(!registered){
			this.name = name;
		}else{
			unregister();
			register(name);
		}
	}
	public static Image load(String name){
		return library.get(name);
	}
	
	
}
