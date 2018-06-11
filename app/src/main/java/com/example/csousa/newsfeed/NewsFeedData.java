package com.example.csousa.newsfeed;

public class NewsFeedData {

    private String id;
    private String type;
    private String sectionId;
    private String sectionName;
    private String webPublicationDate;
    private String webTitle;
    private String webUrl;
    private String contributor;

    public NewsFeedData(final String id, final String type, final String sectionId,
                        final String sectionName, final String webPublicationDate, final String webTitle,
                        final String webUrl, final String contributor) {
        this.id = id;
        this.type = type;
        this.sectionId = sectionId;
        this.sectionName = sectionName;
        this.webPublicationDate = webPublicationDate;
        this.webTitle = webTitle;
        this.webUrl = webUrl;
        this.contributor = contributor;

    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getSectionId() {
        return sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getContributor() {
        if (contributor == null) {
            return "";
        }
        return contributor;
    }
}
