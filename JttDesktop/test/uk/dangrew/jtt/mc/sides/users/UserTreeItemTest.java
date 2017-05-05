/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.sides.users;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.scene.control.Label;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;

/**
 * {@link UserTreeItem} test.
 */
public class UserTreeItemTest {
   
   @Spy private JavaFxStyle styling;
   private JenkinsUser user;
   private UserTreeItem systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      user = new JenkinsUserImpl( "User" );
      systemUnderTest = new UserTreeItem( user, styling );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullUser(){
      new UserTreeItem( null );
   }//End Method

   @Test public void shouldProvideUserGiven() {
      assertThat( systemUnderTest.getJenkinsUser(), is( user ) );
   }//End Method
   
   @Test public void shouldNotProvideOtherColumns(){
      assertThat( systemUnderTest.secondColumnProperty(), is( nullValue() ) );
      assertThat( systemUnderTest.detailProperty(), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldProvideUsersNameInLabel(){
      verify( styling ).createBoldLabel( user.nameProperty().get() );
      
      assertThat( systemUnderTest.firstColumnProperty().get(), is( instanceOf( Label.class ) ) );
      Label label = ( Label ) systemUnderTest.firstColumnProperty().get();
      assertThat( label.getText(), is( user.nameProperty().get() ) );
   }//End Method
   
   @Test public void shouldNotBeAssociatedWithAssignment(){
      assertThat( systemUnderTest.isAssociatedWithUserAssignment( mock( UserAssignment.class ) ), is( false ) );
      assertThat( systemUnderTest.getAssignment(), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldBeDetachedFromSystem(){
      assertThat( systemUnderTest.isDetachedFromSystem(), is( true ) );
   }//End Method
}//End Class
