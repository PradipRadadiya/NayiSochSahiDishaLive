package com.vimalsagarji.vimalsagarjiapp.fcm;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vimalsagarji.vimalsagarjiapp.ActivityHomeMain;
import com.vimalsagarji.vimalsagarjiapp.activity.commentlist.AudioCommentList;
import com.vimalsagarji.vimalsagarjiapp.activity.commentlist.ByPeopleCommentList;
import com.vimalsagarji.vimalsagarjiapp.activity.commentlist.EventCommentList;
import com.vimalsagarji.vimalsagarjiapp.activity.commentlist.InformationCommentList;
import com.vimalsagarji.vimalsagarjiapp.activity.commentlist.ThoughtCommentList;
import com.vimalsagarji.vimalsagarjiapp.activity.commentlist.VideoCommentList;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.EventCategory;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.model.EventCategoryItem;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.AudioCategoryitem;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.ByPeople;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.CompetitionList;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.EventActivity;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.GalleryCategory;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.InformationCategory;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.OpinionPoll;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.ThoughtsActivity;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.VideoCategoryItem;
import com.vimalsagarji.vimalsagarjiapp.utils.AllQuestionDetail;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;
    Sharedpreferance sharedpreferance;
    public static String questionid;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        sharedpreferance = new Sharedpreferance(MyFirebaseMessagingService.this);
        Log.e(TAG, "From:------------- " + remoteMessage.getFrom());
        Log.e( "data dfgdfgdf------ " ,""+ remoteMessage.getData());
//        Log.e( "notificatioonn------ " ,""+ remoteMessage.getNotification().getTitle());
//        Log.e( "notificatioonn------ " ,""+ remoteMessage.getNotification().getBody());

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message Data Body: " + remoteMessage.getData().toString());
            JSONObject json = new JSONObject(remoteMessage.getData());
            handleDataMessage(json);
//            handleMessage(json);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }
    }

    private void handleMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());
        try {
            Log.e(TAG, "get parameter: " + json.getString("Nick"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());
        try {

            JSONObject data = json;
            String imageUrl = "";
//            String imageUrl = data.getString("image");
            String title = CommonMethod.decodeEmoji(data.getString("title"));

            String click_action = CommonMethod.decodeEmoji(data.getString("click_action"));
            String message = CommonMethod.decodeEmoji(data.getString("message"));
            String categoty_id = CommonMethod.decodeEmoji(data.getString("categoty_id"));
            String category_title = CommonMethod.decodeEmoji(data.getString("category_title"));

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "categoty_id: " + categoty_id);
            Log.e(TAG, "category_title: " + category_title);



            // app is in foreground, broadcast the push message

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
//                // app is in foreground, broadcast the push message
//                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//                pushNotification.putExtra("message", message);
//                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//                // play notification sound
//                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//                notificationUtils.playNotificationSound();

                if (click_action.equalsIgnoreCase("information_click")) {
                    Log.e("click", "--------------information_click");
                    Intent resultIntent = new Intent(getApplicationContext(), InformationCategory.class);
                    resultIntent.putExtra("message", message);

                    Log.e("push", "--------------"+sharedpreferance.getPushNotification());
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {

                            Log.e("info inside","-------------------"+imageUrl);

                            showNotificationMessage(getApplicationContext(), "Information-" + title, message, "false", resultIntent);

                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Information -" + title, message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("event_click")) {
                    Log.e("click", "--------------event_click");

                    Intent resultIntent = new Intent(getApplicationContext(), EventCategory.class);
                    resultIntent.putExtra("message", message);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Event-" + title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Event -" + title, message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("audio_cat_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), AudioCategoryitem.class);
                    resultIntent.putExtra("listTitle", category_title);
                    resultIntent.putExtra("listId", categoty_id);
                    resultIntent.putExtra("listCategoryId", categoty_id);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Audio", category_title + "New audio added", "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Audio", category_title + "- New audio added", "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("vidio_cat_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), VideoCategoryItem.class);
                    resultIntent.putExtra("listTitle", category_title);
                    resultIntent.putExtra("v_cid", categoty_id);
                    resultIntent.putExtra("message", message);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Video ", category_title + "-New video added", "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Video ", category_title + "-New video added", "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("question_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), AllQuestionDetail.class);
                    questionid=categoty_id;
                    resultIntent.putExtra("qid", categoty_id);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Question & Answer", category_title + "-Admin reply.", "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Question & Answer", category_title + "-Admin reply.", "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("thought_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), ThoughtsActivity.class);
                    resultIntent.putExtra("message", message);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Thought-" + title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Thought -" + title, message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("competition_item_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), CompetitionList.class);
                    resultIntent.putExtra("listTitle", category_title);
                    resultIntent.putExtra("categoryID", categoty_id);
                    resultIntent.putExtra("message", message);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Competition", category_title, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Competition", category_title, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }

                } else if (click_action.equalsIgnoreCase("bypeople_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), ByPeople.class);
                    resultIntent.putExtra("message", message);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "ByPeople", "New post add", "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "ByPeople", "New post add", "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("opinionPoll_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), OpinionPoll.class);
                    resultIntent.putExtra("message", message);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Opinion Poll", category_title, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Opinion Poll", category_title, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("gallery_item_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), GalleryCategory.class);
                    resultIntent.putExtra("cid", categoty_id);
                    resultIntent.putExtra("catname", category_title);
                    resultIntent.putExtra("message", message);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Gallery", category_title + "- New image added.", "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Gallery", category_title + "- New image added.", "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("user_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), ActivityHomeMain.class);
                    resultIntent.putExtra("message", message);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), sharedpreferance.getEmail(), "Approved for Admin side", "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), sharedpreferance.getEmail(), "Approved for Admin side", "false", resultIntent, imageUrl);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }

                } else if (click_action.equalsIgnoreCase("information_comment_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), InformationCommentList.class);
                    resultIntent.putExtra("click_action", "information_comment_click");
                    resultIntent.putExtra("listID", categoty_id);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Information comment-"+CommonMethod.decodeEmoji(title), CommonMethod.decodeEmoji(message), "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Information Comment", message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }

                } else if (click_action.equalsIgnoreCase("event_comment_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), EventCommentList.class);
                    resultIntent.putExtra("click_action", "event_comment_click");
                    resultIntent.putExtra("listID", categoty_id);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Event comment-"+title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Event Comment", message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("audio_comment_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), AudioCommentList.class);
                    resultIntent.putExtra("click_action", "audio_comment_click");
                    resultIntent.putExtra("id", categoty_id);
                    resultIntent.putExtra("categoryname", category_title);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Audio comment-"+title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Audio-"+title, message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("video_comment_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), VideoCommentList.class);
                    resultIntent.putExtra("click_action", "video_comment_click");
                    resultIntent.putExtra("id", categoty_id);
                    resultIntent.putExtra("categoryname", category_title);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Video comment-"+title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Video Comment", message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("thought_comment_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), ThoughtCommentList.class);
                    resultIntent.putExtra("click_action", "thought_comment_click");
                    resultIntent.putExtra("thoughtid", categoty_id);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Thought comment-"+title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Thought Comment", message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("bypeople_comment_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), ByPeopleCommentList.class);
                    resultIntent.putExtra("click_action", "bypeople_comment_click");
                    resultIntent.putExtra("postId", categoty_id);
                    resultIntent.putExtra("Name", title);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "By People Comment-"+title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "By People-"+title, message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Intent resultIntent = new Intent(getApplicationContext(), ActivityHomeMain.class);
                    resultIntent.putExtra("message", message);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), title, message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                }

            }

            // app is in background, show the notification in notification tray
            else {

                if (click_action.equalsIgnoreCase("information_click")) {
                    Log.e("click", "--------------information_click");
                    Intent resultIntent = new Intent(getApplicationContext(), InformationCategory.class);
                    resultIntent.putExtra("message", message);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Information-" + title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Information -" + title, message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }

                } else if (click_action.equalsIgnoreCase("event_click")) {
                    Log.e("click", "--------------event_click");

                    Intent resultIntent = new Intent(getApplicationContext(), EventCategory.class);
                    resultIntent.putExtra("message", message);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Event-" + title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Event -" + title, message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("audio_cat_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), AudioCategoryitem.class);
                    resultIntent.putExtra("listTitle", category_title);
                    resultIntent.putExtra("listId", categoty_id);
                    resultIntent.putExtra("listCategoryId", categoty_id);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Audio", category_title + "-New audio added", "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Audio", category_title + "-New audio added", "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("vidio_cat_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), VideoCategoryItem.class);
                    resultIntent.putExtra("listTitle", category_title);
                    resultIntent.putExtra("v_cid", categoty_id);
                    resultIntent.putExtra("message", message);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Video ", category_title + "-New video added", "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Video ", category_title + "-New video added", "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("question_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), AllQuestionDetail.class);
                    resultIntent.putExtra("qid", categoty_id);
                    questionid=categoty_id;
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Question & Answer", category_title, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Question & Answer", category_title, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("thought_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), ThoughtsActivity.class);
                    resultIntent.putExtra("message", message);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Thought-" + title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Thought -" + title, message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("competition_item_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), CompetitionList.class);
                    resultIntent.putExtra("listTitle", category_title);
                    resultIntent.putExtra("categoryID", categoty_id);
                    resultIntent.putExtra("message", message);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Competition", category_title, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Competition", category_title, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }

                } else if (click_action.equalsIgnoreCase("bypeople_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), ByPeople.class);
                    resultIntent.putExtra("message", message);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {
                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "ByPeople", "New post add", "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "ByPeople", "New post add", "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("opinionPoll_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), OpinionPoll.class);
                    resultIntent.putExtra("message", message);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Opinion Poll", category_title, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Opinion Poll", category_title, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("gallery_item_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), GalleryCategory.class);
                    resultIntent.putExtra("cid", categoty_id);
                    resultIntent.putExtra("catname", category_title);
                    resultIntent.putExtra("message", message);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Gallery", category_title + "- New image added.", "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Gallery", category_title + "- New image added", "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("user_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), ActivityHomeMain.class);
                    resultIntent.putExtra("message", message);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), sharedpreferance.getEmail(), "Approved for Admin side", "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), sharedpreferance.getEmail(), "Approved for Admin side", "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("information_comment_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), InformationCommentList.class);
                    resultIntent.putExtra("click_action", "information_comment_click");
                    resultIntent.putExtra("listID", categoty_id);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Information Comment-"+title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Information Comment", message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("event_comment_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), EventCommentList.class);
                    resultIntent.putExtra("click_action", "event_comment_click");
                    resultIntent.putExtra("listID", categoty_id);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {
                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Event Comment-"+title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Event Comment", message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("audio_comment_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), AudioCommentList.class);
                    resultIntent.putExtra("click_action", "audio_comment_click");
                    resultIntent.putExtra("id", categoty_id);
                    resultIntent.putExtra("categoryname", category_title);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Audio Comment-"+title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Audio Comment", message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("video_comment_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), VideoCommentList.class);
                    resultIntent.putExtra("click_action", "video_comment_click");
                    resultIntent.putExtra("id", categoty_id);
                    resultIntent.putExtra("categoryname", category_title);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Video Comment-"+title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Video Comment", message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("thought_comment_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), ThoughtCommentList.class);
                    resultIntent.putExtra("click_action", "thought_comment_click");
                    resultIntent.putExtra("thoughtid", categoty_id);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Thought Comment-"+title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Thought Comment", message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("bypeople_comment_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), ByPeopleCommentList.class);
                    resultIntent.putExtra("click_action", "bypeople_comment_click");
                    resultIntent.putExtra("postId", categoty_id);
                    resultIntent.putExtra("Name", title);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "By People Comment-"+title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "By People Comment", message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Intent resultIntent = new Intent(getApplicationContext(), ActivityHomeMain.class);
                    resultIntent.putExtra("message", message);
                    if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), title, message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }
    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        Log.e(TAG, "show noti: " + title);

        notificationUtils = new NotificationUtils(context);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}
