package net.java.sip.communicator.impl.configuration;

/**
 * Created by msaavedra on 1/9/17.
 */
public class ConfigurationAlzProvider {
    private static JitsiConfigurationAlzService jitsiConfigurationAlzService;
    private static LibJitsiConfigurationAlzService libJitsiConfigurationAlzService;

    public static synchronized LibJitsiConfigurationAlzService getLibJitsiConfigurationAlzService() {
        if (libJitsiConfigurationAlzService == null) {
            libJitsiConfigurationAlzService = new LibJitsiConfigurationAlzService();
        }
        return libJitsiConfigurationAlzService;
    }

    public static synchronized JitsiConfigurationAlzService getJitsiConfigurationAlzService() {
        if (jitsiConfigurationAlzService == null) {
            jitsiConfigurationAlzService = new JitsiConfigurationAlzService();
        }
        return jitsiConfigurationAlzService;
    }

}
