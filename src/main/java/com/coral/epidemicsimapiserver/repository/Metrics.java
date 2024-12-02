package com.coral.epidemicsimapiserver.repository;

public record Metrics(
        int NewInfections,
        int NewHospitalizations,
        int NewRecoveries,
        int NewDeaths,
        int InfectedPopulation,
        int InfectiousPopulation,
        int HospitalizedPopulation,
        int ImmunePopulation,
        int DeadPopulation
) {
}
