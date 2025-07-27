package cliplookup;
	
import java.io.File;


import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;


public class ClipLookupMain extends Application {
	private static Stage mainStage;
	private static Scene queryScene;
	private static Scene videoScene;
	private static VBox vidRoot ;
	private static Label clipTitleLabel ;
	private static MediaView mediaView;
	private static HBox clipInfoHBox;
	private static Label whenLabel;
	private static Label whereLabel;


	@Override
	public void start(Stage primaryStage) {
		try {
			mainStage = primaryStage;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ClipLookupScene.fxml"));
			VBox root = (VBox)loader.load();
			String dataFile = System.getProperty("dataFile");
			DataSource dataSource = new DataSource(dataFile);
			ClipController controller = (ClipController)loader.getController();
			controller.setDataSource(dataSource);
			Rectangle2D screenBounds = Screen.getPrimary().getBounds();
			queryScene = new Scene(root,screenBounds.getWidth(),screenBounds.getHeight());
			
			//queryScene = new Scene(root,1200,800);
			queryScene.getStylesheets().add(getClass().getResource("clipLookupApp.css").toExternalForm());
			primaryStage.setFullScreenExitHint("");
			primaryStage.initStyle(StageStyle.UNDECORATED);
			 primaryStage.setX(screenBounds.getMinX());
			 primaryStage.setY(screenBounds.getMinY());
			 primaryStage.setWidth(screenBounds.getWidth());
			 primaryStage.setHeight(screenBounds.getHeight());
			primaryStage.setFullScreen(true);
			primaryStage.setScene(queryScene);

			primaryStage.show();
			


			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void showClip(ClipInfo clipInfo) {
		//mainStage.hide();
		if (videoScene == null) {
			//construct videoScene
			vidRoot = new VBox();
			vidRoot.setAlignment(Pos.CENTER);
			clipTitleLabel = new Label();
			vidRoot.getChildren().add(clipTitleLabel);
			mediaView = new MediaView();
			vidRoot.getChildren().add(mediaView);
			//set up clip info below media view
			clipInfoHBox = new HBox();
			clipInfoHBox.setAlignment(Pos.CENTER);
			whenLabel = new Label();
			whereLabel = new Label();
			clipInfoHBox.getChildren().add(whereLabel);
			clipInfoHBox.getChildren().add(whenLabel);
			vidRoot.getChildren().add(clipInfoHBox);
			Label returnNoteLabel= new Label("Click on video to stop and return.");
			vidRoot.getChildren().add(returnNoteLabel);
			Rectangle2D screenBounds = Screen.getPrimary().getBounds();
			videoScene = new Scene(vidRoot,screenBounds.getWidth(),screenBounds.getHeight());
			videoScene.getStylesheets().add(clipInfo.getClass().getResource("videoScene.css").toExternalForm());

		}
		System.out.println("File:"+clipInfo.getClipLink());
		try {
			clipTitleLabel.setText(clipInfo.getClipTitle());
			whenLabel.setText("Interviewed on "+clipInfo.getDateOfInterview());
			whereLabel.setText(clipInfo.getCommunity()+", "+clipInfo.getState()+"   ");			
			Media media = new Media(new File(clipInfo.getClipLink()).toURI().toString());
			media.setOnError(() -> {
				MediaException e = media.getError();
				System.err.println("Media encountered error:");
				e.printStackTrace();
			});
			MediaPlayer mediaPlayer = new MediaPlayer(media);
			mediaView.setMediaPlayer(mediaPlayer);
			mediaView.getStyleClass().add(".media-view");
			DoubleProperty mvw = mediaView.fitWidthProperty();
			DoubleProperty mvh = mediaView.fitHeightProperty();
			//mvw.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
			mvh.set(720.0);
			mvw.set(1280.0);
			mediaView.setPreserveRatio(true);
			mediaPlayer.setOnEndOfMedia(() -> {
				//mainStage.hide();
				mediaPlayer.dispose();
				mainStage.setScene(queryScene);
				mainStage.setFullScreenExitHint("");
				mainStage.setFullScreen(true);
				//mainStage.show();
			});
			mediaPlayer.setOnError(() -> {
				MediaException e = mediaPlayer.getError();
				System.err.println("MediaPlayer encountered error:");

				e.printStackTrace();
			});
			vidRoot.setOnMouseClicked(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent me) {
						try {
							mediaView.getMediaPlayer().stop();
							mediaView.getMediaPlayer().dispose();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//mainStage.hide();
						mainStage.setScene(queryScene);
						mainStage.setFullScreenExitHint("");
						mainStage.setFullScreen(true);
						//mainStage.show();
					}
			});			
			mainStage.setScene(videoScene);
			mainStage.setFullScreenExitHint("");
			mainStage.setFullScreen(true);
			//mainStage.show();
			mediaView.setVisible(true);
			mediaPlayer.setAutoPlay(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
