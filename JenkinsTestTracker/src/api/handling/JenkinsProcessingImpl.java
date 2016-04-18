/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package api.handling;

import model.jobs.JenkinsJob;
import storage.database.JenkinsDatabase;

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
      for ( JenkinsJob job : database.jenkinsJobs() ) {
         updateJobDetails( job );
         digest.updatedJob( job );
         
//         if ( job.lastBuildStatusProperty().get() == BuildResultStatus.UNSTABLE ) {
//            updateTestResults( job );
//         }
      }
      digest.jobsUpdated();
   }//End Method
   
}//End Class
