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
import javafx.scene.layout.GridPane;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.buildwall.configuration.theme.BuildWallTheme;

/**
 * The {@link ThemeBuilderPanel} brings together a {@link DisjointBuilderWall} and
 * {@link ThemeConfigurationPanel} to provide a custom environment for configuring 
 * {@link BuildWallTheme}s.
 */
public class ThemeBuilderPanel extends GridPane {
   
   private final JavaFxStyle styling;
   private final DisjointBuilderWall builderWall;
   private final ThemeConfigurationPanel configurationPanel;
   private final ScrollPane scroller;

   /**
    * Constructs a new {@link ThemeBuilderPanel}.
    * @param theme the {@link BuildWallTheme} to configure.
    */
   public ThemeBuilderPanel( BuildWallTheme theme ) {
      this( new JavaFxStyle(), theme );
   }//End Constructor
   
   /**
    * Constructs a new {@link ThemeBuilderPanel}.
    * @param styling the {@link JavaFxStyle}.
    * @param theme the {@link BuildWallTheme} to configure.
    */
   ThemeBuilderPanel( JavaFxStyle styling, BuildWallTheme theme ) {
      this.styling = styling;
      this.builderWall = new DisjointBuilderWall( theme );
      this.add( builderWall, 0, 0 );
      
      this.configurationPanel = new ThemeConfigurationPanel( theme );
      this.scroller = new ScrollPane( configurationPanel );
      this.scroller.setFitToWidth( true );
      this.add( scroller, 0, 1 );
      
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
   
}//End Class
