package com.randombot.common.buttons;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Quint;

import com.badlogic.gdx.math.MathUtils;
import com.randombot.common.tweens.ParticleAccessor;

public class ExpansionButton extends DoubleButton {

	private DoubleButton button1, button2;
	private boolean rotating;

	public ExpansionButton(float xStartEntry,
			float yStartEntry, float xStartExit, float yStartExit, float width,
			float height, float percentage) {
		super(xStartEntry, yStartEntry, xStartExit, yStartExit, width, height, percentage);	


		float y1 = yStartExit + 0.2f*height;
		float halfwAux = w/2;
		float w1 = 0.6f*w, h1 = 0.6f*h, halfw = w*0.3f;
		float inioffset = xStartExit + w*.75f;
		float offsetx = 2*w/3;
		float xStartExit1 = inioffset + offsetx - halfw, xStartExit2 =inioffset + 2*offsetx - halfw;

		float srcX = xStartExit+halfwAux,srcY = yStartExit+halfwAux;

		this.rotating = false;

		this.button1 = new DoubleButton(xStartExit1, y1, xStartExit1, y1, w1, h1, 0.7f, srcX, srcY, 180);
		this.button2 = new DoubleButton(xStartExit2, y1, xStartExit2, y1, w1, h1, 0.7f, srcX, srcY, 180);		
	}

	/**
	 * Comprueba si el getX y el getYY esta en el rectangulo
	 * Ojo tiene en cuenta si se han tocado los botones internos que contiene 
	 * @return verdad si input est� dentro
	 */
	public void justTouched(int px, int py) {			
		if( !this.justTouched && !this.rotating){
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

			} else if (activated){
				button1.justTouched(px, py);
				button2.justTouched(px, py);
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

			if ((def.x <= px) && (def.x + w >= px) 
					&& (def.y <= py) && (def.y + h >= py)){

				rotating = true;
				if (activated){
					button1.startRotatingAnimation(1f, 180);
					button2.startRotatingAnimation(1f, rotatingCallBack, 180);
				} else {
					setStatus(true);
					button1.startRotatingAnimation(1f, 360);
					button2.startRotatingAnimation(1f, onlyRotatingCallBack, 360);				
				}		

				return true;
			}
		}	
		return false;
	}

	private final TweenCallback rotatingCallBack = new TweenCallback() {
		public void onEvent(int type, BaseTween<?> source) {
			rotating = false;
			setStatus(false);
		}
	};
	
	private final TweenCallback onlyRotatingCallBack = new TweenCallback() {
		public void onEvent(int type, BaseTween<?> source) {
			rotating = false;
		}
	};

	/**
	 * Dibuja el boton
	 */
	public void draw(){		
		super.draw();
		if (activated) {	
			if(rotating){
				button1.drawRotating();			
				button2.drawRotating();	
			} else {			
				button1.draw();			
				button2.draw();	
			}
		}
	}

	public DoubleButton getButton1() {
		return button1;
	}

	public DoubleButton getButton2() {
		return button2;
	}	

}
