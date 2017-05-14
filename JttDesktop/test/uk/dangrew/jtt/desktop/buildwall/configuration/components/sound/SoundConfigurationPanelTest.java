/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components.sound;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.scene.control.TitledPane;
import javafx.scene.layout.Region;
import junitparams.JUnitParamsRunner;
import uk.dangrew.jtt.connection.api.handling.live.BuildResultStatusChange;
import uk.dangrew.jtt.desktop.buildwall.configuration.components.sound.applier.FailFailApplier;
import uk.dangrew.jtt.desktop.buildwall.configuration.components.sound.applier.FailPassApplier;
import uk.dangrew.jtt.desktop.buildwall.configuration.components.sound.applier.IndividualChangeApplier;
import uk.dangrew.jtt.desktop.buildwall.configuration.components.sound.applier.PassFailApplier;
import uk.dangrew.jtt.desktop.buildwall.configuration.components.sound.applier.PassPassApplier;
import uk.dangrew.jtt.desktop.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.buildwall.effects.sound.SoundConfiguration;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.sd.graphics.launch.TestApplication;

@RunWith( JUnitParamsRunner.class )
public class SoundConfigurationPanelTest {

   private static final String FILENAME = SoundConfigurationPanelTest.class.getResource( "valid-media-file.m4a" ).getPath();
   
   @Spy private JavaFxStyle stying;
   private SoundConfiguration configuration;
   private SoundConfigurationPanel systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      configuration = new SoundConfiguration();
      systemUnderTest = new SoundConfigurationPanel( stying, configuration );
   }//End Method

   @Ignore
   @Test public void manual() throws InterruptedException {
      TestApplication.launch( () -> systemUnderTest );
      
      Thread.sleep( 10000000 );
   }//End Method
   
   @Test public void shouldUpdateSelectedFileInRow(){
      BuildResultStatusChange change = new BuildResultStatusChange( BuildResultStatus.FAILURE, BuildResultStatus.NOT_BUILT );
      configuration.statusChangeSounds().put( change, FILENAME );
      assertThat( systemUnderTest.rowFor( change.getPreviousStatus(), change.getCurrentStatus() ).getSelectedFile(), is( FILENAME ) );
   }//End Method

   @Test public void shouldClearUpdateSelectedFileInRow(){
      BuildResultStatusChange change = new BuildResultStatusChange( BuildResultStatus.FAILURE, BuildResultStatus.NOT_BUILT );
      configuration.statusChangeSounds().put( change, FILENAME );
      assertThat( systemUnderTest.rowFor( change.getPreviousStatus(), change.getCurrentStatus() ).getSelectedFile(), is( FILENAME ) );
      configuration.statusChangeSounds().put( change, null );
      assertThat( systemUnderTest.rowFor( change.getPreviousStatus(), change.getCurrentStatus() ).getSelectedFile(), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldUsePadding(){
      assertThat( systemUnderTest.getPadding().getBottom(), is( SoundConfigurationPanel.PADDING ) );
      assertThat( systemUnderTest.getPadding().getTop(), is( SoundConfigurationPanel.PADDING ) );
      assertThat( systemUnderTest.getPadding().getRight(), is( SoundConfigurationPanel.PADDING ) );
      assertThat( systemUnderTest.getPadding().getLeft(), is( SoundConfigurationPanel.PADDING ) );
   }//End Method

   @Test public void shouldConfigureColumnConstraints(){
      verify( stying ).configureFullWidthConstraints( systemUnderTest );
   }//End Method
   
   @Test public void shouldContainSimpleElements(){
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.simple() ), is( true ) );
      TitledPane pane = systemUnderTest.simple();
      assertThat( pane.getText(), is( SoundConfigurationPanel.SIMPLE_TEXT ) );
      
      Region region = ( Region ) pane.getContent();
      assertThat( region.getChildrenUnmodifiable().contains( systemUnderTest.passFailRow() ), is( true ) );
      assertThat( region.getChildrenUnmodifiable().contains( systemUnderTest.failFailRow() ), is( true ) );
      assertThat( region.getChildrenUnmodifiable().contains( systemUnderTest.failPassRow() ), is( true ) );
      assertThat( region.getChildrenUnmodifiable().contains( systemUnderTest.passPassRow() ), is( true ) );
      
      assertThat( systemUnderTest.passFailRow().getLabelText(), is( SoundConfigurationPanel.PASS_FAIL_TEXT ) );
      assertThat( systemUnderTest.passPassRow().getLabelText(), is( SoundConfigurationPanel.PASS_PASS_TEXT ) );
      assertThat( systemUnderTest.failFailRow().getLabelText(), is( SoundConfigurationPanel.FAIL_FAIL_TEXT ) );
      assertThat( systemUnderTest.failPassRow().getLabelText(), is( SoundConfigurationPanel.FAIL_PASS_TEXT ) );
      
      assertThat( systemUnderTest.passFailRow().isAssociatedWith( configuration ), is( true ) );
      assertThat( systemUnderTest.passPassRow().isAssociatedWith( configuration ), is( true ) );
      assertThat( systemUnderTest.failFailRow().isAssociatedWith( configuration ), is( true ) );
      assertThat( systemUnderTest.failPassRow().isAssociatedWith( configuration ), is( true ) );
      
      assertThat( systemUnderTest.passFailRow().isAssociatedWithType( PassFailApplier.class ), is( true ) );
      assertThat( systemUnderTest.passPassRow().isAssociatedWithType( PassPassApplier.class ), is( true ) );
      assertThat( systemUnderTest.failFailRow().isAssociatedWithType( FailFailApplier.class ), is( true ) );
      assertThat( systemUnderTest.failPassRow().isAssociatedWithType( FailPassApplier.class ), is( true ) );
   }//End Method
   
   @Test public void shouldContainAdvancedElements(){
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.advanced() ), is( true ) );
      TitledPane pane = systemUnderTest.advanced();
      assertThat( pane.getText(), is( SoundConfigurationPanel.ADVANCED_TEXT ) );
      
      Region region = ( Region ) pane.getContent();
      for ( BuildResultStatus from : BuildResultStatus.values() ) {
         for ( BuildResultStatus to : BuildResultStatus.values() ) {
            SoundConfigurationRow row = systemUnderTest.rowFor( from, to );
            assertThat( region.getChildrenUnmodifiable().contains( row ), is( true ) );
            assertThat( row.getLabelText(), is( from.name() + " -> " + to.name() ) );
            assertThat( row.isAssociatedWith( configuration ), is( true ) );
            assertThat( row.isAssociatedWithType( IndividualChangeApplier.class ), is( true ) );
         }
      }
   }//End Method
   
   @Test public void shouldBeAssociatedWithConfiguration(){
      assertThat( systemUnderTest.isAssociatedWith( configuration ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( new SoundConfiguration() ), is( false ) );
   }//End Method
   
   @Test public void shouldInitiallyUseSelectedFilesFromConfiguration(){
      configuration.statusChangeSounds().put( 
               new BuildResultStatusChange( BuildResultStatus.ABORTED, BuildResultStatus.FAILURE ), FILENAME 
      );
      configuration.statusChangeSounds().put( 
               new BuildResultStatusChange( BuildResultStatus.NOT_BUILT, BuildResultStatus.UNSTABLE ), FILENAME 
      );
      systemUnderTest = new SoundConfigurationPanel( stying, configuration );
      
      assertThat( systemUnderTest.rowFor( BuildResultStatus.ABORTED, BuildResultStatus.FAILURE ).getSelectedFile(), is( FILENAME ) );
      assertThat( systemUnderTest.rowFor( BuildResultStatus.NOT_BUILT, BuildResultStatus.UNSTABLE ).getSelectedFile(), is( FILENAME ) );
   }//End Method
   
}//End Class
