/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.handling;

import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.sd.core.message.Messages;
import uk.dangrew.sd.core.progress.Progresses;
import uk.dangrew.sd.core.source.SourceImpl;
import uk.dangrew.sd.digest.object.ObjectDigestImpl;

/**
 * {@link JenkinsProcessingDigest} provides an {@link ObjectDigestImpl} for the {@link JenkinsProcessing}.
 */
public class JenkinsProcessingDigest extends ObjectDigestImpl {
   
   static final String UPDATED_ALL_JOBS = "Updated all jobs.";
   static final String UPDATING_JOBS = "Updating jobs";
   static final String JENKINS_PROCESSING_NAME = "Jenkins Api Processing";

   private int jobsToProcess = 0;
   private int jobsProcessed = 0;
   
   /**
    * Method to attach the {@link JenkinsProcessing} as {@link Source} to this {@link ObjectDigestImpl}.
    * @param jenkinsProcessing the {@link JenkinsFetcher} to attach.
    */
   void attachSource( JenkinsProcessing jenkinsProcessing ) {
      super.attachSource( new SourceImpl( jenkinsProcessing, JENKINS_PROCESSING_NAME ) );
   }//End Method
   
   /**
    * Method to indicate that the {@link JenkinsProcessing} has started updating jobs.
    * @param jobCount the number of {@link JenkinsJob}s it intends to update.
    */
   void startUpdatingJobs( int jobCount ) {
      this.jobsToProcess = jobCount;
      this.jobsProcessed = 0;
      progress( Progresses.simpleProgress( 0 ), Messages.simpleMessage( UPDATING_JOBS ) );
   }//End Method

   /**
    * Method to indicate that a {@link JenkinsJob} has been updated.
    * @param job the {@link JenkinsJob} updated.
    */
   void updatedJob( JenkinsJob job ) {
      if ( jobsToProcess == 0 ) throw new IllegalStateException( "Updating jobs not started." );
         
      jobsProcessed++;
      progress( 
               Progresses.simpleProgress( jobsProcessed * 100.0 / jobsToProcess ), 
               Messages.simpleMessage( "Updated " + job.nameProperty().get() ) 
      );
   }//End Method

   /**
    * Method to indicate all jobs have been updated.
    */
   void jobsUpdated() {
      jobsToProcess = 0;
      progress( Progresses.complete(), Messages.simpleMessage( UPDATED_ALL_JOBS ) );
   }//End Method

}//End Class
