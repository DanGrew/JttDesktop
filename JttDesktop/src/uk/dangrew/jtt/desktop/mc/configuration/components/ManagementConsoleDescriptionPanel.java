/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.configuration.components;

import uk.dangrew.jtt.desktop.configuration.content.SimpleDescriptionPanel;

/**
 * the {@link ManagementConsoleDescriptionPanel} provides a simple panel that describes what the
 * configuration of the management console is.
 */
public class ManagementConsoleDescriptionPanel extends SimpleDescriptionPanel {
   
   static final String FIRST_SENTENCE = 
            "This area of the configuration allows you to customise how the "
            + "management console works and behaves. ";
   static final String SECOND_PARAGRAPH = 
            "It is divided into multiple views and panels that can be individually "
            + "configured based on what you are montioring. They each have subsections "
            + "of configuration based on their properties.";

   /**
    * Constructs a new {@link ManagementConsoleDescriptionPanel}.
    */
   public ManagementConsoleDescriptionPanel() {
      super( FIRST_SENTENCE, SECOND_PARAGRAPH );
   }//End Constructor
}//End Class
