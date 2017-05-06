/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.synchronisation.time;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Timer;
import java.util.TimerTask;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javafx.collections.FXCollections;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;

/**
 * {@link BuildProgressor} test.
 */
public class BuildProgressorTest {

   @Captor private ArgumentCaptor< TimerTask > timerTaskCaptor;
   @Captor private ArgumentCaptor< Long > delayCaptor;
   @Captor private ArgumentCaptor< Long > intervalCaptor;
   
   @Mock private Timer timer;
   private JenkinsDatabase database;
   private Long interval;
   private BuildProgressor systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      interval = 1000l;
      database = Mockito.mock( JenkinsDatabase.class );
      Mockito.when( database.jenkinsJobs() ).thenReturn( FXCollections.observableArrayList() );
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new BuildProgressor( database, timer, interval );
   }//End Method
   
   @Test public void shouldScheduleTimerTaskOnTimer(){
      Mockito.verify( timer ).schedule( 
               timerTaskCaptor.capture(), 
               delayCaptor.capture(),
               intervalCaptor.capture() 
      );
      
      Assert.assertEquals( 1, delayCaptor.getAllValues().size() );
      Assert.assertEquals( JobUpdater.UPDATE_DELAY, delayCaptor.getValue().longValue() );
      
      Assert.assertEquals( 1, intervalCaptor.getAllValues().size() );
      Assert.assertEquals( interval, intervalCaptor.getValue() );
      
      Assert.assertEquals( 1, timerTaskCaptor.getAllValues().size() );
      Assert.assertNotNull( timerTaskCaptor.getValue() );
      
      Mockito.verifyZeroInteractions( database );
      timerTaskCaptor.getValue().run();
      Mockito.verify( database ).jenkinsJobs();
   }//End Method
   
   @Test public void pollShouldRunCalculator(){
      Mockito.verifyZeroInteractions( database );
      systemUnderTest.poll();
      Mockito.verify( database ).jenkinsJobs();
   }//End Method
   
   @Test public void shouldBeAssociatedWith(){
      assertThat( systemUnderTest.isAssociatedWith( database ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( new TestJenkinsDatabaseImpl() ), is( false ) );
   }//End Method
   
}//End Class
