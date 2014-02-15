package com.randombot.common.tweens;

public interface ITweenAccesor<T> {
	/**
     * Gets one or many values from the target object associated to the
     * given tween type. It is used by the Tween Engine to determine starting
     * values.
     *
     * @param target The target object of the tween.
     * @param tweenType An integer representing the tween type.
     * @param returnValues An array which should be modified by this method.
     * @return The count of modified slots from the returnValues array.
     */
    public int getValues(T target, int tweenType, float[] returnValues);

    /**
     * This method is called by the Tween Engine each time a running tween
     * associated with the current target object has been updated.
     *
     * @param target The target object of the tween.
     * @param tweenType An integer representing the tween type.
     * @param newValues The new values determined by the Tween Engine.
     */
    public void setValues(T target, int tweenType, float[] newValues);

}
