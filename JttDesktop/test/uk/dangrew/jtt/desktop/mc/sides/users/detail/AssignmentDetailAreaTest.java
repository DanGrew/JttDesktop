/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.sides.users.detail;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import uk.dangrew.jtt.desktop.mc.resources.ManagementConsoleImages;
import uk.dangrew.jtt.desktop.mc.sides.users.UserAssignment;
import uk.dangrew.jtt.desktop.mc.view.ManagementConsoleStyle;
import uk.dangrew.kode.launch.TestApplication;

/**
 * {@link AssignmentDetailArea} test.
 */
public class AssignmentDetailAreaTest {

   private static final Image IMAGE = mock( Image.class );
   
   private StringProperty assignment1Detail;
   private StringProperty assignment2Detail;
   @Mock private UserAssignment assignment1;
   @Mock private UserAssignment assignment2;
   
   @Spy private ManagementConsoleStyle styling;
   @Mock private ManagementConsoleImages images;
   private AssignmentDetailArea systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      
      when( images.constuctLockImage() ).thenReturn( IMAGE );
      assignment1Detail = new SimpleStringProperty();
      assignment2Detail = new SimpleStringProperty();
      when( assignment1.detailProperty() ).thenReturn( assignment1Detail );
      when( assignment2.detailProperty() ).thenReturn( assignment2Detail );
      
      systemUnderTest = new AssignmentDetailArea( styling, images );
   }//End Method
   
   @Ignore
   @Test public void manual() throws InterruptedException {
      TestApplication.launch( () -> new AssignmentDetailArea( 
               new ManagementConsoleStyle(), new ManagementConsoleImages() ) 
      );
      
      Thread.sleep( 100000 );
   }//End Method
   
   @Test public void shouldContainToolBox(){
      assertThat( systemUnderTest.getTop(), is( systemUnderTest.toolBox() ) );
      assertThat( systemUnderTest.toolBox().getChildren(), contains( systemUnderTest.lockButton() ) );
   }//End Method
   
   @Test public void shouldContainLockButton(){
      assertThat( systemUnderTest.lockButton(), is( instanceOf( ToggleButton.class ) ) );
      assertThat( systemUnderTest.lockButton().getGraphic(), is( instanceOf( ImageView.class ) ) );
      
      ImageView view = ( ImageView )systemUnderTest.lockButton().getGraphic();
      assertThat( view.getImage(), is( IMAGE ) );
      
      assertThat( systemUnderTest.lockButton().getTooltip().getText(), is( AssignmentDetailArea.LOCK_TOOL_TIP ) );
   }//End Method
   
   @Test public void shouldInitiallyLockLockButton(){
      assertThat( systemUnderTest.lockButton().isSelected(), is( true ) );
      assertThat( systemUnderTest.detailArea().isEditable(), is( false ) );
   }//End Method
   
   @Test public void shouldContainTextAreaForDetail(){
      assertThat( systemUnderTest.detailArea(), is( instanceOf( TextArea.class ) ) );
      assertThat( systemUnderTest.getCenter(), is( systemUnderTest.detailArea() ) );
   }//End Method
   
   @Test public void shouldDisplayDetailFromAssignment(){
      final String detail = "some special detail";
      assignment1Detail.set( detail );
      
      systemUnderTest.setAssignment( assignment1 );
      assertThat( systemUnderTest.detailArea().getText(), is( detail ) );
   }//End Method
   
   @Test public void shouldReplaceDetailFromAssignment(){
      final String detail = "some special detail";
      assignment1Detail.set( detail );
      
      final String detail2 = "another special something";
      assignment2Detail.set( detail2 );
      
      systemUnderTest.setAssignment( assignment1 );
      assertThat( systemUnderTest.detailArea().getText(), is( detail ) );
      systemUnderTest.setAssignment( assignment2 );
      assertThat( systemUnderTest.detailArea().getText(), is( detail2 ) );
      
      assignment1Detail.set( "something else" );
      assertThat( systemUnderTest.detailArea().getText(), is( detail2 ) );
   }//End Method
   
   @Test public void shouldUpdateDetailWhenAssignmentChanges(){
      systemUnderTest.lockButton().setSelected( false );
      
      final String detail = "some special detail";
      assignment1Detail.set( detail );
      
      systemUnderTest.setAssignment( assignment1 );
      assertThat( systemUnderTest.detailArea().getText(), is( detail ) );
      
      final String detail2 = "another special something";
      assignment1Detail.set( detail2 );
      assertThat( systemUnderTest.detailArea().getText(), is( detail2 ) );
   }//End Method
   
   @Test public void shouldIndicateWhenNoAssignmentSelected(){
      assertThat( systemUnderTest.detailArea().getText(), is( AssignmentDetailArea.NOT_SELECTED ) );
      
      systemUnderTest.setAssignment( assignment1 );
      assertThat( systemUnderTest.detailArea().getText(), is( not( AssignmentDetailArea.NOT_SELECTED ) ) );
      
      systemUnderTest.setAssignment( null );
      assertThat( systemUnderTest.detailArea().getText(), is( AssignmentDetailArea.NOT_SELECTED ) );
   }//End Method
   
   @Test public void shouldNotAllowDetailEditWhenLocked(){
      systemUnderTest.lockButton().setSelected( false );
      assertThat( systemUnderTest.detailArea().isEditable(), is( true ) );
      
      systemUnderTest.lockButton().setSelected( true );
      assertThat( systemUnderTest.detailArea().isEditable(), is( false ) );
   }//End Method
   
   @Test public void shouldNotUpdateDetailWhenUnlocked(){
      final String detail = "some special detail";
      assignment1Detail.set( detail );
      systemUnderTest.setAssignment( assignment1 );
      systemUnderTest.lockButton().setSelected( false );
      
      final String editedText = "something new and improved";
      assignment1Detail.set( editedText );
      assertThat( systemUnderTest.detailArea().getText(), is( detail ) );
   }//End Method
   
   @Test public void shouldUpdateDetailWhenLocked(){
      final String detail = "some special detail";
      assignment1Detail.set( detail );
      systemUnderTest.setAssignment( assignment1 );
      systemUnderTest.lockButton().setSelected( true );
      
      final String editedText = "something new and improved";
      assignment1Detail.set( editedText );
      assertThat( systemUnderTest.detailArea().getText(), is( editedText ) );
   }//End Method
   
   @Test public void shouldUpdateDetailOnAssignmentWhenLocked(){
      final String detail = "some special detail";
      assignment1Detail.set( detail );
      systemUnderTest.setAssignment( assignment1 );
      systemUnderTest.lockButton().setSelected( false );
      
      final String editedText = "something new and improved";
      systemUnderTest.detailArea().setText( editedText );
      assertThat( assignment1Detail.get(), is( detail ) );
      systemUnderTest.lockButton().setSelected( true );
      assertThat( assignment1Detail.get(), is( editedText ) );
   }//End Method
   
   @Test public void shouldLockAndDiscardWhenAssignmentIsChanged(){
      final String detail = "some special detail";
      assignment1Detail.set( detail );
      systemUnderTest.setAssignment( assignment1 );
      systemUnderTest.lockButton().setSelected( false );
      
      final String editedText = "something new and improved";
      systemUnderTest.detailArea().setText( editedText );
      assertThat( assignment1Detail.get(), is( detail ) );
      
      systemUnderTest.setAssignment( assignment2 );
      assertThat( systemUnderTest.lockButton().isSelected(), is( true ) );
      assertThat( assignment1Detail.get(), is( detail ) );
   }//End Method
   
   @Test public void shouldNotAllowUnlockWhenNoAssignmentAssociated(){
      assertThat( systemUnderTest.lockButton().isDisable(), is( true ) );
      systemUnderTest.setAssignment( assignment1 );
      assertThat( systemUnderTest.lockButton().isDisable(), is( false ) );
      systemUnderTest.setAssignment( null );
      assertThat( systemUnderTest.lockButton().isDisable(), is( true ) );
   }//End Method
}//End Class
