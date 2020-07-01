package com.example.meditation.utils;

import java.util.Random;

public class VideoIdsProvider {
    private static String[] videoIds = {"XeXz8fIZDCE", "Nnd5Slo02us", "0cCYQh0czfY", "tTZRx453Lyo","DSfmZHa14uI","-6MGjYmHdYQ","QeYhMXJWpJg","i6EPVHHlFNk","qqhx3bckcjs","tFIrxireDAo","RY2nFv743-A","9FL5WlTXo-0","FSdVBFpT6i4","drBqFWcLEcE"};
//    private static String[] liveVideoIds = {"hHW1oY26kxQ"};
    private static Random random = new Random();

    public static String getNextVideoId() {
        return videoIds[random.nextInt(videoIds.length)];
    }

//    public static String getNextLiveVideoId() {
//        return liveVideoIds[random.nextInt(liveVideoIds.length)];
//    }
}


