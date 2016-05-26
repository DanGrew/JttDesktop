/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
 package uk.dangrew.jtt.model.tests;

import org.junit.Assert;
import org.junit.Test;

import uk.dangrew.jtt.model.tests.TestCase;
import uk.dangrew.jtt.model.tests.TestCaseImpl;
import uk.dangrew.jtt.model.tests.TestClass;
import uk.dangrew.jtt.model.tests.TestClassImpl;
import uk.dangrew.jtt.utility.TestCommon;

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
    * Prove that an empty location is accepted.
    */
   @Test public void shouldAcceptEmptyLocation() {
      new TestClassImpl( "anything", "" );
   }//End Method
   
   /**
    * Prove that a location consisting only of spaces is rejected.
    */
   @Test( expected = IllegalArgumentException.class ) public void shouldRejectSpaceOnlyLocation() {
      new TestClassImpl( "anything", "    " );
   }//End Method

   /**
    * Prove that a full name can be given to the constructor, and the {@link TestClass} named correctly.
    */
   @Test public void shouldAcceptNameAndLocation(){
      TestClass testClass = new TestClassImpl( "something.anything.name" );
      Assert.assertEquals( "name", testClass.nameProperty().get() );
      Assert.assertEquals( "something.anything", testClass.locationProperty().get() );
      
      testClass = new TestClassImpl( "something.name" );
      Assert.assertEquals( "name", testClass.nameProperty().get() );
      Assert.assertEquals( "something", testClass.locationProperty().get() );
      
      testClass = new TestClassImpl( "name" );
      Assert.assertEquals( "name", testClass.nameProperty().get() );
      Assert.assertEquals( "", testClass.locationProperty().get() );
   }//End Method
   
   /**
    * Prove that a null full name is rejected.
    */
   @Test( expected = IllegalArgumentException.class ) public void shouldRejectNullFullName(){
      new TestClassImpl( null );
   }//End Method
   
   /**
    * Prove that an empty full name is rejected.
    */
   @Test( expected = IllegalArgumentException.class ) public void shouldRejectEmptyFullName(){
      new TestClassImpl( "" );
   }//End Method
   
   /**
    * Prove that a space only full name is rejected.
    */
   @Test( expected = IllegalArgumentException.class ) public void shouldRejectSpacesOnlyFullName(){
      new TestClassImpl( "    " );
   }//End Method
   
   /**
    * Prove that the {@link TestClass} name can be extracted from a full name.
    */
   @Test public void shouldExtractName(){
      Assert.assertEquals( "name", TestClassImpl.identifyName( "location.here.name" ) );
      Assert.assertEquals( "name", TestClassImpl.identifyName( "location.name" ) );
      Assert.assertEquals( "name", TestClassImpl.identifyName( "name" ) );
      Assert.assertEquals( "name", TestClassImpl.identifyName( ".name" ) );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotIdentifyNameWithNullInput(){
      Assert.assertNull( TestClassImpl.identifyName( null ) );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotIdentifyNameWithEmptyInput(){
      Assert.assertNull( TestClassImpl.identifyName( "   " ) );
   }//End Method
   
   /**
    * Prove that the location of the {@link TestClass} can be extracted from the full name.
    */
   @Test public void shouldExtractLocation(){
      Assert.assertEquals( "location.here", TestClassImpl.identifyLocation( "location.here.name" ) );
      Assert.assertEquals( "location", TestClassImpl.identifyLocation( "location.name" ) );
      Assert.assertEquals( "", TestClassImpl.identifyLocation( "name" ) );
      Assert.assertEquals( "", TestClassImpl.identifyLocation( ".name" ) );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotIdentifyLocationWithNullInput(){
      Assert.assertNull( TestClassImpl.identifyLocation( null ) );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotIdentifyLocationWithEmptyInput(){
      Assert.assertNull( TestClassImpl.identifyLocation( "   " ) );
   }//End Method
   
   @Test public void shouldProvideDescription(){
      final String fullName = "somewhere.something.MyClassName";
      TestClass testClass = new TestClassImpl( fullName );
      Assert.assertEquals( fullName, testClass.getDescription() );
      
      testClass.nameProperty().set( "Alternative" );
      Assert.assertEquals( "somewhere.something.Alternative", testClass.getDescription() );
   }//End Method
   
   @Test public void shouldContainTestCases(){
      TestClass testClass = new TestClassImpl( "something.somehwere.ClassName" );
      
      TestCase testCase1 = new TestCaseImpl( "testCase1", testClass );
      Assert.assertFalse( testClass.hasTestCase( testCase1.nameProperty().get() ) );
      
      testClass.addTestCase( testCase1 );
      Assert.assertTrue( testClass.hasTestCase( testCase1.nameProperty().get() ) );
      
      TestCase testCase2 = new TestCaseImpl( "testCase2", testClass );
      Assert.assertFalse( testClass.hasTestCase( testCase2.nameProperty().get() ) );
      
      testClass.addTestCase( testCase2 );
      Assert.assertTrue( testClass.hasTestCase( testCase2.nameProperty().get() ) );
   }//End Method
   
   @Test public void shouldContainTestCaseAfterReplacement(){
      TestClass testClass = new TestClassImpl( "something.somehwere.ClassName" );
      
      TestCase testCase1 = new TestCaseImpl( "testCase1", testClass );
      Assert.assertFalse( testClass.hasTestCase( testCase1.nameProperty().get() ) );
      
      testClass.addTestCase( testCase1 );
      Assert.assertTrue( testClass.hasTestCase( testCase1.nameProperty().get() ) );
      
      TestCase testCase2 = new TestCaseImpl( "testCase1", testClass );
      Assert.assertTrue( testClass.hasTestCase( testCase2.nameProperty().get() ) );
      
      testClass.addTestCase( testCase2 );
      Assert.assertTrue( testClass.hasTestCase( testCase1.nameProperty().get() ) );
      Assert.assertTrue( testClass.hasTestCase( testCase2.nameProperty().get() ) );
      Assert.assertEquals( 1, testClass.testCasesList().size() );
      Assert.assertFalse( testClass.testCasesList().contains( testCase1 ) );
      Assert.assertTrue( testClass.testCasesList().contains( testCase2 ) );
   }//End Method
   
   @Test public void shouldGetTestCase(){
      TestClass testClass = new TestClassImpl( "something.somehwere.ClassName" );
      
      TestCase testCase1 = new TestCaseImpl( "testCase1", testClass );
      testClass.addTestCase( testCase1 );
      Assert.assertEquals( testCase1, testClass.getTestCase( testCase1.nameProperty().get() ) );
      TestCase testCase2 = new TestCaseImpl( "testCase1", testClass );
      testClass.addTestCase( testCase2 );
      Assert.assertEquals( testCase2, testClass.getTestCase( testCase1.nameProperty().get() ) );
   }//End Method
   
   @Test public void shouldRemoveTestCase(){
      TestClass testClass = new TestClassImpl( "something.somehwere.ClassName" );
      
      TestCase testCase1 = new TestCaseImpl( "testCase1", testClass );
      testClass.addTestCase( testCase1 );
      TestCase testCase2 = new TestCaseImpl( "testCase2", testClass );
      testClass.addTestCase( testCase2 );
      
      Assert.assertTrue( testClass.hasTestCase( testCase1.nameProperty().get() ) );
      Assert.assertEquals( testCase1, testClass.getTestCase( testCase1.nameProperty().get() ) );
      Assert.assertTrue( testClass.hasTestCase( testCase2.nameProperty().get() ) );
      Assert.assertEquals( testCase2, testClass.getTestCase( testCase2.nameProperty().get() ) );
      
      testClass.removeTestCase( testCase1 );
      Assert.assertFalse( testClass.hasTestCase( testCase1.nameProperty().get() ) );
      Assert.assertNull( testClass.getTestCase( testCase1.nameProperty().get() ) );
      Assert.assertTrue( testClass.hasTestCase( testCase2.nameProperty().get() ) );
      Assert.assertEquals( testCase2, testClass.getTestCase( testCase2.nameProperty().get() ) );
   }//End Method
   
   @Test public void shouldAddTestCase() {
      TestClass testClass = new TestClassImpl( "something.somehwere.ClassName" );
      
      TestCase testCase = new TestCaseImpl( "anything", testClass );
      testClass.addTestCase( testCase );
      
      Assert.assertTrue( testClass.testCasesList().contains( testCase ) );
      Assert.assertTrue( testClass.hasTestCase( testCase.nameProperty().get() ) );
   }//End Method
   
   @Test public void shouldHaveTestCase() {
      TestClass testClass = new TestClassImpl( "something.somehwere.ClassName" );
      
      TestCase testCase = new TestCaseImpl( "anything", testClass );
      testClass.addTestCase( testCase );
      Assert.assertTrue( testClass.hasTestCase( testCase.nameProperty().get() ) );
      
      testClass.removeTestCase( testCase );
      Assert.assertFalse( testClass.hasTestCase( testCase.nameProperty().get() ) );
   }//End Method
   
}//End Class
