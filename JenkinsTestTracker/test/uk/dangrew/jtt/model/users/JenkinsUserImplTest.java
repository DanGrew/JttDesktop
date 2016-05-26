/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.users;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;

/**
 * {@link JenkinsUserImpl} test.
 */
public class JenkinsUserImplTest {

   private static final String TEST_NAME = "any user name";
   private JenkinsUser systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new JenkinsUserImpl( TEST_NAME );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullName(){
      new JenkinsUserImpl( null );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptInvalidName(){
      new JenkinsUserImpl( "    " );
   }//End Method
   
   @Test public void shouldProvideNameProperty() {
      assertThat( systemUnderTest.nameProperty(), notNullValue() );
      assertThat( systemUnderTest.nameProperty().get(), is( TEST_NAME ) );
   }//End Method

}//End Class
