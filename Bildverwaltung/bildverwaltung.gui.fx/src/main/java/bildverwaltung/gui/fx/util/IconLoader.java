package bildverwaltung.gui.fx.util;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class IconLoader {

	private IconLoader() {
	}
	
	public static ImageView loadIcon(String resource) {
		InputStream in = IconLoader.class.getClassLoader().getResourceAsStream(resource);
		ImageView node = new ImageView();
		node.setImage(new Image(in));
		return node;
	}
	
}
