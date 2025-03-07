package cliplookup;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class StateController implements Initializable{
	private DataSource dataSource;
	
	@FXML
	private Button byStateButton;
	@FXML
	private Button byNameButton;
	@FXML
	private Button byValueButton;
	@FXML
	private HBox criteriaHbox;
	@FXML
	private VBox stateChoiceVbox;
	@FXML
	private VBox sirNameChoiceVbox;
	@FXML
	private VBox townChoiceVbox;
	@FXML
	private VBox personChoiceVbox;
	@FXML
	private VBox clipChoiceVbox;
	@FXML
	private VBox themeVbox;
	@FXML
	private ComboBox<String> townComboBox;
	@FXML
	private ComboBox<String> personComboBox;
	@FXML
	private ComboBox<String> stateCombo;
	@FXML
	private ComboBox<String> clipComboBox;
	@FXML
	private ComboBox<String> sirNameComboBox;
	@FXML
	private ComboBox<String> themeComboBox;
	@FXML
	private MediaView mediaView;
	@FXML
	private Label clipTitleLabel;
	private MediaPlayer mediaPlayer;
	
	private Node[] stateCriteriaNodes;
	private Node[] sirNameCriteriaNodes;
	private Node[] themeCriteriaNodes;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		dataSource = new DataSource();
		stateCriteriaNodes  = new Node[] { stateChoiceVbox, townChoiceVbox, personChoiceVbox, clipChoiceVbox };
		sirNameCriteriaNodes  = new Node[] { sirNameChoiceVbox, personChoiceVbox, clipChoiceVbox };		
		themeCriteriaNodes  = new Node[] { themeVbox, clipChoiceVbox };		
   		for (Node node : stateCriteriaNodes) {
			node.setVisible(false);
		}
		for (Node node : sirNameCriteriaNodes) {
			node.setVisible(false);
		}
		for (Node node : themeCriteriaNodes) {
			node.setVisible(false);
		}
		ObservableList<String> items =FXCollections.observableArrayList ( dataSource.getAllStates() );
		stateCombo.setItems(items);
		ObservableList<String> snItems =FXCollections.observableArrayList (dataSource.getAllSirNames());
		sirNameComboBox.setItems(snItems);
		ObservableList<String> themeItems =FXCollections.observableArrayList (dataSource.getAllThemes());
		themeComboBox.setItems(themeItems);
		
	}
	
	@FXML
	public void stateSelected(ActionEvent e) {
        String state = stateCombo.getValue();
        System.out.println(state + " selected");
        resetVideo();
        setupTownsForState(state);
        townChoiceVbox.setVisible(true);
		hideDownStreamNodes(townChoiceVbox, stateCriteriaNodes);

	}
	
	protected void setupTownsForState(String s) {
		Collection<String> towns = dataSource.getTowns(s);
		ObservableList<String> items =FXCollections.observableArrayList ( towns);
		townComboBox.setItems(items);
		
	}

	@FXML
	public void byStateSelected() {
		resetVideo();
		criteriaHbox.getChildren().clear();
		criteriaHbox.getChildren().addAll(stateCriteriaNodes);
		stateChoiceVbox.setVisible(true);
		hideDownStreamNodes(stateChoiceVbox, stateCriteriaNodes);
	}

	@FXML
	public void byThemeSelected() {
		resetVideo();
		criteriaHbox.getChildren().clear();
		criteriaHbox.getChildren().addAll(themeCriteriaNodes);
		themeVbox.setVisible(true);
		hideDownStreamNodes(themeVbox, themeCriteriaNodes);
	}
	
	private void hideDownStreamNodes(Node disableAfter, Node[] nodeList) {
		boolean found = false;
		for (Node node : nodeList ) {
			if (found) {
				node.setVisible(false);
			}
			else if (node.equals(disableAfter)) {
				found = true;
			}
		}
	}

	@FXML
	public void bySirNameSelected() {
		resetVideo();
		criteriaHbox.getChildren().clear();
		criteriaHbox.getChildren().addAll(sirNameCriteriaNodes);
		sirNameChoiceVbox.setVisible(true);
		hideDownStreamNodes(sirNameChoiceVbox, sirNameCriteriaNodes);

	}
	
	@FXML
	public void sirNameSelected() {
		resetVideo();
		System.out.println("sir name:"+sirNameComboBox.getValue());
		Collection<String> persons = dataSource.getPersonsWithSirName(sirNameComboBox.getValue());
		ObservableList<String> items =FXCollections.observableArrayList (persons);		
		personComboBox.setItems(items);
		personChoiceVbox.setVisible(true);
		hideDownStreamNodes(personChoiceVbox, sirNameCriteriaNodes);

	}	
	@FXML
	public void townSelected() {
		resetVideo();
		System.out.println("town:"+townComboBox.getValue());
		Collection<String> persons = dataSource.getPersonsFromTown(townComboBox.getValue());
		ObservableList<String> items =FXCollections.observableArrayList (persons);		
		personComboBox.setItems(items);

		personChoiceVbox.setVisible(true);
		hideDownStreamNodes(personChoiceVbox, stateCriteriaNodes);

	}
	@FXML
	public void personSelected() {
		resetVideo();
		System.out.println("person:"+personComboBox.getValue());
		Collection<String> clips = dataSource.getClipsOfPerson(personComboBox.getValue());
		ObservableList<String> items =FXCollections.observableArrayList (clips);		
		clipComboBox.setItems(items);
		clipChoiceVbox.setVisible(true);
	}
	
	@FXML
	public void clipSelected() {
		String clipName = clipComboBox.getValue();
		System.out.println("clip: "+clipName+" selected");
		clipTitleLabel.setText(clipName);
		clipTitleLabel.setVisible(true);
		String fileName = dataSource.getClipLocator( clipName);
        if (fileName != null) {
        	System.out.println("File:"+fileName);
			Media media = new Media(new File(fileName).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			mediaView.setMediaPlayer(mediaPlayer);
			mediaView.setVisible(true);
			mediaPlayer.play();
		}
	}
	
	@FXML
	public void themeSelected() {
		resetVideo();
		String theme = themeComboBox.getValue();
		Collection<String> clips = dataSource.getClipsOfTheme(theme);
		ObservableList<String> items =FXCollections.observableArrayList (clips);		
		clipComboBox.setItems(items);
		clipChoiceVbox.setVisible(true);		
	}
	
	private void resetVideo() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
		mediaView.setVisible(false);
		clipTitleLabel.setVisible(false);
	}

	
}
