/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import uk.dangrew.kode.javafx.style.JavaFxStyle;

/**
 * {@link ConfigurationPanelDefaults} provides default styling and properties for the configuration panel.
 */
public class ConfigurationPanelDefaults {

   static final double LABEL_PERCENTAGE_WIDTH = 40;
   static final double CONTROLS_PERCENTAGE_WIDTH = 60;
   
   private final JavaFxStyle styling;
   
   /**
    * Constructs a new {@link ConfigurationPanelDefaults}.
    */
   public ConfigurationPanelDefaults() {
      this.styling = new JavaFxStyle();
   }//End Constructor
   
   /**
    * Method to configure the {@link javafx.scene.layout.ColumnConstraints} on the given {@link GridPane}.
    * @param grid the {@link GridPane} to apply constraints to.
    */
   public void configureColumnConstraints( GridPane grid ){
      styling.configureConstraintsForColumnPercentages( grid, LABEL_PERCENTAGE_WIDTH, CONTROLS_PERCENTAGE_WIDTH );
      grid.getColumnConstraints().forEach( c -> c.setHgrow( Priority.ALWAYS ) );
   }//End Method
   
}//End Class
