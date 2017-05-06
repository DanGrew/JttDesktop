/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.graphics;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * {@link TestPlatformDecouplerImpl} test.
 */
public class TestPlatformDecouplerImplTest {
   
   private TestPlatformDecouplerImpl systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new TestPlatformDecouplerImpl();
   }//End Method

   @Test public void shouldRunRunnableInDueCourse() throws InterruptedException {
      BooleanProperty result = new SimpleBooleanProperty( false );
      systemUnderTest.run( () -> {
         result.set( true );
      } );
      Assert.assertTrue( result.get() );
   }//End Method
   
   @Test public void shouldRecordUsingGivenRunnable(){
      BooleanProperty result = new SimpleBooleanProperty( false );
      systemUnderTest = new TestPlatformDecouplerImpl( () -> result.set( true ) );
      systemUnderTest.run( () -> {} );
      assertThat( result.get(), is( true ) );
   }//End Method

}//End Class
