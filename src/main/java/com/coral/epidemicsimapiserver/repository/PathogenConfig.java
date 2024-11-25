package com.coral.epidemicsimapiserver.repository;

public record PathogenConfig(
        double[] incubation_period,
        double[] recovery_period,
        double[] immunity_period,
        double[] quanta_emission_rate
) {

}
