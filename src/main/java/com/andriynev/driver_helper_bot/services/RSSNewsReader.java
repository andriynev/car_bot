package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dao.RSSNewsSourceRepository;
import com.andriynev.driver_helper_bot.dto.NewsItem;
import com.andriynev.driver_helper_bot.dto.RSSNewsSource;
import com.rometools.rome.io.XmlReader;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RSSNewsReader {
    private final RSSNewsSourceRepository rssNewsSourceRepository;

    @Autowired
    public RSSNewsReader(RSSNewsSourceRepository rssNewsSourceRepository) {
        this.rssNewsSourceRepository = rssNewsSourceRepository;
    }

    public Map<String, List<NewsItem>> processRSSFeeds() {
        Map<RSSNewsSource, SyndFeed> feedsUpdates = fetchNewsChanges();
        Map<String, List<NewsItem>> news = new HashMap<>();
        for (Map.Entry<RSSNewsSource, SyndFeed> entry : feedsUpdates.entrySet()) {
            List<NewsItem> feedNews = getNewsItemFromRssFeed(entry.getKey(), entry.getValue());
            news.put(entry.getKey().getCategory(), feedNews);
        }
        return news;
    }

    private List<NewsItem> getNewsItemFromRssFeed(RSSNewsSource source, SyndFeed feed) {
        List<NewsItem> news = new ArrayList<>();
        Date lastPublishedAt = null;

        for (Object entry: feed.getEntries()) {
            if (entry instanceof SyndEntryImpl) {

                Date entryPublishedDate = ((SyndEntryImpl) entry).getPublishedDate();
                boolean needToSkipEntry = source.getLastUpdatedAt() != null &&
                        (source.getLastUpdatedAt().after(entryPublishedDate) ||
                                source.getLastUpdatedAt().equals(entryPublishedDate));

                if (needToSkipEntry) {
                    continue;
                }

                if (lastPublishedAt == null) {
                    lastPublishedAt = entryPublishedDate;
                }

                if (lastPublishedAt != null &&
                        lastPublishedAt.before(entryPublishedDate)) {

                    lastPublishedAt = entryPublishedDate;
                }

                Optional<NewsItem> item = parseFeedEntry(source.getCategory(), (SyndEntryImpl) entry);
                if (!item.isPresent()) {
                    continue;
                }
                news.add(item.get());
            }
        }

        if (lastPublishedAt == null) {
            return news;
        }

        if (source.getLastUpdatedAt() == null || source.getLastUpdatedAt().before(lastPublishedAt)) {
            source.setLastUpdatedAt(lastPublishedAt);
            rssNewsSourceRepository.save(source);
        }
        return news;
    }

    private Optional<NewsItem> parseFeedEntry(String category, SyndEntryImpl feedEntry) {

        Document doc = Jsoup.parse(feedEntry.getDescription().getValue());
        String photoUrl = doc.select("div > img").attr("src");
        String text = doc.select("p").first().text();
        NewsItem item = new NewsItem(feedEntry.getTitle(), text, photoUrl, feedEntry.getLink(), category);
        return Optional.of(item);
    }

    private Map<RSSNewsSource, SyndFeed> fetchNewsChanges() {
        List<RSSNewsSource> sources = rssNewsSourceRepository.findAll();
        Map<RSSNewsSource, SyndFeed> changes = new HashMap<>();
        for (RSSNewsSource source: sources) {
            Optional<SyndFeed> feedChange = fetchRSSSourceChanges(source);
            if (!feedChange.isPresent()) {
                continue;
            }

            changes.put(source, feedChange.get());
        }

        return changes;

    }

    private Optional<SyndFeed> fetchRSSSourceChanges(RSSNewsSource source) {

        try {
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(source.getSource()));
            return Optional.of(feed);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }
}
