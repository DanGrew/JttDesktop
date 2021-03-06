/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.dangrew.jtt.desktop.buildwall.panel.type.JobPanelDescriptionProviders;
import uk.dangrew.kode.synchronization.SynchronizedObservableMap;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

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
   
   @Test public void shouldProvideSynchronizedMapForPolicies(){
      assertThat( systemUnderTest.jobPolicies(), is( instanceOf( SynchronizedObservableMap.class ) ) );
   }//End Method
   
}//End Class
