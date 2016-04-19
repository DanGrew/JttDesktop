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
import data.JsonTestResultsImporter;
import data.json.jobs.JsonJobImporter;
import data.json.jobs.JsonJobImporterImpl;
import data.json.tests.JsonTestResultsImporterImpl;
import data.json.users.JsonUserImporter;
import data.json.users.JsonUserImporterImpl;
import model.jobs.JenkinsJob;
import storage.database.JenkinsDatabase;

/**
 * {@link JenkinsFetcherImpl} provides an implementation of the {@link JenkinsFetcher} interface
 * for updating {@link JenkinsJob}s.
 */
public class JenkinsFetcherImpl implements JenkinsFetcher {

   private ExternalApi externalApi;
   private JsonJobImporter jobsImporter;
   private JsonUserImporter usersImporter;
   private JsonTestResultsImporter testsImporter;
   private JenkinsFetcherDigest digest;
   
   /**
    * Constructs a new {@link JenkinsFetcherImpl}.
    * @param database the {@link JenkinsDatabase} to populate and update.
    * @param externalApi the {@link ExternalApi} to retrieve updates from.
    */
   public JenkinsFetcherImpl( JenkinsDatabase database, ExternalApi externalApi ) {
      this( database, externalApi, new JenkinsFetcherDigest() );
   }//End Constructor

   /**
    * Constructs a new {@link JenkinsFetcherImpl}.
    * @param database the {@link JenkinsDatabase} to populate and update.
    * @param externalApi the {@link ExternalApi} to retrieve updates from.
    * @param digest the {@link JenkinsFetcherDigest} to use.
    */
   JenkinsFetcherImpl( JenkinsDatabase database, ExternalApi externalApi, JenkinsFetcherDigest digest ) {
      if ( database == null ) throw new IllegalArgumentException( "Null database provided." );
      if ( externalApi == null ) throw new IllegalArgumentException( "Null api provided." );
      
      this.externalApi = externalApi;
      jobsImporter = new JsonJobImporterImpl( database, this );
      usersImporter = new JsonUserImporterImpl( database );
      testsImporter = new JsonTestResultsImporterImpl( database );
      
      this.digest = digest;
      this.digest.attachSource( this );
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void updateBuildState( JenkinsJob jenkinsJob ) {
      digest.fetching( JenkinsFetcherDigest.BUILD_STATE, jenkinsJob );
      String response = externalApi.getLastBuildBuildingState( jenkinsJob );
      digest.parsing( JenkinsFetcherDigest.BUILD_STATE, jenkinsJob );
      jobsImporter.updateBuildState( jenkinsJob, response );
      digest.updated( JenkinsFetcherDigest.BUILD_STATE, jenkinsJob );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void updateJobDetails( JenkinsJob jenkinsJob ) {
      if ( jenkinsJob == null ) return;
      
      updateBuildState( jenkinsJob );
      
      if ( isOutDated( jenkinsJob ) ) {
         digest.fetching( JenkinsFetcherDigest.JOB_DETAIL, jenkinsJob );
         String response = externalApi.getLastBuildJobDetails( jenkinsJob );
         digest.parsing( JenkinsFetcherDigest.JOB_DETAIL, jenkinsJob );
         jobsImporter.updateJobDetails( jenkinsJob, response );
         digest.updated( JenkinsFetcherDigest.JOB_DETAIL, jenkinsJob );
      }
   }//End Method
   
   private boolean isOutDated( JenkinsJob job ) {
      BuildState buildState = job.buildStateProperty().get();
      if ( buildState == BuildState.Built ) {
         return true;
      }
      
      int lastBuild = job.lastBuildNumberProperty().get();
      int currentBuild = job.currentBuildNumberProperty().get();
      
      int difference = currentBuild - lastBuild;
      return difference > 1;
   }

   /**
    * {@inheritDoc}
    */
   @Override public void fetchJobs() {
      digest.fetching( JenkinsFetcherDigest.JOBS );
      String response = externalApi.getJobsList();
      digest.parsing( JenkinsFetcherDigest.JOBS );
      jobsImporter.importJobs( response );
      digest.updated( JenkinsFetcherDigest.JOBS );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void fetchUsers() {
      digest.fetching( JenkinsFetcherDigest.USERS );
      String response = externalApi.getUsersList();
      digest.parsing( JenkinsFetcherDigest.USERS );
      usersImporter.importUsers( response );
      digest.updated( JenkinsFetcherDigest.USERS );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void updateTestResults( JenkinsJob jenkinsJob ) {
      if ( jenkinsJob == null ) return;
      if ( !jenkinsJob.testResultsAreSynchronizedProperty().get() ) return;
      
      String response = externalApi.getLatestTestResultsWrapped( jenkinsJob );
      testsImporter.updateTestResults( jenkinsJob, response );
      response = externalApi.getLatestTestResultsUnwrapped( jenkinsJob );
      testsImporter.updateTestResults( jenkinsJob, response );
   }//End Method

}//End Class
