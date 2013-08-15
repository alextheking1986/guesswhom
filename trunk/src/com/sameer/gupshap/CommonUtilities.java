package com.sameer.gupshap;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {
	
	// give your server registration url here
    static final String SERVER_URL = "http://paradigmcraft.com/gcm_server_php/register.php"; 
    static final String SEND_URL = "http://paradigmcraft.com/gcm_server_php/send_message.php";

    // Google project id
    static final String SENDER_ID = "100975445587"; 

    /**
     * Tag used on log messages.
     */
    static final String TAG = "sameer";
    static final String TAG_ID = "regId";
    static final String TAG_MSG = "message";
    static final String GAME_REQUEST="#game#chlng#";
    static final String GAME_ACCEPT="#chlng#acptd#";
    static final String GAME_REJECT="#chlng#rjctd#";
    static final String GAME_ACK="#ack#ok#";

    static final String DISPLAY_MESSAGE_ACTION =
            "com.sameer.gupshap.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
