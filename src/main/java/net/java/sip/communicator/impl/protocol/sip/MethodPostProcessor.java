/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Copyright @ 2015 Atlassian Pty Ltd
 * Copyright @ 2017 Alianza, Inc.
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

import javax.sip.ClientTransaction;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.message.Request;
import javax.sip.message.Response;

/**
 * Represents a processor of events with a specific method received in
 * {@link ProtocolProviderAlzService} much like {@link MethodProcessor} but
 * guaranteed to be processed after any registered {@link MethodProcessor}s
 *
 * @author Mike Saavedra
 */
public interface MethodPostProcessor
{

    /**
     * Post-Processes a Request received on a <code>ProtocolProviderAlzService</code> upon which this processor is registered.
     *
     * @param serverTransaction the serverTransaction from the <code>RequestEvent</code> to the processor representing a Request received from the network
     * @param request the request from the <code>RequestEvent</code> to the processor representing a Request received from the network
     * @param event the handle to the event where the request and transaction came from
     * @return <tt>true</tt> if the specified event has been handled by this postprocessor and shouldn't be offered to other processors registered for the same method; <tt>false</tt>, otherwise
     */
    boolean postProcessRequest(ServerTransaction serverTransaction, Request request, RequestEvent event);

    /**
     * Post-Processes a Response received on a <code>ProtocolProviderAlzService</code> upon which this processor is registered.
     *
     * @param clientTransaction the clientTransaction from the <code>ResponseEvent</code> to the processor representing a Response received from the network
     * @param response the response from the <code>ResponseEvent</code> to the processor representing a Response received from the network
     * @param event the handle to the event where the request and transaction came from
     * @return <tt>true</tt> if the specified event has been handled by this postprocessor and shouldn't be offered to other processors registered for the same method; <tt>false</tt>, otherwise
     */
    boolean postProcessResponse(ClientTransaction clientTransaction, Response response, ResponseEvent event);

}
