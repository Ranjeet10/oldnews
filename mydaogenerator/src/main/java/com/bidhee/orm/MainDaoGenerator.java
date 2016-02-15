package com.bidhee.orm;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MainDaoGenerator {
    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "nagarikschema");

        Entity newsTitleTable = schema.addEntity("News");
        newsTitleTable.addIdProperty();
        newsTitleTable.addStringProperty("title");

        new DaoGenerator().generateAll(schema, args[0]);
    }
}
