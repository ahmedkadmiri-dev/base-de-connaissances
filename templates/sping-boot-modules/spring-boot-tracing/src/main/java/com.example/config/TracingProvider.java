package com.example.config;

import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TracingProvider {

    private final Tracer tracer;
}