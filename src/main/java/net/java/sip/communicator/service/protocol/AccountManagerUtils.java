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
package net.java.sip.communicator.service.protocol;

import net.java.sip.communicator.service.protocol.event.AccountManagerEvent;
import net.java.sip.communicator.service.protocol.event.AccountManagerListener;

import java.util.Collection;

/**
 * Provides utilities to aid the manipulation of {@link AccountManager}.
 *
 * @author Lyubomir Marinov
 */
public final class AccountManagerUtils
{
    private static AccountManager getAccountManager()
    {
        return null;
    }

    /**
     * Starts a specific <code>Bundle</code> and waits for the
     * <code>AccountManager</code> available in a specific
     * <code>BundleContext</code> to load the stored accounts of a
     * <code>ProtocolProviderFactory</code> with a specific protocol name.
     *
     * @param bundleContextWithAccountManager
     *            the <code>BundleContext</code> in which an
     *            <code>AccountManager</code> service is registered
     * @param bundleToStart
     *            the <code>Bundle</code> to be started
     * @param protocolNameToWait
     *            the protocol name of a <code>ProtocolProviderFactory</code> to
     *            wait the end of the loading of the stored accounts for
     * @throws InterruptedException
     *             if any thread interrupted the current thread before or while
     *             the current thread was waiting for the loading of the stored
     *             accounts
     */
    public static void startBundleAndWaitStoredAccountsLoaded(final String protocolNameToWait) throws InterruptedException {
        AccountManager accountManager = null;
        final boolean[] storedAccountsAreLoaded = new boolean[1];
        AccountManagerListener listener = new AccountManagerListener()
        {
            public void handleAccountManagerEvent(AccountManagerEvent event)
            {
                if (AccountManagerEvent.STORED_ACCOUNTS_LOADED
                        != event.getType())
                    return;

                ProtocolProviderFactory factory = event.getFactory();

                /*
                 * If the event is for a factory with a protocol name other than
                 * protocolNameToWait, it's not the one we're waiting for.
                 */
                if ((factory != null)
                        && !protocolNameToWait
                                .equals(factory.getProtocolName()))
                    return;

                synchronized (storedAccountsAreLoaded)
                {
                    storedAccountsAreLoaded[0] = true;
                    storedAccountsAreLoaded.notify();
                }
            }
        };

        accountManager.addListener(listener);
    }

    /**
     * Prevents the creation of <code>AccountManagerUtils</code> instances.
     */
    private AccountManagerUtils()
    {
    }
}
