/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.handling;

import org.json.JSONObject;

import uk.dangrew.jtt.api.sources.ExternalApi;
import uk.dangrew.jtt.data.JsonTestResultsImporter;
import uk.dangrew.jtt.data.json.conversion.ApiResponseToJsonConverter;
import uk.dangrew.jtt.data.json.jobs.JsonJobImporter;
import uk.dangrew.jtt.data.json.jobs.JsonJobImporterImpl;
import uk.dangrew.jtt.data.json.tests.JsonTestResultsImporterImpl;
import uk.dangrew.jtt.data.json.users.JsonUserImporter;
import uk.dangrew.jtt.data.json.users.JsonUserImporterImpl;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * {@link JenkinsFetcherImpl} provides an implementation of the {@link JenkinsFetcher} interface
 * for updating {@link JenkinsJob}s.
 */
public class JenkinsFetcherImpl implements JenkinsFetcher {

   private final ExternalApi externalApi;
   private final JsonJobImporter jobsImporter;
   private final JsonUserImporter usersImporter;
   private final JsonTestResultsImporter testsImporter;
   private final JenkinsFetcherDigest digest;
   private final ApiResponseToJsonConverter converter;
   
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
      this.jobsImporter = new JsonJobImporterImpl( database, this );
      this.usersImporter = new JsonUserImporterImpl( database );
      this.testsImporter = new JsonTestResultsImporterImpl( database );
      
      this.digest = digest;
      this.digest.attachSource( this );
      
      this.converter = new ApiResponseToJsonConverter();
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void updateBuildState( JenkinsJob jenkinsJob ) {
      digest.fetching( JenkinsFetcherDigest.BUILD_STATE, jenkinsJob );
      String response = externalApi.getLastBuildBuildingState( jenkinsJob );
      digest.parsing( JenkinsFetcherDigest.BUILD_STATE, jenkinsJob );
      
      JSONObject converted = converter.convert( response );
      jobsImporter.updateBuildState( jenkinsJob, converted );
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
         
         JSONObject converted = converter.convert( response );
         jobsImporter.updateJobDetails( jenkinsJob, converted );
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
      
      JSONObject converted = converter.convert( response );
      jobsImporter.importJobs( converted );
      digest.updated( JenkinsFetcherDigest.JOBS );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void fetchUsers() {
      digest.fetching( JenkinsFetcherDigest.USERS );
      String response = externalApi.getUsersList();
      digest.parsing( JenkinsFetcherDigest.USERS );
      
      JSONObject converted = converter.convert( response );
      usersImporter.importUsers( converted );
      digest.updated( JenkinsFetcherDigest.USERS );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void updateTestResults( JenkinsJob jenkinsJob ) {
      if ( jenkinsJob == null ) return;
      
      String response = externalApi.getLatestTestResultsWrapped( jenkinsJob );
      testsImporter.updateTestResults( jenkinsJob, response );
      response = externalApi.getLatestTestResultsUnwrapped( jenkinsJob );
      testsImporter.updateTestResults( jenkinsJob, response );
   }//End Method

}//End Class
