/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.configuration.system;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import uk.dangrew.jtt.graphics.JavaFxInitializer;

/**
 * {@link SystemVersionPanel} test.
 */
public class SystemVersionPanelTest {

   @Mock private CheckForUpdates updates;
   private SystemVersionPanel systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new SystemVersionPanel( updates );
   }//End Method

   @Test public void labelsShouldWrapText(){
      assertThat( systemUnderTest.firstSentence().isWrapText(), is( true ) );
      assertThat( systemUnderTest.secondParagraph().isWrapText(), is( true ) );
   }//End Method 
   
   @Test public void shouldContainAllElements() {
      assertThat( systemUnderTest.getChildren(), contains( 
               systemUnderTest.firstSentence(), 
               systemUnderTest.firstGap(), 
               systemUnderTest.secondParagraph(),
               systemUnderTest.secondGap(),
               systemUnderTest.checkForUpdatesButton()
      ) );
   }//End Method
   
   @Test public void elementsShouldBeInOrder(){
      assertThat( 
               GridPane.getRowIndex( systemUnderTest.firstGap() ), 
               is( greaterThan( GridPane.getRowIndex( systemUnderTest.firstSentence() ) ) ) 
      );
      assertThat( 
               GridPane.getRowIndex( systemUnderTest.secondParagraph() ), 
               is( greaterThan( GridPane.getRowIndex( systemUnderTest.firstGap() ) ) ) 
      );
   }//End Method
   
   @Test public void elementsShouldBeOnTheirOwnRow(){
      assertThat( GridPane.getColumnIndex( systemUnderTest.firstSentence() ), is( 0 ) );
      assertThat( GridPane.getColumnIndex( systemUnderTest.firstGap() ), is( 0 ) );
      assertThat( GridPane.getColumnIndex( systemUnderTest.secondParagraph() ), is( 0 ) );
      assertThat( GridPane.getColumnIndex( systemUnderTest.secondGap() ), is( 0 ) );
      assertThat( GridPane.getColumnIndex( systemUnderTest.checkForUpdatesButton() ), is( 0 ) );
   }//End Method
   
   @Test public void shouldCheckForUpdatesOnButtonPress(){
      systemUnderTest.checkForUpdatesButton().getOnAction().handle( new ActionEvent() );
      verify( updates ).checkForUpdates();
   }//End Method

}//End Class
