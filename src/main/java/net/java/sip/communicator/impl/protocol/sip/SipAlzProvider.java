/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Copyright @ 2015 Atlassian Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.java.sip.communicator.impl.protocol.sip;

import lombok.extern.slf4j.Slf4j;
import net.java.sip.communicator.service.certificate.CertificateService;
import net.java.sip.communicator.service.gui.UIService;
import net.java.sip.communicator.service.hid.HIDService;
import net.java.sip.communicator.service.netaddr.NetworkAddressManagerService;
import org.jitsi.service.fileaccess.FileAccessService;
import org.jitsi.service.neomedia.MediaService;
import org.jitsi.service.packetlogging.PacketLoggingService;
import org.jitsi.service.resources.ResourceManagementService;
import org.jitsi.service.version.VersionService;

/**
 * Activates the SIP package
 * @author Emil Ivov
 * @author Mike Saavedra
 */
@Slf4j
public class SipAlzProvider {
    private static NetworkAddressManagerService networkAddressManagerService = null;
    private static MediaService         mediaService          = null;
    private static VersionService       versionService        = null;
    private static UIService            uiService             = null;
    private static HIDService           hidService            = null;
    private static PacketLoggingService packetLoggingService  = null;
    private static CertificateService   certService           = null;
    private static FileAccessService    fileService           = null;
    private static ResourceManagementService resources        = null;
    private static ProtocolProviderAlzFactory sipProviderFactory = null;
    private UriHandlerSipImpl uriHandlerSipImpl = null;

    public void start() throws Exception
    {
        sipProviderFactory = new ProtocolProviderAlzFactory();
        uriHandlerSipImpl = new UriHandlerSipImpl(sipProviderFactory);
    }

    public static CertificateService getCertificateVerificationService()
    {
        return certService;
    }

    public static NetworkAddressManagerService getNetworkAddressManagerService()
    {
        return networkAddressManagerService;
    }

    public static HIDService getHIDService()
    {
        return hidService;
    }

    public static ProtocolProviderAlzFactory getProtocolProviderFactory()
    {
        return sipProviderFactory;
    }

    public static MediaService getMediaService()
    {
        return mediaService;
    }

    public static VersionService getVersionService()
    {
        return versionService;
    }

    public static UIService getUIService()
    {
        return uiService;
    }

    public static ResourceManagementService getResources()
    {
        return resources;
    }

    public static PacketLoggingService getPacketLogging()
    {
        return packetLoggingService;
    }

    public static FileAccessService getFileAccessService()
    {
        return fileService;
    }

}
