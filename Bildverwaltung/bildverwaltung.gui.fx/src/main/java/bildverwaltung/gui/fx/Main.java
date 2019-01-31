package bildverwaltung.gui.fx;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;

public class Main extends Application {
	
	private static final String ORGANIZE = "Verwalten";
	
	private static final double HEIGHT  = 800.0;
	private static final double WIDTH = 600.0;
	private static final Logger LOG = LoggerFactory.getLogger(Main.class);

@Override
public void start( final javafx.stage.Stage primaryStage ){
	LOG.info( "Start" );
	
	primaryStage.setTitle("Bilderverwaltung");
	primaryStage.setHeight(HEIGHT);
	primaryStage.setWidth(WIDTH);
	
	Group root = new Group();

	primaryStage.setResizable(true);
	
	HBox menu = new HBox();
	
	 /*
	  * Menu Verwaltung werden hier angelegt
	  */
	 Menu menuOrganize   		= new Menu(ORGANIZE);
	 Menu organizeFiles 		= new Menu("Alben Verwalten");
	 MenuItem openFile 		= new MenuItem("Album Öffnen");
	 MenuItem newFile   		= new MenuItem("Album Anlegen");
	 MenuItem fileLocation 		= new MenuItem("Album Verschieben");
	 MenuItem deleteFile 		= new MenuItem("Album Löschen");
	 
	 try{
		 	organizeFiles.getItems().addAll(openFile, newFile, fileLocation,deleteFile);
	 
		 	Menu organizeImages 		= new Menu("Bilder Verwalten");
		 	MenuItem showImage  		= new MenuItem("Bild Anzeigen");
		 	MenuItem saveAsImage		= new MenuItem("Bild Zufügen zu einem Album");
		 	MenuItem imageLocation		= new MenuItem("Bild Verschieben");
		 	MenuItem deleteImage 		= new MenuItem("Bild Löschen");
		 	organizeImages.getItems().addAll(showImage, saveAsImage, imageLocation,deleteImage);
	
		 	Menu menuImport 	= new Menu("Importieren");
		 	MenuItem importFile 	= new MenuItem("Album Importieren");
		 	MenuItem importImage 	= new MenuItem("Bild Importieren");
		 	menuImport.getItems().addAll(importFile, importImage);
	 
		 	menuOrganize.getItems().addAll(organizeFiles,organizeImages,menuImport);
	 
		 	Menu menuHelp   = new Menu("Hilfe");
		 	MenuItem getHelpOnline   = new MenuItem("Hilfe Online erhalten");
		 	MenuItem checkForUpdates = new MenuItem("Auf Updates überprüfen");
		 	menuHelp.getItems().addAll(getHelpOnline, checkForUpdates);
		 	//Create MenuItems
	 
		 	MenuBar menuBar = new MenuBar();
		 	menuBar.getMenus().addAll(menuOrganize, menuHelp);
		 	menu.getChildren().add(menuBar);
	 
    	 	//Buttons werden erstellt
    	 	ToolBar toolBar     = new ToolBar();
    	 	Button buttonImport = new Button("Import");
    	 	Button buttonSearch = new Button("Suchen");
    	 	toolBar.getItems().addAll(buttonImport, buttonSearch);
    
    	 
     	//Es werden Bilder/Graphiken anstelle der Buttons hinzugefügt
     	buttonImport.setGraphic(new ImageView("https://bit.ly/2DG1GtN"));
     	
      	//Bennenungen der Buttons anzeigen
    	 	Tooltip tooltipImport = new Tooltip("Import");
    	 	buttonImport.setTooltip(tooltipImport);
    	 	tooltipImport.setTextAlignment(TextAlignment.RIGHT);
     
    	 	Tooltip tooltipSearch = new Tooltip("Suchen");
    	 	buttonSearch.setTooltip(tooltipSearch);
    	 	tooltipSearch.setTextAlignment(TextAlignment.RIGHT);
          
    	 	SplitPane splitPaneTop = new SplitPane();
    	 	splitPaneTop.setOrientation(Orientation.VERTICAL);
    	 	splitPaneTop.getItems().addAll(menuBar, toolBar);
     
    
		     TitledPane firstTitledPane = new TitledPane();
		     firstTitledPane.setText("Alben");
		     VBox alben = new VBox(); //Hier Datei Baum
		     alben.getChildren().add(new TextField());
		     firstTitledPane.setContent(alben);
		      
		     TitledPane secondTitledPane = new TitledPane();
		     secondTitledPane.setText("Suchen/Filtern");
		     VBox suchen = new VBox();
		  
		        // create a textfield 
		        TextField b1 = new TextField("Suche"); 
		        TextField b2 = new TextField(" "); 
		        TextField b3 = new TextField(" "); 
		        TextField b4 = new TextField(" "); 
		        TextField b5 = new TextField(" "); 
		        TextField b6 = new TextField(" "); 
		        TextField b7 = new TextField(" "); 
		        
		  
		        // create a tile pane 
		        TilePane r = new TilePane(); 
		  
		        // create a label 
		        Label l1 = new Label(""); 
		  
		        // action event 
		        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
		            public void handle(ActionEvent e) 
		            { 
		            	 String input1 = b1.getText().trim();
		            	 String input2 = b2.getText().trim();
		            	 String input3 = b3.getText().trim();
		            	 String input4 = b4.getText().trim();
		            	 String input5 = b5.getText().trim();
		            	 String input6 = b6.getText().trim();
		            	 String input7 = b7.getText().trim();
		            	 
		            	 String suchWerte  = input1 + " " +input2 + " " +input3 + " " +input4 + " " +input5 + " " +input6 + " " +input7;
		                l1.setText(suchWerte); 
		            } 
		        }; 
		  
		        //Nachdem Enter betätigt wurden ist 
		        
		        b1.setOnAction(event); 
		        b2.setOnAction(event); 
		        b3.setOnAction(event); 
		        b4.setOnAction(event); 
		        b5.setOnAction(event); 
		        b6.setOnAction(event); 
		        b7.setOnAction(event); 
		        
		     suchen.getChildren().addAll(b1, b2, b3, b4,b5, b6, b7,r,l1);
		     secondTitledPane.setContent(suchen);
		     
		     TitledPane thirdTitledPane = new TitledPane();
		     thirdTitledPane.setText("Informationen");
		     VBox infos = new VBox();
		     infos.getChildren().add(new TextField());

		     thirdTitledPane.setContent(infos);
		     
		     VBox borderPaneLeft = new VBox();
		     borderPaneLeft.getChildren().addAll(firstTitledPane, secondTitledPane, thirdTitledPane);
		     
		    
		     
		     
		     FlowPane flow = new FlowPane();
		     flow.setVgap(8);
		     flow.setHgap(4);
		     flow.setPrefWrapLength(400); 
		
		     
		     ImageView[] imageView ;
		     Image [] image = new Image[10];
		     List<Image> AllImages = new LinkedList();
		     for (int i =0;i<image.length;i++){
		    	 image[i]=new Image("https://bit.ly/1GvKLBX");
		    	  AllImages.add(image[i]);
		     }
		     
		     imageView = new ImageView[AllImages.size()];
		     for (int j = 0; j < imageView.length; j++) {
		         imageView[j] = new ImageView(AllImages.get(j));
		         imageView[j].setFitHeight(150);
		         imageView[j].setFitWidth(150);
		         imageView[j].setSmooth(true);
		         imageView[j].setPreserveRatio(true);
		     }
		     
		    
		
		     Menu menuSave = new Menu("Speichern");
		     MenuItem saveAs = new MenuItem("Speichern Unter");
		     MenuItem movePic= new MenuItem("In Album Verschieben");
		     menuSave.getItems().addAll(saveAs, movePic);
		     //menuSave.getItems().add(new CheckMenuItem("Speichern Unter"));
		     //menuSave.getItems().add(new CheckMenuItem("In Album Verschieben"));
		     saveAs.setOnAction(new EventHandler<ActionEvent>() {
		     
		     	
		         @Override
		         public void handle(ActionEvent event) {
		             //zuaetzliches speichern in anderem Ordner);
		        	 
		         }
		     });
		     
		     movePic.setOnAction(new EventHandler<ActionEvent>() {
		     
		     	
		         @Override
		         public void handle(ActionEvent event) {
		             //Verschieben des Bildes in anderen Ordner
		         }
		     });
		     
		    
		     flow.getChildren().addAll(imageView);
		      
		      
			BorderPane borderPane = new BorderPane();
		 	borderPane.setTop(splitPaneTop);
		 	borderPane.setLeft(borderPaneLeft);
		 	borderPane.setCenter(flow);
		 	
		 	Scene scene = new Scene(borderPane);
		 	
		 	primaryStage.setScene(scene);
		    primaryStage.show();
	 }
	 catch(Exception e){
		 LOG.error("Fehler ist in der GUI-Main aufgetreten", e );
	    }
	 }

	public static void main(String[] args) {
    	Application.launch(args);
    }
}
