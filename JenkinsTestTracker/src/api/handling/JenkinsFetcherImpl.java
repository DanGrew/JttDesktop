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
import model.jobs.JenkinsJob;

/**
 * {@link JenkinsFetcherImpl} provides an implementation of the {@link Fetcher} interface
 * for updating {@link JenkinsJob}s.
 */
public class JenkinsFetcherImpl implements Fetcher {

   private ExternalApi externalApi;
   private JsonJobImporter jobsImporter;
   
   /**
    * Constructs a new {@link JenkinsFetcherImpl}.
    * @param externalApi the {@link ExternalApi} to retrieve updates from.
    */
   public JenkinsFetcherImpl( ExternalApi externalApi ) {
      this.externalApi = externalApi;
      jobsImporter = new JsonJobImporter();
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

}//End Class
