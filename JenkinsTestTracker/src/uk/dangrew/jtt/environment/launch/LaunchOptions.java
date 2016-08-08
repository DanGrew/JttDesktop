/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.environment.launch;

import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import uk.dangrew.jtt.buildwall.dual.DualBuildWallDisplayImpl;
import uk.dangrew.jtt.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.environment.main.EnvironmentWindow;
import uk.dangrew.jtt.releases.ReleasesWrapper;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.sd.viewer.basic.DigestViewer;

/**
 * {@link LaunchOptions} provided buttons to launch different elements of the system.
 */
public class LaunchOptions extends VBox {
   
   static final String MANAGEMENT_CONSOLE_BUTTON_TEXT = "Launch Management Console";
   static final String BUILD_WALL_BUTTON_TEXT = "Launch Build Wall";
   static final double BUTTON_SPACING = 10;
   
   private final EnvironmentWindow window;
   private final SystemConfiguration configuration;
   private final JenkinsDatabase database;
   private final DigestViewer digest;
   
   private final Button buildWallButton;
   private final Button managementConsoleButton;
   
   /**
    * Constructs a new {@link LaunchOptions}.
    * @param window the {@link EnvironmentWindow} to update based on launch.
    * @param configuration the {@link SystemConfiguration} to use in the launch.
    * @param database the {@link JenkinsDatabase} for the system.
    * @param digest the {@link DigestViewer} for the system.
    */
   public LaunchOptions( EnvironmentWindow window, SystemConfiguration configuration, JenkinsDatabase database, DigestViewer digest ) {
      this.window = window;
      this.configuration = configuration;
      this.database = database;
      this.digest = digest;
      
      this.buildWallButton = new Button( BUILD_WALL_BUTTON_TEXT );
      this.buildWallButton.setMaxWidth( Double.MAX_VALUE );
      this.buildWallButton.setOnAction( event -> launchBuildWall() );
      getChildren().add( this.buildWallButton );
      
      this.managementConsoleButton = new Button( MANAGEMENT_CONSOLE_BUTTON_TEXT );
      this.managementConsoleButton.setMaxWidth( Double.MAX_VALUE );
      this.managementConsoleButton.setDisable( true );
      getChildren().add( this.managementConsoleButton );
      
      setSpacing( BUTTON_SPACING );
   }//End Constructor

   /**
    * Method to launch the {@link DualBuildWallDisplayImpl} and place it in the {@link EnvironmentWindow}.
    */
   private void launchBuildWall(){
      DualBuildWallDisplayImpl dualWall = new DualBuildWallDisplayImpl( database, configuration );
      BorderPane digestWrapper = new BorderPane( dualWall );
      
      ReleasesWrapper releaseNotifier = new ReleasesWrapper();
      releaseNotifier.setContent( digestWrapper );
      
      digestWrapper.setTop( new TitledPane( "System Digest", digest ) );
      dualWall.initialiseContextMenu();
      window.setContent( releaseNotifier );
      
      releaseNotifier.maxWidthProperty().bind( window.widthProperty() );
      releaseNotifier.minWidthProperty().bind( window.widthProperty() );
      releaseNotifier.maxHeightProperty().bind( window.heightProperty() );
      releaseNotifier.minHeightProperty().bind( window.heightProperty() );
   }//End Method

   Button managementConsoleButton() {
      return managementConsoleButton;
   }//End Method
   
   Button buildWallButton() {
      return buildWallButton;
   }//End Method
}//End Class
