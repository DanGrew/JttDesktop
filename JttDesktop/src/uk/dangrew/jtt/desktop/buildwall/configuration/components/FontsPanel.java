/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.desktop.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.javafx.combobox.FontFamilyPropertyBox;
import uk.dangrew.jtt.desktop.javafx.spinner.DefensiveIntegerSpinnerValueFactory;
import uk.dangrew.jtt.desktop.javafx.spinner.PropertySpinner;

/**
 * The {@link FontsPanel} is responsible for displaying all configurable items related to {@link Font}s
 * and configuring them on the {@link BuildWallConfiguration}.
 */
public class FontsPanel extends GridPane {
   
   private final BuildWallConfiguration configuration;
   
   private final Label jobNameFontLabel;
   private final Label buildNumberFontLabel;
   private final Label completionEstimateFontLabel;
   private final Label detailFontLabel;
   
   private final Label jobNameFontSizeLabel;
   private final Label buildNumberFontSizeLabel;
   private final Label completionEstimateFontSizeLabel;
   private final Label detailFontSizeLabel;
   
   private final FontFamilyPropertyBox jobNameFontBox;
   private final FontFamilyPropertyBox buildNumberFontBox;
   private final FontFamilyPropertyBox completionEstimateFontBox;
   private final FontFamilyPropertyBox detailFontBox;
   
   private final PropertySpinner< Integer, Font > jobNameFontSizeSpinner;
   private final PropertySpinner< Integer, Font > buildNumberFontSizeSpinner;
   private final PropertySpinner< Integer, Font > completionEstimateFontSizeSpinner;
   private final PropertySpinner< Integer, Font > detailFontSizeSpinner;
  
   /**
    * Constructs a new {@link FontsPanel}.
    * @param configuration the {@link BuildWallConfiguration} associated to be configured.
    */
   public FontsPanel( BuildWallConfiguration configuration) {
      this( configuration, new JavaFxStyle() );
   }//End Constructor
   
   /**
    * Constructs a new {@link FontsPanel}.
    * @param configuration the {@link BuildWallConfiguration} associated to be configured.
    * @param styling the {@link BuildWallConfigurationStyle} to use for style.
    */
   FontsPanel( BuildWallConfiguration configuration, JavaFxStyle styling ) {
      this.configuration = configuration;
      
      jobNameFontLabel = styling.createBoldLabel( "Job Name Font" );
      add( jobNameFontLabel, 0, 0 );
      jobNameFontBox = new FontFamilyPropertyBox( configuration.jobNameFont() );
      jobNameFontBox.setMaxWidth( Double.MAX_VALUE );
      add( jobNameFontBox, 1, 0 );
      
      jobNameFontSizeLabel = styling.createBoldLabel( "Job Name Size" );
      add( jobNameFontSizeLabel, 0, 2 );
      jobNameFontSizeSpinner = new PropertySpinner<>();  
      styling.configureFontSizeSpinner( jobNameFontSizeSpinner, configuration.jobNameFont() );
      add( jobNameFontSizeSpinner, 1, 2 );

      buildNumberFontLabel = styling.createBoldLabel( "Build Number Font" );
      add( buildNumberFontLabel, 0, 4 );
      buildNumberFontBox = new FontFamilyPropertyBox( configuration.buildNumberFont() );
      buildNumberFontBox.setMaxWidth( Double.MAX_VALUE );
      add( buildNumberFontBox, 1, 4 );
      
      buildNumberFontSizeLabel = styling.createBoldLabel( "Build Number Size" );
      add( buildNumberFontSizeLabel, 0, 6 );
      buildNumberFontSizeSpinner = new PropertySpinner<>();  
      styling.configureFontSizeSpinner( buildNumberFontSizeSpinner, configuration.buildNumberFont() );
      add( buildNumberFontSizeSpinner, 1, 6 );

      completionEstimateFontLabel = styling.createBoldLabel( "Build Time Font" );
      add( completionEstimateFontLabel, 0, 8 );
      completionEstimateFontBox = new FontFamilyPropertyBox( configuration.completionEstimateFont() );
      completionEstimateFontBox.setMaxWidth( Double.MAX_VALUE );
      add( completionEstimateFontBox, 1, 8 );
      
      completionEstimateFontSizeLabel = styling.createBoldLabel( "Build Time Size" );
      add( completionEstimateFontSizeLabel, 0, 10 );
      completionEstimateFontSizeSpinner = new PropertySpinner<>();  
      styling.configureFontSizeSpinner( completionEstimateFontSizeSpinner, configuration.completionEstimateFont() );
      add( completionEstimateFontSizeSpinner, 1, 10 );
      
      detailFontLabel = styling.createBoldLabel( "Detail Font" );
      add( detailFontLabel, 0, 12 );
      detailFontBox = new FontFamilyPropertyBox( configuration.detailFont() );
      detailFontBox.setMaxWidth( Double.MAX_VALUE );
      add( detailFontBox, 1, 12 );
      
      detailFontSizeLabel = styling.createBoldLabel( "Detail Size" );
      add( detailFontSizeLabel, 0, 14 );
      detailFontSizeSpinner = new PropertySpinner<>();  
      styling.configureFontSizeSpinner( detailFontSizeSpinner, configuration.detailFont() );
      add( detailFontSizeSpinner, 1, 14 );
      
      styling.configureColumnConstraints( this );
   }//End Constructor
   
   /**
    * Method to determine whether the given {@link BuildWallConfiguration} is associated.
    * @param configuration the {@link BuildWallConfiguration} in question.
    * @return true if associated.
    */
   public boolean hasConfiguration( BuildWallConfiguration configuration ) {
      return this.configuration.equals( configuration );
   }//End Method

   Label jobNameFontLabel() {
      return jobNameFontLabel;
   }//End Method

   Labeled buildNumberFontLabel() {
      return buildNumberFontLabel;
   }//End Method

   Labeled completionEstimateFontLabel() {
      return completionEstimateFontLabel;
   }//End Method

   Labeled detailFontLabel() {
      return detailFontLabel;
   }//End Method
   
   PropertySpinner< Integer, Font > jobNameFontSizeSpinner() {
      return jobNameFontSizeSpinner;
   }//End Method
   
   PropertySpinner< Integer, Font > buildNumberFontSizeSpinner() {
      return buildNumberFontSizeSpinner;
   }//End Method
   
   PropertySpinner< Integer, Font > completionEstimateFontSizeSpinner() {
      return completionEstimateFontSizeSpinner;
   }//End Method
   
   PropertySpinner< Integer, Font > detailFontSizeSpinner() {
      return detailFontSizeSpinner;
   }//End Method

   Label jobNameFontSizeLabel() {
      return jobNameFontSizeLabel;
   }//End Method
   
   Label buildNumberFontSizeLabel() {
      return buildNumberFontSizeLabel;
   }//End Method
   
   Label completionEstimateFontSizeLabel() {
      return completionEstimateFontSizeLabel;
   }//End Method
   
   Label detailFontSizeLabel() {
      return detailFontSizeLabel;
   }//End Method
   
   ComboBox< String > jobNameFontBox() {
      return jobNameFontBox;
   }//End Method

   ComboBox< String > buildNumberFontBox() {
      return buildNumberFontBox;
   }//End Method
   
   ComboBox< String > completionEstimateFontBox() {
      return completionEstimateFontBox;
   }//End Method
   
   ComboBox< String > detailFontBox() {
      return detailFontBox;
   }//End Method
}//End Class
