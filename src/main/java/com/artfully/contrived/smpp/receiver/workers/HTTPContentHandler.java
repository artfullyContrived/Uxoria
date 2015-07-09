package com.artfully.contrived.smpp.receiver.workers;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.artfully.contrived.smpp.model.ContentItem;
import com.artfully.contrived.smpp.model.MyDeliverSM;
import com.artfully.contrived.util.UxoriaUtils;

public class HTTPContentHandler implements ContentHandler, Runnable {

  private static final Logger logger = Logger.getLogger(HTTPContentHandler.class);
  private final MyDeliverSM deliverSm;

  public HTTPContentHandler(MyDeliverSM deliversm) {
    this.deliverSm = deliversm;
  }

  @Override
  public void run() {
    logger.debug("handle()." + deliverSm.getSourceAddr());
    StringBuffer buffer = new StringBuffer();
    ContentItem contentItem = getContentItem(deliverSm);

    if (contentItem != null) {
      String content = getContent(contentItem.getContentURL());

      String headText = contentItem.getHeadText();
      String tailText = contentItem.getTailText();

      if (!headText.isEmpty()) {
        buffer.append(headText);
        buffer.append(' ');
      }

      buffer.append(content);

      if (!tailText.isEmpty())
        buffer.append(' ');
        buffer.append(tailText);
    } else {
      logger.debug("Unknown keyword");
      buffer.append("invalid keyword");
    }
    UxoriaUtils.pushToMessageQueue(buffer.toString(), deliverSm.getSourceAddr(), deliverSm.getDestAddress());
  }

  private ContentItem getContentItem(MyDeliverSM deliverSm) {
    return UxoriaUtils.getContentItem(deliverSm);
  }

  //TODO investigate best, fastest, cheapest way hitting contentURL
  private String getContent(String contentURL) {
    logger.debug("Getting Content for: " + contentURL);
    String content = "";

    GetMethod get = new GetMethod(contentURL);
    HttpClient client = new HttpClient();
    try {
      client.executeMethod(get);
      content = get.getResponseBodyAsString().replaceAll("\\n", "");;
    } catch (HttpException e) {
      logger.error(e, e);
    } catch (IOException e) {
      logger.error(e, e);
    }
    logger.debug("Content: " + content);
    return content;
  }
}
