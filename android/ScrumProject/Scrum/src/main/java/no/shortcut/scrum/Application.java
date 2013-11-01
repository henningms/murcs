package no.shortcut.scrum;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

/**
 * Created by HenningMosand on 31.10.13.
 */
public class Application
{
    public static MobileServiceClient mClient;

    public static MobileServiceClient getClient()
    {
        return mClient;
    }

}
