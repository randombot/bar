package com.randombot.common.buttons;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Quint;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.randombot.common.tweens.ParticleAccessor;

public class DoubleButton extends Button {

	protected TextureRegion texture1, texture2;
	protected boolean activated;
	
	public DoubleButton(float xStartEntry, float yStartEntry, float xStartExit, float yStartExit, 
			float width, float height, float percent) {
		super(xStartEntry, yStartEntry, xStartExit, yStartExit, width, height, percent);
		this.texture1 = null;
		this.texture2 = null;
		this.activated = false;
	}
	
	public DoubleButton(float xStartEntry, float yStartEntry, float xStartExit, float yStartExit, 
			float width, float height, float percent, float srcX, float srcY, float startAngle) {
		super(xStartEntry, yStartEntry, xStartExit, yStartExit, width, height, percent, srcX, srcY);
		this.texture1 = null;
		this.texture2 = null;
		this.activated = false;
		this.def.angleORalpha = startAngle;
	}
	
	public void setAngle(float angle){
		this.def.angleORalpha = angle;		
	}
	
	/**
	 * Comprueba si el getX y el getYY esta en el rectangulo
	 * @return verdad si input est� dentro
	 */
	public void justTouched(float px, float py) {				
		if(!this.justTouched){
			if (((def.x <= px) && (def.x + w >= px) && 
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
	}
	
	@Override
	public boolean justReleased(float px, float py) {				
		if (this.justTouched) {
			this.justTouched = false;

			Tween.to(def, ParticleAccessor.SCALE_XY, 0.2f)
			.target(1)
			.ease(Back.OUT)
			.start(tm);			
			if ((def.x <= px) && (def.x + w >= px) && 
					(def.y <= py) && (def.y + h >= py)) {
				setStatus(!activated);
				return true;
			}			
		}	
		return false;
	}
	
	public void setTexturesDoubleButton(TextureRegion texture, TextureRegion texture2, boolean activated){
		this.texture1 = texture;
		this.texture2 = texture2;
		if (activated){
			this.showingTexture = texture;
		} else {
			this.showingTexture = texture2;			
		}
		this.activated = activated;
	}
	
	public void setStatus(boolean activated){
		if (activated){
			this.showingTexture = texture1;
		} else {
			this.showingTexture = texture2;			
		}
		this.activated = activated;
	}	
}
