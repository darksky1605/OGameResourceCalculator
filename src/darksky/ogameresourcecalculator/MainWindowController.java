package darksky.ogameresourcecalculator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.function.UnaryOperator;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import darksky.ogameresourcecalculator.OGameUnit.Type;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.util.Pair;
import javafx.util.StringConverter;

/**
 * GUI controller
 * 
 * @author ogame.de Dark Sky
 *
 */

public class MainWindowController {

	private Logger logger = Logger.getLogger("darksky.ogameresourcecalculator.MainWindowController");

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField prodMetInput;

	@FXML
	private TextField prodKrisInput;

	@FXML
	private TextField prodDeutInput;

	@FXML
	private TextField haveMetInput;

	@FXML
	private TextField haveKrisInput;

	@FXML
	private TextField haveDeutInput;

	@FXML
	private TextField wantMetInput;

	@FXML
	private TextField wantKrisInput;

	@FXML
	private TextField wantDeutInput;

	@FXML
	private Button calcButton;

	@FXML
	private ProgressBar calcProgressBar;

	@FXML
	private TextField rateMMInput;

	@FXML
	private TextField rateMKInput;

	@FXML
	private TextField rateMDInput;

	@FXML
	private TextField rateKMInput;

	@FXML
	private TextField rateKKInput;

	@FXML
	private TextField rateKDInput;

	@FXML
	private TextField rateDMInput;

	@FXML
	private TextField rateDKInput;

	@FXML
	private TextField rateDDInput;

	@FXML
	private TextField dayStepInput;

	@FXML
	private ListView<Profile> profileListView;

	@FXML
	private Button createProfileButton;

	@FXML
	private Button deleteProfileButton;

	@FXML
	private Button saveInProfileButton;

	@FXML
	private TextArea tradeSummaryArea;

	@FXML
	private CheckBox doubleTradeCheckbox;

	@FXML
	private Button setResourcesButton;

	@FXML
	private Button addResourcesButton;

	ExecutorService executor;

	ObservableList<Profile> productionProfiles = FXCollections.observableArrayList();
	NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMANY);
	DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.GERMANY);
	DecimalFormat decimalFormat = new DecimalFormat("0.00", dfs);

	String[] resourceNames;

	@FXML
	void calculate(ActionEvent event) {

		CalculatorInput input = getCalculatorInput();
		logger.info("calculate input = " + input.toString());
		CalculatorTask task = new CalculatorTask(input);

		calcButton.disableProperty().bind(task.runningProperty());
		calcProgressBar.visibleProperty().bind(task.runningProperty());
		calcProgressBar.progressProperty().bind(task.progressProperty());

		task.setOnSucceeded((event2) -> {
			CalculatorOutput output = task.getValue();
			logger.info("calculate output = " + output.toString());
			displayOutput(output);
		});
		task.setOnFailed((event2) -> {
			Throwable th = task.getException();
			if (th != null) {
				logger.log(Level.SEVERE, "CalculatorTask failed", th);
			}
		});
		executor.submit(task);
	}

	@FXML
	void setResources(ActionEvent event) {
		UnitChooserDialog d = new UnitChooserDialog(resources);
		Optional<Pair<OGameUnit, Integer>> opt = d.showAndWait();
		opt.ifPresent(p -> {
			logger.info("UnitChooserDialog returned [id=" + p.getKey().id + ", level=" + p.getValue() + "]");
			long[] costs = getCosts(p.getKey(), p.getValue());
			logger.info("Costs for [id=" + p.getKey().id + ", level=" + p.getValue() + "] : " + Arrays.toString(costs));
			wantMetInput.setText("" + costs[0]);
			wantKrisInput.setText("" + costs[1]);
			wantDeutInput.setText("" + costs[2]);
		});
	}

	@SuppressWarnings("unchecked")
	@FXML
	void addResources(ActionEvent event) {
		UnitChooserDialog d = new UnitChooserDialog(resources);
		Optional<Pair<OGameUnit, Integer>> opt = d.showAndWait();
		opt.ifPresent(p -> {
			logger.info("UnitChooserDialog returned [id=" + p.getKey().id + ", level=" + p.getValue() + "]");
			long[] costs = getCosts(p.getKey(), p.getValue());
			logger.info("Costs for [id=" + p.getKey().id + ", level=" + p.getValue() + "] : " + Arrays.toString(costs));
			wantMetInput.setText("" + (((TextFormatter<Long>)wantMetInput.getTextFormatter()).getValue() + costs[0]));
			wantKrisInput.setText("" + (((TextFormatter<Long>)wantKrisInput.getTextFormatter()).getValue() + costs[1]));
			wantDeutInput.setText("" + (((TextFormatter<Long>)wantDeutInput.getTextFormatter()).getValue() + costs[2]));
		});
	}

	@FXML
	void createProfile(ActionEvent event) {
		Profile p = addNewProfile();
		saveProfilesToDisk();
		logger.info("created profile " + p.name);
	}

	@FXML
	void deleteProfile(ActionEvent event) {
		Profile p = profileListView.getSelectionModel().getSelectedItem();
		if (p != null) {
			productionProfiles.remove(p);
			saveProfilesToDisk();
			logger.info("deleted profile " + p.name);
		}
	}

	@SuppressWarnings("unchecked")
	@FXML
	void saveInProfile(ActionEvent event) {
		Profile saveInProfile = profileListView.getSelectionModel().getSelectedItem();
		if (saveInProfile == null) {
			saveInProfile = addNewProfile();
		}

		saveInProfile.prodMet = ((TextFormatter<Long>)prodMetInput.getTextFormatter()).getValue();
		saveInProfile.prodKris = ((TextFormatter<Long>)prodKrisInput.getTextFormatter()).getValue();
		saveInProfile.prodDeut = ((TextFormatter<Long>)prodDeutInput.getTextFormatter()).getValue();
		saveInProfile.rateMM = ((TextFormatter<Double>)rateMMInput.getTextFormatter()).getValue();
		saveInProfile.rateMK = ((TextFormatter<Double>)rateMKInput.getTextFormatter()).getValue();
		saveInProfile.rateMD = ((TextFormatter<Double>)rateMDInput.getTextFormatter()).getValue();
		saveInProfile.rateKM = ((TextFormatter<Double>)rateKMInput.getTextFormatter()).getValue();
		saveInProfile.rateKK = ((TextFormatter<Double>)rateKKInput.getTextFormatter()).getValue();
		saveInProfile.rateKD = ((TextFormatter<Double>)rateKDInput.getTextFormatter()).getValue();
		saveInProfile.rateDM = ((TextFormatter<Double>)rateDMInput.getTextFormatter()).getValue();
		saveInProfile.rateDK = ((TextFormatter<Double>)rateDKInput.getTextFormatter()).getValue();
		saveInProfile.rateDD = ((TextFormatter<Double>)rateDDInput.getTextFormatter()).getValue();

		saveProfilesToDisk();
		logger.info("saved values in profile " + saveInProfile.name);
	}

	@FXML
	void initialize() {
		assert doubleTradeCheckbox != null : "fx:id=\"doubleTradeCheckbox\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert prodMetInput != null : "fx:id=\"prodMetInput\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert prodKrisInput != null : "fx:id=\"prodKrisInput\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert prodDeutInput != null : "fx:id=\"prodDeutInput\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert haveMetInput != null : "fx:id=\"haveMetInput\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert haveKrisInput != null : "fx:id=\"haveKrisInput\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert haveDeutInput != null : "fx:id=\"haveDeutInput\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert wantMetInput != null : "fx:id=\"wantMetInput\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert wantKrisInput != null : "fx:id=\"wantKrisInput\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert wantDeutInput != null : "fx:id=\"wantDeutInput\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert calcButton != null : "fx:id=\"calcButton\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert calcProgressBar != null : "fx:id=\"calcProgressBar\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert setResourcesButton != null : "fx:id=\"setResourcesButton\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert addResourcesButton != null : "fx:id=\"addResourcesButton\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert rateMMInput != null : "fx:id=\"rateMMInput\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert rateMKInput != null : "fx:id=\"rateMKInput\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert rateMDInput != null : "fx:id=\"rateMDInput\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert rateKMInput != null : "fx:id=\"rateKMInput\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert rateKKInput != null : "fx:id=\"rateKKInput\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert rateKDInput != null : "fx:id=\"rateKDInput\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert rateDMInput != null : "fx:id=\"rateDMInput\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert rateDKInput != null : "fx:id=\"rateDKInput\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert rateDDInput != null : "fx:id=\"rateDDInput\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert dayStepInput != null : "fx:id=\"dayStepInput\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert profileListView != null : "fx:id=\"profileListView\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert createProfileButton != null : "fx:id=\"createProfileButton\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert deleteProfileButton != null : "fx:id=\"deleteProfileButton\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert saveInProfileButton != null : "fx:id=\"saveInProfileButton\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert tradeSummaryArea != null : "fx:id=\"tradeSummaryArea\" was not injected: check your FXML file 'MainWindow.fxml'.";

		logger.setLevel(Level.SEVERE);
		logger.setUseParentHandlers(false);

		Handler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.ALL);
		logger.addHandler(consoleHandler);

		try {
			final String folder = "logfiles";
			Files.createDirectories(Paths.get(folder));
			Handler fileHandler = new FileHandler(folder + File.separatorChar + "log.log", 50000, 1, true);
			fileHandler.setFormatter(new SimpleFormatter());
			fileHandler.setLevel(Level.INFO);
			logger.addHandler(fileHandler);
		} catch (Exception e) {
			System.out.println("Could not initialize logger");
			e.printStackTrace();
		}

		resourceNames = new String[] { resources.getString("metal"), resources.getString("crystal"),
				resources.getString("deuterium") };

		profileListView.setCellFactory(listView -> {
			ListCell<Profile> cell = new ListCell<Profile>() {
				@Override
				protected void updateItem(Profile item, boolean bln) {
					super.updateItem(item, bln);
					if (item != null && item.name != null) {
						setText(item.name);
					} else {
						setText(null);
						setGraphic(null);
					}
				}
			};
			return cell;
		});

		profileListView.setOnMouseClicked((event) -> {
			if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
				Profile profile = profileListView.getSelectionModel().getSelectedItem();
				if (profile != null) {
					applyProfile(profile);
				}
			}
		});

		loadProfilesFromDisk();
		profileListView.setItems(productionProfiles);

		StringConverter<Long> longinputconverter = new StringConverter<Long>() {
			@Override
			public String toString(Long object) {
				return nf.format(object);
			}

			@Override
			public Long fromString(String string) {
				return Long.parseLong(string.replaceAll("[.]", ""));
			}
		};

		UnaryOperator<TextFormatter.Change> digitPointFilter = c -> {
			if (c.getControlNewText().isEmpty()) {
				c.setText("0");
				return c;
			}
			if (!c.getControlNewText().matches("[0-9.]+")) {
				return null;
			}
			return c;
		};

		StringConverter<Double> doubleinputconverter = new StringConverter<Double>() {
			@Override
			public String toString(Double object) {
				String s = decimalFormat.format(object);
				return s;
			}

			@Override
			public Double fromString(String string) {
				Double d = Double.parseDouble(string.replaceAll(",", "."));
				return d;
			}
		};

		UnaryOperator<TextFormatter.Change> decimalFilter = c -> {
			if (c.getControlNewText().isEmpty()) {
				c.setText("1");
				return c;
			}
			if (!c.getControlNewText().matches("[0-9,]+")) {
				return null;
			}
			return c;
		};

		prodMetInput.setTextFormatter(new TextFormatter<Long>(longinputconverter, (long) 0, digitPointFilter));
		prodKrisInput.setTextFormatter(new TextFormatter<Long>(longinputconverter, (long) 0, digitPointFilter));
		prodDeutInput.setTextFormatter(new TextFormatter<Long>(longinputconverter, (long) 0, digitPointFilter));
		haveMetInput.setTextFormatter(new TextFormatter<Long>(longinputconverter, (long) 0, digitPointFilter));
		haveKrisInput.setTextFormatter(new TextFormatter<Long>(longinputconverter, (long) 0, digitPointFilter));
		haveDeutInput.setTextFormatter(new TextFormatter<Long>(longinputconverter, (long) 0, digitPointFilter));
		wantMetInput.setTextFormatter(new TextFormatter<Long>(longinputconverter, (long) 0, digitPointFilter));
		wantKrisInput.setTextFormatter(new TextFormatter<Long>(longinputconverter, (long) 0, digitPointFilter));
		wantDeutInput.setTextFormatter(new TextFormatter<Long>(longinputconverter, (long) 0, digitPointFilter));
		rateMMInput.setTextFormatter(new TextFormatter<Double>(doubleinputconverter, 2.0, decimalFilter));
		rateMKInput.setTextFormatter(new TextFormatter<Double>(doubleinputconverter, 1.0, decimalFilter));
		rateMDInput.setTextFormatter(new TextFormatter<Double>(doubleinputconverter, 1.0, decimalFilter));
		rateKMInput.setTextFormatter(new TextFormatter<Double>(doubleinputconverter, 2.0, decimalFilter));
		rateKKInput.setTextFormatter(new TextFormatter<Double>(doubleinputconverter, 1.0, decimalFilter));
		rateKDInput.setTextFormatter(new TextFormatter<Double>(doubleinputconverter, 1.0, decimalFilter));
		rateDMInput.setTextFormatter(new TextFormatter<Double>(doubleinputconverter, 2.0, decimalFilter));
		rateDKInput.setTextFormatter(new TextFormatter<Double>(doubleinputconverter, 1.0, decimalFilter));
		rateDDInput.setTextFormatter(new TextFormatter<Double>(doubleinputconverter, 1.0, decimalFilter));
		dayStepInput.setTextFormatter(new TextFormatter<Double>(doubleinputconverter, 1.0, decimalFilter));
	}

	@SuppressWarnings("unchecked")
	private CalculatorInput getCalculatorInput() {

		CalculatorInput p = new CalculatorInput();
		try {
			double[][] rates = {
					{ Math.max(10e-3, ((TextFormatter<Double>)rateMMInput.getTextFormatter()).getValue()),
							Math.max(10e-3, ((TextFormatter<Double>)rateMKInput.getTextFormatter()).getValue()),
							Math.max(10e-3, ((TextFormatter<Double>)rateMDInput.getTextFormatter()).getValue()) },
					{ Math.max(10e-3, ((TextFormatter<Double>)rateKMInput.getTextFormatter()).getValue()),
							Math.max(10e-3, ((TextFormatter<Double>)rateKKInput.getTextFormatter()).getValue()),
							Math.max(10e-3, ((TextFormatter<Double>)rateKDInput.getTextFormatter()).getValue()) },
					{ Math.max(10e-3, ((TextFormatter<Double>)rateDMInput.getTextFormatter()).getValue()),
							Math.max(10e-3, ((TextFormatter<Double>)rateDKInput.getTextFormatter()).getValue()),
							Math.max(10e-3, ((TextFormatter<Double>)rateDDInput.getTextFormatter()).getValue()) } };

			long prodMet = Math.max(0, ((TextFormatter<Long>)prodMetInput.getTextFormatter()).getValue());
			long prodKris = Math.max(0, ((TextFormatter<Long>)prodKrisInput.getTextFormatter()).getValue());
			long prodDeut = Math.max(0, ((TextFormatter<Long>)prodDeutInput.getTextFormatter()).getValue());
			long haveMet = Math.max(0, ((TextFormatter<Long>)haveMetInput.getTextFormatter()).getValue());
			long haveKris = Math.max(0, ((TextFormatter<Long>)haveKrisInput.getTextFormatter()).getValue());
			long haveDeut = Math.max(0, ((TextFormatter<Long>)haveDeutInput.getTextFormatter()).getValue());
			long wantMet = Math.max(0, ((TextFormatter<Long>)wantMetInput.getTextFormatter()).getValue());
			long wantKris = Math.max(0, ((TextFormatter<Long>)wantKrisInput.getTextFormatter()).getValue());
			long wantDeut = Math.max(0, ((TextFormatter<Long>)wantDeutInput.getTextFormatter()).getValue());

			double dayStep = Math.max(10E-3, ((TextFormatter<Double>)dayStepInput.getTextFormatter()).getValue());

			boolean doubleTrade = doubleTradeCheckbox.isSelected();

			p = new CalculatorInput(prodMet, prodKris, prodDeut, haveMet, haveKris, haveDeut, wantMet, wantKris,
					wantDeut, rates, dayStep, doubleTrade);

		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Error getting CalculatorInput", ex);
		}
		return p;
	}

	private Profile addNewProfile() {
		TextInputDialog dialog = new TextInputDialog(resources.getString("profilename"));
		dialog.setTitle(resources.getString("profilecreation"));
		dialog.setHeaderText(resources.getString("profilecreation"));
		dialog.setContentText(resources.getString("pleaseenterprofilename"));

		Optional<String> result = dialog.showAndWait();
		String profileName = result.orElse("DEFAULT");
		Profile profile = new Profile(profileName);
		productionProfiles.add(profile);
		profileListView.getSelectionModel().select(profile);

		return profile;
	}

	private void saveProfilesToDisk() {
		Path savePath = Paths.get("profiles.txt");
		try (BufferedWriter bw = Files.newBufferedWriter(savePath);) {
			for (Profile p : productionProfiles) {
				bw.write(p.name + "#" + p.prodMet + "#" + p.prodKris + "#" + p.prodDeut + "#" + p.rateMM + "#"
						+ p.rateMK + "#" + p.rateMD + "#" + p.rateKM + "#" + p.rateKK + "#" + p.rateKD + "#" + p.rateDM
						+ "#" + p.rateDK + "#" + p.rateDD + "|");
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "could not save profiles to disk", e);
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(resources.getString("error_savingprofiles"));
			alert.showAndWait();
		}
	}

	private void loadProfilesFromDisk() {
		Path savePath = Paths.get("profiles.txt");
		if (Files.exists(savePath)) {
			try (BufferedReader br = Files.newBufferedReader(savePath);) {
				String line = br.readLine();
				String[] profiles = line.split("[|]");
				for (String profilestring : profiles) {
					String[] data = profilestring.split("#");
					Profile profile = new Profile(data[0], Long.parseLong(data[1]), Long.parseLong(data[2]),
							Long.parseLong(data[3]), Double.parseDouble(data[4]), Double.parseDouble(data[5]),
							Double.parseDouble(data[6]), Double.parseDouble(data[7]), Double.parseDouble(data[8]),
							Double.parseDouble(data[9]), Double.parseDouble(data[10]), Double.parseDouble(data[11]),
							Double.parseDouble(data[12]));
					productionProfiles.add(profile);
				}
			} catch (Exception e) {
				logger.log(Level.SEVERE, "could not load profiles from disk", e);
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle(resources.getString("error_loadprofiles"));
				alert.showAndWait();
			}
		}
	}

	private void applyProfile(Profile profile) {
		Objects.requireNonNull(profile, "applyProfile profile is null");
		prodMetInput.setText("" + profile.prodMet);
		prodKrisInput.setText("" + profile.prodKris);
		prodDeutInput.setText("" + profile.prodDeut);
		rateMMInput.setText(decimalFormat.format(profile.rateMM));
		rateMKInput.setText(decimalFormat.format(profile.rateMK));
		rateMDInput.setText(decimalFormat.format(profile.rateMD));
		rateKMInput.setText(decimalFormat.format(profile.rateKM));
		rateKKInput.setText(decimalFormat.format(profile.rateKK));
		rateKDInput.setText(decimalFormat.format(profile.rateKD));
		rateDMInput.setText(decimalFormat.format(profile.rateDM));
		rateDKInput.setText(decimalFormat.format(profile.rateDK));
		rateDDInput.setText(decimalFormat.format(profile.rateDD));
		logger.info("applyProfile " + profile.toString());
	}

	private long[] getCosts(OGameUnit u, long level) {
		Objects.requireNonNull(u, "getCosts OGameUnit is null");

		double[] costs = { 0, 0, 0 };
		long[] retCosts = { 0, 0, 0 };
		for (int i = 0; i < 3; i++) {
			if (u.type == Type.UNIT)
				costs[i] = u.baseCosts[i] * level;
			else
				costs[i] = u.baseCosts[i] * Math.pow(u.factor, Math.max(0, level) - 1);
			retCosts[i] = Math.max(0, (long) costs[i]);
		}
		return retCosts;
	}

	private void displayOutput(CalculatorOutput output) {
		Objects.requireNonNull(output, "displayOutput output is null");

		tradeSummaryArea.clear();

		if (output.alreadyDone) {
			tradeSummaryArea.appendText(resources.getString("output_youalreadyhaveenough") + "\n");
		} else {

			if (output.maximumSaveDays == Double.MAX_VALUE) {
				tradeSummaryArea.appendText(resources.getString("output_savenotpossiblewithouttrade") + "\n");
			} else {
				String days = decimalFormat.format(((output.maximumSaveDays * 100)) / 100.0);
				String s = MessageFormat.format(resources.getString("output_dayswithouttrade"), days);
				tradeSummaryArea.appendText(s + "\n");
			}

			if (output.saveDays == Double.MAX_VALUE) {
				tradeSummaryArea.appendText(resources.getString("output_savenotpossiblewithtrade"));
				tradeSummaryArea.appendText(
						(output.tradeList.size() == 0? "" : " (" + resources.getString("output_incomplete") + ")") + "\n\n");
			} else {
				String days = decimalFormat.format(((output.saveDays * 100)) / 100.0);
				String s = MessageFormat.format(resources.getString("output_dayswithtrade"), days);
				tradeSummaryArea.appendText(s);
				tradeSummaryArea.appendText(
						(output.complete ? "" : " (" + resources.getString("output_incomplete") + ")") + "\n\n");
			}

			long[] resourcesAfterTrade = { 0, 0, 0 };

			if (output.complete) {
				String days = decimalFormat.format(((output.saveDays * 100)) / 100.0);
				String s = MessageFormat.format(resources.getString("output_producedresourcesafterdays"), days);
				tradeSummaryArea.appendText(s + "\n");
			}
			for (int i = 0; i < 3; i++) {
				resourcesAfterTrade[i] += output.producedRessources[i] + output.haveResources[i];
				if (output.complete)
					tradeSummaryArea
							.appendText(nf.format(output.producedRessources[i]) + " " + resourceNames[i] + "\n");
			}
			tradeSummaryArea.appendText("\n");

			for (Trade t : output.tradeList) {
				resourcesAfterTrade[t.from] -= t.fromAmount;
				resourcesAfterTrade[t.to] += t.toAmount;

				tradeSummaryArea.appendText(MessageFormat.format(resources.getString("output_traderesources"),
						nf.format(t.fromAmount), resourceNames[t.from]));
				if (t.middle != -1)
					tradeSummaryArea.appendText(" --> " + nf.format(t.middleAmount) + " " + resourceNames[t.middle]);
				tradeSummaryArea.appendText(" --> " + nf.format(t.toAmount) + " " + resourceNames[t.to] + "\n");
			}
			tradeSummaryArea.appendText("\n");

			if (output.tradeList.size() > 0) {
				tradeSummaryArea.appendText(resources.getString("output_resourcesaftertrade") + ":" + "\n");
				for (int i = 0; i < 3; i++) {
					tradeSummaryArea.appendText(nf.format(resourcesAfterTrade[i]) + " " + resourceNames[i] + "\n");
				}
			}
		}

	}

	public void setLoggerLevel(Level level){
		logger.setLevel(level);
	}

}
