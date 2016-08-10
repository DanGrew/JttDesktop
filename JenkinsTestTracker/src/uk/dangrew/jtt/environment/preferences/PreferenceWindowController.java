/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.environment.preferences;

import com.sun.javafx.application.PlatformImpl;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The {@link PreferenceWindowController} is responsible for controlling a
 * {@link DualBuildWallConfigurationWindow} in a separate {@link Stage}. Note that this requires
 * the {@link PlatformImpl} {@link Thread} therefore some of these calls are run later.
 */
public class PreferenceWindowController {
   
   static final double HEIGHT = 400;
   static final double WIDTH = 800;
   
   private Stage configurationWindowStage;
   
   /**
    * Constructs a new {@link PreferenceWindowController}.
    * {@link #associateWithConfiguration(BuildWallConfiguration, ImageFlasherProperties, BuildWallConfiguration)}
    * should be called to initialise this object. This is broken down to allow more flexibility with construction
    * since this is a heavy setup and sometimes may not be required.
    */
   public PreferenceWindowController() {}//End Constructor
   
   /**
    * Constructs a new {@link DualBuildWallConfigurationWindowController}.
    * @param configurationWindow the {@link Parent} to show in the window.
    */
   public void associateWithConfiguration(
            Parent configurationWindow
   ) {
         Scene configurationScene = new Scene( configurationWindow );
         PlatformImpl.runAndWait( () -> {
            configurationWindowStage = new Stage();
            configurationWindowStage.setWidth( WIDTH );
            configurationWindowStage.setHeight( HEIGHT );
            configurationWindowStage.setFullScreen( false );
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
   public void showConfigurationWindow(){
      verifyState();
      PlatformImpl.runLater( () -> { 
         configurationWindowStage.show();
         configurationWindowStage.toFront();
      } );
   }//End Method
   
   /**
    * Method to hide the {@link DualBuildWallConfigurationWindow}.
    */
   public void hideConfigurationWindow(){
      verifyState();
      PlatformImpl.runLater( () -> configurationWindowStage.hide() );
   }//End Method
   
   /**
    * Method to determine whether the {@link DualBuildWallConfigurationWindow} is showing.
    * @return true if showing, false otherwise.
    */
   public boolean isConfigurationWindowShowing(){
      verifyState();
      return configurationWindowStage.isShowing();
   }//End Method
   
   Stage stage(){
      return configurationWindowStage;
   }//End Method

}//End Class
