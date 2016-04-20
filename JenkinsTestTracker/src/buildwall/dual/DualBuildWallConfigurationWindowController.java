/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.dual;

import com.sun.javafx.application.PlatformImpl;

import buildwall.configuration.BuildWallConfiguration;
import buildwall.configuration.window.DualBuildWallConfigurationWindow;
import buildwall.effects.flasher.ImageFlasherProperties;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The {@link DualBuildWallConfigurationWindowController} is responsible for controlling a
 * {@link DualBuildWallConfigurationWindow} in a separate {@link Stage}. Note that this requires
 * the {@link PlatformImpl} {@link Thread} therefore some of these calls are run later.
 */
public class DualBuildWallConfigurationWindowController {
   
   private Stage configurationWindowStage;
   
   /**
    * Constructs a new {@link DualBuildWallConfigurationWindowController}.
    * @param leftConfiguration the {@link BuildWallConfiguration} for the left wall.
    * @param imageFlasherProperties the {@link ImageFlasherProperties}.
    * @param rightConfiguration the {@link BuildWallConfiguration} for the right wall.
    */
   DualBuildWallConfigurationWindowController(
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
    * Method to show the {@link DualBuildWallConfigurationWindow}.
    */
   void showConfigurationWindow(){
      PlatformImpl.runLater( () -> configurationWindowStage.show() );
   }//End Method
   
   /**
    * Method to hide the {@link DualBuildWallConfigurationWindow}.
    */
   void hideConfigurationWindow(){
      PlatformImpl.runLater( () -> configurationWindowStage.hide() );
   }//End Method
   
   /**
    * Method to detemrine whether the {@link DualBuildWallConfigurationWindow} is showing.
    * @return true if showing, false otherwise.
    */
   boolean isConfigurationWindowShowing(){
      return configurationWindowStage.isShowing();
   }//End Method
   
   Stage stage(){
      return configurationWindowStage;
   }//End Method

}//End Class
