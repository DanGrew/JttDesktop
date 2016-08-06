/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.configuration.system;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.versioning.Versioning;

/**
 * the {@link SystemVersionPanel} provides a simple panel that describes what the
 * version of the system is.
 */
public class SystemVersionPanel extends GridPane {
   
   static final String FIRST_SENTENCE = 
            "Jenkins Test Tracker: ";
   static final String SECOND_PARAGRAPH = 
            "Designed, developed and produced by Dan Grew. This project started as a way "
            + "to learn more about creating a product, about developing something with "
            + "good principles from it's first lines to full releases, automating as much "
            + "of the process as possible and understanding technical debt and how to manage "
            + "it.";

   private final Label firstSentence;
   private final Label firstGap;
   private final Label secondParagraph;
   
   /**
    * Constructs a new {@link SystemVersionPanel}.
    */
   public SystemVersionPanel() {
      JavaFxStyle styling = new JavaFxStyle();
      Versioning versionig = new Versioning();
      
      add( 
               firstSentence = styling.createWrappedTextLabel( FIRST_SENTENCE + versionig.getVersionNumber() ), 
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
