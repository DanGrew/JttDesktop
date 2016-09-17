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
 * the {@link UserAssignmentsDescriptionPanel} provides a simple panel that describes what the
 * configuration of the user assignments is.
 */
public class UserAssignmentsDescriptionPanel extends SimpleDescriptionPanel {
   
   static final String FIRST_SENTENCE = 
            "This area of the configuration allows you to customise how the "
            + "user assignment system works. ";
   static final String SECOND_PARAGRAPH = 
            "It is intended track assignments to users such as fixing builds, "
            + "where their progress will be over time and needs to be monitored. "
            + "This can include, but isn't limited to fixing builds, investigating "
            + "hung builds, etc.";

   /**
    * Constructs a new {@link UserAssignmentsDescriptionPanel}.
    */
   public UserAssignmentsDescriptionPanel() {
      super( FIRST_SENTENCE, SECOND_PARAGRAPH );
   }//End Constructor
}//End Class
