package com.vimalsagarji.vimalsagarjiapp.jcplayer.JCPlayerExceptions;

/**
 * Created by Pradip on 01/09/16.
 */

@SuppressWarnings("ALL")
public class AudioUrlInvalidException extends Exception {
    public AudioUrlInvalidException(String url) {
        super("The url does not appear valid: " + url);
    }
}
