/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.utility.mockito;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;

/**
 * {@link DoAnswerNullReturn} test.
 */
public class DoAnswerNullReturnTest {
   
   @Mock private Runnable runnable;
   private DoAnswerNullReturn systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new DoAnswerNullReturn( runnable );
   }//End Method

   @Test public void shouldCallRunnableWhenCalled() throws Throwable {
      systemUnderTest.answer( mock( InvocationOnMock.class ) );
      verify( runnable ).run();
   }//End Method
   
   @Test public void shouldBeUsableWithMocks(){
      Object mocked = mock( Object.class );
      doAnswer( systemUnderTest ).when( mocked ).toString();
      
      verify( runnable, never() ).run();
      mocked.toString();
      verify( runnable ).run();
   }//End Method

}//End Class
