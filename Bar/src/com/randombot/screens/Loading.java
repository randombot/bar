package com.randombot.screens;

import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static com.randombot.common.Assets.*;

import com.randombot.bar.Bar;
import com.randombot.common.tweens.Particle;
import com.randombot.common.tweens.ParticleAccessor;
import com.randombot.objects.Quad;

public class Loading extends BaseScreen {

	private AssetManager am;
	private Particle drawingPosition;
	private Quad barraLoading, progresoLoading;
	private float barraX, barraY, barraW, barraH, progress;

	@Override
	public void create() {
		Quad.load();
		this.barraLoading = Quad.getHealthBar();
		this.progresoLoading = Quad.getProgressLoading();

		float halfh = screenh/2;
		float halfw = screenw/2;

		this.barraW = halfw * 1.5f;
		this.barraH = halfh / 6f;
		this.barraX = halfw - this.barraW/2;
		this.barraY = halfh - this.barraH/2f;
		this.drawingPosition = new Particle(-barraW, 0, 0, 0);

		this.progress = 0.05f;

		this.am = new AssetManager();
	}

	@Override
	public void show() {
		Tween.to(drawingPosition, ParticleAccessor.POSITION_XY, .5f).target(barraX, 0).start(tm);

		am.load("data/screens/play/play.atlas", TextureAtlas.class);	
		am.load("data/screens/background/bg.atlas", TextureAtlas.class);
		am.load("data/font/arial.fnt", BitmapFont.class);

		/*load music/sound here*/
	}

	@Override
	public void hide() {
		Tween.to(drawingPosition, ParticleAccessor.POSITION_XY, .25f).target(screenw, 0).start(tm);	
	}

	@Override
	public void render(float delta) {			
		if (am.update()){
			/*init music/sound here*/

			setTextures();

			game.play.create();

			game.setScreen(game.play);
		}

		progress = am.getProgress();	

		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void draw() {
		sb.setColor(0f, progress, 1f, 1f);
		barraLoading.draw(sb, drawingPosition.x, barraY, barraW, barraH);
		sb.setColor(1f-progress, 1f, 0f, 1f);
		progresoLoading.draw(sb, drawingPosition.x, barraY, barraW*progress, barraH);
	}	

	private void setTextures(){
		font = am.get("data/font/arial.fnt", BitmapFont.class);

		BaseScreen.font.setScale(Bar.changeResolution(.575f));	
		BaseScreen.font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		TextureAtlas atlas = am.get("data/screens/play/play.atlas", TextureAtlas.class);

		beer = atlas.findRegion("drinks/cerveza");
		cosmopolitan = atlas.findRegion("drinks/cosmopolitan");
		whisky = atlas.findRegion("drinks/whisky");
		gintonic = atlas.findRegion("drinks/gintonic");

		cross = atlas.findRegion("people/cross");
		tick = atlas.findRegion("people/tick");
		borrachoFlip = atlas.findRegion("people/borracho");
		borracho = new TextureRegion(borrachoFlip);
		borracho.flip(true, false);
		rubiaFlip = atlas.findRegion("people/rubia");
		rubia = new TextureRegion(rubiaFlip);
		rubia.flip(true, false);
		cachas = atlas.findRegion("people/cachas");
		cachasFlip = new TextureRegion(cachas);
		cachasFlip.flip(true, false);
		abuelo = atlas.findRegion("people/abuelo");
		abueloFlip = new TextureRegion(abuelo);
		abueloFlip.flip(true, false);

		camarero = new TextureRegion[2];
		camarero[0] = atlas.findRegion("people/cam0");
		camarero[1] = atlas.findRegion("people/cam1");
		abueloMano = atlas.findRegion("people/abueloMano");
		abueloManoFlip = new TextureRegion(abueloMano); abueloManoFlip.flip(true, false);
		rubiaManoFlip = atlas.findRegion("people/rubiaMano");
		rubiaMano = new TextureRegion(rubiaManoFlip); rubiaMano.flip(true, false);
		cachasMano = atlas.findRegion("people/cachasMano");
		cachasManoFlip = new TextureRegion(cachasMano); cachasManoFlip.flip(true, false);
		borrachoManoFlip = atlas.findRegion("people/borrachoMano");
		borrachoMano = new TextureRegion(borrachoManoFlip); borrachoMano.flip(true, false);


		cod1 = atlas.findRegion("cod1");
		cod2 = atlas.findRegion("cod2");
		teachingFrame = atlas.findRegion("teachingFrame");
		box = atlas.findRegion("box"); // pauseBox before
		pauseBox = atlas.findRegion("box");
		numBox = atlas.findRegion("numBox");
		play = atlas.findRegion("play");
		playGreen = atlas.findRegion("playGreen");
		info = atlas.findRegion("info");
		restart = atlas.findRegion("restart");
		musicOn = atlas.findRegion("musicOn");
		musicOff = atlas.findRegion("musicOff");
		funbares1 = atlas.findRegion("funbares1");
		funbares2 = atlas.findRegion("funbares2");
		funbares3 = atlas.findRegion("funbares3");
		facebook = atlas.findRegion("facebook");
		twitter = atlas.findRegion("twitter");

		atlas = am.get("data/screens/background/bg.atlas", TextureAtlas.class);

		bg = atlas.findRegion("bg");
		table = atlas.findRegion("table");
	}

	public void dispose(){ 
		Quad.dispose();
		this.am.dispose(); 
	}
}
