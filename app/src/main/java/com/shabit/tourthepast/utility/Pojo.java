package com.shabit.tourthepast.utility;

public class Pojo {

    private int id;
    private String CatId;
    private String CId;
    private String CategoryName;
    private String TtpHeading;
    private String TtpImage;
    private String TtpDesc;
    private String TtpCont;
    private String TtpIngre;
    private String TtpCalories;
    private String TtpFat;
    private String TtpProteins;
    private String TtpCarbs;
    private String TtpTime;

    public Pojo() {

    }

    public Pojo(String CatId) {
        this.CatId = CatId;
    }

    public Pojo(String catid, String cid, String categoryname, String ttpheading, String ttpimage, String ttpdesc, String ttpcont, String ttpingre, String ttpcalories, String ttpfat, String ttpcarbs, String ttpproteins, String ttptime) {
        this.CatId = catid;
        this.CId = cid;
        this.CategoryName = categoryname;
        this.TtpHeading = ttpheading;
        this.TtpImage = ttpimage;
        this.TtpDesc = ttpdesc;
        this.TtpCont = ttpcont;
        this.TtpIngre = ttpingre;
        this.TtpProteins = ttpproteins;
        this.TtpCarbs = ttpcarbs;
        this.TtpFat = ttpfat;
        this.TtpCalories = ttpcalories;
        this.TtpTime = ttptime;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getCatId() {
        return CatId;
    }

    public void setCatId(String catid) {
        this.CatId = catid;
    }

    public String getCId() {
        return CId;
    }

    public void setCId(String cid) {
        this.CId = cid;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryname) {
        this.CategoryName = categoryname;
    }

    public String getTtpHeading() {
        return TtpHeading;
    }

    public void setTtpHeading(String ttpheading) {
        this.TtpHeading = ttpheading;
    }

    public String getTtpImage() {
        return TtpImage;
    }

    public void setTtpImage(String ttpimage) {
        this.TtpImage = ttpimage;
    }

    public String getTtpDesc() {
        return TtpDesc;
    }

    public void setTtpDesc(String ttpdesc) {
        this.TtpDesc = ttpdesc;
    }

    public String getTtpCont() {
        return TtpCont;
    }

    public void setTtpCont(String ttpcont) {
        this.TtpCont = ttpcont;
    }

    public String getTtpIngre() {
        return TtpIngre;
    }

    public void setTtpIngre(String ttpingre) {
        this.TtpIngre = ttpingre;
    }

    public String getTtpProteins() {
        return TtpProteins;
    }

    public void setTtpProteins(String ttpproteins) {
        this.TtpProteins = ttpproteins;
    }

    public String getTtpCarbs() {
        return TtpCarbs;
    }

    public void setTtpCarbs(String ttpcarbs) {
        this.TtpCarbs = ttpcarbs;
    }

    public String getTtpFat() {
        return TtpFat;
    }

    public void setTtpFat(String ttpfat) {
        this.TtpFat = ttpfat;
    }

    public String getTtpCalories() {
        return TtpCalories;
    }

    public void setTtpCalories(String ttpcalories) {
        this.TtpCalories = ttpcalories;
    }

    public String getTtpTime() {
        return TtpTime;
    }

    public void setTtpTime(String ttptime) {
        this.TtpTime = ttptime;
    }

}
