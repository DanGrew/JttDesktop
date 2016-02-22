/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.configuration.style;

import buildwall.configuration.BuildWallConfigurationPanelImpl;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * The {@link BuildWallConfigurationStyle} provides the common styling options for 
 * the {@link BuildWallConfigurationPanelImpl}.
 */
public class BuildWallConfigurationStyle {

   /**
    * Method to create a bold {@link Label}.
    * @param title the text in the {@link Label}.
    * @return the constructed {@link Label}.
    */
   public Label createBoldLabel( String title ) {
      Label label = new Label( title );
      Font existingFont = label.getFont();
      label.setFont( Font.font( existingFont.getFamily(), FontWeight.BOLD, FontPosture.REGULAR, existingFont.getSize() ) );
      return label;
   }//End Method
}//End Class
