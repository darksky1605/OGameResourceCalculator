package darksky.ogameresourcecalculator;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class
 * 
 * @author ogame.de Dark Sky
 * 
 */

public class GUIApplication extends Application {

	private final String fxmlResourcePath = "/darksky/ogameresourcecalculator/fxml/";

	private final String cssResourcePath = "/darksky/ogameresourcecalculator/fxml/";

	private final String localizationPath = "localization/";

	@Override
	public void start(Stage primaryStage) {

		final Parameters params = getParameters();
		final String defaultLanguage = "de";

		try {
			final FXMLLoader fxmlLoader = new FXMLLoader();

			String lang = params.getNamed().getOrDefault("lang", defaultLanguage);

			File file = new File(localizationPath);
			URL[] urls = { file.toURI().toURL() };
			ClassLoader loader = new URLClassLoader(urls);

			final ResourceBundle defaultBundle = ResourceBundle.getBundle("gui",
					new Locale(defaultLanguage, Locale.getDefault().getCountry()), loader);
			try {
				final ResourceBundle bundle = ResourceBundle.getBundle("gui",
						new Locale(lang, Locale.getDefault().getCountry()), loader);
				if (bundle != null)
					fxmlLoader.setResources(bundle);
				else
					fxmlLoader.setResources(defaultBundle);
			} catch (MissingResourceException e) {
				fxmlLoader.setResources(defaultBundle);
			}			

			Parent root = fxmlLoader.load(getClass().getResource(fxmlResourcePath + "MainWindow.fxml").openStream());
			Scene scene = new Scene(root, 1000, 500);
			scene.getStylesheets().add(getClass().getResource(cssResourcePath + "MainWindow.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("OGame Resource Calculator (Dark Sky)");

			ExecutorService calculatorService = Executors.newFixedThreadPool(1);
			MainWindowController c = fxmlLoader.getController();
			c.executor = calculatorService;
			String logparam = params.getNamed().getOrDefault("log", "");
			if(logparam.equals("all"))
				c.setLoggerLevel(Level.ALL);

			primaryStage.setOnCloseRequest((windowEvent) -> {
				calculatorService.shutdownNow();
				try {
					calculatorService.awaitTermination(3, TimeUnit.SECONDS);
				} catch (Exception e) {
					e.printStackTrace();
				}

			});
			primaryStage.show();

		} catch (Exception e) {
			throw new RuntimeException("Could not load main window");
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
