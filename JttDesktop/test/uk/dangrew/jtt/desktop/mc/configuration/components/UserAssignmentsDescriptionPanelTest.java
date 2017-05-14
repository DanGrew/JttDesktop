/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.configuration.components;

import javafx.util.Pair;
import uk.dangrew.jtt.desktop.configuration.content.SimpleDescriptionPanel;
import uk.dangrew.jtt.desktop.configuration.content.SimpleDescriptionPanelTest;

/**
 * {@link UserAssignmentsDescriptionPanel} test.
 */
public class UserAssignmentsDescriptionPanelTest extends SimpleDescriptionPanelTest {

   /**
    * {@inheritDoc}
    */
   @Override protected Pair< String, String > getSentences() {
      return new Pair<>( UserAssignmentsDescriptionPanel.FIRST_SENTENCE, UserAssignmentsDescriptionPanel.SECOND_PARAGRAPH );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override protected SimpleDescriptionPanel constructSut() {
      return new UserAssignmentsDescriptionPanel();
   }//End Method

}//End Class
