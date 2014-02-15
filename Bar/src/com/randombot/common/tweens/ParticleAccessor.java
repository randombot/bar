package com.randombot.common.tweens;

import aurelienribon.tweenengine.TweenAccessor;

//We implement TweenAccessor<Particle>. Note the use of a generic.

public class ParticleAccessor implements TweenAccessor<Particle> {

	public static final int POSITION_XY = 0;
	public static final int SCALE_XY = 1;
	/**
	 * es una OR, no ambos a la vez de manera precisa
	 */
	public static final int ANGLEorALPHA = 2;

	@Override
	public int getValues(Particle target, int tweenType, float[] returnValues) {
		if(tweenType == 0){//POSITION_XY
			returnValues[0] = target.x;
			returnValues[1] = target.y;
			return 2;
		} else if(tweenType == 1) {//SCALE_XY
			returnValues[0] = target.scaleXY;
			return 1;
		} else {//ANGLE OR ALPHA
			returnValues[0] = target.angleORalpha;
			return 1;
		}
	}

	@Override
	public void setValues(Particle target, int tweenType, float[] newValues) {
		if(tweenType == 0){//POSITION_XY
			target.x = newValues[0];
			target.y = newValues[1];
		} else if(tweenType == 1) {//SCALE_XY
			target.scaleXY = newValues[0];
		} else {//ANGLE OR ALPHA
			target.angleORalpha = newValues[0];
		}
	}
}
