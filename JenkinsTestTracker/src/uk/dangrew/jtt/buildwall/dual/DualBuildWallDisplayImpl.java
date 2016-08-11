/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.dual;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import uk.dangrew.jtt.buildwall.configuration.persistence.buildwall.BuildWallConfigurationSessions;
import uk.dangrew.jtt.buildwall.configuration.persistence.dualwall.DualWallConfigurationSessions;
import uk.dangrew.jtt.buildwall.configuration.updating.JobPolicyUpdater;
import uk.dangrew.jtt.buildwall.effects.flasher.ImageFlasherImpl;
import uk.dangrew.jtt.buildwall.effects.flasher.ImageFlasherProperties;
import uk.dangrew.jtt.buildwall.effects.flasher.ImageFlasherPropertiesImpl;
import uk.dangrew.jtt.buildwall.effects.flasher.configuration.ImageFlasherConfigurationPanel;
import uk.dangrew.jtt.buildwall.effects.triggers.JobFailureTrigger;
import uk.dangrew.jtt.buildwall.layout.GridWallImpl;
import uk.dangrew.jtt.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.environment.preferences.PreferenceWindowController;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * The {@link DualBuildWallDisplayImpl} provides a display for two {@link GridWallImpl}s
 * that can have different {@link BuildWallConfiguration}s.
 */
public class DualBuildWallDisplayImpl extends StackPane {
   
   private final JenkinsDatabase database;

   private final SystemConfiguration systemConfiguration;
   
   private final ImageFlasherProperties imageFlasherProperties;

   private BorderPane buildWallPane;
   private DualBuildWallSplitter buildWallSplitter;
   private GridWallImpl leftGridWall;
   private GridWallImpl rightGridWall;
   private ImageFlasherImpl imageFlasher;
   private DualBuildWallConfigurer buildWallConfigurer;
   
   /**
    * Constructs a new {@link BuildWallDisplayImpl}.
    * @param database the {@link JenkinsDatabase} associated.
    * @param systemConfiguration the {@link SystemConfiguration}.
    */
   public DualBuildWallDisplayImpl( 
            JenkinsDatabase database, 
            SystemConfiguration systemConfiguration 
   ) {
      this.database = database;
      this.systemConfiguration = systemConfiguration;
      this.imageFlasherProperties = new ImageFlasherPropertiesImpl();
      
      applyPolicyUpdaters();
      createAndArrangeWalls();
      provideImageFlasherOverlay();
      applyConfigurationControls();
      applyEffectsTriggers();
   }//End Constructor

   /**
    * Method to set up the {@link JobPolicyUpdater}s.
    */
   private void applyPolicyUpdaters(){
      new JobPolicyUpdater( database, systemConfiguration.getRightConfiguration() );
      new JobPolicyUpdater( database, systemConfiguration.getLeftConfiguration() );
   }//End Method
   
   /**
    * Method to create the {@link GridWallImpl}s and arrange them.
    */
   private void createAndArrangeWalls(){
      rightGridWall = new GridWallImpl( systemConfiguration.getRightConfiguration(), database );
      leftGridWall = new GridWallImpl( systemConfiguration.getLeftConfiguration(), database );
      
      buildWallSplitter = new DualBuildWallSplitter( systemConfiguration.getDualConfiguration(), leftGridWall, rightGridWall );
      buildWallPane = new BorderPane();
      buildWallPane.setCenter( buildWallSplitter );
      getChildren().add( buildWallPane );
   }//End Method
   
   /**
    * Method to set up the {@link ImageFlasherImpl} and add it to this.
    */
   private void provideImageFlasherOverlay(){
      imageFlasher = new ImageFlasherImpl( imageFlasherProperties );
      getChildren().add( imageFlasher );  
   }//End Method
   
   /**
    * Method to apply the controls for configuration.
    */
   private void applyConfigurationControls(){
      buildWallConfigurer = new DualBuildWallConfigurer( buildWallPane, imageFlasherProperties );
      new DualBuildWallAutoHider( this, leftGridWall.emptyProperty(), rightGridWall.emptyProperty() );
   }//End Method
   
   /**
    * Method to apply the effects triggers.
    */
   private void applyEffectsTriggers(){
      new JobFailureTrigger( database, imageFlasherProperties );
   }//End Method
   
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
   
   BorderPane buildWallPane(){
      return buildWallPane;
   }//End Method
   
   GridWallImpl rightGridWall(){
      return buildWallSplitter.rightGridWall();
   }//End Method
   
   GridWallImpl leftGridWall(){
      return buildWallSplitter.leftGridWall();
   }//End Method
   
   SystemConfiguration systemConfiguration(){
      return systemConfiguration;
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
