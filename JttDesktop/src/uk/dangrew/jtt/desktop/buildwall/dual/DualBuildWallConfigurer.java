/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.dual;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import uk.dangrew.jtt.desktop.buildwall.effects.flasher.ImageFlasherProperties;
import uk.dangrew.jtt.desktop.buildwall.effects.flasher.configuration.ImageFlasherConfigurationPanel;

/**
 * The {@link DualBuildWallConfigurer} is responsible for controlling the configuration panels
 * displayed as part of the {@link DualBuildWallDisplayImpl}.
 */
class DualBuildWallConfigurer {
   
   private final ScrollPane configurationScroller;
   private final BorderPane display;
   
   private final ImageFlasherProperties imageFlasherProperties;
   private final ImageFlasherConfigurationPanel imageFlasherPanel;
   
   /**
    * Constructs a new {@link DualBuildWallConfigurer}.
    * @param display the {@link BorderPane}, {@link DualBuildWallDisplayImpl}.
    * @param imageFlasherProperties the {@link ImageFlasherProperties} to configure.
    */
   DualBuildWallConfigurer( 
            BorderPane display, 
            ImageFlasherProperties imageFlasherProperties
   ) {
      this.display = display;
      this.configurationScroller = new ScrollPane();
      
      this.imageFlasherProperties = imageFlasherProperties;
      this.imageFlasherPanel = new ImageFlasherConfigurationPanel( "Failure Image Flash", imageFlasherProperties );
   }//End Constructor

   /**
    * Method to show the {@link ImageFlasherProperties} in a configuration panel.
    */
   void showImageFlasherConfiguration(){
      configurationScroller.setContent( imageFlasherPanel );
      display.setRight( configurationScroller );
   }//End Method
   
   /**
    * Method to hide the {@link BuildWallConfiguration} for whichever is currently showing.
    */
   void hideConfiguration(){
      display.setRight( null );
   }//End Method
   
   /**
    * Method to determine whether the configuration is currently showing.
    * @return true if on, false otherwise.
    */
   boolean isConfigurationShowing(){
      return display.getRight() != null;
   }//End Method
   
   ScrollPane scroller(){
      return configurationScroller;
   }//End Method
   
   ImageFlasherConfigurationPanel imageFlasherConfigurationPanel(){
      return imageFlasherPanel;
   }//End Method

   ImageFlasherProperties imageFlasherProperties(){
      return imageFlasherProperties;
   }//End Method
}//End Class
