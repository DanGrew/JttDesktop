/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.handling;

import java.util.ArrayList;
import java.util.List;

import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * {@link JenkinsProcessing} is a basic implementation of the {@link JenkinsProcessing}.
 * The logic here determines whether job details, test results, etc should be updated.
 */
public class JenkinsProcessingImpl implements JenkinsProcessing {
   
   private final JenkinsDatabase database;
   private final JenkinsFetcher jenkinFetcher;
   private final JenkinsProcessingDigest digest;
   
   /**
    * Constructs a new {@link JenkinsProcessing}.
    * @param database the {@link JenkinsDatabase} associated
    * @param fetcher the {@link JenkinsFetcher} to retrieve information for jenkins.
    */
   public JenkinsProcessingImpl( JenkinsDatabase database, JenkinsFetcher fetcher ) {
      this( database, fetcher, new JenkinsProcessingDigest() );
   }//End Constructor
   
   /**
    * Constructs a new {@link JenkinsProcessing}.
    * @param database the {@link JenkinsDatabase} associated
    * @param fetcher the {@link JenkinsFetcher} to retrieve information for jenkins.
    * @param digest the {@link JenkinsProcessingDigest} to provide information to.
    */
   JenkinsProcessingImpl( JenkinsDatabase database, JenkinsFetcher fetcher, JenkinsProcessingDigest digest ) {
      if ( database == null ) throw new IllegalArgumentException( "Null database provided." );
      if ( fetcher == null ) throw new IllegalArgumentException( "Null JenkinsFetcher provided." );
      
      this.database = database;
      this.jenkinFetcher = fetcher;
      this.digest = digest;
      this.digest.attachSource( this );
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override public void updateBuildState( JenkinsJob jenkinsJob ) {
      jenkinFetcher.updateBuildState( jenkinsJob );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void updateJobDetails( JenkinsJob jenkinsJob ) {
      jenkinFetcher.updateJobDetails( jenkinsJob );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void fetchJobs() {
      jenkinFetcher.fetchJobs();
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void fetchUsers() {
      jenkinFetcher.fetchUsers();
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void updateTestResults( JenkinsJob jenkinsJob ) {
      jenkinFetcher.updateTestResults( jenkinsJob );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void fetchJobsAndUpdateDetails() {
      fetchJobs();
      
      digest.startUpdatingJobs( database.jenkinsJobs().size() );
      
      List< JenkinsJob > toProcess = new ArrayList<>();
      database.jenkinsJobs().forEach( toProcess::add );
      
      for ( JenkinsJob job : toProcess ) {
         int previousBuildNumber = job.lastBuildNumberProperty().get();
         BuildResultStatus previousStatus = job.lastBuildStatusProperty().get();
         
         updateJobDetails( job );
         digest.updatedJob( job );
         
         updateTestsIfStateChanged( job, previousBuildNumber, previousStatus );
      }
      digest.jobsUpdated();
   }//End Method

   /**
    * Method to update the tests for the {@link JenkinsJob} is the state has changed in a way that requires
    * tests to be reimported.
    * @param job the {@link JenkinsJob} to update for.
    * @param previousBuildNumber the build number before the details were updated.
    * @param previousStatus the {@link BuildResultStatus} before the details were updated.
    */
   private void updateTestsIfStateChanged( JenkinsJob job, int previousBuildNumber, BuildResultStatus previousStatus ) {
      int lastBuildNumber = job.lastBuildNumberProperty().get();
      
      if ( previousBuildNumber == lastBuildNumber ) {
         return;
      }
      
      if ( previousStatus == BuildResultStatus.UNSTABLE ) {
         updateTestResults( job );
         return;
      }
      
      if ( job.lastBuildStatusProperty().get() == BuildResultStatus.UNSTABLE ) {
         updateTestResults( job );
         return;
      }
   }//End Method
   
}//End Class
