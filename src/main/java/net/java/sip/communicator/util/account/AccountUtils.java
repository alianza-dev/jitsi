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
import net.java.sip.communicator.service.protocol.OperationSet;
import net.java.sip.communicator.service.protocol.ProtocolProviderFactory;
import net.java.sip.communicator.service.protocol.ProtocolProviderService;
import net.java.sip.communicator.util.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * The <tt>AccountUtils</tt> provides utility methods helping us to easily
 * obtain an account or a groups of accounts or protocol providers by some
 * specific criteria.
 *
 * @author Yana Stamcheva
 */
public class AccountUtils
{
    /**
     * The logger.
     */
    private static Logger logger = Logger.getLogger(AccountUtils.class);

    /**
     * Returns an iterator over a list of all stored <tt>AccountID</tt>-s.
     *
     * @return an iterator over a list of all stored <tt>AccountID</tt>-s
     */
    public static Collection<AccountID> getStoredAccounts()
    {
        return null;
    }

    /**
     * Return the <tt>AccountID</tt> corresponding to the given string account
     * identifier.
     *
     * @param accountID the account identifier string
     * @return the <tt>AccountID</tt> corresponding to the given string account
     * identifier
     */
    public static AccountID getAccountForID(String accountID)
    {
        Collection<AccountID> allAccounts = getStoredAccounts();

        for(AccountID account : allAccounts)
        {
            if(account.getAccountUniqueID().equals(accountID))
                return account;
        }
        return null;
    }

    /**
     * Returns a list of all currently registered providers, which support the
     * given <tt>operationSetClass</tt>.
     *
     * @param opSetClass the operation set class for which we're looking
     * for providers
     * @return a list of all currently registered providers, which support the
     * given <tt>operationSetClass</tt>
     */
    public static List<ProtocolProviderService> getRegisteredProviders(
            Class<? extends OperationSet> opSetClass)
    {
        List<ProtocolProviderService> opSetProviders
            = new LinkedList<ProtocolProviderService>();

        return opSetProviders;
    }


    /**
     * Returns a list of all currently registered telephony providers for the
     * given protocol name.
     * @param protocolName the protocol name
     * @param opSetClass the operation set class for which we're looking for
     * providers
     * @return a list of all currently registered providers for the given
     * <tt>protocolName</tt> and supporting the given <tt>operationSetClass</tt>
     */
    public static List<ProtocolProviderService> getRegisteredProviders(
            String protocolName,
            Class<? extends OperationSet> opSetClass)
    {
        List<ProtocolProviderService> opSetProviders
            = new LinkedList<ProtocolProviderService>();
        ProtocolProviderFactory providerFactory
            = getProtocolProviderFactory(protocolName);

        if (providerFactory != null)
        {
            for (AccountID accountID : providerFactory.getRegisteredAccounts())
            {
                ProtocolProviderService protocolProvider = providerFactory.getProviderForAccount(accountID);

                if ((protocolProvider.getOperationSet(opSetClass) != null)
                        && protocolProvider.isRegistered())
                {
                    opSetProviders.add(protocolProvider);
                }
            }
        }
        return opSetProviders;
    }

    /**
     * Returns a list of all registered protocol providers that could be used
     * for the operation given by the operation set. Prefers the given preferred
     * protocol provider and preferred protocol name if they're available and
     * registered.
     *
     * @param opSet
     * @param preferredProvider
     * @param preferredProtocolName
     * @return a list of all registered protocol providers that could be used
     * for the operation given by the operation set
     */
    public static List<ProtocolProviderService> getOpSetRegisteredProviders(
            Class<? extends OperationSet> opSet,
            ProtocolProviderService preferredProvider,
            String preferredProtocolName)
    {
        List<ProtocolProviderService> providers
            = new ArrayList<ProtocolProviderService>();

        if (preferredProvider != null)
        {
            if (preferredProvider.isRegistered())
            {
                providers.add(preferredProvider);
            }
            // If we have a provider, but it's not registered we try to
            // obtain all registered providers for the same protocol as the
            // given preferred provider.
            else
            {
                providers
                    = getRegisteredProviders(
                            preferredProvider.getProtocolName(),
                            opSet);
            }
        }
        // If we don't have a preferred provider we try to obtain a
        // preferred protocol name and all registered providers for it.
        else
        {
            if (preferredProtocolName != null)
            {
                providers
                    = getRegisteredProviders(preferredProtocolName, opSet);
            }
            // If the protocol name is null we simply obtain all telephony
            // providers.
            else
            {
                providers = getRegisteredProviders(opSet);
            }
        }

        return providers;
    }

    /**
     * Returns the <tt>ProtocolProviderService</tt> corresponding to the given
     * account identifier that is registered in the given factory
     * @param accountID the identifier of the account
     * @return the <tt>ProtocolProviderService</tt> corresponding to the given
     * account identifier that is registered in the given factory
     */
    public static ProtocolProviderService getRegisteredProviderForAccount(
                                                        AccountID accountID)
    {
        return null;
    }

    /**
     * Returns a <tt>ProtocolProviderFactory</tt> for a given protocol
     * provider.
     * @param protocolName the name of the protocol
     * @return a <tt>ProtocolProviderFactory</tt> for a given protocol
     * provider
     */
    public static ProtocolProviderFactory getProtocolProviderFactory(
            String protocolName)
    {
        ProtocolProviderFactory protocolProviderFactory = null;

        return protocolProviderFactory;
    }


}
