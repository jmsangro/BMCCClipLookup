package cliplookup;

import java.io.File;
import java.net.MalformedURLException;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class PicNameCell  extends ListCell<PicLabelData> {

     public PicNameCell() {
    	 
     }
       
     @Override
     protected void updateItem(PicLabelData item, boolean empty) {
         // calling super here is very important - don't skip this!
         super.updateItem(item, empty);
         if (item != null) {
			HBox hbox = new HBox();
			try {
				ImageView imageView = new ImageView();
				Image image = new Image(new File(item.imageURL).toURI().toURL().toExternalForm());
				imageView.setImage(image);
				imageView.setFitHeight(80);
				imageView.setFitWidth(120);
				hbox.getChildren().add(imageView);
			} catch (MalformedURLException e) {
				System.err.println(item.imageURL + "  " + e);
			}
			Label label = new Label();
			label.setText(item.labelText);
			hbox.getChildren().add(label);
			setGraphic(hbox);
		}
         

     }
}
