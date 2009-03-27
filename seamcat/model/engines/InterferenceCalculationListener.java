// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InterferenceCalculationListener.java

package org.seamcat.model.engines;

import org.seamcat.function.Point2D;

public interface InterferenceCalculationListener
{

    public abstract void calculationStarted();

    public abstract void calculationComplete();

    public abstract boolean confirmContinueOnWarning(String s);

    public abstract void updateStatus(String s);

    public abstract void setCurrentProcessCompletionPercentage(int i);

    public abstract void incrementCurrentProcessCompletionPercentage(int i);

    public abstract void setTotalProcessCompletionPercentage(int i);

    public abstract void incrementTotalProcessCompletionPercentage(int i);

    public abstract void warningMessage(String s);

    public abstract void infoMessage(String s);

    public abstract void propabilityResult(double d);

    public abstract void addTranslationResult(Point2D point2d);
}
