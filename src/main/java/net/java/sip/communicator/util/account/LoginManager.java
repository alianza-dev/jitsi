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
package net.java.sip.communicator.util.account;

import net.java.sip.communicator.service.protocol.AccountID;
import net.java.sip.communicator.service.protocol.OperationFailedException;
import net.java.sip.communicator.service.protocol.PresenceStatus;
import net.java.sip.communicator.service.protocol.ProtocolProviderFactory;
import net.java.sip.communicator.service.protocol.ProtocolProviderService;
import net.java.sip.communicator.service.protocol.RegistrationState;
import net.java.sip.communicator.service.protocol.SecurityAuthority;
import net.java.sip.communicator.service.protocol.event.AccountManagerEvent;
import net.java.sip.communicator.service.protocol.event.AccountManagerListener;
import net.java.sip.communicator.service.protocol.event.RegistrationStateChangeEvent;
import net.java.sip.communicator.service.protocol.event.RegistrationStateChangeListener;
import net.java.sip.communicator.util.Logger;

/**
 * The <tt>LoginManager</tt> manages the login operation. Here we obtain the
 * <tt>ProtocolProviderFactory</tt>, we make the account installation and we
 * handle all events related to the registration state.
 * <p>
 * The <tt>LoginManager</tt> is the one that opens one or more
 * <tt>LoginWindow</tt>s for each <tt>ProtocolProviderFactory</tt>. The
 * <tt>LoginWindow</tt> is where user could enter an identifier and password.
 * <p>
 * Note that the behavior of this class will be changed when the Configuration
 * Service is ready.
 *
 * @author Yana Stamcheva
 */
public class LoginManager implements RegistrationStateChangeListener, AccountManagerListener {
    private static final Logger logger = Logger.getLogger(LoginManager.class);

    private boolean manuallyDisconnected = false;

    private final LoginRenderer loginRenderer;

    /**
     * Creates an instance of the <tt>LoginManager</tt>, by specifying the main
     * application window.
     *
     * @param loginRenderer the main application window
     */
    public LoginManager(LoginRenderer loginRenderer)
    {
        this.loginRenderer = loginRenderer;
    }

    /**
     * Registers the given protocol provider.
     *
     * @param protocolProvider the ProtocolProviderService to register.
     */
    public void login(ProtocolProviderService protocolProvider)
    {
        loginRenderer.startConnectingUI(protocolProvider);

        new RegisterProvider(protocolProvider,
            loginRenderer.getSecurityAuthorityImpl(protocolProvider)).start();
    }

    /**
     * Unregisters the given protocol provider.
     *
     * @param protocolProvider the ProtocolProviderService to unregister
     */
    public static void logoff(ProtocolProviderService protocolProvider)
    {
        new UnregisterProvider(protocolProvider).start();
    }

    /**
     * Notifies that the loading of the stored accounts of a
     * specific <code>ProtocolProviderFactory</code> has finished.
     *
     * @param event the <code>AccountManagerEvent</code> describing the
     *            <code>AccountManager</code> firing the notification and the
     *            other details of the specific notification.
     */
    public void handleAccountManagerEvent(AccountManagerEvent event)
    {
        if(event.getType()
            == AccountManagerEvent.STORED_ACCOUNTS_LOADED)
        {
            addAccountsForProtocolProviderFactory(event.getFactory());
        }
    }

    /**
     * Handles stored accounts for a protocol provider factory and add them
     * to the UI and register them if needed.
     * @param providerFactory the factory to handle.
     */
    private void addAccountsForProtocolProviderFactory(
        ProtocolProviderFactory providerFactory)
    {
        for (AccountID accountID : providerFactory.getRegisteredAccounts())
        {
            ProtocolProviderService protocolProvider = providerFactory.getProviderForAccount(accountID);
            handleProviderAdded(protocolProvider);
        }
    }

    /**
     * The method is called by a ProtocolProvider implementation whenever a
     * change in the registration state of the corresponding provider had
     * occurred.
     *
     * @param evt ProviderStatusChangeEvent the event describing the status
     *            change.
     */
    public void registrationStateChanged(RegistrationStateChangeEvent evt)
    {
        RegistrationState newState = evt.getNewState();
        ProtocolProviderService protocolProvider = evt.getProvider();
        AccountID accountID = protocolProvider.getAccountID();

        if (logger.isTraceEnabled())
            logger.trace("Protocol provider: " + protocolProvider
            + " changed its state to: " + evt.getNewState().getStateName());

        if (newState.equals(RegistrationState.REGISTERED)
            || newState.equals(RegistrationState.UNREGISTERED)
            || newState.equals(RegistrationState.EXPIRED)
            || newState.equals(RegistrationState.AUTHENTICATION_FAILED)
            || newState.equals(RegistrationState.CONNECTION_FAILED)
            || newState.equals(RegistrationState.CHALLENGED_FOR_AUTHENTICATION)
            || newState.equals(RegistrationState.REGISTERED))
        {
            loginRenderer.stopConnectingUI(protocolProvider);
        }

        if (newState.equals(RegistrationState.REGISTERED))
        {
            loginRenderer.protocolProviderConnected(protocolProvider,
                                                    System.currentTimeMillis());
        }
    }

    /**
     * Adds all UI components (status selector box, etc) related to the given
     * protocol provider.
     *
     * @param protocolProvider the <tt>ProtocolProviderService</tt>
     */
    private void handleProviderAdded(ProtocolProviderService protocolProvider)
    {
        if (logger.isTraceEnabled())
            logger.trace("The following protocol provider was just added: "
            + protocolProvider.getAccountID().getAccountAddress());

        synchronized(loginRenderer)
        {
            if(!loginRenderer.containsProtocolProviderUI(protocolProvider))
            {
                protocolProvider.addRegistrationStateChangeListener(this);
                loginRenderer.addProtocolProviderUI(protocolProvider);
            }
            // we have already added this provider and scheduled
            // a login if needed
            // we've done our work, if it fails or something else
            // reconnect or other plugins will take care
            else
                return;
        }

        Object status = null;

        if (status == null
            || ((status instanceof PresenceStatus) && (((PresenceStatus) status)
                .getStatus() >= PresenceStatus.ONLINE_THRESHOLD)))
        {
            login(protocolProvider);
        }
    }

    /**
     * Removes all UI components related to the given protocol provider.
     *
     * @param protocolProvider the <tt>ProtocolProviderService</tt>
     */
    private void handleProviderRemoved(ProtocolProviderService protocolProvider)
    {
        loginRenderer.removeProtocolProviderUI(protocolProvider);
    }

    /**
     * Returns <tt>true</tt> to indicate the jitsi has been manually
     * disconnected, <tt>false</tt> - otherwise.
     *
     * @return <tt>true</tt> to indicate the jitsi has been manually
     * disconnected, <tt>false</tt> - otherwise
     */
    public boolean isManuallyDisconnected()
    {
        return manuallyDisconnected;
    }

    /**
     * Sets the manually disconnected property.
     *
     * @param manuallyDisconnected <tt>true</tt> to indicate the jitsi has been
     * manually disconnected, <tt>false</tt> - otherwise
     */
    public void setManuallyDisconnected(boolean manuallyDisconnected)
    {
        this.manuallyDisconnected = manuallyDisconnected;
    }

    /**
     * Registers a protocol provider in a separate thread.
     */
    private class RegisterProvider
        extends Thread
    {
        private final ProtocolProviderService protocolProvider;

        private final SecurityAuthority secAuth;

        RegisterProvider(   ProtocolProviderService protocolProvider,
                            SecurityAuthority secAuth)
        {
            this.protocolProvider = protocolProvider;
            this.secAuth = secAuth;

            if(logger.isTraceEnabled())
                logger.trace("Registering provider: "
                    + protocolProvider.getAccountID().getAccountAddress(),
                    new Exception(
                        "Just tracing, provider registering, not an error!"));
        }

        /**
         * Registers the contained protocol provider and process all possible
         * errors that may occur during the registration process.
         */
        @Override
        public void run()
        {
            try
            {
                protocolProvider.register(secAuth);
            }
            catch (OperationFailedException ex)
            {
                handleOperationFailedException(ex);
            }
            catch (Throwable ex)
            {
                logger.error("Failed to register protocol provider. ", ex);

                AccountID accountID = protocolProvider.getAccountID();
            }
        }

        private void handleOperationFailedException(OperationFailedException ex)
        {
            String errorMessage = "";

            switch (ex.getErrorCode())
            {
            case OperationFailedException.NETWORK_FAILURE:
            {
                if (logger.isInfoEnabled())
                {
                    logger.info("Provider could not be registered"
                            + " due to a network failure: " + ex);
                }

                loginRenderer.protocolProviderConnectionFailed(
                    protocolProvider,
                    LoginManager.this);
            }
                break;
            default:
                logger.error("Provider could not be registered.", ex);
            }
        }
    }

    /**
     * Unregisters a protocol provider in a separate thread.
     */
    private static class UnregisterProvider
        extends Thread
    {
        ProtocolProviderService protocolProvider;

        UnregisterProvider(ProtocolProviderService protocolProvider)
        {
            this.protocolProvider = protocolProvider;
        }

        /**
         * Unregisters the contained protocol provider and process all possible
         * errors that may occur during the un-registration process.
         */
        @Override
        public void run()
        {
            try
            {
                protocolProvider.unregister(true);
            }
            catch (OperationFailedException ex)
            {
                int errorCode = ex.getErrorCode();

                if (errorCode == OperationFailedException.GENERAL_ERROR)
                {
                    logger.error("Provider could not be unregistered"
                        + " due to the following general error: " + ex);
                }
                else if (errorCode == OperationFailedException.INTERNAL_ERROR)
                {
                    logger.error("Provider could not be unregistered"
                        + " due to the following internal error: " + ex);
                }
                else if (errorCode == OperationFailedException.NETWORK_FAILURE)
                {
                    logger.error("Provider could not be unregistered"
                        + " due to a network failure: " + ex);
                }
            }
        }
    }
}
