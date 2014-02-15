package com.randombot.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;


public class TransitionScreen extends BaseScreen{

	private BaseScreen actualScreen, nextScreen;

	private float alpha;
	private boolean backTrack;

	public TransitionScreen() 
	{
		actualScreen = game.showingScreen;
	}
	
	public BaseScreen initializer(BaseScreen nextScreen)
	{
		this.actualScreen.hide();		
		this.nextScreen = nextScreen;
		this.alpha = 1;
		this.backTrack = false;
		return this;
	}	

	@Override
	public void render(float delta) 
	{		
		if(this.backTrack) 
		{		
			if(this.alpha >= 1)
			{
				game.showingScreen = this.nextScreen;
			} else {
				this.alpha = this.alpha + 4*delta;
			}
		} 
		else
		{
			if(alpha <= 0)
			{
				this.nextScreen.show();
				this.backTrack = true;
				this.actualScreen = this.nextScreen;
			} else {
				this.alpha = this.alpha - 4*delta;	// 4 = alphaSpeed ,time t =~ 0.25, 1/0.25 =~ 4
			}
		}

		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}
	
	@Override
	public void draw() { this.actualScreen.draw(); }
}
