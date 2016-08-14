/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.notifiers.jobs;

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
    * @return the resulting {@link BuildResultStatusChange}.
    */
   public BuildResultStatusChange identifyChangeType( BuildResultStatus previousStatus, BuildResultStatus newStatus ) {
      if ( previousStatus == newStatus ) {
         return BuildResultStatusChange.Unchanged;
      }
      switch( newStatus ) {
         case ABORTED:
         case FAILURE:
         case NOT_BUILT:
         case UNKNOWN:
         case UNSTABLE:
            return BuildResultStatusChange.ActionRequired;
         case SUCCESS:
            return BuildResultStatusChange.Passed;
         default:
            return null;
      }
   }//End Method
}//End Class
