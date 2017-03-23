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
package net.java.sip.communicator.service.resources;

import net.java.sip.communicator.util.Logger;
import org.jitsi.service.resources.ResourceManagementService;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * The abstract class for ResourceManagementService. It implements default behaviour for most methods.
 */
public abstract class AbstractResourcesService implements ResourceManagementService
{
    /**
     * The logger
     */
    private static final Logger logger =
            Logger.getLogger(AbstractResourcesService.class);

    /**
     * Resources for currently loaded <tt>SettingsPack</tt>.
     */
    private Map<String, String> settingsResources;

    /**
     * Resources for currently loaded <tt>LanguagePack</tt>.
     */
    private Map<String, String> languageResources;

    /**
     * The {@link Locale} of <code>languageResources</code> so that the caching
     * of the latter can be used when a string with the same <code>Locale</code>
     * is requested.
     */
    private Locale languageLocale;

    /**
     * Resources for currently loaded <tt>ImagePack</tt>.
     */
    private Map<String, String> imageResources;

    /**
     * Resources for currently loaded <tt>ColorPack</tt>.
     */
    private Map<String, String> colorResources;

    /**
     * Resources for currently loaded <tt>SoundPack</tt>.
     */
    private Map<String, String> soundResources;

    /**
     * Creates an instance of <tt>AbstractResourcesService</tt>.
     *
     */
//    public AbstractResourcesService(BundleContext bundleContext)
    public AbstractResourcesService()
    {
    }

    /**
     * Method is invoked when the SkinPack is loaded or unloaded.
     */
    protected abstract void onSkinPackChanged();

    /**
     * All the locales in the language pack.
     * @return all the locales this Language pack contains.
     */
    public Iterator<Locale> getAvailableLocales()
    {
        return null;
    }

    /**
     * Returns an internationalized string corresponding to the given key.
     *
     * @param key The identifier of the string in the resources properties file.
     * @return An internationalized string corresponding to the given key.
     */
    public String getI18NString(String key)
    {
        return getI18NString(key, null, Locale.getDefault());
    }

    /**
     * Returns an internationalized string corresponding to the given key.
     *
     * @param key The identifier of the string.
     * @param params the parameters to pass to the localized string
     * @return An internationalized string corresponding to the given key.
     */
    public String getI18NString(String key, String[] params)
    {
        return getI18NString(key, params, Locale.getDefault());
    }

    /**
     * Returns an internationalized string corresponding to the given key.
     *
     * @param key The identifier of the string in the resources properties file.
     * @param locale The locale.
     * @return An internationalized string corresponding to the given key and
     * given locale.
     */
    public String getI18NString(String key, Locale locale)
    {
        return getI18NString(key, null, locale);
    }

    /**
     * Does the additional processing on the resource string. It removes "&"
     * marks used for mnemonics and other characters.
     *
     * @param resourceString the resource string to be processed
     * @return the processed string
     */
    private String processI18NString(String resourceString)
    {
        if(resourceString == null)
            return null;

        int mnemonicIndex = resourceString.indexOf('&');

        if (mnemonicIndex == 0
                || (mnemonicIndex > 0
                && resourceString.charAt(mnemonicIndex - 1) != '\\'))
        {
            String firstPart = resourceString.substring(0, mnemonicIndex);
            String secondPart = resourceString.substring(mnemonicIndex + 1);

            resourceString = firstPart.concat(secondPart);
        }

        if (resourceString.indexOf('\\') > -1)
        {
            resourceString = resourceString.replaceAll("\\\\", "");
        }

        if (resourceString.indexOf("''") > -1)
        {
            resourceString = resourceString.replaceAll("''", "'");
        }

        return resourceString;
    }

    /**
     * Returns an internationalized string corresponding to the given key.
     *
     * @param key The identifier of the string in the resources properties
     * file.
     * @param params the parameters to pass to the localized string
     * @param locale The locale.
     * @return An internationalized string corresponding to the given key.
     */
    public String getI18NString(String key, String[] params, Locale locale)
    {
        String resourceString = null;
        if (resourceString == null)
        {
            logger.warn("Missing resource for key: " + key);
            return '!' + key + '!';
        }

        if(params != null)
        {
            resourceString
                    = MessageFormat.format(resourceString, (Object[]) params);
        }

        return processI18NString(resourceString);
    }

    /**
     * Returns the character after the first '&' in the internationalized
     * string corresponding to <tt>key</tt>
     *
     * @param key The identifier of the string in the resources properties file.
     * @return the character after the first '&' in the internationalized
     * string corresponding to <tt>key</tt>.
     */
    public char getI18nMnemonic(String key)
    {
        return getI18nMnemonic(key, Locale.getDefault());
    }

    /**
     * Returns the character after the first '&' in the internationalized
     * string corresponding to <tt>key</tt>
     *
     * @param key The identifier of the string in the resources properties file.
     * @param locale The locale that we'd like to receive the result in.
     * @return the character after the first '&' in the internationalized
     * string corresponding to <tt>key</tt>.
     */
    public char getI18nMnemonic(String key, Locale locale)
    {
        String resourceString = null;

        if (resourceString == null)
        {
            logger.warn("Missing resource for key: " + key);
            return 0;
        }

        int mnemonicIndex = resourceString.indexOf('&');
        if (mnemonicIndex > -1 && mnemonicIndex < resourceString.length() - 1)
        {
            return resourceString.charAt(mnemonicIndex + 1);
        }

        return 0;
    }

    /**
     * Returns the string value of the corresponding configuration key.
     *
     * @param key The identifier of the string in the resources properties file.
     * @return the string of the corresponding configuration key.
     */
    public String getSettingsString(String key)
    {
        return (settingsResources == null) ? null : settingsResources.get(key);
    }

    /**
     * Returns the int value of the corresponding configuration key.
     *
     * @param key The identifier of the string in the resources properties file.
     * @return the int value of the corresponding configuration key.
     */
    public int getSettingsInt(String key)
    {
        String resourceString = getSettingsString(key);

        if (resourceString == null)
        {
            logger.warn("Missing resource for key: " + key);
            return 0;
        }

        return Integer.parseInt(resourceString);
    }

    /**
     * Returns an <tt>URL</tt> from a given identifier.
     *
     * @param urlKey The identifier of the url.
     * @return The url for the given identifier.
     */
    public URL getSettingsURL(String urlKey)
    {
        String path = getSettingsString(urlKey);

        if (path == null || path.length() == 0)
        {
            logger.warn("Missing resource for key: " + urlKey);
            return null;
        }
        return null;
    }

    /**
     * Returns a stream from a given identifier.
     *
     * @param streamKey The identifier of the stream.
     * @return The stream for the given identifier.
     */
    public InputStream getSettingsInputStream(String streamKey)
    {
        return null;
    }

    /**
     * Returns a stream from a given identifier, obtained through the class
     * loader of the given resourceClass.
     *
     * @param streamKey The identifier of the stream.
     * @param resourceClass the resource class through which the resource would
     * be obtained
     * @return The stream for the given identifier.
     */
    public InputStream getSettingsInputStream(  String streamKey,
                                                Class<?> resourceClass)
    {
        String path = getSettingsString(streamKey);

        if (path == null || path.length() == 0)
        {
            logger.warn("Missing resource for key: " + streamKey);
            return null;
        }

        return resourceClass.getClassLoader().getResourceAsStream(path);
    }

    /**
     * Returns the image path corresponding to the given key.
     *
     * @param key The identifier of the image in the resource properties file.
     * @return the image path corresponding to the given key.
     */
    public String getImagePath(String key)
    {
        return (imageResources == null) ? null : imageResources.get(key);
    }

    /**
     * Loads an image from a given image identifier.
     *
     * @param imageID The identifier of the image.
     * @return The image for the given identifier.
     */
    public byte[] getImageInBytes(String imageID)
    {
        InputStream in = getImageInputStream(imageID);

        if(in == null)
            return null;

        byte[] image = null;

        try
        {
            image = new byte[in.available()];
            in.read(image);
        }
        catch (IOException e)
        {
            logger.error("Failed to load image:" + imageID, e);
        }

        return image;
    }

    /**
     * Loads an image from a given image identifier.
     *
     * @param imageID The identifier of the image.
     * @return The image for the given identifier.
     */
    public ImageIcon getImage(String imageID)
    {
        URL imageURL = getImageURL(imageID);

        return (imageURL == null) ? null : new ImageIcon(imageURL);
    }

    /**
     * Returns the path of the sound corresponding to the given
     * property key.
     *
     * @param soundKey the key, for the sound path
     * @return the path of the sound corresponding to the given
     * property key.
     */
    public String getSoundPath(String soundKey)
    {
        return soundResources.get(soundKey);
    }

    /**
     * Resources for currently loaded <tt>ColorPack</tt>.
     *
     * @return the currently color resources
     */
    protected Map<String, String> getColorResources()
    {
        return colorResources;
    }

}
