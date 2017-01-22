/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.core;

import uk.dangrew.jtt.api.sources.ExternalApi;
import uk.dangrew.jtt.api.sources.JenkinsApiImpl;
import uk.dangrew.jtt.credentials.login.JenkinsLogin;
import uk.dangrew.jtt.main.digest.SystemDigestController;

/**
 * The {@link JenkinsApiConnector} is responsible for connecting to the {@link JenkinsApiImpl}. This is 
 * a simple extraction to allow login for other systems.
 */
public class JenkinsApiConnector {

   private final JttApplicationController applicationController;
   
   /**
    * Constructs a new {@link JenkinsApiConnector}.
    */
   public JenkinsApiConnector() {
      this( new JttApplicationController() );
   }//End Constructor
   
   /**
    * Constructs a new {@link JenkinsApiConnector}.
    * @param applicationController the {@link JttApplicationController}.
    */
   JenkinsApiConnector( JttApplicationController applicationController ){
      this.applicationController = applicationController;
   }//End Constructor
   
   /**
    * Method to connect to the {@link JenkinsApiImpl}.
    * @param digestController the {@link SystemDigestController} for presenting login information.
    * @return the {@link ExternalApi} is successful, null otherwise.
    */
   public ExternalApi connect( SystemDigestController digestController ) {
      ExternalApi api = new JenkinsApiImpl();
      
      if ( !applicationController.login( new JenkinsLogin( api, digestController.getDigestViewer() ) ) ) {
         return null;
      }
      
      return api;
   }//End Method

}//End Class
