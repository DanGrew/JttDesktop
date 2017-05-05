/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.statistics.panel;

import com.sun.javafx.application.PlatformImpl;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.javafx.registrations.ChangeListenerRegistrationImpl;
import uk.dangrew.jtt.javafx.registrations.RegistrationManager;
import uk.dangrew.jtt.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * The {@link StatisticPanel} provides a {@link StatisticView} for a general value with
 * description.
 */
public class StatisticPanel extends BorderPane implements StatisticView {

   static final double PADDING = 5;
   static final double SHAPE_ARC = 20;
   static final double SHAPE_SIDE_LENGTH = 100;
   
   private final StatisticsConfiguration configuration;
   private final JenkinsDatabase database;
   private final JavaFxStyle styling;
   
   private final Button button;
   private final Rectangle panelShape;
   
   private final BorderPane statWrapper;
   
   private final Label statValue;
   private final Label statDescription;
   
   /**
    * Constructs a new {@link StatisticPanel}.
    * @param styling the {@link JavaFxStyle}.
    * @param configuration the {@link StatisticsConfiguration}.
    * @param database the {@link JenkinsDatabase}.
    * @param description the description of the statistic.
    * @param initialValue the initial value.
    */
   protected StatisticPanel( 
            JavaFxStyle styling, 
            StatisticsConfiguration configuration, 
            JenkinsDatabase database, 
            String description, 
            String initialValue 
   ) {
      this.styling = styling;
      this.configuration = configuration;
      this.database = database;
      
      this.panelShape = new Rectangle();
      this.panelShape.setWidth( SHAPE_SIDE_LENGTH );
      this.panelShape.setHeight( SHAPE_SIDE_LENGTH );
      this.panelShape.setArcWidth( SHAPE_ARC );
      this.panelShape.setArcHeight( SHAPE_ARC );
      
      this.button = new Button();
      this.button.setShape( panelShape );
      this.button.setMaxWidth( Double.MAX_VALUE );
      this.updateBackground();
      
      this.setCenter( button );
      
      this.statWrapper = new BorderPane();
      this.button.setGraphic( statWrapper );
      
      this.statValue = styling.constructTitle( initialValue );
      this.statValue.setTextFill( Color.WHITE );
      this.statDescription = new Label( description );
      this.statDescription.setTextFill( Color.WHITE );
      this.updateText();
      this.updateValueFont();
      this.updateDescriptionFont();
      
      this.statWrapper.setCenter( statValue );
      this.statWrapper.setBottom( statDescription );
      BorderPane.setAlignment( statValue, Pos.CENTER );
      BorderPane.setAlignment( statDescription, Pos.CENTER );
      
      this.setPadding( new Insets( PADDING ) );
      
      RegistrationManager registrations = new RegistrationManager();
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               configuration.statisticBackgroundColourProperty(), ( s, o, n ) -> updateBackground() ) 
      );
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               configuration.statisticTextColourProperty(), ( s, o, n ) -> updateText() ) 
      );
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               configuration.statisticValueTextFontProperty(), ( s, o, n ) -> updateValueFont() ) 
      );
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               configuration.statisticDescriptionTextFontProperty(), ( s, o, n ) -> updateDescriptionFont() ) 
      );
   }//End Constructor
   
   /**
    * Method to update the background of the {@link StatisticPanel}.
    */
   private void updateBackground(){
      this.button.setBackground( new Background( new BackgroundFill( 
               configuration.statisticBackgroundColourProperty().get(), null, null 
      ) ) );
   }//End Method
   
   /**
    * Method to update the text elements of the {@link StatisticPanel}.
    */
   private void updateText(){
      statValue.setTextFill( configuration.statisticTextColourProperty().get() );
      statDescription.setTextFill( configuration.statisticTextColourProperty().get() );
   }//End Method
   
   /**
    * Method to update the {@link javafx.scene.text.Font} of the value element in the {@link StatisticPanel}.
    */
   private void updateValueFont(){
      statValue.setFont( configuration.statisticValueTextFontProperty().get() );
   }//End Method
   
   /**
    * Method to update the {@link javafx.scene.text.Font} of the description element in the {@link StatisticPanel}.
    */
   private void updateDescriptionFont(){
      statDescription.setFont( configuration.statisticDescriptionTextFontProperty().get() );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void setStatisticValue( String value ) {
      PlatformImpl.runAndWait( () -> statValue.setText( value ) );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public String getStatisticValue() {
      return statValue.getText();
   }//End Method
   
   /**
    * Method to determine whether the given is associated.
    * @param configuration the {@link StatisticsConfiguration}.
    * @return true if identical.
    */
   public boolean isAssociatedWith( StatisticsConfiguration configuration ) {
      return this.configuration == configuration;
   }//End Method
   
   /**
    * Method to determine whether the given is associated.
    * @param database the {@link JenkinsDatabase}.
    * @return true if identical.
    */
   public boolean isAssociatedWith( JenkinsDatabase database ) {
      return this.database == database;
   }//End Method
   
   Label statValue(){
      return statValue;
   }//End Method
   
   Label statDescription(){
      return statDescription;
   }//End Method
   
   Button button(){
      return button;
   }//End Method
   
}//End Class
