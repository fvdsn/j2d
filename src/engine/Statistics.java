package engine;

import math.Vec2;

/** This module provides various usefull statistics that can be
 *  used to asses the performances of the game. Statistics are
 *  generally reset on the beginning of each frame and built during
 *  the frame and are only supposed to be complete in the OnFrameEnd() 
 *  function.
 */
public class Statistics {
	/** The total count of entities this frame in the active scene */
	public static int entCount = 0;
	/** The total count of entities that have been updated this frame in the active scene */
	public static int entUpdatedCount = 0;
	/** The total count of root entities this frame in the active scene */
	public static int entRootCount = 0;
	/** The total count of entites that appeared this frame in the active scene */
	public static int entNewCount = 0;
	/** The total count of PhysEnt updated this frame in the active scene */
	public static int entPhysUpCount = 0;
	/** The total count of entities destroyed this frame in the active scene */
	public static int entDestroyedCount = 0;
	/** The total count of entities that could have emitted a collision this frame in the active scene */
	public static int entCEmitterCount = 0;
	/** The total count of entities that could have received a collision this frame in the active scene */
	public static int entCReceiverCount = 0;
	/** The total count of collision that occurred this frame. */
	public static int entCollisionCount = 0;
	
	/** The time in millisec it took to update all the entities this frame in the active scene */
	public static int updateTime = 0;
	/** The time in millisec it took to update the physics of all entities this frame in the active scene */
	public static int physicsTime = 0;
	/** The time in millisec it took to perform the collision detection and effects this frame in the active scene */
	public static int collisionTime = 0;
	/** The time in millisec it took to draw(render) the frame */
	public static int drawTime = 0;
	/** The time in millisec remaining before the next frame. */
	public static int waitTime = 0;
	/** The time in millisec it took to compute the frame */
	public static int frameTime = 0;
	/** The frame per second the game could virtually run at. (Should by equal or higher than Main.fps) */
	public static int fps = 0;
	
	/** Resets all the frame local statistics, to be called at the beginning of each frame */
	public static void resetFrameStats(){
		entCount = 0;
		entUpdatedCount = 0;
		entRootCount = 0;
		entNewCount = 0;
		entDestroyedCount = 0;
		entCEmitterCount = 0;
		entCReceiverCount = 0;
		entPhysUpCount = 0;
		entCollisionCount = 0;

		updateTime = 0;
		physicsTime = 0;
		collisionTime = 0;
		drawTime = 0;
		waitTime = 0;
		frameTime = 0;
	}
	/** Reset all the statistics. To be called at the beginning of each game */
	public static void resetGameStats(){
		
	}
	/** Draws the statistics as lcd text on the screen at worldpos pos top left corner */
	public static void drawStats(Vec2 pos){
		Draw.lcd(pos, 0, 8, 			"Ent Count        : "+entCount);
		Draw.lcd(pos.addn(0,-10), 0, 8, "Ent Updated Count: "+entUpdatedCount);
		Draw.lcd(pos.addn(0,-20), 0, 8, "Ent Root Count   : "+entRootCount);
		Draw.lcd(pos.addn(0,-30), 0, 8, "Ent New Count    : "+entNewCount);
		Draw.lcd(pos.addn(0,-40), 0, 8, "Ent Destr Count  : "+entDestroyedCount);
		Draw.lcd(pos.addn(0,-50), 0, 8, "Ent Emit Count   : "+entCEmitterCount);
		Draw.lcd(pos.addn(0,-60), 0, 8, "Ent Recvr Count  : "+entCReceiverCount);
		Draw.lcd(pos.addn(0,-70), 0, 8, "Ent Col Count    : "+entCollisionCount);
		Draw.lcd(pos.addn(0,-80), 0, 8, "Ent PhysUp Count : "+entPhysUpCount);
		
		Draw.lcd(pos.addn(0,-100), 0, 8, "Update Time      : "+updateTime);
		Draw.lcd(pos.addn(0,-110), 0, 8, "physics Time     : "+physicsTime);
		Draw.lcd(pos.addn(0,-120), 0, 8, "collision Time   : "+collisionTime);
		Draw.lcd(pos.addn(0,-130), 0, 8, "draw Time        : "+drawTime);
		Draw.lcd(pos.addn(0,-140), 0, 8, "wait Time        : "+waitTime);
		Draw.lcd(pos.addn(0,-150), 0, 8, "frame Time       : "+frameTime);
		Draw.lcd(pos.addn(0,-160), 0, 8, "fps              : "+fps);
	}
}
