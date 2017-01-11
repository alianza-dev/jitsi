package net.java.sip.communicator.impl.libjitsi;

import org.jitsi.service.audionotifier.AudioNotifierService;
import org.jitsi.service.configuration.ConfigurationService;
import org.jitsi.service.fileaccess.FileAccessService;
import org.jitsi.service.libjitsi.LibJitsi;
import org.jitsi.service.neomedia.MediaService;
import org.jitsi.service.packetlogging.PacketLoggingService;
import org.jitsi.service.resources.ResourceManagementService;

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

    public static FileAccessService getFileAccessService()
    {
        return LibJitsi.getFileAccessService();
    }

    public static MediaService getMediaService()
    {
        return LibJitsi.getMediaService();
    }

    public static PacketLoggingService getPacketLoggingService()
    {
        return LibJitsi.getPacketLoggingService();
    }

    public static ResourceManagementService getResourceManagementService()
    {
        return LibJitsi.getResourceManagementService();
    }

}
