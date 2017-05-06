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
import uk.dangrew.jtt.desktop.mc.configuration.components.NotificationsDescriptionPanel;

/**
 * {@link NotificationsDescriptionPanel} test.
 */
public class NotificationsDescriptionPanelTest extends SimpleDescriptionPanelTest {

   /**
    * {@inheritDoc}
    */
   @Override protected Pair< String, String > getSentences() {
      return new Pair<>( NotificationsDescriptionPanel.FIRST_SENTENCE, NotificationsDescriptionPanel.SECOND_PARAGRAPH );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override protected SimpleDescriptionPanel constructSut() {
      return new NotificationsDescriptionPanel();
   }//End Method
}//End Class
