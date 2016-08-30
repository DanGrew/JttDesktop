/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.environment.main;

import org.controlsfx.control.HiddenSidesPane;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import uk.dangrew.jtt.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.environment.launch.LaunchOptions;
import uk.dangrew.jtt.environment.layout.CenterScreenWrapper;
import uk.dangrew.jtt.environment.preferences.PreferenceController;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.sd.viewer.basic.DigestViewer;

/**
 * The {@link EnvironmentWindow} is responsible for providing an overall frame to the 
 * application that hosts multiple tools and systems.
 */
public class EnvironmentWindow extends BorderPane {

   static final String NEW_VERSIONS_MESSAGE = "New versions of JTT are available. Click here to install...";
   
   private final CenterScreenWrapper centerWrapper;
   private final SystemConfiguration configuration;
   private final PreferenceController preferenceOpener;
   
   /**
    * Constructs a new {@link EnvironmentWindow}.
    * @param database the {@link JenkinsDatabase} used for launching the relevant system.
    * @param digest the {@link DigestViewer} for the system.
    */
   public EnvironmentWindow( SystemConfiguration configuration, JenkinsDatabase database, DigestViewer digest ) {
      this.configuration = configuration;
      this.preferenceOpener = new PreferenceController( configuration );
      
      EnvironmentMenuBar menuBar = new EnvironmentMenuBar();
      setTop( menuBar );
      centerWrapper = new CenterScreenWrapper( new LaunchOptions( this, configuration, database, digest ) );
      
      setCenter( centerWrapper );
   }//End Constructor
   
   /**
    * Method to set the content of the window, replacing the current content.
    * @param content the {@link Node} content.
    */
   public void setContent( Node content ){
      centerWrapper.setCenter( content );
   }//End Method
   
   PreferenceController preferenceOpener(){
      return preferenceOpener;
   }//End Method

}//End Class
