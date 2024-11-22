package com.coral.epidemicsimapiserver.repository;

public record CreateGamePacket(
        String SERVER_UUID,
        String SIMULATION_UUID
) {
}
