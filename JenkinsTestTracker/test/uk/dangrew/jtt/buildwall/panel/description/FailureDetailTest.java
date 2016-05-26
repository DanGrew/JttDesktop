/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.panel.description;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.sun.javafx.application.PlatformImpl;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uk.dangrew.jtt.buildwall.configuration.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.BuildWallConfigurationImpl;
import uk.dangrew.jtt.buildwall.panel.description.FailureDetail;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.TestPlatformDecouplerImpl;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.tests.TestCase;
import uk.dangrew.jtt.model.tests.TestCaseImpl;
import uk.dangrew.jtt.model.tests.TestClassImpl;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;

/**
 * {@link FailureDetail} test.
 */
public class FailureDetailTest {
   
   private BuildWallConfiguration configuration;
   private JenkinsUser rick;
   private JenkinsUser daryl;
   private JenkinsUser carl;
   
   private TestCase survivalTestCase;
   private TestCase zombieKillingTestCase;
   private TestCase spoilerTestCase;
   private TestCase cliffHangerTestCase;
   
   private JenkinsJob jenkinsJob;
   private FailureDetail systemUnderTest;
   
   @BeforeClass public static void initialisePlatform(){
      JavaFxInitializer.startPlatform();
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      
      jenkinsJob = new JenkinsJobImpl( "Some Job" );
      jenkinsJob.lastBuildStatusProperty().set( BuildResultStatus.UNSTABLE );
      
      rick = new JenkinsUserImpl( "Rick" );
      daryl = new JenkinsUserImpl( "Daryl" );
      carl = new JenkinsUserImpl( "Carl" );
      
      survivalTestCase = constructTestCase( "SurvivalTest", "shouldSurvive" );
      zombieKillingTestCase = constructTestCase( "ZombieKillingTest", "shouldStrikeTheHead" );
      spoilerTestCase = constructTestCase( "SpoilerTest", "shouldNotProvideSpoilers" );
      cliffHangerTestCase = constructTestCase( "CliffHangerTest", "shouldHaveAGoodButNotSillyCliffHanger" );
      
      jenkinsJob.culprits().addAll( rick, daryl, carl );
      jenkinsJob.failingTestCases().addAll( survivalTestCase, zombieKillingTestCase, spoilerTestCase, cliffHangerTestCase );
      
      configuration = new BuildWallConfigurationImpl();
      systemUnderTest = new FailureDetail( jenkinsJob, configuration );
   }//End Method

   @Ignore
   @Test public void manual() throws InterruptedException {
      JavaFxInitializer.launchInWindow( () -> {
         FailureDetail detail = new FailureDetail( jenkinsJob, new BuildWallConfigurationImpl() );
         detail.setBackground( new Background( new BackgroundFill( Color.BLACK, null, null ) ) );
         return detail;
      } );
      
      Thread.sleep( 2000 );
      PlatformImpl.runLater( () -> {
         jenkinsJob.culprits().remove( 1 );
         jenkinsJob.failingTestCases().clear();
      } );
      
      Thread.sleep( 2000 );
      PlatformImpl.runLater( () -> {
         jenkinsJob.culprits().addAll( new JenkinsUserImpl( "Maggie" ), new JenkinsUserImpl( "Carol" ) );
         jenkinsJob.failingTestCases().addAll( survivalTestCase, zombieKillingTestCase, spoilerTestCase, cliffHangerTestCase );
      } );
      
      Thread.sleep( 2000 );
      PlatformImpl.runLater( () -> {
         jenkinsJob.culprits().addAll( new JenkinsUserImpl( "Glenn" ), new JenkinsUserImpl( "Michonne" ) );
      } );
      
      Thread.sleep( 2000 );
      PlatformImpl.runLater( () -> {
         jenkinsJob.culprits().remove( 4 );
      } );
      
      Thread.sleep( 100000 );
   }//End Method
   
   private TestCase constructTestCase( String className, String caseName ) {
      return new TestCaseImpl( caseName, new TestClassImpl( className ) );
   }//End Method
   
   private String constructFailuresString( TestCase... cases ) {
      if ( cases.length == 0 ) {
         return "";
      }
      
      StringBuilder builder = new StringBuilder( "\n" );
      for ( int i = 0; i < cases.length - 1; i++ ) {
         builder.append( cases[ i ].testClassProperty().get().nameProperty().get() );
         builder.append( "\n");
      }
      builder.append( cases[ cases.length - 1 ].testClassProperty().get().nameProperty().get() );
      return builder.toString();
   }
   
   @Test public void shouldPrefixCulpritsDescription(){
      assertThat( systemUnderTest.culpritsLabel().getText(), startsWith( FailureDetail.CULPRITS_PREFIX ) );
   }//End Method
   
   @Test public void shouldListAllCulpritsInOrderDefinedByJob(){
      assertThat( systemUnderTest.culpritsLabel().getText(), is( "Suspects: Rick, Daryl, Carl." ) );
   }//End Method
   
   @Test public void shouldShowCulpritForSingle(){
      jenkinsJob.culprits().clear();
      jenkinsJob.culprits().add( rick );
      assertThat( systemUnderTest.culpritsLabel().getText(), is( "Suspect: Rick." ) );
   }//End Method
   
   @Test public void shouldShowNoCulpritsWhenNoneAvailable(){
      jenkinsJob.culprits().clear();
      assertThat( systemUnderTest.culpritsLabel().getText(), is( FailureDetail.NO_CULPRITS ) );
   }//End Method
   
   @Test public void shouldShowAbortedDescriptionWhenAborted(){
      jenkinsJob.failingTestCases().clear();
      jenkinsJob.lastBuildStatusProperty().set( BuildResultStatus.ABORTED );
      assertThat( systemUnderTest.failuresLabel().getText(), is( FailureDetail.ABORTED_DESCRIPTION ) );
   }//End Method
   
   @Test public void shouldShowFailureDescriptionWhenFailed(){
      jenkinsJob.failingTestCases().clear();
      jenkinsJob.lastBuildStatusProperty().set( BuildResultStatus.FAILURE );
      assertThat( systemUnderTest.failuresLabel().getText(), is( FailureDetail.FAILURE_DESCRIPTION ) );
   }//End Method
   
   @Test public void shouldUpdateCulpritsListWhenCulpritsAdded(){
      shouldListAllCulpritsInOrderDefinedByJob();
      
      jenkinsJob.culprits().add( new JenkinsUserImpl( "Walker" ) );
      assertThat( systemUnderTest.culpritsLabel().getText(), is( "Suspects: Rick, Daryl, Carl, Walker." ) );
      
      jenkinsJob.culprits().addAll( new JenkinsUserImpl( "Crawler" ), new JenkinsUserImpl( "Governor" ) );
      assertThat( systemUnderTest.culpritsLabel().getText(), is( "Suspects: Rick, Daryl, Carl, Walker, Crawler, Governor." ) );
   }//End Method
   
   @Test public void shouldUpdateCulpritsListWhenCulpritsRemoved(){
      jenkinsJob.culprits().addAll( new JenkinsUserImpl( "Walker" ), new JenkinsUserImpl( "Crawler" ), new JenkinsUserImpl( "Governor" ) );
      
      assertThat( systemUnderTest.culpritsLabel().getText(), is( "Suspects: Rick, Daryl, Carl, Walker, Crawler, Governor." ) );  
      jenkinsJob.culprits().removeAll( 
               jenkinsJob.culprits().get( 2 ), 
               jenkinsJob.culprits().get( 4 ) 
      );
      
      assertThat( systemUnderTest.culpritsLabel().getText(), is( "Suspects: Rick, Daryl, Walker, Governor." ) );
      jenkinsJob.culprits().remove( jenkinsJob.culprits().get( 2 ) );
      
      assertThat( systemUnderTest.culpritsLabel().getText(), is( "Suspects: Rick, Daryl, Governor." ) );
   }//End Method
   
   @Test public void shouldRestrainRowToFitInPanel(){
      assertThat( systemUnderTest.getRowConstraints(), hasSize( 2 ) );
      
      RowConstraints constraint = systemUnderTest.getRowConstraints().get( 0 );
      assertThat( constraint.getMaxHeight(), is( Double.MAX_VALUE ) );
      assertThat( constraint.getPercentHeight(), is( FailureDetail.CULPRITS_ROW_PERCENT ) );
      
      constraint = systemUnderTest.getRowConstraints().get( 1 );
      assertThat( constraint.getMaxHeight(), is( Double.MAX_VALUE ) );
      assertThat( constraint.getPercentHeight(), is( FailureDetail.FAILURES_ROW_PERCENT ) );
   }//End Method
   
   @Test public void shouldWrapTextToFillRow(){
      assertThat( systemUnderTest.culpritsLabel().isWrapText(), is( true ) );
      assertThat( systemUnderTest.failuresLabel().isWrapText(), is( true ) );
   }//End Method
   
   @Test public void shouldUseDefaultConfigurationOnText(){
      assertThat( systemUnderTest.culpritsLabel().getFont(), is( configuration.detailFont().get() ) );
      assertThat( systemUnderTest.culpritsLabel().getTextFill(), is( configuration.detailColour().get() ) );
      assertThat( systemUnderTest.failuresLabel().getFont(), is( configuration.detailFont().get() ) );
      assertThat( systemUnderTest.failuresLabel().getTextFill(), is( configuration.detailColour().get() ) );
   }//End Method
   
   @Test public void shouldUpdateCulpritsFontFromConfiguration(){
      assertThat( systemUnderTest.culpritsLabel().getFont(), is( configuration.detailFont().get() ) );
      
      Font alternateFont = Font.font( 100 );
      configuration.detailFont().set( alternateFont );
      
      assertThat( systemUnderTest.culpritsLabel().getFont(), is( alternateFont ) );
   }//End Method
   
   @Test public void shouldUpdateCulpritsColourFromConfiguration(){
      assertThat( systemUnderTest.culpritsLabel().getTextFill(), is( configuration.detailColour().get() ) );
      
      Color alternateColour = Color.AQUAMARINE;
      configuration.detailColour().set( alternateColour );
      
      assertThat( systemUnderTest.culpritsLabel().getTextFill(), is( alternateColour ) );
   }//End Method
   
   @Test public void shouldUpdateFailuresFontFromConfiguration(){
      assertThat( systemUnderTest.failuresLabel().getFont(), is( configuration.detailFont().get() ) );
      
      Font alternateFont = Font.font( 100 );
      configuration.detailFont().set( alternateFont );
      
      assertThat( systemUnderTest.failuresLabel().getFont(), is( alternateFont ) );
   }//End Method
   
   @Test public void shouldUpdateFailuresColourFromConfiguration(){
      assertThat( systemUnderTest.failuresLabel().getTextFill(), is( configuration.detailColour().get() ) );
      
      Color alternateColour = Color.AQUAMARINE;
      configuration.detailColour().set( alternateColour );
      
      assertThat( systemUnderTest.failuresLabel().getTextFill(), is( alternateColour ) );
   }//End Method
   
   @Test public void shouldDetachCulpritsFontListenersFromSystem(){
      systemUnderTest.detachFromSystem();
      
      Font original = configuration.detailFont().get();
      assertThat( systemUnderTest.culpritsLabel().getFont(), is( configuration.detailFont().get() ) );
      
      Font alternateFont = Font.font( 100 );
      configuration.detailFont().set( alternateFont );
      
      assertThat( systemUnderTest.culpritsLabel().getFont(), is( original ) );
   }//End Method
   
   @Test public void shouldDetachCulpritsColorListenersFromSystem(){
      systemUnderTest.detachFromSystem();
      
      Color orignal = configuration.detailColour().get();
      assertThat( systemUnderTest.culpritsLabel().getTextFill(), is( configuration.detailColour().get() ) );
      
      Color alternateColour = Color.AQUAMARINE;
      configuration.detailColour().set( alternateColour );
      
      assertThat( systemUnderTest.culpritsLabel().getTextFill(), is( orignal ) );
   }//End Method
   
   @Test public void shouldDetachCulpritUpdatesFromSystem(){
      systemUnderTest.detachFromSystem();
      
      assertThat( systemUnderTest.culpritsLabel().getText(), is( "Suspects: Rick, Daryl, Carl." ) );
      
      jenkinsJob.culprits().add( new JenkinsUserImpl( "Walker" ) );
      jenkinsJob.culprits().addAll( new JenkinsUserImpl( "Crawler" ), new JenkinsUserImpl( "Governor" ) );
      
      assertThat( systemUnderTest.culpritsLabel().getText(), is( "Suspects: Rick, Daryl, Carl." ) );
      
      jenkinsJob.culprits().removeAll( 
               jenkinsJob.culprits().get( 2 ), 
               jenkinsJob.culprits().get( 4 ) 
      );
      jenkinsJob.culprits().remove( jenkinsJob.culprits().get( 2 ) );
      
      assertThat( systemUnderTest.culpritsLabel().getText(), is( "Suspects: Rick, Daryl, Carl." ) );
   }//End Method
   
   @Test public void shouldDetachFailuresFontListenersFromSystem(){
      systemUnderTest.detachFromSystem();
      
      Font original = configuration.detailFont().get();
      assertThat( systemUnderTest.failuresLabel().getFont(), is( configuration.detailFont().get() ) );
      
      Font alternateFont = Font.font( 100 );
      configuration.detailFont().set( alternateFont );
      
      assertThat( systemUnderTest.failuresLabel().getFont(), is( original ) );
   }//End Method
   
   @Test public void shouldDetachFailuresColorListenersFromSystem(){
      systemUnderTest.detachFromSystem();
      
      Color orignal = configuration.detailColour().get();
      assertThat( systemUnderTest.failuresLabel().getTextFill(), is( configuration.detailColour().get() ) );
      
      Color alternateColour = Color.AQUAMARINE;
      configuration.detailColour().set( alternateColour );
      
      assertThat( systemUnderTest.failuresLabel().getTextFill(), is( orignal ) );
   }//End Method
   
   @Test public void shouldDetachFailuresUpdatesFromSystem(){
      systemUnderTest.detachFromSystem();
      
      assertThat( systemUnderTest.failuresLabel().getText(), is( "Failures:" + constructFailuresString( 
               survivalTestCase, zombieKillingTestCase, spoilerTestCase, cliffHangerTestCase 
      ) ) );
      
      TestCase anotherTestCase = constructTestCase( "AnotherTest", "AnotherCase" );
      jenkinsJob.failingTestCases().add( anotherTestCase );
      
      String expected = constructFailuresString( 
               survivalTestCase, zombieKillingTestCase, spoilerTestCase, cliffHangerTestCase
      );
      assertThat( systemUnderTest.failuresLabel().getText(), is( "Failures:" + expected ) );
      
      jenkinsJob.failingTestCases().removeAll( 
               jenkinsJob.failingTestCases().get( 2 ), 
               jenkinsJob.failingTestCases().get( 4 ) 
      );
      jenkinsJob.failingTestCases().remove( jenkinsJob.failingTestCases().get( 1 ) );
      
      assertThat( systemUnderTest.failuresLabel().getText(), is( "Failures:" + expected ) );
   }//End Method
   
   @Test public void shouldUseDecoupledPlatformToSetCulpritText(){
      DecoupledPlatformImpl.setInstance( runnable -> {} );
    
      jenkinsJob.culprits().add( new JenkinsUserImpl( "Walker" ) );
      assertThat( systemUnderTest.culpritsLabel().getText(), is( "Suspects: Rick, Daryl, Carl." ) );
      
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      jenkinsJob.culprits().add( new JenkinsUserImpl( "Walker" ) );
      assertThat( systemUnderTest.culpritsLabel().getText(), is( "Suspects: Rick, Daryl, Carl, Walker, Walker." ) );
   }//End Method
   
   @Test public void shouldUseDecoupledPlatformToSetFailuresText(){
      DecoupledPlatformImpl.setInstance( runnable -> {} );
    
      TestCase anotherTestCase = constructTestCase( "AnotherTest", "AnotherCase" );
      jenkinsJob.failingTestCases().add( anotherTestCase );
      
      String expected = constructFailuresString( 
               survivalTestCase, zombieKillingTestCase, spoilerTestCase, cliffHangerTestCase
      );
      assertThat( systemUnderTest.failuresLabel().getText(), is( "Failures:" + expected ) );
      
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      jenkinsJob.failingTestCases().add( anotherTestCase );
      
      expected = constructFailuresString( 
               survivalTestCase, zombieKillingTestCase, spoilerTestCase, cliffHangerTestCase, anotherTestCase
      );
      assertThat( systemUnderTest.failuresLabel().getText(), is( "Failures:" + expected ) );
   }//End Method
   
   @Test public void shouldPrefixFailuresDescription(){
      assertThat( systemUnderTest.failuresLabel().getText(), startsWith( FailureDetail.FAILING_TEST_CLASSES_PREFIX ) );
   }//End Method
   
   @Test public void shouldListAllFailuresInOrderDefinedByJob(){
      assertThat( 
               systemUnderTest.failuresLabel().getText(), 
               is( "Failures:" + constructFailuresString( survivalTestCase, zombieKillingTestCase, spoilerTestCase, cliffHangerTestCase ) ) 
      );
   }//End Method
   
   @Test public void shouldShowFailureForSingle(){
      jenkinsJob.failingTestCases().clear();
      jenkinsJob.failingTestCases().add( zombieKillingTestCase );
      assertThat( systemUnderTest.failuresLabel().getText(), is( "Failure:\nZombieKillingTest" ) );
   }//End Method
   
   @Test public void shouldShowNoFailuresWhenNoneAvailable(){
      jenkinsJob.failingTestCases().clear();
      assertThat( systemUnderTest.failuresLabel().getText(), is( "No Failures." ) );
   }//End Method
   
   @Test public void shouldUpdateFailuresListWhenFailuresAdded(){
      shouldListAllFailuresInOrderDefinedByJob();
      
      TestCase crossBowTest = constructTestCase( "WhereIsDarylsCrossBowTest", "anything" );
      jenkinsJob.failingTestCases().add( crossBowTest );
      
      String expected = constructFailuresString( 
               survivalTestCase, zombieKillingTestCase, spoilerTestCase, cliffHangerTestCase,
               crossBowTest
      );
      assertThat( systemUnderTest.failuresLabel().getText(), is( "Failures:" + expected ) );
      
      TestCase lucilleTest = constructTestCase( "LucilleIsEvilTest", "anything" ); 
      TestCase comicTest = constructTestCase( "ComicWasBetterTest", "anything" ); 
      jenkinsJob.failingTestCases().addAll( lucilleTest, comicTest );
      
      expected = constructFailuresString( 
               survivalTestCase, zombieKillingTestCase, spoilerTestCase, cliffHangerTestCase,
               crossBowTest, lucilleTest, comicTest
      );
      assertThat( systemUnderTest.failuresLabel().getText(), is( "Failures:" + expected ) );
   }//End Method
   
   @Test public void shouldUpdateFailuresListWhenFailuresRemoved(){
      jenkinsJob.failingTestCases().removeAll( 
               jenkinsJob.failingTestCases().get( 2 ), 
               jenkinsJob.failingTestCases().get( 1 ) 
      );
      
      String expected = constructFailuresString( 
               survivalTestCase, cliffHangerTestCase
      );
      assertThat( systemUnderTest.failuresLabel().getText(), is( "Failures:" + expected ) );
      jenkinsJob.failingTestCases().remove( 1 );
      
      assertThat( systemUnderTest.failuresLabel().getText(), is( "Failure:" + constructFailuresString( 
               survivalTestCase
      ) ) );
   }//End Method
   
   @Test public void failuresShouldHaveZeroLineSpacing(){
      assertThat( systemUnderTest.failuresLabel().getLineSpacing(), is( 0.0 ) );
   }//End Method
   
   @Test public void shouldUpdateFailuresWhenJobBuildStatusChanges(){
      jenkinsJob.failingTestCases().clear();
      assertThat( systemUnderTest.failuresLabel().getText(), is( FailureDetail.NO_FAILING_TESTS ) );
      
      jenkinsJob.lastBuildStatusProperty().set( BuildResultStatus.ABORTED );
      assertThat( systemUnderTest.failuresLabel().getText(), is( FailureDetail.ABORTED_DESCRIPTION ) );

      jenkinsJob.lastBuildStatusProperty().set( BuildResultStatus.FAILURE );
      assertThat( systemUnderTest.failuresLabel().getText(), is( FailureDetail.FAILURE_DESCRIPTION ) );
      
      jenkinsJob.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
      assertThat( systemUnderTest.failuresLabel().getText(), is( FailureDetail.NO_FAILING_TESTS ) );
   }//End Method
   
   @Test public void shouldDetachFailuresBuildStatusListenerFromSystem(){
      jenkinsJob.failingTestCases().clear();
      
      systemUnderTest.detachFromSystem();
      
      jenkinsJob.lastBuildStatusProperty().set( BuildResultStatus.ABORTED );
      assertThat( systemUnderTest.failuresLabel().getText(), is( FailureDetail.NO_FAILING_TESTS ) );
   }//End Method

}//End Class
