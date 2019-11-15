package com.vimalsagarji.vimalsagarjiapp.retrofit;

import com.google.gson.JsonObject;
import com.vimalsagarji.vimalsagarjiapp.activity.competition.CompetitionWinner;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    //Using for splash screen
    @FormUrlEncoded
    @POST("aluser/checkuser")
    Call<JsonObject> checkUserLogin(@Field("EmailID") String email, @Field("Phone") String phone, @Field("DeviceID") String deviceId);

    @GET("quote/viewQuote")
    Call<JsonObject> getDailyContent();

    //Using for Register screen
    @FormUrlEncoded
    @POST("userregistration/signinsignup")
    Call<JsonObject> userLogin(@Field("Name") String name, @Field("DeviceID") String deviceId, @Field("EmailID") String email, @Field("Address") String address, @Field("Phone") String phone, @Field("DeviceToken") String token);

    //    Favourite item for All module
    @FormUrlEncoded
    @POST("info/addfavorite")
    Call<JsonObject> favInfo(@Field("infoid") String id, @Field("uid") String uid);

    @FormUrlEncoded
    @POST("event/addfavorite")
    Call<JsonObject> favEvent(@Field("eid") String id, @Field("uid") String uid);

    @FormUrlEncoded
    @POST("thought/addfavorite")
    Call<JsonObject> favThought(@Field("tid") String id, @Field("uid") String uid);

    @FormUrlEncoded
    @POST("audio/addfavorite")
    Call<JsonObject> favAudio(@Field("aid") String id, @Field("uid") String uid);

    @FormUrlEncoded
    @POST("video/addfavorite")
    Call<JsonObject> favVideo(@Field("vid") String id, @Field("uid") String uid);

    @FormUrlEncoded
    @POST("questionanswer/addfavorite")
    Call<JsonObject> favQuestion(@Field("qid") String id, @Field("uid") String uid);

    @FormUrlEncoded
    @POST("bypeople/addfavorite")
    Call<JsonObject> favByPeople(@Field("pid") String id, @Field("uid") String uid);


//    Favourite item list for  All module

    @FormUrlEncoded
    @POST("info/getfavoriteinfo")
    Call<JsonObject> favInfoList(@Field("uid") String id, @Field("page") String page, @Field("psize") String psize);

    @FormUrlEncoded
    @POST("event/getfavoriteevent")
    Call<JsonObject> favEventList(@Field("uid") String id, @Field("page") String page, @Field("psize") String psize);

    @FormUrlEncoded
    @POST("thought/getfavoritethought")
    Call<JsonObject> favThoughtList(@Field("uid") String id, @Field("page") String page, @Field("psize") String psize);

    @FormUrlEncoded
    @POST("audio/getfavoriteaudio")
    Call<JsonObject> favAudioList(@Field("uid") String id, @Field("page") String page, @Field("psize") String psize);

    @FormUrlEncoded
    @POST("video/getfavoritevideo")
    Call<JsonObject> favVideoList(@Field("uid") String id, @Field("page") String page, @Field("psize") String psize);

    @FormUrlEncoded
    @POST("questionanswer/getfavoritequestionans")
    Call<JsonObject> favQuestionList(@Field("uid") String id, @Field("page") String page, @Field("psize") String psize);

    @FormUrlEncoded
    @POST("bypeople/getfavoritebypeople")
    Call<JsonObject> favByPeopleList(@Field("uid") String id, @Field("page") String page, @Field("psize") String psize);

    @FormUrlEncoded
    @POST("competition/getcompetitionwinner")
    Call<CompetitionWinner> getCompetitionWinner(@Field("from_date") String from_date,
                                                 @Field("to_date") String to_date,
                                                 @Field("attended_competition") String attended_competition,
                                                 @Field("percentage") String percentage);



    @GET("competition/getcompetitionresultnote")
    Call<JsonObject> getCompetitionAlert();



}
