/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.layout;

import buildwall.configuration.BuildWallConfiguration;
import buildwall.configuration.BuildWallConfigurationImpl;
import buildwall.configuration.BuildWallConfigurationPanelImpl;
import buildwall.configuration.updating.JobPolicyUpdater;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import storage.database.JenkinsDatabase;

/**
 * The {@link BuildWallDisplayImpl} provides a {@link BorderPane} to combine the {@link GridWallImpl} and
 * {@link BuildWallConfigurationPanelImpl}.
 */
public class BuildWallDisplayImpl extends BorderPane {
   
   private ScrollPane configurationScroller;
   private BuildWallConfiguration configuration;
   private BuildWallConfigurationPanelImpl configurationPanel;
   
   /**
    * Constructs a new {@link BuildWallDisplayImpl}.
    * @param database the {@link JenkinsDatabase} associated.
    */
   public BuildWallDisplayImpl( JenkinsDatabase database ) {
      this.configuration = new BuildWallConfigurationImpl();
      new JobPolicyUpdater( database, configuration );
      
      setCenter( new GridWallImpl( configuration, database ) );
      configurationPanel = new BuildWallConfigurationPanelImpl( "Wall Configuration", configuration );
      configurationScroller = new ScrollPane( configurationPanel );
   }//End Constructor
   
   /**
    * Method to turn the configuration panel on an off.
    */
   public void toggleConfiguration(){
      if ( !hasConfigurationTurnedOn() ) {
         setRight( configurationScroller );
      } else {
         setRight( null );
      }
   }//End Method
   
   /**
    * Method to determine whether the configuration is currently showing.
    * @return true if on, false otherwise.
    */
   public boolean hasConfigurationTurnedOn(){
      return getRight() != null;
   }//End Method
   
   BuildWallConfiguration configuration(){
      return configuration;
   }//End Method

}//End Class
