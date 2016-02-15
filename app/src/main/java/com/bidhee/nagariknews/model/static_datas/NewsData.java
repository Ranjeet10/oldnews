package com.bidhee.nagariknews.model.static_datas;

import android.content.Context;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.model.NewsObj;

import java.util.ArrayList;

/**
 * Created by ronem on 2/14/16.
 */
public class NewsData {
    public static ArrayList<NewsObj> getNewsRepublica(Context context) {

        ArrayList<NewsObj> newsObjs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            newsObjs.add(new NewsObj("12",
                    "",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQnqivhS7umB9iog3r5wL5k5pNZPF_jOtvEIe6dii7csdFmmPwN",
                    context.getResources().getString(R.string.news_title_republica),
                    "Andy Rubin",
                    "Jan 20 2016",
                    context.getResources().getString(R.string.news_description_republica)));
        }
        return newsObjs;

    }

    public static ArrayList<NewsObj> getNewsNagarik(Context context) {

        ArrayList<NewsObj> newsObjs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            newsObjs.add(new NewsObj("12",
                    "",
                    "http://nagariknews.com/media/k2/items/cache/xaafbf109d9cc513c903b1a05e07fc919_L.jpg.pagespeed.ic.T8f9vg-kZj.webp",
                    context.getResources().getString(R.string.news_title_nagarik),
                    "कल्पना पौडेल",
                    "आइतबार २ फागुन, २०७२",
                    context.getResources().getString(R.string.news_description_nagarik)));
        }
        return newsObjs;

    }
}
