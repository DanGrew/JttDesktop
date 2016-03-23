/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package api.handling;

import api.sources.ExternalApi;
import core.category.Categories;
import core.message.Messages;
import core.progress.Progresses;
import core.source.Source;
import core.source.SourceImpl;
import digest.object.ObjectDigestImpl;
import model.jobs.JenkinsJob;

/**
 * {@link JenkinsFetcherDigest} provides an {@link ObjectDigestImpl} for the {@link JenkinsFetcher}.
 */
public class JenkinsFetcherDigest extends ObjectDigestImpl {
   
   static final String UPDATED_ALL_JOBS = "Updated all jobs.";
   static final String UPDATING_JOBS = "Updating jobs";
   static final String JENKINS_FETCHER_NAME = "Jenkins Api Fetcher";
   static final String BUILD_STATE = "build state";
   static final String JOB_DETAIL = "job details";
   static final String JOBS = "jobs";

   private int jobsToProcess = 0;
   private int jobsProcessed = 0;
   
   /**
    * Method to attach the {@link JenkinsFetcher} as {@link Source} to this {@link ObjectDigestImpl}.
    * @param jenkinsFetcher the {@link JenkinsFetcher} to attach.
    */
   void attachSource( JenkinsFetcher jenkinsFetcher ) {
      super.attachSource( new SourceImpl( jenkinsFetcher, JENKINS_FETCHER_NAME ) );
   }//End Method
   
   /**
    * Method to show that some information is being fetched from the {@link ExternalApi} for the given {@link JenkinsJob}.
    * @param whatIsBeingFetched {@link String} describing what's being fetched.
    * @param job the {@link JenkinsJob} being fetched for.
    */
   void fetching( String whatIsBeingFetched, JenkinsJob job ) {
      log( Categories.information(), Messages.simpleMessage( "Fetching " + whatIsBeingFetched + " for " + job.nameProperty().get() ) );
   }//End Method
   
   /**
    * Method to show that some information is being parsed from the {@link ExternalApi} for the given {@link JenkinsJob}.
    * @param whatIsBeingParsed {@link String} describing what's being parsed.
    * @param job the {@link JenkinsJob} being parsed for.
    */
   void parsing( String whatIsBeingParsed, JenkinsJob job ) {
      log( Categories.processingSequence(), Messages.simpleMessage( "Parsing " + whatIsBeingParsed + " for " + job.nameProperty().get() ) );
   }//End Method

   /**
    * Method to show that some information has been updated from the {@link ExternalApi} for the given {@link JenkinsJob}.
    * @param whatHasBeenUpdated {@link String} describing what's been updated.
    * @param job the {@link JenkinsJob} updated.
    */
   void updated( String whatHasBeenUpdated, JenkinsJob jenkinsJob ) {
      log( Categories.status(), Messages.simpleMessage( jenkinsJob.nameProperty().get() + " " + whatHasBeenUpdated + " updated" ) );
   }//End Method
   
   /**
    * Method to show that some information is being fetched from the {@link ExternalApi}.
    * @param whatIsBeingFetched {@link String} describing what's being fetched.
    */
   void fetching( String whatIsBeingFetched ) {
      log( Categories.information(), Messages.simpleMessage( "Fetching " + whatIsBeingFetched ) );
   }//End Method
   
   /**
    * Method to show that some information is being parsed from the {@link ExternalApi}.
    * @param whatIsBeingParsed {@link String} describing what's being parsed.
    */
   void parsing( String whatIsBeingParsed ) {
      log( Categories.processingSequence(), Messages.simpleMessage( "Parsing " + whatIsBeingParsed ) );
   }//End Method

   /**
    * Method to show that some information has been updated from the {@link ExternalApi}.
    * @param whatHasBeenUpdated {@link String} describing what's been updated.
    */
   void updated( String whatHasBeenUpdated ) {
      log( Categories.status(), Messages.simpleMessage( whatHasBeenUpdated + " updated" ) );
   }//End Method

   /**
    * Method to indicate that the {@link JenkinsFetcher} has started updating jobs.
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
