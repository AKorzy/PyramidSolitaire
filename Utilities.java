//+++++++++++++++++++++++++++ Utilities +++++++++++++++++++++++++++
import javax.swing.*;
import java.io.*;
/** 
  * Utilities.java: class provides several useful utility functions as
  *    static methods.
  * 
  *        void   frame()
  *        void   sleep( int msecs )
  *        int    intArg( String[], int, int )
  *        String getFileName()
  * 
  * @author rdb
  */

public  class Utilities 
{
    //-------------------- class variables -----------------------
    private static int frameTime = 100;
    private static JFileChooser chooser = null;
 
    //---------------------- class methods ------------------------
  
    //---------------------- frame() ------------------------------
    /**
     * Delay an appropriate time to get a friendly frame rate (100 ms).
     */
    public static void frame( ) 
    {
        sleep( frameTime );
    }
    
    //---------------------- sleep( int ) ------------------------------
    /**
     * Put program (thread, actually) to sleep for the specified millisecs.
     * @param milliseconds int     time to delay
     */
    public static void sleep( int milliseconds ) 
    {
        try
        {
            Thread.sleep( milliseconds );
        }
        catch ( java.lang.InterruptedException e ) 
        {  
        }
    }
    
    //---------------------- intArg( String[], int, int ) -----------------
    /**
     * This is a utility method for accessing one of the command line
     * string arguments and converting it to an int. It accepts the 
     * entire array of arguments in String form, an integer indicating 
     * which is to be converted to an int and a default value to be 
     * returned if this argument was not on the command line, or if 
     * the specified String is not a valid integer representation.
     *
     * @param args String[]    command line as array of strings
     * @param which int        argument to be converted to int
     * @param defaultVal int   default if arg is not present or not valid
     * @return int             value returned
     */
    public static int intArg( String[ ] args, int which, int defaultVal )
    {
        try
        {
            return Integer.parseInt( args[ which ] );
        }
        catch ( ArrayIndexOutOfBoundsException oob )
        {
            // If there is no args[ which ] element, Java "throws" an exception.
            // This code "catches" the exception and handles it gracefull.
            // In this case, it is not an error. The parameter is optional
            // and there is a specified default value that is returned.
        }
        catch ( NumberFormatException nfe )
        {
            // If the string is not a valid representation of an integer
            // (such as Qd3), Integer.parseInt throws NumberFormatException.
            // This code catches the exception, gives an error message
            // and uses the default value.
            
            System.err.println( "Error: improper command line argument "
                    + which + " = " + args[ which ]
                    + ".  It should be an integer; using default value: "
                    + defaultVal );
        }  
        return defaultVal;
    }
    
    //---------------------- String getFileName() ----------------------
    /**
     * Use a JFileChooser dialog to get a valid file name from a user.
     *   Will not return the name unless the file exists.
     *   Returns null if no valid file selected.
     * @return String
     */
    public static String getFileName()
    {
        String fileName = null;
       
        if ( chooser == null )
        {
            chooser = new JFileChooser();
            chooser.setCurrentDirectory( new File( "." ) );
        }
       
        int returnVal = chooser.showOpenDialog( null );
        while ( fileName == null && returnVal != JFileChooser.CANCEL_OPTION ) 
        {
            if ( returnVal == JFileChooser.APPROVE_OPTION )
            {
                File f = chooser.getSelectedFile();
                if ( f.isFile() )
                    fileName = f.getPath();
                else
                    returnVal = chooser.showOpenDialog( null );
            }
        }
        return fileName;
    }
}
