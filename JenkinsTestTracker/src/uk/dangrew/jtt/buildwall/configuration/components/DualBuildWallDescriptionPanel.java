/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.components;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;

/**
 * the {@link DualBuildWallDescriptionPanel} provides a simple panel that describes what the
 * configuration of the dual build wall is.
 */
public class DualBuildWallDescriptionPanel extends GridPane {
   
   static final String FIRST_SENTENCE = 
            "This area of the configuration allows you to customise how the "
            + "dual build wall looks and behaves. ";
   static final String SECOND_PARAGRAPH = 
            "The dual wall itself has properties to control the layout of the "
            + "walls within it such as orientation. The individual walls themselves "
            + "can be configured. That will be the most useful area to consider "
            + "since it allows you to customise the content of the walls.";

   private final Label firstSentence;
   private final Label firstGap;
   private final Label secondParagraph;
   
   /**
    * Constructs a new {@link DualBuildWallDescriptionPanel}.
    */
   public DualBuildWallDescriptionPanel() {
      JavaFxStyle styling = new JavaFxStyle();
      
      add( 
               firstSentence = styling.createWrappedTextLabel( FIRST_SENTENCE ), 
      0, 0 );
      
      add( 
               firstGap = new Label(), 
      0, 1 );
      
      add( 
               secondParagraph = styling.createWrappedTextLabel( SECOND_PARAGRAPH ), 
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
