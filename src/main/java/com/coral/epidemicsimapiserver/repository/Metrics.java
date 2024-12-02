package com.coral.epidemicsimapiserver.repository;

public record Metrics(
        int new_infections,
        int new_hospitalizations,
        int new_recoveries,
        int new_deaths,
        int infected_population,
        int infectious_population,
        int hospitalized_population,
        int immune_population,
        int dead_population,

        int new_tests,
        int new_positive_tests,
        int total_tests,
        int total_positive_tests,
        int test_backlog,
        int test_capacity
) {
}
