/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.wallbuilder;

import javafx.scene.paint.Color;

/**
 * {@link ContentAreaColours} provides colour preferences and applications for {@link ContentArea}.
 */
class ContentAreaColours {
   
   static final double STROKE_THICKNESS = 5;
   
   static final Color SELECTED_BACKGROUND_COLOUR = Color.GREEN;
   static final Color SELECTED_BORDER_COLOUR = Color.DARKGREEN;
   
   static final Color UNSELECTED_BACKGROUND_COLOUR = Color.BLUE;
   static final Color UNSELECTED_BORDER_COLOUR = Color.DARKBLUE;
   
   /**
    * Method to colour the {@link ContentArea} for selection.
    * @param area the {@link ContentArea}.
    */
   void applySelectedColours( ContentArea area ) {
      area.setFill( SELECTED_BACKGROUND_COLOUR );
      area.setStroke( SELECTED_BORDER_COLOUR );
      area.setStrokeWidth( STROKE_THICKNESS );
   }//End Method

   /**
    * Method to colour the {@link ContentArea} when not selected.
    * @param area the {@link ContentArea}.
    */
   void applyUnselectedColours( ContentArea area ) {
      area.setFill( UNSELECTED_BACKGROUND_COLOUR );
      area.setStroke( UNSELECTED_BORDER_COLOUR );
      area.setStrokeWidth( STROKE_THICKNESS );
   }//End Method

}//End Class
