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
   
   static final String TRACK_COLOUR_STRING = "Background Colour";
   static final String BAR_COLOUR_STRING = "Progress Colour";
   static final String JOB_NAME_COLOUR_STRING = "Job Name Colour";
   static final String BUILD_NUMBER_COLOUR_STRING = "Build Number Colour";
   static final String BUILD_ESTIMATE_COLOUR_STRING = "Build Estimate Colour";
   static final String DETAIL_COLOUR_STRING = "Detail Colour";
   static final double PADDING = 10;
   
   private final JavaFxStyle styling;
   private final BuildWallTheme theme;
   private final BuildResultStatus status;
   
   private final Label barLabel;
   private final Label trackLabel;
   private final Label jobNameLabel;
   private final Label buildNumberLabel;
   private final Label completionEstimateLabel;
   private final Label detailLabel;
   
   private final ColorPicker barPicker;
   private final ColorPicker trackPicker;
   private final ColorPicker jobNamePicker;
   private final ColorPicker buildNumberPicker;
   private final ColorPicker completionEstimatePicker;
   private final ColorPicker detailPicker;
   
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
      
      barLabel = styling.createBoldLabel( BAR_COLOUR_STRING );
      barPicker = new ColorPicker();
      addColorPicker( 0, barLabel, barPicker, theme.barColoursMap() );
      
      trackLabel = styling.createBoldLabel( TRACK_COLOUR_STRING );
      trackPicker = new ColorPicker();
      addColorPicker( 1, trackLabel, trackPicker, theme.trackColoursMap() );
      
      jobNameLabel = styling.createBoldLabel( JOB_NAME_COLOUR_STRING );
      jobNamePicker = new ColorPicker();
      addColorPicker( 2, jobNameLabel, jobNamePicker, theme.jobNameColoursMap() );
      
      buildNumberLabel = styling.createBoldLabel( BUILD_NUMBER_COLOUR_STRING );
      buildNumberPicker = new ColorPicker();
      addColorPicker( 3, buildNumberLabel, buildNumberPicker, theme.buildNumberColoursMap() );
      
      completionEstimateLabel = styling.createBoldLabel( BUILD_ESTIMATE_COLOUR_STRING );
      completionEstimatePicker = new ColorPicker();
      addColorPicker( 4, completionEstimateLabel, completionEstimatePicker, theme.completionEstimateColoursMap() );
      
      detailLabel = styling.createBoldLabel( DETAIL_COLOUR_STRING );
      detailPicker = new ColorPicker();
      addColorPicker( 5, detailLabel, detailPicker, theme.detailColoursMap() );
      
      setPadding( new Insets( PADDING ) );
   }//End Constructor
   
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

   Label jobNameLabel(){
      return jobNameLabel;
   }//End Method
   
   Label buildNumberLabel(){
      return buildNumberLabel;
   }//End Method
   
   Label completionEstimateLabel(){
      return completionEstimateLabel;
   }//End Method
   
   Label detailLabel(){
      return detailLabel;
   }//End Method
   
   ColorPicker barPicker(){
      return barPicker;
   }//End Method
   
   ColorPicker trackPicker(){
      return trackPicker;
   }//End Method
   
   ColorPicker jobNamePicker(){
      return jobNamePicker;
   }//End Method
   
   ColorPicker buildNumberPicker(){
      return buildNumberPicker;
   }//End Method
   
   ColorPicker completionEstimatePicker(){
      return completionEstimatePicker;
   }//End Method
   
   ColorPicker detailPicker(){
      return detailPicker;
   }//End Method

}//End Class
