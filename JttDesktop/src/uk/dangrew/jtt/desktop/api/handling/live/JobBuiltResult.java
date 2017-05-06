/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.api.handling.live;

import uk.dangrew.jtt.desktop.buildwall.effects.sound.BuildResultStatusChange;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link JobBuiltResult} provides a {@link BuildResultStatusChange} for a specific {@link JenkinsJob}.
 */
public class JobBuiltResult extends BuildResultStatusChange {

   private final JenkinsJob job;
   
   /**
    * Constructs a new {@link JobBuiltResult}.
    * @param job the {@link JenkinsJob}.
    * @param previous the previous {@link BuildResultStatus}.
    * @param current the current {@link BuildResultStatus}.
    */
   public JobBuiltResult( JenkinsJob job, BuildResultStatus previous, BuildResultStatus current ) {
      super( previous, current );
      this.job = job;
   }//End Constructor
   
   /**
    * Getter for the associated {@link JenkinsJob}.
    * @return the {@link JenkinsJob}.
    */
   public JenkinsJob getJenkinsJob(){
      return job;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ( ( job == null ) ? 0 : job.hashCode() );
      return result;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public boolean equals( Object obj ) {
      if ( this == obj ) {
         return true;
      }
      if ( !super.equals( obj ) ) {
         return false;
      }
      if ( getClass() != obj.getClass() ) {
         return false;
      }
      JobBuiltResult other = ( JobBuiltResult ) obj;
      if ( job == null ) {
         if ( other.job != null ) {
            return false;
         }
      } else if ( !job.equals( other.job ) ) {
         return false;
      }
      return true;
   }//End Method
   
}//End Class
