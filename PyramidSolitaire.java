//+++++++++++++++++++++ PyramidSolitaire ++++++++++++++++++++++++++
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/**
 * PyramidSolitaire -- App class Pyramid Solitaire.
 * 
 * @author rdb
 * Last modified 03/28/14
 */
public class PyramidSolitaire extends JFrame
{
    //---------------------- class variables -------------------------
    static PrintStream log = null;
    static String      deckFile = null;
    
    //---------------------- instance variables ----------------------
    private GUI _appPanel;      // the app's JPanel
    
    //--------------------------- constructor ------------------------
    /**
     * Constructor for playable solitaire game. The first argument on the
     *    command line is interpreted as a file name for the initial deck.
     * 
     * @param title String     window title
     * @param args String[]    command line args; only arg is initial deck
     */
    public PyramidSolitaire( String title, String[] args )     
    {
        super( title );
        this.setBackground( Color.LIGHT_GRAY );
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        _appPanel = new GUI( );
        this.add( _appPanel );
        this.setSize( _appPanel.getWidth(), _appPanel.getHeight() + 100 );
        
        this.setVisible( true );
    }
    //-------------------- batchRun ------------------------------
    /**
     * Run the application in batch mode; bypass the JFrame that is
     *    this class. This should even work while running on agate with
     *    the "headless" option:
     * 
     * java -Djava.awt.headless=true PyramidSolitaire deck-s0n0.txt 5
     * 
     * @param args String[]    command line args
     */
    private static void batchRun( String[] args )
    {
        try 
        {
            log = new PrintStream( "pyramid.log" );
        }
        catch ( FileNotFoundException fnf )
        {
            System.err.println( "Can't open pyramid.log; "
                                   + "using System.out" );
        }
        if ( args.length > 0 )  // 1st argument is an initial deck
            deckFile = args[ 0 ];
        if ( args.length > 1 ) // 2nd argument is depth
            Game.numRows = Utilities.intArg( args, 1, 5 );

        Game game = new Game( 500 );
        game.setBaseDeck( game.readDeck( deckFile ) );
        game.autoPlayAll();
        log.close();
        System.exit( 0 );
    } 
    //----------------------- log( ... ) -----------------------
    /**
     * Print the status of a deck of cards; will go to log file if it
     *    is defined or to System.out if it isn't.
     * @param title String            title the output
     * @param cardSet Iterable<Card>     draw or play or deck cardSet
     */
    public static void log( String title, Iterable<Card> cardSet )
    {
        if ( log == null )
        {
            log( System.out, title, cardSet );
        }
        else
            log( log, title, cardSet );
    }
    //----------------------- log( ... ) -----------------------
    /**
     * Print the status of a deck of cards.
     * 
     * @param out   PrintStream  output file
     * @param title String       title the output
     * @param cardSet  CardGroup     draw or play pile
     */
    private static void log( PrintStream out, String title, 
                               Iterable<Card> cardSet )
    {
        String dashes = " ----------------------- ";
        out.println( dashes + title + dashes );
        out.println( Game.cardsToString( cardSet ) );
    }
    //--------------------------- main --------------------------------
    /**
     * This main starts the application.
     * 
     * @param args String[]            command line arguments.
     */
    public static void main( String [ ] args ) 
    {
        if ( args.length > 0 )  // run in batch without display
            batchRun( args );
        else
            new PyramidSolitaire( "PyramidSolitaire", args );
    }
}
