/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.users;

import javafx.beans.property.ObjectProperty;

/**
 * The {@link JenkinsUser} represents any user or culprit in jenkins.
 */
public interface JenkinsUser {

   /**
    * Provides the {@link StringProperty} of the name of the user.
    * @return the {@link StringProperty}.
    */
   public ObjectProperty< String > nameProperty();

}//End Interface
