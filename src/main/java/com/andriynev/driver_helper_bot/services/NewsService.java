package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dao.UserRepository;
import com.andriynev.driver_helper_bot.dto.CarRepairTree;
import com.andriynev.driver_helper_bot.dto.NewsItem;
import com.andriynev.driver_helper_bot.dto.State;
import com.andriynev.driver_helper_bot.dto.User;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NewsService {
    private final RSSNewsReader rssNewsReader;
    private final UserService userService;
    private final ResponseService responseService;

    @Autowired
    public NewsService(RSSNewsReader rssNewsReader, UserService userService, ResponseService responseService) {
        this.rssNewsReader = rssNewsReader;
        this.userService = userService;
        this.responseService = responseService;
    }

    public Map<String, List<NewsItem>> processRSSFeeds() {
        Map<String, SyndFeed> feedsUpdates = rssNewsReader.fetchNewsChanges();
        Map<String, List<NewsItem>> news = new HashMap<>();
        for (Map.Entry<String, SyndFeed> entry : feedsUpdates.entrySet()) {
            List<NewsItem> feedNews = getNewsItemFromRssFeed(entry.getValue());
            news.put(entry.getKey(), feedNews);
        }
        return news;
    }

    private List<NewsItem> getNewsItemFromRssFeed(SyndFeed feed) {
        List<NewsItem> news = new ArrayList<>();
        for (Object entry: feed.getEntries()) {
            if (entry instanceof SyndEntryImpl) {
                NewsItem item = parseFeedEntry((SyndEntryImpl) entry);
                news.add(item);
            }
        }
        return news;
    }

    private NewsItem parseFeedEntry(SyndEntryImpl feedEntry) {
        Document doc = Jsoup.parse(feedEntry.getDescription().getValue());
        String photoUrl = doc.select("div > img").attr("src");
        String text = doc.select("p").first().text();
        NewsItem item = new NewsItem(feedEntry.getTitle(), text, photoUrl, feedEntry.getLink());
        return item;
    }

    @Scheduled(fixedRate = 600000)
    public void checkNewsUpdates() {
        Map<String, List<NewsItem>> news = processRSSFeeds();
        if (news.isEmpty()) {
            return;
        }

        List<User> users = userService.getUsers();
        if (users.isEmpty()) {
            return;
        }
        for (Map.Entry<String, List<NewsItem>> entry : news.entrySet()) {
            if (entry.getValue().isEmpty()) {
                continue;
            }

            for (User user: users) {
                if (!user.getSubscriptions().contains(entry.getKey())){
                    continue;
                }

                for (NewsItem newsItem: entry.getValue()) {
                    responseService.sendNewsItem(newsItem, user.getChatID());
                }
            }
        }
    }
}
