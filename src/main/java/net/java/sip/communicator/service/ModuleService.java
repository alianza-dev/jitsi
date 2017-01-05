package net.java.sip.communicator.service;

/**
 * Created by msaavedra on 1/4/17.
 */
public interface ModuleService {
    /**
     * Starts the service. This method blocks until the service has completely started.
     */
    void start() throws Exception;

    /**
     * Stops the service. This method blocks until the service has completely shut down.
     */
    void stop();
}
