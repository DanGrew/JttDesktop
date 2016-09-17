/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.configuration.content;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;

/**
 * the {@link SimpleDescriptionPanel} provides a simple panel that describes what the
 * an element of configuration with two sentences.
 */
public class SimpleDescriptionPanel extends GridPane {
   
   private final Label firstSentence;
   private final Label firstGap;
   private final Label secondParagraph;
   
   /**
    * Constructs a new {@link SimpleDescriptionPanel}.
    * @param firstSentenceText the first sentence text to place.
    * @param secondSentenceText the second sentence text to place.
    */
   protected SimpleDescriptionPanel( String firstSentenceText, String secondSentenceText ) {
      JavaFxStyle styling = new JavaFxStyle();
      
      add( 
               firstSentence = styling.createWrappedTextLabel( firstSentenceText ), 
      0, 0 );
      
      add( 
               firstGap = new Label(), 
      0, 1 );
      
      add( 
               secondParagraph = styling.createWrappedTextLabel( secondSentenceText ), 
      0, 2 );
   }//End Constructor
   
   Label firstSentence() {
      return firstSentence;
   }//End Method

   Label firstGap() {
      return firstGap;
   }//End Method
   
   Label secondParagraph() {
      return secondParagraph;
   }//End Method
}//End Class
