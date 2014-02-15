package com.randombot.screens;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Quad;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.randombot.bar.Bar;
import com.randombot.common.Assets;
import com.randombot.common.Frame;
import com.randombot.common.buttons.Button;
import com.randombot.common.buttons.DoubleButton;
import com.randombot.objects.Drink;
import com.randombot.objects.DrinkQueue;
import com.randombot.objects.Person;

public class Play extends BaseScreen implements TextInputListener{

	/**LOGIC**/
	private final int INITIAL_MAX_POINTS = 10, INITIAL_SECONDS = 25, OFFER_SECONDS = 15*60, TICKET_SECONDS = 24*3600;
	private int MAX_POINTS = INITIAL_MAX_POINTS;
	private final String LAST_SECONDS = "0x013455BF", TICKET = "0x00495110", 
							TICKET_TIME = "0.004CDF8E", OFFER = "0.0042AE0F", 
							OFFER_TIME = "0.0041BE4E", COD = "FunBEV",
							FACEBOOK_URL = "https://www.facebook.com/Elviajerocafe",
							TWITTER_URL = "https://twitter.com/el_viajero_cafe";
	private boolean paused, teaching, hasTicket, hasOffer;
	private float speed, minspeed, speedStep, timeDelta, screenXBefore, screenYBefore;
	private int seconds, points, teachingStep;
	private boolean drinkTouched, drinkMove;

	/**OBJECTS**/
	private TextureRegion bg;
	private DoubleButton music;
	private Button resumeFromPause, info, restart, resumeFromTeachingAndTicket, facebook, twitter;
	private Frame pausedBox, teachingAndTicketBox, table, time, score, ticketOfferTime;
	private List<Person> people;
	private Drink drink;
	private float drinkW, drinkH, deltaBarman, barmanX, barmanY, barmanW, barmanH;
	private int barmanInt, level;
	private final int MAX_LEVELS = 3;
	private DrinkQueue dq;
	private Frame levelGraph;

	@Override
	public void create() {

		/**LOGIC**/
		float halfScreenW = screenw/2f, halfScreenH = screenh/2f;
		// 0x00495110 == tiene un ticket ya asignado.
		hasTicket = settings.getBoolean(TICKET, false);
		paused = false;

		people = new ArrayList<Person>();	

		/**BG**/
		bg = Assets.bg; 
		table = new Frame(0, 0, screenw, screenh);
		table.setTexture(Assets.table);

		/**PAUSE**/
		float frameW = screenw*.9f, frameH = screenh*.7f;
		float frameX = halfScreenW - frameW/2f, frameY = halfScreenH - frameH/2f;

		float buttonWH = Bar.changeResolution(55f);

		pausedBox = new Frame(frameX, frameY, frameW, frameH);
		pausedBox.setTexture(Assets.pauseBox);

		float offsetY = frameH/3f, offsetX = frameW/8f;
		float halfButtonWH = buttonWH/2F;
		float yStartExit = frameY+offsetY*2f - halfButtonWH;

		info = new Button(-buttonWH, 0, frameX+offsetX*2 - halfButtonWH, frameY + offsetY - halfButtonWH, buttonWH, buttonWH, 0.6f);
		info.setTexture(Assets.info);
		resumeFromPause = new Button(screenw, 0, frameX+offsetX*6 - halfButtonWH, frameY + offsetY - halfButtonWH, buttonWH, buttonWH, 0.6f);
		resumeFromPause.setTexture(Assets.play);
		restart = new Button(-buttonWH, screenh, frameX+offsetX*2 - halfButtonWH, yStartExit, buttonWH, buttonWH, 0.5f);
		restart.setTexture(Assets.restart);
		music = new DoubleButton(screenw, screenh, frameX+offsetX*6 - halfButtonWH, yStartExit, buttonWH, buttonWH, 0.5f);
		music.setTexturesDoubleButton(Assets.musicOn, Assets.musicOff, audio.isMusicActivated());
		

		facebook = new Button(-buttonWH, 0, frameX+offsetX*2 - halfButtonWH, frameY + offsetY - halfButtonWH, buttonWH, buttonWH, 0.6f);
		facebook.setTexture(Assets.facebook);
		twitter = new Button(screenw, 0, frameX+offsetX*6 - halfButtonWH, frameY + offsetY - halfButtonWH, buttonWH, buttonWH, 0.6f);
		twitter.setTexture(Assets.twitter);
		
		/**BARMAN**/
		this.deltaBarman = 0f;
		this.barmanInt = 0;
		this.barmanW = buttonWH*.9f;
		this.barmanH = buttonWH*2.3f;
		this.barmanX = screenw - this.barmanW*.8f;
		this.barmanY = 0f;

		/**TEACHING**/
		teachingAndTicketBox = new Frame(frameX, frameY, frameW, frameH);
		resumeFromTeachingAndTicket = new Button(-buttonWH, -buttonWH, 0, 0, buttonWH, buttonWH, 0.6f);
		resumeFromTeachingAndTicket.setTexture(Assets.playGreen);

		/** DRINK **/
		drinkW = Bar.changeResolution(30f);
		drinkH = Bar.changeResolution(47f);		
		dq = new DrinkQueue(screenw*.5f-drinkW*.5f, screenh*.18f, drinkW, drinkH);
		drink = dq.next();

		/**TIMER AND SCORE**/
		levelGraph = new Frame(-screenw, screenh/2f, screenw*.6f, screenw*.23f, "NIVEL 1"); levelGraph.startEntryAnimation(.5f);
		levelGraph.setTexture(Assets.box);
		float auxH = screenh*.1f, auxW = screenw*.55f;
		float offset = auxH*.2f;
		ticketOfferTime = new Frame((screenw-auxW)/2f, offset, auxW, auxH);
		ticketOfferTime.setTexture(Assets.numBox);
		 auxW = screenw*.45f;
		time = new Frame(offset, screenh-2*auxH-offset, auxW, auxH);
		time.setTexture(Assets.numBox);

		score = new Frame(screenw-auxW - offset, screenh-2*auxH-offset, auxW, auxH);
		score.setTexture(Assets.numBox);

		if(!hasTicket){
			if(settings.getBoolean("teach", true)){
				settings.putBoolean("teach", false);
				startTeaching();
			}
		} else {

			hasOffer = settings.getBoolean(OFFER, false);
			long lastSecs = settings.getLong(LAST_SECONDS, 0), elapsed = 0;
			if(lastSecs > 0){
				Date d = new Date();
				elapsed = (d.getTime() - lastSecs)/1000;
			}
			if(hasOffer){
				int sec = settings.getInteger(OFFER_TIME, OFFER_SECONDS);
				sec -= elapsed;
				startOffer(sec < 0 ? 0 : sec);
			} else {
				int sec = settings.getInteger(TICKET_TIME, TICKET_SECONDS);
				sec -= elapsed;
				startHasTicket(sec < 0 ? 0 : sec);
			}
		}
		level = 1;
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);	
		generatePersons();
		if(!teaching && !hasTicket)levelGraph.circularLevelAnimation();

		restartTouch();

		table.startEntryAnimation(.5f, Quad.OUT);

		if(!hasOffer && !hasTicket)seconds = INITIAL_SECONDS;		
		timeDelta = 0f;		
		time.setText(String.valueOf(seconds));
		time.startEntryAnimation(1f);

		points = 0;
		score.setText("0");
		score.startEntryAnimation(2f);
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
		table.startExitAnimation();
		time.startExitAnimation();
		score.startExitAnimation();
	}

	@Override
	public void render(float delta) {
		if(!paused && !teaching && !hasTicket){

			int size = people.size();
			for (int i = 0; i < size; ++i){	
				if (people.get(i).justTouched(drink)){
					break;
				}				
			}

			if(seconds > 0){
				timeDelta+=delta;
				if(timeDelta>= 1f){
					timeDelta = 0f;
					--seconds;
					time.setText(String.valueOf(seconds));
				}
			} else {
				game.setScreen(this);
				level = 1;
				levelGraph.setLevel(level);
				MAX_POINTS = INITIAL_MAX_POINTS;
			}
		} else if (teaching || hasTicket) {
			this.deltaBarman += delta;
			if(this.deltaBarman > .25f){
				this.deltaBarman = 0f;
				++this.barmanInt;
				this.barmanInt %=2;
			}
			if(hasTicket || hasOffer){
				if(seconds > 0){
					timeDelta+=delta;
					if(timeDelta>= 1f){
						timeDelta = 0f;
						--seconds;
						if(hasOffer){
							ticketOfferTime.setText(sec2minSec(seconds));
						} else {
							ticketOfferTime.setText(sec2hourMin(seconds));
						}
					}
				} else {
					restartFromOfferOrTicket();
				}
			}
		} 
		drink.update(delta);
	}

	private void restartFromOfferOrTicket(){
		game.setScreen(this);
		level = 1;
		levelGraph.setLevel(level);
		MAX_POINTS = INITIAL_MAX_POINTS;
		ticketOfferTime.startExitAnimation();
		settings.putBoolean(TICKET, false);
		settings.putBoolean(OFFER, false);
		settings.flush();
		hasOffer = hasTicket = false;
	}

	@Override
	public void draw() {
		sb.disableBlending();
		sb.draw(bg, 0, 0, screenw, screenh);
		sb.enableBlending();
		int i, initial = people.size()-1;
		table.drawTexture();
		for(i = initial; i >= 0; --i) people.get(i).draw();
		dq.draw();
		drink.draw();
		levelGraph.drawAll();
		if (paused) {
			pausedBox.drawTexture();
			resumeFromPause.draw();
			restart.draw();
			music.draw();
			info.draw();
		} else if (teaching || hasTicket) {
			teachingAndTicketBox.drawTexture();
			resumeFromTeachingAndTicket.draw();
			facebook.draw();
			twitter.draw();
			sb.draw(Assets.camarero[this.barmanInt], this.barmanX, this.barmanY, this.barmanW, this.barmanH);
			if(hasTicket) ticketOfferTime.drawAll();

		} else {
			time.drawAll();
			score.drawAll();
		}
	}

	@Override
	public void pause() {
		if(!hasTicket && !hasOffer && !teaching){
			paused = true;
			teaching = false;
			pausedBox.startEntryAnimation(0.25f);
			resumeFromPause.startEntryAnimation(0.5f);
			info.startEntryAnimation(0.5f);
			music.startEntryAnimation(0.5f);
			restart.startEntryAnimation(0.5f);
		}
		if(hasTicket ){
			if(hasOffer){
				settings.putBoolean(OFFER, true);
				settings.putInteger(OFFER_TIME, seconds);
			} else {
				settings.putBoolean(TICKET, true);
				settings.putInteger(TICKET_TIME, seconds);				
			}
			settings.putLong(LAST_SECONDS, new Date().getTime());
			settings.flush();
		}
	}

	@Override
	public void resume() {
		if(hasTicket){
			long lastSecs = settings.getLong(LAST_SECONDS, 0);
			if(lastSecs > 0){
				Date d = new Date();
				long elapsed = (d.getTime() - lastSecs)/1000;
				seconds -= elapsed;
				if(seconds < 0) seconds = 0;
			} 
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		if (paused || hasTicket) {
			if (keycode == Keys.BACKSPACE || keycode == Keys.BACK){
				game.tryToClose();
			}
		} else if (!teaching) {
			if (keycode == Keys.BACKSPACE || keycode == Keys.BACK){
				pause();
			}
		}

		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (pointer == 0) {
			screenY = (int) (screenh - screenY);
			if (paused) {
				this.music.justTouched(screenX, screenY);
				this.restart.justTouched(screenX, screenY);
				this.resumeFromPause.justTouched(screenX, screenY);
				this.info.justTouched(screenX, screenY);
			} else if (teaching) {
				this.resumeFromTeachingAndTicket.justTouched(screenX, screenY);
				this.facebook.justTouched(screenX, screenY);
				this.twitter.justTouched(screenX, screenY);
			} else if (hasTicket) {
				this.resumeFromTeachingAndTicket.justTouched(screenX, screenY);
			} else if (!this.drinkTouched && this.drink.isNear(screenX, screenY)) { 
				this.drinkTouched = true;
				this.screenXBefore = screenX;
				this.screenYBefore = screenY;
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (pointer == 0) {
			screenY = (int) (screenh - screenY);
			if (paused) {
				if(this.music.justReleased(screenX, screenY)){
					audio.changeMusicState();					
				}else if(this.restart.justReleased(screenX, screenY)){
					justLeftPause();
					game.setScreen(game.play);	
					level = 1;		
					levelGraph.setLevel(level);
					MAX_POINTS = INITIAL_MAX_POINTS;		
				}else if(this.resumeFromPause.justReleased(screenX, screenY)){
					justLeftPause();
				}else if(this.info.justReleased(screenX, screenY)){
					justLeftPause();
					startTeaching();
				}

			} else if (teaching) {
				if (this.resumeFromTeachingAndTicket.justReleased(screenX, screenY)){
					if(teachingStep == 0){
						teachingAndTicketBox.startExitAndEntryAnimation(0.3f);
						teachingAndTicketBox.setTexture(Assets.funbares2);
						facebook.startExitAnimation();
						twitter.startExitAnimation();
						teachingStep = 1;
					} else if (teachingStep == 1){
						teachingAndTicketBox.startExitAndEntryAnimation(0.3f);
						teachingAndTicketBox.setTexture(Assets.funbares3);
						teachingStep = 2;					
					} else {
						this.teachingAndTicketBox.startExitAnimation();
						this.resumeFromTeachingAndTicket.startExitAnimation(teachingCallBack);
					}
				} else if (teachingStep == 0){
					if(this.facebook.justReleased(screenX, screenY)){
						game.mostrarURL(FACEBOOK_URL);	
					} else if(this.twitter.justReleased(screenX, screenY)){
						game.mostrarURL(TWITTER_URL);							
					}
				}
				
			} else if (hasTicket) {
				if (this.resumeFromTeachingAndTicket.justReleased(screenX, screenY)){
					if(hasOffer){
						restartFromOfferOrTicket();
					} else {
						Gdx.input.getTextInput(this, "Has ganado!\nCanjea tu código para recibir\ntu recompensa.", "Código");
					}
				}
			} else if (!drinkMove && drinkTouched){
				this.drink.start(screenX, screenY, speed);
				this.drinkMove = true;
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (pointer == 0) {
			//System.out.println("drag");
			screenY = (int) (screenh - screenY);
			/*if (paused) {

			} else if (teaching) {

			} else {*/
			if (drinkTouched && !drinkMove) { 


				float difX = Math.abs(screenXBefore - screenX);
				float difY = Math.abs(screenYBefore - screenY);

				screenXBefore = screenX;
				screenYBefore = screenY;

				if (difX <= 0.15f && difY <= 0.15f){
					restartTouch();
				} else {	
					this.speed -= speedStep; 
					if (this.speed < this.minspeed || screenY > screenh*0.55f){
						this.drink.start(screenX, screenY, minspeed);
						this.drinkMove = true;
					}
					speedStep += speedStep;
				}
			}
			//}
		}
		return false;
	}

	public void drinkFinished(boolean nice){
		if (nice){
			++points;
			if(points == MAX_POINTS){
				if(level == MAX_LEVELS){
					settings.putBoolean(TICKET, true);
					startHasTicket(TICKET_SECONDS);
				} else {
					++level;
					levelGraph.setLevel(level);
					game.setScreen(game.play);
					MAX_POINTS += 5;
				}
			}
		} else {
			if(points > 0) --points;		
		}
		score.setText(String.valueOf(points));
		restartTouch();
		this.drink = dq.next();
	}

	private void generatePersons(){
		for(Person p:people)
			p.startExitAnimation();
		people.clear();
		Person.borracho = Person.abuelo = Person.rubia = Person.cachas = false;

		boolean IZQUIERDA = false, DERECHA = true, FILA1 = true, FILA2 = false;

		//Fila mÃ¡s cercana, primera
		//float personW=screenw/2.5f, personH=screenh/2.5f;

		people.add(new Person(FILA1, IZQUIERDA));//Iz
		people.add(new Person(FILA1, DERECHA));//Dr
		//Segunda fila
		//personW *= .7f; personH *= .7f;
		people.add(new Person(FILA2, IZQUIERDA));//Iz
		people.add(new Person(FILA2, DERECHA));//Dr		
		for (Person p:people)
			p.startEntryAnimation(1f);
	}

	private void justLeftPause(){
		this.pausedBox.startExitAnimation();
		this.music.startExitAnimation();
		this.restart.startExitAnimation();
		this.info.startExitAnimation();
		this.resumeFromPause.startExitAnimation(pauseCallBack);		
	}

	private void startTeaching(){
		teaching = true;
		teachingStep = 0;
		teachingAndTicketBox.setTexture(Assets.funbares1);
		teachingAndTicketBox.startEntryAnimation(.7f);
		facebook.startEntryAnimation(1f);
		twitter.startEntryAnimation(1f);
		resumeFromTeachingAndTicket.startEntryAnimation(.9f);
	}

	private void startHasTicket(int sec){
		hasTicket = true;
		Gdx.input.getTextInput(this, "Has ganado!\nCanjea tu código.", "Código");
		seconds = sec;
		ticketOfferTime.setText(sec2hourMin(seconds));
		ticketOfferTime.startEntryAnimation(1f);
		teachingAndTicketBox.setTexture(Assets.cod1);
		teachingAndTicketBox.setText("");
		teachingAndTicketBox.startEntryAnimation(1f);
		resumeFromTeachingAndTicket.startEntryAnimation(1f);
	}

	private void startOffer(int sec){
		hasOffer = true;
		seconds = sec;
		ticketOfferTime.setText(sec2minSec(seconds));
		ticketOfferTime.startEntryAnimation(1f);
		teachingAndTicketBox.startEntryAnimation(1f);
		teachingAndTicketBox.setTexture(Assets.cod2);
		teachingAndTicketBox.setText("");
		resumeFromTeachingAndTicket.startEntryAnimation(1f);
	}

	private void restartTouch(){
		this.speed = 10*(screenh/854f);
		this.minspeed = speed * 0.6f;
		this.speedStep = screenh*0.00001f;
		this.drinkTouched = false;
		this.drinkMove = false;
	}	

	/**CALLBACKS**/
	private final TweenCallback teachingCallBack = new TweenCallback() {
		public void onEvent(int type, BaseTween<?> source) {
			teaching = false;
		}
	};

	private final TweenCallback pauseCallBack = new TweenCallback() {
		public void onEvent(int type, BaseTween<?> source) {
			paused = false;
		}
	};

	@Override
	public void canceled() {
		System.out.println("Cancelado ticket...");
		//Gdx.input.getTextInput(this, "Has ganado!\nCanjea tu código para recibir tu recompensa.", "código");		
	}

	@Override
	public void input(String cod) {
		if(hasTicket){
			System.out.println(cod);	
			if(cod.equals(COD)){
				//Irse a la pantalla de la oferta....
				System.out.println("irse a la pantalla de la oferta...");
				startOffer(OFFER_SECONDS);
			} else {
				Gdx.input.getTextInput(this, "Introduce un código correcto.", "Nuevo código.");
			}
		}
	}

	private String sec2minSec(int sec){ 
		int segundos = sec%60;
		if(segundos < 10) return sec/60+":0"+segundos;
		else return sec/60+":"+segundos;
	}

	private String sec2hourMin(int sec){ 
		if(sec < 60){
			return sec2minSec(sec);
		}
		int min = (sec%3600)/60;
		if(min < 10) return sec/3600+":0"+min+"m";
		else return sec/3600+":"+min+"m";
	}
}
