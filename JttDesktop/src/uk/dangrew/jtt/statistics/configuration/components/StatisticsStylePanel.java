/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.statistics.configuration.components;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.javafx.combobox.FontFamilyPropertyBox;
import uk.dangrew.jtt.javafx.spinner.PropertySpinner;
import uk.dangrew.jtt.statistics.configuration.StatisticsConfiguration;

/**
 * The {@link StatisticsStylePanel} is responsible for providing configuration options on the statistics
 * panel.
 */
public class StatisticsStylePanel extends GridPane {
   
   static final String DESCRIPTION_TEXT_FONT_SIZE_TEXT = "Description Text Font Size";
   static final String DESCRIPTION_TEXT_FONT_TEXT = "Description Text Font";
   static final String VALUE_TEXT_FONT_SIZE_TEXT = "Value Text Font Size";
   static final String VALUE_TEXT_FONT_TEXT = "Value Text Font";
   static final String TEXT_COLOUR_TEXT = "Text Colour";
   static final String BACKGROUND_COLOUR_TEXT = "Background Colour";
   
   private final StatisticsConfiguration configuration;
   
   private final Label backgroundColourLabel;
   private final ColorPicker backgroundColourPicker;
   private final Label textColourLabel;
   private final ColorPicker textColourPicker;
   private final Label valueFontLabel;
   private final FontFamilyPropertyBox valueFontBox; 
   private final Label valueFontSizeLabel; 
   private final PropertySpinner< Integer, Font > valueFontSizeSpinner;
   private final Label descriptionFontLabel; 
   private final FontFamilyPropertyBox descriptionFontBox;
   private final Label descriptionFontSizeLabel;
   private final PropertySpinner< Integer, Font > descriptionFontSizeSpinner;
   
   /**
    * Constructs a new {@link StatisticsStylePanel}.
    * @param configuration the {@link StatisticsConfiguration}.
    */
   public StatisticsStylePanel( StatisticsConfiguration configuration ) {
      this( new JavaFxStyle(), configuration );
   }//End Constructor
   
   /**
    * Constructs a new {@link StatisticsStylePanel}.
    * @param styling the {@link JavaFxStyle}.
    * @param configuration the {@link StatisticsConfiguration}.
    */
   StatisticsStylePanel( JavaFxStyle styling, StatisticsConfiguration configuration ) {
      this.configuration = configuration;
      
      styling.configureHalfWidthConstraints( this );
      
      backgroundColourLabel = styling.createBoldLabel( BACKGROUND_COLOUR_TEXT );
      add( backgroundColourLabel, 0, 0 );
      
      backgroundColourPicker = new ColorPicker();
      styling.configureColorPicker( backgroundColourPicker, configuration.statisticBackgroundColourProperty() );
      add( backgroundColourPicker, 1, 0 );
      
      textColourLabel = styling.createBoldLabel( TEXT_COLOUR_TEXT );
      add( textColourLabel, 0, 1 );
      
      textColourPicker = new ColorPicker();
      styling.configureColorPicker( textColourPicker, configuration.statisticTextColourProperty() );
      add( textColourPicker, 1, 1 );
      
      valueFontLabel = styling.createBoldLabel( VALUE_TEXT_FONT_TEXT );
      add( valueFontLabel, 0, 2 );
      
      valueFontBox = new FontFamilyPropertyBox( configuration.statisticValueTextFontProperty() );
      valueFontBox.setMaxWidth( Double.MAX_VALUE );
      add( valueFontBox, 1, 2 );
      
      valueFontSizeLabel = styling.createBoldLabel( VALUE_TEXT_FONT_SIZE_TEXT );
      add( valueFontSizeLabel, 0, 3 );
      
      valueFontSizeSpinner = new PropertySpinner<>();  
      styling.configureFontSizeSpinner( valueFontSizeSpinner, configuration.statisticValueTextFontProperty() );
      add( valueFontSizeSpinner, 1, 3 );
      
      descriptionFontLabel = styling.createBoldLabel( DESCRIPTION_TEXT_FONT_TEXT );
      add( descriptionFontLabel, 0, 4 );
      
      descriptionFontBox = new FontFamilyPropertyBox( configuration.statisticDescriptionTextFontProperty() );
      descriptionFontBox.setMaxWidth( Double.MAX_VALUE );
      add( descriptionFontBox, 1, 4 );
      
      descriptionFontSizeLabel = styling.createBoldLabel( DESCRIPTION_TEXT_FONT_SIZE_TEXT );
      add( descriptionFontSizeLabel, 0, 5 );
      
      descriptionFontSizeSpinner = new PropertySpinner<>();  
      styling.configureFontSizeSpinner( descriptionFontSizeSpinner, configuration.statisticDescriptionTextFontProperty() );
      add( descriptionFontSizeSpinner, 1, 5 );
   }//End Constructor
   
   /**
    * Method to determine whether the given is associated with this.
    * @param configuration the {@link StatisticsConfiguration} in question.
    * @return true if identical.
    */
   public boolean isAssociatedWith( StatisticsConfiguration configuration ) {
      return this.configuration == configuration;
   }//End Method
   
   Label backgroundColourLabel(){
      return backgroundColourLabel;
   }//End Method
   
   ColorPicker backgroundColour(){
      return backgroundColourPicker;
   }//End Method
   
   Label textColourLabel(){
      return textColourLabel;
   }//End Method
   
   ColorPicker textColour(){
      return textColourPicker;
   }//End Method
   
   Label valueTextFontStyleLabel(){
      return valueFontLabel;
   }//End Method
   
   FontFamilyPropertyBox valueTextFontStyle(){
      return valueFontBox;
   }//End Method
   
   Label valueTextFontSizeLabel(){
      return valueFontSizeLabel;
   }//End Method
   
   PropertySpinner< Integer, Font > valueTextFontSize(){
      return valueFontSizeSpinner;
   }//End Method
   
   Label descriptionTextFontStyleLabel(){
      return descriptionFontLabel;
   }//End Method
   
   FontFamilyPropertyBox descriptionTextFontStyle(){
      return descriptionFontBox;
   }//End Method
   
   Label descriptionTextFontSizeLabel(){
      return descriptionFontSizeLabel;
   }//End Method
   
   PropertySpinner< Integer, Font > descriptionTextFontSize(){
      return descriptionFontSizeSpinner;
   }//End Method

}//End Class
