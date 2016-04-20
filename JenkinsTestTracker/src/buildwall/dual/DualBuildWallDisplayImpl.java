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
import buildwall.effects.flasher.ImageFlasherImpl;
import buildwall.effects.flasher.ImageFlasherProperties;
import buildwall.effects.flasher.ImageFlasherPropertiesImpl;
import buildwall.effects.flasher.configuration.ImageFlasherConfigurationPanel;
import buildwall.effects.triggers.JobFailureTrigger;
import buildwall.layout.GridWallImpl;
import buildwall.panel.type.JobPanelDescriptionProviders;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import storage.database.JenkinsDatabase;

/**
 * The {@link DualBuildWallDisplayImpl} provides a display for two {@link GridWallImpl}s
 * that can have different {@link BuildWallConfiguration}s.
 */
public class DualBuildWallDisplayImpl extends StackPane {

   private final BorderPane buildWallPane;
   private final ImageFlasherImpl imageFlasher;
   private final DualBuildWallSplitter buildWallSplitter;
   private final DualBuildWallConfigurer buildWallConfigurer;
   private final DualBuildWallConfigurationWindowController configWindowController;
   
   /**
    * Constructs a new {@link BuildWallDisplayImpl}.
    * @param database the {@link JenkinsDatabase} associated.
    */
   public DualBuildWallDisplayImpl( JenkinsDatabase database ) {
      BuildWallConfiguration rightConfiguration = new BuildWallConfigurationImpl();
      rightConfiguration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Detailed );
      BuildWallConfiguration leftConfiguration = new BuildWallConfigurationImpl();
      leftConfiguration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Simple );
      leftConfiguration.numberOfColumns().set( 1 );
      
      new JobPolicyUpdater( database, rightConfiguration );
      new JobPolicyUpdater( database, leftConfiguration );
      
      GridWallImpl rightGridWall = new GridWallImpl( rightConfiguration, database );
      GridWallImpl leftGridWall = new GridWallImpl( leftConfiguration, database );
      
      buildWallSplitter = new DualBuildWallSplitter( leftGridWall, rightGridWall );
      buildWallPane = new BorderPane();
      buildWallPane.setCenter( buildWallSplitter );
      getChildren().add( buildWallPane );
      
      ImageFlasherProperties imageFlasherProperties = new ImageFlasherPropertiesImpl();
      
      imageFlasher = new ImageFlasherImpl( imageFlasherProperties );
      getChildren().add( imageFlasher );
      
      new JobFailureTrigger( database, imageFlasherProperties );
      
      buildWallConfigurer = new DualBuildWallConfigurer( buildWallPane, leftConfiguration, rightConfiguration, imageFlasherProperties );
      new DualBuildWallAutoHider( this, leftGridWall.emptyProperty(), rightGridWall.emptyProperty() );
      
      configWindowController = new DualBuildWallConfigurationWindowController( 
               leftConfiguration, imageFlasherProperties, rightConfiguration 
      );
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
      buildWallConfigurer.showRightConfiguration();
   }//End Method

   /**
    * Method to show the {@link BuildWallConfiguration} for the left {@link GridWallImpl}.
    */
   public void showLeftConfiguration(){
      buildWallConfigurer.showLeftConfiguration();
   }//End Method
   
   /**
    * Method to show the {@link ImageFlasherConfiguration} for the {@link DualBuildWallDisplayImpl}.
    */
   public void showImageFlasherConfiguration(){
      buildWallConfigurer.showImageFlasherConfiguration();
   }//End Method
   
   /**
    * Method to hide the {@link BuildWallConfiguration} for whichever is currently showing.
    */
   public void hideConfiguration(){
      buildWallConfigurer.hideConfiguration();
   }//End Method
   
   /**
    * Method to hide the right {@link GridWallImpl}. The configuration will be hidden is for this {@link GridWallImpl}.
    */
   public void hideRightWall() {
      buildWallConfigurer.hideRightWall();
      buildWallSplitter.hideRightWall();
   }//End Method
   
   /**
    * Method to show the right {@link GridWallImpl}, if not already showing.
    */
   public void showRightWall() {
      if ( isRightWallShowing() ) return;
      
      buildWallSplitter.showRightWall();
   }//End Method
   
   /**
    * Method to determine whether the right wall is currently showing.
    * @return true if showing, false otherwise.
    */
   public boolean isRightWallShowing(){
      return buildWallSplitter.isRightWallShowing();
   }//End Method
   
   /**
    * Method to hide the left {@link GridWallImpl}. The configuration will be hidden is for this {@link GridWallImpl}.
    */
   public void hideLeftWall() {
      buildWallConfigurer.hideLeftWall();
      buildWallSplitter.hideLeftWall();
   }//End Method
   
   /**
    * Method to show the left {@link GridWallImpl}, if not already showing.
    */
   public void showLeftWall() {
      if ( isLeftWallShowing() ) return;
      
      buildWallSplitter.showLeftWall();
   }//End Method
   
   /**
    * Method to determine whether the left wall is currently showing.
    * @return true if showing, false otherwise.
    */
   public boolean isLeftWallShowing(){
      return buildWallSplitter.isLeftWallShowing();
   }//End Method

   /**
    * Method to determine whether the configuration is currently showing.
    * @return true if on, false otherwise.
    */
   public boolean isConfigurationShowing(){
      return buildWallConfigurer.isConfigurationShowing();
   }//End Method
   
   /**
    * Method to show the {@link DualBuildWallConfigurationWindow}.
    */
   public void showConfigurationWindow(){
      configWindowController.showConfigurationWindow();
   }//End Method
   
   /**
    * Method to hide the {@link DualBuildWallConfigurationWindow}.
    */
   public void hideConfigurationWindow(){
      configWindowController.hideConfigurationWindow();
   }//End Method
   
   /**
    * Method to detemrine whether the {@link DualBuildWallConfigurationWindow} is showing.
    * @return true if showing, false otherwise.
    */
   public boolean isConfigurationWindowShowing(){
      return configWindowController.isConfigurationWindowShowing();
   }//End Method
   
   BorderPane buildWallPane(){
      return buildWallPane;
   }//End Method
   
   GridWallImpl rightGridWall(){
      return buildWallSplitter.rightGridWall();
   }//End Method
   
   GridWallImpl leftGridWall(){
      return buildWallSplitter.leftGridWall();
   }//End Method
   
   BuildWallConfigurationPanelImpl rightConfigurationPanel(){
      return buildWallConfigurer.rightConfigurationPanel();
   }//End Method
   
   BuildWallConfigurationPanelImpl leftConfigurationPanel(){
      return buildWallConfigurer.leftConfigurationPanel();
   }//End Method

   BuildWallConfiguration rightConfiguration() {
      return buildWallConfigurer.rightConfiguration();
   }//End Method
   
   BuildWallConfiguration leftConfiguration() {
      return buildWallConfigurer.leftConfiguration();
   }//End Method
   
   ImageFlasherProperties imageFlasherConfiguration(){
      return buildWallConfigurer.imageFlasherProperties();
   }//End Method

   DualBuildWallSplitter splitPane() {
      return buildWallSplitter;
   }//End Method
   
   ImageFlasherImpl imageFlasher(){
      return imageFlasher;
   }//End Method
   
   ImageFlasherConfigurationPanel imageFlasherConfigurationPanel(){
      return buildWallConfigurer.imageFlasherConfigurationPanel();
   }//End Method
   
}//End Class
