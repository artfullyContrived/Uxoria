package com.artfully.contrived.smpp.model;

import java.io.Serializable;
import java.util.Map;

public class ContentItem implements Serializable {

  private static final long serialVersionUID = 8734880964191807240L;

  private int Id;
  private String keyword;
  private String headText;
  private String contentURL;
  private String tailText;
  private int shortCode;
  private Map<String, String> params;

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

  public String getHeadText() {
    return headText;
  }

  public void setHeadText(String headText) {
    this.headText = headText;
  }

  public String getContentURL() {
    return contentURL;
  }

  public void setContentURL(String contentURL) {
    this.contentURL = contentURL;
  }

  public String getTailText() {
    return tailText;
  }

  public void setTailText(String tailText) {
    this.tailText = tailText;
  }

  public int getShortCode() {
    return shortCode;
  }

  public void setShortCode(int shortCode) {
    this.shortCode = shortCode;
  }

  public Map<String, String> getParams() {
    return params;
  }

  public void setParams(Map<String, String> params) {
    this.params = params;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Id;
    result = prime * result + ((contentURL == null) ? 0 : contentURL.hashCode());
    result = prime * result + ((headText == null) ? 0 : headText.hashCode());
    result = prime * result + ((keyword == null) ? 0 : keyword.hashCode());
    result = prime * result + ((params == null) ? 0 : params.hashCode());
    result = prime * result + shortCode;
    result = prime * result + ((tailText == null) ? 0 : tailText.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
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
    if (keyword == null) {
      if (other.keyword != null)
        return false;
    } else if (!keyword.equals(other.keyword))
      return false;
    if (params == null) {
      if (other.params != null)
        return false;
    } else if (!params.equals(other.params))
      return false;
    if (shortCode != other.shortCode)
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
    StringBuilder builder = new StringBuilder();
    builder.append("ContentItem [Id=");
    builder.append(Id);
    builder.append(", keyword=");
    builder.append(keyword);
    builder.append(", headText=");
    builder.append(headText);
    builder.append(", contentURL=");
    builder.append(contentURL);
    builder.append(", tailText=");
    builder.append(tailText);
    builder.append(", shortCode=");
    builder.append(shortCode);
    builder.append(", params=");
    builder.append(params);
    builder.append("]");
    return builder.toString();
  }
}