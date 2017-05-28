/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.configuration.api;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import uk.dangrew.sd.graphics.launch.TestApplication;
import uk.dangrew.sd.table.model.DigestTable;
import uk.dangrew.sd.table.presentation.DigestTableRowLimit;

public class ApiConfigurationPanelTest {

   private JenkinsConnectionTable table;
   private JenkinsConnectionControls controls;
   @Spy private DigestTable digest;
   @Mock private ApiConfigurationController controller;
   private ApiConfigurationPanel systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      
      table = new JenkinsConnectionTable();
      controls = new JenkinsConnectionControls( controller );
      
      systemUnderTest = new ApiConfigurationPanel( 
               controller, 
               table, 
               controls, 
               digest 
      );
   }//End Method
   
   @Ignore
   @Test public void manual() throws InterruptedException {
      TestApplication.launch( () -> new ApiConfigurationPanel() );
      
      Thread.sleep( 1000000 );
   }//End Method
   
   @Test public void shouldProvideComponentsToController() {
      verify( controller ).control( table, controls );
   }//End Method
   
   @Test public void shouldPutTableInTop() {
      assertThat( systemUnderTest.getTop(), is( table ) );
      assertThat( table.getPrefWidth(), is( ApiConfigurationPanel.PREF_WIDTH ) );
      assertThat( table.getPrefHeight(), is( ApiConfigurationPanel.PREF_HEIGHT ) );
   }//End Method
   
   @Test public void shouldPutControlsInCenter() {
      assertThat( systemUnderTest.getCenter(), is( controls ) );
   }//End Method
   
   @Test public void shouldPutDigestInBottom() {
      assertThat( systemUnderTest.getBottom(), is( digest ) );
      assertThat( digest.getPrefWidth(), is( ApiConfigurationPanel.PREF_WIDTH ) );
      assertThat( digest.getPrefHeight(), is( ApiConfigurationPanel.PREF_HEIGHT ) );
      verify( digest ).setRowLimit( DigestTableRowLimit.OneHundred );
   }//End Method
   
   @Test public void shouldUseInsets() {
      assertThat( systemUnderTest.getPadding().getBottom(), is( ApiConfigurationPanel.INSETS ) );
      assertThat( systemUnderTest.getPadding().getLeft(), is( ApiConfigurationPanel.INSETS ) );
      assertThat( systemUnderTest.getPadding().getRight(), is( ApiConfigurationPanel.INSETS ) );
      assertThat( systemUnderTest.getPadding().getTop(), is( ApiConfigurationPanel.INSETS ) );
   }//End Method

}//End Class
