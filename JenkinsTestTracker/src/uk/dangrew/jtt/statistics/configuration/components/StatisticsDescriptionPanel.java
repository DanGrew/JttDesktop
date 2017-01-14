/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.statistics.configuration.components;

import uk.dangrew.jtt.configuration.content.SimpleDescriptionPanel;

/**
 * the {@link StatisticsDescriptionPanel} provides a simple panel that describes what the
 * configuration of the statistics is.
 */
public class StatisticsDescriptionPanel extends SimpleDescriptionPanel {
   
   static final String FIRST_SENTENCE = 
            "This area of the configuration allows you to customise how the "
                     + "statistics appear and are calculated. ";
   static final String SECOND_PARAGRAPH = 
            "Statistics are provided for various information in the system "
                     + "providing metrics to measure the systme by, and current state "
                     + "information for maintenance and usability.";

   /**
    * Constructs a new {@link DualBuildWallDescriptionPanel}.
    */
   public StatisticsDescriptionPanel() {
      super( FIRST_SENTENCE, SECOND_PARAGRAPH );
   }//End Constructor
}//End Class
