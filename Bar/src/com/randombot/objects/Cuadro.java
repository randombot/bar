package com.randombot.objects;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.randombot.bar.Bar;

public class Cuadro extends NinePatch {

	 private static Cuadro instance1, instance2, instance3;
     private static TextureAtlas atlas;
     
     public static void load(){
     	atlas = new TextureAtlas("data/ninepatch/ninepatch.atlas");
     }
     
     private Cuadro(int num){
     	super(atlas.findRegion("" + num), 4, 4, 4, 4);  
     }   
     
     private Cuadro(boolean progreso){
     	super(atlas.findRegion("1"), 39, 39, 39, 39);
     	float npWH = Bar.changeResolution(2f);          	
     	setBottomHeight(npWH);
 		setLeftWidth(npWH);
 		setRightWidth(npWH);
 		setTopHeight(npWH);
     }  
     
     public static Cuadro getBarraDeVida(){
         if(instance3 == null){
                 instance3 = new Cuadro(true);
         }
         return instance3;
     }
     
		public static Cuadro getProgresoLoading() {
			 if(instance2 == null){
              instance2 = new Cuadro(2);
	         }
	         return instance2;
		}
		
		public static Cuadro getProgresoLoadingBG() {
			 if(instance1 == null){
             instance1 = new Cuadro(3);
	         }
	         return instance1;
		}
}