/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.sides.users;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import javafx.scene.control.Label;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;

/**
 * {@link AssignedTreeItem} test.
 */
public class AssignedTreeItemTest {
   
   private static final long TIMESTAMP = 3487527090L;
   private static final String DESCRIPTION = "some description";
   private static final String DETAIL = "some detail";
   private JenkinsUser user;
   
   private UserAssignment assignment;
   private AssignedTreeItem systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      user = new JenkinsUserImpl( "Dan" );
      assignment = new UserAssignment( user, TIMESTAMP, DESCRIPTION, DETAIL );
      systemUnderTest = new AssignedTreeItem( assignment );
   }//End Method

   @Test public void shouldBeAssociatedWithAssignment() {
      assertThat( systemUnderTest.isAssociatedWithUserAssignment( assignment ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWithUserAssignment( mock( UserAssignment.class ) ), is( false ) );
   }//End Method
   
   @Test public void shouldProvideJenkinsUser() {
      assertThat( systemUnderTest.getJenkinsUser(), is( user ) );
   }//End Method
   
   @Test public void shouldProvideLabelWithTimestamp() {
      Label label = ( Label ) systemUnderTest.firstColumnProperty().get();
      assertThat( label.getText(), is( AssignedTreeItem.formatTimestamp( TIMESTAMP ) ) );
   }//End Method
   
   @Test public void shouldProvideLabelWithWrappedDescription() {
      Label label = ( Label ) systemUnderTest.secondColumnProperty().get();
      assertThat( label.getText(), is( DESCRIPTION ) );
      assertThat( label.isWrapText(), is( true ) );
   }//End Method
   
   @Test public void shouldProvideLabelWithWrappedDetail() {
      Label label = ( Label ) systemUnderTest.detailProperty().get();
      assertThat( label.getText(), is( DETAIL ) );
      assertThat( label.isWrapText(), is( true ) );
   }//End Method
   
   @Test public void shouldUpdateTimestampIfAssignmentChanges(){
      long timestamp = 124723;
      assignment.timestampProperty().set( timestamp );
      
      Label label = ( Label ) systemUnderTest.firstColumnProperty().get();
      assertThat( label.getText(), is( AssignedTreeItem.formatTimestamp( timestamp ) ) );
   }//End Method
   
   @Test public void shouldUpdateDescriptionIfAssignmentChanges(){
      String value = "something else";
      assignment.descriptionProperty().set( value );
      
      Label label = ( Label ) systemUnderTest.secondColumnProperty().get();
      assertThat( label.getText(), is( value ) );
   }//End Method
   
   @Test public void shouldUpdateDetailIfAssignmentChanges(){
      String value = "something else";
      assignment.detailProperty().set( value );
      
      Label label = ( Label ) systemUnderTest.detailProperty().get();
      assertThat( label.getText(), is( value ) );
   }//End Method
   
   @Test public void shouldNotUpdateTimestampIfDetached(){
      systemUnderTest.detachFromSystem();
      
      long timestamp = 124723;
      assignment.timestampProperty().set( timestamp );
      
      Label label = ( Label ) systemUnderTest.firstColumnProperty().get();
      assertThat( label.getText(), is( AssignedTreeItem.formatTimestamp( TIMESTAMP ) ) );
   }//End Method
   
   @Test public void shouldNotUpdateDescriptionIfDetached(){
      systemUnderTest.detachFromSystem();
      
      String value = "something else";
      assignment.descriptionProperty().set( value );
      
      Label label = ( Label ) systemUnderTest.secondColumnProperty().get();
      assertThat( label.getText(), is( DESCRIPTION ) );
   }//End Method
   
   @Test public void shouldNotUpdateDetailIfDetached(){
      systemUnderTest.detachFromSystem();
      
      String value = "something else";
      assignment.detailProperty().set( value );
      
      Label label = ( Label ) systemUnderTest.detailProperty().get();
      assertThat( label.getText(), is( DETAIL ) );
   }//End Method
   
   @Test public void shouldFormatTimestamp(){
      assertThat( AssignedTreeItem.formatTimestamp( 1000 ), is( "1970-01-01 at 00:16:40" ) );
   }//End Method
   
   @Test public void shouldBeDetachedWhenNoRegistrationsHeld(){
      assertThat( systemUnderTest.isDetachedFromSystem(), is( false ) );
      systemUnderTest.detachFromSystem();
      assertThat( systemUnderTest.isDetachedFromSystem(), is( true ) );
   }//End Method

}//End Class
