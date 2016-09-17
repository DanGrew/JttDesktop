/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.configuration.components;

import uk.dangrew.jtt.configuration.content.SimpleDescriptionPanel;

/**
 * the {@link JobProgressDescriptionPanel} provides a simple panel that describes what the
 * configuration of the job progress view is.
 */
public class JobProgressDescriptionPanel extends SimpleDescriptionPanel {
   
   static final String FIRST_SENTENCE = 
            "This area of the configuration allows you to customise how the "
            + "Job Progress view behaves. This is the view that shows the state "
            + "of the builds live, one progress bar per build. ";
   static final String SECOND_PARAGRAPH = 
            "It is intended to show you what the current state of the builds "
            + "that you are interested in, and complete actions related to them "
            + "such as assigning them to users to work on or look at.";

   /**
    * Constructs a new {@link JobProgressDescriptionPanel}.
    */
   public JobProgressDescriptionPanel() {
      super( FIRST_SENTENCE, SECOND_PARAGRAPH );
   }//End Constructor
}//End Class
