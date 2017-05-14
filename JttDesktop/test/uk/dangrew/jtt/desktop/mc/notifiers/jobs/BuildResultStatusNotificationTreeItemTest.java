/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.notifiers.jobs;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import uk.dangrew.jtt.desktop.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.mc.resources.ManagementConsoleImages;
import uk.dangrew.jtt.desktop.mc.view.ManagementConsoleStyle;
import uk.dangrew.jtt.desktop.mc.view.tree.NotificationTreeController;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.sd.graphics.launch.TestApplication;

/**
 * {@link BuildResultStatusNotificationTreeItem} test.
 */
public class BuildResultStatusNotificationTreeItemTest {

   private static final Image PEOPLE_IMAGE = mock( Image.class );
   private static final Image CLOSE_IMAGE = mock( Image.class );
   
   @Mock private NotificationTreeController controller;
   @Mock private ChangeIdentifier changeIdentifier;
   @Spy private JavaFxStyle javaFxStyling;
   @Spy private ManagementConsoleStyle mcStyling;
   @Mock private ManagementConsoleImages images;
   private BuildResultStatusNotification notification;
   private BuildResultStatusNotificationTreeItem systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      notification = new BuildResultStatusNotification( 
               new JenkinsJobImpl( "This is my name" ), BuildResultStatus.UNKNOWN, BuildResultStatus.FAILURE 
      );
      
      when( changeIdentifier.identifyChangeType( 
               notification.getPreviousBuildResultStatus(), notification.getNewBuildResultStatus() 
      ) ).thenReturn( BuildResultStatusHighLevelChange.Passed );
      when( images.constuctPeopleImage() ).thenReturn( PEOPLE_IMAGE );
      when( images.constuctCloseImage() ).thenReturn( CLOSE_IMAGE );
      
      systemUnderTest = new BuildResultStatusNotificationTreeItem( 
               notification, controller, changeIdentifier, javaFxStyling, mcStyling, images 
      );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shoudlNotAcceptNullNotification() {
      new BuildResultStatusNotificationTreeItem( null, controller );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shoudlNotAcceptNullController() {
      new BuildResultStatusNotificationTreeItem( notification, null );
   }//End Method
   
   @Test public void shouldUseColumnConstraints(){
      Node content = systemUnderTest.contentProperty().get();
      assertThat( content, is( instanceOf( GridPane.class ) ) );
      
      GridPane pane = ( GridPane ) content;
      ObservableList< ColumnConstraints > constraints = pane.getColumnConstraints();
      
      assertThat( constraints.get( 0 ).getPercentWidth(), is( BuildResultStatusNotificationTreeItem.STATUS_PROPORTION ) );
      assertThat( constraints.get( 1 ).getPercentWidth(), is( BuildResultStatusNotificationTreeItem.TITLE_PROPORTION ) );
      assertThat( constraints.get( 2 ).getPercentWidth(), is( BuildResultStatusNotificationTreeItem.DESCRIPTION_PROPORTION ) );
      assertThat( constraints.get( 3 ).getPercentWidth(), is( BuildResultStatusNotificationTreeItem.PEOPLE_PROPORTION ) );
      assertThat( constraints.get( 4 ).getPercentWidth(), is( BuildResultStatusNotificationTreeItem.CLOSE_PROPORTION ) );
   }//End Method
   
   @Test public void shouldConainAllItems(){
      Node content = systemUnderTest.contentProperty().get();
      GridPane pane = ( GridPane ) content;
      
      assertThat( pane.getChildren(), contains( 
               systemUnderTest.status(), 
               systemUnderTest.title(),
               systemUnderTest.description(),
               systemUnderTest.people(),
               systemUnderTest.close()
      ) );
   }//End Method
   
   @Test public void shouldProvideStatus(){
      ImageView view = ( ImageView ) systemUnderTest.status();
      assertThat( view.getImage(), is( BuildResultStatusHighLevelChange.Passed.constructImage().getImage() ) );
      assertThat( view.getFitWidth(), is( BuildResultStatusNotificationTreeItem.PREFERRED_IMAGE_WIDTH ) );
      assertThat( view.getFitHeight(), is( BuildResultStatusNotificationTreeItem.PREFERRED_IMAGE_HEIGHT ) );
   }//End Method
      
   @Test public void shouldProvideTitle(){
      BorderPane wrapper = ( BorderPane ) systemUnderTest.title();
      assertThat( wrapper.getCenter(), is( instanceOf( Label.class ) ) );
      
      Label label = ( Label ) wrapper.getCenter();
      assertThat( label.getText(), is( notification.getJenkinsJob().nameProperty().get() ) );
   }//End Method
   
   @Test public void shouldProvideDescription(){
      BorderPane wrapper = ( BorderPane ) systemUnderTest.description();
      assertThat( wrapper.getCenter(), is( instanceOf( Label.class ) ) );
      
      String description = notification.getDescription();
      verify( javaFxStyling ).createWrappedTextLabel( description );
      
      Label label = ( Label ) wrapper.getCenter();
      assertThat( label.getText(), is( description ) );
      assertThat( label.getPrefHeight(), is( BuildResultStatusNotificationTreeItem.PREFERRED_ROW_HEIGHT ) );
   }//End Method
   
   @Test public void shouldProvidePeople(){
      BorderPane wrapper = ( BorderPane ) systemUnderTest.people();
      assertThat( wrapper.getCenter(), is( instanceOf( Button.class ) ) );
      
      Button button = ( Button ) wrapper.getCenter();
      assertButtonIsConfiguredCorrectly( button, PEOPLE_IMAGE );
   }//End Method
   
   @Test public void shouldProvideClose(){
      BorderPane wrapper = ( BorderPane ) systemUnderTest.close();
      assertThat( wrapper.getCenter(), is( instanceOf( Button.class ) ) );
      
      Button button = ( Button ) wrapper.getCenter();
      assertButtonIsConfiguredCorrectly( button, CLOSE_IMAGE );
   }//End Method
   
   private void assertButtonIsConfiguredCorrectly( Button button, Image image ){
      assertThat( button.getGraphic(), is( instanceOf( ImageView.class ) ) );
      
      ImageView view = ( ImageView ) button.getGraphic();
      assertThat( view.getImage(), is( image ) );
      assertThat( view.getFitWidth(), is( BuildResultStatusNotificationTreeItem.PREFERRED_IMAGE_WIDTH ) );
      assertThat( view.getFitHeight(), is( BuildResultStatusNotificationTreeItem.PREFERRED_IMAGE_HEIGHT ) );
      
      assertThat( button.getPrefWidth(), is( BuildResultStatusNotificationTreeItem.PREFERRED_ROW_HEIGHT ) );
      assertThat( button.getPrefHeight(), is( BuildResultStatusNotificationTreeItem.PREFERRED_ROW_HEIGHT ) );
      assertThat( button.getAlignment(), is( Pos.CENTER ) );
      
      verify( javaFxStyling ).removeBackgroundAndColourOnClick( button, Color.GRAY );
      verify( mcStyling ).styleButtonSize( button, image );
   }//End Method
   
   @Test public void closeButtonShouldRemoveNotificationWhenClicked(){
      BorderPane wrapper = ( BorderPane ) systemUnderTest.close();
      Button button = ( Button ) wrapper.getCenter();
      button.getOnAction().handle( new ActionEvent() );
      
      verify( controller ).remove( systemUnderTest );
   }//End Method
   
   @Test public void shouldHaveControllerAssociated(){
      assertThat( systemUnderTest.hasController( controller ), is( true ) );
      assertThat( systemUnderTest.hasController( mock( NotificationTreeController.class ) ), is( false ) );
   }//End Method

}//End Class
