package com.bidhee.greendaogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.bidhee.nagariknews.nagarikdb");

        Entity newsMapObj = schema.addEntity("News");
        newsMapObj.addIdProperty();
        newsMapObj.addStringProperty("newsId");
        newsMapObj.addStringProperty("newsCategory");
        newsMapObj.addStringProperty("newsTitle");
        newsMapObj.addStringProperty("newsDesc");
        newsMapObj.addStringProperty("newsDate");
        newsMapObj.addStringProperty("newsImage");
        newsMapObj.addStringProperty("reportedBy");

        new DaoGenerator().generateAll(schema, "./app/src/main/java");
    }
}
