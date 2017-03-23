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

import gov.nist.javax.sip.header.HeaderFactoryImpl;
import net.java.sip.communicator.service.protocol.CallPeer;
import net.java.sip.communicator.service.protocol.OperationFailedException;
import net.java.sip.communicator.service.protocol.OperationSetTelephonyPark;
import net.java.sip.communicator.util.Logger;

import javax.sip.Dialog;
import javax.sip.address.Address;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import java.text.ParseException;

/**
 * Provides operations necessary to park calls. Using rfc5359 as a reference.
 *
 * @author Damian Minkov
 */
public class OperationSetTelephonyParkSipImpl
    implements OperationSetTelephonyPark
{
    /**
     * Our class logger.
     */
    private static final Logger logger =
        Logger.getLogger(OperationSetTelephonyParkSipImpl.class);

    /**
     * A reference to the <tt>ProtocolProviderAlzService</tt> instance that
     * created us.
     */
    private final ProtocolProviderAlzService protocolProvider;

    /**
     * Constructs <tt>OperationSetTelephonyParkSipImpl</tt>.
     * @param provider the parent provider.
     */
    OperationSetTelephonyParkSipImpl(ProtocolProviderAlzService provider)
    {
        this.protocolProvider = provider;
    }

    /**
     * Parks an already existing call to the specified parkSlot.
     *
     * @param parkSlot the parking slot where to park the call.
     * @param peer the <tt>CallPeer</tt> to be parked to the specified
     * parking slot.
     * @throws OperationFailedException if parking the specified call to the
     * specified park slot fails
     */
    @Override
    public void parkCall(String parkSlot, CallPeer peer)
        throws OperationFailedException
    {
        CallPeerSipImpl sipPeer = (CallPeerSipImpl) peer;
        Dialog dialog = sipPeer.getDialog();
        Request refer = protocolProvider.getMessageFactory()
            .createRequest(dialog, Request.REFER);
        HeaderFactory headerFactory = protocolProvider.getHeaderFactory();

        String addressString = parkSlot;

        String callParkPrefix = protocolProvider.getAccountID()
            .getAccountPropertyString(CALL_PARK_PREFIX_PROPERTY, null);

        if(callParkPrefix != null)
            addressString = callParkPrefix + addressString;

        Address address = null;
        try
        {
            address = protocolProvider.parseAddressString(addressString);
        }
        catch (ParseException ex)
        {
            ProtocolProviderAlzService.throwOperationFailedException(
                "Failed to parse address string " + addressString,
                OperationFailedException.ILLEGAL_ARGUMENT, ex, logger);
        }

        // Refer-To is required.
        refer.setHeader(headerFactory.createReferToHeader(address));

        /*
         * Referred-By is optional but only to the extent that the refer target
         * may choose to require a valid Referred-By token.
         */
        refer.addHeader( ((HeaderFactoryImpl) headerFactory)
            .createReferredByHeader(dialog.getLocalParty()));

        protocolProvider.sendInDialogRequest(
            sipPeer.getJainSipProvider(), refer, dialog);
    }
}
