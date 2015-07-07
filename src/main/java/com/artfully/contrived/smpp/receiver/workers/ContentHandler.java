package com.artfully.contrived.smpp.receiver.workers;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.artfully.contrived.smpp.model.ContentItem;
import com.artfully.contrived.smpp.model.MyDeliverSM;
import com.artfully.contrived.util.UxoriaUtils;

public class ContentHandler implements Runnable {

  private static final Logger logger = Logger.getLogger(ContentHandler.class);
  private final MyDeliverSM deliverSm;

  public ContentHandler(MyDeliverSM deliversm) {
    this.deliverSm = deliversm;
  }

  @Override
  public void run() {
    logger.debug("handle()." + deliverSm.getSourceAddr());
    StringBuffer buffer = new StringBuffer();
    ContentItem contentElement = getContentElements(deliverSm);

    if (contentElement != null) {
      String content = getContent(contentElement.getContentURL());

      String headText = contentElement.getHeadText();
      String tailText = contentElement.getTailText();

      if (!headText.isEmpty())
        buffer.append(headText);

      buffer.append(content);

      if (!tailText.isEmpty())
        buffer.append(tailText);
    } else {
      logger.debug("Unknown keyword");
      buffer.append("invalid keyword");
    }
    UxoriaUtils.pushToMessageQueue(buffer.toString(), deliverSm.getSourceAddr(), deliverSm.getDestAddress());

  }

  private ContentItem getContentElements(MyDeliverSM deliverSm2) {
    return UxoriaUtils.getContentElement(deliverSm2);
  }

  // get content for URL
  private String getContent(String contentURL) {
    logger.debug("Getting Content for: " + contentURL);
    String content = "";

    GetMethod get = new GetMethod(contentURL);
    HttpClient client = new HttpClient();
    try {
      client.executeMethod(get);
      content = get.getResponseBodyAsString();
    } catch (HttpException e) {
      logger.error(e, e);
    } catch (IOException e) {
      logger.error(e, e);
    }
    logger.debug("Content: " + content);

    return content;
  }

}
