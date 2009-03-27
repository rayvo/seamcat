// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ErrorCode.java

package org.seamcat.remote.util;


public class ErrorCode
{

    public ErrorCode()
    {
    }

    public static String getErrorDescription(int errNumber)
    {
        String errDescription = null;
        switch(errNumber)
        {
        case 1: // '\001'
            errDescription = serverErrorDescription;
            break;

        case 2: // '\002'
            errDescription = clientErrorDescription;
            break;

        case 3: // '\003'
            errDescription = userNameErrorDescription;
            break;

        case 4: // '\004'
            errDescription = loginFailErrorDescription;
            break;

        case 5: // '\005'
            errDescription = xmlVersionErrorDescription;
            break;

        case 6: // '\006'
            errDescription = workspaceErrorDescription;
            break;

        case 7: // '\007'
            errDescription = fileErrorDescription;
            break;

        case 8: // '\b'
            errDescription = requestTypeErrorDescription;
            break;

        case 9: // '\t'
            errDescription = getResultErrorDescription;
            break;

        case 10: // '\n'
            errDescription = findingJobIdErrorDescription;
            break;

        case 11: // '\013'
            errDescription = resultNotReadyErrorDescription;
            break;

        case 200: 
            errDescription = resultsDownloaded;
            break;
        }
        return errDescription;
    }

    public static final int OK = 0;
    public static final int RESULTS_DOWNLOADED = 200;
    public static final int ERR_SERVER_ERROR = 1;
    public static final int ERR_CLIENT_ERROR = 2;
    public static final int ERR_USERNAME = 3;
    public static final int ERR_LOGIN_FAIL = 4;
    public static final int ERR_XML_VERSION = 5;
    public static final int ERR_WORKSPACE = 6;
    public static final int ERR_FILE = 7;
    public static final int ERR_REQUEST_TYPE = 8;
    public static final int ERR_GETTING_RESULT = 9;
    public static final int ERR_FINDING_JOBID = 10;
    public static final int ERR_RESULT_NOT_READY = 11;
    private static String serverErrorDescription = "Sorry, server error. Please...";
    private static String clientErrorDescription = "Sorry, client error. Please...";
    private static String userNameErrorDescription = "Sorry, the username is not valid. Please verify that it is a valid email address.";
    private static String loginFailErrorDescription = "Sorry, username/password not valid. Please correct and try again.";
    private static String xmlVersionErrorDescription = "Sorry, the workspace XML version is not valid. Please update your SEAMCAT application and try again.";
    private static String workspaceErrorDescription = "Sorry, error found in the workspace XML. Please correct and try again.";
    private static String fileErrorDescription = "Sorry, error saving file. Please contact administrator.";
    private static String requestTypeErrorDescription = "Sorry, not valid request type.";
    private static String getResultErrorDescription = "Sorry, there was an error getting the result. Please contact administrator.";
    private static String findingJobIdErrorDescription = "Sorry, the requested job cant't be found at the server. Please....";
    private static String resultNotReadyErrorDescription = "Sorry, the requested job is not finished. You will recieve an email when the job is done.";
    private static String resultsDownloaded = "Results have been downloaded";

}
