package com.artfully.contrived.smpp.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.common.base.Objects;

public class ContentItem implements Serializable {

    private static final long serialVersionUID = 8734880964191807240L;
    private int Id;
    private String keyword;
    private String headText;
    private String contentURL;
    private String tailText;

    public int getId() {
	return Id;
    }

    public void setId(int id) {
	Id = id;
    }

    public String getKeyword() {
	return keyword;
    }

    public void setKeyword(String keyword) {
	this.keyword = keyword;
    }

    /**
     * @return the headText
     */
    public String getHeadText() {
	return headText;
    }

    /**
     * @param headText
     *            the headText to set
     */
    public void setHeadText(String headText) {
	this.headText = headText;
    }

    /**
     * @return the contentURL
     */
    public String getContentURL() {
	return contentURL;
    }

    /**
     * @param contentURL
     *            the contentURL to set
     */
    public void setContentURL(String contentURL) {
	this.contentURL = contentURL;
    }

    /**
     * @return the tailText
     */
    public String getTailText() {
	return tailText;
    }

    /**
     * @param tailText
     *            the tailText to set
     */
    public void setTailText(String tailText) {
	this.tailText = tailText;
    }

    @Override
    public int hashCode() {
	return Objects.hashCode(Id, headText, contentURL, tailText);

    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (!(obj instanceof ContentItem))
	    return false;
	ContentItem other = (ContentItem) obj;
	if (Id != other.Id)
	    return false;
	if (contentURL == null) {
	    if (other.contentURL != null)
		return false;
	} else if (!contentURL.equals(other.contentURL))
	    return false;
	if (headText == null) {
	    if (other.headText != null)
		return false;
	} else if (!headText.equals(other.headText))
	    return false;
	if (tailText == null) {
	    if (other.tailText != null)
		return false;
	} else if (!tailText.equals(other.tailText))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return ReflectionToStringBuilder.toString(this,
		ToStringStyle.SHORT_PREFIX_STYLE);
    }

}