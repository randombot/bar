package com.randombot.bar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class BarAndroid extends AndroidApplication implements IActionResolver {
	
	@Override 
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useImmersiveMode = true;
		cfg.hideStatusBar = true;
		cfg.useGL20 = true;
		cfg.useAccelerometer = false;
		cfg.useCompass = false;
		cfg.useGLSurfaceViewAPI18 = false;
		cfg.useWakelock = false;

		initialize(new Bar(this), cfg);		
	}

	@Override
	public void openUri(String uri) {
		Uri myUri = Uri.parse(uri);
		Intent intent = new Intent(Intent.ACTION_VIEW, myUri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		startActivity(intent);
	}

	@Override
	public void showDecisionBox(final String alertBoxTitle, final String alertBoxQuestion,
			final String answerA, final String answerB, final AnswerListener ql,
			final int question) {
		handler.post(new Runnable() {
			public void run() {
				new AlertDialog.Builder(BarAndroid.this)
				.setTitle(alertBoxTitle)
				.setMessage(alertBoxQuestion)
				.setPositiveButton(answerA, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						ql.onReceiveAnswer(question, 1);
						dialog.cancel();
					}
				})
				.setNegativeButton(answerB, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						ql.onReceiveAnswer(question, 2);
						dialog.cancel();
					}
				})
				.setCancelable(false)
				.create()
				.show();
			}
		});
	}
}