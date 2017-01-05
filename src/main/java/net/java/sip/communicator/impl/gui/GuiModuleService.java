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
package net.java.sip.communicator.impl.gui;

import net.java.sip.communicator.impl.gui.main.account.Account;
import net.java.sip.communicator.impl.gui.main.contactlist.TreeContactList;
import net.java.sip.communicator.service.ModuleService;
import net.java.sip.communicator.service.browserlauncher.BrowserLauncherService;
import net.java.sip.communicator.service.callhistory.CallHistoryService;
import net.java.sip.communicator.service.contactlist.MetaContactListService;
import net.java.sip.communicator.service.contactsource.ContactSourceService;
import net.java.sip.communicator.service.credentialsstorage.CredentialsStorageService;
import net.java.sip.communicator.service.desktop.DesktopService;
import net.java.sip.communicator.service.globaldisplaydetails.GlobalDisplayDetailsService;
import net.java.sip.communicator.service.gui.AlertUIService;
import net.java.sip.communicator.service.keybindings.KeybindingsService;
import net.java.sip.communicator.service.metahistory.MetaHistoryService;
import net.java.sip.communicator.service.msghistory.MessageHistoryService;
import net.java.sip.communicator.service.muc.MUCService;
import net.java.sip.communicator.service.notification.NotificationService;
import net.java.sip.communicator.service.protocol.AccountID;
import net.java.sip.communicator.service.protocol.AccountManager;
import net.java.sip.communicator.service.protocol.PhoneNumberI18nService;
import net.java.sip.communicator.service.protocol.ProtocolProviderFactory;
import net.java.sip.communicator.service.protocol.ProtocolProviderService;
import net.java.sip.communicator.service.protocol.SecurityAuthority;
import net.java.sip.communicator.service.protocol.globalstatus.GlobalStatusService;
import net.java.sip.communicator.service.replacement.ReplacementService;
import net.java.sip.communicator.service.replacement.directimage.DirectImageReplacementService;
import net.java.sip.communicator.service.replacement.smilies.SmiliesReplacementService;
import net.java.sip.communicator.service.systray.SystrayService;
import net.java.sip.communicator.util.ConfigAlzService;
import net.java.sip.communicator.util.Logger;
import net.java.sip.communicator.util.UtilModuleService;
import org.jitsi.service.audionotifier.AudioNotifierService;
import org.jitsi.service.configuration.ConfigurationService;
import org.jitsi.service.fileaccess.FileAccessService;
import org.jitsi.service.neomedia.MediaService;
import org.jitsi.service.resources.ResourceManagementService;

import javax.inject.Inject;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The GUI Activator class.
 *
 * @author Yana Stamcheva
 */
public class GuiModuleService implements ModuleService
{
    /**
     * The <tt>Logger</tt> used by the <tt>GuiActivator</tt> class and its
     * instances for logging output.
     */
    private static final Logger logger = Logger.getLogger(GuiModuleService.class);

    private static final Map<String, ReplacementService>
            replacementSourcesMap = new Hashtable<String, ReplacementService>();

    /**
     * Indicates if this bundle has been started.
     */
    public static boolean isStarted = false;

    /**
     * The contact list object.
     */
    private static TreeContactList contactList;

    List<ContactSourceService> contactSources = new ArrayList<>();






    UIServiceImpl uiService;
    ConfigurationService configService;
    MetaHistoryService metaHistoryService;
    MetaContactListService metaCListService;
    CallHistoryService callHistoryService;
//    AudioNotifierService audioNotifierService;
    BrowserLauncherService browserLauncherService;
    SystrayService systrayService;
    ResourceManagementService resourcesService;
    KeybindingsService keybindingsService;
//    FileAccessService fileAccessService;
    DesktopService desktopService;
//    MediaService mediaService;
    SmiliesReplacementService smiliesService;
    DirectImageReplacementService directImageService;
    GlobalStatusService globalStatusService;
    AccountManager accountManager;
    NotificationService notificationService;
    SecurityAuthority securityAuthority;
//    DemuxContactSourceService demuxContactSourceService;
    GlobalDisplayDetailsService globalDisplayDetailsService;
    AlertUIService alertUIService;
    CredentialsStorageService credentialsService;
    MUCService mucService;
    MessageHistoryService messageHistoryService;
    PhoneNumberI18nService phoneNumberI18nService;
    UtilModuleService utilModuleService;
    ConfigAlzService configAlzService;

    @Inject
    GuiModuleService(
            UIServiceImpl uiService,
//            ConfigurationService configService,
            MetaHistoryService metaHistoryService,
            MetaContactListService metaCListService,
            CallHistoryService callHistoryService,
//            AudioNotifierService audioNotifierService,
            BrowserLauncherService browserLauncherService,
            SystrayService systrayService,
            ResourceManagementService resourcesService,
            KeybindingsService keybindingsService,
//            FileAccessService fileAccessService,
            DesktopService desktopService,
//            MediaService mediaService,
            SmiliesReplacementService smiliesService,
            DirectImageReplacementService directImageService,
            GlobalStatusService globalStatusService,
            AccountManager accountManager,
            NotificationService notificationService,
            SecurityAuthority securityAuthority,
//            DemuxContactSourceService demuxContactSourceService,
            GlobalDisplayDetailsService globalDisplayDetailsService,
            AlertUIService alertUIService,
            CredentialsStorageService credentialsService,
            MUCService mucService,
            MessageHistoryService messageHistoryService,
            PhoneNumberI18nService phoneNumberI18nService,
            UtilModuleService utilModuleService,
            ConfigAlzService configAlzService
    ) {
        //TODO DEVTE-1304 fix me for GUI
        this.uiService = uiService;
//        this.configService = configService;
        this.metaHistoryService = metaHistoryService;
        this.metaCListService = metaCListService;
        this.callHistoryService = callHistoryService;
//        this.audioNotifierService = audioNotifierService;
        this.browserLauncherService = browserLauncherService;
        this.systrayService = systrayService;
        this.resourcesService = resourcesService;
        this.keybindingsService = keybindingsService;
//        this.fileAccessService = fileAccessService;
        this.desktopService = desktopService;
//        this.mediaService = mediaService;
        this.smiliesService = smiliesService;
        this.directImageService = directImageService;
        this.globalStatusService = globalStatusService;
        this.accountManager = accountManager;
        this.notificationService = notificationService;
        this.securityAuthority = securityAuthority;
//        this.demuxContactSourceService = demuxContactSourceService;
        this.globalDisplayDetailsService = globalDisplayDetailsService;
        this.alertUIService = alertUIService;
        this.credentialsService = credentialsService;
        this.mucService = mucService;
        this.messageHistoryService = messageHistoryService;
        this.phoneNumberI18nService = phoneNumberI18nService;
        this.utilModuleService = utilModuleService;
        this.configAlzService = configAlzService;
    }

    @Override
    public void start() throws Exception
    {
        isStarted = true;
        //TODO DEVTE-1304 fix me for GUI
//        GuiModuleService.bundleContext = bContext;

        configAlzService.loadGuiConfigurations();

        try
        {
            alertUIService = new AlertUIServiceImpl();
            // Registers an implementation of the AlertUIService.
            //TODO DEVTE-1304 fix me for GUI
//            bundleContext.registerService(  AlertUIService.class.getName(),
//                    alertUIService,
//                    null);

            // Registers an implementation of the ImageLoaderService.
//            bundleContext.registerService(  ImageLoaderService.class.getName(),
//                    new ImageLoaderServiceImpl(),
//                    null);

            // Create the ui service
            uiService = new UIServiceImpl();

            SwingUtilities.invokeLater(new Runnable()
            {
                @Override
                public void run()
                {
                    uiService.loadApplicationGui();

                    //TODO DEVTE-1304 fix me for GUI
//                    getConfigurationService().addPropertyChangeListener(uiService);

//                    bundleContext.addServiceListener(uiService);

                    // don't block the ui thread with registering services, as
                    // they are executed in the same thread as registering
                    new Thread()
                    {
                        @Override
                        public void run()
                        {
                            if (logger.isInfoEnabled())
                                logger.info("UI ModuleService...[  STARTED ]");

                            //TODO DEVTE-1304 fix me for GUI
//                            bundleContext.registerService(
//                                    UIService.class.getName(),
//                                    uiService,
//                                    null);

                            if (logger.isInfoEnabled())
                                logger.info("UI ModuleService ...[REGISTERED]");

                            // UIServiceImpl also implements ShutdownService.
                            //TODO DEVTE-1304 fix me for GUI
//                            bundleContext.registerService(
//                                    ShutdownService.class.getName(),
//                                    uiService,
//                                    null);
                        }
                    }.start();
                }
            });

            logger.logEntry();
        }
        finally
        {
            logger.logExit();
        }
    }

    /**
     * Stops the service. This method blocks until the service has completely shut down.
     */
    @Override
    public void stop() {

    }

    /**
     * Returns all <tt>ProtocolProviderFactory</tt>s obtained from the bundle
     * context.
     *
     * @return all <tt>ProtocolProviderFactory</tt>s obtained from the bundle
     *         context
     */
    public Map<Object, ProtocolProviderFactory> getProtocolProviderFactories()
    {
        return utilModuleService.getProtocolProviderFactories();
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
     * Returns the <tt>ConfigurationService</tt> obtained from the bundle
     * context.
     * @return the <tt>ConfigurationService</tt> obtained from the bundle
     * context
     */
    public ConfigurationService getConfigurationService()
    {
        return configService;
    }

    /**
     * Returns the <tt>MetaHistoryService</tt> obtained from the bundle
     * context.
     * @return the <tt>MetaHistoryService</tt> obtained from the bundle
     * context
     */
    public MetaHistoryService getMetaHistoryService()
    {
        return metaHistoryService;
    }

    /**
     * Returns the <tt>MetaContactListService</tt> obtained from the bundle
     * context.
     * @return the <tt>MetaContactListService</tt> obtained from the bundle
     * context
     */
    public MetaContactListService getContactListService()
    {
        return metaCListService;
    }

    /**
     * Returns the <tt>CallHistoryService</tt> obtained from the bundle
     * context.
     * @return the <tt>CallHistoryService</tt> obtained from the bundle
     * context
     */
    public CallHistoryService getCallHistoryService()
    {
        return callHistoryService;
    }

    /**
     * Returns the <tt>AudioNotifierService</tt> obtained from the bundle
     * context.
     * @return the <tt>AudioNotifierService</tt> obtained from the bundle
     * context
     */
    //TODO DEVTE-1304 fix me for GUI
//    public AudioNotifierService getAudioNotifier()
//    {
//        return audioNotifierService;
//    }

    /**
     * Returns the <tt>BrowserLauncherService</tt> obtained from the bundle
     * context.
     * @return the <tt>BrowserLauncherService</tt> obtained from the bundle
     * context
     */
    public BrowserLauncherService getBrowserLauncher()
    {
        return browserLauncherService;
    }

    /**
     * Returns the <tt>GlobalStatusService</tt> obtained from the bundle
     * context.
     * @return the <tt>GlobalStatusService</tt> obtained from the bundle
     * context
     */
    public GlobalStatusService getGlobalStatusService()
    {
        return globalStatusService;
    }

    /**
     * Returns the current implementation of the <tt>UIService</tt>.
     * @return the current implementation of the <tt>UIService</tt>
     */
    public UIServiceImpl getUIService()
    {
        return uiService;
    }

    /**
     * Returns the implementation of the <tt>AlertUIService</tt>.
     * @return the implementation of the <tt>AlertUIService</tt>
     */
    public AlertUIService getAlertUIService()
    {
        return alertUIService;
    }

    /**
     * Returns the <tt>SystrayService</tt> obtained from the bundle context.
     *
     * @return the <tt>SystrayService</tt> obtained from the bundle context
     */
    public SystrayService getSystrayService()
    {
        return systrayService;
    }

    /**
     * Returns the <tt>KeybindingsService</tt> obtained from the bundle context.
     *
     * @return the <tt>KeybindingsService</tt> obtained from the bundle context
     */
    public KeybindingsService getKeybindingsService()
    {
        return keybindingsService;
    }

    /**
     * Returns the <tt>ResourceManagementService</tt>, through which we will
     * access all resources.
     *
     * @return the <tt>ResourceManagementService</tt>, through which we will
     * access all resources.
     */
    public ResourceManagementService getResources()
    {
        return resourcesService;
    }

    /**
     * Returns the <tt>FileAccessService</tt> obtained from the bundle context.
     *
     * @return the <tt>FileAccessService</tt> obtained from the bundle context
     */
    //TODO DEVTE-1304 fix me for GUI
//    public FileAccessService getFileAccessService()
//    {
//        return fileAccessService;
//    }

    /**
     * Returns the <tt>DesktopService</tt> obtained from the bundle context.
     *
     * @return the <tt>DesktopService</tt> obtained from the bundle context
     */
    public DesktopService getDesktopService()
    {
        return desktopService;
    }

    /**
     * Returns an instance of the <tt>MediaService</tt> obtained from the
     * bundle context.
     * @return an instance of the <tt>MediaService</tt> obtained from the
     * bundle context
     */
    public MediaService getMediaService()
    {
        return null;//mediaService;
    }

    /**
     * Returns the <tt>DemuxContactSourceService</tt> obtained from the bundle
     * context.
     *
     * @return the <tt>DemuxContactSourceService</tt> obtained from the bundle
     * context
     */
    //TODO DEVTE-1304 fix me for GUI
//    public DemuxContactSourceService getDemuxContactSourceService()
//    {
//        return demuxContactSourceService;
//    }

    /**
     * Returns the <tt>GlobalDisplayDetailsService</tt> obtained from the bundle
     * context.
     *
     * @return the <tt>GlobalDisplayDetailsService</tt> obtained from the bundle
     * context
     */
    public GlobalDisplayDetailsService getGlobalDisplayDetailsService()
    {
        return globalDisplayDetailsService;
    }

    /**
     * Returns a list of all registered contact sources.
     * @return a list of all registered contact sources
     */
    public List<ContactSourceService> getContactSources()
    {
        return contactSources;
    }

    /**
     * Returns all <tt>ReplacementService</tt>s obtained from the bundle
     * context.
     *
     * @return all <tt>ReplacementService</tt> implementation obtained from the
     *         bundle context
     */
    public Map<String, ReplacementService> getReplacementSources()
    {
        return replacementSourcesMap;
    }

    /**
     * Returns the <tt>SmiliesReplacementService</tt> obtained from the bundle
     * context.
     *
     * @return the <tt>SmiliesReplacementService</tt> implementation obtained
     * from the bundle context
     */
    public SmiliesReplacementService getSmiliesReplacementSource()
    {
        return smiliesService;
    }

    /**
     * Returns the <tt>DirectImageReplacementService</tt> obtained from the
     * bundle context.
     * 
     * @return the <tt>DirectImageReplacementService</tt> implementation
     * obtained from the bundle context
     */
    public DirectImageReplacementService getDirectImageReplacementSource()
    {
        return directImageService;
    }

    /**
     * Returns the <tt>SecurityAuthority</tt> implementation registered to
     * handle security authority events.
     *
     * @return the <tt>SecurityAuthority</tt> implementation obtained
     * from the bundle context
     */
    public SecurityAuthority getSecurityAuthority()
    {
        return securityAuthority;
    }

    /**
     * Returns the <tt>NotificationService</tt> obtained from the bundle context.
     *
     * @return the <tt>NotificationService</tt> obtained from the bundle context
     */
    public NotificationService getNotificationService()
    {
        return notificationService;
    }

    /**
     * Returns the <tt>SecurityAuthority</tt> implementation registered to
     * handle security authority events.
     *
     * @param protocolName protocol name
     * @return the <tt>SecurityAuthority</tt> implementation obtained
     * from the bundle context
     */
    public SecurityAuthority getSecurityAuthority(String protocolName)
    {
        return securityAuthority;
    }

    /**
     * Sets the <tt>contactList</tt> component currently used to show the
     * contact list.
     * @param list the contact list object to set
     */
    public void setContactList(TreeContactList list)
    {
        contactList = list;
    }

    /**
     * Returns the component used to show the contact list.
     * @return the component used to show the contact list
     */
    public TreeContactList getContactList()
    {
        return contactList;
    }

    /**
     * Returns the list of wrapped protocol providers.
     *
     * @param providers the list of protocol providers
     * @return an array of wrapped protocol providers
     */
    public Account[] getAccounts(List<ProtocolProviderService> providers)
    {
        Iterator<ProtocolProviderService> accountsIter = providers.iterator();
        List<Account> accounts = new ArrayList<Account>();

        while (accountsIter.hasNext())
            accounts.add(new Account(accountsIter.next()));

        return accounts.toArray(new Account[accounts.size()]);
    }

    /**
     * Returns the preferred account if there's one.
     *
     * @return the <tt>ProtocolProviderService</tt> corresponding to the
     * preferred account
     */
    public ProtocolProviderService getPreferredAccount()
    {
        // check for preferred wizard
        String prefWName = getResources().getSettingsString("impl.gui.PREFERRED_ACCOUNT_WIZARD");
        if(prefWName == null || prefWName.length() <= 0)
            return null;

        //TODO DEVTE-1304 fix me for GUI
//        Collection<ServiceReference<AccountRegistrationWizard>> accountWizardRefs
//            = ServiceUtils.getServiceReferences(
//                    GuiModuleService.bundleContext,
//                    AccountRegistrationWizard.class);

        // in case we found any, add them in this container.
//        if (accountWizardRefs != null)
        {
            if (logger.isDebugEnabled())
            {
//                logger.debug(
//                        "Found " + accountWizardRefs.size()
//                            + " already installed providers.");
            }

//            for (ServiceReference<AccountRegistrationWizard> accountWizardRef
//                    : accountWizardRefs)
            {
//                AccountRegistrationWizard wizard
//                    = GuiModuleService.bundleContext.getService(accountWizardRef);

                // is it the preferred protocol ?
//                if(wizard.getClass().getName().equals(prefWName))
                {
                    for (ProtocolProviderFactory providerFactory : getProtocolProviderFactories().values())
                    {
                        for (AccountID accountID
                                : providerFactory.getRegisteredAccounts())
                        {
//                            ServiceReference<ProtocolProviderService> serRef
//                                = providerFactory.getProviderForAccount(
//                                        accountID);
//                            ProtocolProviderService protocolProvider
//                                = GuiModuleService.bundleContext.getService(serRef);

//                            if (protocolProvider.getAccountID()
//                                    .getProtocolDisplayName()
//                                        .equals(wizard.getProtocolName()))
//                            {
//                                return protocolProvider;
//                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns a reference to a CredentialsStorageService implementation
     * currently registered in the bundle context or null if no such
     * implementation was found.
     *
     * @return a currently valid implementation of the
     * CredentialsStorageService.
     */
    public CredentialsStorageService getCredentialsStorageService()
    {
        return credentialsService;
    }

    /**
     * Returns a reference to a MUCService implementation
     * currently registered in the bundle context or null if no such
     * implementation was found.
     *
     * @return a currently valid implementation of the
     * MUCService.
     */
    public MUCService getMUCService()
    {
        return mucService;
    }
    
    /**
     * Gets the service giving access to message history.
     *
     * @return the service giving access to message history.
     */
    public MessageHistoryService getMessageHistoryService()
    {
        return messageHistoryService;
    }

    /**
     * Returns the PhoneNumberI18nService.
     * @return returns the PhoneNumberI18nService.
     */
    public PhoneNumberI18nService getPhoneNumberI18nService()
    {
        return phoneNumberI18nService;
    }
}
