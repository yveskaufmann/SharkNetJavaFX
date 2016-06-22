package net.sharksystem.sharknet.javafx.controls.animations;

import javafx.animation.Transition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.util.Duration;


public class DoublePropertyTransition extends Transition {

	private DoubleProperty property;
	private double startValue;
	private double targetValue;


	public DoublePropertyTransition(Duration duration, DoubleProperty property, double target) {
		setCycleDuration(duration);
		this.property = property;
		this.startValue = property.get();
		this.targetValue = target;
	}

	@Override
	public void play() {
		startValue = property.get();
		super.play();
	}

	@Override
	protected void interpolate(double frac) {
		property.setValue(startValue + frac * (targetValue - startValue));
	}



	public ReadOnlyDoubleProperty transitionProperty() {
		return property;
	}

	public void setTargetValue(double targetValue) {
		this.targetValue = targetValue;
	}
}
