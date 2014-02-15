package com.randombot.common;

import java.util.TreeMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.randombot.screens.BaseScreen;

public class AudioManager {	

	public static final String M_MOONLIGHTHALL = "data/music/MoonlightHall.ogg";
	
	public static final String S_DB1 = "SOUND_PATH_HERE"; // 13
	
	private TreeMap<String, Music> musica;	
	private TreeMap<String, Sound> sonidos;	

	private boolean sound, music;
	private String musicPlaying;
	private Music temporalMusic;

	public AudioManager() {			

		if (!BaseScreen.settings.getBoolean("music")){
			this.music = true;	
		} else {
			this.music = false;	
		}

		if (!BaseScreen.settings.getBoolean("sound")){
			this.sound = true;	
		} else {
			this.sound = false;	
		}

		BaseScreen.settings.putBoolean("music", !music);		
		BaseScreen.settings.putBoolean("sound", !sound);		
		BaseScreen.settings.flush();

		this.musica = new TreeMap<String, Music>();
		this.sonidos = new TreeMap<String, Sound>();
	}

	public void postLoading(AssetManager am, String ... dir){
		this.musicPlaying = dir[0];
		this.musica.put(musicPlaying, am.get(musicPlaying, Music.class));
		for (int i = 1; i < dir.length; i++){
			this.sonidos.put(dir[i], am.get(dir[i], Sound.class));
		}
	}

	public void save(){
		BaseScreen.settings.putBoolean("music", !music);		
		BaseScreen.settings.putBoolean("sound", !sound);		
	}

	public void load(AssetManager am, String ... dir){
		String music = dir[0];
		if (music != null){
			if (!am.isLoaded(music, Music.class)){
				am.load(music, Music.class);
				musicPlaying = music;
			}
		}
		String sound = "";
		for (int i = 1; i < dir.length; i++){
			sound = dir[i];
			if (!am.isLoaded(sound, Sound.class)){
				am.load(sound, Sound.class);
			}
		}
	}

	public void unload(AssetManager am){
		this.temporalMusic = null;
		try {	
			while (!this.musica.isEmpty()){
				String first = this.musica.firstKey();
				this.musica.get(first).stop();
				am.unload(first);
				this.musica.remove(first);
			}	
			while (!this.sonidos.isEmpty()){
				String first = this.sonidos.firstKey();
				this.sonidos.get(first).stop();			
				am.unload(first);
				this.sonidos.remove(first);
			}
		} catch (Exception e){
			
		}
	}

	public boolean isSoundActivated(){
		return this.sound;
	}

	public boolean isMusicActivated(){
		return this.music;
	}

	/**
	 * Sirve para mutear/desmutear la musica
	 */
	public void changeMusicState(){		
		if (music){
			music = false;
			if (temporalMusic != null){
				try {	
					temporalMusic.stop();
				} catch (Exception e){
					
				}
			}
		} else {
			music = true;
			try {
				Gdx.app.log("Music", "PLAY DE : " + musicPlaying);
				temporalMusic = musica.get(musicPlaying);
				temporalMusic.play();
				temporalMusic.setLooping(true);		
			} catch (Exception e){
				
			}
		}
	}

	/**
	 * Sirve para mutear/desmutear los sonidos
	 */
	public void changeSoundState(){
		this.sound = !this.sound;
	}

	/**
	 * @param num
	 */
	public void playSound(String sound){
		if (this.sound){
			try {
				Sound s = sonidos.get(sound);
				if (s != null){
					s.play();				
				}
			} catch (Exception e){
				
			}
		}
	}

	/**
	 * @param num
	 */
	public void playMusic(String song){	
		if (music){			
			if (temporalMusic != null){
				try {
					if (!musicPlaying.equalsIgnoreCase(song)){
						musicPlaying = song;			
						temporalMusic.stop();
						temporalMusic = musica.get(song);
						temporalMusic.play();
						temporalMusic.setLooping(true);
					}	
				} catch (Exception e){}
			} else {
				try {
					temporalMusic = musica.get(song);
					temporalMusic.play();
					temporalMusic.setLooping(true);
					musicPlaying = song;
				} catch (Exception e){}
			}
		}
	}

	public void forceStopMusic(){
		if (music && this.temporalMusic != null){
			try {
				this.temporalMusic.stop();
			} catch (Exception e) {
				
			}
		}
	}

	public void forcePlayMusic(){
		if (music && this.temporalMusic != null){
			try {
				this.temporalMusic.play();
			} catch (Exception e) {
				
			}
		}
	}

}
