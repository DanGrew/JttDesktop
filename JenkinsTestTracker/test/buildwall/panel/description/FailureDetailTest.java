/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.panel.description;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.sun.javafx.application.PlatformImpl;

import buildwall.configuration.BuildWallConfiguration;
import buildwall.configuration.BuildWallConfigurationImpl;
import graphics.DecoupledPlatformImpl;
import graphics.JavaFxInitializer;
import graphics.TestPlatformDecouplerImpl;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;
import model.users.JenkinsUser;
import model.users.JenkinsUserImpl;

/**
 * {@link FailureDetail} test.
 */
public class FailureDetailTest {
   
   private BuildWallConfiguration configuration;
   private JenkinsUser rick;
   private JenkinsUser daryl;
   private JenkinsUser carl;
   private JenkinsJob jenkinsJob;
   private FailureDetail systemUnderTest;
   
   @BeforeClass public static void initialisePlatform(){
      JavaFxInitializer.startPlatform();
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      jenkinsJob = new JenkinsJobImpl( "Some Job" );
      
      rick = new JenkinsUserImpl( "Rick" );
      daryl = new JenkinsUserImpl( "Daryl" );
      carl = new JenkinsUserImpl( "Carl" );

      jenkinsJob.culprits().addAll( rick, daryl, carl );
      
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
      } );
      
      Thread.sleep( 2000 );
      PlatformImpl.runLater( () -> {
         jenkinsJob.culprits().addAll( new JenkinsUserImpl( "Maggie" ), new JenkinsUserImpl( "Carol" ) );
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
      assertThat( systemUnderTest.culpritsLabel().getText(), is( "No Suspects." ) );
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
      assertThat( systemUnderTest.getRowConstraints(), hasSize( 1 ) );
      RowConstraints constraint = systemUnderTest.getRowConstraints().get( 0 );
      assertThat( constraint.getMaxHeight(), is( Double.MAX_VALUE ) );
      assertThat( constraint.getPercentHeight(), is( 100.0 ) );
   }//End Method
   
   @Test public void shouldWrapTextToFillRow(){
      assertThat( systemUnderTest.culpritsLabel().isWrapText(), is( true ) );
   }//End Method
   
   @Test public void shouldUseDefaultConfigurationOnText(){
      assertThat( systemUnderTest.culpritsLabel().getFont(), is( configuration.culpritsFont().get() ) );
      assertThat( systemUnderTest.culpritsLabel().getTextFill(), is( configuration.culpritsColour().get() ) );
   }//End Method
   
   @Test public void shouldUpdateFontFromConfiguration(){
      assertThat( systemUnderTest.culpritsLabel().getFont(), is( configuration.culpritsFont().get() ) );
      
      Font alternateFont = Font.font( 100 );
      configuration.culpritsFont().set( alternateFont );
      
      assertThat( systemUnderTest.culpritsLabel().getFont(), is( alternateFont ) );
   }//End Method
   
   @Test public void shouldUpdateColourFromConfiguration(){
      assertThat( systemUnderTest.culpritsLabel().getTextFill(), is( configuration.culpritsColour().get() ) );
      
      Color alternateColour = Color.AQUAMARINE;
      configuration.culpritsColour().set( alternateColour );
      
      assertThat( systemUnderTest.culpritsLabel().getTextFill(), is( alternateColour ) );
   }//End Method
   
   @Test public void shouldDetachFontListenersFromSystem(){
      systemUnderTest.detachFromSystem();
      
      Font original = configuration.culpritsFont().get();
      assertThat( systemUnderTest.culpritsLabel().getFont(), is( configuration.culpritsFont().get() ) );
      
      Font alternateFont = Font.font( 100 );
      configuration.culpritsFont().set( alternateFont );
      
      assertThat( systemUnderTest.culpritsLabel().getFont(), is( original ) );
   }//End Method
   
   @Test public void shouldDetachColorListenersFromSystem(){
      systemUnderTest.detachFromSystem();
      
      Color orignal = configuration.culpritsColour().get();
      assertThat( systemUnderTest.culpritsLabel().getTextFill(), is( configuration.culpritsColour().get() ) );
      
      Color alternateColour = Color.AQUAMARINE;
      configuration.culpritsColour().set( alternateColour );
      
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
   
   @Test public void shouldUseDecoupledPlatformToSetText(){
      DecoupledPlatformImpl.setInstance( runnable -> {} );
    
      jenkinsJob.culprits().add( new JenkinsUserImpl( "Walker" ) );
      assertThat( systemUnderTest.culpritsLabel().getText(), is( "Suspects: Rick, Daryl, Carl." ) );
      
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      jenkinsJob.culprits().add( new JenkinsUserImpl( "Walker" ) );
      assertThat( systemUnderTest.culpritsLabel().getText(), is( "Suspects: Rick, Daryl, Carl, Walker, Walker." ) );
   }//End Method

}//End Class
