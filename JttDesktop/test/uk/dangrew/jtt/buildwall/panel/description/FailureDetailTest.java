/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.panel.description;

import static org.hamcrest.Matchers.contains;
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
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfigurationImpl;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.TestPlatformDecouplerImpl;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.nodes.JenkinsNode;
import uk.dangrew.jtt.model.nodes.JenkinsNodeImpl;
import uk.dangrew.jtt.model.tests.TestCase;
import uk.dangrew.jtt.model.tests.TestCaseImpl;
import uk.dangrew.jtt.model.tests.TestClassImpl;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;

/**
 * {@link FailureDetail} test.
 */
public class FailureDetailTest {
   
   private static final String FAILURES_PREFIX = "0/0 Failed:";
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
      jenkinsJob.setBuildStatus( BuildResultStatus.UNSTABLE );
      
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
         jenkinsJob.testFailureCount().set( 10 );
         jenkinsJob.testTotalCount().set( 1001 );
         jenkinsJob.culprits().remove( 1 );
         jenkinsJob.failingTestCases().clear();
      } );
      
      Thread.sleep( 2000 );
      PlatformImpl.runLater( () -> {
         jenkinsJob.testFailureCount().set( 11 );
         jenkinsJob.testTotalCount().set( 23001 );
         jenkinsJob.culprits().addAll( new JenkinsUserImpl( "Maggie" ), new JenkinsUserImpl( "Carol" ) );
         jenkinsJob.failingTestCases().addAll( survivalTestCase, zombieKillingTestCase, spoilerTestCase, cliffHangerTestCase );
      } );
      
      Thread.sleep( 2000 );
      PlatformImpl.runLater( () -> {
         jenkinsJob.culprits().addAll( new JenkinsUserImpl( "Glenn" ), new JenkinsUserImpl( "Michonne" ) );
      } );
      
      Thread.sleep( 2000 );
      PlatformImpl.runLater( () -> {
         jenkinsJob.testFailureCount().set( 999 );
         jenkinsJob.testTotalCount().set( 1000 );
         jenkinsJob.culprits().remove( 4 );
      } );
      
      Thread.sleep( 100000 );
   }//End Method
   
   private TestCase constructTestCase( String className, String caseName ) {
      return new TestCaseImpl( caseName, new TestClassImpl( className ) );
   }//End Method
   
   /**
    * Method to construct the expected failures {@link String}.
    * @param cases the {@link TestCase}s to construct for.
    * @return the expected {@link String}.
    */
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
   }//End Method
   
   @Test public void shouldContainAllElements(){
      assertThat( systemUnderTest.getChildren(), contains(
               systemUnderTest.culpritsLabel(), 
               systemUnderTest.lastBuiltOnLabel(), 
               systemUnderTest.failuresLabel() 
      ) );
   }//End Method
   
   @Test public void shouldPrefixCulpritsDescription(){
      assertThat( systemUnderTest.culpritsLabel().getText(), startsWith( FailureDetail.CULPRITS_PREFIX ) );
   }//End Method
   
   @Test public void shouldDisplayUnknownStateWhenNoNodeAssociated(){
      assertThat( systemUnderTest.lastBuiltOnLabel().getText(), is( FailureDetail.BUILT_ON_PREFIX + FailureDetail.UNKNOWN_NODE ) );
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
      jenkinsJob.setBuildStatus( BuildResultStatus.ABORTED );
      assertThat( systemUnderTest.failuresLabel().getText(), is( FailureDetail.ABORTED_DESCRIPTION ) );
   }//End Method
   
   @Test public void shouldShowFailureDescriptionWhenFailed(){
      jenkinsJob.failingTestCases().clear();
      jenkinsJob.setBuildStatus( BuildResultStatus.FAILURE );
      assertThat( systemUnderTest.failuresLabel().getText(), is( FailureDetail.FAILURE_DESCRIPTION ) );
   }//End Method
   
   @Test public void shouldShowLastBuiltOnWhenInitiallyPresent(){
      JenkinsNode node = new JenkinsNodeImpl( "This is a node." );
      jenkinsJob.builtOnProperty().set( node );
      
      systemUnderTest = new FailureDetail( jenkinsJob, configuration );
      assertThat( systemUnderTest.lastBuiltOnLabel().getText(), is( FailureDetail.BUILT_ON_PREFIX + node.nameProperty().get() ) );
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
   
   @Test public void shouldUpdateLastBuiltWhenStateChanges(){
      JenkinsNode node = new JenkinsNodeImpl( "This is a node." );
      jenkinsJob.builtOnProperty().set( node );
      assertThat( systemUnderTest.lastBuiltOnLabel().getText(), is( FailureDetail.BUILT_ON_PREFIX + node.nameProperty().get() ) );
   }//End Method
   
   @Test public void shouldRestrainRowToFitInPanel(){
      assertThat( systemUnderTest.getRowConstraints(), hasSize( 3 ) );
      
      RowConstraints constraint = systemUnderTest.getRowConstraints().get( 0 );
      assertThat( constraint.getMaxHeight(), is( Double.MAX_VALUE ) );
      assertThat( constraint.getPercentHeight(), is( FailureDetail.CULPRITS_ROW_PERCENT ) );
      
      constraint = systemUnderTest.getRowConstraints().get( 1 );
      assertThat( constraint.getMaxHeight(), is( Double.MAX_VALUE ) );
      assertThat( constraint.getPercentHeight(), is( FailureDetail.BUILT_ON_ROW_PERCENT ) );
      
      constraint = systemUnderTest.getRowConstraints().get( 2 );
      assertThat( constraint.getMaxHeight(), is( Double.MAX_VALUE ) );
      assertThat( constraint.getPercentHeight(), is( FailureDetail.FAILURES_ROW_PERCENT ) );
   }//End Method
   
   @Test public void shouldWrapTextToFillRow(){
      assertThat( systemUnderTest.culpritsLabel().isWrapText(), is( true ) );
      assertThat( systemUnderTest.lastBuiltOnLabel().isWrapText(), is( true ) );
      assertThat( systemUnderTest.failuresLabel().isWrapText(), is( true ) );
   }//End Method
   
   @Test public void shouldUseDefaultConfigurationOnText(){
      assertThat( systemUnderTest.culpritsLabel().getFont(), is( configuration.detailFont().get() ) );
      assertThat( systemUnderTest.culpritsLabel().getTextFill(), is( configuration.detailColour().get() ) );
      assertThat( systemUnderTest.lastBuiltOnLabel().getFont(), is( configuration.detailFont().get() ) );
      assertThat( systemUnderTest.lastBuiltOnLabel().getTextFill(), is( configuration.detailColour().get() ) );
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
   
   @Test public void shouldUpdateBuiltOnFontFromConfiguration(){
      assertThat( systemUnderTest.lastBuiltOnLabel().getFont(), is( configuration.detailFont().get() ) );
      
      Font alternateFont = Font.font( 100 );
      configuration.detailFont().set( alternateFont );
      
      assertThat( systemUnderTest.lastBuiltOnLabel().getFont(), is( alternateFont ) );
   }//End Method
   
   @Test public void shouldUpdateBuiltOnColourFromConfiguration(){
      assertThat( systemUnderTest.lastBuiltOnLabel().getTextFill(), is( configuration.detailColour().get() ) );
      
      Color alternateColour = Color.AQUAMARINE;
      configuration.detailColour().set( alternateColour );
      
      assertThat( systemUnderTest.lastBuiltOnLabel().getTextFill(), is( alternateColour ) );
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
   
   @Test public void shouldDetachBuiltOnFontListenersFromSystem(){
      systemUnderTest.detachFromSystem();
      
      Font original = configuration.detailFont().get();
      assertThat( systemUnderTest.lastBuiltOnLabel().getFont(), is( configuration.detailFont().get() ) );
      
      Font alternateFont = Font.font( 100 );
      configuration.detailFont().set( alternateFont );
      
      assertThat( systemUnderTest.lastBuiltOnLabel().getFont(), is( original ) );
   }//End Method
   
   @Test public void shouldDetachBuiltOnColorListenersFromSystem(){
      systemUnderTest.detachFromSystem();
      
      Color orignal = configuration.detailColour().get();
      assertThat( systemUnderTest.lastBuiltOnLabel().getTextFill(), is( configuration.detailColour().get() ) );
      
      Color alternateColour = Color.AQUAMARINE;
      configuration.detailColour().set( alternateColour );
      
      assertThat( systemUnderTest.lastBuiltOnLabel().getTextFill(), is( orignal ) );
   }//End Method
   
   @Test public void shouldDetachBuiltOnUpdatesFromSystem(){
      systemUnderTest.detachFromSystem();
      assertThat( systemUnderTest.lastBuiltOnLabel().getText(), is( FailureDetail.BUILT_ON_PREFIX + FailureDetail.UNKNOWN_NODE ) );
      
      jenkinsJob.builtOnProperty().set( new JenkinsNodeImpl( "anything" ) );
      assertThat( systemUnderTest.lastBuiltOnLabel().getText(), is( FailureDetail.BUILT_ON_PREFIX + FailureDetail.UNKNOWN_NODE ) );
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
      
      assertThat( systemUnderTest.failuresLabel().getText(), is( FAILURES_PREFIX + constructFailuresString( 
               survivalTestCase, zombieKillingTestCase, spoilerTestCase, cliffHangerTestCase 
      ) ) );
      
      TestCase anotherTestCase = constructTestCase( "AnotherTest", "AnotherCase" );
      jenkinsJob.failingTestCases().add( anotherTestCase );
      
      String expected = constructFailuresString( 
               survivalTestCase, zombieKillingTestCase, spoilerTestCase, cliffHangerTestCase
      );
      assertThat( systemUnderTest.failuresLabel().getText(), is( FAILURES_PREFIX + expected ) );
      
      jenkinsJob.failingTestCases().removeAll( 
               jenkinsJob.failingTestCases().get( 2 ), 
               jenkinsJob.failingTestCases().get( 4 ) 
      );
      jenkinsJob.failingTestCases().remove( jenkinsJob.failingTestCases().get( 1 ) );
      
      assertThat( systemUnderTest.failuresLabel().getText(), is( FAILURES_PREFIX + expected ) );
   }//End Method
   
   @Test public void shouldUseDecoupledPlatformToSetCulpritText(){
      DecoupledPlatformImpl.setInstance( runnable -> {} );
    
      jenkinsJob.culprits().add( new JenkinsUserImpl( "Walker" ) );
      assertThat( systemUnderTest.culpritsLabel().getText(), is( "Suspects: Rick, Daryl, Carl." ) );
      
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      jenkinsJob.culprits().add( new JenkinsUserImpl( "Walker" ) );
      assertThat( systemUnderTest.culpritsLabel().getText(), is( "Suspects: Rick, Daryl, Carl, Walker, Walker." ) );
   }//End Method
   
   @Test public void shouldUseDecoupledPlatformToSetBuiltOnText(){
      DecoupledPlatformImpl.setInstance( runnable -> {} );
    
      jenkinsJob.builtOnProperty().set( new JenkinsNodeImpl( "anything" ) );
      assertThat( systemUnderTest.lastBuiltOnLabel().getText(), is( FailureDetail.BUILT_ON_PREFIX + FailureDetail.UNKNOWN_NODE ) );
      
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      jenkinsJob.builtOnProperty().set( new JenkinsNodeImpl( "something" ) );
      assertThat( systemUnderTest.lastBuiltOnLabel().getText(), is( FailureDetail.BUILT_ON_PREFIX + "something" ) );
   }//End Method
   
   @Test public void shouldUseDecoupledPlatformToSetFailuresText(){
      DecoupledPlatformImpl.setInstance( runnable -> {} );
    
      TestCase anotherTestCase = constructTestCase( "AnotherTest", "AnotherCase" );
      jenkinsJob.failingTestCases().add( anotherTestCase );
      
      String expected = constructFailuresString( 
               survivalTestCase, zombieKillingTestCase, spoilerTestCase, cliffHangerTestCase
      );
      assertThat( systemUnderTest.failuresLabel().getText(), is( FAILURES_PREFIX + expected ) );
      
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      jenkinsJob.failingTestCases().add( anotherTestCase );
      
      expected = constructFailuresString( 
               survivalTestCase, zombieKillingTestCase, spoilerTestCase, cliffHangerTestCase, anotherTestCase
      );
      assertThat( systemUnderTest.failuresLabel().getText(), is( FAILURES_PREFIX + expected ) );
   }//End Method
   
   @Test public void shouldPrefixFailuresDescription(){
      assertThat( systemUnderTest.failuresLabel().getText(), startsWith( FAILURES_PREFIX ) );
   }//End Method
   
   @Test public void shouldListAllFailuresInOrderDefinedByJob(){
      assertThat( 
               systemUnderTest.failuresLabel().getText(), 
               is( FAILURES_PREFIX + constructFailuresString( survivalTestCase, zombieKillingTestCase, spoilerTestCase, cliffHangerTestCase ) ) 
      );
   }//End Method
   
   @Test public void shouldShowFailureForSingle(){
      jenkinsJob.failingTestCases().clear();
      jenkinsJob.failingTestCases().add( zombieKillingTestCase );
      assertThat( systemUnderTest.failuresLabel().getText(), is( "0/0 Failed:\nZombieKillingTest" ) );
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
      assertThat( systemUnderTest.failuresLabel().getText(), is( FAILURES_PREFIX + expected ) );
      
      TestCase lucilleTest = constructTestCase( "LucilleIsEvilTest", "anything" ); 
      TestCase comicTest = constructTestCase( "ComicWasBetterTest", "anything" ); 
      jenkinsJob.failingTestCases().addAll( lucilleTest, comicTest );
      
      expected = constructFailuresString( 
               survivalTestCase, zombieKillingTestCase, spoilerTestCase, cliffHangerTestCase,
               crossBowTest, lucilleTest, comicTest
      );
      assertThat( systemUnderTest.failuresLabel().getText(), is( FAILURES_PREFIX + expected ) );
   }//End Method
   
   @Test public void shouldUpdateFailuresListWhenFailuresRemoved(){
      jenkinsJob.failingTestCases().removeAll( 
               jenkinsJob.failingTestCases().get( 2 ), 
               jenkinsJob.failingTestCases().get( 1 ) 
      );
      
      String expected = constructFailuresString( 
               survivalTestCase, cliffHangerTestCase
      );
      assertThat( systemUnderTest.failuresLabel().getText(), is( FAILURES_PREFIX + expected ) );
      jenkinsJob.failingTestCases().remove( 1 );
      
      assertThat( systemUnderTest.failuresLabel().getText(), is( FAILURES_PREFIX + constructFailuresString( 
               survivalTestCase
      ) ) );
   }//End Method
   
   @Test public void failuresShouldHaveZeroLineSpacing(){
      assertThat( systemUnderTest.failuresLabel().getLineSpacing(), is( 0.0 ) );
   }//End Method
   
   @Test public void shouldUpdateFailuresWhenJobBuildChanges(){
      jenkinsJob.failingTestCases().clear();
      assertThat( systemUnderTest.failuresLabel().getText(), is( FailureDetail.NO_FAILING_TESTS ) );
      
      jenkinsJob.setBuildStatus( BuildResultStatus.ABORTED );
      assertThat( systemUnderTest.failuresLabel().getText(), is( FailureDetail.ABORTED_DESCRIPTION ) );

      jenkinsJob.setBuildStatus( BuildResultStatus.FAILURE );
      assertThat( systemUnderTest.failuresLabel().getText(), is( FailureDetail.FAILURE_DESCRIPTION ) );
      
      jenkinsJob.setBuildStatus( BuildResultStatus.SUCCESS );
      assertThat( systemUnderTest.failuresLabel().getText(), is( FailureDetail.NO_FAILING_TESTS ) );
   }//End Method
   
   @Test public void shouldDetachFailuresBuildListenerFromSystem(){
      jenkinsJob.failingTestCases().clear();
      
      systemUnderTest.detachFromSystem();
      
      jenkinsJob.setBuildStatus( BuildResultStatus.ABORTED );
      assertThat( systemUnderTest.failuresLabel().getText(), is( FailureDetail.NO_FAILING_TESTS ) );
   }//End Method
   
   @Test public void shouldBeDetachedInApiAfterDetachment(){
      assertThat( systemUnderTest.isDetached(), is( false ) );
      systemUnderTest.detachFromSystem();
      assertThat( systemUnderTest.isDetached(), is( true ) );
   }//End Method
   
   @Test public void shouldPrefixFailuresWithNumberOfFailedOverTotal(){
      jenkinsJob.testFailureCount().set( 201 );
      jenkinsJob.testTotalCount().set( 9876 );
      assertThat( systemUnderTest.failuresLabel().getText(), startsWith( "201/9876 Failed:" ) );
   }//End Method
   
   @Test public void shouldUpdateFailedNumberInFailuresText(){
      jenkinsJob.testFailureCount().set( 201 );
      jenkinsJob.testTotalCount().set( 9876 );
      assertThat( systemUnderTest.failuresLabel().getText(), startsWith( "201/9876 Failed:" ) );
      jenkinsJob.testFailureCount().set( 564 );
      assertThat( systemUnderTest.failuresLabel().getText(), startsWith( "564/9876 Failed:" ) );
   }//End Method
   
   @Test public void shouldUpdateTotalNumberInFailuresText(){
      jenkinsJob.testFailureCount().set( 201 );
      jenkinsJob.testTotalCount().set( 9876 );
      assertThat( systemUnderTest.failuresLabel().getText(), startsWith( "201/9876 Failed:" ) );
      jenkinsJob.testTotalCount().set( 202 );
      assertThat( systemUnderTest.failuresLabel().getText(), startsWith( "201/202 Failed:" ) );
   }//End Method

}//End Class
