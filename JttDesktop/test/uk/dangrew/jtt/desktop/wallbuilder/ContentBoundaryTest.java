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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javafx.beans.value.ChangeListener;

public class ContentBoundaryTest {

   private static final double INITIAL_POSITION = 34;
   
   @Mock private ContentArea area1;
   @Mock private ContentArea area2;
   private ContentBoundary systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new ContentBoundary( INITIAL_POSITION );
   }//End Method

   @Test public void shouldProvidePosition() {
      assertThat( systemUnderTest.positionPercentage(), is( INITIAL_POSITION ) );
   }//End Method
   
   @Test public void shouldProvidePositionRegistration(){
      systemUnderTest.registerForPositionChanges( area1 );
      systemUnderTest.changePosition( 10 );
      verify( area1 ).refreshDimensions();
      
      assertThat( systemUnderTest.boundedAreas(), is( Arrays.asList( area1 ) ) );
      
      systemUnderTest.unregisterForPositionChanges( area1 );
      systemUnderTest.changePosition( 10 );
      verifyNoMoreInteractions( area1 );
      
      assertThat( systemUnderTest.boundedAreas(), is( Arrays.asList() ) );
   }//End Method
   
   @Test public void shouldProvidePositionRegistrationForMultiple(){
      systemUnderTest.registerForPositionChanges( area1 );
      systemUnderTest.registerForPositionChanges( area2 );
      systemUnderTest.changePosition( 10 );
      verify( area1 ).refreshDimensions();
      verify( area2 ).refreshDimensions();
      
      assertThat( systemUnderTest.boundedAreas(), is( Arrays.asList( area1, area2 ) ) );
      
      systemUnderTest.unregisterForPositionChanges( area2 );
      systemUnderTest.changePosition( 10 );
      verifyNoMoreInteractions( area2 );
      verify( area1, times( 2 ) ).refreshDimensions();
      
      assertThat( systemUnderTest.boundedAreas(), is( Arrays.asList( area1 ) ) );
   }//End Method
   
   @Test public void shouldNotChangeWhenFixed(){
      assertThat( systemUnderTest.isFixed(), is( false ) );
      systemUnderTest.setFixed( true );
      assertThat( systemUnderTest.isFixed(), is( true ) );
      
      systemUnderTest.changePosition( 10 );
      assertThat( systemUnderTest.positionPercentage(), is( INITIAL_POSITION ) );
   }//End Method
   
   @Test public void shouldProvideFixedRegistration(){
      ChangeListener< Boolean > listener = mock( ChangeListener.class );
      systemUnderTest.registerForFixedChanges( listener );
      systemUnderTest.setFixed( true );
      verify( listener ).changed( Mockito.any(), eq( false ), eq( true ) );
      
      systemUnderTest.unregisterForFixedChanges( listener );
      systemUnderTest.setFixed( false );
      verifyNoMoreInteractions( listener );
   }//End Method
   
}//End Class
