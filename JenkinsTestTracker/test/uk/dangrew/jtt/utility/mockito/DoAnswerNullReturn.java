/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.utility.mockito;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * {@link org.mockito.Mockito} {@link Answer} where the return type is irrelevant but we want
 * an action to be performed on the interaction.
 */
public class DoAnswerNullReturn implements Answer< Object > {
   
   private final Runnable runnable;
   
   /**
    * Constructs a new {@link DoAnswerNullReturn}.
    * @param runnable the {@link Runnable} to run when the mock is called.
    */
   public DoAnswerNullReturn( Runnable runnable ) {
      this.runnable = runnable;
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public Object answer( InvocationOnMock invocation ) throws Throwable {
      runnable.run();
      return null;
   }//End Method

}//End Constructor
