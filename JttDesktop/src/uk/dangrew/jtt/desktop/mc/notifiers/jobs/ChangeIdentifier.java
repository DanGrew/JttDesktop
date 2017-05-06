/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.notifiers.jobs;

import uk.dangrew.jtt.model.jobs.BuildResultStatus;

/**
 * The {@link ChangeIdentifier} is responsible for identify change types of state change
 * in the system.
 */
public class ChangeIdentifier {

   /**
    * Method to identify the change type between the two {@link BuildResultStatus}'s.
    * @param previousStatus the {@link BuildResultStatus} previously.
    * @param newStatus the new {@link BuildResultStatus}.
    * @return the resulting {@link BuildResultStatusHighLevelChange}.
    */
   public BuildResultStatusHighLevelChange identifyChangeType( BuildResultStatus previousStatus, BuildResultStatus newStatus ) {
      if ( previousStatus == newStatus ) {
         return BuildResultStatusHighLevelChange.Unchanged;
      }
      switch( newStatus ) {
         case ABORTED:
         case FAILURE:
         case NOT_BUILT:
         case UNKNOWN:
         case UNSTABLE:
            return BuildResultStatusHighLevelChange.ActionRequired;
         case SUCCESS:
            return BuildResultStatusHighLevelChange.Passed;
         default:
            return null;
      }
   }//End Method
}//End Class
