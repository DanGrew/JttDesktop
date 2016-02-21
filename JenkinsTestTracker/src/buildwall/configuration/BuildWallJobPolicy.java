/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.configuration;

import java.util.function.Predicate;
import model.jobs.JenkinsJob;

/**
 * The {@link BuildWallJobPolicy} defines the types of rules that can be applied
 * for displaying jobs.
 */
public enum BuildWallJobPolicy {
   
   AlwaysShow( job -> { return true; } ),
   
   OnlyShowFailures( job -> {
      switch ( job.lastBuildStatusProperty().get() ) {
         case FAILURE:
         case UNSTABLE:
            return true;
         case ABORTED:
         case NOT_BUILT:
         case SUCCESS:
         case UNKNOWN:
         default:
            return false;
      }
   } ),
   
   NeverShow( job -> { return false; } );
   
   private Predicate< JenkinsJob > deciderFunction;
   
   /**
    * Constructs a new {@link BuildWallJobPolicy}.
    * @param deciderFunction the {@link Predicate} decider function.
    */
   private BuildWallJobPolicy( Predicate< JenkinsJob > deciderFunction ) {
      this.deciderFunction = deciderFunction;
   }//End Constructor

   /**
    * Method to determine whether the given {@link JenkinsJob} should be shown 
    * according to this rule.
    * @param job the {@link JenkinsJob} in question.
    * @return true if it can be displayed, false otherwise.
    */
   public boolean shouldShow( JenkinsJob job ) {
      return deciderFunction.test( job );
   }//End Method

}//End Class
