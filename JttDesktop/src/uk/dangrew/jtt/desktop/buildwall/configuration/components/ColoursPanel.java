/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.GridPane;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.kode.javafx.style.JavaFxStyle;

/**
 * The {@link ColoursPanel} provides a panel for configuring the colours in the system and 
 * on an associated {@link BuildWallConfiguration}.
 */
public class ColoursPanel extends GridPane {
   
   private final BuildWallConfiguration configuration;
   
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
      this( configuration, new JavaFxStyle(), new ConfigurationPanelDefaults() );
   }//End Constructor
   
   /**
    * Constructs a new {@link ColoursPanel}.
    * @param configuration the {@link BuildWallConfiguration} to configure.
    * @param styling the {@link JavaFxStyle} to apply.
    * @param defaults the {@link ConfigurationPanelDefaults}.
    */
   ColoursPanel( BuildWallConfiguration configuration, JavaFxStyle styling, ConfigurationPanelDefaults defaults ) {
      this.configuration = configuration;
      
      jobNameColourLabel = styling.createBoldLabel( "Job Name" );
      add( jobNameColourLabel, 0, 0 );
      
      jobNameColourPicker = new ColorPicker();
      styling.configureColorPicker( jobNameColourPicker, configuration.jobNameColour() );
      add( jobNameColourPicker, 1, 0 );
      
      buildNumberColourLabel = styling.createBoldLabel( "Build Number" );
      add( buildNumberColourLabel, 0, 1 );
      
      buildNumberColourPicker = new ColorPicker();
      styling.configureColorPicker( buildNumberColourPicker, configuration.buildNumberColour() );
      add( buildNumberColourPicker, 1, 1 );
      
      completionEstimateColourLabel = styling.createBoldLabel( "Build Time" );
      add( completionEstimateColourLabel, 0, 2 );
      
      completionEstimateColourPicker = new ColorPicker();
      styling.configureColorPicker( completionEstimateColourPicker, configuration.completionEstimateColour() );
      add( completionEstimateColourPicker, 1, 2 );
      
      detailColourLabel = styling.createBoldLabel( "Detail" );
      add( detailColourLabel, 0, 3 );
      
      detailColourPicker = new ColorPicker();
      styling.configureColorPicker( detailColourPicker, configuration.detailColour() );
      add( detailColourPicker, 1, 3 );
      
      defaults.configureColumnConstraints( this );
   }//End Constructor
   
   /**
    * Method to determine whether the given {@link BuildWallConfiguration} is associated.
    * @param configuration the {@link BuildWallConfiguration} in question.
    * @return true if associated.
    */
   public boolean hasConfiguration( BuildWallConfiguration configuration ) {
      return this.configuration.equals( configuration );
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
