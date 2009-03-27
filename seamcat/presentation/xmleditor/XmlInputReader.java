// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XmlInputReader.java

package org.seamcat.presentation.xmleditor;

import java.io.*;

// Referenced classes of package org.seamcat.presentation.xmleditor:
//            XmlInputStream

public class XmlInputReader extends FilterReader
{

    public XmlInputReader(XmlInputStream inputstream)
        throws UnsupportedEncodingException
    {
        super(new InputStreamReader(inputstream));
        stream = null;
        pos = 0L;
        chpos = 0x100000000L;
        pushBack = -1;
        lastChar = -1;
        currentIndex = 0;
        numChars = 0;
        stream = inputstream;
    }

    public void setRange(int start, int end)
    {
        stream.setRange(start, end);
        pos = 0L;
        chpos = 0x100000000L;
        pushBack = -1;
        lastChar = -1;
        currentIndex = 0;
        numChars = 0;
    }

    public int read()
        throws IOException
    {
        lastChar = readInternal();
        return lastChar;
    }

    public int getLastChar()
    {
        return lastChar;
    }

    private int readInternal()
        throws IOException
    {
        int i;
label0:
        {
            pos = chpos;
            chpos++;
            i = pushBack;
            if(i == -1)
            {
                if(currentIndex >= numChars)
                {
                    numChars = in.read(buffer);
                    if(numChars == -1)
                    {
                        i = -1;
                        break label0;
                    }
                    currentIndex = 0;
                }
                i = buffer[currentIndex++];
            } else
            {
                pushBack = -1;
            }
        }
        switch(i)
        {
        case -2: 
            return 92;

        case 92: // '\\'
            if((i = getNextChar()) != 117)
            {
                pushBack = i == 92 ? -2 : i;
                return 92;
            }
            for(chpos++; (i = getNextChar()) == 117; chpos++);
            int j = 0;
            for(int k = 0; k < 4;)
            {
                switch(i)
                {
                case 48: // '0'
                case 49: // '1'
                case 50: // '2'
                case 51: // '3'
                case 52: // '4'
                case 53: // '5'
                case 54: // '6'
                case 55: // '7'
                case 56: // '8'
                case 57: // '9'
                    j = ((j << 4) + i) - 48;
                    break;

                case 97: // 'a'
                case 98: // 'b'
                case 99: // 'c'
                case 100: // 'd'
                case 101: // 'e'
                case 102: // 'f'
                    j = ((j << 4) + 10 + i) - 97;
                    break;

                case 65: // 'A'
                case 66: // 'B'
                case 67: // 'C'
                case 68: // 'D'
                case 69: // 'E'
                case 70: // 'F'
                    j = ((j << 4) + 10 + i) - 65;
                    break;

                case 58: // ':'
                case 59: // ';'
                case 60: // '<'
                case 61: // '='
                case 62: // '>'
                case 63: // '?'
                case 64: // '@'
                case 71: // 'G'
                case 72: // 'H'
                case 73: // 'I'
                case 74: // 'J'
                case 75: // 'K'
                case 76: // 'L'
                case 77: // 'M'
                case 78: // 'N'
                case 79: // 'O'
                case 80: // 'P'
                case 81: // 'Q'
                case 82: // 'R'
                case 83: // 'S'
                case 84: // 'T'
                case 85: // 'U'
                case 86: // 'V'
                case 87: // 'W'
                case 88: // 'X'
                case 89: // 'Y'
                case 90: // 'Z'
                case 91: // '['
                case 92: // '\\'
                case 93: // ']'
                case 94: // '^'
                case 95: // '_'
                case 96: // '`'
                default:
                    System.err.println((new StringBuilder()).append("Error [").append(pos).append("]: invalid escape character!").toString());
                    pushBack = i;
                    return j;
                }
                k++;
                chpos++;
                i = getNextChar();
            }

            pushBack = i;
            switch(j)
            {
            case 10: // '\n'
                chpos += 0x100000000L;
                return 10;

            case 13: // '\r'
                if((i = getNextChar()) != 10)
                    pushBack = i;
                else
                    chpos++;
                chpos += 0x100000000L;
                return 10;
            }
            return j;

        case 10: // '\n'
            chpos += 0x100000000L;
            return 10;

        case 13: // '\r'
            if((i = getNextChar()) != 10)
                pushBack = i;
            else
                chpos++;
            chpos += 0x100000000L;
            return 10;
        }
        return i;
    }

    private int getNextChar()
        throws IOException
    {
        if(currentIndex >= numChars)
        {
            numChars = in.read(buffer);
            if(numChars == -1)
                return -1;
            currentIndex = 0;
        }
        return buffer[currentIndex++];
    }

    private static final int BUFFERLEN = 10240;
    private final char buffer[] = new char[10240];
    private XmlInputStream stream;
    long pos;
    private long chpos;
    private int pushBack;
    private int lastChar;
    private int currentIndex;
    private int numChars;
}
