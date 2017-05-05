/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.nodes;

import javafx.beans.property.ObjectProperty;

/**
 * The {@link JenkinsNode} represents a node that can run {@link uk.dangrew.jtt.model.jobs.JenkinsJob}s.
 */
public interface JenkinsNode {
   
   /**
    * Access to the property holding the {@link String} name of the {@link JenkinsNode}.
    * @return the {@link ObjectProperty}.
    */
   public ObjectProperty< String > nameProperty();

}//End Interface
