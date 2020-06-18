/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.panel.description;


import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfigurationImpl;
import uk.dangrew.jtt.desktop.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.desktop.graphics.TestPlatformDecouplerImpl;
import uk.dangrew.jtt.model.commit.Commit;
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
import uk.dangrew.kode.javafx.platform.JavaFxThreading;
import uk.dangrew.kode.launch.TestApplication;

import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * {@link FailureDetail} test.
 */
public class FailureDetailTest {
   
   private static final String FAILURES_PREFIX = "0/0 Failed:";
   private BuildWallConfiguration configuration;
   
   private JenkinsUser rick;
   private JenkinsUser daryl;
   private JenkinsUser carl;
   private JenkinsUser jesus;
   private JenkinsUser negan;
   private JenkinsUser walker;
   private JenkinsUser crawler;
   private JenkinsUser govenor;
   
   private Commit commitForRick;
   private Commit commitForDaryl;
   private Commit commitForCarl;
   private Commit commitForJesus;
   private Commit commitForNegan;
   private Commit commitForWalker;
   private Commit commitForCrawler;
   private Commit commitForGovenor;
   
   private TestCase survivalTestCase;
   private TestCase zombieKillingTestCase;
   private TestCase spoilerTestCase;
   private TestCase cliffHangerTestCase;
   
   private JenkinsJob jenkinsJob;
   private FailureDetail systemUnderTest;
   
   @BeforeClass public static void initialisePlatform(){
      TestApplication.startPlatform();
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      
      jenkinsJob = new JenkinsJobImpl( "Some Job" );
      jenkinsJob.setBuildStatus( BuildResultStatus.UNSTABLE );
      
      rick = new JenkinsUserImpl( "Rick" );
      daryl = new JenkinsUserImpl( "Daryl" );
      carl = new JenkinsUserImpl( "Carl" );
      jesus = new JenkinsUserImpl( "Jesus" );
      negan = new JenkinsUserImpl( "Negan" );
      walker = new JenkinsUserImpl( "Walker" );
      crawler = new JenkinsUserImpl( "Crawler" );
      govenor = new JenkinsUserImpl( "Governor" );
      
      commitForRick = makeCommitFor( rick );
      commitForDaryl = makeCommitFor( daryl );
      commitForCarl = makeCommitFor( carl );
      commitForJesus = makeCommitFor( jesus );
      commitForNegan = makeCommitFor( negan );
      commitForWalker = makeCommitFor( walker );
      commitForCrawler = makeCommitFor( crawler );
      commitForGovenor = makeCommitFor( govenor );
      
      survivalTestCase = constructTestCase( "SurvivalTest", "shouldSurvive" );
      zombieKillingTestCase = constructTestCase( "ZombieKillingTest", "shouldStrikeTheHead" );
      spoilerTestCase = constructTestCase( "SpoilerTest", "shouldNotProvideSpoilers" );
      cliffHangerTestCase = constructTestCase( "CliffHangerTest", "shouldHaveAGoodButNotSillyCliffHanger" );
      
      jenkinsJob.commits().addAll( commitForRick, commitForDaryl, commitForCarl );
      jenkinsJob.supplements().commitsSinceLastFailure().addAll( commitForJesus, commitForNegan );
      jenkinsJob.failingTestCases().addAll( survivalTestCase, zombieKillingTestCase, spoilerTestCase, cliffHangerTestCase );
      
      configuration = new BuildWallConfigurationImpl();
      systemUnderTest = new FailureDetail( jenkinsJob, configuration );
   }//End Method

   @Ignore
   @Test public void manual() throws InterruptedException {
      TestApplication.launch( () -> {
         FailureDetail detail = new FailureDetail( jenkinsJob, new BuildWallConfigurationImpl() );
         detail.setBackground( new Background( new BackgroundFill( Color.BLACK, null, null ) ) );
         return detail;
      } );
      
      Thread.sleep( 2000 );
       JavaFxThreading.runLater( () -> {
         jenkinsJob.testFailureCount().set( 10 );
         jenkinsJob.testTotalCount().set( 1001 );
         jenkinsJob.commits().remove( 1 );
         jenkinsJob.failingTestCases().clear();
      } );
      
      Thread.sleep( 2000 );
       JavaFxThreading.runLater( () -> {
         jenkinsJob.testFailureCount().set( 11 );
         jenkinsJob.testTotalCount().set( 23001 );
         jenkinsJob.commits().addAll( 
                  makeCommitFor( new JenkinsUserImpl( "Maggie" ) ), 
                  makeCommitFor( new JenkinsUserImpl( "Carol" ) )
         );
         jenkinsJob.failingTestCases().addAll( survivalTestCase, zombieKillingTestCase, spoilerTestCase, cliffHangerTestCase );
      } );
      
      Thread.sleep( 2000 );
       JavaFxThreading.runLater( () -> {
         jenkinsJob.commits().addAll( 
                  makeCommitFor( new JenkinsUserImpl( "Glenn" ) ), 
                  makeCommitFor( new JenkinsUserImpl( "Michonne" ) )
         );
      } );
      
      Thread.sleep( 2000 );
       JavaFxThreading.runLater( () -> {
         jenkinsJob.testFailureCount().set( 999 );
         jenkinsJob.testTotalCount().set( 1000 );
         jenkinsJob.commits().remove( 4 );
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
               systemUnderTest.sinceLastFailureLabel(),
               systemUnderTest.newCommittersLabel(),
               systemUnderTest.lastBuiltOnLabel(), 
               systemUnderTest.failuresLabel() 
      ) );
   }//End Method
   
   @Test public void shouldPrefixCommittersDescription(){
      assertThat( systemUnderTest.sinceLastFailureLabel().getText(), startsWith( FailureDetail.SINCE_LAST_FAILURE_PREFIX ) );
      assertThat( systemUnderTest.newCommittersLabel().getText(), startsWith( FailureDetail.NEW_COMMITTERS_PREFIX ) );
   }//End Method
   
   @Test public void shouldDisplayUnknownStateWhenNoNodeAssociated(){
      assertThat( systemUnderTest.lastBuiltOnLabel().getText(), is( FailureDetail.BUILT_ON_PREFIX + FailureDetail.UNKNOWN_NODE ) );
   }//End Method
   
   @Test public void shouldListAllCommittersInOrderDefinedByJob(){
      assertThat( systemUnderTest.sinceLastFailureLabel().getText(), is( "Since Last Failure: Jesus, Negan." ) );
      assertThat( systemUnderTest.newCommittersLabel().getText(), is( "New Committers: Rick, Daryl, Carl." ) );
   }//End Method
   
   @Test public void shouldShowNoCommittersWhenNoneAvailable(){
      jenkinsJob.commits().clear();
      assertThat( systemUnderTest.newCommittersLabel().getText(), is( FailureDetail.NEW_COMMITTERS_PREFIX + FailureDetail.NO_COMMITTERS ) );
      jenkinsJob.supplements().commitsSinceLastFailure().clear();
      assertThat( systemUnderTest.sinceLastFailureLabel().getText(), is( FailureDetail.SINCE_LAST_FAILURE_PREFIX + FailureDetail.NO_COMMITTERS ) );
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
   
   @Test public void shouldUpdateNewCommittersListWhenAdded(){
      shouldListAllCommittersInOrderDefinedByJob();
      
      jenkinsJob.commits().add( commitForWalker );
      assertThat( systemUnderTest.newCommittersLabel().getText(), is( "New Committers: Rick, Daryl, Carl, Walker." ) );
      
      jenkinsJob.commits().addAll( commitForCrawler, commitForGovenor );
      assertThat( systemUnderTest.newCommittersLabel().getText(), is( "New Committers: Rick, Daryl, Carl, Walker, Crawler, Governor." ) );
   }//End Method
   
   @Test public void shouldUpdateNewCommittersListWhenRemoved(){
      jenkinsJob.commits().addAll( commitForWalker, commitForCrawler, commitForGovenor );
      
      assertThat( systemUnderTest.newCommittersLabel().getText(), is( "New Committers: Rick, Daryl, Carl, Walker, Crawler, Governor." ) );  
      jenkinsJob.commits().removeAll( 
               jenkinsJob.commits().get( 2 ), 
               jenkinsJob.commits().get( 4 ) 
      );
      
      assertThat( systemUnderTest.newCommittersLabel().getText(), is( "New Committers: Rick, Daryl, Walker, Governor." ) );
      jenkinsJob.commits().remove( jenkinsJob.commits().get( 2 ) );
      
      assertThat( systemUnderTest.newCommittersLabel().getText(), is( "New Committers: Rick, Daryl, Governor." ) );
   }//End Method
   
   @Test public void shouldUpdateSinceFailureListWhenAdded(){
      shouldListAllFailuresInOrderDefinedByJob();
      
      jenkinsJob.supplements().commitsSinceLastFailure().add( commitForWalker );
      assertThat( systemUnderTest.sinceLastFailureLabel().getText(), is( "Since Last Failure: Jesus, Negan, Walker." ) );
      
      jenkinsJob.supplements().commitsSinceLastFailure().addAll( commitForCrawler, commitForGovenor );
      assertThat( systemUnderTest.sinceLastFailureLabel().getText(), is( "Since Last Failure: Jesus, Negan, Walker, Crawler, Governor." ) );
   }//End Method
   
   @Test public void shouldUpdateSinceFailureListWhenRemoved(){
      jenkinsJob.supplements().commitsSinceLastFailure().addAll( commitForWalker, commitForCrawler, commitForGovenor );
      
      assertThat( systemUnderTest.sinceLastFailureLabel().getText(), is( "Since Last Failure: Jesus, Negan, Walker, Crawler, Governor." ) );  
      jenkinsJob.supplements().commitsSinceLastFailure().removeAll( 
               jenkinsJob.supplements().commitsSinceLastFailure().get( 2 ), 
               jenkinsJob.supplements().commitsSinceLastFailure().get( 4 ) 
      );
      
      assertThat( systemUnderTest.sinceLastFailureLabel().getText(), is( "Since Last Failure: Jesus, Negan, Crawler." ) );
      jenkinsJob.supplements().commitsSinceLastFailure().remove( jenkinsJob.commits().get( 2 ) );
      
      assertThat( systemUnderTest.sinceLastFailureLabel().getText(), is( "Since Last Failure: Jesus, Negan, Crawler." ) );
   }//End Method
   
   @Test public void shouldUpdateLastBuiltWhenStateChanges(){
      JenkinsNode node = new JenkinsNodeImpl( "This is a node." );
      jenkinsJob.builtOnProperty().set( node );
      assertThat( systemUnderTest.lastBuiltOnLabel().getText(), is( FailureDetail.BUILT_ON_PREFIX + node.nameProperty().get() ) );
   }//End Method
   
   @Test public void shouldRestrainRowToFitInPanel(){
      assertThat( systemUnderTest.getRowConstraints(), hasSize( 4 ) );
      
      RowConstraints constraint = systemUnderTest.getRowConstraints().get( 0 );
      assertThat( constraint.getMaxHeight(), is( Double.MAX_VALUE ) );
      assertThat( constraint.getPercentHeight(), is( FailureDetail.SINCE_FAILURE_COMMITTERS_ROW_PERCENT ) );
      
      constraint = systemUnderTest.getRowConstraints().get( 1 );
      assertThat( constraint.getMaxHeight(), is( Double.MAX_VALUE ) );
      assertThat( constraint.getPercentHeight(), is( FailureDetail.NEW_COMMITTERS_ROW_PERCENT ) );
      
      constraint = systemUnderTest.getRowConstraints().get( 2 );
      assertThat( constraint.getMaxHeight(), is( Double.MAX_VALUE ) );
      assertThat( constraint.getPercentHeight(), is( FailureDetail.BUILT_ON_ROW_PERCENT ) );
      
      constraint = systemUnderTest.getRowConstraints().get( 3 );
      assertThat( constraint.getMaxHeight(), is( Double.MAX_VALUE ) );
      assertThat( constraint.getPercentHeight(), is( FailureDetail.FAILURES_ROW_PERCENT ) );
   }//End Method
   
   @Test public void shouldWrapTextToFillRow(){
      assertThat( systemUnderTest.sinceLastFailureLabel().isWrapText(), is( true ) );
      assertThat( systemUnderTest.newCommittersLabel().isWrapText(), is( true ) );
      assertThat( systemUnderTest.lastBuiltOnLabel().isWrapText(), is( true ) );
      assertThat( systemUnderTest.failuresLabel().isWrapText(), is( true ) );
   }//End Method
   
   @Test public void shouldUseDefaultConfigurationOnText(){
      assertThat( systemUnderTest.sinceLastFailureLabel().getFont(), is( configuration.detailFont().get() ) );
      assertThat( systemUnderTest.sinceLastFailureLabel().getTextFill(), is( configuration.detailColour().get() ) );
      assertThat( systemUnderTest.newCommittersLabel().getFont(), is( configuration.detailFont().get() ) );
      assertThat( systemUnderTest.newCommittersLabel().getTextFill(), is( configuration.detailColour().get() ) );
      assertThat( systemUnderTest.lastBuiltOnLabel().getFont(), is( configuration.detailFont().get() ) );
      assertThat( systemUnderTest.lastBuiltOnLabel().getTextFill(), is( configuration.detailColour().get() ) );
      assertThat( systemUnderTest.failuresLabel().getFont(), is( configuration.detailFont().get() ) );
      assertThat( systemUnderTest.failuresLabel().getTextFill(), is( configuration.detailColour().get() ) );
   }//End Method
   
   @Test public void shouldUpdateCommittersFontFromConfiguration(){
      assertThat( systemUnderTest.sinceLastFailureLabel().getFont(), is( configuration.detailFont().get() ) );
      assertThat( systemUnderTest.newCommittersLabel().getFont(), is( configuration.detailFont().get() ) );
      
      Font alternateFont = Font.font( 100 );
      configuration.detailFont().set( alternateFont );
      
      assertThat( systemUnderTest.sinceLastFailureLabel().getFont(), is( alternateFont ) );
      assertThat( systemUnderTest.newCommittersLabel().getFont(), is( alternateFont ) );
   }//End Method
   
   @Test public void shouldUpdateCommittersColourFromConfiguration(){
      assertThat( systemUnderTest.sinceLastFailureLabel().getTextFill(), is( configuration.detailColour().get() ) );
      assertThat( systemUnderTest.newCommittersLabel().getTextFill(), is( configuration.detailColour().get() ) );
      
      Color alternateColour = Color.AQUAMARINE;
      configuration.detailColour().set( alternateColour );
      
      assertThat( systemUnderTest.sinceLastFailureLabel().getTextFill(), is( alternateColour ) );
      assertThat( systemUnderTest.newCommittersLabel().getTextFill(), is( alternateColour ) );
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
   
   @Test public void shouldDetachCommittersFontListenersFromSystem(){
      systemUnderTest.detachFromSystem();
      
      Font original = configuration.detailFont().get();
      assertThat( systemUnderTest.sinceLastFailureLabel().getFont(), is( configuration.detailFont().get() ) );
      assertThat( systemUnderTest.newCommittersLabel().getFont(), is( configuration.detailFont().get() ) );
      
      Font alternateFont = Font.font( 100 );
      configuration.detailFont().set( alternateFont );
      
      assertThat( systemUnderTest.sinceLastFailureLabel().getFont(), is( original ) );
      assertThat( systemUnderTest.newCommittersLabel().getFont(), is( original ) );
   }//End Method
   
   @Test public void shouldDetachCommittersColorListenersFromSystem(){
      systemUnderTest.detachFromSystem();
      
      Color orignal = configuration.detailColour().get();
      assertThat( systemUnderTest.sinceLastFailureLabel().getTextFill(), is( configuration.detailColour().get() ) );
      assertThat( systemUnderTest.newCommittersLabel().getTextFill(), is( configuration.detailColour().get() ) );
      
      Color alternateColour = Color.AQUAMARINE;
      configuration.detailColour().set( alternateColour );
      
      assertThat( systemUnderTest.sinceLastFailureLabel().getTextFill(), is( orignal ) );
      assertThat( systemUnderTest.newCommittersLabel().getTextFill(), is( orignal ) );
   }//End Method
   
   @Test public void shouldDetachCommittersUpdatesFromSystem(){
      systemUnderTest.detachFromSystem();
      
      assertThat( systemUnderTest.sinceLastFailureLabel().getText(), is( "Since Last Failure: Jesus, Negan." ) );
      assertThat( systemUnderTest.newCommittersLabel().getText(), is( "New Committers: Rick, Daryl, Carl." ) );
      
      jenkinsJob.commits().add( commitForWalker );
      jenkinsJob.commits().addAll( commitForCrawler, commitForGovenor );
      jenkinsJob.supplements().commitsSinceLastFailure().add( commitForWalker );
      jenkinsJob.supplements().commitsSinceLastFailure().addAll( commitForCrawler, commitForGovenor );
      
      assertThat( systemUnderTest.sinceLastFailureLabel().getText(), is( "Since Last Failure: Jesus, Negan." ) );
      assertThat( systemUnderTest.newCommittersLabel().getText(), is( "New Committers: Rick, Daryl, Carl." ) );
      
      jenkinsJob.commits().removeAll( 
               jenkinsJob.commits().get( 2 ), 
               jenkinsJob.commits().get( 4 ) 
      );
      jenkinsJob.commits().remove( jenkinsJob.commits().get( 2 ) );
      jenkinsJob.supplements().commitsSinceLastFailure().removeAll( 
               jenkinsJob.commits().get( 1 ), 
               jenkinsJob.commits().get( 2 ) 
      );
      jenkinsJob.supplements().commitsSinceLastFailure().remove( jenkinsJob.commits().get( 2 ) );
      
      assertThat( systemUnderTest.sinceLastFailureLabel().getText(), is( "Since Last Failure: Jesus, Negan." ) );
      assertThat( systemUnderTest.newCommittersLabel().getText(), is( "New Committers: Rick, Daryl, Carl." ) );
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
   
   @Test public void shouldUseDecoupledPlatformToSetNewCommitterText(){
      DecoupledPlatformImpl.setInstance( runnable -> {} );
    
      jenkinsJob.commits().add( commitForWalker );
      assertThat( systemUnderTest.newCommittersLabel().getText(), is( "New Committers: Rick, Daryl, Carl." ) );
      
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      jenkinsJob.commits().add( commitForWalker );
      assertThat( systemUnderTest.newCommittersLabel().getText(), is( "New Committers: Rick, Daryl, Carl, Walker." ) );
   }//End Method
   
   @Test public void shouldUseDecoupledPlatformToSetSinceFailureText(){
      DecoupledPlatformImpl.setInstance( runnable -> {} );
    
      jenkinsJob.supplements().commitsSinceLastFailure().add( commitForWalker );
      assertThat( systemUnderTest.sinceLastFailureLabel().getText(), is( "Since Last Failure: Jesus, Negan." ) );
      
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      jenkinsJob.supplements().commitsSinceLastFailure().add( commitForWalker );
      assertThat( systemUnderTest.sinceLastFailureLabel().getText(), is( "Since Last Failure: Jesus, Negan, Walker." ) );
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
   
   @Test public void shouldShowPassingMessageForSinceFailureWhenPassing(){
      jenkinsJob.setBuildStatus( BuildResultStatus.SUCCESS );
      assertThat( systemUnderTest.sinceLastFailureLabel().getText(), is( FailureDetail.SINCE_LAST_FAILURE_PREFIX + FailureDetail.PASSING ) );
   }//End Method
   
   /**
    * Convenience method for making a {@link Commit} for a {@link JenkinsUser}.
    * @param user the {@link JenkinsUser}.
    * @return the {@link Commit}.
    */
   private Commit makeCommitFor( JenkinsUser user ) {
      return new Commit( "anything", 0L, user, "anything", "anything", new ArrayList<>() );
   }//End Method

}//End Class
