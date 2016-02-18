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
import javafx.scene.layout.BorderPane;
import storage.database.JenkinsDatabase;

/**
 * The {@link BuildWallDisplayImpl} provides a {@link BorderPane} to combine the {@link GridWallImpl} and
 * {@link BuildWallConfigurationPanelImpl}.
 */
public class BuildWallDisplayImpl extends BorderPane {
   
   private BuildWallConfigurationPanelImpl configurationPanel;
   
   /**
    * Constructs a new {@link BuildWallDisplayImpl}.
    * @param database the {@link JenkinsDatabase} associated.
    */
   public BuildWallDisplayImpl( JenkinsDatabase database ) {
      BuildWallConfiguration configuration = new BuildWallConfigurationImpl();
      setCenter( new GridWallImpl( configuration, database ) );
      configurationPanel = new BuildWallConfigurationPanelImpl( configuration );
   }//End Constructor
   
   /**
    * Method to turn the configuration panel on an off.
    */
   public void toggleConfiguration(){
      if ( !hasConfigurationTurnedOn() ) {
         setRight( configurationPanel );
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

}//End Class
