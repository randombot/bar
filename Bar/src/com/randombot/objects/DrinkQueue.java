package com.randombot.objects;

import java.util.ArrayList;
import java.util.List;

import aurelienribon.tweenengine.TweenEquations;

import com.randombot.screens.BaseScreen;

public class DrinkQueue {
	
	private static final int MAX_DRINKS = 4;
	private static final float OFFSETX = BaseScreen.screenw/(MAX_DRINKS+1);
	
	private float x, y, w, h;
	private List<Drink> drinks;
	
	public DrinkQueue(float x, float y, float w, float h){
		this.x = x; this.y = y; this.w = w; this.h = h;
		this.drinks = new ArrayList<Drink>();
		for(int i = 0; i < MAX_DRINKS; ++i) this.addRandomDrink(i);
	}
	
	public void draw(){
		int size = this.drinks.size(), i;
		for(i = 0; i < size; ++i){
			this.drinks.get(i).draw();
		}		
	}
	
	public Drink next(){
		int indexToRemove = 0;
		
		Drink ret = this.drinks.get(indexToRemove);
		ret.moveTo(this.x, this.y, TweenEquations.easeOutBack);
		
		int size = this.drinks.size()-1, i;
		for (i = 0; i < size; ++i){
			this.drinks.set(i, this.drinks.get(i+1));
		}
		this.drinks.remove(size);
		
		this.addRandomDrink(size);
		
		for (i = 0; i < MAX_DRINKS; ++i){
			this.drinks.get(i).moveTo((MAX_DRINKS-i)*OFFSETX - w/2, 0, TweenEquations.easeOutSine);
		}		
		
		return ret;
	}
	
	private void addRandomDrink(int i){
		Drink d = new Drink(-this.w, 0, this.w, this.h);
		d.moveTo((MAX_DRINKS-i)*OFFSETX - w/2, 0, TweenEquations.easeOutSine);
		this.drinks.add(i, d);
	}
}
