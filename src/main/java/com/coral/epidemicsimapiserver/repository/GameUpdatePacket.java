package com.coral.epidemicsimapiserver.repository;

import java.util.UUID;

public record GameUpdatePacket(
        UUID ApiServerID,
        UUID SimServerID,
        UUID SimID,
        byte[] payload
) {
}
