/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.persistence.dualwall;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import javafx.geometry.Orientation;
import uk.dangrew.jtt.buildwall.configuration.properties.DualWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.properties.DualWallConfigurationImpl;

/**
 * {@link DualWallConfigurationModel} test.
 */
public class DualWallConfigurationModelTest {

   private static final String ANYTHING = "anything";
   
   private DualWallConfiguration configuration;
   private DualWallConfigurationModel systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      configuration = new DualWallConfigurationImpl();
      systemUnderTest = new DualWallConfigurationModel( configuration );
   }//End Method
   
   @Test( expected = NullPointerException.class ) public void shouldNotAcceptNullConfiguration(){
      new DualWallConfigurationModel( null );
   }//End Method
   
   @Test public void shouldSetDividerPositionInConfiguration() {
      configuration.dividerPositionProperty().set( 0.9 );
      
      systemUnderTest.setDividerPosition( ANYTHING, 0.2 );
      assertThat( configuration.dividerPositionProperty().get(), is( 0.2 ) );
      
      systemUnderTest.setDividerPosition( ANYTHING, 1.0 );
      assertThat( configuration.dividerPositionProperty().get(), is( 1.0 ) );
   }//End Method
   
   @Test public void shouldGetDividerPositionFromConfiguration() {
      configuration.dividerPositionProperty().set( 0.1 );
      assertThat( systemUnderTest.getDividerPosition( ANYTHING ), is( 0.1 ) );
      
      configuration.dividerPositionProperty().set( 0.6 );
      assertThat( systemUnderTest.getDividerPosition( ANYTHING ), is( 0.6 ) );
   }//End Method
   
   @Test public void shouldIgnoreNullDividerPositionValue(){
      configuration.dividerPositionProperty().set( 0.1 );
      systemUnderTest.setDividerPosition( ANYTHING, null );
      assertThat( configuration.dividerPositionProperty().get(), is( 0.1 ) );
   }//End Method
   
   @Test public void shouldAcceptBoundaryDividerPositionValue(){
      configuration.dividerPositionProperty().set( 0.1 );
      
      systemUnderTest.setDividerPosition( ANYTHING, 0.0 );
      assertThat( configuration.dividerPositionProperty().get(), is( 0.0 ) );
      
      systemUnderTest.setDividerPosition( ANYTHING, 1.0 );
      assertThat( configuration.dividerPositionProperty().get(), is( 1.0 ) );
   }//End Method
   
   @Test public void shouldIgnoreInvalidPositionOutsideOfRange(){
      configuration.dividerPositionProperty().set( 0.1 );
      
      systemUnderTest.setDividerPosition( ANYTHING, -1.0 );
      systemUnderTest.setDividerPosition( ANYTHING, 2.0 );
      systemUnderTest.setDividerPosition( ANYTHING, 100.0 );
      
      assertThat( configuration.dividerPositionProperty().get(), is( 0.1 ) );
   }//End Method
   
   @Test public void shouldSetDividerOrientationInConfiguration() {
      configuration.dividerOrientationProperty().set( Orientation.HORIZONTAL );
      
      systemUnderTest.setDividerOrientation( ANYTHING, Orientation.VERTICAL );
      assertThat( configuration.dividerOrientationProperty().get(), is( Orientation.VERTICAL ) );
      
      systemUnderTest.setDividerOrientation( ANYTHING, Orientation.HORIZONTAL );
      assertThat( configuration.dividerOrientationProperty().get(), is( Orientation.HORIZONTAL ) );
   }//End Method
   
   @Test public void shouldGetDividerOrientationFromConfiguration() {
      configuration.dividerOrientationProperty().set( Orientation.HORIZONTAL );
      assertThat( systemUnderTest.getDividerOrientation( ANYTHING ), is( Orientation.HORIZONTAL ) );
      
      configuration.dividerOrientationProperty().set( Orientation.VERTICAL );
      assertThat( systemUnderTest.getDividerOrientation( ANYTHING ), is( Orientation.VERTICAL ) );
   }//End Method
   
   @Test public void shouldIgnoreNullDividerOrientationValue(){
      configuration.dividerOrientationProperty().set( Orientation.HORIZONTAL );
      systemUnderTest.setDividerPosition( ANYTHING, null );
      assertThat( configuration.dividerOrientationProperty().get(), is( Orientation.HORIZONTAL ) );
   }//End Method
   
}//End Class
