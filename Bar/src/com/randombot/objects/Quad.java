package com.randombot.objects;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.randombot.bar.Bar;

public class Quad extends NinePatch {

	private static Quad instance1, instance2, instance3;
	private static TextureAtlas atlas;

	public static void load(){
		atlas = new TextureAtlas("data/ninepatch/ninepatch.atlas");
	}

	private Quad(int num){
		super(atlas.findRegion("" + num), 4, 4, 4, 4);  
	}   

	private Quad(boolean progreso){
		super(atlas.findRegion("1"), 39, 39, 39, 39);
		float npWH = Bar.changeResolution(2f);          	
		setBottomHeight(npWH);
		setLeftWidth(npWH);
		setRightWidth(npWH);
		setTopHeight(npWH);
	}  

	public static Quad getHealthBar(){
		if(instance3 == null){
			instance3 = new Quad(true);
		}
		return instance3;
	}

	public static Quad getProgressLoading() {
		if(instance2 == null){
			instance2 = new Quad(2);
		}
		return instance2;
	}

	public static Quad getProgressLoadingBG() {
		if(instance1 == null){
			instance1 = new Quad(3);
		}
		return instance1;
	}
	
	public static void dispose(){
		atlas.dispose();
	}
}