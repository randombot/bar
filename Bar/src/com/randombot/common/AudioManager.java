package com.randombot.common;

import java.util.TreeMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.randombot.screens.BaseScreen;

public class AudioManager {	

	public static final String M_MOONLIGHTHALL = "data/music/MoonlightHall.ogg";
	
	public static final String S_DB1 = "data/sound/db1.ogg"; // 0
	public static final String S_DB2 = "data/sound/db2.ogg"; // 1
	public static final String S_DEAD1 = "data/sound/dead1.ogg"; // 2
	public static final String S_DEAD2 = "data/sound/dead2.ogg"; // 3
	
	public static final String S_GAMEOVER  = "data/sound/gameover.ogg";  // 4
	public static final String S_LEVELUP = "data/sound/levelup.ogg"; // 5
	public static final String S_MAGIC1 = "data/sound/magic1.ogg"; // 6
	public static final String S_SB1 = "data/sound/sb1.ogg"; // 7
	public static final String S_SB2 = "data/sound/sb2.ogg"; // 8
	public static final String S_WB1 = "data/sound/wb1.ogg"; // 9
	public static final String S_WB2 = "data/sound/wb2.ogg"; // 10
	public static final String S_MAGIC2 = "data/sound/magic2.ogg"; // 11
	public static final String S_BONUS1 = "data/sound/bonus1.ogg"; // 12
	public static final String S_BONUS2 = "data/sound/bonus2.ogg"; // 13
	
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
				System.out.println("PLAY DE : " + musicPlaying);
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
