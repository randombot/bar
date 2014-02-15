package com.randombot.common;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Quint;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.randombot.common.tweens.ParticleAccessor;
import com.randombot.common.tweens.Tweenable;
import com.randombot.screens.BaseScreen;

public class Frame extends Tweenable {

	protected TextureRegion showingTexture;
	protected static final SpriteBatch sb = BaseScreen.sb;
	protected static final TweenManager tm = BaseScreen.tm;
	protected static final BitmapFont font = BaseScreen.font;	
	protected float w, h, srcX, srcY, textHeight;
	protected boolean justTouched;	
	protected String text;		
	
	/**
	 * Marco con texto
	 */
	public Frame(float x, float y, float w, float h, String text) {		
		super(x, y, 0, 0, 3f);
		this.text = text;
		this.showingTexture = null;
		this.srcX = w/2;
		this.srcY = h/2;
		this.w = w;
		this.h = h;
		float auxH = font.getWrappedBounds(text, w).height;
		this.textHeight = auxH + (h-auxH)/1.7f;
	}

	/**
	 * Marco SIN texto
	 */
	public Frame(float x, float y, float width, float height) {		
		super(x, y, 0, 0, 3f);
		
		this.showingTexture = null;

		this.srcX = width/2;
		this.srcY = height/2;

		this.w = width;
		this.h = height;
	}
	

	public void setTexture(TextureRegion texture){
		this.showingTexture = texture;
	}

	/**
	 * Dibuja el boton
	 */
	public void drawTexture(){
		sb.setColor(1f, 1f, 1f, super.def.angleORalpha);
		sb.draw(showingTexture, def.x, def.y, srcX, srcY, w, h, 
				def.scaleXY, def.scaleXY, 0);
		sb.setColor(1f, 1f, 1f, 1f);
	}
	
	/**
	 * Dibuja el boton
	 */
	public void drawTextureWithoutColor(){
		sb.draw(showingTexture, def.x, def.y, srcX, srcY, w, h, 
				def.scaleXY, def.scaleXY, 0);
	}
	
	/**
	 * Dibuja el boton con texto
	 */
	public void drawAll(){
		sb.setColor(1f, 1f, 1f, super.def.angleORalpha);
		sb.draw(showingTexture, def.x, def.y, srcX, srcY, w, h, 
				def.scaleXY, def.scaleXY, 0);
		//font.drawCenterLines(text, drawingPosition.x, drawingPosition.y, w, h, drawingPosition.scaleXY);
		font.drawWrapped(sb, text, def.x, def.y+textHeight, w, HAlignment.CENTER);
		sb.setColor(1f, 1f, 1f, 1f);
	}
	
	/**
	 * Dibuja texto
	 */
	public void drawText(){
		sb.setColor(1f, 1f, 1f, super.def.angleORalpha);
		//font.drawCenterLines(text, drawingPosition.x, drawingPosition.y, w, 0, drawingPosition.scaleXY);	
		font.drawWrapped(sb, text, def.x, def.y+textHeight, w, HAlignment.CENTER);
		sb.setColor(1f, 1f, 1f, 1f);
	}
	
	public void setText(String text){
		this.text = text;
		float auxH = font.getWrappedBounds(text, w).height;
		this.textHeight = auxH + (h-auxH)/1.7f;
	}

	@Override
	public void startExitAnimation() {
		Tween.to(this.def, ParticleAccessor.SCALE_XY, 0.25f).
		target(3f).
		start(tm);
		Tween.to(this.def, ParticleAccessor.ANGLEorALPHA, 0.25f).
		target(0f).
		start(tm);
	}

	@Override
	public void startEntryAnimation(float duration)
	{		
		Tween.to(this.def, ParticleAccessor.SCALE_XY, duration).
		target(1f).
		ease(Back.OUT).
		start(tm);
		Tween.to(this.def, ParticleAccessor.ANGLEorALPHA, duration).
		target(1f).
		ease(Quint.OUT).
		start(tm);
	}
	
	public void startEntryAndExitAnimation(float halfduration)
	{		
		Tween.to(this.def, ParticleAccessor.SCALE_XY, halfduration).
		target(1f).
		ease(Back.OUT).
		start(tm);
		Tween.to(this.def, ParticleAccessor.ANGLEorALPHA, halfduration).
		target(1f).
		ease(Quint.OUT).
		start(tm);
		
		Tween.to(this.def, ParticleAccessor.SCALE_XY, 0.25f).
		delay(halfduration).
		target(3f).
		start(tm);
		Tween.to(this.def, ParticleAccessor.ANGLEorALPHA, 0.25f).
		delay(halfduration).
		target(0f).
		start(tm);
	}
	
	public void startExitAndEntryAnimation(float halfduration)
	{	
		
		Tween.to(this.def, ParticleAccessor.SCALE_XY, 0.25f).
		target(3f).
		start(tm);
		Tween.to(this.def, ParticleAccessor.ANGLEorALPHA, 0.25f).
		target(0f).
		start(tm);
		
		Tween.to(this.def, ParticleAccessor.SCALE_XY, halfduration).
		delay(0.25f).
		target(1f).
		ease(Back.OUT).
		start(tm);
		Tween.to(this.def, ParticleAccessor.ANGLEorALPHA, halfduration).
		delay(.25f).
		target(1f).
		ease(Quint.OUT).
		start(tm);
	}
	
	public void startEntryAnimation(float duration, TweenEquation te)
	{		
		Tween.to(this.def, ParticleAccessor.SCALE_XY, duration).
		target(1f).
		ease(te).
		start(tm);
		Tween.to(this.def, ParticleAccessor.ANGLEorALPHA, duration).
		target(1f).
		ease(te).
		start(tm);
	}

	public void startExitAnimation(TweenCallback callback) {		
		Tween.to(this.def, ParticleAccessor.SCALE_XY, 0.25f).
		setCallback(callback).setCallbackTriggers(TweenCallback.COMPLETE).
		target(3f).
		start(tm);
		Tween.to(this.def, ParticleAccessor.ANGLEorALPHA, 0.25f).
		target(0f).
		start(tm);
	}
	
	public void moveTo(float px, float py){
		Tween.to(this.def, ParticleAccessor.POSITION_XY, 1f).
		ease(TweenEquations.easeOutBack).
		target(px - w/2, py - h/2).
		start(tm);		
	}
	
	/**
	 * Comprueba si el getX y el getYY esta en el rectangulo
	 * @return verdad si input est� dentro
	 */
	public void justTouched(float px, float py) {
		if (!this.justTouched && ((def.x <= px) && (def.x + w >= px) && 
								  (def.y <= py) && (def.y + h >= py))) {

			this.justTouched = true;

			Tween.to(def, ParticleAccessor.SCALE_XY, 0.2f)
			.target(scaleXY/4)
			.ease(Back.IN)
			.start(tm);	
		}
	}

	public boolean justReleased(float px, float py) {
		if (this.justTouched) {
			this.justTouched = false;

			Tween.to(def, ParticleAccessor.SCALE_XY, 0.2f)
			.target(1)
			.ease(Back.OUT)
			.start(tm);		
			
			return ((def.x <= px) && (def.x + w >= px) && 
					(def.y <= py) && (def.y + h >= py));
		}	
		return false;
	}
	
	public void setLevel(int num){
		this.text = "NIVEL " + num;
		float auxH = font.getWrappedBounds(text, w).height;
		this.textHeight = auxH + (h-auxH)/1.7f;
	}
	
	public void setDefaultAngleScale(){
		this.def.angleORalpha = 1f;
		this.def.scaleXY = 1;		
	}
	
	public void circularLevelAnimation(){
		Timeline.createSequence()
		.push(Tween.set(def, ParticleAccessor.POSITION_XY).target(-w, def.y))
		.push(Tween.to(def, ParticleAccessor.POSITION_XY, 0.75f).target((BaseScreen.screenw - w)/2f, def.y))
		.pushPause(2f)
		.push(Tween.to(def, ParticleAccessor.POSITION_XY, 0.75f).target(BaseScreen.screenw, def.y))
		.start(tm);	
	}
}
