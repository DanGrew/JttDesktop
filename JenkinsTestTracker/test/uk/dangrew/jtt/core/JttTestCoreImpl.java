/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.core;

import java.time.Clock;

import org.mockito.Mockito;

import uk.dangrew.jtt.api.sources.ExternalApi;
import uk.dangrew.jtt.core.JenkinsTestTrackerCoreImpl;
import uk.dangrew.jtt.synchronisation.model.TimeKeeper;
import uk.dangrew.jtt.synchronisation.time.BuildProgressor;
import uk.dangrew.jtt.synchronisation.time.JobUpdater;

/**
 * The {@link JttTestCoreImpl} provides a test environment for the {@link JenkinsTestTrackerCoreImpl}.
 */
public class JttTestCoreImpl extends JenkinsTestTrackerCoreImpl {

   private Clock simulationClock;
   
   /**
    * Constructs a new {@link JttSystemCoreImpl}.
    * @param api the {@link ExternalApi} to connect to.
    */
   public JttTestCoreImpl( ExternalApi api ) {
      super( api );
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void initialiseTimeKeepers() {
      simulationClock = Mockito.mock( Clock.class );
      TimeKeeper jobUpdater = new JobUpdater( getJenkinsProcessing(), null, null );
      TimeKeeper buildProgressor = new BuildProgressor( getJenkinsDatabase(), null, simulationClock, null );
      setTimeKeepers( jobUpdater, buildProgressor );
   }//End Method
   
   /**
    * Method to set the simulated time for progressing the environment.
    * @param millis the current system time in milliseconds.
    */
   public void setSimulatedTime( long millis ) {
      Mockito.when( simulationClock.millis() ).thenReturn( millis );
      getBuildProgressor().poll();
   }//End Method

   /**
    * Getter for the {@link Clock} simulating the time.
    * @return the {@link Clock}.
    */
   Clock getClock() {
      return simulationClock;
   }//End Method

}//End Class
