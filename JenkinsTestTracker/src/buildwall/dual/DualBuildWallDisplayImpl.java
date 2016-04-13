/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.dual;

import buildwall.configuration.BuildWallConfiguration;
import buildwall.configuration.BuildWallConfigurationImpl;
import buildwall.configuration.BuildWallConfigurationPanelImpl;
import buildwall.configuration.updating.JobPolicyUpdater;
import buildwall.layout.GridWallImpl;
import buildwall.panel.type.JobPanelDescriptionProviders;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import storage.database.JenkinsDatabase;

/**
 * The {@link DualBuildWallDisplayImpl} provides a display for two {@link GridWallImpl}s
 * that can have different {@link BuildWallConfiguration}s.
 */
public class DualBuildWallDisplayImpl extends BorderPane {

   private ScrollPane configurationScroller;
   private SplitPane buildWallSplitter;
   
   private GridWallImpl rightGridWall;
   private GridWallImpl leftGridWall;
   private BuildWallConfiguration rightConfiguration;
   private BuildWallConfigurationPanelImpl rightConfigurationPanel;
   
   private BuildWallConfiguration leftConfiguration;
   private BuildWallConfigurationPanelImpl leftConfigurationPanel;
   
   /**
    * Constructs a new {@link BuildWallDisplayImpl}.
    * @param database the {@link JenkinsDatabase} associated.
    */
   public DualBuildWallDisplayImpl( JenkinsDatabase database ) {
      this.rightConfiguration = new BuildWallConfigurationImpl();
      this.rightConfiguration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Detailed );
      this.leftConfiguration = new BuildWallConfigurationImpl();
      this.leftConfiguration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Simple );
      this.leftConfiguration.numberOfColumns().set( 1 );
      
      new JobPolicyUpdater( database, rightConfiguration );
      new JobPolicyUpdater( database, leftConfiguration );
      
      rightGridWall = new GridWallImpl( rightConfiguration, database );
      leftGridWall = new GridWallImpl( leftConfiguration, database );
      
      buildWallSplitter = new SplitPane( leftGridWall, rightGridWall );
      setCenter( buildWallSplitter );
      
      rightConfigurationPanel = new BuildWallConfigurationPanelImpl( "Right Wall Configuration", rightConfiguration );
      leftConfigurationPanel = new BuildWallConfigurationPanelImpl( "Left Wall Configuration", leftConfiguration );
      configurationScroller = new ScrollPane();
      
      new DualBuildWallAutoHider( this, leftGridWall.emptyProperty(), rightGridWall.emptyProperty() );
   }//End Constructor
   
   /**
    * Method to initialise the {@link DualBuildWallContextMenuOpener} for the display. This is a separate
    * initialisation requirement as the {@link DualBuildWallContextMenu} has a dependency on the initialisation
    * of the {@link DualBuildWallDisplayImpl}. This can be called at any point, but if the {@link DualBuildWallDisplayImpl}
    * is not attached to its parent, then the system digest cannot be found and used in the menu. 
    */
   public void initialiseContextMenu(){
      setOnContextMenuRequested( new DualBuildWallContextMenuOpener( this ) );
   }//End Method
   
   /**
    * Method to show the {@link BuildWallConfiguration} for the right {@link GridWallImpl}.
    */
   public void showRightConfiguration(){
      configurationScroller.setContent( rightConfigurationPanel );
      setRight( configurationScroller );
   }//End Method

   /**
    * Method to show the {@link BuildWallConfiguration} for the left {@link GridWallImpl}.
    */
   public void showLeftConfiguration(){
      configurationScroller.setContent( leftConfigurationPanel );
      setRight( configurationScroller );
   }//End Method
   
   /**
    * Method to hide the {@link BuildWallConfiguration} for whichever is currently showing.
    */
   public void hideConfiguration(){
      setRight( null );
   }//End Method
   
   /**
    * Method to hide the right {@link GridWallImpl}. The configuration will be hidden is for this {@link GridWallImpl}.
    */
   public void hideRightWall() {
      if ( isConfigurationShowing() && configurationScroller.getContent() == rightConfigurationPanel ) {
         hideConfiguration();
      }
      buildWallSplitter.getItems().remove( rightGridWall );
   }//End Method
   
   /**
    * Method to show the right {@link GridWallImpl}, if not already showing.
    */
   public void showRightWall() {
      if ( isRightWallShowing() ) return;
      
      buildWallSplitter.getItems().add( rightGridWall );
   }//End Method
   
   /**
    * Method to determine whether the right wall is currently showing.
    * @return true if showing, false otherwise.
    */
   public boolean isRightWallShowing(){
      return buildWallSplitter.getItems().contains( rightGridWall );
   }//End Method
   
   /**
    * Method to hide the left {@link GridWallImpl}. The configuration will be hidden is for this {@link GridWallImpl}.
    */
   public void hideLeftWall() {
      if ( isConfigurationShowing() && configurationScroller.getContent() == leftConfigurationPanel ) {
         hideConfiguration();
      }
      buildWallSplitter.getItems().remove( leftGridWall );
   }//End Method
   
   /**
    * Method to show the left {@link GridWallImpl}, if not already showing.
    */
   public void showLeftWall() {
      if ( isLeftWallShowing() ) return;
      
      buildWallSplitter.getItems().add( 0, leftGridWall );
   }//End Method
   
   /**
    * Method to determine whether the left wall is currently showing.
    * @return true if showing, false otherwise.
    */
   public boolean isLeftWallShowing(){
      return buildWallSplitter.getItems().contains( leftGridWall );
   }//End Method

   /**
    * Method to determine whether the configuration is currently showing.
    * @return true if on, false otherwise.
    */
   public boolean isConfigurationShowing(){
      return getRight() != null;
   }//End Method
   
   GridWallImpl rightGridWall(){
      return rightGridWall;
   }//End Method
   
   GridWallImpl leftGridWall(){
      return leftGridWall;
   }//End Method
   
   BuildWallConfigurationPanelImpl rightConfigurationPanel(){
      return rightConfigurationPanel;
   }//End Method
   
   BuildWallConfigurationPanelImpl leftConfigurationPanel(){
      return leftConfigurationPanel;
   }//End Method

   BuildWallConfiguration rightConfiguration() {
      return rightConfiguration;
   }//End Method
   
   BuildWallConfiguration leftConfiguration() {
      return leftConfiguration;
   }//End Method
   
}//End Class
