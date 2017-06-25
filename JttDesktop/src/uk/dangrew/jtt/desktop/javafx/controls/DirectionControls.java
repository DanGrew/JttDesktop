/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.controls;

import java.util.EnumMap;
import java.util.Map;

import de.jensd.fx.glyphs.GlyphIcon;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import uk.dangrew.jtt.desktop.buildwall.configuration.style.JavaFxStyle;

/**
 * {@link DirectionControls} provides a common direction pad like set of {@link Button}s.
 */
public class DirectionControls extends BorderPane {
   
   static final double PADDING = 10.0;
   
   private final GridPane grid;
   private final Map< DirectionControlType, Button > buttons;
   
   /**
    * Constructs a new {@link DirectionControls}.
    * @param glyphs the {@link DirectionControlType} {@link GlyphIcon}s to use.
    */
   public DirectionControls( 
            Map< DirectionControlType, GlyphIcon< ? > > glyphs
   ) {
      this( new JavaFxStyle(), glyphs );
   }//End Constructor
   
   /**
    * Constructs a new {@link DirectionControls}.
    * @param styling the {@link JavaFxStyle} to use.
    * @param glyphs the {@link DirectionControlType} {@link GlyphIcon}s to use.
    */
   DirectionControls(
            JavaFxStyle styling,
            Map< DirectionControlType, GlyphIcon< ? > > glyphs
   ) {
      this.buttons = new EnumMap<>( DirectionControlType.class );
      
      grid = new GridPane();
      grid.setHgap( PADDING );
      grid.setVgap( PADDING );
      
      for ( DirectionControlType direction : DirectionControlType.values() ) {
         GlyphIcon< ? > glyph = glyphs.get( direction );
         buttons.put( direction, styling.createGlyphButton( glyph ) );
      }
      
      grid.add( buttons.get( DirectionControlType.Up ), 1, 0 );
      grid.add( buttons.get( DirectionControlType.Left ), 0, 1 );
      grid.add( buttons.get( DirectionControlType.Right ), 2, 1 );
      grid.add( buttons.get( DirectionControlType.Down ), 1, 2 );
      
      styling.configureConstraintsForEvenColumns( grid, 3 );
      styling.configureConstraintsForEvenRows( grid, 3 );
      setButtonSizePolicy();
      
      setPadding( new Insets( PADDING ) );
      setCenter( grid );
   }//End Constructor
   
   /**
    * Convenience setter for the {@link Button} resize policy.
    */
   private void setButtonSizePolicy(){
      for ( DirectionControlType direction : DirectionControlType.values() ) {
         buttons.get( direction ).setMaxSize( Double.MAX_VALUE, Double.MAX_VALUE );
      }
   }//End Method
   
   /**
    * Method to set the {@link DirectionControlListener} for the {@link Button} presses.
    * @param listener the {@link DirectionControlListener}.
    */
   public void setActionListener( DirectionControlListener listener ) {
      for ( DirectionControlType direction : DirectionControlType.values() ) {
         if ( listener == null ) {
            buttons.get( direction ).setOnAction( null );
         } else {
            buttons.get( direction ).setOnAction( e -> listener.action( direction ) );
         }
      }
   }//End Method
   
   GridPane grid(){
      return grid;
   }//End Method

   Button buttonFor( DirectionControlType type ) {
      return buttons.get( type );
   }//End Method

}//End Class
