package com.randombot.bar;

public interface IActionResolver {
	public void openUri(String uri);
	public void showDecisionBox(final String alertBoxTitle, final String alertBoxQuestion, 
			final String answerA, final String answerB, final AnswerListener ql, final int questionNumber);
}
