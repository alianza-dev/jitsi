package net.java.sip.communicator.impl.libjitsi;

import org.jitsi.service.audionotifier.AudioNotifierService;
import org.jitsi.service.configuration.ConfigurationService;
import org.jitsi.service.libjitsi.LibJitsi;

/**
 * Created by msaavedra on 1/9/17.
 */
public class LibJitsiAlzProvider {
    static {
        LibJitsi.start();
    }

    public static AudioNotifierService getAudioNotifierService()
    {
        return LibJitsi.getAudioNotifierService();
    }

    public static ConfigurationService getConfigurationService()
    {
        return LibJitsi.getConfigurationService();
    }

}
