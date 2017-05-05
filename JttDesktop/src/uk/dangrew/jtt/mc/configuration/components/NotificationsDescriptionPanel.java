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
 * the {@link NotificationsDescriptionPanel} provides a simple panel that describes what the
 * configuration of notification system is.
 */
public class NotificationsDescriptionPanel extends SimpleDescriptionPanel {
   
   static final String FIRST_SENTENCE = 
            "This area of the configuration allows you to customise how the "
            + "notification system works and behaves. ";
   static final String SECOND_PARAGRAPH = 
            "It is intended to alert you when the state of the system changes, "
            + "such as when builds complete, or changes are committed, etc. These "
            + "can then be actioned using other tools, or just used for reference.";

   /**
    * Constructs a new {@link NotificationsDescriptionPanel}.
    */
   public NotificationsDescriptionPanel() {
      super( FIRST_SENTENCE, SECOND_PARAGRAPH );
   }//End Constructor
}//End Class
