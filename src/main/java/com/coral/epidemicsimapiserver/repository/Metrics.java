package com.coral.epidemicsimapiserver.repository;

public record Metrics(
        int NewInfections,
        int NewRecoveries,
        int InfectedPopulation,
        int InfectiousPopulation,
        int ImmunePopulation
) {
}
