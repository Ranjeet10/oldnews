package com.bajratechnologies.nagariknews.model.epaper;

import java.util.List;

/**
 * Created by ronem on 2/29/16.
 */
public class EpaperBundle {
//    private Boolean success;
    private String type;
    private List<Epaper> epapers;

    public EpaperBundle(String type, List<Epaper> epapers) {
        this.type = type;
        this.epapers = epapers;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Epaper> getEpapers() {
        return epapers;
    }

    public void setEpapers(List<Epaper> epapers) {
        this.epapers = epapers;
    }
}
