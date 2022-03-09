package com.greenmile.commons.adapter;

import com.greenmile.commons.entity.Vehicle;
import com.greenmile.commons.vo.VehicleVo;

import java.util.Collections;
import java.util.Optional;

public class VehicleAdapter {

    public static VehicleVo modelToVo(Vehicle vehicle){
        return VehicleVo.builder().capacity(vehicle.getCapacity())
                .routesId(Optional.ofNullable(vehicle.getRoutes()).map(RouteAdapter::getListIds).orElse(Collections.emptyList()))
                .id(vehicle.getId()).build();
    }
}
