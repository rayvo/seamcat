// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XmlScanner.java

package org.seamcat.presentation.xmleditor;

import java.io.IOException;
import javax.swing.text.Document;

// Referenced classes of package org.seamcat.presentation.xmleditor:
//            XmlInputReader, XmlInputStream

public class XmlScanner
{
    abstract class Scanner
    {

        public abstract int scan(XmlInputReader xmlinputreader)
            throws IOException;

        protected void finished()
        {
            finished = true;
        }

        public boolean isFinished()
        {
            return finished;
        }

        public void reset()
        {
            finished = false;
            token = -1;
        }

        public int getToken()
        {
            return token;
        }

        protected int token;
        private boolean finished;
        final XmlScanner this$0;

        Scanner()
        {
            this$0 = XmlScanner.this;
            super();
            token = -1;
            finished = false;
        }
    }

    private class AttributeScanner extends Scanner
    {

        public int scan(XmlInputReader in)
            throws IOException
        {
            int character = in.getLastChar();
            do
            {
                do
                    while(mode == 0) 
                    {
                        switch(character)
                        {
                        case -1: 
                            finished();
                            return 5;

                        case 120: // 'x'
                            if(firstTime)
                            {
                                character = in.read();
                                if(character == 109)
                                {
                                    character = in.read();
                                    if(character == 108)
                                    {
                                        character = in.read();
                                        if(character == 110)
                                        {
                                            character = in.read();
                                            if(character == 115)
                                            {
                                                skipWhitespace();
                                                character = in.getLastChar();
                                                if(character == 58 || character == 61)
                                                    isNamespace = true;
                                            }
                                        }
                                    }
                                }
                            } else
                            {
                                character = in.read();
                            }
                            break;

                        case 58: // ':'
                            if(hasPrefix)
                            {
                                character = in.read();
                                return 20;
                            }
                            if(isNamespace)
                            {
                                hasPrefix = true;
                                return 10;
                            } else
                            {
                                hasPrefix = true;
                                return 6;
                            }

                        case 62: // '>'
                            finished();
                            return 20;

                        case 61: // '='
                            mode = 1;
                            if(isNamespace && hasPrefix)
                                return 11;
                            return !isNamespace ? 5 : 10;

                        default:
                            character = in.read();
                            break;
                        }
                        firstTime = false;
                    }
                while(mode != 1);
                switch(character)
                {
                case -1: 
                    return -1;

                case 61: // '='
                    character = in.read();
                    return 20;

                case 34: // '"'
                case 39: // '\''
                    scanString(character);
                    skipWhitespace();
                    if(isNamespace)
                    {
                        reset();
                        return 12;
                    } else
                    {
                        reset();
                        return 7;
                    }

                case 62: // '>'
                    character = in.read();
                    finished();
                    return 20;
                }
                character = in.read();
            } while(true);
        }

        public void reset()
        {
            super.reset();
            mode = 0;
            hasPrefix = false;
            firstTime = true;
            isNamespace = false;
        }

        private final int NAME = 0;
        private final int VALUE = 1;
        private final int END = 2;
        private int mode;
        private boolean hasPrefix;
        private boolean firstTime;
        private boolean isNamespace;
        final XmlScanner this$0;

        private AttributeScanner()
        {
            this$0 = XmlScanner.this;
            super();
            mode = 0;
            hasPrefix = false;
            firstTime = true;
            isNamespace = false;
        }

    }

    private class ElementNameScanner extends Scanner
    {

        public int scan(XmlInputReader in)
            throws IOException
        {
            int character = in.getLastChar();
            do
                switch(character)
                {
                case -1: 
                    finished();
                    return 1;

                case 58: // ':'
                    if(hasPrefix)
                    {
                        character = in.read();
                        return 20;
                    } else
                    {
                        hasPrefix = true;
                        return 2;
                    }

                case 47: // '/'
                    if(emptyElement)
                    {
                        character = in.read();
                    } else
                    {
                        emptyElement = true;
                        return 1;
                    }
                    // fall through

                case 62: // '>'
                    finished();
                    return !emptyElement ? 1 : 20;

                case 10: // '\n'
                case 13: // '\r'
                case 32: // ' '
                    skipWhitespace();
                    finished();
                    return 1;

                default:
                    character = in.read();
                    break;
                }
            while(true);
        }

        public void reset()
        {
            super.reset();
            emptyElement = false;
            hasPrefix = false;
        }

        private boolean hasPrefix;
        private boolean emptyElement;
        final XmlScanner this$0;

        private ElementNameScanner()
        {
            this$0 = XmlScanner.this;
            super();
            hasPrefix = false;
            emptyElement = false;
        }

    }

    private class ElementStartTagScanner extends Scanner
    {

        public int scan(XmlInputReader in)
            throws IOException
        {
            int token = 0;
            if(scanner == null)
            {
                scanner = ELEMENT_NAME_SCANNER;
                scanner.reset();
                token = scanner.scan(in);
            } else
            {
                token = scanner.scan(in);
            }
            if(scanner.isFinished())
                if(scanner instanceof ElementNameScanner)
                {
                    scanner = ATTRIBUTE_SCANNER;
                    scanner.reset();
                } else
                {
                    finished();
                }
            return token;
        }

        public void reset()
        {
            super.reset();
            scanner = null;
        }

        private Scanner scanner;
        final XmlScanner this$0;

        private ElementStartTagScanner()
        {
            this$0 = XmlScanner.this;
            super();
            scanner = null;
        }

    }

    private class ElementEndTagScanner extends Scanner
    {

        public int scan(XmlInputReader in)
            throws IOException
        {
            if(scanner == null)
            {
                scanner = ELEMENT_NAME_SCANNER;
                scanner.reset();
            }
            int token = scanner.scan(in);
            if(scanner.isFinished())
                finished();
            return token;
        }

        public void reset()
        {
            super.reset();
            scanner = null;
        }

        private Scanner scanner;
        final XmlScanner this$0;

        private ElementEndTagScanner()
        {
            this$0 = XmlScanner.this;
            super();
            scanner = null;
        }

    }

    private class CommentScanner extends Scanner
    {

        public int scan(XmlInputReader in)
            throws IOException
        {
            int character = in.read();
            do
                switch(character)
                {
                case -1: 
                    finished();
                    return 16;

                case 45: // '-'
                    character = in.read();
                    if(character == 45)
                    {
                        character = in.read();
                        if(character == 62)
                        {
                            finished();
                            return 16;
                        }
                    }
                    break;

                default:
                    character = in.read();
                    break;
                }
            while(true);
        }

        public void reset()
        {
            super.reset();
        }

        final XmlScanner this$0;

        private CommentScanner()
        {
            this$0 = XmlScanner.this;
            super();
        }

    }

    private class EntityTagScanner extends Scanner
    {

        public int scan(XmlInputReader in)
            throws IOException
        {
            int character = in.read();
            do
            {
                switch(character)
                {
                case -1: 
                    finished();
                    return 15;

                case 62: // '>'
                    finished();
                    return 15;
                }
                character = in.read();
            } while(true);
        }

        public void reset()
        {
            super.reset();
        }

        final XmlScanner this$0;

        private EntityTagScanner()
        {
            this$0 = XmlScanner.this;
            super();
        }

    }

    private class TagScanner extends Scanner
    {

        public int scan(XmlInputReader in)
            throws IOException
        {
            if(scanner != null)
            {
                int token = scanner.scan(in);
                if(scanner.isFinished())
                    scanner = null;
                return token;
            }
            int character = in.getLastChar();
            if(character == 33)
            {
                character = in.read();
                if(character == 45)
                {
                    character = in.read();
                    if(character == 45)
                        scanner = COMMENT_SCANNER;
                }
                if(scanner == null)
                    scanner = ENTITY_TAG_SCANNER;
                scanner.reset();
                return 20;
            }
            if(character == 63)
            {
                character = in.read();
                scanner = ENTITY_TAG_SCANNER;
                scanner.reset();
                return 20;
            }
            if(character == 47)
            {
                character = in.read();
                scanner = ELEMENT_END_TAG_SCANNER;
                scanner.reset();
                return 20;
            }
            if(character == 62)
            {
                character = in.read();
                finished();
                return 20;
            } else
            {
                scanner = ELEMENT_START_TAG_SCANNER;
                scanner.reset();
                return 20;
            }
        }

        public void reset()
        {
            super.reset();
            scanner = null;
        }

        private Scanner scanner;
        final XmlScanner this$0;

        private TagScanner()
        {
            this$0 = XmlScanner.this;
            super();
            scanner = null;
        }

    }


    public XmlScanner(Document document)
        throws IOException
    {
        tagScanner = null;
        start = 0;
        in = null;
        token = -1;
        pos = 0L;
        try
        {
            in = new XmlInputReader(new XmlInputStream(document));
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        in.read();
        scan();
    }

    public void setRange(int start, int end)
        throws IOException
    {
        in.setRange(start, end);
        this.start = start;
        token = -1;
        pos = 0L;
        tagScanner = null;
        in.read();
        scan();
    }

    public final int getStartOffset()
    {
        int begOffs = (int)(pos & 0xffffffffL);
        return start + begOffs;
    }

    public final int getEndOffset()
    {
        int endOffs = (int)(in.pos & 0xffffffffL);
        return start + endOffs;
    }

    public long scan()
        throws IOException
    {
        long l = pos;
        if(tagScanner != null)
        {
            token = tagScanner.scan(in);
            if(tagScanner.isFinished())
                tagScanner = null;
            return l;
        }
        pos = in.pos;
        int ch = in.getLastChar();
        switch(ch)
        {
        case -1: 
            token = -1;
            return l;

        case 60: // '<'
            ch = in.read();
            tagScanner = TAG_SCANNER;
            tagScanner.reset();
            token = tagScanner.scan(in);
            return l;
        }
        scanValue();
        token = 3;
        return l;
    }

    private void scanValue()
        throws IOException
    {
        int ch = in.read();
        do
        {
            switch(ch)
            {
            case -1: 
                return;

            case 60: // '<'
                return;
            }
            ch = in.read();
        } while(true);
    }

    private void skipWhitespace()
        throws IOException
    {
        for(int ch = in.read(); Character.isWhitespace((char)ch); ch = in.read());
    }

    private void scanString(int end)
        throws IOException
    {
        for(int ch = in.read(); ch != end && ch != 62 && ch != -1; ch = in.read());
    }

    private Scanner tagScanner;
    private final AttributeScanner ATTRIBUTE_SCANNER = new AttributeScanner();
    private final ElementEndTagScanner ELEMENT_END_TAG_SCANNER = new ElementEndTagScanner();
    private final ElementStartTagScanner ELEMENT_START_TAG_SCANNER = new ElementStartTagScanner();
    private final ElementNameScanner ELEMENT_NAME_SCANNER = new ElementNameScanner();
    private final EntityTagScanner ENTITY_TAG_SCANNER = new EntityTagScanner();
    private final CommentScanner COMMENT_SCANNER = new CommentScanner();
    private final TagScanner TAG_SCANNER = new TagScanner();
    private int start;
    protected XmlInputReader in;
    public int token;
    public long pos;








}
