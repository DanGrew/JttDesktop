/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.statistics.configuration.components;

import javafx.util.Pair;
import uk.dangrew.jtt.configuration.content.SimpleDescriptionPanel;
import uk.dangrew.jtt.configuration.content.SimpleDescriptionPanelTest;

public class StatisticsDescriptionPanelTest extends SimpleDescriptionPanelTest {

   /**
    * {@inheritDoc}
    */
   @Override protected Pair< String, String > getSentences() {
      return new Pair<>( StatisticsDescriptionPanel.FIRST_SENTENCE, StatisticsDescriptionPanel.SECOND_PARAGRAPH );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override protected SimpleDescriptionPanel constructSut() {
      return new StatisticsDescriptionPanel();
   }//End Method
}//End Class