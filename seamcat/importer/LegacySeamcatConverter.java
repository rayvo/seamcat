// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   LegacySeamcatConverter.java

package org.seamcat.importer;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.seamcat.model.*;
import org.seamcat.model.core.InterferenceLink;
import org.seamcat.model.core.VictimSystemLink;

// Referenced classes of package org.seamcat.importer:
//            ImportedScenario, ImportedLibrary, LegacySeamcatTypes, ReceiverAction, 
//            LegacySeamcatReceiverActions, VictimLinkAction, LegacySeamcatVictimLinkActions, InterferingLinkAction, 
//            LegacySeamcatInterferingLinkActions, TransmitterAction, LegacySeamcatTransmitterActions, AntennaAction, 
//            LegacySeamcatAntennaActions

public class LegacySeamcatConverter
{
    private static final class ReadReceiverState extends State
    {

        public void parse(String line, BufferedReader reader)
            throws IOException
        {
            if(receiver != null)
            {
                String param = LegacySeamcatTypes.getParamname(line).toUpperCase();
                ReceiverAction action = (ReceiverAction)actionHandler.get(param);
                if(action != null)
                    action.processLine(line, reader, receiver);
                else
                    LegacySeamcatConverter.LOG.warn((new StringBuilder()).append("No action for line [").append(line).append("]").toString());
            } else
            {
                throw new IllegalStateException("A receiver parsing session has not been started");
            }
        }

        public void begin()
        {
            if(receiver == null)
                receiver = new Receiver();
            else
                throw new IllegalStateException("A receiver parsing session has has already been started");
        }

        public void end()
        {
            if(receiver != null)
            {
                content.add(receiver);
                receiver = null;
            } else
            {
                throw new IllegalStateException("A receiver parsing session has not been started");
            }
        }

        public Object[] getContent()
        {
            if(receiver != null)
                throw new IllegalStateException("A receiver parsing session is still in progress");
            else
                return content.toArray(new Receiver[content.size()]);
        }

        private static final Map actionHandler;
        private Receiver receiver;

        static 
        {
            actionHandler = LegacySeamcatReceiverActions.RECEIVER_ACTIONS;
        }

        private ReadReceiverState()
        {
            receiver = null;
        }

    }

    private static final class ReadVictimLinkState extends State
    {

        public void parse(String line, BufferedReader reader)
            throws IOException
        {
            if(vlk != null)
            {
                String param = LegacySeamcatTypes.getParamname(line).toUpperCase();
                VictimLinkAction action = (VictimLinkAction)actionHandler.get(param);
                if(action != null)
                    action.processLine(line, reader, vlk);
                else
                    LegacySeamcatConverter.LOG.warn((new StringBuilder()).append("No action for line [").append(line).append("]").toString());
            } else
            {
                throw new IllegalStateException("A victimlink parsing session has not been started");
            }
        }

        public void begin()
        {
            if(content.size() == 0)
            {
                if(vlk == null)
                    vlk = new VictimSystemLink();
                else
                    throw new IllegalStateException("A victimlink parsing session has has already been started");
            } else
            {
                throw new IllegalStateException("Trying to read more than one victim link into content");
            }
        }

        public void end()
        {
            if(vlk != null)
            {
                content.add(vlk);
                vlk = null;
            } else
            {
                throw new IllegalStateException("A victimlink parsing session has not been started");
            }
        }

        public Object[] getContent()
        {
            if(vlk != null)
                throw new IllegalStateException("A victimlink parsing session is still in progress");
            else
                return content.toArray(new VictimSystemLink[content.size()]);
        }

        private static final Map actionHandler;
        private VictimSystemLink vlk;

        static 
        {
            actionHandler = LegacySeamcatVictimLinkActions.VLK_ACTIONS;
        }

        private ReadVictimLinkState()
        {
            vlk = null;
        }

    }

    private static final class ReadInterferingLinkState extends State
    {

        public void parse(String line, BufferedReader reader)
            throws IOException
        {
            if(il != null)
            {
                String param = LegacySeamcatTypes.getParamname(line).toUpperCase();
                InterferingLinkAction action = (InterferingLinkAction)actionHandler.get(param);
                if(action != null)
                    action.processLine(line, reader, il);
                else
                    LegacySeamcatConverter.LOG.warn((new StringBuilder()).append("No action for line [").append(line).append("]").toString());
            } else
            {
                throw new IllegalStateException("A victimlink parsing session has not been started");
            }
        }

        public void begin()
        {
            if(il == null)
                il = new InterferenceLink(new String(), vlk);
            else
                throw new IllegalStateException("A victimlink parsing session has has already been started");
        }

        public void end()
        {
            if(il != null)
            {
                content.add(il);
                il = null;
            } else
            {
                throw new IllegalStateException("A victimlink parsing session has not been started");
            }
        }

        public Object[] getContent()
        {
            if(il != null)
                throw new IllegalStateException("An interfering link parsing session is still in progress");
            else
                return content.toArray(new InterferenceLink[content.size()]);
        }

        private static final Map actionHandler;
        private InterferenceLink il;
        private final VictimSystemLink vlk;

        static 
        {
            actionHandler = LegacySeamcatInterferingLinkActions.INTERFERING_LINK_ACTIONS;
        }

        ReadInterferingLinkState(VictimSystemLink vlk)
        {
            il = null;
            this.vlk = vlk;
        }
    }

    private static final class GarbageState extends State
    {

        public final void parse(String s, BufferedReader bufferedreader)
        {
        }

        public final void begin()
        {
        }

        public final void end()
        {
        }

        public final Object[] getContent()
        {
            throw new UnsupportedOperationException("Can't get content from GarbageState");
        }

        private GarbageState()
        {
        }

    }

    private static final class ReadTransmitterState extends State
    {

        public final void parse(String line, BufferedReader reader)
            throws IOException
        {
            if(transmitter != null)
            {
                String param = LegacySeamcatTypes.getParamname(line).toUpperCase();
                TransmitterAction action = (TransmitterAction)actionHandler.get(param);
                if(action != null)
                    action.processLine(line, reader, transmitter);
                else
                    LegacySeamcatConverter.LOG.warn((new StringBuilder()).append("No action for line [").append(line).append("]").toString());
            } else
            {
                throw new IllegalStateException("A transmitter parsing session has not been started");
            }
        }

        public final void begin()
        {
            if(transmitter == null)
                transmitter = new Transmitter();
            else
                throw new IllegalStateException("A transmitter parsing session has has already been started");
        }

        public final void end()
        {
            if(transmitter != null)
            {
                content.add(transmitter);
                transmitter = null;
            } else
            {
                throw new IllegalStateException("A transmitter parsing session has not been started");
            }
        }

        public final Object[] getContent()
        {
            if(transmitter != null)
                throw new IllegalStateException("A transmitter parsing session is still in progress");
            else
                return content.toArray(new Transmitter[content.size()]);
        }

        private static final Map actionHandler;
        private Transmitter transmitter;

        static 
        {
            actionHandler = LegacySeamcatTransmitterActions.TRANSMITTER_ACTIONS;
        }

        private ReadTransmitterState()
        {
            transmitter = null;
        }

    }

    private static final class ReadAntennaState extends State
    {

        public final void parse(String line, BufferedReader reader)
            throws IOException
        {
            if(antenna != null)
            {
                String param = LegacySeamcatTypes.getParamname(line).toUpperCase();
                AntennaAction action = (AntennaAction)actionHandler.get(param);
                if(action != null)
                    action.processLine(line, reader, antenna);
                else
                    LegacySeamcatConverter.LOG.warn((new StringBuilder()).append("No action for line <").append(line).append(">").toString());
            } else
            {
                throw new IllegalStateException("An antenna parsing session has not been started");
            }
        }

        public final void begin()
        {
            if(antenna == null)
                antenna = new Antenna();
            else
                throw new IllegalStateException("An antenna parsing session has has already been started");
        }

        public final void end()
        {
            if(antenna != null)
            {
                content.add(antenna);
                antenna = null;
            } else
            {
                throw new IllegalStateException("An antenna parsing session has not been started");
            }
        }

        public final Object[] getContent()
        {
            if(antenna != null)
                throw new IllegalStateException("An antenna parsing session is still in progress");
            else
                return content.toArray(new Antenna[content.size()]);
        }

        private static final Map actionHandler;
        private Antenna antenna;

        static 
        {
            actionHandler = LegacySeamcatAntennaActions.ANTENNA_ACTIONS;
        }

        private ReadAntennaState()
        {
            antenna = null;
        }

    }

    private static abstract class State
    {

        public abstract void parse(String s, BufferedReader bufferedreader)
            throws IOException;

        public abstract void begin();

        public abstract void end();

        public int size()
        {
            return content.size();
        }

        public abstract Object[] getContent();

        protected List content;

        private State()
        {
            content = new ArrayList();
        }

    }


    private LegacySeamcatConverter()
    {
    }

    public static ImportedScenario importScenario(BufferedReader reader)
    {
        ImportedScenario scenario;
        try
        {
            StringBuffer file = new StringBuffer();
            for(; reader.ready(); file.append((new StringBuilder()).append(reader.readLine()).append("\n").toString()));
            reader.close();
            reader = new BufferedReader(new StringReader(file.toString()));
            LOG.debug((new StringBuilder()).append("Read ").append(file.length()).append(" characters").toString());
            State READ_VICTIMLINK = new ReadVictimLinkState();
            State GARBAGE = new GarbageState();
            State state = GARBAGE;
            lineno = 0;
            do
            {
                String line;
                if((line = reader.readLine()) == null)
                    break;
                lineno++;
                String param = LegacySeamcatTypes.getParamname(line);
                if(param != null)
                    if(param.equals("ENTITY"))
                    {
                        String data = LegacySeamcatTypes.toString(line).toUpperCase();
                        LOG.debug((new StringBuilder()).append("Data <").append(data).append(">").toString());
                        Matcher matcher = VICTIMLINK_ENTITY.matcher(data);
                        matcher.find();
                        if(matcher.matches())
                        {
                            state.end();
                            if(!state.equals(READ_VICTIMLINK))
                                state = READ_VICTIMLINK;
                            LOG.debug("Loading victim link");
                            state.begin();
                        }
                        matcher = INTERFERINGLINK_ENTITY.matcher(data);
                        matcher.find();
                        if(matcher.matches())
                        {
                            state.end();
                            if(!state.equals(GARBAGE))
                                state = GARBAGE;
                        }
                    } else
                    if(!param.equals("SECTION"))
                        try
                        {
                            state.parse(line, reader);
                        }
                        catch(Exception e)
                        {
                            LOG.warn((new StringBuilder()).append("Unable to process line ").append(lineno).append(" [").append(line).append("]").toString());
                        }
            } while(true);
            state.end();
            Object _vlk[] = READ_VICTIMLINK.getContent();
            if(_vlk.length > 0)
            {
                state = GARBAGE;
                reader = new BufferedReader(new StringReader(file.toString()));
                State READ_INTERFERINGLINK = new ReadInterferingLinkState((VictimSystemLink)READ_VICTIMLINK.getContent()[0]);
                do
                {
                    String line;
                    if((line = reader.readLine()) == null)
                        break;
                    String param = LegacySeamcatTypes.getParamname(line);
                    if(param != null)
                        if(param.equals("ENTITY"))
                        {
                            String data = LegacySeamcatTypes.toString(line).toUpperCase();
                            LOG.debug((new StringBuilder()).append("Data <").append(data).append(">").toString());
                            Matcher matcher = INTERFERINGLINK_ENTITY.matcher(data);
                            matcher.find();
                            if(matcher.matches())
                            {
                                state.end();
                                if(!state.equals(READ_INTERFERINGLINK))
                                    state = READ_INTERFERINGLINK;
                                LOG.debug("Loading interfering link");
                                state.begin();
                            }
                            matcher = VICTIMLINK_ENTITY.matcher(data);
                            matcher.find();
                            if(matcher.matches())
                            {
                                state.end();
                                if(!state.equals(GARBAGE))
                                    state = GARBAGE;
                            }
                        } else
                        if(!param.equals("SECTION"))
                            try
                            {
                                state.parse(line, reader);
                            }
                            catch(Exception e)
                            {
                                LOG.warn((new StringBuilder()).append("Unable to process line ").append(lineno).append(" [").append(line).append("]").toString());
                            }
                } while(true);
                state.end();
                scenario = new ImportedScenario();
                scenario.setVictimSystemLink((VictimSystemLink)READ_VICTIMLINK.getContent()[0]);
                scenario.setInterferenceLinks((InterferenceLink[])(InterferenceLink[])READ_INTERFERINGLINK.getContent());
            } else
            {
                throw new IllegalArgumentException("No victimlink found");
            }
        }
        catch(Exception e)
        {
            scenario = null;
            LOG.debug((new StringBuilder()).append("LegacySeamcatConverter import of scenario failed <").append(e.getMessage()).append(">").toString());
            e.printStackTrace();
        }
        LOG.info((new StringBuilder()).append("Read ").append(scenario.getVictimSystemLink() == null ? "0" : "1").append(" victimlink(s) and ").append(scenario.getInterferenceLinks().length).append(" interfering links").toString());
        return scenario;
    }

    public static ImportedLibrary importLibrary(BufferedReader reader)
    {
        ImportedLibrary library;
        try
        {
            State READ_ANTENNA = new ReadAntennaState();
            State READ_RECEIVER = new ReadReceiverState();
            State READ_TRANSMITTER = new ReadTransmitterState();
            State GARBAGE = new GarbageState();
            State state = GARBAGE;
            lineno = 0;
            do
            {
                if(!reader.ready())
                    break;
                String line = reader.readLine();
                lineno++;
                String param = LegacySeamcatTypes.getParamname(line);
                LOG.debug((new StringBuilder()).append("Processing line <").append(line).append(">").toString());
                if(param != null)
                    if(param.equals("ENTITY"))
                    {
                        String data = LegacySeamcatTypes.toString(line).toUpperCase();
                        LOG.debug((new StringBuilder()).append("Data <").append(data).append(">").toString());
                        Matcher matcher = ANTENNA_ENTITY.matcher(data);
                        matcher.find();
                        if(matcher.matches())
                        {
                            state.end();
                            if(!state.equals(READ_ANTENNA))
                                state = READ_ANTENNA;
                            LOG.debug("Loading antenna");
                            state.begin();
                        }
                        matcher = RECEIVER_ENTITY.matcher(data);
                        matcher.find();
                        if(matcher.matches())
                        {
                            state.end();
                            if(!state.equals(READ_RECEIVER))
                                state = READ_RECEIVER;
                            LOG.debug("Loading receiver");
                            state.begin();
                        }
                        matcher = TRANSMITTER_ENTITY.matcher(data);
                        matcher.find();
                        if(matcher.matches())
                        {
                            state.end();
                            if(!state.equals(READ_TRANSMITTER))
                                state = READ_TRANSMITTER;
                            LOG.debug("Loading transmitter");
                            state.begin();
                        }
                        matcher = MODEL_ENTITY.matcher(data);
                        matcher.find();
                        if(matcher.matches())
                        {
                            state.end();
                            if(!state.equals(GARBAGE))
                                state = GARBAGE;
                            LOG.debug("Skipping user model");
                            state.begin();
                        }
                    } else
                    if(!param.equals("SECTION"))
                        try
                        {
                            state.parse(line, reader);
                        }
                        catch(Exception e)
                        {
                            LOG.warn((new StringBuilder()).append("Unable to process line ").append(lineno).append(" [").append(line).append("]").toString());
                        }
            } while(true);
            reader.close();
            LOG.debug((new StringBuilder()).append("LegacySeamcatConverter read <").append(lineno).append("> lines").toString());
            if(state != null)
                try
                {
                    state.end();
                }
                catch(IllegalStateException e) { }
            library = new ImportedLibrary();
            library.setAntennas((Antenna[])(Antenna[])READ_ANTENNA.getContent());
            library.setReceivers((Receiver[])(Receiver[])READ_RECEIVER.getContent());
            library.setTransmitters((Transmitter[])(Transmitter[])READ_TRANSMITTER.getContent());
            LOG.debug((new StringBuilder()).append("Convert count: Antennas=").append(library.getAntennas().length).append(", receivers=").append(library.getReceivers().length).append(", transmitters=").append(library.getTransmitters().length).toString());
        }
        catch(Exception e)
        {
            library = null;
            LOG.info((new StringBuilder()).append("LegacySeamcatConverter import of library failed <").append(e.getMessage()).append(">").toString());
            e.printStackTrace();
        }
        return library;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/importer/LegacySeamcatConverter);
    private static final Pattern ANTENNA_ENTITY = Pattern.compile("Antenna \\d+", 2);
    private static final Pattern RECEIVER_ENTITY = Pattern.compile("Receiver \\d+", 2);
    private static final Pattern TRANSMITTER_ENTITY = Pattern.compile("Transmitter \\d+", 2);
    private static final Pattern MODEL_ENTITY = Pattern.compile("Model \\d+", 2);
    private static final Pattern VICTIMLINK_ENTITY = Pattern.compile("Victim Link", 2);
    private static final Pattern INTERFERINGLINK_ENTITY = Pattern.compile("Interfering Link \\d+", 2);
    private static final String ENTITY = "ENTITY";
    private static final String SECTION = "SECTION";
    public static int lineno = 0;


}