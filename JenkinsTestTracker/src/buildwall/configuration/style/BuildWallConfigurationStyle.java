/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.configuration.style;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.spinner.DefensiveDoubleSpinnerValueFactory;
import javafx.spinner.DefensiveIntegerSpinnerValueFactory;
import javafx.spinner.DoublePropertySpinner;
import javafx.spinner.IntegerPropertySpinner;

/**
 * The {@link BuildWallConfigurationStyle} provides the common styling options for 
 * the {@link BuildWallConfigurationPanelImpl}.
 */
public class BuildWallConfigurationStyle {

   /**
    * Method to create a bold {@link Label}.
    * @param title the text in the {@link Label}.
    * @param fontSize the size of the {@link Font}.
    * @return the constructed {@link Label}.
    */
   public Label createBoldLabel( String title, double fontSize ) {
      Label label = new Label( title );
      Font existingFont = label.getFont();
      label.setFont( Font.font( existingFont.getFamily(), FontWeight.BOLD, FontPosture.REGULAR, fontSize ) );
      return label;
   }//End Method
   
   /**
    * Method to create a bold {@link Label}.
    * @param title the text in the {@link Label}.
    * @return the constructed {@link Label}.
    */
   public Label createBoldLabel( String title ) {
      return createBoldLabel( title, Font.getDefault().getSize() );
   }//End Method
   
   /**
    * Method to configure an {@link IntegerPropertySpinner} for the given property and range.
    * @param spinner the {@link IntegerPropertySpinner} to configure.
    * @param property the {@link IntegerProperty} to bind to.
    * @param min the min range.
    * @param max the max range.
    * @param step the step of increments.
    */
   public void configureIntegerSpinner( 
            IntegerPropertySpinner spinner, 
            IntegerProperty property, 
            int min, 
            int max,
            int step
   ){
      DefensiveIntegerSpinnerValueFactory factory = new DefensiveIntegerSpinnerValueFactory( min, max );
      factory.setAmountToStepBy( step );
      
      spinner.setValueFactory( factory );
      spinner.bindProperty( property );
      spinner.setMaxWidth( Double.MAX_VALUE );
      spinner.setEditable( true );
   }//End Method
   
   /**
    * Method to configure an {@link DoublePropertySpinner} for the given property and range.
    * @param spinner the {@link DoublePropertySpinner} to configure.
    * @param property the {@link DoubleProperty} to bind to.
    * @param min the min range.
    * @param max the max range.
    * @param step the step of increments.
    */
   public void configureDoubleSpinner( 
            DoublePropertySpinner spinner, 
            DoubleProperty property, 
            double min, 
            double max,
            double step
   ){
      DefensiveDoubleSpinnerValueFactory factory = new DefensiveDoubleSpinnerValueFactory( min, max );
      factory.setAmountToStepBy( step );
      
      spinner.setValueFactory( factory );
      spinner.bindProperty( property );
      spinner.setMaxWidth( Double.MAX_VALUE );
      spinner.setEditable( true );
   }//End Method
   
}//End Class
