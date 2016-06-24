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
import uk.dangrew.jtt.buildwall.configuration.style.BuildWallConfigurationStyle;

/**
 * the {@link BuildWallDescriptionPanel} provides a simple panel that describes what the
 * configuration of build walls is.
 */
public class BuildWallDescriptionPanel extends GridPane {
   
   static final String FIRST_SENTENCE = 
            "This area of the configuration allows you to customise how the "
            + "dual build wall looks and behaves. ";
   static final String SECOND_PARAGRAPH = 
            "There are two separate build walls in the dual (as the name suggests!) "
            + "that allow you to have two streams of information, or methods of handling "
            + "state change on the builds. Both the left and right walls are configured "
            + "individually and persisted individually.";

   private final Label firstSentence;
   private final Label firstGap;
   private final Label secondParagraph;
   
   /**
    * Constructs a new {@link BuildWallDescriptionPanel}.
    */
   public BuildWallDescriptionPanel() {
      BuildWallConfigurationStyle styling = new BuildWallConfigurationStyle();
      
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
