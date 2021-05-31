package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dto.NewsItem;
import com.andriynev.driver_helper_bot.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Scheduled(fixedRate = 600000)
    public void checkNewsUpdates() {
        Map<String, List<NewsItem>> news = rssNewsReader.processRSSFeeds();
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
                // skip disabled users
                if (!user.isEnabled()) {
                    continue;
                }
                
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
