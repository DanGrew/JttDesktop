/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.tree.item;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import uk.dangrew.jtt.buildwall.configuration.components.JobPolicyPanel;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfigurationImpl;
import uk.dangrew.jtt.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.environment.preferences.PreferenceController;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.PlatformDecouplerImpl;

/**
 * {@link JobPolicyTreeItem} test.
 */
public class JobPolicyTreeItemTest {

   private BuildWallConfiguration configuration;
   @Mock private PreferenceController controller;
   @Captor private ArgumentCaptor< Node > contentTitleCaptor;
   @Captor private ArgumentCaptor< Node > contentCaptor;
   
   private JobPolicyTreeItem systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      MockitoAnnotations.initMocks( this );
      configuration = new BuildWallConfigurationImpl();
      systemUnderTest = new JobPolicyTreeItem( controller, configuration );
   }//End Method
   
   @Test public void shouldHandleClickByInstructingController() {
      systemUnderTest.handleBeingSelected();
      verify( controller ).displayContent( contentTitleCaptor.capture(), contentCaptor.capture() );
      
      assertThat( contentTitleCaptor.getValue(), is( instanceOf( SimpleConfigurationTitle.class ) ) );
      SimpleConfigurationTitle title = ( SimpleConfigurationTitle ) contentTitleCaptor.getValue();
      assertThat( title.getTitle(), is( JobPolicyTreeItem.TITLE ) );
      assertThat( title.getDescription(), is( JobPolicyTreeItem.DESCRIPTION ) );
      
      assertThat( contentCaptor.getValue(), is( instanceOf( ScrollPane.class ) ) );
      ScrollPane scroller = ( ScrollPane ) contentCaptor.getValue();
      assertThat( scroller.getContent(), is( instanceOf( JobPolicyPanel.class ) ) );
      
      JobPolicyPanel panel = ( JobPolicyPanel ) scroller.getContent();
      assertThat( panel.hasConfiguration( configuration ), is( true ) );
   }//End Method
   
   @Test public void shouldProvideToStringUsingName(){
      assertThat( systemUnderTest.toString(), is( JobPolicyTreeItem.NAME ) );
   }//End Method
   
   @Test public void shouldBeAssociatedWithConfiguration(){
      assertThat( systemUnderTest.isAssociatedWith( configuration ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( new Object() ), is( false ) );
   }//End Method

}//End Class
