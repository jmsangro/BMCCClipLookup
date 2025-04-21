package cliplookup;
	
import java.io.File;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;


public class ClipLookupMain extends Application {
	private static Stage mainStage;
	private static Scene queryScene;
	private static Scene videoScene;
	private static VBox vidRoot ;
	private static Label clipTitleLabel ;
	private static MediaView mediaView;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			mainStage = primaryStage;
			VBox root = (VBox)FXMLLoader.load(getClass().getResource("StateScene.fxml"));
			queryScene = new Scene(root,1200,800);
			queryScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(queryScene);
			primaryStage.show();
			


			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void showClip(String clipTitle, String clipFile) {
		mainStage.hide();
		if (videoScene == null) {
			//construct videoScene
			vidRoot = new VBox();
			vidRoot.setAlignment(Pos.CENTER);
			clipTitleLabel = new Label();
			vidRoot.getChildren().add(clipTitleLabel);
			mediaView = new MediaView();
			vidRoot.getChildren().add(mediaView);
			videoScene = new Scene(vidRoot,1200,800);
		}
		System.out.println("File:"+clipFile);
		try {
			clipTitleLabel.setText(clipTitle);
			Media media = new Media(new File(clipFile).toURI().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(media);
			mediaView.setMediaPlayer(mediaPlayer);

			mediaPlayer.setOnEndOfMedia(() -> {
				mainStage.hide();
				mainStage.setScene(queryScene);
				mainStage.show();
			});
			vidRoot.setOnMouseClicked(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent me) {
						try {
							mediaView.getMediaPlayer().stop();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mainStage.hide();
						mainStage.setScene(queryScene);
						mainStage.show();
					}
			});			
			mainStage.setScene(videoScene);
			mediaView.setVisible(true);
			mainStage.show();
			mediaPlayer.play();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
