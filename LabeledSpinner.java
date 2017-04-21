/** 
 * LabeledSpinner.java:
 * A class that combines a spinner and a label.
 * 
 * Child classes must define the valueChanged( int ) method.
 *
 * Based on a class written by   Matthew Plumlee, Spring 2008
  */

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class LabeledSpinner extends JPanel 
{
   //---------------- instance variables -------------------------
   private JSpinner _spinner;
   //------------------- constructor ------------------------------
   /**
    * Constructor specifies name, min, max  and initial values.
    */
   public LabeledSpinner ( String name, int min, int max, int val ) 
   {
      super();
      // create the label and spinner and set the parameters.
      JLabel label = new JLabel( name );
      add( label );
      
      SpinnerModel model = new SpinnerNumberModel( val, min, max, 1 );
      _spinner = new JSpinner( model );
      
      add( _spinner );
   }
   //--------------------- addChangeListener( ChangeListener )-----------------
   /**
    * add a listener for the Spinner's change events
    */
   public void addChangeListener( ChangeListener listener )
   {
      _spinner.addChangeListener( listener );
   }
}
