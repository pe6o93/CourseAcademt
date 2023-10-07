package com.example.academy.service;

import com.example.academy.model.view.StatsView;

public interface StatsService {
    void onRequest();

    StatsView getStats();
}
