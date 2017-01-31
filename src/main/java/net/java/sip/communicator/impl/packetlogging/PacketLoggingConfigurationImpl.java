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
package net.java.sip.communicator.impl.packetlogging;

import net.java.sip.communicator.impl.libjitsi.LibJitsiAlzProvider;
import org.jitsi.service.configuration.*;
import org.jitsi.service.packetlogging.*;

/**
 * Extends PacketLoggingConfiguration by storing and loading values from
 * configuration service.
 *
 * @author Damian Minkov
 */
public class PacketLoggingConfigurationImpl
    extends PacketLoggingConfiguration
{
    /**
     * The log file name
     */
    private String logfileName = "jitsi";

    /**
     * The log file directory
     */
    private String logfileDir = PacketLoggingActivator.LOGGING_DIR_NAME;

    /**
     * Creates new PacketLoggingConfiguration and load values from
     * configuration service and if missing uses already defined
     * default values.
     */
    PacketLoggingConfigurationImpl()
    {
        // load values from config service
        ConfigurationService configService = LibJitsiAlzProvider.getConfigurationService();

        super.setGlobalLoggingEnabled(
            configService.getBoolean(
                PACKET_LOGGING_ENABLED_PROPERTY_NAME,
                isGlobalLoggingEnabled()));
        super.setSipLoggingEnabled(
            configService.getBoolean(
                PACKET_LOGGING_SIP_ENABLED_PROPERTY_NAME,
                isSipLoggingEnabled()));
        super.setJabberLoggingEnabled(
            configService.getBoolean(
                PACKET_LOGGING_JABBER_ENABLED_PROPERTY_NAME,
                isJabberLoggingEnabled()));
        super.setRTPLoggingEnabled(
            configService.getBoolean(
                PACKET_LOGGING_RTP_ENABLED_PROPERTY_NAME,
                isRTPLoggingEnabled()));
        super.setIce4JLoggingEnabled(
            configService.getBoolean(
                PACKET_LOGGING_ICE4J_ENABLED_PROPERTY_NAME,
                isIce4JLoggingEnabled()));
        super.setArbitraryLoggingEnabled(
            configService.getBoolean(
                PACKET_LOGGING_ARBITRARY_ENABLED_PROPERTY_NAME,
                isArbitraryLoggingEnabled()));
        super.setLimit(
            configService.getLong(
                PACKET_LOGGING_FILE_SIZE_PROPERTY_NAME,
                getLimit()));
        super.setLogfileCount(
            configService.getInt(
                PACKET_LOGGING_FILE_COUNT_PROPERTY_NAME,
                getLogfileCount()));
    }

    /**
     * Change whether packet logging is enabled and save it in configuration.
     * @param enabled <tt>true</tt> if we enable it.
     */
    @Override
    public void setGlobalLoggingEnabled(boolean enabled)
    {
        super.setGlobalLoggingEnabled(enabled);

        LibJitsiAlzProvider.getConfigurationService().setProperty(
            PACKET_LOGGING_ENABLED_PROPERTY_NAME, enabled);
    }

    /**
     * Change whether packet logging for sip protocol is enabled
     * and save it in configuration.
     * @param enabled <tt>true</tt> if we enable it.
     */
    @Override
    public void setSipLoggingEnabled(boolean enabled)
    {
        super.setSipLoggingEnabled(enabled);

        LibJitsiAlzProvider.getConfigurationService().setProperty(
            PACKET_LOGGING_SIP_ENABLED_PROPERTY_NAME,
            enabled);
    }

    /**
     * Change whether packet logging for jabber protocol is enabled
     * and save it in configuration.
     * @param enabled <tt>true</tt> if we enable it.
     */
    @Override
    public void setJabberLoggingEnabled(boolean enabled)
    {
        super.setJabberLoggingEnabled(enabled);

        LibJitsiAlzProvider.getConfigurationService().setProperty(
            PACKET_LOGGING_JABBER_ENABLED_PROPERTY_NAME,
            enabled);
    }

    /**
     * Change whether packet logging for RTP is enabled
     * and save it in configuration.
     * @param enabled <tt>true</tt> if we enable it.
     */
    @Override
    public void setRTPLoggingEnabled(boolean enabled)
    {
        super.setRTPLoggingEnabled(enabled);

        LibJitsiAlzProvider.getConfigurationService().setProperty(
            PACKET_LOGGING_RTP_ENABLED_PROPERTY_NAME,
            enabled);
    }

    /**
     * Change whether packet logging for Ice4J is enabled
     * and save it in configuration.
     * @param enabled <tt>true</tt> if we enable it.
     */
    @Override
    public void setIce4JLoggingEnabled(boolean enabled)
    {
        super.setIce4JLoggingEnabled(enabled);

        LibJitsiAlzProvider.getConfigurationService().setProperty(
            PACKET_LOGGING_ICE4J_ENABLED_PROPERTY_NAME,
            enabled);
    }

    /**
     * Changes the file size limit.
     * @param limit the new limit size.
     */
    @Override
    public void setLimit(long limit)
    {
        super.setLimit(limit);

        LibJitsiAlzProvider.getConfigurationService().setProperty(
                PACKET_LOGGING_FILE_SIZE_PROPERTY_NAME,
                limit);
    }

    /**
     * Changes file count.
     * @param logfileCount the new file count.
     */
    @Override
    public void setLogfileCount(int logfileCount)
    {
        super.setLogfileCount(logfileCount);

        LibJitsiAlzProvider.getConfigurationService().setProperty(
                PACKET_LOGGING_FILE_COUNT_PROPERTY_NAME,
                logfileCount);
    }

    /**
     * The name of the log file
     * @return log file name
     */
    public String getLogfileName() {
        return logfileName;
    }

    /**
     * The name of the log file directory
     * @return log file directory
     */
    public String getLogfileDir() {
        return logfileDir;
    }

    /**
     * Changes log file name
     * @param logfileName the new log file name
     */
    public void setLogfileName(String logfileName) {
        this.logfileName = logfileName;
    }

    /**
     * Changes log file directory
     * @param logfileDir the new log file directory
     */
    public void setLogfileDir(String logfileDir) {
        this.logfileDir = logfileDir;
    }

}
