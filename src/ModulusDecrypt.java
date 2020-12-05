
public class ModulusDecrypt 
{

    public ModulusDecrypt() 
    {
        //testMethod("036147258", 3);  // → "012345678"
        //testMethod("01234", 3);  // → "03142"
        //testMethod("03142", 3);  // → "01234"
        
        ///testMethod("024681357", 2);  // → "012345678"
        //testMethod("0481592637", 4); // → "0123456789"
        testMethod("Hlwleoodl r", 3); // → "Hello world"
        testMethod("Hldksmo ideoansylfel re  drn", 3); // → "Hello darkness my old friend"
        testMethod("H pmiredeh  nesleIbgpspl'e re", 4); // → "Help help I'm being repressed"
        
    }
    /*
    Hldksmo id
    eoansylfe
    l re  drn
    
    H pmired
    eh  nes
    leIbgps
    pl'e re
    048
    159
    26
    37
    */
    public void testMethod( String input, int key )
    {
        System.err.println( input + " " + key + " -> "  ) ;
        System.err.println( "|"+apcsaDecryptMod(input, key)+"|") ;
        System.err.println("xxxxxxxxxxxxxxxxxxxxxxxxxxx") ;
    }
    
    String apcsaDecryptMod( String ciphertext, int modulus )
    {
     	char[] outArray = new char[ciphertext.length()] ;
    	int pos = 0 ;
    	for( int i = 0 ; i < ciphertext.length() ; i++ )
    	{
    		outArray[pos] = ciphertext.charAt(i) ;
    		System.err.println( generateString( outArray ) ) ;

    		if( pos + modulus >= ciphertext.length() )
    		{
    			pos = (pos % modulus) + 1;
    		}
    		else
    		{
    			pos += modulus ;
    		}
    	}
    	
        return generateString(outArray) ;
    }
    
    String generateString( char[] outArray )
    {
    	String out = "";
    	for( int i = 0 ; i < outArray.length ; i++ )
    	{
    		if( outArray[i] == '\u0000' )
    		{ 
    			out += "." ;
    		}
    		else
    		{
    	    	out += outArray[i] ;
    		}
    	}
    	
        return out ;
    }
    
    String apcsaDecryptMod2( String input, int key )
    {
        String[] groups = new String[key] ;
        int sizeOfLargestGroup = buildGroups( input, groups );
        String output = buildOutput( groups, sizeOfLargestGroup ) ;
        return output ;
    }
    
    /**
     * This method divides the input into groups of characters.  Each group is
     * a cluster that were placed together by the encryptor based on the modulus 
     * of their original character index.
     * 
     * There are two types of groups Big and Small.  Big groups have 1 more character
     * than small groups.
     * 
     * Example of created groups...
     * 
     * Input:
     *  "0123456789"
     *  
     * Output Groups:
     *  "048"  (Big)
     *  "159"  (Big)
     *  "26"   (Small)
     *  "37"   (Small)
     * 
     * @param input Characters to be decoded
     * @param groups Output character array
     * @return Size of the largest group.
     */
    private int buildGroups( String input, String[] groups )
    {
        int numGroups = groups.length ;
        int numChars = input.length() ;
        int smallGroupSize = numChars/numGroups ;
        int bigGroupSize = smallGroupSize + 1 ;
        int numBigGroups = numChars % numGroups ;
        int numSmallGroups = numGroups - numBigGroups ;
        
        
        System.err.println( "input="+input );
        System.err.println( "numChars="+numChars );
        System.err.println( "numGroups="+numGroups );
        System.err.println( "bigGroupSize="+bigGroupSize );
        System.err.println( "smallGroupSize="+smallGroupSize );
        System.err.println( "numBigGroups="+numBigGroups );
        System.err.println( "numSmallGroups="+numSmallGroups );
        System.err.println( "===================" );
         
        // Extract the big groups.
        System.err.println( "BIG" );
        String[] bigGroups = new String[numBigGroups] ;
        int numBigGroupChars = numBigGroups * bigGroupSize ;
        String bigGroupChars = input.substring( 0, numBigGroupChars ) ;
        extractGroups( bigGroupChars, bigGroupSize, numBigGroups, bigGroups ) ;
        
        // Extract the small groups.
        System.err.println( "SMALL" );
        String[] smallGroups = new String[numSmallGroups] ;
        String smallGroupChars = input.substring( numBigGroupChars ) ;
        extractGroups( smallGroupChars, smallGroupSize, numSmallGroups, smallGroups ) ;
        
        // Move the big and small groups into a single array.
        int groupIndex = 0 ;
        for( int i = 0 ; i < numBigGroups ; i++ )
        {
            groups[groupIndex] = bigGroups[i] ;
            groupIndex++ ;
        }
        for( int i = 0 ; i < numSmallGroups ; i++ )
        {
            groups[groupIndex] = smallGroups[i] ;
            groupIndex++ ;
        }
        
        // Determine largest group size.  Either Big or Small.
        int groupSize = bigGroupSize;
        if( numBigGroups == 0 )
        {
            groupSize = smallGroupSize ;
        }
        return groupSize ;
    }
    
    /**
     * Method to extract groups of text from the input.
     * 
     * @param input The string to extract groups from
     * @param groupSize The number of characters in each extracted group.
     * @param numGroups The number of groups to extract.
     * @param groups This is the array to put the groups in.
     */
    private void extractGroups( String input, int groupSize, int numGroups, String[] groups )
    {
        int startingIndex = 0 ;
        int endIndex = startingIndex + groupSize ;
        for( int i = 0 ; i < numGroups ; i++ )
        {
            String group = input.substring( startingIndex, endIndex );
            groups[i] = group;
            System.err.println( "  " + group );
            startingIndex = endIndex ;
            endIndex = startingIndex + groupSize ;
        }
    }
    
    /**
     * This method takes the groups of characters and puts them together into a
     * single string.
     * 
     * @param groups Array of groups to step through to pull out characters.
     * @param sizeOfGroups The number of characters in each group.
     * @return The decrypted string.
     */
    private String buildOutput( String[] groups, int sizeOfGroups )
    {
        String output = "" ;
        
        // Each group has a number of characters.  This loop will step through 
        // each character in the group.
        for( int j = 0 ; j < sizeOfGroups ; j++ )    
        {
            //System.err.println( "length = " + groups.length ) ;
            // This loop steps through each group.  Inside this loop we will
            // pull off one character from each group and push it on the 
            // output.
            for( int i = 0 ; i < groups.length ; i++ )
            {
                //System.err.println( "i = " + i ) ;
                // Grab the current group.
                String group = groups[i] ;
                
                // Only do this if there is a character at position j
                if( group.length() > j )
                {
                    String myChar = group.substring(j,j+1) ;
                    System.err.println( group + " " + j + " " + myChar ) ;
                    output += myChar ;
                    System.err.println( "-> " + output ) ;
                }
            }
        }
        
        return output ;
    }

    /**
     * @param args
     */
    public static void main(String[] args) 
    {
        new ModulusDecrypt() ;
    }

}
