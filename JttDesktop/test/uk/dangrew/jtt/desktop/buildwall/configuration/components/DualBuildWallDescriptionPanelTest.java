/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components;

import javafx.util.Pair;
import uk.dangrew.jtt.desktop.configuration.content.SimpleDescriptionPanel;
import uk.dangrew.jtt.desktop.configuration.content.SimpleDescriptionPanelTest;

/**
 * {@link DualBuildWallDescriptionPanel} test.
 */
public class DualBuildWallDescriptionPanelTest extends SimpleDescriptionPanelTest {

   /**
    * {@inheritDoc}
    */
   @Override protected Pair< String, String > getSentences() {
      return new Pair<>( DualBuildWallDescriptionPanel.FIRST_SENTENCE, DualBuildWallDescriptionPanel.SECOND_PARAGRAPH );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override protected SimpleDescriptionPanel constructSut() {
      return new DualBuildWallDescriptionPanel();
   }//End Method
}//End Class
