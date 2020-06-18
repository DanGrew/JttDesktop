/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components.themebuilder;

import javafx.collections.ObservableMap;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import uk.dangrew.jtt.desktop.buildwall.configuration.theme.BuildWallTheme;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.kode.javafx.style.JavaFxStyle;

/**
 * {@link StatusConfigurationPane} provides the configuration items for a single
 * {@link BuildResultStatus}.
 */
class StatusConfigurationPane extends GridPane {
   
   static final String SHORTCUT_BUTTON_TEXT = "Use Shortcut";
   static final String TRACK_COLOUR_STRING = "Background Colour";
   static final String BAR_COLOUR_STRING = "Progress Colour";
   static final String JOB_NAME_COLOUR_STRING = "Job Name Colour";
   static final String BUILD_NUMBER_COLOUR_STRING = "Build Number Colour";
   static final String BUILD_ESTIMATE_COLOUR_STRING = "Build Estimate Colour";
   static final String DETAIL_COLOUR_STRING = "Detail Colour";
   
   static final double LABEL_WIDTH_PERCENTAGE = 50;
   static final double PICKER_WIDTH_PERCENTAGE = 25;
   static final double SHORTCUT_WIDTH_PERCENTAGE = 25;
   
   private final JavaFxStyle styling;
   private final BuildWallTheme theme;
   private final ThemeBuilderShortcutProperties shortcuts;
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
   
   private final Button barShortcut;
   private final Button trackShortcut;
   private final Button jobNameShortcut;
   private final Button buildNumberShortcut;
   private final Button completionEstimateShortcut;
   private final Button detailShortcut;
   
   /**
    * Constructs a new {@link StatusConfigurationPane}.
    * @param theme the {@link BuildWallTheme} configured for.
    * @param shortcuts the {@link ThemeBuilderShortcutProperties}.
    * @param status the {@link BuildResultStatus} being configured.
    */
   public StatusConfigurationPane( BuildWallTheme theme, ThemeBuilderShortcutProperties shortcuts, BuildResultStatus status ) {
      this( new JavaFxStyle(), theme, shortcuts, status );
   }//End Constructor
   
   /**
    * Constructs a new {@link StatusConfigurationPane}.
    * @param styling the {@link JavaFxStyle}.
    * @param shortcuts the {@link ThemeBuilderShortcutProperties}.
    * @param theme the {@link BuildWallTheme} configured for.
    * @param status the {@link BuildResultStatus} being configured.
    */
   StatusConfigurationPane( JavaFxStyle styling, BuildWallTheme theme, ThemeBuilderShortcutProperties shortcuts, BuildResultStatus status ) {
      this.styling = styling;
      this.theme = theme;
      this.shortcuts = shortcuts;
      this.status = status;
      this.styling.configureConstraintsForColumnPercentages( 
               this, LABEL_WIDTH_PERCENTAGE, PICKER_WIDTH_PERCENTAGE, SHORTCUT_WIDTH_PERCENTAGE 
      );
      
      barLabel = styling.createBoldLabel( BAR_COLOUR_STRING );
      barPicker = new ColorPicker();
      barShortcut = new Button( SHORTCUT_BUTTON_TEXT );
      addItemConfiguration( 0, barLabel, barPicker, barShortcut, theme.barColoursMap() );
      
      trackLabel = styling.createBoldLabel( TRACK_COLOUR_STRING );
      trackPicker = new ColorPicker();
      trackShortcut = new Button( SHORTCUT_BUTTON_TEXT );
      addItemConfiguration( 1, trackLabel, trackPicker, trackShortcut, theme.trackColoursMap() );
      
      jobNameLabel = styling.createBoldLabel( JOB_NAME_COLOUR_STRING );
      jobNamePicker = new ColorPicker();
      jobNameShortcut = new Button( SHORTCUT_BUTTON_TEXT );
      addItemConfiguration( 2, jobNameLabel, jobNamePicker, jobNameShortcut, theme.jobNameColoursMap() );
      
      buildNumberLabel = styling.createBoldLabel( BUILD_NUMBER_COLOUR_STRING );
      buildNumberPicker = new ColorPicker();
      buildNumberShortcut = new Button( SHORTCUT_BUTTON_TEXT );
      addItemConfiguration( 3, buildNumberLabel, buildNumberPicker, buildNumberShortcut, theme.buildNumberColoursMap() );
      
      completionEstimateLabel = styling.createBoldLabel( BUILD_ESTIMATE_COLOUR_STRING );
      completionEstimatePicker = new ColorPicker();
      completionEstimateShortcut = new Button( SHORTCUT_BUTTON_TEXT );
      addItemConfiguration( 4, completionEstimateLabel, completionEstimatePicker, completionEstimateShortcut, theme.completionEstimateColoursMap() );
      
      detailLabel = styling.createBoldLabel( DETAIL_COLOUR_STRING );
      detailPicker = new ColorPicker();
      detailShortcut = new Button( SHORTCUT_BUTTON_TEXT );
      addItemConfiguration( 5, detailLabel, detailPicker, detailShortcut, theme.detailColoursMap() );
      
      styling.applyBasicPadding( this );
   }//End Constructor
   
   /**
    * General mechanism for adding {@link ColorPicker}s.
    * @param row the row to add the items on.
    * @param label the {@link Label} describing the {@link ColorPicker}.
    * @param picker the {@link ColorPicker}.
    * @param shortcut the {@link Button} providing the shortcut.
    * @param map the {@link ObservableMap} providing the {@link Color}.
    */
   private void addItemConfiguration( int row, Label label, ColorPicker picker, Button shortcut, ObservableMap< BuildResultStatus, Color > map ) {
      add( label, 0, row );
      
      styling.configureColorPicker( picker, map.get( status ) );
      
      map.addListener( 
               new StatusFilterPropertyUpdater( map, status, picker.valueProperty() )
      );
      picker.valueProperty().addListener( ( s, o, n ) -> map.put( status, n ) );
      add( picker, 1, row );
      
      shortcut.setMaxWidth( Double.MAX_VALUE );
      shortcut.setOnAction( e -> picker.setValue( shortcuts.shortcutColorProperty().get() ) );
      add( shortcut, 2, row );
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
    * @param shortcuts the {@link ThemeBuilderShortcutProperties} in question.
    * @return true if identical.
    */
   public boolean isAssociatedWith( ThemeBuilderShortcutProperties shortcuts ) {
      return this.shortcuts == shortcuts;
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
   
   Button barShortcut(){
      return barShortcut;
   }//End Method
   
   Button trackShortcut(){
      return trackShortcut;
   }//End Method

   Button jobNameShortcut(){
      return jobNameShortcut;
   }//End Method
   
   Button buildNumberShortcut(){
      return buildNumberShortcut;
   }//End Method
   
   Button completionEstimateShortcut(){
      return completionEstimateShortcut;
   }//End Method
   
   Button detailShortcut(){
      return detailShortcut;
   }//End Method

}//End Class
