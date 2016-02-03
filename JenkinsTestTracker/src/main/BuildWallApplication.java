/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package main;

import buildwall.layout.BuildWallDisplayImpl;
import buildwall.layout.GridWallImpl;
import graphics.DecoupledPlatformImpl;
import graphics.PlatformDecouplerImpl;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import model.jobs.JenkinsJobImpl;
import storage.database.JenkinsDatabase;
import storage.database.JenkinsDatabaseImpl;
import styling.SystemStyling;

/**
 * The {@link BuildWallApplication} provides an entry point to the {@link GridWallImpl}.
 */
public class BuildWallApplication extends Application {
   
   static KeyCombination configurationCombination = new KeyCodeCombination( KeyCode.O, KeyCombination.META_DOWN );
   static ObjectProperty< Stage > launchedStageProperty = new SimpleObjectProperty< Stage >( null );

   public static void main( String[] args ) {
      BuildWallApplication.launch();
   }// End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void start( Stage stage ) throws Exception {
      SystemStyling.initialise();
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      
      JenkinsDatabase database = new JenkinsDatabaseImpl();
      database.store( new JenkinsJobImpl( "Project Build" ) );
      database.store( new JenkinsJobImpl( "Subset A" ) );
      database.store( new JenkinsJobImpl( "Subset B" ) );
      database.store( new JenkinsJobImpl( "Subset C" ) );
      database.store( new JenkinsJobImpl( "Units (Legacy)" ) );
      database.store( new JenkinsJobImpl( "Long Tests" ) );
      database.store( new JenkinsJobImpl( "Capacity Tests" ) );
      database.store( new JenkinsJobImpl( "Integration Tests" ) );
      database.store( new JenkinsJobImpl( "Configurable Build1" ) );
      database.store( new JenkinsJobImpl( "Configurable Build2" ) );
      database.store( new JenkinsJobImpl( "Configurable Build3" ) );
      database.store( new JenkinsJobImpl( "Configurable Build4" ) );
      database.store( new JenkinsJobImpl( "Configurable Build5" ) );
      database.store( new JenkinsJobImpl( "Configurable Build6" ) );
      database.store( new JenkinsJobImpl( "Configurable Build7" ) );
      
      BuildWallDisplayImpl display = new BuildWallDisplayImpl( database );
      Scene scene = new Scene( display, 1200, 800 );
      stage.setScene( scene );
      
      scene.getAccelerators().put( 
              configurationCombination,
              () -> display.toggleConfiguration()
      );
      stage.show();
      
      launchedStageProperty.set( stage );
   }// End Method
   
}// End Class
