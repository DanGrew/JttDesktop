/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.window;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import uk.dangrew.jtt.buildwall.configuration.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.BuildWallConfigurationPanelImpl;
import uk.dangrew.jtt.buildwall.effects.flasher.ImageFlasherProperties;
import uk.dangrew.jtt.buildwall.effects.flasher.configuration.ImageFlasherConfigurationPanel;

/**
 * The {@link DualBuildWallConfigurationWindow} provides a layout of {@link BuildWallConfigurationPanelImpl}s
 * and {@link ImageFlasherConfigurationPanel}s to allow separate configuration outside of the build wall.
 */
public class DualBuildWallConfigurationWindow extends GridPane {
   
   static final double CONFIG_PANEL_WIDTH_PERCENT = 66;
   
   private ScrollPane leftScroller;
   private ScrollPane rightScroller;
   
   private BuildWallConfigurationPanelImpl leftConfigPanel;
   private ImageFlasherConfigurationPanel imageConfigPanel;
   private BuildWallConfigurationPanelImpl rightConfigPanel;

   /**
    * Constructs a new {@link DualBuildWallConfigurationWindow}.
    * @param leftConfiguration the {@link BuildWallConfiguration} for the left wall.
    * @param imageFlasherProperties the {@link ImageFlasherProperties}.
    * @param rightConfiguration the {@link BuildWallConfiguration} for the right wall.
    */
   public DualBuildWallConfigurationWindow( 
            BuildWallConfiguration leftConfiguration, 
            ImageFlasherProperties imageFlasherProperties, 
            BuildWallConfiguration rightConfiguration 
   ) {
      applyColumnConstraints();
      applyConfigurationPanels( leftConfiguration, imageFlasherProperties, rightConfiguration );
   }//End Constructor

   /**
    * Method to apply the configuration panels to the window.
    * @param leftConfiguration the {@link BuildWallConfiguration} for the left wall.
    * @param imageFlasherProperties the {@link ImageFlasherProperties}.
    * @param rightConfiguration the {@link BuildWallConfiguration} for the right wall.
    */
   private void applyConfigurationPanels( 
            BuildWallConfiguration leftConfiguration, 
            ImageFlasherProperties imageFlasherProperties,
            BuildWallConfiguration rightConfiguration 
   ) {
      leftConfigPanel = new BuildWallConfigurationPanelImpl( "Left Wall", leftConfiguration );
      imageConfigPanel = new ImageFlasherConfigurationPanel( "Image Flasher", imageFlasherProperties );
      rightConfigPanel = new BuildWallConfigurationPanelImpl( "Right Wall", rightConfiguration );
      
      leftScroller = new ScrollPane( leftConfigPanel );
      rightScroller = new ScrollPane( rightConfigPanel );
      
      add( leftScroller, 0, 0 );
      add( imageConfigPanel, 1, 0 );
      add( rightScroller, 2, 0 );
   }//End Method

   /**
    * Method to apply the {@link ColumnConstraints} for the panels in the window.
    */
   private void applyColumnConstraints() {
      ColumnConstraints leftColumn = new ColumnConstraints();
      leftColumn.setPercentWidth( CONFIG_PANEL_WIDTH_PERCENT );
      
      ColumnConstraints imageFlasherColumn = new ColumnConstraints();
      imageFlasherColumn.setPercentWidth( CONFIG_PANEL_WIDTH_PERCENT );
      
      ColumnConstraints rightColumn = new ColumnConstraints();
      rightColumn.setPercentWidth( CONFIG_PANEL_WIDTH_PERCENT );
      
      getColumnConstraints().addAll( leftColumn, imageFlasherColumn, rightColumn );
   }//End Method

   BuildWallConfigurationPanelImpl leftConfigPanel() {
      return leftConfigPanel;
   }//End Method

   ImageFlasherConfigurationPanel imageConfigPanel() {
      return imageConfigPanel;
   }//End Method

   BuildWallConfigurationPanelImpl rightConfigPanel() {
      return rightConfigPanel;
   }//End Method
   
   ScrollPane leftScroller(){
      return leftScroller;
   }//End Method
   
   ScrollPane rightScroller(){
      return rightScroller;
   }//End Method
   
}//End Class
