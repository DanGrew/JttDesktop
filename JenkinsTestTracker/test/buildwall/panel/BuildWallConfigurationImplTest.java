/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.panel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link BuildWallConfigurationImpl} test.
 */
public class BuildWallConfigurationImplTest {

   private BuildWallConfiguration systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new BuildWallConfigurationImpl();
   }//End Method
   
   @Test public void shouldConstructWithDefaultValues() {
      Assert.assertEquals( BuildWallConfigurationImpl.DEFAULT_JOB_NAME_FONT, systemUnderTest.jobNameFont().get() );
      Assert.assertEquals( BuildWallConfigurationImpl.DEFAULT_TEXT_COLOUR, systemUnderTest.jobNameColour().get() );
      
      Assert.assertEquals( BuildWallConfigurationImpl.DEFAULT_PROPERTIES_FONT, systemUnderTest.propertiesFont().get() );
      Assert.assertEquals( BuildWallConfigurationImpl.DEFAULT_TEXT_COLOUR, systemUnderTest.propertiesColour().get() );
   }//End Method
   
}//End Class
