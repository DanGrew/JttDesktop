/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.properties;

import java.util.function.Predicate;

import uk.dangrew.jtt.model.jobs.BuildState;
import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link BuildWallJobPolicy} defines the types of rules that can be applied
 * for displaying jobs.
 */
public enum BuildWallJobPolicy {
   
   AlwaysShow( job -> true ),
   
   OnlyShowFailures( job -> {
      switch ( job.buildProperty().get().getValue() ) {
         case FAILURE:
         case UNSTABLE:
         case ABORTED:
            return true;
         case NOT_BUILT:
         case SUCCESS:
         case UNKNOWN:
         default:
            return false;
      }
   } ),
   
   OnlyShowPassing( job -> {
      switch ( job.buildProperty().get().getValue() ) {
         case SUCCESS:
            return true;
         case UNSTABLE:
         case ABORTED:
         case FAILURE:
         case NOT_BUILT:
         case UNKNOWN:
         default:
            return false;
      }
   } ),
   
   OnlyShowBuilding( job -> job.buildStateProperty().get().equals( BuildState.Building ) ),
   
   NeverShow( job -> false );
   
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
