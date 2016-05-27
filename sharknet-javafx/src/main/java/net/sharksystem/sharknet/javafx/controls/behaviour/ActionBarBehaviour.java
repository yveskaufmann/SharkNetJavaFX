package net.sharksystem.sharknet.javafx.controls.behaviour;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import net.sharksystem.sharknet.javafx.controls.ActionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * @see ActionBar
 */
public class ActionBarBehaviour extends BehaviorBase<ActionBar> {

	/**
	 * Create a new BehaviorBase for the given control. The Control must not
	 * be null.
	 *
	 * @param control     The control. Must not be null.
	 */
	public ActionBarBehaviour(ActionBar control) {
		super(control, ACTIONBAR_BINDINGS);
	}

	protected static final List<KeyBinding> ACTIONBAR_BINDINGS = new ArrayList<KeyBinding>();


}
