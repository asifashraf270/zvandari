package com.glowingsoft.zvandiri.Utils;

/**
 * Created by cwtausif on 2/10/2019.
 */

public class ServerConnection {
    public static String connectionIp = "http://hub.zvandiri.org/admin/api/";
    public static String Login = "login";
    public static String profile = connectionIp + "profile";
    public static String getQuizCategories = "getQuizCategories";
    public static String getQuizByCategory = connectionIp + "getQuizByCategory";
    public static String getAudios = connectionIp + "getAudios";
    public static String youtubeEndPointForChannel = "https://www.googleapis.com/youtube/v3/search?channelId=UC3SlRJmM33bBOUXkUMp4ukw&part=snippet,id&order=date&key=";
    public static String apiKey = "AIzaSyDNWd0bC1jCTIc3Of2Xr_yfltn6J1159X8";
    public static String endpoint = youtubeEndPointForChannel + apiKey;
    public static String videoSearch = "https://www.youtube.com/watch?v=";
    public static String updateProfile = "updateprofile";
    public static String updateProfileEndPoint = connectionIp + updateProfile;
    public static String updateProfileImage = "updateProfilePicture";
    public static String updateProfileImageEndPoint = connectionIp + updateProfileImage;
    public static String getHealthTracker = connectionIp + "getHealthTracker";
    public static String updateHealth = connectionIp + "updateHealth";
    public static String contactUs = connectionIp + "contactUs";
    public static String forgotPassword = connectionIp + "forgotPassword";
    public static String getPages = connectionIp + "getPages";

}
