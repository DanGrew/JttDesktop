/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.dual;

import com.sun.javafx.application.PlatformImpl;

import javafx.scene.Scene;
import javafx.stage.Stage;
import uk.dangrew.jtt.buildwall.configuration.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.window.DualBuildWallConfigurationWindow;
import uk.dangrew.jtt.buildwall.effects.flasher.ImageFlasherProperties;

/**
 * The {@link DualBuildWallConfigurationWindowController} is responsible for controlling a
 * {@link DualBuildWallConfigurationWindow} in a separate {@link Stage}. Note that this requires
 * the {@link PlatformImpl} {@link Thread} therefore some of these calls are run later.
 */
public class DualBuildWallConfigurationWindowController {
   
   private Stage configurationWindowStage;
   
   /**
    * Constructs a new {@link DualBuildWallConfigurationWindowController}.
    * {@link #associateWithConfiguration(BuildWallConfiguration, ImageFlasherProperties, BuildWallConfiguration)}
    * should be called to initialise this object. This is broken down to allow more flexibility with construction
    * since this is a heavy setup and sometimes may not be required.
    */
   DualBuildWallConfigurationWindowController() {}//End Constructor
   
   /**
    * Constructs a new {@link DualBuildWallConfigurationWindowController}.
    * @param leftConfiguration the {@link BuildWallConfiguration} for the left wall.
    * @param imageFlasherProperties the {@link ImageFlasherProperties}.
    * @param rightConfiguration the {@link BuildWallConfiguration} for the right wall.
    */
   void associateWithConfiguration(
            BuildWallConfiguration leftConfiguration,
            ImageFlasherProperties imageFlasherProperties,
            BuildWallConfiguration rightConfiguration
   ) {
         DualBuildWallConfigurationWindow configurationWindow = new DualBuildWallConfigurationWindow( 
                  leftConfiguration, imageFlasherProperties, rightConfiguration 
         );
         Scene configurationScene = new Scene( configurationWindow );
         PlatformImpl.runAndWait( () -> {
            configurationWindowStage = new Stage();
            configurationWindowStage.setScene( configurationScene );
         } );
   }//End Constructor
   
   /**
    * Method to verify that the controller is associated with the configuration it needs.
    */
   private void verifyState(){
      if ( configurationWindowStage == null ) {
         throw new IllegalStateException( "Controller has not be associated." );
      }
   }//End Method
   
   /**
    * Method to show the {@link DualBuildWallConfigurationWindow}.
    */
   void showConfigurationWindow(){
      verifyState();
      PlatformImpl.runLater( () -> configurationWindowStage.show() );
   }//End Method
   
   /**
    * Method to hide the {@link DualBuildWallConfigurationWindow}.
    */
   void hideConfigurationWindow(){
      verifyState();
      PlatformImpl.runLater( () -> configurationWindowStage.hide() );
   }//End Method
   
   /**
    * Method to determine whether the {@link DualBuildWallConfigurationWindow} is showing.
    * @return true if showing, false otherwise.
    */
   boolean isConfigurationWindowShowing(){
      verifyState();
      return configurationWindowStage.isShowing();
   }//End Method
   
   Stage stage(){
      return configurationWindowStage;
   }//End Method

}//End Class
