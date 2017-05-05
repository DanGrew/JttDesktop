/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.components.themebuilder;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.buildwall.configuration.theme.BuildWallTheme;

/**
 * The {@link ThemeBuilderPanel} brings together a {@link DisjointBuilderWall} and
 * {@link ThemeConfigurationPanel} to provide a custom environment for configuring 
 * {@link BuildWallTheme}s.
 */
public class ThemeBuilderPanel extends GridPane {
   
   static final String SHORTCUTS_TITLE = "Shortcuts";
   
   private final JavaFxStyle styling;
   private final ThemeBuilderShortcutProperties shortcuts;
   private final DisjointBuilderWall builderWall;
   private final ThemeConfigurationPanel configurationPanel;
   private final ScrollPane scroller;
   private final TitledPane shortcutsPane;
   private final BorderPane scrollerSplit;

   /**
    * Constructs a new {@link ThemeBuilderPanel}.
    * @param theme the {@link BuildWallTheme} to configure.
    */
   public ThemeBuilderPanel( BuildWallTheme theme ) {
      this( new JavaFxStyle(), new ThemeBuilderShortcutProperties(), theme );
   }//End Constructor
   
   /**
    * Constructs a new {@link ThemeBuilderPanel}.
    * @param styling the {@link JavaFxStyle}.
    * @param shortcuts the {@link ThemeBuilderShortcutProperties}.
    * @param theme the {@link BuildWallTheme} to configure.
    */
   ThemeBuilderPanel( JavaFxStyle styling, ThemeBuilderShortcutProperties shortcuts, BuildWallTheme theme ) {
      this.styling = styling;
      this.shortcuts = shortcuts;
      this.builderWall = new DisjointBuilderWall( theme );
      this.add( builderWall, 0, 0 );
      
      this.configurationPanel = new ThemeConfigurationPanel( theme, shortcuts );
      this.scroller = new ScrollPane( configurationPanel );
      this.scroller.setFitToWidth( true );
      
      this.shortcutsPane = new TitledPane( SHORTCUTS_TITLE, new ThemeBuilderShortcutsPane( shortcuts ) );
      styling.applyBasicPadding( shortcutsPane );
      
      this.scrollerSplit = new BorderPane( scroller );
      this.scrollerSplit.setTop( shortcutsPane );
      this.add( scrollerSplit, 0, 1 );
      
      this.styling.configureFullWidthConstraints( this );
   }//End Constructor
   
   DisjointBuilderWall wall(){
      return builderWall;
   }//End Method
   
   ThemeConfigurationPanel configuration(){
      return configurationPanel;
   }//End Method
   
   ScrollPane scroller(){
      return scroller;
   }//End Method
   
   TitledPane shortcutsPane(){
      return shortcutsPane;
   }//End Method
   
   BorderPane scrollerSplit(){
      return scrollerSplit;
   }//End Method
   
}//End Class
