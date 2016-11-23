/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.theme;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.utility.synchronisation.SynchronizedObservableMap;

public class BuildWallThemeImplTest {

   private static final String NAME = "Some name";
   private BuildWallThemeImpl systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      systemUnderTest = new BuildWallThemeImpl( NAME );
   }//End Method
   
   @Test ( expected = IllegalArgumentException.class ) public void shouldNotAllowNullName(){
      new BuildWallThemeImpl( null );
   }//End Method
   
   @Test ( expected = IllegalArgumentException.class ) public void shouldNotAllowInvalidName(){
      new BuildWallThemeImpl( "    " );
   }//End Method
   
   @Test public void shouldProvideNameProperty(){
      assertThat( systemUnderTest.nameProperty(), is( notNullValue() ) );
      assertThat( systemUnderTest.nameProperty(), is( systemUnderTest.nameProperty() ) );
   }//End Method

   @Test public void shouldProvideSynchronisedMapForBarColours() {
      assertThat( systemUnderTest.barColoursMap(), is( instanceOf( SynchronizedObservableMap.class ) ) );
      assertThat( systemUnderTest.barColoursMap(), is( systemUnderTest.barColoursMap() ) );
   }//End Method
   
   @Test public void shouldProvideSynchronisedMapForTrackColours() {
      assertThat( systemUnderTest.trackColoursMap(), is( instanceOf( SynchronizedObservableMap.class ) ) );
      assertThat( systemUnderTest.trackColoursMap(), is( systemUnderTest.trackColoursMap() ) );
   }//End Method
   
   @Test public void shouldProvideSynchronisedMapForJobNameColours() {
      assertThat( systemUnderTest.jobNameTextColoursMap(), is( instanceOf( SynchronizedObservableMap.class ) ) );
      assertThat( systemUnderTest.jobNameTextColoursMap(), is( systemUnderTest.trackColoursMap() ) );
   }//End Method
   
   @Test public void shouldProvideSynchronisedMapForBuildNumberColours() {
      assertThat( systemUnderTest.buildNumberTextColoursMap(), is( instanceOf( SynchronizedObservableMap.class ) ) );
      assertThat( systemUnderTest.buildNumberTextColoursMap(), is( systemUnderTest.trackColoursMap() ) );
   }//End Method
   
   @Test public void shouldProvideSynchronisedMapForCompletionEstimateColours() {
      assertThat( systemUnderTest.completionEstimateTextColoursMap(), is( instanceOf( SynchronizedObservableMap.class ) ) );
      assertThat( systemUnderTest.completionEstimateTextColoursMap(), is( systemUnderTest.trackColoursMap() ) );
   }//End Method
   
   @Test public void shouldProvideSynchronisedMapForDetailColours() {
      assertThat( systemUnderTest.detailTextColoursMap(), is( instanceOf( SynchronizedObservableMap.class ) ) );
      assertThat( systemUnderTest.detailTextColoursMap(), is( systemUnderTest.trackColoursMap() ) );
   }//End Method

}//End Class
