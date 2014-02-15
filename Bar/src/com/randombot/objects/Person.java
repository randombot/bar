package com.randombot.objects;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Expo;
import aurelienribon.tweenengine.equations.Quint;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.randombot.common.Assets;
import com.randombot.common.Frame;
import com.randombot.common.tweens.ParticleAccessor;
import com.randombot.common.tweens.Tweenable;
import com.randombot.screens.BaseScreen;

public class Person extends Tweenable{

	private static final SpriteBatch sb = BaseScreen.sb;
	private static final TweenManager tm = BaseScreen.tm;
	private static final float screenw = BaseScreen.screenw;
	private static final float screenh = BaseScreen.screenh;
	
	public static boolean borracho, abuelo, rubia, cachas;

	public static enum PEOPLE {
		BORRACHO, ABUELO, RUBIA, CACHAS;
		public static TextureRegion getTexture(PEOPLE type){
			if (type == BORRACHO) return Assets.borracho;
			if (type == ABUELO) return Assets.abuelo;
			if (type == RUBIA) return Assets.rubia;
			return Assets.cachas;
		}
		public static TextureRegion getFlippedTexture(PEOPLE type){
			if (type == BORRACHO) return Assets.borrachoFlip;
			if (type == ABUELO) return Assets.abueloFlip;
			if (type == RUBIA) return Assets.rubiaFlip;
			return Assets.cachasFlip;
		}
		public static TextureRegion getHandTexture(PEOPLE type){
			if (type == BORRACHO) return Assets.borrachoMano;
			if (type == ABUELO) return Assets.abueloMano;
			if (type == RUBIA) return Assets.rubiaMano;
			return Assets.cachasMano;
		}
		public static TextureRegion getHandFlippedTexture(PEOPLE type){
			if (type == BORRACHO) return Assets.borrachoManoFlip;
			if (type == ABUELO) return Assets.abueloManoFlip;
			if (type == RUBIA) return Assets.rubiaManoFlip;
			return Assets.cachasManoFlip;
		}
		public static PEOPLE getRandomPerson(){
			if (!borracho && !abuelo && !rubia && !cachas){
				int rand = MathUtils.random(0, 3);
				if (rand == 0){ cachas = true; return CACHAS; }
				if (rand == 1){ abuelo = true; return ABUELO; }
				if (rand == 2){ rubia = true; return RUBIA; }
				borracho = true; return BORRACHO; 
			}			
			
			if (MathUtils.randomBoolean()){
				if (!rubia){ rubia = true; return RUBIA; }			
				if (!borracho){ borracho = true; return BORRACHO; }
			} else {
				if (!borracho){ borracho = true; return BORRACHO; }
				if (!rubia){ rubia = true; return RUBIA; }			
			}
			
			if (MathUtils.randomBoolean()){
				if (!abuelo){ abuelo = true; return ABUELO;  }			
				if (!cachas){ cachas = true; return CACHAS;  }
			} else {	
				if (!cachas){ cachas = true; return CACHAS;  }
				if (!abuelo){ abuelo = true; return ABUELO;  }						
			}
			
			return MathUtils.randomBoolean() ? MathUtils.randomBoolean() ? BORRACHO : ABUELO : MathUtils.randomBoolean() ? CACHAS : RUBIA;
		}
	}

	private Hand hand;
	private TextureRegion tex;	
	private float w, h;
	private PEOPLE type;
	private Frame cross, tick;

	public Person(boolean firstRow, boolean isRight){
		super(0f, 0f, 0f, 0f, 1f);
		this.type = PEOPLE.getRandomPerson();
		generatePositions(firstRow, isRight);

		float halfw = w/2f, quartw = w/4f, quarth = h/4f;
		this.cross = new Frame(def.x+quartw, def.y+quarth, halfw,halfw);
		this.cross.setTexture(Assets.cross);
		this.tick = new Frame(def.x+quartw, def.y+quarth, halfw,halfw);
		this.tick.setTexture(Assets.tick);
		
	}

	private void generatePositions(boolean firstRow, boolean isRight){
		float personW=screenw/2.5f, personH=screenh/2.5f;
		float x = 0f, y = 0f, handx = 0f, handy = 0f, handw = 0f, handh = 0f;
		TextureRegion handTex = null;	
		if(firstRow){ //PRIMERA FILA
			if(isRight){ //DERECHA
				this.tex = PEOPLE.getTexture(type);	
				handTex = PEOPLE.getHandTexture(type);
				
				if(type == PEOPLE.BORRACHO){
					x = screenw-personW*.91f; y = screenh/6f;
					handw = personW/3f; handh = personH*.22f;
					handx = x - handw*.013f; handy = y+personH*.415f;
					
				} else if(type == PEOPLE.RUBIA){
					x = screenw-personW*1.083f; y = screenh/5.5F;
					handw = personW/2.16f; handh = personH*.16f;
					handx = x - handw*.06f; handy = y+personH*.248f;

				} else if(type == PEOPLE.CACHAS){
					personH*=0.92f;
					x = screenw-personW*.918f; y = screenh/5.3F;
					handw = personW/3f; handh = personH*.22f;
					handx = x + handw*.019f; handy = y+personH*.291f;

				} else if(type == PEOPLE.ABUELO){
					x = screenw-personW*1.08F; y = screenh/5.7F;
					handw = personW/2.22f; handh = personH*.219f;
					handx = x+personW*.031f; handy = y+personH*.317f;

				} 

			} else { //IZQUIERDA
				this.tex = PEOPLE.getFlippedTexture(type);	
				handTex = PEOPLE.getHandFlippedTexture(type);
				
				if(type == PEOPLE.BORRACHO){
					x = -personW*.098f; y = screenh/6f;
					handw = personW/3f; handh = personH*.22f;
					handx = x + personW - handw + handw*.013f; handy = y+personH*.415f;

				} else if(type == PEOPLE.RUBIA){
					x = personW*.068f; y = screenh/5.5F;
					handw = personW/2.16f; handh = personH*.16f;
					handx = x + personW - handw*.94f; handy = y+personH*.248f;

				} else if(type == PEOPLE.CACHAS){
					personH*=0.92f;
					x = -personW*.084F; y = screenh/5.3F;
					handw = personW/3f; handh = personH*.22f;
					handx = x + personW - handw*1.07f; handy = y+personH*.291f;

				} else if(type == PEOPLE.ABUELO){
					x = personW*.06f; y = screenh/5.7F;
					handw = personW/2.22f; handh = personH*.219f;
					handx =x + personW - handw - personW*.031f; handy = y+personH*.317f;

				} 
			}
		} else { //SEGUNDA FILA
			personW *= .7f; personH *= .7f;
			
			if(isRight){ //DERECHA
				this.tex = PEOPLE.getTexture(type);	
				handTex = PEOPLE.getHandTexture(type);
				
				if(type == PEOPLE.BORRACHO){
					x = screenw-personW*1.495f; y = screenh/2.75f;
					handw = personW/3f; handh = personH*.22f;
					handx = x - handw*.01f; handy = y+personH*.413f;

				} else if(type == PEOPLE.RUBIA){
					x = screenw-personW*1.735f; y = screenh/2.5f;
					handw = personW/2.16f; handh = personH*.16f;
					handx = x; handy = y+personH*.242f;

				} else if(type == PEOPLE.CACHAS){
					personH*=0.92f;
					x = screenw-personW*1.525f; y = screenh/2.5f;
					handw = personW/3f; handh = personH*.22f;
					handx = x + handw*.039f; handy = y+personH*.29f;

				} else if(type == PEOPLE.ABUELO){
					x = screenw-personW*1.696f; y = screenh/2.55f;
					handw = personW/2.22f; handh = personH*.219f;
					handx = x+personW*.031f; handy = y+personH*.317f;

				} 
			} else { //IZQUIERDA
				this.tex = PEOPLE.getFlippedTexture(type);	
				handTex = PEOPLE.getHandFlippedTexture(type);	
				
				if(type == PEOPLE.BORRACHO){
					x = personW*0.51f;y = screenh/2.75f;
					handw = personW/3f; handh = personH*.22f;
					handx = x + personW - handw + handw*.024f; handy = y+personH*.413f;

				} else if(type == PEOPLE.RUBIA){
					x = personW*0.758f;y = screenh/2.5f;
					handw = personW/2.16f; handh = personH*.16f;
					handx = x + personW - handw; handy = y+personH*.242f;

				} else if(type == PEOPLE.CACHAS){
					personH*=0.92f;
					x = personW*0.58f;y = screenh/2.5f;
					handw = personW/3f; handh = personH*.22f;
					handx = x + personW - handw*1.07f; handy = y+personH*.29f;

				} else if(type == PEOPLE.ABUELO){
					x = personW*0.74f;y = screenh/2.55f;
					handw = personW/2.22f; handh = personH*.219f;
					handx =x + personW - handw - personW*.031f; handy = y+personH*.317f;

				} 
			}
		}

		this.w = personW; this.h = personH;
		
		this.def.angleORalpha = 0f;
		this.def.x = xStartEntry = xStartExit = x;
		this.def.y = yStartEntry = yStartExit = y;
		this.hand = new Hand(handx, handy, handw, handh, handTex);
	}

	public void setTexture(TextureRegion personTexture, TextureRegion handTexture){
		this.tex = personTexture;
		this.hand.setTexture(handTexture);
	}

	public void draw(){
		sb.setColor(1f, 1f, 1f, this.def.angleORalpha);
		sb.draw(tex, this.def.x, this.def.y, this.w, this.h);
		sb.setColor(1f, 1f, 1f, 1f);
		hand.draw();
		this.cross.drawTexture();
		this.tick.drawTexture();
	}


	public boolean justTouched(Drink d){
		if (this.hand.justTouched(d.getCenterX(), d.getCenterY())) {

			if (type.ordinal() == d.getType().ordinal()){
				this.tick.startEntryAndExitAnimation(0.25f);
				d.isInHand(true);
			} else {
				this.cross.startEntryAndExitAnimation(0.25f);
				d.isInHand(false);
			}			
			return true;
		}
		return false;
	}

	/**
	 * Inicia la animaci�n de entrada por defecto establecida segun los datos del constructor, con una duraci�n.
	 * 
	 * @param tm puntero al manager de tween.
	 * @param duration �cu�ntos segundos quieres que dure?
	 */
	public void startEntryAnimation(float duration)
	{
		Tween.to(this.def, ParticleAccessor.ANGLEorALPHA, duration).
		target(1f).
		ease(Quint.OUT).
		start(tm);
		this.hand.startEntryAnimation(duration*0.7f);
	}

	@Override
	public void startExitAnimation() {
		Tween.to(this.def, ParticleAccessor.ANGLEorALPHA, 0.25f).
		target(0f).
		start(tm);
		this.hand.startExitAnimation();
	}

	private class Hand extends Tweenable{
		protected TextureRegion showingTexture;
		protected float w, h, srcX, srcY;

		/**
		 * Mano
		 * scaleXY porcentaje(tanto por UNO) para el popup OJO: tanto por uno!
		 */
		public Hand(float x, float y,float width, float height, TextureRegion st) {		
			super(x, y, 0f, 0f, 0.7f);

			this.showingTexture = st;

			this.srcX = width/2;
			this.srcY = height/2;

			this.w = width;
			this.h = height;
		}		

		public void setTexture(TextureRegion texture){
			this.showingTexture = texture;
		}

		/**
		 * Dibuja la mano
		 */
		public void draw(){		
			sb.setColor(1f, 1f, 1f, this.def.angleORalpha);
			sb.draw(this.showingTexture, this.def.x, this.def.y, this.srcX, this.srcY, this.w, this.h, 
					this.def.scaleXY, this.def.scaleXY, 0f);
			sb.setColor(1f, 1f, 1f, 1f);
		}

		/**
		 * Comprueba si el getX y el getYY esta en el rectangulo
		 * @return verdad si input est� dentro
		 */
		public boolean justTouched(float px, float py) {
			if (((this.def.x < px) && (this.def.x + w > px) && 
					(this.def.y < py) && (this.def.y + h > py))) {	

				Tween.to(this.def, ParticleAccessor.SCALE_XY, 0.2f)
				.target(scaleXY)
				.ease(Back.IN)
				.start(tm);	
				Tween.to(this.def, ParticleAccessor.SCALE_XY, 0.2f)
				.target(1f)
				.delay(0.2f)
				.ease(Back.OUT)
				.start(tm);	
				return true;
			}
			return false;
		}

		/**
		 * Inicia la animaci�n de entrada por defecto establecida segun los datos del constructor, con una duraci�n.
		 * 
		 * @param duration �cu�ntos segundos quieres que dure?
		 */
		public void startEntryAnimation(float duration)
		{
			Tween.to(this.def, ParticleAccessor.ANGLEorALPHA, duration).
			target(1f).
			ease(Expo.OUT).
			start(tm);
		}

		@Override
		public void startExitAnimation() {
			Tween.to(this.def, ParticleAccessor.ANGLEorALPHA, 0.25f).
			target(0f).
			start(tm);
		}	
	}
}
