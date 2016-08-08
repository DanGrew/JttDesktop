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
import uk.dangrew.jtt.environment.preferences.PreferenceOpener;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.sd.viewer.basic.DigestViewer;

/**
 * The {@link EnvironmentWindow} is responsible for providing an overall frame to the 
 * application that hosts multiple tools and systems.
 */
public class EnvironmentWindow extends BorderPane {

   private final HiddenSidesPane content;
   private final CenterScreenWrapper center;
   private final SystemConfiguration configuration;
   private final PreferenceOpener preferenceOpener;
   
   /**
    * Constructs a new {@link EnvironmentWindow}.
    * @param database the {@link JenkinsDatabase} used for launching the relevant system.
    * @param digest the {@link DigestViewer} for the system.
    */
   public EnvironmentWindow( JenkinsDatabase database, DigestViewer digest ) {
      this.content = new HiddenSidesPane();
      
      this.configuration = new SystemConfiguration();
      this.preferenceOpener = new PreferenceOpener( configuration );
      
      EnvironmentMenuBar menuBar = new EnvironmentMenuBar();
      content.setTop( menuBar );
      center = new CenterScreenWrapper( new LaunchOptions( this, configuration, database, digest ) );
      content.setContent( center );
      
      setCenter( content );
   }//End Constructor
   
   /**
    * Method to set the content of the window, replacing the current content.
    * @param content the {@link Node} content.
    */
   public void setContent( Node content ){
      center.setCenter( content );
   }//End Method
   
   HiddenSidesPane hiddenSidesPane(){
      return content;
   }//End Method
   
   PreferenceOpener preferenceOpener(){
      return preferenceOpener;
   }//End Method

}//End Class
