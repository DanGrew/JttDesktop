/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.components;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import uk.dangrew.jtt.buildwall.configuration.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.style.BuildWallConfigurationStyle;

/**
 * The {@link ColoursPanel} provides a panel for configuring the colours in the system and 
 * on an associated {@link BuildWallConfiguration}.
 */
public class ColoursPanel extends GridPane {
   
   private final Label jobNameColourLabel;
   private final Label buildNumberColourLabel;
   private final Label completionEstimateColourLabel;
   private final Label detailColourLabel;
   
   private final ColorPicker jobNameColourPicker;
   private final ColorPicker buildNumberColourPicker;
   private final ColorPicker completionEstimateColourPicker;
   private final ColorPicker detailColourPicker;

   /**
    * Constructs a new {@link ColoursPanel}.
    * @param configuration the {@link BuildWallConfiguration} to configure.
    */
   public ColoursPanel( BuildWallConfiguration configuration ) {
      this( configuration, new BuildWallConfigurationStyle() );
   }//End Constructor
   
   /**
    * Constructs a new {@link ColoursPanel}.
    * @param configuration the {@link BuildWallConfiguration} to configure.
    * @param styling the {@link BuildWallConfigurationStyle} to apply.
    */
   ColoursPanel( BuildWallConfiguration configuration, BuildWallConfigurationStyle styling ) {
      jobNameColourLabel = styling.createBoldLabel( "Job Name" );
      add( jobNameColourLabel, 0, 0 );
      
      jobNameColourPicker = new ColorPicker();
      configureColorPicker( jobNameColourPicker, configuration.jobNameColour() );
      add( jobNameColourPicker, 1, 0 );
      
      buildNumberColourLabel = styling.createBoldLabel( "Build Number" );
      add( buildNumberColourLabel, 0, 1 );
      
      buildNumberColourPicker = new ColorPicker();
      configureColorPicker( buildNumberColourPicker, configuration.buildNumberColour() );
      add( buildNumberColourPicker, 1, 1 );
      
      completionEstimateColourLabel = styling.createBoldLabel( "Build Time" );
      add( completionEstimateColourLabel, 0, 2 );
      
      completionEstimateColourPicker = new ColorPicker();
      configureColorPicker( completionEstimateColourPicker, configuration.completionEstimateColour() );
      add( completionEstimateColourPicker, 1, 2 );
      
      detailColourLabel = styling.createBoldLabel( "Detail" );
      add( detailColourLabel, 0, 3 );
      
      detailColourPicker = new ColorPicker();
      configureColorPicker( detailColourPicker, configuration.detailColour() );
      add( detailColourPicker, 1, 3 );
      
      styling.configureColumnConstraints( this );
   }//End Constructor
   
   /**
    * Method to configure a {@link ColorPicker} to synchronise with the given property.
    * @param colorPicker the {@link ColorPicker} for changing the {@link Color}.
    * @param colorProperty the property being configured.
    */
   private void configureColorPicker( ColorPicker colorPicker, ObjectProperty< Color > colorProperty ){
      colorPicker.setMaxWidth( Double.MAX_VALUE );
      colorPicker.valueProperty().set( colorProperty.get() );
      colorPicker.valueProperty().addListener( ( source, old, updated ) -> colorProperty.set( updated ) );
      colorProperty.addListener( ( source, old, updated ) -> colorPicker.valueProperty().set( updated ) );
   }//End Method
   
   ColorPicker jobNameColourPicker() {
      return jobNameColourPicker;
   }//End Method

   ColorPicker buildNumberColourPicker() {
      return buildNumberColourPicker;
   }//End Method

   ColorPicker completionEstimateColourPicker() {
      return completionEstimateColourPicker;
   }//End Method
   
   ColorPicker detailColourPicker() {
      return detailColourPicker;
   }//End Method

   Labeled jobNameColourLabel() {
      return jobNameColourLabel;
   }//End Method

   Labeled buildNumberColourLabel() {
      return buildNumberColourLabel;
   }//End Method

   Labeled completionEstimateColourLabel() {
      return completionEstimateColourLabel;
   }//End Method
   
   Labeled detailColourLabel() {
      return detailColourLabel;
   }//End Method
   
}//End Class
