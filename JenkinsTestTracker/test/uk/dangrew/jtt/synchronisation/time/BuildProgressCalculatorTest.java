/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.synchronisation.time;

import java.time.Clock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import uk.dangrew.jtt.model.jobs.BuildState;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;
import uk.dangrew.jtt.synchronisation.time.BuildProgressCalculator;
import uk.dangrew.jtt.utility.TestCommon;

/**
 * {@link BuildProgressCalculator} test.
 */
@RunWith(JUnitParamsRunner.class)
public class BuildProgressCalculatorTest {

   @Mock private Clock clock;
   private JenkinsDatabase database;
   private BuildProgressCalculator systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      database = new JenkinsDatabaseImpl();
      database.store( new JenkinsJobImpl( "anything" ) );
      database.jenkinsJobs().get( 0 ).buildStateProperty().set( BuildState.Building );
      database.store( new JenkinsJobImpl( "something" ) );
      database.jenkinsJobs().get( 1 ).buildStateProperty().set( BuildState.Building );
      systemUnderTest = new BuildProgressCalculator( database, clock );
   }//End Method
   
   /**
    * Method to define the test cases for the calculation.
    * @return the array of object input arrays.
    */
   @SuppressWarnings("unused") //Reflection - JUnitParams. 
   private Object[] shouldCalculateProgressCases() {
      return new Object[]{
                   new Object[]{ 
                            467, 
                            0, 467,
                            100, 367 
                   },
                   new Object[]{ 
                            0, 
                            0, 0,
                            0, 0 
                   },
                   new Object[]{ 
                            1000, 
                            2000, 0,
                            1000, 0 
                   }
              };
   }//End Method
   
   /**
    * Prove that {@link JenkinsJob}s are updated correctly.
    * @param currentTime the current time being simulated.
    * @param startTime the first job start time.
    * @param expectedProgress the first job expected progress.
    * @param startTime2 the second job start time.
    * @param expectedProgress2 the second job expected progress. 
    */
   @Parameters( method = "shouldCalculateProgressCases" )
   @Test public void shouldCalculateProgress( 
            long currentTime,
            long startTime, long expectedProgress,
            long startTime2, long expectedProgress2
   ) {
      Mockito.when( clock.millis() ).thenReturn( currentTime );
      
      JenkinsJob job = database.jenkinsJobs().get( 0 );
      job.buildTimestampProperty().set( startTime );
      
      JenkinsJob job2 = database.jenkinsJobs().get( 1 );
      job2.buildTimestampProperty().set( startTime2 );
      
      systemUnderTest.run();
      
      Assert.assertEquals( expectedProgress, job.currentBuildTimeProperty().doubleValue(), TestCommon.precision() );
      Assert.assertEquals( expectedProgress2, job2.currentBuildTimeProperty().doubleValue(), TestCommon.precision() );
   }//End Method
   
   @Test public void shouldNotCalculateProgressIfJobBuilt(){
      Mockito.when( clock.millis() ).thenReturn( 10l );
      
      JenkinsJob job = database.jenkinsJobs().get( 0 );
      job.buildTimestampProperty().set( 0L );
      job.buildStateProperty().set( BuildState.Built );
      
      JenkinsJob job2 = database.jenkinsJobs().get( 1 );
      job2.buildTimestampProperty().set( 0L );
      
      Assert.assertEquals( 0, job.currentBuildTimeProperty().doubleValue(), TestCommon.precision() );
      Assert.assertEquals( 0, job2.currentBuildTimeProperty().doubleValue(), TestCommon.precision() );
      
      systemUnderTest.run();
      
      Assert.assertEquals( 0, job.currentBuildTimeProperty().doubleValue(), TestCommon.precision() );
      Assert.assertEquals( 10, job2.currentBuildTimeProperty().doubleValue(), TestCommon.precision() );
   }//End Method

}//End Class
