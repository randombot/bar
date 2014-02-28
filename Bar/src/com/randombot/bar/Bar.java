package com.randombot.bar;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.randombot.common.AudioManager;
import com.randombot.common.tweens.Particle;
import com.randombot.common.tweens.ParticleAccessor;
import com.randombot.common.tweens.SpriteAccessor;
import com.randombot.screens.BaseScreen;
import com.randombot.screens.Loading;
import com.randombot.screens.Play;
import com.randombot.screens.TransitionScreen;

public class Bar implements ApplicationListener, AnswerListener{
	
	/** SCREENS **/
	private TransitionScreen transitionScreen;
	
	
	public BaseScreen showingScreen;

	private Loading loading;
	public Play play;
	
	/** UTILES **/
	private IActionResolver ar;
	private boolean close; 
	private float delta;
	
	public Bar(IActionResolver ar){
		this.ar = ar;
	}
	
	@Override
	public void create() {
		this.close = false;
		
		BaseScreen.game = this;

		BaseScreen.sb = new SpriteBatch(200);	

		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		Tween.registerAccessor(Particle.class, new ParticleAccessor());
		BaseScreen.tm = new TweenManager();
		
		BaseScreen.settings = Gdx.app.getPreferences("rb_bar");		

		BaseScreen.audio = new AudioManager();
		
		this.loading = new Loading();
		this.play = new Play();

		this.showingScreen = loading;
		this.showingScreen.create();
		this.showingScreen.show();	
		
		this.transitionScreen = new TransitionScreen();			

		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void render() {
		delta = Gdx.graphics.getDeltaTime();
		
		this.showingScreen.render(delta);	
		BaseScreen.tm.update(delta);
		
		BaseScreen.sb.begin();
		this.showingScreen.draw();	
		/* FPS */// font.drawString(graphics.getFramesPerSecond() + "", 0, graphics.getHeight()/4);
		BaseScreen.sb.end();
		
		//System.out.println("Java Heap: " + Gdx.app.getJavaHeap() + ", Natives Heap: " + Gdx.app.getNativeHeap());
		//System.out.println(sb.renderCalls + ", " + sb.maxSpritesInBatch);
	}	

	@Override
	public void dispose() {
		BaseScreen.audio.save();
		this.loading.dispose();
		BaseScreen.font.dispose();
		BaseScreen.sb.dispose();			
		System.exit(0);
	}

	@Override
	public void resize(int w, int h) {}
	
	@Override
	public void pause() {
		this.showingScreen.pause();
	}
	
	@Override
	public void resume() {
		this.showingScreen.resume();
	}
	
	@Override
	public void onReceiveAnswer(int question, int answer) {
		if (question == 0){
			if (answer == 1){
				Gdx.app.exit();
			} else {
				this.close = false;		
			}	
		}
	}
	
	public void mostrarURL(String string) { this.ar.openUri(string); }
	
	public void tryToClose() {
		if (!this.close){
			this.close = true;
			this.ar.showDecisionBox("Salir", "¿Seguro?", "Sí", "No", this, 0);					
		}
	}
	
	public void setScreen(BaseScreen nextScreen){
		this.showingScreen = this.transitionScreen.initializer(nextScreen);		
	}

	/**
	 * Funcion que adapta tamaï¿½o de una figura a las resoluciones que normalmente usamos.
	 * 
	 * @param width_height - ancho o alto de la figura.
	 * @return en el tamaï¿½o segun la resoluciï¿½n.
	 */
	public static float changeResolution(float width_height){
		float aux = (width_height * Gdx.graphics.getHeight() + width_height * Gdx.graphics.getWidth());	
		return   aux * 0.000201f + aux * 0.0017f;
	}
}
