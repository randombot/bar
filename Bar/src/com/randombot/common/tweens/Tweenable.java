package com.randombot.common.tweens;

/**
 * Proporciona a cualquier objeto las propiedades de ser animado mediante el TweenAccesor a la clase Particle(x, y) para 
 * hacer animaciones de entrada y salida a un Screen con mayor facilidad y otras cosas...
 *
 */
public abstract class Tweenable {

	protected Particle def;
	protected float   xStartEntry, //X desde donde se inicia la animaci�n de entrada al Screen.
	xStartExit,  //X desde donde se inicia la animaci�n de salida al Screen.
	yStartEntry, //Y desde donde se inicia la animaci�n de entrada al Screen.
	yStartExit,	 //Y desde donde se inicia la animaci�n de entrada al Screen.;
	scaleXY;  //La escala hasta la cual se reducir� la part�cula
	
	public Tweenable(float xStartEntry, float yStartEntry, float xStartExit, float yStartExit, float scaleXY)
	{
		this.xStartEntry = xStartEntry;
		this.yStartEntry = yStartEntry;
		this.xStartExit = xStartExit;
		this.yStartExit = yStartExit;
		this.scaleXY = scaleXY;		

		this.def = new Particle(this.xStartEntry, this.yStartEntry, scaleXY <= 1.0f ? 1.0f : scaleXY, 0f);
	}

	/**
	 * Inicia la animaci�n de entrada por defecto establecida segun los datos del constructor, con una duraci�n.
	 * 
	 * @param duration �cu�ntos segundos quieres que dure?
	 */
	public void startEntryAnimation(float duration){}

	/**
	 * Inicia la animaci�n de salida por defecto establecida segun los datos del constructor, con una duraci�n.
	 * Realiza la animaci�n opuesta a la entrada. No tiene duraci�n porque est� limitada por la impuesta por el Transiti�nScreen 
	 * una vez que se pone todo en negro ya no se ve nada por eso duracion =~ 0.3s.
	 * 
	 * @param tm puntero al manager de tween.
	 */
	public void startExitAnimation(){}

}
