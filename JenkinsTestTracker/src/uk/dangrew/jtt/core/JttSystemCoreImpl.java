/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.core;

import java.util.Timer;

import uk.dangrew.jtt.api.sources.ExternalApi;
import uk.dangrew.jtt.synchronisation.model.TimeKeeper;
import uk.dangrew.jtt.synchronisation.time.BuildProgressor;
import uk.dangrew.jtt.synchronisation.time.JobUpdater;

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
   @Override public void initialiseTimeKeepers() {
      TimeKeeper jobUpdater = new JobUpdater( new Timer(), getJenkinsProcessing(), 5000l );
      TimeKeeper buildProgressor = new BuildProgressor( new Timer(), getJenkinsDatabase(), 1000l );
      setTimeKeepers( jobUpdater, buildProgressor );
   }//End Method

}//End Class
