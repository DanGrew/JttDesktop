/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components.themebuilder;

import java.util.EnumMap;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import uk.dangrew.jtt.desktop.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.buildwall.configuration.theme.BuildWallTheme;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;

/**
 * The {@link ThemeConfigurationPanel} is responsible for providing configuration options
 * for a {@link BuildWallTheme}.
 */
class ThemeConfigurationPanel extends GridPane {
   
   static final double COLUMN_PERCENTAGE = 100;
   static final double HALF_WIDTH_COLUMN = 50;
   static final double PADDING = 10;
   
   private final BuildWallTheme theme;
   private final ThemeBuilderShortcutProperties shortcuts;
   private final JavaFxStyle styling;
   
   private final Map< BuildResultStatus, TitledPane > titledPanes;
   
   /**
    * Constructs a new {@link ThemeConfigurationPanel}.
    * @param theme the {@link BuildWallTheme} being configured.
    * @param shortcuts the {@link ThemeBuilderShortcutProperties}.
    */
   public ThemeConfigurationPanel( BuildWallTheme theme, ThemeBuilderShortcutProperties shortcuts ) {
      this( new JavaFxStyle(), theme, shortcuts );
   }//End Constructor
   
   /**
    * Constructs a new {@link ThemeConfigurationPanel}.
    * @param styling the {@link JavaFxStyle}.
    * @param theme the {@link BuildWallTheme} being configured.
    * @param shortcuts the {@link ThemeBuilderShortcutProperties}.
    */
   ThemeConfigurationPanel( JavaFxStyle styling, BuildWallTheme theme, ThemeBuilderShortcutProperties shortcuts ) {
      if ( theme == null || shortcuts == null ) {
         throw new IllegalArgumentException( "Must provide non null arguments." );
      }
      
      this.theme = theme;
      this.shortcuts = shortcuts;
      this.styling = styling;
      this.titledPanes = new EnumMap<>( BuildResultStatus.class );
      
      this.configureLayout();
   }//End Constructor
   
   /**
    * Method to configure the layout and add the components.
    */
   private void configureLayout(){
      setPadding( new Insets( PADDING ) );
      styling.configureConstraintsForColumnPercentages( this, COLUMN_PERCENTAGE );
      
      for ( BuildResultStatus status : BuildResultStatus.values() ) {
         TitledPane pane = new TitledPane( status.displayName(), new StatusConfigurationPane( theme, shortcuts, status ) );
         add( pane, 0, status.ordinal() );
         titledPanes.put( status, pane );
      }
   }//End Constructor
   
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
   
}//End Class
