package com.coral.epidemicsimapiserver.repository;

public record CreateGamePacket(
        String id,
        int time_step,
        int num_agents,
        PathogenConfig pathogen
) {
}
