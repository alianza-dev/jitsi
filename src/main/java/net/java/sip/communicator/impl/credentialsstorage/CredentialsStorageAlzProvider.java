package net.java.sip.communicator.impl.credentialsstorage;

/**
 * Created by msaavedra on 1/9/17.
 */
public class CredentialsStorageAlzProvider {
    public static CredentialsStorageAlzService getCredentialStorageAlzService() {
        return new CredentialsStorageAlzService();
    }
}
