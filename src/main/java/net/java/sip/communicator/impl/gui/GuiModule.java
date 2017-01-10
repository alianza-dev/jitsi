package net.java.sip.communicator.impl.gui;

import com.google.inject.AbstractModule;
import net.java.sip.communicator.impl.browserlauncher.BrowserLauncherImpl;
import net.java.sip.communicator.impl.callhistory.CallHistoryServiceImpl;
import net.java.sip.communicator.impl.contactlist.MetaContactListServiceImpl;
import net.java.sip.communicator.impl.credentialsstorage.CredentialsStorageServiceImpl;
import net.java.sip.communicator.impl.globaldisplaydetails.GlobalDisplayDetailsImpl;
import net.java.sip.communicator.impl.globaldisplaydetails.GlobalStatusServiceImpl;
import net.java.sip.communicator.impl.gui.main.login.DefaultSecurityAuthority;
import net.java.sip.communicator.impl.keybindings.KeybindingsServiceImpl;
import net.java.sip.communicator.impl.metahistory.MetaHistoryServiceImpl;
import net.java.sip.communicator.impl.msghistory.MessageHistoryServiceImpl;
import net.java.sip.communicator.impl.muc.MUCServiceImpl;
import net.java.sip.communicator.impl.neomedia.MediaConfigurationImpl;
import net.java.sip.communicator.impl.osdependent.DesktopServiceImpl;
import net.java.sip.communicator.impl.osdependent.jdic.SystrayServiceJdicImpl;
import net.java.sip.communicator.impl.phonenumbers.PhoneNumberI18nServiceImpl;
import net.java.sip.communicator.impl.protocol.sip.ProtocolProviderAlzService;
import net.java.sip.communicator.impl.replacement.directimage.ReplacementServiceDirectImageImpl;
import net.java.sip.communicator.impl.replacement.smiley.ReplacementServiceSmileyImpl;
import net.java.sip.communicator.impl.resources.ResourceManagementServiceImpl;
import net.java.sip.communicator.service.browserlauncher.BrowserLauncherService;
import net.java.sip.communicator.service.callhistory.CallHistoryService;
import net.java.sip.communicator.service.contactlist.MetaContactListService;
import net.java.sip.communicator.service.credentialsstorage.CredentialsStorageService;
import net.java.sip.communicator.service.desktop.DesktopService;
import net.java.sip.communicator.service.globaldisplaydetails.GlobalDisplayDetailsService;
import net.java.sip.communicator.service.gui.AlertUIService;
import net.java.sip.communicator.service.gui.UIService;
import net.java.sip.communicator.service.keybindings.KeybindingsService;
import net.java.sip.communicator.service.metahistory.MetaHistoryService;
import net.java.sip.communicator.service.msghistory.MessageHistoryService;
import net.java.sip.communicator.service.muc.MUCService;
import net.java.sip.communicator.service.notification.NotificationService;
import net.java.sip.communicator.service.notification.NotificationServiceImpl;
import net.java.sip.communicator.service.protocol.PhoneNumberI18nService;
import net.java.sip.communicator.service.protocol.ProtocolProviderService;
import net.java.sip.communicator.service.protocol.SecurityAuthority;
import net.java.sip.communicator.service.protocol.globalstatus.GlobalStatusService;
import net.java.sip.communicator.service.replacement.directimage.DirectImageReplacementService;
import net.java.sip.communicator.service.replacement.smilies.SmiliesReplacementService;
import net.java.sip.communicator.service.systray.SystrayService;
import org.jitsi.impl.configuration.ConfigurationServiceImpl;
import org.jitsi.impl.fileaccess.FileAccessServiceImpl;
import org.jitsi.impl.neomedia.MediaServiceImpl;
import org.jitsi.impl.neomedia.notify.AudioNotifierServiceImpl;
import org.jitsi.service.audionotifier.AudioNotifierService;
import org.jitsi.service.configuration.ConfigurationService;
import org.jitsi.service.fileaccess.FileAccessService;
import org.jitsi.service.neomedia.MediaConfigurationService;
import org.jitsi.service.neomedia.MediaService;
import org.jitsi.service.resources.ResourceManagementService;

/**
 * Created by msaavedra on 1/4/17.
 */
public class GuiModule extends AbstractModule {
    @Override
    protected void configure() {
        //use bind().to() specify which class (impl) should be used to satisfy a dependency (interface)
        //TODO DEVTE-1304 fix me for GUI
//        bind(ConfigurationService.class).to(ConfigurationServiceImpl.class);
        bind(MetaHistoryService.class).to(MetaHistoryServiceImpl.class);
        bind(MetaContactListService.class).to(MetaContactListServiceImpl.class);
        bind(CallHistoryService.class).to(CallHistoryServiceImpl.class);
//        bind(AudioNotifierService.class).to(AudioNotifierServiceImpl.class);
        bind(BrowserLauncherService.class).to(BrowserLauncherImpl.class);
        bind(SystrayService.class).to(SystrayServiceJdicImpl.class);
        bind(ResourceManagementService.class).to(ResourceManagementServiceImpl.class);
        bind(KeybindingsService.class).to(KeybindingsServiceImpl.class);
        bind(FileAccessService.class).to(FileAccessServiceImpl.class);
        bind(DesktopService.class).to(DesktopServiceImpl.class);
//        bind(MediaService.class).to(MediaServiceImpl.class);
        bind(SmiliesReplacementService.class).to(ReplacementServiceSmileyImpl.class);
        bind(DirectImageReplacementService.class).to(ReplacementServiceDirectImageImpl.class);
        bind(GlobalStatusService.class).to(GlobalStatusServiceImpl.class);
        bind(NotificationService.class).to(NotificationServiceImpl.class);
        bind(SecurityAuthority.class).to(DefaultSecurityAuthority.class);
        bind(GlobalDisplayDetailsService.class).to(GlobalDisplayDetailsImpl.class);
        bind(AlertUIService.class).to(AlertUIServiceImpl.class);
        bind(CredentialsStorageService.class).to(CredentialsStorageServiceImpl.class);
        bind(MUCService.class).to(MUCServiceImpl.class);
        bind(MessageHistoryService.class).to(MessageHistoryServiceImpl.class);
        bind(PhoneNumberI18nService.class).to(PhoneNumberI18nServiceImpl.class);
        bind(ProtocolProviderService.class).to(ProtocolProviderAlzService.class);
        bind(UIService.class).to(UIServiceImpl.class);
        bind(MediaConfigurationService.class).to(MediaConfigurationImpl.class);
    }
}
