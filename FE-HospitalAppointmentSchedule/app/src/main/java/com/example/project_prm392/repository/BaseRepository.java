package com.example.project_prm392.repository;

import com.example.project_prm392.network.ApiService;
import com.example.project_prm392.network.RetrofitClient;

public abstract class BaseRepository {
    protected final ApiService api;

    public BaseRepository() {
        this.api = RetrofitClient.getInstance().getApi();
    }
} 