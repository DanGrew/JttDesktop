/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components;

import uk.dangrew.jtt.desktop.configuration.content.SimpleDescriptionPanel;

/**
 * the {@link DualBuildWallDescriptionPanel} provides a simple panel that describes what the
 * configuration of the dual build wall is.
 */
public class DualBuildWallDescriptionPanel extends SimpleDescriptionPanel {
   
   static final String FIRST_SENTENCE = 
            "This area of the configuration allows you to customise how the "
            + "dual build wall looks and behaves. ";
   static final String SECOND_PARAGRAPH = 
            "The dual wall itself has properties to control the layout of the "
            + "walls within it such as orientation. The individual walls themselves "
            + "can be configured. That will be the most useful area to consider "
            + "since it allows you to customise the content of the walls.";

   /**
    * Constructs a new {@link DualBuildWallDescriptionPanel}.
    */
   public DualBuildWallDescriptionPanel() {
      super( FIRST_SENTENCE, SECOND_PARAGRAPH );
   }//End Constructor
}//End Class
