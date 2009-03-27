// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMABalancingListener.java

package org.seamcat.cdma;


// Referenced classes of package org.seamcat.cdma:
//            UserTerminal

public interface CDMABalancingListener
{

    public abstract void balancingStarted();

    public abstract void balancingComplete();

    public abstract void voiceActiveUserAdded(UserTerminal userterminal);

    public abstract void voiceActiveUserDropped(UserTerminal userterminal);

    public abstract void voiceActiveUserNotActivated(UserTerminal userterminal);

    public abstract void voiceInActiveUserAdded();

    public abstract void voiceActiveUserIgnored();
}