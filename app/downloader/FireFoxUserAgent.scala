package downloader

import crawlercommons.fetcher.http.UserAgent

/**
 * The Class FireFoxUserAgent.
 *
 * @author Nguyen Duc Dung
 * @since 3/1/13 12:12 PM
 *
 */
class FireFoxUserAgent extends UserAgent("linkerz crawler","","") {
  override def getUserAgentString = "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2"
}
