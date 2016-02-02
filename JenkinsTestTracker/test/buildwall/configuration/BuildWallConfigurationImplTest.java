/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.configuration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import buildwall.configuration.BuildWallConfiguration;
import buildwall.configuration.BuildWallConfigurationImpl;

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
      
      Assert.assertEquals( BuildWallConfigurationImpl.DEFAULT_PROPERTIES_FONT, systemUnderTest.buildNumberFont().get() );
      Assert.assertEquals( BuildWallConfigurationImpl.DEFAULT_TEXT_COLOUR, systemUnderTest.buildNumberColour().get() );
      
      Assert.assertEquals( BuildWallConfigurationImpl.DEFAULT_PROPERTIES_FONT, systemUnderTest.completionEstimateFont().get() );
      Assert.assertEquals( BuildWallConfigurationImpl.DEFAULT_TEXT_COLOUR, systemUnderTest.completionEstimateColour().get() );
      
      Assert.assertEquals( BuildWallConfigurationImpl.DEFAULT_NUMBER_OF_COLUMNS, systemUnderTest.numberOfColumns().get() );
   }//End Method
   
}//End Class
