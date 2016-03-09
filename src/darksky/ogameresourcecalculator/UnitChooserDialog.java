package darksky.ogameresourcecalculator;

import java.util.ResourceBundle;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

/**
 * A Dialog to retrieve an {@link OGameUnit} and a level / count
 * 
 * @author ogame.de Dark Sky
 * 
 */
public class UnitChooserDialog extends Dialog<Pair<OGameUnit, Integer>> {

	/**
	 * 
	 * @param resources
	 *            The {@link ResourceBundle} containing the names of
	 *            {@link OGameUnit}s
	 */
	public UnitChooserDialog(ResourceBundle resources) {

		this.setTitle(resources.getString("input"));

		ButtonType okButtonType = new ButtonType("OK", ButtonData.OK_DONE);
		this.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		TextField level = new TextField("1");
		level.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("[0-9\\-]+")) {
				level.setText("0");
			}
		});

		ListView<OGameUnit> listview = new ListView<>();

		listview.setCellFactory(listView -> {
			ListCell<OGameUnit> cell = new ListCell<OGameUnit>() {
				@Override
				protected void updateItem(OGameUnit item, boolean bln) {
					super.updateItem(item, bln);
					if (item != null) {
						try {
							String name = resources.getString("" + item.id);
							setText(name);
						} catch (Exception e) {
							setText(resources.getString("error_namenotfound") + " (ID " + item.id + ")");
						}

					} else {
						setText(null);
						setGraphic(null);
					}
				}

			};
			return cell;
		});

		listview.setItems(OGameUnit.UNIT_LIST);
		listview.setPrefHeight(150);
		listview.getSelectionModel().select(0);

		grid.add(listview, 0, 0);
		grid.add(level, 0, 1);

		this.getDialogPane().setContent(grid);
		this.setResultConverter(dialogButton -> {
			if (dialogButton == okButtonType) {
				return new Pair<>(listview.getSelectionModel().getSelectedItem(), Integer.parseInt(level.getText()));
			}
			return null;
		});
	}
}
