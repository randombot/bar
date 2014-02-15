package com.randombot.screens;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.randombot.bar.Bar;
import com.randombot.common.AudioManager;

public class BaseScreen extends InputAdapter {

	public static Bar game;
	public static SpriteBatch sb;
	public static BitmapFont font;	
	public static TweenManager tm;
	public static Preferences settings;
	public static AudioManager audio;
	public static final float screenw = Gdx.graphics.getWidth();
	public static final float screenh = Gdx.graphics.getHeight();
	
	/**
	 * Se llama en AppListener con getDeltaTime().
	 * @param delta getDeltaTime()
	 */
	public void render(float delta){ }
	
	/**
	 * Se llama siempre al final del render, y lo primero de hace es sb.Begin().
	 * @param delta getDeltaTime()
	 */
	public void draw() { }
	
	/**
	 * Se entra 1 vez al hacer setScreen(..) y se ponene los punteros apuntando a sus direcciones.
	 */
	public void create() { }
	
	/**
	 * Se entra 1 vez al hacer setScreen(..) y se ponene los punteros apuntando a sus direcciones.
	 */
	public void show() { }
	
	/**
	 * Aqu� se realizan las animaciones que quieres que hagan los objetos al SALIR en el Screen.
	 * Es necesario que los objetos a los que se les anima dispongan de Particle.
	 * 
	 * @param tm Se pasa un puntero desde TransitionScreen.
	 */
	public void hide() { }	
	
	/**
	 * Se utiliza para dejar de entrar en el render, puede interesar si es juego con movimientos.
	 */
	public void pause() { }

	/**
	 * Para resumir el boleano que se ha seteado en pause() previamente.
	 */
	public void resume() { }	
	
}
