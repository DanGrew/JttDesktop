/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.nodes;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * {@link JenkinsNodeImpl} test.
 */
public class JenkinsNodeImplTest {

   private static final String TEST_NAME = "any node name";
   private JenkinsNode systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new JenkinsNodeImpl( TEST_NAME );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullName(){
      new JenkinsNodeImpl( null );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptInvalidName(){
      new JenkinsNodeImpl( "    " );
   }//End Method
   
   @Test public void shouldProvideNameProperty() {
      assertThat( systemUnderTest.nameProperty(), notNullValue() );
      assertThat( systemUnderTest.nameProperty().get(), is( TEST_NAME ) );
   }//End Method

}//End Class
