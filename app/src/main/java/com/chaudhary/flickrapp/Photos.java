package com.chaudhary.flickrapp;

import java.io.Serializable;

public class Photos implements Serializable {

    public static final long serialVersionUID = 1L;
    private String mTitle;
    private String mAuthor;
    private String mAuthorId;
    private String mLink;
    private String mTags;
    private String mImages;

    public Photos(String title, String author, String authorId, String link, String tags, String images) {
        mTitle = title;
        mAuthor = author;
        mAuthorId = authorId;
        mLink = link;
        mTags = tags;
        mImages = images;
    }

    String getTitle() {
        return mTitle;
    }

    String getAuthor() {
        return mAuthor;
    }

    String getAuthorId() {
        return mAuthorId;
    }

    String getLink() {
        return mLink;
    }

    String getTags() {
        return mTags;
    }

    public String getImages() {
        return mImages;
    }

    @Override
    public String toString() {
        return "Photos{" +
                "mTitle='" + mTitle + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mAuthorId='" + mAuthorId + '\'' +
                ", mLink='" + mLink + '\'' +
                ", mTags='" + mTags + '\'' +
                ", mImages='" + mImages + '\'' +
                '}';
    }
}
