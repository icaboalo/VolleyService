package com.icaboalo.dccomicslist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by icaboalo on 9/11/2015.
 */
public class JusticeLeague {
    String heroId;
    String heroName;
    String heroDescription;
    List<String> heroList;
    String heroImage;
    String heroImage2;

    public JusticeLeague(String heroId, String heroName) {
        this.heroId = heroId;
        this.heroName = heroName;
    }



    public JusticeLeague(String heroName) {
        this.heroName = heroName;
        heroList = new ArrayList<>();
    }

    public JusticeLeague(String heroId, String heroName, String heroDescription, String heroImage2) {
        this.heroId = heroId;
        this.heroName = heroName;
        this.heroDescription = heroDescription;
        this.heroImage2 = heroImage2;
        heroList = new ArrayList<>();
    }


    public void addImage(String urlImage) {
        heroList.add(urlImage);
    }

    public String getHeroId() {
        return heroId;
    }

    public String getHeroName() {
        return heroName;
    }

    public String getHeroDescription() {
        return heroDescription;
    }

    public String getHeroImage2() {
        return heroImage2;
    }

    public void setHeroImage2(String heroImage2) {
        this.heroImage2 = heroImage2;
    }

    public String getHeroImage() {
        return heroImage;
    }

    public void setHeroImage(String heroImage) {
        this.heroImage = heroImage;
    }

    public List<String> getHeroList() {
        return heroList;
    }
}
