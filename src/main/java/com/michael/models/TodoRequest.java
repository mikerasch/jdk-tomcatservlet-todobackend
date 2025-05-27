package com.michael.models;

public record TodoRequest(String title, Boolean completed, Integer order) {}
