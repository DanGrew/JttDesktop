/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.style;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import uk.dangrew.jtt.javafx.spinner.DefensiveDoubleSpinnerValueFactory;
import uk.dangrew.jtt.javafx.spinner.DefensiveIntegerSpinnerValueFactory;
import uk.dangrew.jtt.javafx.spinner.DoublePropertySpinner;
import uk.dangrew.jtt.javafx.spinner.IntegerPropertySpinner;

/**
 * The {@link JavaFxStyle} provides the common styling options for JavaFx elements.
 */
public class JavaFxStyle {

   static final int TITLE_FONT_SIZE = 30;
   static final double LABEL_PERCENTAGE_WIDTH = 40;
   static final double CONTROLS_PERCENTAGE_WIDTH = 60;
   
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
    * Method to create a new {@link Label} with the given text that is wrapped
    * across lines.
    * @param text the {@link String} text on the {@link Label}.
    * @return the {@link Label}.
    */
   public Label createWrappedTextLabel( String text ) {
      Label label = new Label( text );
      label.setWrapText( true );
      return label;
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
   
   /**
    * Method to construct a title for the configuration panel.
    * @param title the string title to create a {@link Label} for.
    * @return the {@link Label} constructed.
    */
   public Label constructTitle( String title ) {
      Label titleLabel = createBoldLabel( title, TITLE_FONT_SIZE );
      titleLabel.setAlignment( Pos.CENTER );
      GridPane.setConstraints( titleLabel, 0, 0, 2, 1, HPos.CENTER, VPos.CENTER );
      return titleLabel;
   }//End Method
   
   /**
    * Method to configure the {@link ColumnConstraints} on the given {@link GridPane}.
    * @param grid the {@link GridPane} to apply constraints to.
    */
   public void configureColumnConstraints( GridPane grid ){
      ColumnConstraints labels = new ColumnConstraints();
      labels.setPercentWidth( LABEL_PERCENTAGE_WIDTH );
      labels.setHgrow( Priority.ALWAYS );
      ColumnConstraints controls = new ColumnConstraints();
      controls.setPercentWidth( CONTROLS_PERCENTAGE_WIDTH );
      controls.setHgrow( Priority.ALWAYS );
      grid.getColumnConstraints().addAll( labels, controls );  
   }//End Method
   
   /**
    * Method to apply a style change where there is no background but instead a {@link Color} placed
    * when pressed and removed when released.
    * @param button the {@link Button} to apply the style to.
    * @param backgroundWhenPressed the {@link Color} to use for the background.
    */
   public void removeBackgroundAndColourOnClick( Button button, Color backgroundWhenPressed ) {
      button.setBackground( null );
      button.setOnMousePressed( event -> button.setBackground( new Background( 
               new BackgroundFill( backgroundWhenPressed, null, null ) 
      ) ) );
      button.setOnMouseReleased( event -> button.setBackground( null ) );
   }//End Method
   
}//End Class
