package net.sharksystem.sharknet.javafx.animations;

import javafx.animation.Transition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.util.Duration;


public class DoublePropertyTransition extends Transition {

	private double startValue;
	private double endValue;
	private DoubleProperty property;

	public DoublePropertyTransition(Duration duration, DoubleProperty property) {
		setCycleDuration(duration);
		this.property = property;
		this.startValue = 0.0;
		this.endValue = 1.0;
	}

	@Override
	protected void interpolate(double frac) {
		property.setValue(startValue + frac * (endValue - startValue));
	}

	public ReadOnlyDoubleProperty transitionProperty() {
		return property;
	}

	public double getStartValue() {
		return startValue;
	}

	public void setStartValue(double startValue) {
		this.startValue = startValue;
	}

	public double getEndValue() {
		return endValue;
	}

	public void setEndValue(double endValue) {
		this.endValue = endValue;
	}
}
