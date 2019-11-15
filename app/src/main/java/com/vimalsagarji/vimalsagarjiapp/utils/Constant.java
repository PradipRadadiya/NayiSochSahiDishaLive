package com.vimalsagarji.vimalsagarjiapp.utils;

import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;

/**
 * Created by BHARAT on 09/02/2016.
 */
@SuppressWarnings("ALL")
public class Constant {
//    public static final String ALL_THOUGHTS_URL = "http://www.aacharyavimalsagarsuriji.com/vimalsagarji/thought/getallthoughtsbycidyear/?page=1";
    public static final String ALL_THOUGHTS_URL = CommonUrl.Main_url+"thought/getallthoughts/?page=1";

    public static final String ALL_BYPEOPLE_URL = CommonUrl.Main_url+"bypeople/getallappposts/?page=1";
//    public static final String ALL_BYPEOPLE_URL = "http://www.aacharyavimalsagarsuriji.com/vimalsagarji/bypeople/getallapppostsyear/?page=1";

    public static final String ALL_Audio_URL = CommonUrl.Main_url+"audio/getaudiobycategoryyear/?page=1&psize=1000";

    public static final String ImgURL = CommonUrl.Main_url+"static/audioimage/";

    public static final String AudioPath = CommonUrl.Main_url+"static/audios/";

    public static final String POST_DATA_URL = CommonUrl.Main_url+"bypeople/addpost/";

    public static final String GET_ALL_OPINION_POLL = CommonUrl.Main_url+"opinionpoll/getallpoll/?page=1&psize=1000";

    public static final String GET_ALLINFORMATION_CATEGORY = CommonUrl.Main_url+"info/getallinfo/?page=1";

    public static final String GET_WEEKINFORMATION_CATEGORY = CommonUrl.Main_url+"info/getallinfobycidweek/?page=1";

    public static final String GET_TODAYINFORMATION_CATEGORY = CommonUrl.Main_url+"info/getallinfobycidtoday/?page=1";

    public static final String GET_THISMONTH_INFORMATION_CATEGORY = CommonUrl.Main_url+"info/getallinfobycidmonth/?page=1&psize=1000";

    public static final String GET_ALLCOMMENT_INFORMATION_CATEGORY = CommonUrl.Main_url+"info/getallcomments/?page=1&psize=1000";

    public static final String POST_COMMENT_DATA_INFO = CommonUrl.Main_url+"info/comment/";

    public static final String GET_ALL_VIDEO_CATEGORY = CommonUrl.Main_url+"video/getallcategory";

    public static final String GET_ALLYEAR_EVENT_DATA = CommonUrl.Main_url+"event/geteventsbycategor/?cid=1&page=1&psize=1000";
//    public static final String GET_ALLYEAR_EVENT_DATA = "http://www.aacharyavimalsagarsuriji.com/vimalsagarji/event/geteventsbycategoryyear/?cid=1&page=1&psize=1000";

    public static final String POST_REGISTER_DATA = CommonUrl.Main_url+"userregistration/adduser/";

    public static final String USER_ALREADY_REGISTER = CommonUrl.Main_url+"userregistration/checkuser/";

    public static final String COUNT_LIKE = CommonUrl.Main_url+"info/countlikes/";

    public static final String LIKE_INFO = CommonUrl.Main_url+"info/likeinfo/";

    public static final String EVENT_LIKE_URL = CommonUrl.Main_url+"event/eventlike/";

    public static final String COUNT_EVENT_LIKE = CommonUrl.Main_url+"event/countlikes";

    public static final String COMMENT_EVENT_URL = CommonUrl.Main_url+"event/comment/?page=1&psize=1000";

    public static final String POST_COMMENT_URL = CommonUrl.Main_url+"event/comment/";

    public static final String GET_ALL_CATEGORY_AUDIO = CommonUrl.Main_url+"audio/getallcategory";

    public static final String GET_AUDIO_IMAGE_URL = CommonUrl.Main_url+"static/audiocategory/";

    public static final String AUDIO_POST_COMMENT = CommonUrl.Main_url+"audio/comment/";

    public static final String GET_ALL_COMMENT_AUDIO = CommonUrl.Main_url+"audio/getallcommentsforadmin/";

    public static final String LIKE_AUDIO = CommonUrl.Main_url+"audio/audiolike/";

    public static final String COUNT_LIKE_AUDIO = CommonUrl.Main_url+"audio/countlikes/";

    public static final String SEARCHALLINFORMATION = CommonUrl.Main_url+"info/searchallinfo/?page=1&psize=1000";

    public static final String SEARCHTODAYINFORMATION = CommonUrl.Main_url+"info/searchallinfobycidtoday/?page=1&psize=1000";

    public static final String SEARCHMONTHINFORMATION = CommonUrl.Main_url+"info/searchallinfobycidmonth/?page=1&psize=1000";

    public static final String SEARCHALLEVENT = CommonUrl.Main_url+"event/searchalleventsbycategory/?page=1&psize=1000";


}
