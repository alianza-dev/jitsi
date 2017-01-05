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
package net.java.sip.communicator.util;

import net.java.sip.communicator.impl.protocol.sip.ProtocolProviderFactorySipImpl;
import net.java.sip.communicator.service.ModuleService;
import net.java.sip.communicator.service.gui.AlertUIService;
import net.java.sip.communicator.service.gui.UIService;
import net.java.sip.communicator.service.protocol.AccountManager;
import net.java.sip.communicator.service.protocol.ProtocolNames;
import net.java.sip.communicator.service.protocol.ProtocolProviderFactory;
import net.java.sip.communicator.service.systray.SystrayService;
import org.jitsi.service.configuration.ConfigurationService;
import org.jitsi.service.fileaccess.FileAccessService;
import org.jitsi.service.neomedia.MediaConfigurationService;
import org.jitsi.service.neomedia.MediaService;
import org.jitsi.service.resources.ResourceManagementService;
import org.jitsi.util.OSUtils;

import javax.inject.Inject;
import java.util.Hashtable;
import java.util.Map;

/**
 * The only raison d'etre for this Activator is so that it would set a global
 * exception handler. It doesn't export any services and neither it runs any
 * initialization - all it does is call
 * <tt>Thread.setUncaughtExceptionHandler()</tt>
 *
 * @author Emil Ivov
 */
public class UtilModuleService
    implements ModuleService,
               Thread.UncaughtExceptionHandler
{
    /**
     * The <tt>Logger</tt> used by the <tt>UtilActivator</tt> class and its
     * instances for logging output.
     */
    private static final Logger logger
        = Logger.getLogger(UtilModuleService.class);

//    ConfigurationService configurationService;
    ResourceManagementService resourceService;
    UIService uiService;
    FileAccessService fileAccessService;
    MediaService mediaService;
    MediaConfigurationService mediaConfigurationService;
    AccountManager accountManager;
    AlertUIService alertUIService;
    SystrayService systrayService;
    ProtocolProviderFactorySipImpl protocolProviderFactorySip;

    @Inject
    UtilModuleService(
//            ConfigurationService configurationService,
            ResourceManagementService resourceService,
            UIService uiService,
            FileAccessService fileAccessService,
//            MediaService mediaService,
            MediaConfigurationService mediaConfigurationService,
            AccountManager accountManager,
            AlertUIService alertUIService,
            SystrayService systrayService,
            ProtocolProviderFactorySipImpl protocolProviderFactorySip
    ) {
        //TODO DEVTE-1304 fix me for GUI
//        this.configurationService = configurationService;
        this.resourceService = resourceService;
        this.uiService = uiService;
        this.fileAccessService = fileAccessService;
//        this.mediaService = mediaService;
        this.mediaConfigurationService = mediaConfigurationService;
        this.accountManager = accountManager;
        this.alertUIService = alertUIService;
        this.systrayService = systrayService;
        this.protocolProviderFactorySip = protocolProviderFactorySip;
    }

    /**
     * Loads logging config if any. Need to be loaded in order to activate
     * logging and need to be activated after bundle context is initialized.
     */
    private void loadLoggingConfig()
    {
        try
        {
            Class.forName(
                    "net.java.sip.communicator.util.JavaUtilLoggingConfig")
                .newInstance();
        }
        catch(Throwable t){}
    }

    /**
     * Method invoked when a thread would terminate due to the given uncaught
     * exception. All we do here is simply log the exception using the system
     * logger.
     *
     * <p>Any exception thrown by this method will be ignored by the
     * Java Virtual Machine and thus won't screw our application.
     *
     * @param thread the thread
     * @param exc the exception
     */
    public void uncaughtException(Thread thread, Throwable exc)
    {
        logger.error("An uncaught exception occurred in thread="
                     + thread
                     + " and message was: "
                     + exc.getMessage()
                     , exc);
    }

    /**
     * Returns the <tt>ConfigurationService</tt> currently registered.
     *
     * @return the <tt>ConfigurationService</tt>
     */
    public ConfigurationService getConfigurationService()
    {
        //TODO DEVTE-1304 fix me for GUI
//        return configurationService;
        return null;
    }

    /**
     * Returns the service giving access to all application resources.
     *
     * @return the service giving access to all application resources.
     */
    public ResourceManagementService getResources()
    {
        return resourceService;
    }

    /**
     * Gets the <tt>UIService</tt> instance registered in the
     * <tt>BundleContext</tt> of the <tt>UtilActivator</tt>.
     *
     * @return the <tt>UIService</tt> instance registered in the
     * <tt>BundleContext</tt> of the <tt>UtilActivator</tt>
     */
    public UIService getUIService()
    {
        return uiService;
    }

    /**
     * Gets the <tt>SystrayService</tt> instance registered in the
     * <tt>BundleContext</tt> of the <tt>UtilActivator</tt>.
     *
     * @return the <tt>SystrayService</tt> instance registered in the
     * <tt>BundleContext</tt> of the <tt>UtilActivator</tt>
     */
    public SystrayService getSystrayService()
    {
        return systrayService;
    }

    /**
     * Returns the <tt>FileAccessService</tt> obtained from the bundle context.
     *
     * @return the <tt>FileAccessService</tt> obtained from the bundle context
     */
    public FileAccessService getFileAccessService()
    {
        return fileAccessService;
    }

    /**
     * Returns an instance of the <tt>MediaService</tt> obtained from the
     * bundle context.
     * @return an instance of the <tt>MediaService</tt> obtained from the
     * bundle context
     */
    public MediaService getMediaService()
    {
        return mediaService;
    }

    /**
     * Returns the {@link MediaConfigurationService} instance registered in the
     * <tt>BundleContext</tt> of the <tt>UtilActivator</tt>.
     *
     * @return the <tt>UIService</tt> instance registered in the
     * <tt>BundleContext</tt> of the <tt>UtilActivator</tt>
     */
    public MediaConfigurationService getMediaConfiguration()
    {
        return mediaConfigurationService;
    }

    /**
     * Returns all <tt>ProtocolProviderFactory</tt>s obtained from the bundle
     * context.
     *
     * @return all <tt>ProtocolProviderFactory</tt>s obtained from the bundle
     *         context
     */
    public Map<Object, ProtocolProviderFactory>
        getProtocolProviderFactories()
    {
        Map<Object, ProtocolProviderFactory> providerFactoriesMap = new Hashtable<>();
        providerFactoriesMap.put(ProtocolNames.SIP, protocolProviderFactorySip);

        return providerFactoriesMap;
    }

    /**
     * Returns the <tt>AccountManager</tt> obtained from the bundle context.
     * @return the <tt>AccountManager</tt> obtained from the bundle context
     */
    public AccountManager getAccountManager()
    {
        return accountManager;
    }

    /**
     * Returns the <tt>MetaContactListService</tt> obtained from the bundle
     * context.
     * @return the <tt>MetaContactListService</tt> obtained from the bundle
     * context
     */
    public AlertUIService getAlertUIService()
    {
        return alertUIService;
    }

    /**
     * Starts the service. This method blocks until the service has completely started.
     */
    @Override
    public void start() throws Exception {
        if(OSUtils.IS_ANDROID)
            loadLoggingConfig();

        if (logger.isTraceEnabled())
            logger.trace("Setting default uncaught exception handler.");
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * Stops the service. This method blocks until the service has completely shut down.
     */
    @Override
    public void stop() {

    }
}
