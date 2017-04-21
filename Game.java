//+++++++++++++++++++++++++ Game.java +++++++++++++++++++++++++++++
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;   
/**
 * Game.java - implementation of a solitaire card game.
 * 
 * @author rdb
 * March 2009
 * mlb 10/09: new cards , new shuffle, new baseDeck
 * rdb 03/14: Changes to reflect Pyramid solitaire and Pyramid class.
 */

public class Game extends JPanel
{
    //----------------------- class variables ---------------------------
    private static int   seed = 0;
    protected static         Game  theGame;
    protected static         int   numRows = 4;
    //----------------------- instance variables ------------------------
    
    private CardStack             _drawPile = null; 
    private CardStack             _discards = null;
    private ArrayList<Card>       _baseDeck = null;
    private int                   _parentWidth;
    private Pyramid<Card>         _pyramid;
    private Random                _rng;
    private ArrayList<Card>       _pyramidCards;
    protected  ArrayList< ArrayList< PyramidNode > > outer;
    private ArrayList<Card> _startLog;
    private CardStack _drawLog;
    private CardStack _playLog;
    private Boolean gameOver = false;
    
    //----- positioning variables
    
    private   int discardX  = 60,  discardY  = 140;
    private   int drawPileX = 60,  drawPileY = 40;
    private   int pyramidX  = 400,   pyramidY  = 40;
    private Stack<String> status = new Stack<String>();
    
    //---------------------- constructor -----------------------------
    /**
     * Game is where most of the game-based code is found.
     * 
     * @param pWidth int     width of the panel (apprx)
     */
    public Game( int pWidth )
    {
        this.setLayout( null );
        theGame = this;
        
        _parentWidth = pWidth;
        _baseDeck = new ArrayList<Card>();
        _pyramidCards = new ArrayList<Card>();
        
        createDeck();
        _discards = new CardStack( this, discardX, discardY );
        _discards.setYOffset( 2 );
        
        _drawPile = new CardStack( this, drawPileX, drawPileY );
        _drawPile.setXOffset( 0 );
        _rng = new Random( seed );
        
        makeNewDeck();
    }
    //------------------------- drawCard() ----------------------------
    /**
     * Draw a card from the display pile.
     * 
     * @return String
     */
    public String drawCard()
    {
        String msg = null;
        ////////////////////////////////////////////////////////////////
        //
        // To draw a card, you need to pop it from the _drawPile 
        //   and push it onto the _discards pile.
        // Of course, check if it is empty.
        // If the draw pile stack is empty, this method should return
        //   a non-null message -- indicating the game is over.
        ///////////////////////////////////////////////////////////////
        if ( _drawPile.size() != 0 )
        {
            Card next = _drawPile.pop();
            _discards.push( next );
        }
        else
            msg = "No more cards to draw." ;
        
        update();
        return msg;
    }
    //------------------------ makeNewDeck() --------------------------
    /**
     * Create new deck.
     */
    public void makeNewDeck()
    {
        gameOver = false;
        Collections.shuffle( _baseDeck, _rng );
        replay();
    }
    
    //---------------------- replay( ) -----------------------------
    /**
     * Replay the game.
     */
    public void replay()
    {
        gameOver = false;
        PyramidSolitaire.log( "Starting Deck", _baseDeck );
        _discards.clear();
        _pyramidCards.clear();
        deckToDrawPile( _baseDeck );          
        dealCards( _drawPile );
        
        _discards.clear();
        _pyramidCards.clear();
        update();
        
    }
    //---------------------- draw( ) -----------------------------
    /**
     * Draw from draw pile. Simulate click on draw pile.
     */
    public void draw()
    {
        play( _drawPile.top() );
    }
    //---------------------- undoPlay( ) -----------------------------
    /**
     * Undo the previous play.
     */
    public void undoPlay()
    {
        /////////////////////////////////////////////////////////////
        // 7I: Do the "inverse" of the last play
        /////////////////////////////////////////////////////////////
//        System.out.println( "Undo not implemented." );
        
        if ( status.isEmpty() && !_discards.isEmpty() )
        {
            _drawPile.push( _discards.pop() );
        }
        
        if ( !status.isEmpty() && status.peek().equals( "stack" ) 
                && !_discards.isEmpty() )
        {
            Card undo = _discards.pop();
            System.out.println( "$ status: " + status.peek() );
            status.pop();
            _drawPile.push( undo );
            
        }
        else if( !_discards.isEmpty() && status.peek().equals( "pyramid" ) )
        {
            Card undo = _discards.pop();
            if ( undo.getNode() != null )
            {
                int x = undo.getNode().getX();
                int y = undo.getNode().getY();
//                System.out.println( "X: " + x + " Y: " + y );
                undo.setLocation( x, y );
                makeChildren( undo.getNode() );
//                System.out.println( "P status: " + status.peek() );
                status.pop();
            }
        }
        else
            System.err.println( "No previous move to undo" );
        
        reDrawPyramid();
        update();
    }
    //---------------------- autoPlay( ) ----------------------------
    /**
     * Go into auto play mode.
     */
    public void autoPlay()
    {
//        deckToDrawPile( _baseDeck );  
        
//        _discards.clear();
//        _pyramid = new Pyramid<Card>( numRows, pyramidX, pyramidY, 
//                                  Card.width, Card.height );
//        dealCards( _drawPile );
        //////////////////////////////////////////////////////////////
        // This will be part of 7P
        //////////////////////////////////////////////////////////////
        
        
//         _startLog 
//         _drawLog
//         _playLog
//        
        
        Boolean b = false;
        if ( _discards.isEmpty() )
        {
            draw();
//            System.out.println( "Card drew in autoplay" );
        }
//        if ( outer.get( 0 ).get( 0 ) != null )
//        {
        Card tCard = _discards.peek();
        for( int i = numRows - 1; i >= 0; i-- )
        {
            for( int j = 0; j < outer.get( i ).size(); j++ )
            {
//                    System.out.println( ( outer.get( i ).get( j ) ) );
                if ( outer.get( i ).get( j ).getAuto() != null )
                {
//   System.out.println( "THIS: "+( Card ) outer.get( i ).get( j ).getData() );
                    b = play( ( Card ) outer.get( i ).get( j ).getData() );
//                        System.out.println( b );
                    if( b == true )
                    {
                        break;
                    }
                }
//                    System.out.println( b );
            }
            if( b == true )
                break;
        }
        if( b == false )
        {
            if ( outer.get( 0 ).get( 0 ).getAuto() != null )
                draw();
        }
//    }
//    else
//        System.err.println( "Game Over!" );
//    b = false;
        update();
    }
//---------------------- autoPlayAll( ) ---------------------------
    /**
     * Make autoPlays until end of game.
     */
    public void autoPlayAll()
    {
        //////////////////////////////////////////////////////////////
        // Part of 7P
        //////////////////////////////////////////////////////////////
        while(  outer.get( 0 ).get( 0 ).getAuto() != null   )
        {
//        System.out.println(outer.get(0).get(0) + " " + _drawPile.size());
            autoPlay();
            if ( _drawPile.size() == 0 )
                break;
        }
        
//    System.out.println("Left while");
        
        
        update();        
    }
//---------------------- autoPlayAll( ) ---------------------------
    /**
     * Make autoPlays until end of game.
     * 
     * @return int
     */
    public int calculateScore()
    {
        int score = 0;
        if( _drawPile.isEmpty() )
        {
            for( int i = numRows - 1; i >= 0; i-- )
            {
                for( int j = 0; j < outer.get( i ).size(); j++ )
                {
                    if ( outer.get( i ).get( j ).getAuto() != null )
                        score--;
                }
            }
        }
        else
        {
            score = _drawPile.size();
            
        }
        return score;
    }
//------------------------ play( Card ) --------------------------
    /**
     * Play a card.
     * 
     * @param picked Card     a card that is to be played.
     * @return boolean        true means a valid play occurred.
     */
    public boolean play( Card picked )
    {
        boolean playedFromPyramid = false;
        
        if ( picked == _drawPile.top() )
        {
            drawCard();
            status.push( "stack" );
//            System.out.println( "Added stack to status" );
        }
        else if ( playPyramidCard( picked ) )
        {
            playedFromPyramid = true;
            ///////////////////////////////////////////////////////////
            // 7I: Need to check here to see if game has ended
            ///////////////////////////////////////////////////////////
            if ( _discards.top() != null )
            {
                status.push( "pyramid" );
//                 System.out.println( "Added pyramid to status" );
//                 makeChildren( picked.getNode() );
            }         
        }
        
        return playedFromPyramid;       
    }
//------------------------ playPyramidCard ------------------------
    /** 
     * Play a card from the pyramid.
     *
     * @SupressWarnings( "unchecked" )
     * @param picked Card
     * @return boolean 
     */
    private boolean playPyramidCard( Card picked )
    {
        
        Point position = searchCard( picked );
        int i = ( int ) position.getX();
        int j = ( int ) position.getY();
//    System.out.println( i + ", " + j + " Card Selected" );
        boolean success = false;
//        status.push( "pyramid" );
//        System.out.println( "Added pyramid to status" );
//    if( searchCard( picked ).x == 0 && searchCard( picked ).x == 0 )
//        System.err.println( "Root card selected" );
        if( childCheck( i , j ) == false )
        {
            
            if ( _discards.top() != null )
            {
                
                PyramidNode cardNode = picked.getNode();
                {
                    Card.Rank pickedRank = picked.getRank();
                    Card top = _discards.top();
                    int diff = Math.abs( pickedRank.ordinal() 
                                            - top.getRank().ordinal() );
                    
                    if ( diff == 1 || diff == 12 )
                    {
                        _discards.push( picked );                
                        update();
                        success = true;
                        try
                        {
                            outer.get( i - 1 ).get( j )._left = null; 
                        }
                        catch( java.lang.ArrayIndexOutOfBoundsException e )
                        {
//                    System.out.println( i + ":" + j + " left is AN ORPHAN" );
                        }
                        catch( java.lang.IndexOutOfBoundsException e )
                        {
//               System.err.println( i + ":" + j + "  left parent not found" );
                        }
                        try
                        {
                            outer.get( i - 1 ).get( j - 1 )._right = null;
                        }
                        catch( java.lang.ArrayIndexOutOfBoundsException e )
                        {
//                   System.out.println( i + ":" + j + " right is AN ORPHAN" );
                        }
                        catch( java.lang.IndexOutOfBoundsException e )
                        {
//              System.err.println( i + ":" + j + "  right parent not found" );
                        }
                        outer.get( i ).get( j ).setAuto( null );
                        
                    }
                } 
            }
        }
//    else
//        System.err.println( "ERROR: Card has Children" );
//    System.out.println( "children :" +  outer.get( i ).get( j ).toString() );
        
        return success;
    }
//----------------------------- searchCard() ----------------------
    /**
     * Returns boolean based on existence of children.
     * 
     * @return Point
     * @param c Card 
     */
    public Point searchCard( Card c )
    {
        Point p = new Point();
        for ( int i = 0; i < outer.size(); i++ )
        { 
            for ( int j = 0; j < outer.get( i ).size(); j++ )   
            {
                if ( outer.get( i ).get( j ).getData() == c ) 
                {
//                    if( outer.get( i ).get( j ).getData() == null )
                    p = new Point( i, j );
                }
            }
        }
        return p;
    }
    
//----------------------------- childCheck() ----------------------
    /**
     * Returns boolean based on existence of children.
     * 
     * @param i int
     * @param j int
     * @return Boolean
     */
    public Boolean childCheck( int i, int j )
    {
        Boolean b = true;//= false;
        
        if ( outer.get( i ).get( j )._left == null &&
            outer.get( i ).get( j )._right == null )
            b = false;
        
        else if ( outer.get( i ).get( j )._left != null )
        {
            b = true;
        }
        else if( outer.get( i ).get( j )._right != null )
        {
            b = true;
        }
        
        
        return b;
    }
//----------------------------- update() ----------------------
    /**
     * Update the display components as needed.
     */
    public void update()
    {
        /////////////////////////////////////////////////////////////
        // You might need to do something here. I didn't, but there
        //    or valid implementations that might require it.
        /////////////////////////////////////////////////////////////
        if ( gameOver != true )
        {
            if( outer.get( 0 ).get( 0 ).getAuto() == null 
                   || _drawPile.isEmpty() )
            {
                gameOver = true; 
                PyramidSolitaire.log( "Draw Pile", _drawPile );
                PyramidSolitaire.log( "Play Pile", _discards );
            }
            // show all cards on the _discards stack 
            _discards.showCards( -1 );
            
            // show no cards on the draw pile
            _drawPile.showCards( 0 );
            this.repaint();    
        }
        else
            System.err.println( "Games Over! Score: " + calculateScore() ); 
    }
    
//-------------------- dealCards() ----------------------------
    /**
     * Deal the cards from the drawPile to the pyramid. 
     *    This version just stores all the dealt cards in an ArrayList.
     * 
     * @param deck CardStack    deck to deal from
     */
    public void dealCards( CardStack deck )
    {
        /////////////////////////////////////////////////////////////
        // This approach creates PyramidNodes for each Card in the 
        //    pyramid, but does not put the nodes into a tree.
        // You'll have to figure out where/how to do that and what 
        //    changes you'll have to make here.
        /////////////////////////////////////////////////////////////
        int xGap = 2;
        int yDelta = 30;
        outer = new ArrayList< ArrayList< PyramidNode > >( numRows );
        
        for ( int level = 0; level < numRows; level++ )
        {
            ArrayList< PyramidNode > inner = new ArrayList< PyramidNode >(); 
            
            int span = Card.width * ( level + 1 ) + xGap * level;
            
            int xPos = pyramidX - span / 2;
            int yPos = pyramidY + level * yDelta;
//            PyramidNode<Card> tNode;
            for ( int n = 0; n < level + 1; n++ )
            {
                PyramidNode<Card> node = new PyramidNode<Card>( xPos, yPos, 
                                                               Card.width, 
                                                               Card.height );
                inner.add( node ); //***
//                makeChildren( node ); //***
                
                Card card = deck.pop();
                _pyramidCards.add( card );
                card.setNode( node );
                node.setData( card );
                card.setFaceUp( true );
                card.setLocation( xPos, yPos );
                this.setComponentZOrder( card, 0 );
                xPos += Card.width + xGap;
            }
            outer.add( inner );
            for ( int i = 0; i < _pyramidCards.size(); i++ )
            {
                Card c = _pyramidCards.get( i );
            }
        }
        for ( int i = 0; i < numRows; i++ )
        {
            for ( int j = 0; j < i + 1; j++ )
            {
                makeChildren( outer.get( i ).get( j ) );
            }
        }
        
//        outer = new ArrayList< ArrayList< PyramidNode > >( nRows );
//        int size = 0; 
//        for ( int o = 0; o < outer.size(); o++ )
//        {
//            ArrayList< PyramidNode > inner = new ArrayList< PyramidNode >(); 
//            for( int t = 0; t < size + 1; t++ )
//            {
//                inner.add( new PyramidNode( x, y, nW, nH ) );
//            }
//            size += 1;
//            outer.add( inner );
//        }
        
        reDrawPyramid();
    }
    
//-------------------- makeChildren ----------------------------
    /**
     * Makes the babies.
     * 
     * @param p PyramidNode
     */
    public void makeChildren( PyramidNode p )
    {
        Point point = searchCard( ( Card ) p.getData() );
        int x = point.x;
        int y = point.y;
        try
        {
            p._left = outer.get( x + 1 ).get( y );
//        System.out.println( " left node added in makeChildren()" );
        }
        catch( IndexOutOfBoundsException iob )
        {
//        System.err.println( "left node not found in makeChildren()" );
        }
        try
        {   
            p._right = outer.get( x + 1 ).get( y + 1 );
//        System.out.println( " right node added " + x + " " + y +
//                           "  in makeChildren()" );
        }
        catch( IndexOutOfBoundsException iob )
        {
//        System.err.println( "right node of " + x + " " + y +
//                           " not found in makeChildren()" );
        }
        
        
        
    }
//-------------------- reDrawPyramid ----------------------------
    /**
     * Draw the pyramid tree cards.
     */
    public void reDrawPyramid()
    {
        //////////////////////////////////////////////////////////////
        //  In its present form it just redisplays every card that was
        //     once in the pyramid, whereever they happen to be.
        //  It may or may not be good enough for your implementation.
        //////////////////////////////////////////////////////////////
        for ( Card card: _pyramidCards )
        {
            card.setFaceUp( true );
            this.setComponentZOrder( card, 0 );
        }
        
    }
    
//////////////////////////////////////////////////////////////////
// You probably don't need to edit anything below here,
//    but you may if you wish.
//////////////////////////////////////////////////////////////////
//------------------------ newSeed( ) -----------------------------
    /**
     * Set new random number generator seed.
     * 
     * @param newSeed int
     */
    public void newSeed( int newSeed )
    {
        _rng = new Random( newSeed );
        seed = newSeed;
    }
//------------------------ setRows( ) -----------------------------
    /**
     * Change the number of rows in the game.
     *     Won't have effect until next game is played.
     * @param rows int
     */
    public void setRows( int rows )
    {
        numRows = rows;
    }
//------------------- theGame() --------------------------------
    /**
     * Singleton pattern requires this method to return a reference
     *    to only allowed instance.
     * @return Game
     */
    public static Game theGame()
    {
        return theGame;
    }
//------------------ showDeck() ----------------------------------
    /**
     * Turn all cards in deck face up and spread them out.
     */
    public void showDeck()
    {
        _drawPile.setXOffset( 11 );
        _drawPile.showCards( -1 );
        this.repaint();
    }
//------------------ hideDeck() ----------------------------------
    /**
     * Turn all cards in deck face down and stack them up again.
     */
    public void hideDeck()
    {
        _drawPile.setXOffset( 0 );
        _drawPile.showCards( 0 );
        this.repaint();
    }
    
//------------------------ createDeck() ---------------------------
    /**
     * Create a deck of cards in the _base variable.
     */ 
    private void createDeck()
    {
        int  cardIndex = 0;
        
        for ( Card.Suit suit: Card.Suit.values() )
        {
            for ( Card.Rank rank: Card.Rank.values() )
            {
                Card card = new Card( rank, suit );
                _baseDeck.add( 0, card );
                this.add( card );
            }
        }
    }
    
//------------------------ deckToDrawPile( Card[] ) ---------------
    /**
     * Copy an array of cards into CardStack representing draw pile.
     * 
     * @param deck ArrayList<Card>
     */
    private void deckToDrawPile( ArrayList<Card> deck )
    {
        _drawPile.clear();
        for ( int c = 0; c < 52; c++ )
            _drawPile.push( deck.get( c ) );
//        update();
    }
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++ new code ++++++++++++++++++++++++++++++++++
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//------------------------ setBaseDeck() --------------------------
    /**
     * Create new base deck.
     * @param newDeck ArrayList<Card>    the new deck.
     */
    public void setBaseDeck( ArrayList<Card> newDeck )
    {    
        if ( _baseDeck != null )
        {
            for ( Card card: _baseDeck )
                this.remove( card );   // remove from JPanel
        }
        _baseDeck.clear();
        
        for ( Card card: newDeck )
        {
            _baseDeck.add( card );
            this.add( card );
        }
        PyramidSolitaire.log( "Starting deck", _baseDeck );        
        replay();
    }
    
//---------------------- writeDeck( ) -----------------------------
    /**
     * Write the current _baseDeck to a file.
     */
    public void writeDeck()
    {
        String msg = "Enter file name of desired deck file";
        String outName = JOptionPane.showInputDialog( null, msg );
        //String outName = Utilities.getFileName();
        if ( outName != null && outName.length() > 0 )
            writeDeck( outName );
    }
//---------------------- writeDeck( String ) ----------------------
    /**
     * Write the current _baseDeck to the named file.
     * @param fileName String     name of output file
     */
    public void writeDeck( String fileName )
    {
        PrintStream out;
        try
        {
            out = new PrintStream( new File( fileName ) );
        }
        catch ( FileNotFoundException fnf )
        {
            System.err.println( "*** Error: unable to open " + fileName );
            return;
        }
        out.println( cardsToString( _baseDeck ) );
        out.close();
    }
//---------------------- readDeck( ) -----------------------------
    /**
     * Read a file to replace _baseDeck.
     */
    public void readDeck()
    {
        String inName = Utilities.getFileName();
        if ( inName != null && inName.length() > 0 )
        {
            ArrayList<Card> newDeck = readDeck( inName );
            if ( newDeck.size() != 52 )
                System.err.println( "*** ERROR. Game.readDeck: file has "
                                       + newDeck.size() + " cards!" );
            else                          
                setBaseDeck( newDeck );
        }
    }
//------------------------ readCardFile -------------------------
    /**
     * Read card representations from a file. Input file should consist of
     *    card representations of the form rs separated by spaces, where
     *        r is one of  23456789XJQKA
     *        s is one of  CDHS
     *    Case is not relevant. Lines are not relevant.
     * @param filename String  
     * @return ArrayList<Card>
     */
    public ArrayList<Card> readDeck( String filename )
    { 
        Card.Rank[] rankValues = Card.Rank.values();
        Card.Suit[] suitValues = Card.Suit.values();
        //-------- This assumes Ace low! ----------------------
        String rankCodes = "A23456789XJQK";
        String suitCodes = "CDHS";
        Scanner scan = null;
        
        ArrayList<Card> cards = new ArrayList<Card>();
        try
        {
            scan = new Scanner( new File( filename ) );
        } 
        catch ( FileNotFoundException fnf )
        {
            System.out.println( "Can't find: " + filename );
            return null;
        }
        int count = 0;
        while ( scan.hasNextLine() )
        {
            String line = scan.nextLine();
            line = line.trim();
            if ( line.length() > 0 && line.charAt( 0 ) != '#' ) 
            {
                String[] words = line.split( " " );
                for ( int w = 0; w < words.length; w++ )
                {
                    String word = words[ w ];
                    char rankch = words[ w ].charAt( 0 );
                    char suitch = words[ w ].charAt( 1 );
                    int rankIndex = rankCodes.indexOf( rankch );
                    int suitIndex = suitCodes.indexOf( suitch );
                    if ( rankIndex < 0 || suitIndex < 0 )
                    {
                        System.err.println( "Scan error?: " + rankIndex +
                                           " " +   suitIndex + " " + words[ w ]
                        );
                    }
                    else
                    {
                        Card.Rank rank = rankValues[ rankIndex ];
                        Card.Suit suit = suitValues[ suitIndex ];
                        cards.add( new Card( rank, suit ) );
                        count++;
                    }
                }
            }
        }
        return cards;
    }
//------------------- cardsToString( Iterable<Card> ) -------------
    /**
     * Create a compact string representation of a collection of Cards.
     * 
     * @param cardSet Iterable<Card>   the card collection to process
     * @return String
     */
    public static String cardsToString( Iterable<Card> cardSet )
    {
        //-------- This assumes Ace low! ----------------------
        String rankChar = "A23456789XJQK";
        String suitChar = "CDHS";
        StringBuffer out = new StringBuffer();
        int lineLen = 0;
        int maxLine = 38;
        for ( Card card: cardSet )
        {
            char r = rankChar.charAt( card.getRank().ordinal() );
            char s = suitChar.charAt( card.getSuit().ordinal() );
            out.append( "" + r + s + " " );
            lineLen += 3;
            if ( lineLen > maxLine )
            {
                out.append( "\n" );
                lineLen = 0;
            }
        }
        if ( lineLen > 0 )
            out.append( "\n" );
        return out.toString();
    }
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//--------------------------- main ---------------------------------
    /**
     * A convenient tool for invoking main class.
     * @param args String[]    Command line arguments
     */
    public static void main( String[] args )
    {
        // Invoke main class's main
        PyramidSolitaire.main( args );
    }
}
