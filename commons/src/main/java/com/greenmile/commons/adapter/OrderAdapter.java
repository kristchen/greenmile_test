package com.greenmile.commons.adapter;

import com.greenmile.commons.entity.Order;
import com.greenmile.commons.entity.Route;
import com.greenmile.commons.enums.EOrderIssue;
import com.greenmile.commons.vo.OrderVo;
import com.greenmile.commons.vo.RouteVo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderAdapter {


    public static Order voToModel(OrderVo vo){


        return Order.builder().id(vo.getId())
                              .latitude(vo.getLatitude())
                              .longitude(vo.getLongitude())
                              .cargo(vo.getCargo()).build();
    }

    public static List<OrderVo> modelToVo(List<Order> vos){
        if(vos == null || vos.isEmpty()){
            return Collections.emptyList();
        }
        return vos.stream().map(OrderAdapter::modelToVo).collect(Collectors.toList());
    }

    public static OrderVo modelToVo(Order order){

        Route route = order.getRoute();

        return OrderVo.builder().id(order.getId())
                .latitude(order.getLatitude())
                .longitude(order.getLongitude())
                .cargo(order.getCargo())
                .issue(Optional.ofNullable(order.getIssue()).map(EOrderIssue::findById).map(EOrderIssue::getDescription).orElse(null))
                .routeId(Optional.ofNullable(route).map(Route::getId).orElse(null)).build();
    }

    
    public static List<Long> getListIds(List<Order> order){
        if(order == null || order.isEmpty()){
            return Collections.emptyList();
        }
        return order.stream().map(Order::getId).collect(Collectors.toList());
    }


}
