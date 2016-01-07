/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
 package model.tests;

import org.junit.Assert;
import org.junit.Test;

import utility.TestCommon;

/**
 * {@link TestClassImpl} test.
 */
public class TestClassImplTest {

   /**
    * Prove that the name property is provided.
    */
   @Test public void shouldProvideNameProperty() {
      final String name = "anything";
      TestClass testClass = new TestClassImpl( name, "anywhere" );
      Assert.assertEquals( name, testClass.nameProperty().get() );
   }//End Method
   
   /**
    * Prove that the location property is provided.
    */
   @Test public void shouldProvideLocationProperty() {
      final String location = "anywhere.here.or.there";
      TestClass testClass = new TestClassImpl( "anything", location );
      Assert.assertEquals( location, testClass.locationProperty().get() );
   }//End Method
   
   /**
    * Prove that the duration property is provided.
    */
   @Test public void shouldProvideDurationProperty() {
      TestClass testClass = new TestClassImpl( "anything", "anywhere" );
      Assert.assertEquals( TestClassImpl.DEFAULT_DURATION, testClass.durationProperty().get(), TestCommon.precision() );
   }//End Method
   
   /**
    * Prove that the {@link TestCase}s are provided.
    */
   @Test public void shouldProvideTestCaseList() {
      TestClass testClass = new TestClassImpl( "anything", "anywhere" );
      Assert.assertTrue( testClass.testCasesList().isEmpty() );
   }//End Method
   
   /**
    * Prove that a null name is rejected.
    */
   @Test( expected = IllegalArgumentException.class ) public void shouldRejectNullName() {
      new TestClassImpl( null, "anywhere" );
   }//End Method
   
   /**
    * Prove that an empty name is rejected.
    */
   @Test( expected = IllegalArgumentException.class ) public void shouldRejectEmptyName() {
      new TestClassImpl( "", "anywhere" );
   }//End Method
   
   /**
    * Prove that a name containing only spaces is rejected.
    */
   @Test( expected = IllegalArgumentException.class ) public void shouldRejectSpaceOnlyName() {
      new TestClassImpl( "   ", "anywhere" );
   }//End Method
   
   /**
    * Prove that a null location is rejected.
    */
   @Test( expected = IllegalArgumentException.class ) public void shouldRejectNullLocation() {
      new TestClassImpl( "anything", null );
   }//End Method
   
   /**
    * Prove that an empty location is rejected.
    */
   @Test( expected = IllegalArgumentException.class ) public void shouldRejectEmptyLocation() {
      new TestClassImpl( "anything", "" );
   }//End Method
   
   /**
    * Prove that a location consisting only of spaces is rejected.
    */
   @Test( expected = IllegalArgumentException.class ) public void shouldRejectSpaceOnlyLocation() {
      new TestClassImpl( "anything", "    " );
   }//End Method

}//End Class
