/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.main;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import uk.dangrew.jtt.buildwall.dual.DualBuildWallDisplayImpl;
import uk.dangrew.jtt.buildwall.layout.BuildWallDisplayImpl;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.PlatformDecouplerImpl;
import uk.dangrew.jtt.main.digest.SystemDigestController;
import uk.dangrew.jtt.main.selector.ToolSelector;
import uk.dangrew.jtt.main.selector.Tools;
import uk.dangrew.jtt.releases.ReleasesWrapper;
import uk.dangrew.jtt.view.table.TestTableView;

/**
 * {@link JttSceneConstructor} test.
 */
public class JttSceneConstructorTest {

   @Captor private ArgumentCaptor< ToolSelector > selectorCaptor;
   
   @Mock JttApplicationController controller;
   @Mock SystemDigestController digestController;
   private JttSceneConstructor systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new JttSceneConstructor( controller, digestController );
   }//End Method
   
   @Test public void shouldReturnIfCantLogin() {
      when( controller.login( Mockito.any() ) ).thenReturn( false );
      assertThat( systemUnderTest.makeScene(), is( nullValue() ) );
      verify( controller, times( 1 ) ).login( Mockito.any() );
      verify( controller, times( 0 ) ).selectTool( Mockito.any() );
   }// End Method

   @Test public void shouldReturnIfNotLaunchedCorrectly() {
      when( controller.login( Mockito.any() ) ).thenReturn( true );
      when( controller.selectTool( Mockito.any() ) ).thenReturn( false );
      assertThat( systemUnderTest.makeScene(), is( nullValue() ) );
      verify( controller, times( 1 ) ).login( Mockito.any() );
      verify( controller, times( 1 ) ).selectTool( Mockito.any() );
   }// End Method

   @Test public void shouldUseBuildWallDisplay() {
      when( controller.login( Mockito.any() ) ).thenReturn( true );
      when( controller.selectTool( Mockito.any() ) ).thenReturn( true );
      when( controller.selectTool( selectorCaptor.capture() ) ).then( invocation -> {
         selectorCaptor.getValue().select( Tools.BuildWall );
         return true;
      } );
      Scene scene = systemUnderTest.makeScene();
      
      verify( controller, times( 1 ) ).login( Mockito.any() );
      verify( controller, times( 1 ) ).selectTool( Mockito.any() );

      assertThat( scene.getRoot(), instanceOf( BuildWallDisplayImpl.class ) );
      assertThat( scene.getAccelerators().isEmpty(), is( false ) );
   }// End Method

   @Test public void shouldUseDualBuildWallDisplay() {
      when( controller.login( Mockito.any() ) ).thenReturn( true );
      when( controller.selectTool( Mockito.any() ) ).thenReturn( true );
      when( controller.selectTool( selectorCaptor.capture() ) ).then( invocation -> {
         selectorCaptor.getValue().select( Tools.DualBuildWall );
         return true;
      } );
      
      Scene scene = systemUnderTest.makeScene();
      verify( controller, times( 1 ) ).login( Mockito.any() );
      verify( controller, times( 1 ) ).selectTool( Mockito.any() );

      assertThat( scene.getRoot(), instanceOf( ReleasesWrapper.class ) );
      BorderPane wrapper = ( BorderPane ) ( ( ReleasesWrapper )scene.getRoot() ).getContent();
      assertThat( wrapper.getCenter(), instanceOf( DualBuildWallDisplayImpl.class ) );
      assertThat( wrapper.getTop(), instanceOf( TitledPane.class ) );
      assertThat( scene.getAccelerators().isEmpty(), is( true ) );
   }// End Method

   @Test public void shouldUseTestTableView() {
      when( controller.login( Mockito.any() ) ).thenReturn( true );
      when( controller.selectTool( Mockito.any() ) ).thenReturn( true );
      when( controller.selectTool( selectorCaptor.capture() ) ).then( invocation -> {
         selectorCaptor.getValue().select( Tools.TestTable );
         return true;
      } );
      Scene scene = systemUnderTest.makeScene();
      verify( controller, times( 1 ) ).login( Mockito.any() );
      verify( controller, times( 1 ) ).selectTool( Mockito.any() );

      assertThat( scene.getRoot(), instanceOf( TestTableView.class ) );
      assertThat( scene.getAccelerators().isEmpty(), is( true ) );
   }// End Method

}//End Class
