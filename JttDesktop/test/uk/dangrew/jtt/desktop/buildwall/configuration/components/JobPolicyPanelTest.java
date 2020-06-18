/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfigurationImpl;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallJobPolicy;
import uk.dangrew.kode.javafx.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.desktop.graphics.PlatformDecoupler;
import uk.dangrew.jtt.desktop.graphics.TestPlatformDecouplerImpl;
import uk.dangrew.jtt.desktop.javafx.combobox.SimplePropertyBox;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.kode.TestCommon;
import uk.dangrew.jtt.model.utility.comparator.Comparators;
import uk.dangrew.kode.launch.TestApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * {@link JobPolicyPanel} test.
 */
@RunWith( JUnitParamsRunner.class )
public class JobPolicyPanelTest {
   
   @Spy private JavaFxStyle styling;
   @Spy private ConfigurationPanelDefaults defaults;
   private BuildWallConfiguration configuration; 
   private JobPolicyPanel systemUnderTest;
   
   @BeforeClass public static void initialisePlatform(){
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      
      configuration = new BuildWallConfigurationImpl();
      for ( int i = 0; i < 10; i++ ) {
         configuration.jobPolicies().put( new JenkinsJobImpl( "job " + i ), BuildWallJobPolicy.values()[ i % 3 ] );
      }
      TestApplication.startPlatform();
      systemUnderTest = new JobPolicyPanel( configuration, styling, defaults );
   }//End Method
   
   @Ignore //For manual inspection.
   @Test public void manualInspection() throws InterruptedException {
      TestApplication.launch( () -> { return new JobPolicyPanel( configuration ); } );
      Thread.sleep( 100000 );
   }//End Method
   
   @Test public void shouldDisplayRowForEachJobAndContainElements(){
      for ( JenkinsJob job : configuration.jobPolicies().keySet() ) {
         assertThat( systemUnderTest.jobLabel( job ), notNullValue() );
         assertThat( systemUnderTest.jobBox( job ), notNullValue() );
         assertThat( systemUnderTest.jobProperty( job ), notNullValue() );
         
         assertThat( systemUnderTest.getChildren().contains( systemUnderTest.jobLabel( job ) ), is( true ) );
         assertThat( systemUnderTest.getChildren().contains( systemUnderTest.jobBox( job ) ), is( true ) );
      }
   }//End Method
   
   @Test public void shouldDisplayRowForEachJobAlphabetically(){
      List< JenkinsJob > jobs = new ArrayList<>( configuration.jobPolicies().keySet() );
      jobs.sort( Comparators.stringExtractionComparater( job -> { return job.nameProperty().get(); } ) );
      
      for ( Node node : systemUnderTest.getChildren() ) {
         if ( node instanceof Label ) {
            JenkinsJob job = jobs.remove( 0 );
            Label label = ( Label )node;
            assertThat( label.getText(), is( job.nameProperty().get() ) );
         }         
      }
   }//End Method
   
   @Test public void shouldUpdateRowsForNewJobStillShowingAlphabetically(){
      shouldDisplayRowForEachJobAlphabetically();
      configuration.jobPolicies().put( new JenkinsJobImpl( "another job" ), BuildWallJobPolicy.AlwaysShow );
      shouldDisplayRowForEachJobAlphabetically();
   }//End Method
   
   @Test public void shouldApplyColumnConstraintsToAllColumns(){
      for ( JenkinsJob job : configuration.jobPolicies().keySet() ) {
         assertThat( systemUnderTest.jobBox( job ).getMaxWidth(), is( Double.MAX_VALUE ) );
      }
   }//End Method
   
   @Test public void shouldClearAllItemsAndConstraintsWhenReconstructed(){
      Map< JenkinsJob, Label > originalLabels = new HashMap<>();
      Map< JenkinsJob, SimplePropertyBox< BuildWallJobPolicy > > originalBoxes = new HashMap<>();
      Map< JenkinsJob, ObjectProperty< BuildWallJobPolicy > > originalProperties = new HashMap<>();
      
      for ( JenkinsJob job : configuration.jobPolicies().keySet() ) {
         originalLabels.put( job, systemUnderTest.jobLabel( job ) );
         originalBoxes.put( job, systemUnderTest.jobBox( job ) );
         originalProperties.put( job, systemUnderTest.jobProperty( job ) );
      }
      
      systemUnderTest.constructLayout();
      
      Map< JenkinsJob, Label > constructedLabels = new HashMap<>();
      Map< JenkinsJob, SimplePropertyBox< BuildWallJobPolicy > > constructedBoxes = new HashMap<>();
      Map< JenkinsJob, ObjectProperty< BuildWallJobPolicy > > constructedProperties = new HashMap<>();
      
      for ( JenkinsJob job : configuration.jobPolicies().keySet() ) {
         constructedLabels.put( job, systemUnderTest.jobLabel( job ) );
         constructedBoxes.put( job, systemUnderTest.jobBox( job ) );
         constructedProperties.put( job, systemUnderTest.jobProperty( job ) );
      }
      
      for ( JenkinsJob job : configuration.jobPolicies().keySet() ) {
         assertThat( originalLabels.get( job ), not( constructedLabels.get( job ) ) );
         assertThat( originalBoxes.get( job ), not( constructedBoxes.get( job ) ) );
         assertThat( originalProperties.get( job ), not( constructedProperties.get( job ) ) );
      }
   }//End Method
   
   @Test public void shouldUseInsets(){
      assertThat( systemUnderTest.getPadding().getBottom(), is( JobPolicyPanel.INSETS ) );
      assertThat( systemUnderTest.getPadding().getTop(), is( JobPolicyPanel.INSETS ) );
      assertThat( systemUnderTest.getPadding().getRight(), is( JobPolicyPanel.INSETS ) );
      assertThat( systemUnderTest.getPadding().getLeft(), is( JobPolicyPanel.INSETS ) );
   }//End Method
   
   /** 
    * Method to provide the test cases for the {@link BuildWallJobPolicy}.
    */
   static final Object[] buildWallJobPolicyCases(){
      return new Object[] {
               new Object[]{ BuildWallJobPolicy.AlwaysShow },
               new Object[]{ BuildWallJobPolicy.NeverShow },
               new Object[]{ BuildWallJobPolicy.OnlyShowFailures }
      };
   }//End Method
   
   @Parameters( method = "buildWallJobPolicyCases" )
   @Test public void shouldUpdateBoxWhenConfigurationChanges( BuildWallJobPolicy policy ){
      for ( JenkinsJob job : configuration.jobPolicies().keySet() ) {
         assertThat( 
                  systemUnderTest.jobBox( job ).getSelectionModel().getSelectedItem(), 
                  is( configuration.jobPolicies().get( job ) )
         );
         configuration.jobPolicies().put( job, policy );
         assertThat( 
                  systemUnderTest.jobBox( job ).getSelectionModel().getSelectedItem(), 
                  is( policy )
         );
      }
   }//End Method
   
   @Parameters( method = "buildWallJobPolicyCases" )
   @Test public void shouldUpdateConfigurationWhenBoxChanges( BuildWallJobPolicy policy ){
      for ( JenkinsJob job : configuration.jobPolicies().keySet() ) {
         assertThat( 
                  configuration.jobPolicies().get( job ), 
                  is( systemUnderTest.jobBox( job ).getSelectionModel().getSelectedItem() )
         );
         systemUnderTest.jobBox( job ).getSelectionModel().select( policy );
         assertThat( 
                  configuration.jobPolicies().get( job ), 
                  is( policy )
         );
      }
   }//End Method
   
   @Test public void shouldProvideSetAllPropertyBox(){
      assertThat( systemUnderTest.setAllPropertyBox(), is( notNullValue() ) );
      assertThat( systemUnderTest.setAllPropertyBox().getItems(), contains( BuildWallJobPolicy.values() ) );
      assertThat( systemUnderTest.setAllPropertyBox().getMaxWidth(), is( Double.MAX_VALUE ) );
   }//End Method
   
   @Test public void shouldDefaultSetAll(){
      assertThat( systemUnderTest.setAllPropertyBox().getSelectionModel().getSelectedItem(), is( BuildWallJobPolicy.NeverShow ) );
   }//End Method
   
   @Test public void shouldProvideSetAllButton(){
      assertThat( systemUnderTest.setAllButton(), is( notNullValue() ) );
      assertThat( systemUnderTest.setAllButton().getMaxWidth(), is( Double.MAX_VALUE ) );
      TestCommon.assertThatFontIsBold( systemUnderTest.setAllButton().getFont() );
   }//End Method
   
   @Test public void setAllShouldChangePropertyBoxes(){
      configuration.jobPolicies().entrySet().forEach( entry -> entry.setValue( BuildWallJobPolicy.AlwaysShow ) );
      
      systemUnderTest.setAllPropertyBox().getSelectionModel().select( BuildWallJobPolicy.OnlyShowFailures );
      systemUnderTest.setAllButton().getOnAction().handle( new ActionEvent() );
      
      configuration.jobPolicies().entrySet().forEach( entry -> {
         assertThat( 
                  systemUnderTest.jobBox( entry.getKey() ).getSelectionModel().getSelectedItem(), 
                  is( BuildWallJobPolicy.OnlyShowFailures ) 
         );
         assertThat( entry.getValue(), is( BuildWallJobPolicy.OnlyShowFailures ) );
      } );
   }//End Method
   
   @Test public void constructionShouldBePerformedOnJavaFxThread(){
      PlatformDecoupler decoupler = spy( new TestPlatformDecouplerImpl() );
      DecoupledPlatformImpl.setInstance( decoupler );
      
      JenkinsJob first = new ArrayList<>( configuration.jobPolicies().keySet() ).iterator().next();
      Label label = systemUnderTest.jobLabel( first );
      
      systemUnderTest.constructLayout();
      
      verify( decoupler ).run( Mockito.any() );
      assertThat( systemUnderTest.jobLabel( first ), is( not( label ) ) );
   }//End Method
   
   @Test public void shouldApplyStylingToPanel(){
      verify( defaults ).configureColumnConstraints( systemUnderTest );
   }//End Method
   
   @Test public void shouldDetermineWhetherItHasGivenConfiguration(){
      assertThat( systemUnderTest.hasConfiguration( configuration ), is( true ) );
      assertThat( systemUnderTest.hasConfiguration( new BuildWallConfigurationImpl() ), is( false ) );
   }//End Method
}//End Class
