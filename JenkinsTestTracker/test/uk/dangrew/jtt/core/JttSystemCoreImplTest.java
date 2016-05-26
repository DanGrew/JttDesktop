/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import uk.dangrew.jtt.api.sources.ExternalApi;
import uk.dangrew.jtt.core.JttSystemCoreImpl;

/**
 * {@link JttSystemCoreImpl} test.
 */
public class JttSystemCoreImplTest {

   private JttSystemCoreImpl systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new JttSystemCoreImpl( Mockito.mock( ExternalApi.class ) );
   }//End Method
   
   @Test public void shouldHaveTimersInTimeKeepers() {
      Assert.assertTrue( systemUnderTest.getBuildProgressor().hasTimer() );
      Assert.assertTrue( systemUnderTest.getJobUpdater().hasTimer() );
   }//End Method

}//End Class
