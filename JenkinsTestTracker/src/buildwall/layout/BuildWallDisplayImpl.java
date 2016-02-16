/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.layout;

import java.util.Optional;

import org.controlsfx.dialog.FontSelectorDialog;

import buildwall.configuration.BuildWallConfiguration;
import buildwall.configuration.BuildWallConfigurationImpl;
import buildwall.configuration.BuildWallConfigurationPanelImpl;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
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
      configurationPanel = new BuildWallConfigurationPanelImpl( 
            configuration, initial -> {
                  Optional< Font > result = new FontSelectorDialog( initial ).showAndWait();
                  if ( result.isPresent() ) return result.get();
                  else return null;
            }
      );
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
