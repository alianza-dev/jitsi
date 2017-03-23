/*
    This file is part of Peers, a java SIP softphone.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    Copyright 2010-2013 Yohann Martineau 
*/

package net.sourceforge.peers.media;

import net.sourceforge.peers.Config;
import net.sourceforge.peers.Logger;
import net.sourceforge.peers.rtp.RtpPacket;
import net.sourceforge.peers.rtp.RtpSession;
import net.sourceforge.peers.sdp.Codec;
import net.sourceforge.peers.sip.core.useragent.UserAgent;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class MediaManager {

    public static final int DEFAULT_CLOCK = 8000; // Hz

    private CaptureRtpSender captureRtpSender;
    private IncomingRtpReader incomingRtpReader;
    private RtpSession rtpSession;
    private DtmfFactory dtmfFactory;
    private Logger logger;
    private DatagramSocket datagramSocket;
    private FileReader fileReader;
    private boolean isMediaDebug;
    private String peersHome;
    private MediaMode mediaMode;
    private AbstractSoundManager soundManager;
    private Echo echo;
    private Config config;

    public MediaManager(boolean isMediaDebug, String peersHome, MediaMode mediaMode, AbstractSoundManager soundManager, Echo echo, Config config, Logger logger) {
        this.logger = logger;
        this.isMediaDebug = isMediaDebug;
        this.peersHome = peersHome;
        this.mediaMode = mediaMode;
        this.soundManager = soundManager;
        this.echo = echo;
        this.config = config;
        dtmfFactory = new DtmfFactory();
    }

    private void startRtpSessionOnSuccessResponse(String localAddress,
            String remoteAddress, int remotePort, Codec codec,
            SoundSource soundSource) {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(localAddress);
        } catch (UnknownHostException e) {
            logger.error("unknown host: " + localAddress, e);
            return;
        }
        
        rtpSession = new RtpSession(inetAddress, datagramSocket,
                isMediaDebug, logger, peersHome);
        
        try {
            inetAddress = InetAddress.getByName(remoteAddress);
            rtpSession.setRemoteAddress(inetAddress);
        } catch (UnknownHostException e) {
            logger.error("unknown host: " + remoteAddress, e);
        }
        rtpSession.setRemotePort(remotePort);
        
        
        try {
            captureRtpSender = new CaptureRtpSender(rtpSession,
                    soundSource, isMediaDebug, codec, logger,
                    peersHome);
        } catch (IOException e) {
            logger.error("input/output error", e);
            return;
        }

        try {
            captureRtpSender.start();
        } catch (IOException e) {
            logger.error("input/output error", e);
        }
    }

    public void successResponseReceived(String localAddress,
            String remoteAddress, int remotePort, Codec codec) {
        switch (mediaMode) {
        case captureAndPlayback:
            AbstractSoundManager soundManager = this.soundManager;
            soundManager.init();
            startRtpSessionOnSuccessResponse(localAddress, remoteAddress,
                    remotePort, codec, soundManager);
            
            try {
                incomingRtpReader = new IncomingRtpReader(
                        captureRtpSender.getRtpSession(), soundManager, codec,
                        logger);
            } catch (IOException e) {
                logger.error("input/output error", e);
                return;
            }

            incomingRtpReader.start();
            break;

        case echo:
            Echo echo;
            try {
                echo = new Echo(datagramSocket, remoteAddress, remotePort,
                        logger);
            } catch (UnknownHostException e) {
                logger.error("unknown host amongst "
                        + localAddress + " or " + remoteAddress);
                return;
            }
            this.echo = echo;
            Thread echoThread = new Thread(echo, Echo.class.getSimpleName());
            echoThread.start();
            break;
        case file:
            String fileName = config.getMediaFile();
            fileReader = new FileReader(fileName, logger);
            startRtpSessionOnSuccessResponse(localAddress, remoteAddress,
                    remotePort, codec, fileReader);
            try {
                incomingRtpReader = new IncomingRtpReader(
                        captureRtpSender.getRtpSession(), null, codec,
                        logger);
            } catch (IOException e) {
                logger.error("input/output error", e);
                return;
            }

            incomingRtpReader.start();
            break;
        case none:
        default:
            break;
        }
    }

    private void startRtpSession(String destAddress, int destPort,
        Codec codec, SoundSource soundSource) {
        rtpSession = new RtpSession(config
                .getLocalInetAddress(), datagramSocket,
                isMediaDebug, logger, peersHome);

        try {
            InetAddress inetAddress = InetAddress.getByName(destAddress);
            rtpSession.setRemoteAddress(inetAddress);
        } catch (UnknownHostException e) {
            logger.error("unknown host: " + destAddress, e);
        }
        rtpSession.setRemotePort(destPort);
        
        try {
            captureRtpSender = new CaptureRtpSender(rtpSession,
                    soundSource, isMediaDebug, codec, logger,
                    peersHome);
        } catch (IOException e) {
            logger.error("input/output error", e);
            return;
        }
        try {
            captureRtpSender.start();
        } catch (IOException e) {
            logger.error("input/output error", e);
        }

    }

    public void handleAck(String destAddress, int destPort, Codec codec) {
        switch (mediaMode) {
        case captureAndPlayback:

            AbstractSoundManager soundManager = this.soundManager;
            soundManager.init();

            startRtpSession(destAddress, destPort, codec, soundManager);

            try {
                //FIXME RTP sessions can be different !
                incomingRtpReader = new IncomingRtpReader(rtpSession,
                        soundManager, codec, logger);
            } catch (IOException e) {
                logger.error("input/output error", e);
                return;
            }

            incomingRtpReader.start();

            break;
        case echo:
            Echo echo;
            try {
                echo = new Echo(datagramSocket, destAddress, destPort, logger);
            } catch (UnknownHostException e) {
                logger.error("unknown host amongst "
                        + config.getLocalInetAddress()
                            .getHostAddress() + " or " + destAddress);
                return;
            }
            this.echo = echo;
            Thread echoThread = new Thread(echo, Echo.class.getSimpleName());
            echoThread.start();
            break;
        case file:
            if (fileReader != null) {
                fileReader.close();
            }
            String fileName = config.getMediaFile();
            fileReader = new FileReader(fileName, logger);
            startRtpSession(destAddress, destPort, codec, fileReader);
            try {
                incomingRtpReader = new IncomingRtpReader(rtpSession,
                        null, codec, logger);
            } catch (IOException e) {
                logger.error("input/output error", e);
                return;
            }
            incomingRtpReader.start();
            break;
        case none:
        default:
            break;
        }
    }

    public void updateRemote(String destAddress, int destPort, Codec codec) {
        switch (mediaMode) {
        case captureAndPlayback:
            try {
                InetAddress inetAddress = InetAddress.getByName(destAddress);
                rtpSession.setRemoteAddress(inetAddress);
            } catch (UnknownHostException e) {
                logger.error("unknown host: " + destAddress, e);
            }
            rtpSession.setRemotePort(destPort);
            break;
        case echo:
            //TODO update echo socket
            break;
        case file:
            try {
                InetAddress inetAddress = InetAddress.getByName(destAddress);
                rtpSession.setRemoteAddress(inetAddress);
            } catch (UnknownHostException e) {
                logger.error("unknown host: " + destAddress, e);
            }
            rtpSession.setRemotePort(destPort);
            break;

        default:
            break;
        }

    }
    
    public void sendDtmf(char digit) {
        if (captureRtpSender != null) {
            List<RtpPacket> rtpPackets = dtmfFactory.createDtmfPackets(digit);
            RtpSender rtpSender = captureRtpSender.getRtpSender();
            rtpSender.pushPackets(rtpPackets);
        }
    }

    public void stopSession() {
        if (rtpSession != null) {
            rtpSession.stop();
            while (!rtpSession.isSocketClosed()) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    logger.debug("sleep interrupted");
                }
            }
            rtpSession = null;
        }
        if (incomingRtpReader != null) {
            incomingRtpReader = null;
        }
        if (captureRtpSender != null) {
            captureRtpSender.stop();
            captureRtpSender = null;
        }
        if (datagramSocket != null) {
            datagramSocket = null;
        }

        switch (mediaMode) {
        case captureAndPlayback:
            AbstractSoundManager soundManager = this.soundManager;
            if (soundManager != null) {
                soundManager.close();
            }
            break;
        case echo:
            Echo echo = this.echo;
            if (echo != null) {
                echo.stop();
                this.echo = null;
            }
            break;
        case file:
            fileReader.close();
            break;
        default:
            break;
        }
    }

    public void setDatagramSocket(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    public DatagramSocket getDatagramSocket() {
        return datagramSocket;
    }

    public FileReader getFileReader() {
        return fileReader;
    }

}
