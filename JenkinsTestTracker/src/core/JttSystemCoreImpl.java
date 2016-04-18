/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package core;

import java.util.Timer;

import api.sources.ExternalApi;
import synchronisation.model.TimeKeeper;
import synchronisation.time.BuildProgressor;
import synchronisation.time.JobUpdater;

/**
 * {@link JenkinsTestTrackerCoreImpl} that works with the full system.
 */
public class JttSystemCoreImpl extends JenkinsTestTrackerCoreImpl {

   /**
    * Constructs a new {@link JttSystemCoreImpl}.
    * @param api the {@link ExternalApi} connecting to.
    */
   public JttSystemCoreImpl( ExternalApi api ) {
      super( api );
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override void initialiseTimeKeepers() {
      TimeKeeper jobUpdater = new JobUpdater( new Timer(), getJenkinsProcessing(), 5000l );
      TimeKeeper buildProgressor = new BuildProgressor( new Timer(), getJenkinsDatabase(), 1000l );
      setTimeKeepers( jobUpdater, buildProgressor );
   }//End Method

}//End Class
