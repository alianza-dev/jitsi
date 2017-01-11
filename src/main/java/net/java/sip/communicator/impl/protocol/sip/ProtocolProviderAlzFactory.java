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

import net.java.sip.communicator.impl.credentialsstorage.CredentialsStorageAlzProvider;
import net.java.sip.communicator.service.credentialsstorage.CredentialsStorageService;
import net.java.sip.communicator.service.protocol.AccountID;
import net.java.sip.communicator.service.protocol.OperationFailedException;
import net.java.sip.communicator.service.protocol.ProtocolNames;
import net.java.sip.communicator.service.protocol.ProtocolProviderFactory;
import net.java.sip.communicator.service.protocol.ProtocolProviderService;
import net.java.sip.communicator.service.protocol.sip.SipAccountID;
import net.java.sip.communicator.util.Logger;
import net.java.sip.communicator.util.ServiceUtils;

import java.util.Hashtable;
import java.util.Map;

/**
 * A SIP implementation of the protocol provider factory interface.
 *
 * @author Emil Ivov
 * @author Mike Saavedra
 */
public class ProtocolProviderAlzFactory
    extends ProtocolProviderFactory
{
    private static final Logger logger =
        Logger.getLogger(ProtocolProviderAlzFactory.class);

    /**
     * Constructs a new instance of the ProtocolProviderAlzFactory.
     */
    public ProtocolProviderAlzFactory()
    {
        super(ProtocolNames.SIP);
    }

    /**
     * Overrides the original in order not to save the OPT_CLIST_PASSWORD field.
     *
     * @param accountID the account identifier.
     */
    @Override
    protected void storeAccount(AccountID accountID)
    {
        storeXCapPassword(accountID);
        super.storeAccount(accountID);
    }

    /**
     * Stores OPT_CLIST_PASSWORD property.
     *
     * @param accountID the account identifier.
     */
    private void storeXCapPassword(AccountID accountID)
    {
        // don't use getAccountPropertyString as it will query
        // credential storage, use getAccountProperty to see is there such
        // property in the account properties provided.
        // if xcap password property exist, store it through credentialsStorage
        // service
        String password
                = accountID.getAccountPropertyString(
                        SipAccountID.OPT_CLIST_PASSWORD);
        if (password != null)
        {
            CredentialsStorageService credentialStorageAlzService = CredentialsStorageAlzProvider.getCredentialStorageAlzService();
            String accountPrefix = accountID.getAccountUniqueID() + ".xcap";
            credentialStorageAlzService.storePassword(accountPrefix, password);
            // remove unsecured property
            accountID.removeAccountProperty(SipAccountIDImpl.OPT_CLIST_PASSWORD);
        }
    }

    /**
     * Initializes and creates an account corresponding to the specified
     * accountProperties and registers the resulting ProtocolProvider in the
     * <tt>context</tt> BundleContext parameter.
     *
     * @param userIDStr the user identifier uniquely representing the newly
     *   created account within the protocol namespace.
     * @param accountProperties a set of protocol (or implementation)
     *   specific properties defining the new account.
     * @return the AccountID of the newly created account.
     * @throws IllegalArgumentException if userID does not correspond to an
     *   identifier in the context of the underlying protocol or if
     *   accountProperties does not contain a complete set of account
     *   installation properties.
     * @throws IllegalStateException if the account has already been
     *   installed.
     * @throws NullPointerException if any of the arguments is null.
     */
    @Override
    public AccountID installAccount( String userIDStr,
                                 Map<String, String> accountProperties)
    {
        if (userIDStr == null)
            throw new NullPointerException("The specified AccountID was null");
        if (accountProperties == null)
            throw new NullPointerException("The specified property map was null");

        accountProperties.put(USER_ID, userIDStr);

        if (!accountProperties.containsKey(PROTOCOL))
            accountProperties.put(PROTOCOL, ProtocolNames.SIP);

        AccountID accountID = createAccountID(userIDStr, accountProperties);

        //make sure we haven't seen this account id before.
        if( registeredAccounts.containsKey(accountID) )
            throw new IllegalStateException(
                "An account for id " + userIDStr + " was already installed!");

        this.storeAccount(accountID, false);

        try
        {
            accountID = loadAccount(accountProperties);
        }
        catch(RuntimeException exc)
        {
            //it might happen that load-ing the account fails because of a bad
            //initialization. if this is the case, make sure we remove it.
            this.removeStoredAccount(accountID);

            throw exc;
        }

        return accountID;
    }

    /**
     * Modifies the account corresponding to the specified accountID. This
     * method is meant to be used to change properties of already existing
     * accounts. Note that if the given accountID doesn't correspond to any
     * registered account this method would do nothing.
     *
     * @param protocolProvider the protocol provider service corresponding to
     * the modified account.
     * @param accountProperties a set of protocol (or implementation) specific
     * properties defining the new account.
     *
     * @throws NullPointerException if any of the arguments is null.
     */
    @Override
    public void modifyAccount(  ProtocolProviderService protocolProvider,
                                Map<String, String> accountProperties)
    {
        if (protocolProvider == null)
            throw new NullPointerException(
                "The specified Protocol Provider was null");

        SipAccountIDImpl accountID
                = (SipAccountIDImpl) protocolProvider.getAccountID();

        // If the given accountID doesn't correspond to an existing account
        // we return.
        if(!registeredAccounts.containsKey(accountID))
            return;

        if (accountProperties == null)
            throw new NullPointerException(
                "The specified property map was null");

        // serverAddress == null is OK because of registrarless support

        if (!accountProperties.containsKey(PROTOCOL))
            accountProperties.put(PROTOCOL, ProtocolNames.SIP);

        // make a backup of the account properties to restore them if a failure
        // occurs with the new ones
        Map<String,String> oldAcccountProps = accountID.getAccountProperties();
        accountID.setAccountProperties(accountProperties);

        this.storeAccount(accountID);

        String userIDStr = accountProperties.get(USER_ID);

        Hashtable<String, String> properties = new Hashtable<String, String>();
        properties.put(PROTOCOL, ProtocolNames.SIP);
        properties.put(USER_ID, userIDStr);

        try
        {
            Exception initializationException = null;
            try
            {
                ((ProtocolProviderAlzService)protocolProvider)
                    .initialize(userIDStr, accountID);
            }
            catch (Exception ex)
            {
                initializationException = ex;
                accountID.setAccountProperties(oldAcccountProps);
            }

            // We store again the account in order to store all properties added
            // during the protocol provider initialization. Do this even if the
            // initialization failed - after all we're _modifying_ an account
            this.storeAccount(accountID);

            if (initializationException != null)
                throw initializationException;
        }
        catch (Exception ex)
        {
            logger.error("Failed to initialize account", ex);
            throw new IllegalArgumentException("Failed to initialize account. "
                + ex.getMessage());
        }
    }

    /**
     * Creates a new <code>SipAccountIDImpl</code> instance with a specific user
     * ID to represent a given set of account properties.
     *
     * @param userID the user ID of the new instance
     * @param accountProperties the set of properties to be represented by the
     *            new instance
     * @return a new <code>AccountID</code> instance with the specified user ID
     *         representing the given set of account properties
     */
    @Override
    protected AccountID createAccountID(String userID, Map<String, String> accountProperties)
    {
        // serverAddress == null is OK because of registrarless support
        String serverAddress = accountProperties.get(SERVER_ADDRESS);

        return new SipAccountIDImpl(userID, accountProperties, serverAddress);
    }

    /**
     * Initializes a new <code>ProtocolProviderAlzService</code> instance
     * with a specific user ID to represent a specific <code>AccountID</code>.
     *
     * @param userID the user ID to initialize the new instance with
     * @param accountID the <code>AccountID</code> to be represented by the new
     *            instance
     * @return a new <code>ProtocolProviderService</code> instance with the
     *         specific user ID representing the specified
     *         <code>AccountID</code>
     */
    @Override
    protected ProtocolProviderService createService(String userID,
        AccountID accountID)
    {
        ProtocolProviderAlzService service
            = new ProtocolProviderAlzService();

        try
        {
            service.initialize(userID, (SipAccountIDImpl) accountID);

            // We store again the account in order to store all properties added
            // during the protocol provider initialization.
            storeAccount(accountID);
        }
        catch (OperationFailedException ex)
        {
            logger.error("Failed to initialize account", ex);
            throw new IllegalArgumentException("Failed to initialize account"
                + ex.getMessage());
        }
        return service;
    }
}
