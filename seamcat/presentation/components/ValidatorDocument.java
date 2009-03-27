// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ValidatorDocument.java

package org.seamcat.presentation.components;

import javax.swing.text.*;

public class ValidatorDocument extends PlainDocument
{
    public static final class Type extends Enum
    {

        public static Type[] values()
        {
            return (Type[])$VALUES.clone();
        }

        public static Type valueOf(String name)
        {
            return (Type)Enum.valueOf(org/seamcat/presentation/components/ValidatorDocument$Type, name);
        }

        public static final Type INTEGERS;
        public static final Type FLOAT_DELIMITERS;
        public static final Type CHARACTERS;
        public static final Type NEWLINE;
        public static final Type PUNCTUATION;
        public static final Type MATHS;
        public static final Type NEGATE;
        private static final Type $VALUES[];

        static 
        {
            INTEGERS = new Type("INTEGERS", 0);
            FLOAT_DELIMITERS = new Type("FLOAT_DELIMITERS", 1);
            CHARACTERS = new Type("CHARACTERS", 2);
            NEWLINE = new Type("NEWLINE", 3);
            PUNCTUATION = new Type("PUNCTUATION", 4);
            MATHS = new Type("MATHS", 5);
            NEGATE = new Type("NEGATE", 6);
            $VALUES = (new Type[] {
                INTEGERS, FLOAT_DELIMITERS, CHARACTERS, NEWLINE, PUNCTUATION, MATHS, NEGATE
            });
        }

        private Type(String s, int i)
        {
            super(s, i);
        }
    }


    transient ValidatorDocument(Type allowedTypes[])
    {
        overriding = false;
        this.allowedTypes = allowedTypes;
    }

    public void insertString(int offs, String str, AttributeSet a)
        throws BadLocationException
    {
        if(validateString(str))
            super.insertString(offs, str, a);
    }

    private boolean validateString(String s)
    {
        boolean ok = false;
        if(!overriding)
        {
            Type arr$[] = allowedTypes;
            int len$ = arr$.length;
            int i$ = 0;
            do
            {
                if(i$ >= len$)
                    break;
                Type type = arr$[i$];
                static class _cls1
                {

                    static final int $SwitchMap$org$seamcat$presentation$components$ValidatorDocument$Type[];

                    static 
                    {
                        $SwitchMap$org$seamcat$presentation$components$ValidatorDocument$Type = new int[Type.values().length];
                        try
                        {
                            $SwitchMap$org$seamcat$presentation$components$ValidatorDocument$Type[Type.INTEGERS.ordinal()] = 1;
                        }
                        catch(NoSuchFieldError ex) { }
                        try
                        {
                            $SwitchMap$org$seamcat$presentation$components$ValidatorDocument$Type[Type.FLOAT_DELIMITERS.ordinal()] = 2;
                        }
                        catch(NoSuchFieldError ex) { }
                        try
                        {
                            $SwitchMap$org$seamcat$presentation$components$ValidatorDocument$Type[Type.CHARACTERS.ordinal()] = 3;
                        }
                        catch(NoSuchFieldError ex) { }
                        try
                        {
                            $SwitchMap$org$seamcat$presentation$components$ValidatorDocument$Type[Type.NEWLINE.ordinal()] = 4;
                        }
                        catch(NoSuchFieldError ex) { }
                        try
                        {
                            $SwitchMap$org$seamcat$presentation$components$ValidatorDocument$Type[Type.PUNCTUATION.ordinal()] = 5;
                        }
                        catch(NoSuchFieldError ex) { }
                        try
                        {
                            $SwitchMap$org$seamcat$presentation$components$ValidatorDocument$Type[Type.MATHS.ordinal()] = 6;
                        }
                        catch(NoSuchFieldError ex) { }
                        try
                        {
                            $SwitchMap$org$seamcat$presentation$components$ValidatorDocument$Type[Type.NEGATE.ordinal()] = 7;
                        }
                        catch(NoSuchFieldError ex) { }
                    }
                }

                switch(_cls1..SwitchMap.org.seamcat.presentation.components.ValidatorDocument.Type[type.ordinal()])
                {
                case 1: // '\001'
                    ok = validateString(s, INTEGERS);
                    break;

                case 2: // '\002'
                    ok = validateString(s, FLOAT_DELIMITERS);
                    break;

                case 3: // '\003'
                    ok = validateString(s, CHARACTERS);
                    break;

                case 4: // '\004'
                    ok = validateString(s, NEWLINE);
                    break;

                case 5: // '\005'
                    ok = validateString(s, PUNCTUATION);
                    break;

                case 6: // '\006'
                    ok = validateString(s, MATHS);
                    break;

                case 7: // '\007'
                    ok = validateString(s, NEGATE);
                    break;

                default:
                    throw new IllegalStateException((new StringBuilder()).append("Unsupported validation type encountered: ").append(type).toString());
                }
                if(ok)
                    break;
                i$++;
            } while(true);
        } else
        {
            ok = true;
        }
        return ok;
    }

    private static boolean validateString(String s, char allowedChars[])
    {
label0:
        {
            if(s.length() <= 0)
                break label0;
            char chars[] = s.toCharArray();
            int x = 0;
label1:
            do
            {
label2:
                {
                    if(x >= chars.length)
                        break label1;
                    for(int y = 0; y < allowedChars.length; y++)
                        if(allowedChars[y] == chars[x])
                            break label2;

                    return false;
                }
                x++;
            } while(true);
            return true;
        }
        return false;
    }

    public boolean isOverriding()
    {
        return overriding;
    }

    public void setOverriding(boolean overriding)
    {
        this.overriding = overriding;
    }

    protected static final char INTEGERS[] = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };
    protected static final char FLOAT_DELIMITERS[] = {
        '.', ','
    };
    protected static final char CHARACTERS[] = {
        ' ', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 
        'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 
        't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 
        'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 
        'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 
        'X', 'Y', 'Z', '\346', '\306', '\370', '\330', '\345', '\305', '\344', 
        '\304', '\366', '\326', '\351', '\311', '\350', '\310', '\375', '\335', '\363', 
        '\323', '\362', '\322', '\341', '\301', '\340', '\300', '\361', '\321', '@'
    };
    protected static final char NEWLINE[] = {
        '\n'
    };
    protected static final char PUNCTUATION[] = {
        '.', ',', ';', ':', '!', '"', '#', '$', '%', '&', 
        '/', '(', ')', '?'
    };
    protected static final char MATHS[] = {
        '=', '+', '-', '*', '/', '^'
    };
    protected static final char NEGATE[] = {
        '-'
    };
    private boolean overriding;
    private final Type allowedTypes[];

}
