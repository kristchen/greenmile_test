package com.greenmile.routes.service;

import com.greenmile.commons.adapter.RouteAdapter;
import com.greenmile.commons.entity.Order;
import com.greenmile.commons.entity.Route;
import com.greenmile.commons.entity.Vehicle;
import com.greenmile.commons.enums.EOrderIssue;
import com.greenmile.commons.repository.OrderRepository;
import com.greenmile.commons.repository.RouteRepository;
import com.greenmile.commons.util.Haversine;
import com.greenmile.commons.vo.OrderVo;
import com.greenmile.commons.vo.RouteStatisticsVo;
import com.greenmile.commons.vo.RouteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Service
public class RouteService {

    @Autowired
    private RouteRepository repository;

    @Autowired
    private OrderRepository orderRepository;

    public RouteVo createRoute(RouteVo vo){
        Route route = RouteAdapter.voToModel(vo);
        // criar validação para vehiculos
        route = repository.saveAndFlush(route);
        return RouteAdapter.modelToVo(route);
    }

    public List<RouteVo> getRoutes(){
        List<Route> routes = repository.findAll();
        return RouteAdapter.modelToVo(routes);
    }


    public RouteVo findById(Long id){

        Optional<Route> route = repository.findById(id);
        RouteVo vo = route.map(RouteAdapter::modelToVo).orElse(null);
        Optional.ofNullable(vo).ifPresent(v -> v.setCapacityRemaining(verifyCapacityRemaining(route.get())));

        return vo;
    }


    public RouteStatisticsVo getStatisticsRoutes(){

        List<Route> routes = repository.findDistinctByOrdersNotNull();

        Map<Long, Integer> mapIdsRoutes = routes.stream().collect(Collectors.toMap(Route::getId, route -> route.getOrders().size()));

        Entry<Long, Integer> max =  mapIdsRoutes.entrySet().stream().max(Entry.comparingByValue()).get();
        Entry<Long, Integer> min=  mapIdsRoutes.entrySet().stream().min(Entry.comparingByValue()).get();

        final Optional <Route> routeMax = routes.stream().filter( r -> r.getId().compareTo(max.getKey()) == 0L).findFirst();
        final Optional <Route> routeMin = routes.stream().filter( r -> r.getId().compareTo(min.getKey()) == 0L).findFirst();


        List<Order> orders = routes.stream().map(Route::getOrders).collect(Collectors.toList())
                                .stream().flatMap(List::stream).collect(Collectors.toList());

        BigDecimal totalCargo = orders.stream().map(Order::getCargo).reduce(BigDecimal.ZERO, BigDecimal::add);

        return RouteStatisticsVo.builder().averageCargo(totalCargo.divide(BigDecimal.valueOf(orders.size()), RoundingMode.HALF_UP))
                                   .maxRoute(routeMax.map(RouteAdapter::modelToVo).orElse(null))
                                   .minRoute(routeMin.map(RouteAdapter::modelToVo).orElse(null)).build();
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public RouteVo alocateOrder(OrderVo vo){

        Optional<Order> order = orderRepository.findById(vo.getId());
        List<Route> routes = repository.findByVehicleIsNotNull();
        Optional<Route> route = order.map(or -> allocate(or, routes));

        return route.map(RouteAdapter::modelToVo).orElse(null);
    }


    private Route allocate(final Order order, List<Route> routes){

        List<Route> routesWithCapacity = verifyCapacityAlocate(order, routes);
        if(!routesWithCapacity.isEmpty()){
            Optional <Route> routeWithDistance = verifyDistanceAlocate(order, routesWithCapacity);
            routeWithDistance.ifPresentOrElse(order::setRoute, () -> order.setIssue(EOrderIssue.WITHOUT_DISTANCE.getId()));
        }else {
            order.setIssue(EOrderIssue.WITHOUT_CAPACITY.getId());
        }
        Order orderAllocated = orderRepository.saveAndFlush(order);

        return orderAllocated.getRoute();
    }

    private List<Route> verifyCapacityAlocate(final Order order, List<Route> routes) {
        return routes.stream().filter(route -> verifyCapacityRemaining(route).compareTo(order.getCargo()) >= 0).collect(Collectors.toList());

    }

    private Optional<Route> verifyDistanceAlocate(final Order order, List<Route> routesWithCapacity) {

        return routesWithCapacity.stream().filter(
                    route -> (route.getOrders().isEmpty() && verifyCapacityRemaining(route).compareTo(order.getCargo()) >= 0) ||
                            route.getOrders().stream().anyMatch(ord -> Haversine.distance(order.getLatitude(), order.getLongitude(), ord.getLatitude(), ord.getLongitude()) <= 1)).findFirst();

    }


    private BigDecimal verifyCapacityRemaining(Route route){

        List<Route> routesVehicle = Optional.ofNullable(route.getVehicle()).map(Vehicle::getRoutes).orElse(Collections.emptyList());
        List<Order> ordersVehicle = routesVehicle.stream().flatMap(route1 -> route1.getOrders().stream()).collect(Collectors.toList());
        BigDecimal cargoUsed = ordersVehicle.stream().map(Order::getCargo).reduce(BigDecimal.ZERO, BigDecimal::add);

        return route.getVehicle().getCapacity().subtract(cargoUsed);
    }

}