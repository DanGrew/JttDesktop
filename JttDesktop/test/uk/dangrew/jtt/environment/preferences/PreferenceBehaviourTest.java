/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.environment.preferences;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.configuration.tree.ConfigurationTreeItems;

/**
 * {@link PreferenceBehaviour} test.
 */
public class PreferenceBehaviourTest {

   private PreferenceBehaviour systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new PreferenceBehaviour( WindowPolicy.Open, ConfigurationTreeItems.LeftColours );
   }//End Method
   
   @Test public void shouldProvideWindowPolicy() {
      assertThat( systemUnderTest.getWindowPolicy(), is( WindowPolicy.Open ) );
   }//End Method
   
   @Test public void shouldProvideSelection() {
      assertThat( systemUnderTest.getSelection(), is( ConfigurationTreeItems.LeftColours ) );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullPolicy(){
      new PreferenceBehaviour( null, ConfigurationTreeItems.DualWallProperties );
   }//End Method
   
   @Test public void shouldOverrideEquals(){
      assertThat( 
               new PreferenceBehaviour( WindowPolicy.Close, ConfigurationTreeItems.DualWallRoot ), 
               is( new PreferenceBehaviour( WindowPolicy.Close, ConfigurationTreeItems.DualWallRoot ) )
      );
   }//End Method
   
   @Test public void shouldOverrideHashcode(){
      assertThat( 
               new PreferenceBehaviour( WindowPolicy.Close, ConfigurationTreeItems.DualWallRoot ).hashCode(), 
               is( new PreferenceBehaviour( WindowPolicy.Close, ConfigurationTreeItems.DualWallRoot ).hashCode() )
      );
   }//End Method

}//End Class
