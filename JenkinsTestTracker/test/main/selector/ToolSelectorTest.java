/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package main.selector;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sun.javafx.application.PlatformImpl;

import friendly.controlsfx.FriendlyAlert;
import graphics.JavaFxInitializer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;

/**
 * {@link ToolSelector} test.
 */
public class ToolSelectorTest {

   private ObservableList< ButtonType > buttonTypes;
   @Mock private FriendlyAlert alert;
   @Captor private ArgumentCaptor< GridPane > contentCaptor;
   private ToolSelector systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      buttonTypes = FXCollections.observableArrayList();
      Mockito.when( alert.friendly_getButtonTypes() ).thenReturn( buttonTypes );
      
      JavaFxInitializer.runAndWait( () -> {
         systemUnderTest = new ToolSelector();
         systemUnderTest.configureAlert( alert );
      } );
   }//End Method
   
   @Ignore
   @Test public void manualInspection() {
      PlatformImpl.startup( () -> {} );
      PlatformImpl.runLater( () -> {
         FriendlyAlert alert = new FriendlyAlert( AlertType.INFORMATION );
         new ToolSelector().configureAlert( alert );
         alert.showAndWait();
      } );
   }//End Method
   
   @Test public void shouldProvideLaunchButton(){
      assertThat( systemUnderTest.launchButton(), notNullValue() );
   }//End Method
   
   @Test public void shouldHaveLaunchButton(){
      assertThat( buttonTypes.isEmpty(), is( false ) );
      assertThat( buttonTypes, contains( systemUnderTest.launchButton() ) );
      assertThat( buttonTypes.size(), is( 1 ) );
   }//End Method
   
   @Test public void shouldHaveAlertTitle(){
      Mockito.verify( alert ).friendly_setTitle( ToolSelector.TITLE );
   }//End Method
   
   @Test public void shouldHaveHeaderText(){
      Mockito.verify( alert ).friendly_setHeaderText( ToolSelector.HEADER_TEXT );      
   }//End Method
   
   @Test public void shouldHaveNoModality(){
      Mockito.verify( alert ).friendly_initModality( Modality.NONE );
   }//End Method
   
   @Test public void shouldHaveContentAndProvideChoices(){
      Mockito.verify( alert ).friendly_dialogSetContent( contentCaptor.capture() );
      assertThat( contentCaptor.getValue(), notNullValue() );
      assertThat( contentCaptor.getValue(), isA( GridPane.class ) );
      
      GridPane content = contentCaptor.getValue();
      assertThat( content.getChildren(), contains( systemUnderTest.toolsChoices() ) );
   }//End Method
   
   @Test public void shouldDefaultChoice(){
      assertThat( systemUnderTest.toolsChoices().getSelectionModel().getSelectedItem(), is( Tools.BuildWall ) );
   }//End Method
   
   @Test public void shouldExpandContentToWidth(){
      assertThat( GridPane.getHgrow( systemUnderTest.toolsChoices() ), is( Priority.ALWAYS ) );
      assertThat( GridPane.getHgrow( systemUnderTest.content() ), is( Priority.ALWAYS ) );
   }//End Method
   
   @Test public void shouldProvideSelectedItem(){
      assertThat( systemUnderTest.getSelectedTool(), is( systemUnderTest.toolsChoices().getSelectionModel().getSelectedItem() ) );
      systemUnderTest.toolsChoices().getSelectionModel().select( Tools.TestTable );
      assertThat( systemUnderTest.getSelectedTool(), is( systemUnderTest.toolsChoices().getSelectionModel().getSelectedItem() ) );
   }//End Method

}//End Class
