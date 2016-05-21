package net.sharksystem.sharknet.javafx.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import net.sharksystem.sharknet.javafx.i18n.I18N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ResourceBundle;

/**
 * A Controller for a specific view.
 * A view is a fxml file which is load and initialized by this controller.
 * The controller is responsible to manage the specified view.
 */
public abstract class AbstractController {

	/**
	 * The logger for this class
	 */
	private Logger Log = LoggerFactory.getLogger(AbstractController.class);

	/**
	 * The FXML view/file which is related to this controller
	 */
	private URL fxmlFile;

	/**
	 * The current local Resource Bundle.
	 * The {@link ResourceBundle} contains all locale-specific
	 * texts and strings.
	 */
	private ResourceBundle resourceBundle;

	/**
	 * The root node of the controlled view.
	 */
	protected Parent root;

	/**
	 * Creates a controller for the view which is defined
	 * in the fxml file at the specified location.
     */
	public AbstractController(URL location) {
		this(location, I18N.getResourceBundle());
	}

	/**
	 * <b>This constructor is only for internal use</b>
	 *
	 * Creates a controller for the view which is defined
	 * in the fxml file at the specified location. And specifies
	 * the {code ResourceBundle} which should be used by the controlled
	 * view.
	 *
	 * @param location
	 * @param resources
     */
	protected AbstractController(URL location, ResourceBundle resources) {
		this.fxmlFile = location;
		this.resourceBundle = resources;
	}

	/**
	 * Retrieves the root element of the controlled view.
	 *
	 * @return the root element
     */
	public Parent getRoot() {
		if (this.root == null) {
			loadFXML();
		}
		return this.root;
	}

	/**
	 * Defines the parent node of the controlled view.
	 *
	 * @param root the parent node of the controlled view.
     */
	protected final void setRoot(Parent root) {
		assert this.root == null;
		this.root = root;
	}

	/**
	 * Load the corresponding fxml file which contains the
	 * view of this controller.
	 */
	protected void loadFXML() {
		FXMLLoader loader = new FXMLLoader();
		loader.setController(this);
		loader.setLocation(fxmlFile);
		loader.setResources(resourceBundle);
		loader.setCharset(Charset.forName("UTF-8"));
		try {
			setRoot(loader.load());
			onFxmlLoaded();
		} catch (IOException e) {
			Log.debug("Loader.getController : " + loader.getController());
			Log.debug("Loader.getLocation : " + loader.getLocation());
			throw new RuntimeException("Failed to load " + fxmlFile.getFile(), e);
		}
	}

	/**
	 * A subclass must implement its view initial logic in this method.
	 *
	 * <p>
	 * Fields which are annotated with {@link javafx.fxml.FXML} are first accessible
	 * while this method is invoked by the {@link AbstractController}.
	 * </p>
	 *
	 * </p>
	 * Called when the fxml file was loaded this is the
	 * equivalent to {@link javafx.fxml.Initializable#initialize(URL, ResourceBundle)}.
	 * <p>
	 */
	abstract protected void onFxmlLoaded();

	/**
	 * @return the assigned resource bundle which
	 * is used for the controlled view.
	 * The {@link ResourceBundle} contains all locale-specific
	 * texts and strings.
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	/**
	 * @return the location of the corresponding fxml file.
	 * This contains the view which is controlled by
	 * this controller.
	 */
	public URL getLocation() {
		return fxmlFile;
	}
}
