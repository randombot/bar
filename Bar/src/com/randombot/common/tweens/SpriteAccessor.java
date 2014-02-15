package com.randombot.common.tweens;

import com.badlogic.gdx.graphics.g2d.Sprite;

import aurelienribon.tweenengine.TweenAccessor;

public class SpriteAccessor implements TweenAccessor<Sprite> {


	@Override
	public int getValues(Sprite target, int tweenType, float[] returnValues) {

		returnValues[0] = target.getX();
		returnValues[1] = target.getY();
		
		return 2;

	}

	@Override
	public void setValues(Sprite target, int tweenType, float[] newValues) {

		target.setPosition(newValues[0], newValues[1]);

	}
}
