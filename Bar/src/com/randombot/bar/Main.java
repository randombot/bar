package com.randombot.bar;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

public class Main implements IActionResolver{

	private static Main resolver;
	
	public static void main(String args[]){
		
		if(resolver == null){
			resolver = new Main();
		}

		boolean hacer_packer = false; 

		if(hacer_packer){
			
			Settings settings = new Settings();
			settings.pot = false;
			settings.maxHeight = 1024;
			settings.maxWidth = 1024;
			settings.paddingX = 1;
			settings.paddingY = 1;
			settings.filterMin = TextureFilter.Linear;
			settings.filterMag = TextureFilter.Linear;

			TexturePacker.process(settings, "dataPC/screens/play/", "data/screens/play/", "play");			
			
			TexturePacker.process(settings, "dataPC/screens/background/", "data/screens/background", "bg");
		}
		
		new LwjglApplication(new Bar(resolver), "Bar", 480, 854, true);
	}

	@Override
	public void openUri(String uri) {
		Gdx.app.log("TOAST", "would have opend URL");		
	}

	@Override
	public void showDecisionBox(String alertBoxTitle, String alertBoxQuestion,
			String answerA, String answerB, final AnswerListener ql,
			final int question) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run () {
				int result = JOptionPane.showConfirmDialog(null, "Seguro?", "Salir", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION){
					ql.onReceiveAnswer(question, 1);
				} else if (result == JOptionPane.NO_OPTION){
					ql.onReceiveAnswer(question, 2);
				} 	
			}
		});		
	}
}
