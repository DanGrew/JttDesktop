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
import data.json.jobs.JsonJobImporter;
import data.json.jobs.JsonJobImporterImpl;
import model.jobs.JenkinsJob;
import storage.database.JenkinsDatabase;

/**
 * {@link JenkinsFetcherImpl} provides an implementation of the {@link JenkinsFetcher} interface
 * for updating {@link JenkinsJob}s.
 */
public class JenkinsFetcherImpl implements JenkinsFetcher {

   private ExternalApi externalApi;
   private JsonJobImporter jobsImporter;
   
   /**
    * Constructs a new {@link JenkinsFetcherImpl}.
    * @param externalApi the {@link ExternalApi} to retrieve updates from.
    */
   public JenkinsFetcherImpl( ExternalApi externalApi ) {
      this.externalApi = externalApi;
      jobsImporter = new JsonJobImporterImpl();
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void updateBuildState( JenkinsJob jenkinsJob ) {
      String response = externalApi.getLastBuildBuildingState( jenkinsJob );
      jobsImporter.updateBuildState( jenkinsJob, response );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void updateJobDetails( JenkinsJob jenkinsJob ) {
      if ( jenkinsJob == null ) return;
      
      updateBuildState( jenkinsJob );
      switch ( jenkinsJob.buildStateProperty().get() ) {
         case Building:
            //Not useful to interrupt during build.
            break;
         case Built:
            String response = externalApi.getLastBuildJobDetails( jenkinsJob );
            jobsImporter.updateJobDetails( jenkinsJob, response );
            break;
         default:
            break;
         
      }
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void fetchJobs( JenkinsDatabase database ) {
      if ( database == null ) return;
      String response = externalApi.getJobsList();
      jobsImporter.importJobs( database, response );
   }//End Method

}//End Class
