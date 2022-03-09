package com.greenmile.commons.adapter;

import com.greenmile.commons.entity.Route;
import com.greenmile.commons.entity.Vehicle;
import com.greenmile.commons.vo.RouteVo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RouteAdapter {


    public static Route voToModel(RouteVo vo){
        return Route.builder().id(vo.getId())
                              .vehicle(Vehicle.builder().id(vo.getVehicleId()).build()).build();
    }


    public static RouteVo modelToVo(Route route){
        Vehicle vehicle = route.getVehicle();
        return RouteVo.builder().id(route.getId())
                .vehicleId(Optional.ofNullable(vehicle).map(Vehicle::getId).orElse(null))
                .ordersId(Optional.ofNullable(route.getOrders()).map(OrderAdapter::getListIds).orElse(null))
                .build();
    }


    public static List<RouteVo> modelToVo(List<Route> routes){
        if(routes == null || routes.isEmpty()){
            return Collections.emptyList();
        }
        return routes.stream().map(RouteAdapter::modelToVo).collect(Collectors.toList());
    }

    public static List<Long> getListIds(List<Route> routes){
        if(routes == null || routes.isEmpty()){
            return Collections.emptyList();
        }
        return routes.stream().map(Route::getId).collect(Collectors.toList());
    }

}
