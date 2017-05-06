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
import uk.dangrew.jtt.desktop.mc.configuration.components.ManagementConsoleDescriptionPanel;

/**
 * {@link ManagementConsoleDescriptionPanel} test.
 */
public class ManagementConsoleDescriptionPanelTest extends SimpleDescriptionPanelTest {

   /**
    * {@inheritDoc}
    */
   @Override protected Pair< String, String > getSentences() {
      return new Pair<>( ManagementConsoleDescriptionPanel.FIRST_SENTENCE, ManagementConsoleDescriptionPanel.SECOND_PARAGRAPH );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override protected SimpleDescriptionPanel constructSut() {
      return new ManagementConsoleDescriptionPanel();
   }//End Method
}//End Class
