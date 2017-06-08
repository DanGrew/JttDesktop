/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.wallbuilder;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javafx.beans.value.ChangeListener;

public class ContentBoundaryTest {

   private static final double INITIAL_POSITION = 34;
   
   private ContentBoundary systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      systemUnderTest = new ContentBoundary( INITIAL_POSITION );
   }//End Method

   @Test public void shouldProvidePosition() {
      assertThat( systemUnderTest.positionPercentage(), is( INITIAL_POSITION ) );
   }//End Method
   
   @Test public void shouldProvideRegistration(){
      ChangeListener< Number > listener = mock( ChangeListener.class );
      systemUnderTest.registerForPositionChanges( listener );
      systemUnderTest.changePosition( 10 );
      verify( listener ).changed( Mockito.any(), eq( 34.0 ), eq( 44.0 ) );
      
      systemUnderTest.unregisterForPositionChanges( listener );
      systemUnderTest.changePosition( 10 );
      verifyNoMoreInteractions( listener );
   }//End Method

}//End Class
