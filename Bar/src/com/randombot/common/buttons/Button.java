package com.randombot.common.buttons;

import com.randombot.common.tweens.ParticleAccessor;
import com.randombot.common.tweens.Tweenable;
import com.randombot.screens.BaseScreen;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Quint;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Button extends Tweenable {

	protected TextureRegion showingTexture;
	protected float w, h, srcX, srcY, srcXRotating, srcYRotating;
	protected static final SpriteBatch sb = BaseScreen.sb;
	protected static final TweenManager tm = BaseScreen.tm;

	// ANIMATION
	protected boolean justTouched;		

	/**
	 * Boton
	 * scaleXY porcentaje(tanto por UNO) para el popup OJO: tanto por uno!
	 */
	public Button(float xStartEntry, float yStartEntry, float xStartExit, 
			float yStartExit, float width, float height, float scaleXY) {		
		super(xStartEntry, yStartEntry, xStartExit, yStartExit, scaleXY);

		this.showingTexture = null;

		this.srcX = width/2;
		this.srcY = height/2;

		this.w = width;
		this.h = height;

		//ANIMATION	
		this.justTouched = false;
	}
	
	public Button(float xStartEntry, float yStartEntry, float xStartExit, 
			float yStartExit, float width, float height, float scaleXY, float srcX, float srcY) {		
		super(xStartEntry, yStartEntry, xStartExit, yStartExit, scaleXY);

		this.showingTexture = null;

		this.srcXRotating = srcX - xStartExit;
		this.srcYRotating = srcY - yStartExit;

		this.srcX = width/2;
		this.srcY = height/2;
		
		this.w = width;
		this.h = height;

		//ANIMATION	
		this.justTouched = false;
	}
	
	
	public void setTexture(TextureRegion texture){
		this.showingTexture = texture;
	}

	public void drawRotating(){
		sb.draw(showingTexture, def.x, def.y, srcXRotating, srcYRotating, w, h, 
				1f, 1f, def.angleORalpha);
	}
	
	/**
	 * Dibuja el boton
	 */
	public void draw(){		
		sb.draw(showingTexture, def.x, def.y, srcX, srcY, w, h, 
				def.scaleXY, def.scaleXY, def.angleORalpha);
	}
	
	/**
	 * Dibuja el boton volteado
	 */
	public void drawFlip(){
		sb.draw(showingTexture, def.x, def.y, srcX, srcY, w, h, 
				-def.scaleXY, def.scaleXY, def.angleORalpha);
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
			.target(scaleXY)
			.ease(Back.IN)
			.start(tm);	

			def.angleORalpha = 0;
			Tween.to(def, ParticleAccessor.ANGLEorALPHA, 0.7f)			
			.target(MathUtils.randomBoolean() ? 360 : -360)
			.ease(Quint.INOUT)
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

	public float getPosHeight(){
		return this.def.y + h;
	}

	public float getPosWidth() {
		return this.def.x + w;
	}

	/**
	 * Inicia la animaci�n de entrada por defecto establecida segun los datos del constructor, con una duraci�n.
	 * 
	 * @param tm puntero al manager de tween.
	 * @param duration �cu�ntos segundos quieres que dure?
	 */
	public void startEntryAnimation(float duration)
	{
		Tween.to(this.def, ParticleAccessor.POSITION_XY, duration).
		target(this.xStartExit,  this.yStartExit).
		ease(Back.OUT).
		start(tm);
	}

	public void startRotatingAnimation(float duration, TweenCallback callback, float angle)
	{
		Tween.to(def, ParticleAccessor.ANGLEorALPHA, duration)			
		.target(angle)
		.setCallback(callback).setCallbackTriggers(TweenCallback.COMPLETE)
		.ease(Back.INOUT)
		.start(tm);
	}
	
	public void startRotatingAnimation(float duration, float angle)
	{
		Tween.to(def, ParticleAccessor.ANGLEorALPHA, duration)			
		.target(angle)
		.ease(Back.INOUT)
		.start(tm);
	}
	
	/**
	 * Inicia la animaci�n de entrada por defecto establecida segun los datos del constructor, con una duraci�n.
	 * 
	 * @param tm puntero al manager de tween.
	 * @param duration �cu�ntos segundos quieres que dure?
	 */
	public void startEntryAnimation(float duration, TweenEquation te)
	{		
		Tween.to(this.def, ParticleAccessor.POSITION_XY, duration).
		target(this.xStartExit,  this.yStartExit).
		ease(te).
		start(tm);
	}

	@Override
	public void startExitAnimation() {
		Tween.to(this.def, ParticleAccessor.POSITION_XY, 0.25f).
		target(this.xStartEntry,  this.yStartEntry).
		start(tm);
	}


	public void startExitAnimation(TweenCallback callback) {
		Tween.to(this.def, ParticleAccessor.POSITION_XY, 0.25f).
		setCallback(callback).setCallbackTriggers(TweenCallback.COMPLETE).
		target(this.xStartEntry,  this.yStartEntry).
		start(tm);
	}

	public void debug() {
		System.out.println("Boton: " + "x -> " + def.x + ", y -> " + def.y);
	}

	
}
