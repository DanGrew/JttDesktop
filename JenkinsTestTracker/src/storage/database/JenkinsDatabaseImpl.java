/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package storage.database;

import javafx.collections.ObservableList;
import model.jobs.JenkinsJob;
import model.tests.TestClass;
import model.users.JenkinsUser;
import storage.database.events.JenkinsJobPropertyListener;
import storage.structure.MappedObservableStoreManagerImpl;
import storage.structure.ObjectStoreManager;

/**
 * Basic implementation of the {@link JenkinsDatabase}.
 */
public class JenkinsDatabaseImpl implements JenkinsDatabase {

   private ObjectStoreManager< TestClassKey, TestClass > testClasses;
   private ObjectStoreManager< String, JenkinsJob > jenkinsJobs;
   private ObjectStoreManager< String, JenkinsUser > jenkinsUsers;
   private JenkinsJobPropertyListener jenkinsJobProperties;

   /**
    * Constructs a new {@link JenkinsDatabaseImpl}.
    */
   public JenkinsDatabaseImpl() {
      testClasses = new MappedObservableStoreManagerImpl<>();
      jenkinsJobs = new MappedObservableStoreManagerImpl<>();
      jenkinsUsers = new MappedObservableStoreManagerImpl<>();
      jenkinsJobProperties = new JenkinsJobPropertyListener( this );
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean hasNoTestClasses() {
      return testClasses.isEmpty();
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean hasNoJenkinsJobs() {
      return jenkinsJobs.isEmpty();
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean hasNoJenkinsUsers() {
      return jenkinsUsers.isEmpty();
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public boolean hasTestClass( TestClassKey testClassKey ) {
      return testClasses.has( testClassKey );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean hasJenkinsJob( String key ) {
      return jenkinsJobs.has( key );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean hasJenkinsUser( String key ) {
      return jenkinsUsers.has( key );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean containsTestClass( TestClass testClass ) {
      return testClasses.objectList().contains( testClass );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean containsJenkinsJob( JenkinsJob jenkinsJob ) {
      return jenkinsJobs.objectList().contains( jenkinsJob );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean containsJenkinsUser( JenkinsUser jenkinsUser ) {
      return jenkinsUsers.objectList().contains( jenkinsUser );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void store( TestClass testClass ) {
      if ( testClass == null ) return;
      
      TestClassKey representativeKey = new TestClassKeyImpl( 
               testClass.nameProperty().get(), 
               testClass.locationProperty().get() 
      );
      testClasses.store( representativeKey, testClass );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void store( JenkinsJob jenkinsJob ) {
      if ( jenkinsJob == null ) return;
      if ( jenkinsJob.nameProperty().get() == null ) return;
      
      jenkinsJobs.store( jenkinsJob.nameProperty().get(), jenkinsJob );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void store( JenkinsUser jenkinsUser ) {
      if ( jenkinsUser == null ) return;
      if ( jenkinsUser.nameProperty().get() == null ) return;
      
      jenkinsUsers.store( jenkinsUser.nameProperty().get(), jenkinsUser );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public TestClass getTestClass( TestClassKey testClassKey ) {
      return testClasses.get( testClassKey );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public JenkinsJob getJenkinsJob( String key ) {
      return jenkinsJobs.get( key );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public JenkinsUser getJenkinsUser( String key ) {
      return jenkinsUsers.get( key );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public TestClass removeTestClass( TestClassKey testClassKey ) {
      return testClasses.remove( testClassKey );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public JenkinsJob removeJenkinsJob( String key ) {
      return jenkinsJobs.remove( key );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public JenkinsUser removeJenkinsUser( String key ) {
      return jenkinsUsers.remove( key );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean removeTestClass( TestClass testClass ) {
      if ( testClass == null ) return false;
      
      TestClass removed = testClasses.remove( new TestClassKeyImpl( 
               testClass.nameProperty().get(), testClass.locationProperty().get() ) 
      );
      return removed != null;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean removeJenkinsJob( JenkinsJob jenkinsJob ) {
      if ( jenkinsJob == null ) return false;
      
      JenkinsJob removed = jenkinsJobs.remove( jenkinsJob.nameProperty().get() );
      return removed != null;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean removeJenkinsUser( JenkinsUser jenkinsUser ) {
      if ( jenkinsUser == null ) return false;
      
      JenkinsUser removed = jenkinsUsers.remove( jenkinsUser.nameProperty().get() );
      return removed != null;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObservableList< TestClass > testClasses() {
      return testClasses.objectList();
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObservableList< JenkinsJob > jenkinsJobs() {
      return jenkinsJobs.objectList();
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObservableList< JenkinsUser > jenkinsUsers() {
      return jenkinsUsers.objectList();
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public JenkinsJobPropertyListener jenkinsJobProperties() {
      return jenkinsJobProperties;
   }//End Method

}//End Class
