package com.dawidweiss.fsa;

/**
 * This class has several static utility methods for use with the FSA package.
 *
 * @author Dawid Weiss
 */
public final class FSAHelpers
{

    /** Prevent instantiation. */
    private FSAHelpers() {}


    /**
     * Converts an integer with FSA flags to a human-readable string.
     */
    public static String flagsToString( int flags )
    {
        final int    [] aflags = {
            FSA.FSA_FLEXIBLE, FSA.FSA_STOPBIT, FSA.FSA_NEXTBIT, FSA.FSA_TAILS,
            FSA.FSA_WEIGHTED, FSA.FSA_LARGE_DICTIONARIES };

        final String [] sflags =  {
            "FLEXIBLE",   "STOPBIT",   "NEXTBIT",   "TAILS",
            "WEIGHTED",   "LARGE_DICTIONARIES" };

        StringBuffer res = new StringBuffer();

        for (int i=0;i<aflags.length;i++)
        {
            if ((flags & aflags[i])!=0)
            {
                flags = flags & ~aflags[i];
                if (res.length()>0)
                    res.append(',');
                res.append( sflags[i] );
            }
        }

        if (flags != 0)
        {
            if (res.length() > 0)
                res.append(' ');
            res.append("(Some flags were not recognized: " + Integer.toBinaryString(flags) + ")");
        }

        return res.toString();
    }


    /**
     * Returns a version number for a set of flags.
     * In the future, this method should throw an exception if an illegal combination
     * of flags is used.
     */
    protected static byte getVersion( int flags )
    {
        byte version;

        if ( (flags & FSA.FSA_FLEXIBLE) != 0)
        {
            if ((flags & FSA.FSA_STOPBIT) != 0)
            {
                if ((flags & FSA.FSA_NEXTBIT) != 0)
                {
                    if ((flags & FSA.FSA_TAILS) != 0)
                    {
                        version = 7;
                    }
                    else
                    {
                        if ((flags & FSA.FSA_WEIGHTED) != 0)
                            version = 8;
                        else
                            version = 5;
                    }
                }
                else
                {
                    if ((flags & FSA.FSA_TAILS) != 0)
                        version = 6;
                    else
                        version = 4;
                }
            }
            else
            {
                if ((flags & FSA.FSA_NEXTBIT) != 0)
                    version = 2;
                else
                    version = 1;
            }
        }
        else
        {
            if ((flags & FSA.FSA_LARGE_DICTIONARIES) != 0)
                version = (byte) 0x80;
            else
                version = 0;
        }

        return version;
    }


    /**
     * Returns flags integer for a given version number.
     * @throws RuntimeException if the version number is not recognized.
     */
    protected static int getFlags(int version)
    {
        int flags;

        switch (version)
        {
            case 0:     flags = 0; break;
            case (byte) 0x80:  flags = FSA.FSA_LARGE_DICTIONARIES; break;
            case 1:     flags = FSA.FSA_FLEXIBLE; break;
            case 2:     flags = FSA.FSA_FLEXIBLE |                   FSA.FSA_NEXTBIT; break;
            case 4:     flags = FSA.FSA_FLEXIBLE | FSA.FSA_STOPBIT; break;
            case 5:     flags = FSA.FSA_FLEXIBLE | FSA.FSA_STOPBIT | FSA.FSA_NEXTBIT; break;
            case 6:     flags = FSA.FSA_FLEXIBLE | FSA.FSA_STOPBIT |                   FSA.FSA_TAILS; break;
            case 7:     flags = FSA.FSA_FLEXIBLE | FSA.FSA_STOPBIT | FSA.FSA_NEXTBIT | FSA.FSA_TAILS; break;
            default:
                throw new RuntimeException("Unknown version number. FSA created with unknown options.");
        }

        return flags;
    }
}