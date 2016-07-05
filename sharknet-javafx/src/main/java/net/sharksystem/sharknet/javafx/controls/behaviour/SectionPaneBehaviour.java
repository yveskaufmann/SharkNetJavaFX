package net.sharksystem.sharknet.javafx.controls.behaviour;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import net.sharksystem.sharknet.javafx.controls.SectionPane;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Yves Kaufmann
 * @since 03.07.2016
 */
public class SectionPaneBehaviour extends BehaviorBase<SectionPane> {

	private SectionPane sectionPane;

	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/

	public SectionPaneBehaviour(SectionPane pane) {
		super(pane, KEY_BINDINGS);
		this.sectionPane = pane;
	}

	/******************************************************************************
	 *
	 * Key Press Handling
	 *
	 ******************************************************************************/

	private static final List<KeyBinding> KEY_BINDINGS = new ArrayList<>();
	private static final String PRESS_ACTION = "SPACE";

	static {
		KEY_BINDINGS.add(new KeyBinding(KeyCode.SPACE, PRESS_ACTION));
	}

	@Override
	protected void callAction(String action) {
		switch (action) {
			case PRESS_ACTION: {
				if (sectionPane.isCollapsible() && sectionPane.isFocused()) {
					sectionPane.setExpanded(! sectionPane.isExpanded());
					sectionPane.requestFocus();
				}
			} break;
			default:
				super.callAction(action);
		}
	}

	/******************************************************************************
	 *
	 * Mouse event handling
	 *
	 ******************************************************************************/

	@Override
	public void mousePressed(MouseEvent e) {
		e.consume();
		sectionPane.requestFocus();
	}

	public void toggle() {
		sectionPane.setExpanded(! sectionPane.isExpanded());
	}
}
