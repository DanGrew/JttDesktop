/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.layout;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import uk.dangrew.jtt.buildwall.configuration.components.BuildWallConfigurationPanelImpl;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfigurationImpl;
import uk.dangrew.jtt.buildwall.configuration.updating.JobPolicyUpdater;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;

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
