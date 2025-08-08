package cliplookup;


import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class ClipController implements Initializable{
	private DataSource dataSource;
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		initData();
	}

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
	private ComboBox<PicLabelData> personComboBox;
	@FXML
	private ComboBox<String> stateCombo;
	@FXML
	private ComboBox<String> clipComboBox;
	@FXML
	private ComboBox<String> sirNameComboBox;
	@FXML
	private ComboBox<String> themeComboBox;
	@FXML
	private Button clipStartButton;
	@FXML
	private Label playLabel;
	
	private Node[] stateCriteriaNodes;
	private Node[] sirNameCriteriaNodes;
	private Node[] themeCriteriaNodes;
	private long lastActionTime;
	private static final long ACTION_TIMEOUT = 3*60*1000;//minutes*second/minute*ms/sec (3minutes)

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		stateCriteriaNodes  = new Node[] { stateChoiceVbox, townChoiceVbox, personChoiceVbox, clipChoiceVbox };
		sirNameCriteriaNodes  = new Node[] { sirNameChoiceVbox, personChoiceVbox, clipChoiceVbox };		
		themeCriteriaNodes  = new Node[] { themeVbox, clipChoiceVbox };		
   		resetControlVisibility();
		Timer timer = new Timer();
		timer.schedule(new IdleTimerTask(), 120000, 60000);		
		
		
	}

	private void resetControlVisibility() {
		for (Node node : stateCriteriaNodes) {
			node.setVisible(false);
		}
		for (Node node : sirNameCriteriaNodes) {
			node.setVisible(false);
		}
		for (Node node : themeCriteriaNodes) {
			node.setVisible(false);
		}
		clipStartButton.setVisible(false);
		playLabel.setVisible(false);
	}
	
	private class IdleTimerTask extends TimerTask{
		@Override
		public void run() {
			System.out.println("Idle Timer Task Run");
			long now = new Date().getTime();
			if ( now > lastActionTime + ACTION_TIMEOUT) {
				Platform.runLater( () -> {
					//RESET UI
					resetControlVisibility();
				} );
			}
			
		}
	}
	
	private void markActionTime() {
		lastActionTime = new Date().getTime();
	}

	private void initData() {
			ObservableList<String> items =FXCollections.observableArrayList ( dataSource.getAllStates() );
			stateCombo.setItems(items);
			ObservableList<String> snItems =FXCollections.observableArrayList (dataSource.getAllSirNames());
			sirNameComboBox.setItems(snItems);
			ObservableList<String> themeItems =FXCollections.observableArrayList (dataSource.getAllThemes());
			themeComboBox.setItems(themeItems);
			personComboBox.setCellFactory((ListView<PicLabelData> l) -> new PicNameCell());


	}
	
	@FXML
	public void stateSelected(ActionEvent e) {
		markActionTime();
        String state = stateCombo.getValue();
        System.out.println(state + " selected");
        Collection<String> towns = dataSource.getTowns(state);
		ObservableList<String> items =FXCollections.observableArrayList ( towns);
		townComboBox.setItems(items);
        townChoiceVbox.setVisible(true);
        if (towns.size() == 1) {
        	townComboBox.setValue(items.iterator().next());
        }
        else {
        	townComboBox.setValue(null);
        	hideDownStreamNodes(townChoiceVbox, stateCriteriaNodes);
        }

	}
	

	@FXML
	public void byStateSelected() {
		markActionTime();
		criteriaHbox.getChildren().clear();
		criteriaHbox.getChildren().addAll(stateCriteriaNodes);
		stateCombo.setValue(null);
		stateChoiceVbox.setVisible(true);
		
		hideDownStreamNodes(stateChoiceVbox, stateCriteriaNodes);
	}

	@FXML
	public void byThemeSelected() {
		markActionTime();
		criteriaHbox.getChildren().clear();
		criteriaHbox.getChildren().addAll(themeCriteriaNodes);
		themeComboBox.setValue(null);
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
		clipStartButton.setVisible(false);
		playLabel.setVisible(false);
	}

	@FXML
	public void bySirNameSelected() {
		markActionTime();
		criteriaHbox.getChildren().clear();
		criteriaHbox.getChildren().addAll(sirNameCriteriaNodes);
		sirNameComboBox.setValue(null);
		sirNameChoiceVbox.setVisible(true);
		hideDownStreamNodes(sirNameChoiceVbox, sirNameCriteriaNodes);

	}
	
	@FXML
	public void sirNameSelected() {
		markActionTime();
		String value = sirNameComboBox.getValue();
		System.out.println("sir name:"+value);
		if (value != null) {

			Collection<PicLabelData> persons = dataSource.getPicLabelsWithSirName(value);
			ObservableList<PicLabelData> items =FXCollections.observableArrayList (persons);
			personComboBox.setItems(items);
			personChoiceVbox.setVisible(true);
			if (persons.size() == 1) {//skip having to make a choice
				personComboBox.setValue(persons.iterator().next());
			}
			else {
				personComboBox.setValue(null);
				hideDownStreamNodes(personChoiceVbox, sirNameCriteriaNodes);
			}
		}

	}	
	@FXML
	public void townSelected() {
		markActionTime();
		String value =townComboBox.getValue();
		System.out.println("town:"+value);
		if (value != null) {
			Collection<PicLabelData> persons = dataSource.getPicLabelsFromTown(value);
			ObservableList<PicLabelData> items =FXCollections.observableArrayList (persons);		
			personComboBox.setItems(items);
	
			personChoiceVbox.setVisible(true);
			if (persons.size() == 1) {//skip having to make a choice
				personComboBox.setValue(persons.iterator().next());
			}
			else {
				personComboBox.setValue(null);
				hideDownStreamNodes(personChoiceVbox, stateCriteriaNodes);
			}
		}
		

	}
	@FXML
	public void personSelected() {
		markActionTime();
		PicLabelData pld = personComboBox.getValue();
		System.out.println("person:"+pld);
		if (pld != null) {
			Collection<String> clips = dataSource.getClipsOfPerson(personComboBox.getValue().labelText);
			setupClipChoices(clips);
		}

	}

	private void setupClipChoices(Collection<String> clips) {

		if (clips.size() == 1) {//skip having to make a choice
			clipStartButton.setVisible(true);
			playLabel.setVisible(true);
			clipStartButton.setText(clips.iterator().next());

		}
		else {
			ObservableList<String> items =FXCollections.observableArrayList (clips);		
			clipComboBox.setItems(items);
			clipChoiceVbox.setVisible(true);
			clipComboBox.setValue(null);
		}
	}
	
	@FXML void clipStartButtonPushed() {
		ClipInfo clipInfo = dataSource.getClipLocator(clipStartButton.getText());
		if (clipInfo != null) {
			playClip( clipInfo);
		} 
	}
	
	@FXML
	public void clipSelected() {
		markActionTime();
		String clipName = clipComboBox.getValue();
		System.out.println("clip: "+clipName+" selected");
		if (clipName != null) {
			clipStartButton.setVisible(true);
			playLabel.setVisible(true);
			clipStartButton.setText(clipName);
			clipStartButtonPushed();

		}
	}

	private void playClip(ClipInfo clipInfo) {
		ClipLookupMain.showClip(clipInfo);
//		System.out.println("File:"+fileName);
//		Media media = new Media(new File(fileName).toURI().toString());
//		mediaPlayer = new MediaPlayer(media);
//		mediaView.setMediaPlayer(mediaPlayer);
//		mediaView.setVisible(true);
//		mediaPlayer.play();
	}
	
	@FXML
	public void themeSelected() {
		markActionTime();
		String theme = themeComboBox.getValue();
		System.out.println("theme:"+theme);
		if (theme != null) {
			Collection<String> clips = dataSource.getClipsOfTheme(theme);
			setupClipChoices(clips);
		}
	}
	

	
}
