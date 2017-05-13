/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.api.handling;

import static uk.dangrew.jtt.connection.api.sources.JobRequest.LastBuildTestResultsUnwrappedRequest;
import static uk.dangrew.jtt.connection.api.sources.JobRequest.LastBuildTestResultsWrappedRequest;

import uk.dangrew.jtt.connection.api.sources.ExternalApi;
import uk.dangrew.jtt.desktop.data.json.tests.JsonTestResultsImporter;
import uk.dangrew.jtt.desktop.data.json.tests.JsonTestResultsImporterImpl;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.SystemWideJenkinsDatabaseImpl;

/**
 * {@link JenkinsFetcherImpl} provides an implementation of the {@link JenkinsFetcher} interface
 * for updating {@link JenkinsJob}s.
 */
public class JenkinsFetcherImpl implements JenkinsFetcher {

   private final ExternalApi externalApi;
   private final JsonTestResultsImporter testsImporter;
   private final JenkinsFetcherDigest digest;
   
   /**
    * Constructs a new {@link JenkinsFetcherImpl}.
    * @param externalApi the {@link ExternalApi} to retrieve updates from.
    */
   public JenkinsFetcherImpl( ExternalApi externalApi ) {
      this( new SystemWideJenkinsDatabaseImpl().get(), externalApi, new JenkinsFetcherDigest() );
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
      this.testsImporter = new JsonTestResultsImporterImpl( database );
      
      this.digest = digest;
      this.digest.attachSource( this );
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void updateTestResults( JenkinsJob jenkinsJob ) {
      if ( jenkinsJob == null ) {
         return;
      }
      
      String response = externalApi.executeRequest( LastBuildTestResultsWrappedRequest, jenkinsJob );
      testsImporter.updateTestResults( jenkinsJob, response );
      response = externalApi.executeRequest( LastBuildTestResultsUnwrappedRequest, jenkinsJob );
      testsImporter.updateTestResults( jenkinsJob, response );
   }//End Method

}//End Class
