package com.randombot.objects;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquation;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.randombot.common.Assets;
import com.randombot.screens.BaseScreen;
import com.randombot.screens.Play;

public class Drink {
	
	private static final Play play = BaseScreen.game.play;
	
	public static enum DRINKS {	
		CERVEZA, WHISKY, COSMOPOLITAN, GINTONIC;				
		public static int length() { return 3; } 
		public static TextureRegion getTexture(DRINKS type){
			if (type == GINTONIC) return Assets.gintonic;
			if (type == WHISKY) return Assets.whisky;
			if (type == COSMOPOLITAN) return Assets.cosmopolitan;
			return Assets.beer;
		}
		public static DRINKS getRandomDrink(){
			int rand = MathUtils.random(0, 3);
			if (rand == 0) return CERVEZA;
			if (rand == 1) return WHISKY; 
			if (rand == 2) return COSMOPOLITAN;
			return GINTONIC;
		}
	};	
	
	private DRINKS type;	
	private Sprite sprite;
	private static final SpriteBatch sb = BaseScreen.sb;
	private float dx, dy, speed, difLeft, difRight;
	private float leftLimit, rightLimit;
	private float limitY, startY;

	public Drink(float x, float y, float w, float h) {
		this.sprite = new Sprite();
		this.type = DRINKS.getRandomDrink();		

		this.sprite.setRegion(DRINKS.getTexture(type));
		this.sprite.setPosition(x, y);
		this.sprite.setSize(w, h);		
		this.dx = x;
		this.dy = y;		
		
		float screenw = BaseScreen.screenw;
		float screenh = BaseScreen.screenh;
		
		this.leftLimit = screenw * .385f;
		this.rightLimit = screenw * .615f;
		this.limitY = screenh * .52f;
		this.startY = y - screenh * .07f;
		this.speed = 0;	
		difLeft = ((limitY*8)/leftLimit);
		difRight = ((limitY - startY)*8f)/(BaseScreen.screenw-rightLimit);
	}
	
	public void draw() {
		sprite.draw(sb);
	}
	
	public void update(float delta) {
		float y = this.sprite.getY();
		float amount = y/BaseScreen.screenh;
		this.sprite.setScale(1.5f-amount*1.7f);		
		
		float x = sprite.getX();
		float difX = dx - x;
		x += delta * speed * difX;
		
		float difY = dy - y;
		y += delta * speed * difY;
		
		if ((speed > 0 && Math.abs(difX) < 0.5f && Math.abs(difY) < 0.5f) ||
			y < startY || limitY < y || isOutLeft(x, y) || isOutRight(x, y)){
			play.drinkFinished(false);
		} 
		
		sprite.setPosition(x, y);		
	}
	
	//private void l(String s){System.out.println(s);}
	
	public void setPosition(float x, float y){
		sprite.setPosition(x, y);		
	}
	
	public boolean isOutLeft(float x, float y){
		return y > x * difLeft + startY; // y = x * m + h
	}
	
	public boolean isOutRight(float x, float y){
		return  y > (BaseScreen.screenw - x - sprite.getWidth()*sprite.getScaleX()) * 
		(difRight) + startY; // y = x * m + h
	}
	
	public void start(float dx, float dy, float speed){
		this.dx = dx - (sprite.getWidth()*sprite.getScaleX())/2;
		this.dy = dy - (sprite.getHeight()*sprite.getScaleY())/2;
		this.speed = speed;
	}
	
	public boolean isNear(int screenX, int screenY) {
		float x = sprite.getX();
		float y = sprite.getY();
		float w = sprite.getWidth()*sprite.getScaleX();
		float h = sprite.getHeight()*sprite.getScaleY();				
		return (screenX > x - w/2 && screenX < x+w*1.5f && screenY > y - h/2 && screenY < y + h*1.5f);
	}
	
	public float getCenterX(){ 
		return this.sprite.getX() + ((this.sprite.getWidth()*this.sprite.getScaleX())/2); 
	}

	public float getCenterY(){ 
		return this.sprite.getY() + ((this.sprite.getHeight()*this.sprite.getScaleY())/2); 
	}

	public void isInHand(boolean nice) { 
		play.drinkFinished(nice);	
	}

	public void moveTo(float x, float y, TweenEquation te) {
		Tween.to(sprite, 0, 0.385f)
		.target(x, y)
		.ease(te)
		.start(BaseScreen.tm);
	}

	public DRINKS getType() {
		return type;
	}
	
}
