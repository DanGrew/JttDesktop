/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.persistence;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static uk.dangrew.jtt.utility.TestCommon.precision;
import static uk.dangrew.jtt.utility.TestableFonts.commonFont;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfigurationImpl;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallJobPolicy;
import uk.dangrew.jtt.buildwall.panel.type.JobPanelDescriptionProviders;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;
import uk.dangrew.jtt.utility.TestCommon;
import uk.dangrew.jtt.utility.conversion.ColorConverter;

/**
 * {@link BuildWallConfigurationModel} test.
 */
public class BuildWallConfigurationModelTest {

   private static final String ANYTHING = "anything";
   private static final String RED_HEX = new ColorConverter().colorToHex( Color.RED );
   
   private JenkinsJob firstJenkinsJob;
   private JenkinsJob secondJenkinsJob;
   private JenkinsJob thirdJenkinsJob;
   
   private BuildWallConfiguration configuration;
   private JenkinsDatabase database;
   private BuildWallConfigurationModel systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      firstJenkinsJob = new JenkinsJobImpl( "firstJob" );
      secondJenkinsJob = new JenkinsJobImpl( "secondJob" );
      thirdJenkinsJob = new JenkinsJobImpl( "thirdJob" );
      
      configuration = new BuildWallConfigurationImpl();
      database = new JenkinsDatabaseImpl();
      systemUnderTest = new BuildWallConfigurationModel( configuration, database );
   }//End Method
   
   @Test( expected = NullPointerException.class ) public void shouldNotAcceptNullDatabase(){
      new BuildWallConfigurationModel( configuration, null );
   }//End Method
   
   @Test( expected = NullPointerException.class ) public void shouldNotAcceptNullConfiguration(){
      new BuildWallConfigurationModel( null, database );
   }//End Method
   
   @Test public void shouldSetNumberOfColumnsInConfiguration() {
      configuration.numberOfColumns().set( 10 );
      
      systemUnderTest.setColumns( ANYTHING, 4 );
      assertThat( configuration.numberOfColumns().get(), is( 4 ) );
      
      systemUnderTest.setColumns( ANYTHING, 1 );
      assertThat( configuration.numberOfColumns().get(), is( 1 ) );
   }//End Method
   
   @Test public void shouldNumberOfColumnsFromConfiguration() {
      configuration.numberOfColumns().set( 10 );
      assertThat( systemUnderTest.getColumns( ANYTHING ), is( 10 ) );
      
      configuration.numberOfColumns().set( 5 );
      assertThat( systemUnderTest.getColumns( ANYTHING ), is( 5 ) );
   }//End Method
   
   @Test public void shouldSetDescriptionTypeInConfiguration() {
      configuration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Detailed );
      
      systemUnderTest.setDescriptionType( ANYTHING, JobPanelDescriptionProviders.Default );
      assertThat( configuration.jobPanelDescriptionProvider().get(), is( JobPanelDescriptionProviders.Default ) );
      
      systemUnderTest.setDescriptionType( ANYTHING, JobPanelDescriptionProviders.Simple );
      assertThat( configuration.jobPanelDescriptionProvider().get(), is( JobPanelDescriptionProviders.Simple ) );
   }//End Method
   
   @Test public void shouldSetDescriptionTypeFromDescription() {
      configuration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Simple );
      assertThat( systemUnderTest.getDescriptionType( ANYTHING ), is( JobPanelDescriptionProviders.Simple ) );
      
      configuration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Detailed );
      assertThat( systemUnderTest.getDescriptionType( ANYTHING ), is( JobPanelDescriptionProviders.Detailed ) );
   }//End Method

   @Test public void shouldGetJobNameFromFirstInConfiguredPolicies(){
      configuration.jobPolicies().put( firstJenkinsJob, BuildWallJobPolicy.NeverShow );
      
      systemUnderTest.startWritingJobs( ANYTHING );
      assertThat( systemUnderTest.getJobName( ANYTHING ), is( firstJenkinsJob.nameProperty().get() ) );
   }//End Method
   
   @Test public void shouldIgnoreJobNameWhenNoPoliciesConfiguredBecauseHandleWillNotPutNull(){
      systemUnderTest.startWritingJobs( ANYTHING );
      assertThat( systemUnderTest.getJobName( ANYTHING ), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldGetAllJobNamesFromAllConfiguredPolicies(){
      configuration.jobPolicies().put( firstJenkinsJob, BuildWallJobPolicy.OnlyShowFailures );
      configuration.jobPolicies().put( secondJenkinsJob, BuildWallJobPolicy.OnlyShowPassing );
      configuration.jobPolicies().put( thirdJenkinsJob, BuildWallJobPolicy.AlwaysShow );
      
      systemUnderTest.startWritingJobs( ANYTHING );
      
      List< String > results = new ArrayList<>();
      results.add( systemUnderTest.getJobName( ANYTHING ) );
      results.add( systemUnderTest.getJobName( ANYTHING ) );
      results.add( systemUnderTest.getJobName( ANYTHING ) );
      assertThat( systemUnderTest.getJobName( ANYTHING ), is( nullValue() ) );
      
      assertThat( results, containsInAnyOrder( 
               firstJenkinsJob.nameProperty().get(), 
               secondJenkinsJob.nameProperty().get(), 
               thirdJenkinsJob.nameProperty().get() 
      ) );
   }//End Method
   
   @Test public void shouldGetJobPolicyFromFirstInConfiguredPolicies(){
      configuration.jobPolicies().put( firstJenkinsJob, BuildWallJobPolicy.NeverShow );
      
      systemUnderTest.startWritingJobs( ANYTHING );
      assertThat( systemUnderTest.getJobPolicy( ANYTHING ), is( BuildWallJobPolicy.NeverShow ) );
   }//End Method
   
   @Test public void shouldIgnoreJobPolicyWhenNoPoliciesConfiguredBecauseHandleWillNotPutNull(){
      systemUnderTest.startWritingJobs( ANYTHING );
      assertThat( systemUnderTest.getJobPolicy( ANYTHING ), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldGetAllJobPoliciesFromAllConfiguredPolicies(){
      configuration.jobPolicies().put( firstJenkinsJob, BuildWallJobPolicy.OnlyShowFailures );
      configuration.jobPolicies().put( secondJenkinsJob, BuildWallJobPolicy.OnlyShowPassing );
      configuration.jobPolicies().put( thirdJenkinsJob, BuildWallJobPolicy.AlwaysShow );
      
      systemUnderTest.startWritingJobs( ANYTHING );
      
      List< BuildWallJobPolicy > results = new ArrayList<>();
      results.add( systemUnderTest.getJobPolicy( ANYTHING ) );
      results.add( systemUnderTest.getJobPolicy( ANYTHING ) );
      results.add( systemUnderTest.getJobPolicy( ANYTHING ) );
      assertThat( systemUnderTest.getJobPolicy( ANYTHING ), is( nullValue() ) );
      
      assertThat( results, containsInAnyOrder( 
               BuildWallJobPolicy.OnlyShowFailures, 
               BuildWallJobPolicy.OnlyShowPassing, 
               BuildWallJobPolicy.AlwaysShow 
      ) );
   }//End Method
   
   @Test public void shouldManageJobNamesAndPoliciesIndependently(){
      configuration.jobPolicies().put( firstJenkinsJob, BuildWallJobPolicy.OnlyShowFailures );
      configuration.jobPolicies().put( secondJenkinsJob, BuildWallJobPolicy.OnlyShowPassing );
      configuration.jobPolicies().put( thirdJenkinsJob, BuildWallJobPolicy.AlwaysShow );
      
      systemUnderTest.startWritingJobs( ANYTHING );
      
      List< String > jobNames = new ArrayList<>();
      jobNames.add( systemUnderTest.getJobName( ANYTHING ) );
      jobNames.add( systemUnderTest.getJobName( ANYTHING ) );
      jobNames.add( systemUnderTest.getJobName( ANYTHING ) );
      
      List< BuildWallJobPolicy > policies = new ArrayList<>();
      policies.add( systemUnderTest.getJobPolicy( ANYTHING ) );
      policies.add( systemUnderTest.getJobPolicy( ANYTHING ) );
      policies.add( systemUnderTest.getJobPolicy( ANYTHING ) );
      
      for ( Entry< JenkinsJob, BuildWallJobPolicy > entry : configuration.jobPolicies().entrySet() ) {
         int index = jobNames.indexOf( entry.getKey().nameProperty().get() );
         assertThat( policies.get( index ), is( entry.getValue() ) );
      }
   }//End Method
   
   @Test public void shouldNotPutJobUntilPolicyIsParsed(){
      database.store( firstJenkinsJob );
      
      assertThat( configuration.jobPolicies().isEmpty(), is( true ) );
      systemUnderTest.setJobName( ANYTHING, firstJenkinsJob.nameProperty().get() );
      assertThat( configuration.jobPolicies().isEmpty(), is( true ) );
      systemUnderTest.setJobPolicy( ANYTHING, BuildWallJobPolicy.OnlyShowFailures );
      assertThat( configuration.jobPolicies(), hasEntry( firstJenkinsJob, BuildWallJobPolicy.OnlyShowFailures ) );
   }//End Method
   
   @Test public void shouldNotPutPolicyUntilJobIsParsed(){
      database.store( firstJenkinsJob );
      
      assertThat( configuration.jobPolicies().isEmpty(), is( true ) );
      systemUnderTest.setJobPolicy( ANYTHING, BuildWallJobPolicy.OnlyShowFailures );
      assertThat( configuration.jobPolicies().isEmpty(), is( true ) );
      systemUnderTest.setJobName( ANYTHING, firstJenkinsJob.nameProperty().get() );
      assertThat( configuration.jobPolicies(), hasEntry( firstJenkinsJob, BuildWallJobPolicy.OnlyShowFailures ) );
   }//End Method
   
   @Test public void shouldSupportParsingJobsFirstThenPolicies(){
      database.store( firstJenkinsJob );
      database.store( secondJenkinsJob );
      database.store( thirdJenkinsJob );
      
      assertThat( configuration.jobPolicies().isEmpty(), is( true ) );
      systemUnderTest.setJobName( ANYTHING, firstJenkinsJob.nameProperty().get() );
      systemUnderTest.setJobName( ANYTHING, secondJenkinsJob.nameProperty().get() );
      systemUnderTest.setJobName( ANYTHING, thirdJenkinsJob.nameProperty().get() );
      assertThat( configuration.jobPolicies().isEmpty(), is( true ) );
      
      systemUnderTest.setJobPolicy( ANYTHING, BuildWallJobPolicy.OnlyShowFailures );
      systemUnderTest.setJobPolicy( ANYTHING, BuildWallJobPolicy.AlwaysShow );
      systemUnderTest.setJobPolicy( ANYTHING, BuildWallJobPolicy.OnlyShowPassing );
      
      assertThat( configuration.jobPolicies(), hasEntry( firstJenkinsJob, BuildWallJobPolicy.OnlyShowFailures ) );
      assertThat( configuration.jobPolicies(), hasEntry( secondJenkinsJob, BuildWallJobPolicy.AlwaysShow ) );
      assertThat( configuration.jobPolicies(), hasEntry( thirdJenkinsJob, BuildWallJobPolicy.OnlyShowPassing ) );
   }//End Method
   
   @Test public void shouldSupportParsingPoliciesFirstThenJobs(){
      database.store( firstJenkinsJob );
      database.store( secondJenkinsJob );
      database.store( thirdJenkinsJob );
      
      assertThat( configuration.jobPolicies().isEmpty(), is( true ) );
      systemUnderTest.setJobPolicy( ANYTHING, BuildWallJobPolicy.OnlyShowFailures );
      systemUnderTest.setJobPolicy( ANYTHING, BuildWallJobPolicy.AlwaysShow );
      systemUnderTest.setJobPolicy( ANYTHING, BuildWallJobPolicy.OnlyShowPassing );
      assertThat( configuration.jobPolicies().isEmpty(), is( true ) );
      
      systemUnderTest.setJobName( ANYTHING, firstJenkinsJob.nameProperty().get() );
      systemUnderTest.setJobName( ANYTHING, secondJenkinsJob.nameProperty().get() );
      systemUnderTest.setJobName( ANYTHING, thirdJenkinsJob.nameProperty().get() );
      
      assertThat( configuration.jobPolicies(), hasEntry( firstJenkinsJob, BuildWallJobPolicy.OnlyShowFailures ) );
      assertThat( configuration.jobPolicies(), hasEntry( secondJenkinsJob, BuildWallJobPolicy.AlwaysShow ) );
      assertThat( configuration.jobPolicies(), hasEntry( thirdJenkinsJob, BuildWallJobPolicy.OnlyShowPassing ) );
   }//End Method
   
   @Test public void shouldSupportParsingPoliciesAndJobsInAnyOrder(){
      database.store( firstJenkinsJob );
      database.store( secondJenkinsJob );
      database.store( thirdJenkinsJob );
      
      assertThat( configuration.jobPolicies().isEmpty(), is( true ) );
      systemUnderTest.setJobPolicy( ANYTHING, BuildWallJobPolicy.OnlyShowFailures );
      systemUnderTest.setJobName( ANYTHING, firstJenkinsJob.nameProperty().get() );
      systemUnderTest.setJobName( ANYTHING, secondJenkinsJob.nameProperty().get() );
      systemUnderTest.setJobPolicy( ANYTHING, BuildWallJobPolicy.AlwaysShow );
      systemUnderTest.setJobPolicy( ANYTHING, BuildWallJobPolicy.OnlyShowPassing );
      systemUnderTest.setJobName( ANYTHING, thirdJenkinsJob.nameProperty().get() );
      
      assertThat( configuration.jobPolicies(), hasEntry( firstJenkinsJob, BuildWallJobPolicy.OnlyShowFailures ) );
      assertThat( configuration.jobPolicies(), hasEntry( secondJenkinsJob, BuildWallJobPolicy.AlwaysShow ) );
      assertThat( configuration.jobPolicies(), hasEntry( thirdJenkinsJob, BuildWallJobPolicy.OnlyShowPassing ) );
   }//End Method
   
   @Test public void shouldClearBuffersWhenParsingNewArrays(){
      configuration.jobPolicies().put( firstJenkinsJob, BuildWallJobPolicy.OnlyShowFailures );
      systemUnderTest.startWritingJobs( ANYTHING );
      systemUnderTest.startWritingJobs( ANYTHING );
      assertThat( systemUnderTest.getJobName( ANYTHING ), is( firstJenkinsJob.nameProperty().get() ) );
      assertThat( systemUnderTest.getJobName( ANYTHING ), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldClearBuffersWhenWritingNewArrays(){
      database.store( firstJenkinsJob );
      
      systemUnderTest.setJobPolicy( ANYTHING, BuildWallJobPolicy.NeverShow );
      systemUnderTest.startParsingJobs( ANYTHING );
      
      systemUnderTest.setJobPolicy( ANYTHING, BuildWallJobPolicy.AlwaysShow );
      systemUnderTest.setJobName( ANYTHING, firstJenkinsJob.nameProperty().get() );
      
      assertThat( configuration.jobPolicies(), hasEntry( firstJenkinsJob, BuildWallJobPolicy.AlwaysShow ) );
   }//End Method
   
   @Test public void shouldProvideNumberOfJobsEqualToNumberOfPolicies(){
      assertThat( systemUnderTest.getNumberOfJobs( ANYTHING ), is( 0 ) );
      configuration.jobPolicies().put( firstJenkinsJob, BuildWallJobPolicy.AlwaysShow );
      assertThat( systemUnderTest.getNumberOfJobs( ANYTHING ), is( 1 ) );
      configuration.jobPolicies().put( secondJenkinsJob, BuildWallJobPolicy.AlwaysShow );
      assertThat( systemUnderTest.getNumberOfJobs( ANYTHING ), is( 2 ) );
      configuration.jobPolicies().put( thirdJenkinsJob, BuildWallJobPolicy.AlwaysShow );
      assertThat( systemUnderTest.getNumberOfJobs( ANYTHING ), is( 3 ) );
   }//End Method
   
   @Test public void shouldStoreJobThatDoesNotExist(){
      systemUnderTest.startParsingJobs( ANYTHING );
      assertThat( database.jenkinsJobs(), is( empty() ) );
      systemUnderTest.setJobName( ANYTHING, firstJenkinsJob.nameProperty().get() );
      assertThat( database.jenkinsJobs(), hasSize( 1 ) );
      assertThat( database.jenkinsJobs().get( 0 ).nameProperty().get(), is( firstJenkinsJob.nameProperty().get() ) );
   }//End Method
   
   @Test public void shouldChangeJobNameFontFamilyWhenFamilySet(){
      assertThat( configuration.jobNameFont().get().getFamily(), is( not( commonFont() ) ) );
      systemUnderTest.setJobNameFontFamily( ANYTHING, commonFont() );
      assertThat( configuration.jobNameFont().get().getFamily(), is( commonFont() ) );
   }//End Method
   
   @Test public void shouldChangeJobNameFontSzeWhenSizeSet(){
      assertThat( configuration.jobNameFont().get().getSize(), is( not( closeTo( 25, precision() ) ) ) );
      systemUnderTest.setJobNameFontSize( ANYTHING, 25.0 );
      assertThat( configuration.jobNameFont().get().getSize(), is( closeTo( 25, precision() ) ) );
   }//End Method
   
   @Test public void shouldPreserveJobNameFontFamilyWhenSizeSet(){
      assertThat( configuration.jobNameFont().get().getFamily(), is( not( commonFont() ) ) );
      assertThat( configuration.jobNameFont().get().getSize(), is( not( closeTo( 25, precision() ) ) ) );
      
      systemUnderTest.setJobNameFontFamily( ANYTHING, commonFont() );
      systemUnderTest.setJobNameFontSize( ANYTHING, 25.0 );
      
      assertThat( configuration.jobNameFont().get().getFamily(), is( commonFont() ) );
      assertThat( configuration.jobNameFont().get().getSize(), is( closeTo( 25, precision() ) ) );
   }//End Method
   
   @Test public void shouldPreserverJobNameFontSizeWhenFamilySet(){
      assertThat( configuration.jobNameFont().get().getFamily(), is( not( commonFont() ) ) );
      assertThat( configuration.jobNameFont().get().getSize(), is( not( closeTo( 25, precision() ) ) ) );
      
      systemUnderTest.setJobNameFontSize( ANYTHING, 25.0 );
      systemUnderTest.setJobNameFontFamily( ANYTHING, commonFont() );
      
      assertThat( configuration.jobNameFont().get().getFamily(), is( commonFont() ) );
      assertThat( configuration.jobNameFont().get().getSize(), is( closeTo( 25, precision() ) ) );
   }//End Method
   
   @Test public void shouldChangeBuildNumberFontFamilyWhenFamilySet(){
      assertThat( configuration.buildNumberFont().get().getFamily(), is( not( commonFont() ) ) );
      systemUnderTest.setBuildNumberFontFamily( ANYTHING, commonFont() );
      assertThat( configuration.buildNumberFont().get().getFamily(), is( commonFont() ) );
   }//End Method
   
   @Test public void shouldChangeBuildNumberFontSzeWhenSizeSet(){
      assertThat( configuration.buildNumberFont().get().getSize(), is( not( closeTo( 25, precision() ) ) ) );
      systemUnderTest.setBuildNumberFontSize( ANYTHING, 25.0 );
      assertThat( configuration.buildNumberFont().get().getSize(), is( closeTo( 25, precision() ) ) );
   }//End Method
   
   @Test public void shouldPreserveBuildNumberFontFamilyWhenSizeSet(){
      assertThat( configuration.buildNumberFont().get().getFamily(), is( not( commonFont() ) ) );
      assertThat( configuration.buildNumberFont().get().getSize(), is( not( closeTo( 25, precision() ) ) ) );
      
      systemUnderTest.setBuildNumberFontFamily( ANYTHING, commonFont() );
      systemUnderTest.setBuildNumberFontSize( ANYTHING, 25.0 );
      
      assertThat( configuration.buildNumberFont().get().getFamily(), is( commonFont() ) );
      assertThat( configuration.buildNumberFont().get().getSize(), is( closeTo( 25, precision() ) ) );
   }//End Method
   
   @Test public void shouldPreserverBuildNumberFontSizeWhenFamilySet(){
      assertThat( configuration.buildNumberFont().get().getFamily(), is( not( commonFont() ) ) );
      assertThat( configuration.buildNumberFont().get().getSize(), is( not( closeTo( 25, precision() ) ) ) );
      
      systemUnderTest.setBuildNumberFontSize( ANYTHING, 25.0 );
      systemUnderTest.setBuildNumberFontFamily( ANYTHING, commonFont() );
      
      assertThat( configuration.buildNumberFont().get().getFamily(), is( commonFont() ) );
      assertThat( configuration.buildNumberFont().get().getSize(), is( closeTo( 25, precision() ) ) );
   }//End Method
   
   @Test public void shouldChangeCompletionEstimateFontFamilyWhenFamilySet(){
      assertThat( configuration.completionEstimateFont().get().getFamily(), is( not( commonFont() ) ) );
      systemUnderTest.setCompletionEstimateFontFamily( ANYTHING, commonFont() );
      assertThat( configuration.completionEstimateFont().get().getFamily(), is( commonFont() ) );
   }//End Method
   
   @Test public void shouldChangeCompletionEstimateFontSzeWhenSizeSet(){
      assertThat( configuration.completionEstimateFont().get().getSize(), is( not( closeTo( 25, precision() ) ) ) );
      systemUnderTest.setCompletionEstimateFontSize( ANYTHING, 25.0 );
      assertThat( configuration.completionEstimateFont().get().getSize(), is( closeTo( 25, precision() ) ) );
   }//End Method
   
   @Test public void shouldPreserveCompletionEstimateFontFamilyWhenSizeSet(){
      assertThat( configuration.completionEstimateFont().get().getFamily(), is( not( commonFont() ) ) );
      assertThat( configuration.completionEstimateFont().get().getSize(), is( not( closeTo( 25, precision() ) ) ) );
      
      systemUnderTest.setCompletionEstimateFontFamily( ANYTHING, commonFont() );
      systemUnderTest.setCompletionEstimateFontSize( ANYTHING, 25.0 );
      
      assertThat( configuration.completionEstimateFont().get().getFamily(), is( commonFont() ) );
      assertThat( configuration.completionEstimateFont().get().getSize(), is( closeTo( 25, precision() ) ) );
   }//End Method
   
   @Test public void shouldPreserverCompletionEstimateFontSizeWhenFamilySet(){
      assertThat( configuration.completionEstimateFont().get().getFamily(), is( not( commonFont() ) ) );
      assertThat( configuration.completionEstimateFont().get().getSize(), is( not( closeTo( 25, precision() ) ) ) );
      
      systemUnderTest.setCompletionEstimateFontSize( ANYTHING, 25.0 );
      systemUnderTest.setCompletionEstimateFontFamily( ANYTHING, commonFont() );
      
      assertThat( configuration.completionEstimateFont().get().getFamily(), is( commonFont() ) );
      assertThat( configuration.completionEstimateFont().get().getSize(), is( closeTo( 25, precision() ) ) );
   }//End Method
   
   @Test public void shouldChangeDetailFontFamilyWhenFamilySet(){
      assertThat( configuration.detailFont().get().getFamily(), is( not( commonFont() ) ) );
      systemUnderTest.setDetailFontFamily( ANYTHING, commonFont() );
      assertThat( configuration.detailFont().get().getFamily(), is( commonFont() ) );
   }//End Method
   
   @Test public void shouldChangeDetailFontSzeWhenSizeSet(){
      assertThat( configuration.detailFont().get().getSize(), is( not( closeTo( 25, precision() ) ) ) );
      systemUnderTest.setDetailFontSize( ANYTHING, 25.0 );
      assertThat( configuration.detailFont().get().getSize(), is( closeTo( 25, precision() ) ) );
   }//End Method
   
   @Test public void shouldPreserveDetailFontFamilyWhenSizeSet(){
      assertThat( configuration.detailFont().get().getFamily(), is( not( commonFont() ) ) );
      assertThat( configuration.detailFont().get().getSize(), is( not( closeTo( 25, precision() ) ) ) );
      
      systemUnderTest.setDetailFontFamily( ANYTHING, commonFont() );
      systemUnderTest.setDetailFontSize( ANYTHING, 25.0 );
      
      assertThat( configuration.detailFont().get().getFamily(), is( commonFont() ) );
      assertThat( configuration.detailFont().get().getSize(), is( closeTo( 25, precision() ) ) );
   }//End Method
   
   @Test public void shouldPreserverDetailFontSizeWhenFamilySet(){
      assertThat( configuration.detailFont().get().getFamily(), is( not( commonFont() ) ) );
      assertThat( configuration.detailFont().get().getSize(), is( not( closeTo( 25, precision() ) ) ) );
      
      systemUnderTest.setDetailFontSize( ANYTHING, 25.0 );
      systemUnderTest.setDetailFontFamily( ANYTHING, commonFont() );
      
      assertThat( configuration.detailFont().get().getFamily(), is( commonFont() ) );
      assertThat( configuration.detailFont().get().getSize(), is( closeTo( 25, precision() ) ) );
   }//End Method
   
   @Test public void shouldProvideJobNameConfigFont(){
      assertThat( configuration.jobNameFont().get().getFamily(), is( not( commonFont() ) ) );
      assertThat( configuration.jobNameFont().get().getSize(), is( not( closeTo( 25, precision() ) ) ) );
      
      configuration.jobNameFont().set( Font.font( commonFont(), 25 ) );
      
      assertThat( systemUnderTest.getJobNameFontFamily( ANYTHING ), is( commonFont() ) );
      assertThat( systemUnderTest.getJobNameFontSize( ANYTHING ), is( closeTo( 25, precision() ) ) );
   }//End Method
   
   @Test public void shouldProvideBuildNumberConfigFont(){
      assertThat( configuration.buildNumberFont().get().getFamily(), is( not( commonFont() ) ) );
      assertThat( configuration.buildNumberFont().get().getSize(), is( not( closeTo( 25, precision() ) ) ) );
      
      configuration.buildNumberFont().set( Font.font( commonFont(), 25 ) );
      
      assertThat( systemUnderTest.getBuildNumberFontFamily( ANYTHING ), is( commonFont() ) );
      assertThat( systemUnderTest.getBuildNumberFontSize( ANYTHING ), is( closeTo( 25, precision() ) ) );
   }//End Method
   
   @Test public void shouldProvideCompletionEstimateConfigFont(){
      assertThat( configuration.completionEstimateFont().get().getFamily(), is( not( commonFont() ) ) );
      assertThat( configuration.completionEstimateFont().get().getSize(), is( not( closeTo( 25, precision() ) ) ) );
      
      configuration.completionEstimateFont().set( Font.font( commonFont(), 25 ) );
      
      assertThat( systemUnderTest.getCompletionEstimateFontFamily( ANYTHING ), is( commonFont() ) );
      assertThat( systemUnderTest.getCompletionEstimateFontSize( ANYTHING ), is( closeTo( 25, precision() ) ) );
   }//End Method
   
   @Test public void shouldProvideDetailConfigFont(){
      assertThat( configuration.detailFont().get().getFamily(), is( not( commonFont() ) ) );
      assertThat( configuration.detailFont().get().getSize(), is( not( closeTo( 25, precision() ) ) ) );
      
      configuration.detailFont().set( Font.font( commonFont(), 25 ) );
      
      assertThat( systemUnderTest.getDetailFontFamily( ANYTHING ), is( commonFont() ) );
      assertThat( systemUnderTest.getDetailFontSize( ANYTHING ), is( closeTo( 25, precision() ) ) );
   }//End Method
   
   @Test public void shouldSetJobNameColorOnConfigWhenSet(){
      assertThat( configuration.jobNameColour().get(), is( not( Color.RED ) ) );
      systemUnderTest.setJobNameFontColour( ANYTHING, RED_HEX );
      assertThat( configuration.jobNameColour().get(), is( Color.RED ) );
   }//End Method

   @Test public void shouldSetBuildNumberColorOnConfigWhenSet(){
      assertThat( configuration.buildNumberColour().get(), is( not( Color.RED ) ) );
      systemUnderTest.setBuildNumberFontColour( ANYTHING, RED_HEX );
      assertThat( configuration.buildNumberColour().get(), is( Color.RED ) );
   }//End Method
   
   @Test public void shouldSetCompletionEstimateColorOnConfigWhenSet(){
      assertThat( configuration.completionEstimateColour().get(), is( not( Color.RED ) ) );
      systemUnderTest.setCompletionEstimateFontColour( ANYTHING, RED_HEX );
      assertThat( configuration.completionEstimateColour().get(), is( Color.RED ) );
   }//End Method
   
   @Test public void shouldSetDetailColorOnConfigWhenSet(){
      assertThat( configuration.detailColour().get(), is( not( Color.RED ) ) );
      systemUnderTest.setDetailFontColour( ANYTHING, RED_HEX );
      assertThat( configuration.detailColour().get(), is( Color.RED ) );
   }//End Method
   
   @Test public void shouldGetJobNameColorFromConfig(){
      assertThat( configuration.jobNameColour().get(), is( not( Color.RED ) ) );
      assertThat( systemUnderTest.getJobNameFontColour( ANYTHING ), is( not( RED_HEX ) ) );
      
      configuration.jobNameColour().set( Color.RED );
      assertThat( systemUnderTest.getJobNameFontColour( ANYTHING ), is( RED_HEX ) );  
   }//End Method
   
   @Test public void shouldGetBuildNumberColorFromConfig(){
      assertThat( configuration.buildNumberColour().get(), is( not( Color.RED ) ) );
      assertThat( systemUnderTest.getBuildNumberFontColour( ANYTHING ), is( not( RED_HEX ) ) );
      
      configuration.buildNumberColour().set( Color.RED );
      assertThat( systemUnderTest.getBuildNumberFontColour( ANYTHING ), is( RED_HEX ) );  
   }//End Method
   
   @Test public void shouldGetCompletionEstimateColorFromConfig(){
      assertThat( configuration.completionEstimateColour().get(), is( not( Color.RED ) ) );
      assertThat( systemUnderTest.getCompletionEstimateFontColour( ANYTHING ), is( not( RED_HEX ) ) );
      
      configuration.completionEstimateColour().set( Color.RED );
      assertThat( systemUnderTest.getCompletionEstimateFontColour( ANYTHING ), is( RED_HEX ) );  
   }//End Method
   
   @Test public void shouldGetDetailColorFromConfig(){
      assertThat( configuration.detailColour().get(), is( not( Color.RED ) ) );
      assertThat( systemUnderTest.getDetailFontColour( ANYTHING ), is( not( RED_HEX ) ) );
      
      configuration.detailColour().set( Color.RED );
      assertThat( systemUnderTest.getDetailFontColour( ANYTHING ), is( RED_HEX ) );  
   }//End Method
   
   @Test public void shouldNotConcurrentExceptionWhenIteratingOverJobPolicies() throws InterruptedException{
      TestCommon.assertConcurrencyIsNotAnIssue( 
               i -> configuration.jobPolicies().put( new JenkinsJobImpl( "" + i ), BuildWallJobPolicy.NeverShow ),
               i -> systemUnderTest.startWritingJobs( ANYTHING ),
               1000
      );
   }//End Method
   
}//End Class
