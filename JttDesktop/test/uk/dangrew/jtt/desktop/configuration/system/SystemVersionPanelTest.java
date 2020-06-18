/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.configuration.system;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import uk.dangrew.jtt.desktop.friendly.javafx.FriendlyDesktop;
import uk.dangrew.kode.launch.TestApplication;

/**
 * {@link SystemVersionPanel} test.
 */
public class SystemVersionPanelTest {

   @Mock private CheckForUpdates updates;
   @Mock private FriendlyDesktop desktop;
   private SystemVersionPanel systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new SystemVersionPanel( updates, desktop );
   }//End Method

   @Test public void labelsShouldWrapText(){
      assertThat( systemUnderTest.firstSentence().isWrapText(), is( true ) );
      assertThat( systemUnderTest.secondParagraph().isWrapText(), is( true ) );
      assertThat( systemUnderTest.thirdParagraph().isWrapText(), is( true ) );
   }//End Method 
   
   @Test public void shouldContainAllElements() {
      assertThat( systemUnderTest.getChildren(), contains( 
               systemUnderTest.firstSentence(), 
               systemUnderTest.firstGap(), 
               systemUnderTest.secondParagraph(),
               systemUnderTest.secondGap(),
               systemUnderTest.checkForUpdatesButton(),
               systemUnderTest.thirdGap(),
               systemUnderTest.thirdParagraph(),
               systemUnderTest.iconCreditLink()
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
      assertThat( GridPane.getColumnIndex( systemUnderTest.thirdGap() ), is( 0 ) );
      assertThat( GridPane.getColumnIndex( systemUnderTest.thirdParagraph() ), is( 0 ) );
      assertThat( GridPane.getColumnIndex( systemUnderTest.iconCreditLink() ), is( 0 ) );
   }//End Method
   
   @Test public void shouldCheckForUpdatesOnButtonPress(){
      systemUnderTest.checkForUpdatesButton().getOnAction().handle( new ActionEvent() );
      verify( updates ).checkForUpdates();
   }//End Method
   
   @Test public void shouldBrowseToIconCreditLink() throws IOException, URISyntaxException{
      Desktop actualDesktop = mock( Desktop.class );
      when( desktop.getDesktop() ).thenReturn( actualDesktop );
      systemUnderTest.iconCreditLink().getOnAction().handle( new ActionEvent() );
      
      verify( desktop ).getDesktop();
      verify( actualDesktop ).browse( new URI( SystemVersionPanel.ICON_CREDIT ) );
   }//End Method
   
   @Test public void shouldDisplayCorrectLink(){
      assertThat( systemUnderTest.iconCreditLink().getText(), is( SystemVersionPanel.ICON_CREDIT ) );
   }//End Method

}//End Class
