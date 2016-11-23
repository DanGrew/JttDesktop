/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.components.themebuilder;

import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.buildwall.configuration.theme.BuildWallTheme;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;

/**
 * {@link StatusConfigurationPane} provides the configuration items for a single
 * {@link BuildResultStatus}.
 */
class StatusConfigurationPane extends GridPane {
   
   static final String TRACK_COLOUR_STRING = "Track Colour";
   static final String BAR_COLOUR_STRING = "Bar Colour";
   static final double PADDING = 10;
   
   private final JavaFxStyle styling;
   private final BuildWallTheme theme;
   private final BuildResultStatus status;
   
   private Label barLabel;
   private Label trackLabel;
   
   private ColorPicker barPicker;
   private ColorPicker trackPicker;
   
   /**
    * Constructs a new {@link StatusConfigurationPane}.
    * @param theme the {@link BuildWallTheme} configured for.
    * @param status the {@link BuildResultStatus} being configured.
    */
   public StatusConfigurationPane( BuildWallTheme theme, BuildResultStatus status ) {
      this( new JavaFxStyle(), theme, status );
   }//End Constructor
   
   /**
    * Constructs a new {@link StatusConfigurationPane}.
    * @param styling the {@link JavaFxStyle}.
    * @param theme the {@link BuildWallTheme} configured for.
    * @param status the {@link BuildResultStatus} being configured.
    */
   StatusConfigurationPane( JavaFxStyle styling, BuildWallTheme theme, BuildResultStatus status ) {
      this.styling = styling;
      this.theme = theme;
      this.status = status;
      this.styling.configureHalfWidthConstraints( this );
      
      addBarColorPicker();
      addTrackColorPicker();
      
      setPadding( new Insets( PADDING ) );
   }//End Constructor
   
   /**
    * Method to add a {@link ColorPicker} for a {@link BuildResultStatus} bar.
    */
   private void addBarColorPicker() {
      barLabel = styling.createBoldLabel( BAR_COLOUR_STRING );
      barPicker = new ColorPicker();
      
      addColorPicker( 0, barLabel, barPicker, theme.barColoursMap() );
   }//End Method
   
   /**
    * Method to add a {@link ColorPicker} for a {@link BuildResultStatus} track.
    */
   private void addTrackColorPicker() {
      trackLabel = styling.createBoldLabel( TRACK_COLOUR_STRING );
      trackPicker = new ColorPicker();
      
      addColorPicker( 1, trackLabel, trackPicker, theme.trackColoursMap() );
   }//End Method
   
   /**
    * General mechanism for adding {@link ColorPicker}s.
    * @param row the row to add the items on.
    * @param label the {@link Label} describing the {@link ColorPicker}.
    * @param picker the {@link ColorPicker}.
    * @param map the {@link ObservableMap} providing the {@link Color}.
    */
   private void addColorPicker( int row, Label label, ColorPicker picker, ObservableMap< BuildResultStatus, Color > map ) {
      add( label, 0, row );
      
      styling.configureColorPicker( picker, map.get( status ) );
      
      map.addListener( 
               new StatusFilterPropertyUpdater( map, status, picker.valueProperty() )
      );
      picker.valueProperty().addListener( ( s, o, n ) -> map.put( status, n ) );
      add( picker, 1, row );
   }//End Method
   
   /**
    * Method to determine whether this is associated with the given.
    * @param theme the {@link BuildWallTheme} in question.
    * @return true if identical.
    */
   public boolean isAssociatedWith( BuildWallTheme theme ) {
      return this.theme == theme;
   }//End Method
   
   /**
    * Method to determine whether this is associated with the given.
    * @param status the {@link BuildResultStatus} in question.
    * @return true if identical.
    */
   public boolean isAssociatedWith( BuildResultStatus status ) {
      return this.status == status;
   }//End Method
   
   Label barLabel(){
      return barLabel;
   }//End Method
   
   Label trackLabel(){
      return trackLabel;
   }//End Method
   
   ColorPicker barPicker(){
      return barPicker;
   }//End Method
   
   ColorPicker trackPicker(){
      return trackPicker;
   }//End Method

}//End Class
