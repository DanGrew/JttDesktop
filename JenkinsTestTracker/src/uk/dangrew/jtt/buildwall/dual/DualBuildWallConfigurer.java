/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.dual;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import uk.dangrew.jtt.buildwall.configuration.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.BuildWallConfigurationPanelImpl;
import uk.dangrew.jtt.buildwall.effects.flasher.ImageFlasherProperties;
import uk.dangrew.jtt.buildwall.effects.flasher.configuration.ImageFlasherConfigurationPanel;

/**
 * The {@link DualBuildWallConfigurer} is responsible for controlling the configuration panels
 * displayed as part of the {@link DualBuildWallDisplayImpl}.
 */
class DualBuildWallConfigurer {
   
   private final ScrollPane configurationScroller;
   private final BorderPane display;
   
   private final BuildWallConfiguration rightConfiguration;
   private final BuildWallConfigurationPanelImpl rightConfigurationPanel;
   
   private final BuildWallConfiguration leftConfiguration;
   private final BuildWallConfigurationPanelImpl leftConfigurationPanel;
   
   private final ImageFlasherProperties imageFlasherProperties;
   private final ImageFlasherConfigurationPanel imageFlasherPanel;
   
   /**
    * Constructs a new {@link DualBuildWallConfigurer}.
    * @param display the {@link BorderPane}, {@link DualBuildWallDisplayImpl}.
    * @param leftConfiguration the left {@link uk.dangrew.jtt.buildwall.layout.GridWallImpl} {@link BuildWallConfiguration}.
    * @param rightConfiguration the right {@link uk.dangrew.jtt.buildwall.layout.GridWallImpl} {@link BuildWallConfiguration}.
    * @param imageFlasherProperties the {@link ImageFlasherProperties} to configure.
    */
   DualBuildWallConfigurer( 
            BorderPane display, 
            BuildWallConfiguration leftConfiguration, 
            BuildWallConfiguration rightConfiguration,
            ImageFlasherProperties imageFlasherProperties
   ) {
      this.display = display;
      this.configurationScroller = new ScrollPane();
      
      this.leftConfiguration = leftConfiguration;
      this.rightConfiguration = rightConfiguration;
      this.imageFlasherProperties = imageFlasherProperties;
      
      this.leftConfigurationPanel = new BuildWallConfigurationPanelImpl( "Left Wall Configuration", leftConfiguration );
      this.rightConfigurationPanel = new BuildWallConfigurationPanelImpl( "Right Wall Configuration", rightConfiguration );
      this.imageFlasherPanel = new ImageFlasherConfigurationPanel( "Failure Image Flash", imageFlasherProperties );
   }//End Constructor

   /**
    * Method to show the {@link BuildWallConfiguration} for the right {@link GridWallImpl}.
    */
   void showRightConfiguration(){
      configurationScroller.setContent( rightConfigurationPanel );
      display.setRight( configurationScroller );
   }//End Method

   /**
    * Method to show the {@link BuildWallConfiguration} for the left {@link GridWallImpl}.
    */
   void showLeftConfiguration(){
      configurationScroller.setContent( leftConfigurationPanel );
      display.setRight( configurationScroller );
   }//End Method
   
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
    * Method to hide the right {@link GridWallImpl}. The configuration will be hidden is for this {@link GridWallImpl}.
    */
   void hideRightWall() {
      if ( isConfigurationShowing() && configurationScroller.getContent() == rightConfigurationPanel ) {
         hideConfiguration();
      }
   }//End Method
   
   /**
    * Method to hide the left {@link GridWallImpl}. The configuration will be hidden is for this {@link GridWallImpl}.
    */
   void hideLeftWall() {
      if ( isConfigurationShowing() && configurationScroller.getContent() == leftConfigurationPanel ) {
         hideConfiguration();
      }
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
   
   BuildWallConfigurationPanelImpl rightConfigurationPanel(){
      return rightConfigurationPanel;
   }//End Method
   
   BuildWallConfigurationPanelImpl leftConfigurationPanel(){
      return leftConfigurationPanel;
   }//End Method
   
   ImageFlasherConfigurationPanel imageFlasherConfigurationPanel(){
      return imageFlasherPanel;
   }//End Method

   BuildWallConfiguration rightConfiguration() {
      return rightConfiguration;
   }//End Method
   
   BuildWallConfiguration leftConfiguration() {
      return leftConfiguration;
   }//End Method
   
   ImageFlasherProperties imageFlasherProperties(){
      return imageFlasherProperties;
   }//End Method
}//End Class
