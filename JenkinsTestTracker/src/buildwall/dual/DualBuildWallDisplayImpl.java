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
import buildwall.layout.BuildWallDisplayImpl;
import buildwall.layout.GridWallImpl;
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
      this.leftConfiguration = new BuildWallConfigurationImpl();
      
      new JobPolicyUpdater( database, rightConfiguration );
      new JobPolicyUpdater( database, leftConfiguration );
      
      rightGridWall = new GridWallImpl( rightConfiguration, database );
      leftGridWall = new GridWallImpl( leftConfiguration, database );
      
      buildWallSplitter = new SplitPane( leftGridWall, rightGridWall );
      setCenter( buildWallSplitter );
      
      rightConfigurationPanel = new BuildWallConfigurationPanelImpl( rightConfiguration );
      leftConfigurationPanel = new BuildWallConfigurationPanelImpl( leftConfiguration );
      configurationScroller = new ScrollPane();
   }//End Constructor
   
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
      if ( buildWallSplitter.getItems().contains( rightGridWall ) ) return;
      
      buildWallSplitter.getItems().add( rightGridWall );
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
      if ( buildWallSplitter.getItems().contains( leftGridWall ) ) return;
      
      buildWallSplitter.getItems().add( 0, leftGridWall );
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
