/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.wallbuilder;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import uk.dangrew.jtt.desktop.wallbuilder.ContentAreaSelector.ClickHandler;

public class ContentAreaSelectorTest {
   
   private ContentArea initialContent;
   private ContentArea alternateContent;
   private ObservableList< Node > areas;

   @Mock private MouseEvent event;
   @Spy private ContentAreaColours colours;
   private ContentAreaSelector systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      
      areas = FXCollections.observableArrayList();
      initialContent = spy( new TestContentArea() );
      alternateContent = spy( new TestContentArea() );
      areas.add( initialContent );
      systemUnderTest = new ContentAreaSelector( colours );
      systemUnderTest.setNodes( areas );
   }//End Method

   @Test public void shouldRespondToClickBySelecting() {
      assertThat( systemUnderTest.getSelection(), is( nullValue() ) );
      initialContent.getOnMouseClicked().handle( event );
      assertThat( systemUnderTest.getSelection(), is( initialContent ) );
   }//End Method
   
   @Test public void shouldColourFirstSelection() {
      initialContent.getOnMouseClicked().handle( event );
      verify( colours ).applySelectedColours( initialContent );
   }//End Method
   
   @Test public void shouldColourSecondSelectionAndDiscolourFirst() {
      areas.add( alternateContent );
      initialContent.getOnMouseClicked().handle( event );
      alternateContent.getOnMouseClicked().handle( event );
      verify( colours, times( 2 ) ).applyUnselectedColours( initialContent );
      verify( colours ).applySelectedColours( alternateContent );
   }//End Method
   
   @Test public void shouldAddedAndRemoveSelectionListenerToContent(){
      assertThat( alternateContent.getOnMouseClicked(), is( nullValue() ) );
      areas.add( alternateContent );
      assertThat( alternateContent.getOnMouseClicked(), is( instanceOf( ClickHandler.class ) ) );
      areas.remove( alternateContent );
      assertThat( alternateContent.getOnMouseClicked(), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldDeselectIfClickedAgain(){
      initialContent.getOnMouseClicked().handle( event );
      verify( colours ).applySelectedColours( initialContent );
      initialContent.getOnMouseClicked().handle( event );
      verify( colours, times( 2 ) ).applyUnselectedColours( initialContent );
      assertThat( systemUnderTest.getSelection(), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldIgnoreClicksOnNonContentArea(){
      areas.add( new GridPane() );
   }//End Method
   
   @Test public void shouldBringSelectionToFront(){
      areas.add( alternateContent );
      
      alternateContent.getOnMouseClicked().handle( event );
      verify( alternateContent ).toFront();
      
      initialContent.getOnMouseClicked().handle( event );
      verify( initialContent ).toFront();
   }//End Method
   
   @Test public void shouldColourInitially(){
      verify( colours ).applyUnselectedColours( initialContent );
   }//End Method
   
   @Test public void shouldColourWhenAdded(){
      areas.add( alternateContent );
      verify( colours ).applyUnselectedColours( alternateContent );
   }//End Method

}//End Class
