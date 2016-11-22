/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.components.themebuilder;

import java.util.EnumMap;
import java.util.Map;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.buildwall.configuration.theme.BuildWallTheme;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.utility.observable.FunctionMapChangeListenerImpl;

/**
 * The {@link ThemeConfigurationPanel} is responsible for providing configuration options
 * for a {@link BuildWallTheme}.
 */
class ThemeConfigurationPanel extends GridPane {
   
   static final double COLUMN_PERCENTAGE = 100;
   static final double HALF_WIDTH_COLUMN = 50;
   static final double PADDING = 10;
   static final String TRACK_COLOUR_STRING = "Track Colour";
   static final String BAR_COLOUR_STRING = "Bar Colour";
   
   private final BuildWallTheme theme;
   private final JavaFxStyle styling;
   
   private final Map< BuildResultStatus, TitledPane > titledPanes;
   
   private final Map< BuildResultStatus, ColorPicker > barColorPickers;
   private final Map< BuildResultStatus, Label > barLabels;
   private final Map< BuildResultStatus, ObjectProperty< Color > > individualBarProperties;
   
   private final Map< BuildResultStatus, ColorPicker > trackColorPickers;
   private final Map< BuildResultStatus, Label > trackLabels;
   private final Map< BuildResultStatus, ObjectProperty< Color > > individualTrackProperties;
   
   /**
    * Constructs a new {@link ThemeConfigurationPanel}.
    * @param theme the {@link BuildWallTheme} being configured.
    */
   public ThemeConfigurationPanel( BuildWallTheme theme ) {
      this( new JavaFxStyle(), theme );
   }//End Constructor
   
   /**
    * Constructs a new {@link ThemeConfigurationPanel}.
    * @param styling the {@link JavaFxStyle}.
    * @param theme the {@link BuildWallTheme} being configured.
    */
   ThemeConfigurationPanel( JavaFxStyle styling, BuildWallTheme theme ) {
      if ( theme == null ) {
         throw new IllegalArgumentException( "Must provide BuildWallTheme." );
      }
      
      this.theme = theme;
      this.styling = styling;
      this.titledPanes = new EnumMap<>( BuildResultStatus.class );
      this.barColorPickers = new EnumMap<>( BuildResultStatus.class );
      this.trackColorPickers = new EnumMap<>( BuildResultStatus.class );
      this.barLabels = new EnumMap<>( BuildResultStatus.class );
      this.trackLabels = new EnumMap<>( BuildResultStatus.class );
      this.individualBarProperties = new EnumMap<>( BuildResultStatus.class );
      this.individualTrackProperties = new EnumMap<>( BuildResultStatus.class );
      
      this.bindColourProperties();
      this.registerForThemeUpdates();
      this.configureLayout();
   }//End Constructor
   
   /**
    * Method to bind the {@link Color} properties for updating the {@link BuildWallTheme}.
    */
   private void bindColourProperties(){
      for ( BuildResultStatus status : BuildResultStatus.values() ) {
         ObjectProperty< Color > barProperty = new SimpleObjectProperty<>( theme.barColoursMap().get( status ) );
         individualBarProperties.put( status, barProperty );
         barProperty.addListener( ( s, o, n ) -> theme.barColoursMap().put( status, n ) );
         
         ObjectProperty< Color > trackProperty = new SimpleObjectProperty<>( theme.trackColoursMap().get( status ) );
         individualTrackProperties.put( status, trackProperty );
         trackProperty.addListener( ( s, o, n ) -> theme.trackColoursMap().put( status, n ) );
      }
   }//End Method
   
   /**
    * Method to register for the {@link BuildWallTheme} state updates and relay those to the
    * {@link ColorPicker}s.
    */
   private void registerForThemeUpdates(){
      theme.barColoursMap().addListener( new FunctionMapChangeListenerImpl<>( 
               theme.barColoursMap(), 
               ( k, v ) ->  { 
                  if ( v != null ) {
                     individualBarProperties.get( k ).set( v );
                  }
               },
               ( k, v ) -> { /* leave previous color when removed */ } 
      ) );
      theme.trackColoursMap().addListener( new FunctionMapChangeListenerImpl<>( 
               theme.trackColoursMap(), 
               ( k, v ) ->  {
                  if ( v != null ) {
                     individualTrackProperties.get( k ).set( v );
                  }
               },
               ( k, v ) -> { /* leave previous color when removed */ } 
      ) );
   }//End Method
   
   /**
    * Method to configure the layout and add the components.
    */
   private void configureLayout(){
      setPadding( new Insets( PADDING ) );
      styling.configureConstraintsForColumnPercentages( this, COLUMN_PERCENTAGE );
      
      for ( BuildResultStatus status : BuildResultStatus.values() ) {
         GridPane statusWrapper = addTitledPaneWithWrapper( status );
         addBarColorPicker( status, statusWrapper );
         addTrackColorPicker( status, statusWrapper );
      }
   }//End Constructor
   
   /**
    * Method to add a {@link TitledPane} for the {@link BuildResultStatus} used to wrap the properties
    * being configured.
    * @param status the {@link BuildResultStatus} the pane is for.
    * @return the wrapping {@link GridPane} to put properties in.
    */
   private GridPane addTitledPaneWithWrapper( BuildResultStatus status ) {
      GridPane statusWrapper = new GridPane();
      styling.configureConstraintsForColumnPercentages( statusWrapper, HALF_WIDTH_COLUMN, HALF_WIDTH_COLUMN );
      
      TitledPane pane = new TitledPane( status.displayName(), statusWrapper );
      pane.setCollapsible( false );
      
      add( pane, 0, status.ordinal() );
      titledPanes.put( status, pane );
      return statusWrapper;
   }//End Method
   
   /**
    * Method to add a {@link ColorPicker} for a {@link BuildResultStatus} bar.
    * @param status the {@link BuildResultStatus} to add for.
    * @param statusPane the {@link GridPane} to put properties in.
    */
   private void addBarColorPicker( BuildResultStatus status, GridPane statusPane ) {
      Label label = styling.createBoldLabel( BAR_COLOUR_STRING );
      statusPane.add( label, 0, 0 );
      barLabels.put( status, label );
      
      ColorPicker picker = new ColorPicker();
      styling.configureColorPicker( picker, individualBarProperties.get( status ) );
      barColorPickers.put( status, picker );
      
      statusPane.add( picker, 1, 0 );
   }//End Method
   
   /**
    * Method to add a {@link ColorPicker} for a {@link BuildResultStatus} track.
    * @param status the {@link BuildResultStatus} to add for.
    * @param statusPane the {@link GridPane} to put properties in.
    */
   private void addTrackColorPicker( BuildResultStatus status, GridPane statusPane ) {
      Label label = styling.createBoldLabel( TRACK_COLOUR_STRING );
      statusPane.add( label, 0, 1 );
      trackLabels.put( status, label );
      
      ColorPicker picker = new ColorPicker();
      styling.configureColorPicker( picker, individualTrackProperties.get( status ) );
      trackColorPickers.put( status, picker );
      
      statusPane.add( picker, 1, 1 );
   }//End Method
   
   /**
    * Method to determine whether this is associated with the given {@link BuildWallTheme}.
    * @param theme the {@link BuildWallTheme} in question.
    * @return true if identical.
    */
   public boolean isAssociatedWith( BuildWallTheme theme ) {
      return this.theme == theme;
   }//End Method
   
   TitledPane titledPaneFor( BuildResultStatus status ) {
      return titledPanes.get( status );
   }//End Method
   
   ColorPicker barColorPickerFor( BuildResultStatus status ) {
      return barColorPickers.get( status );
   }//End Method
   
   ColorPicker trackColorPickerFor( BuildResultStatus status ) {
      return trackColorPickers.get( status );
   }//End Method
   
   Label barLabelFor( BuildResultStatus status ) {
      return barLabels.get( status );
   }//End Method
   
   Label trackLabelFor( BuildResultStatus status ) {
      return trackLabels.get( status );
   }//End Method

}//End Class
