package com.devool.ucicareconnect.pubnub;

/**
 * Created by GleasonK on 6/8/15.
 *
 * Constants used by this chatting application.
 * TODO: Replace PUBLISH_KEY and SUBSCRIBE_KEY with your personal keys.
 * TODO: Register app for GCM and replace GCM_SENDER_ID
 */
public class Constants {
    //public static final String PUBLISH_KEY   = "pub-c-7fad26fe-6c38-4940-b9c3-fbd19a9633af";

   /* public static final String PUBLISH_KEY   = "pub-c-fe60596b-d9d9-4bc1-b51d-14e1abe41fc8";
    public static final String SUBSCRIBE_KEY = "sub-c-d9af3890-b586-11e8-891e-be748cd08d1d";*/

    public static final String PUBLISH_KEY   = "pub-c-c937656a-c96a-476a-9c72-fec701f800e8";
    public static final String SUBSCRIBE_KEY = "sub-c-9894060a-b4e7-11e8-acd6-a622109c830d";
    public static final String CIPHER_KEY = "my_cipherkey";

    public static final String CHAT_PREFS    = "user_info";
    public static final String CHAT_USERNAME = "USER_NAME";
    public static final String USER_NAME_FROM_ACTIVATION_CODE = "USER_NAME_FROM_ACTIVATION_CODE";
    public static final String CHAT_ROOM     = "me.kevingleason.CHAT_ROOM";

    public static final String JSON_GROUP = "groupMessage";
    public static final String JSON_DM    = "directMessage";
    public static final String JSON_USER  = "sendername";
    public static final String JSON_MSG   = "text";
    public static final String JSON_TIME  = "date";
    public static final String JSON_SENDER_ID = "senderid";
    public static final String JSON_DEVICE_TYPE = "devicetype";
    public static final String JSON_MESSAGE_TYPE = "MsgType";




    public static final String STATE_LOGIN = "loginTime";

    public static final String GCM_REG_ID    = "gcmRegId";
    //public static final String GCM_SENDER_ID = "709361095668"; // Get this from
    public static final String GCM_SENDER_ID = "938944380642"; // Get this from
    public static final String GCM_POKE_FROM = "gcmPokeFrom"; // Get this from
    public static final String GCM_CHAT_ROOM = "gcmChatRoom"; // Get this from
    public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
}
