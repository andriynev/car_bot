package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dao.RSSNewsSourceRepository;
import com.andriynev.driver_helper_bot.dao.UserRepository;
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
    private RSSNewsSourceRepository rssNewsSourceRepository;

    @Autowired
    public RSSNewsReader(RSSNewsSourceRepository rssNewsSourceRepository) {
        this.rssNewsSourceRepository = rssNewsSourceRepository;
    }

    public Map<String, SyndFeed> fetchNewsChanges() {
        List<RSSNewsSource> sources = rssNewsSourceRepository.findAll();
        Map<String, SyndFeed> changes = new HashMap<>();
        for (RSSNewsSource source: sources) {
            Optional<SyndFeed> feedChange = fetchRSSSourceChanges(source);
            if (!feedChange.isPresent()) {
                continue;
            }

            changes.put(source.getCategory(), feedChange.get());
        }

        return changes;

    }

    private Optional<SyndFeed> fetchRSSSourceChanges(RSSNewsSource source) {

        try {
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(source.getSource()));
            if (source.getLastUpdatedAt() == null || source.getLastUpdatedAt().before(feed.getPublishedDate())) {
                source.setLastUpdatedAt(feed.getPublishedDate());
                rssNewsSourceRepository.save(source);
                return Optional.of(feed);
            }
            return Optional.empty();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }
}
