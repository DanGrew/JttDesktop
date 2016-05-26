/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.buildwall.configuration.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.BuildWallConfigurationImpl;
import uk.dangrew.jtt.buildwall.panel.type.JobPanelDescriptionProviders;

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
      
      assertThat( systemUnderTest.jobPolicies().isEmpty(), is( true ) );
      
      assertThat( systemUnderTest.jobPanelDescriptionProvider().get(), is( JobPanelDescriptionProviders.Default ) );
      
      Assert.assertEquals( BuildWallConfigurationImpl.DEFAULT_PROPERTIES_FONT, systemUnderTest.detailFont().get() );
      Assert.assertEquals( BuildWallConfigurationImpl.DEFAULT_TEXT_COLOUR, systemUnderTest.detailColour().get() );
   }//End Method
   
}//End Class
