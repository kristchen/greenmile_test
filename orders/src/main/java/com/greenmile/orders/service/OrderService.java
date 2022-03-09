package com.greenmile.orders.service;

import com.greenmile.commons.adapter.OrderAdapter;
import com.greenmile.commons.adapter.RouteAdapter;
import com.greenmile.commons.adapter.VehicleAdapter;
import com.greenmile.commons.entity.Order;
import com.greenmile.commons.entity.Route;
import com.greenmile.commons.entity.Vehicle;
import com.greenmile.commons.repository.OrderRepository;
import com.greenmile.commons.vo.OrderVo;
import com.greenmile.commons.vo.RouteVo;
import com.greenmile.commons.vo.VehicleVo;
import com.greenmile.orders.mq.OrderProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private OrderProducer producer;

    public OrderVo createOrder(OrderVo vo){
        Order order = OrderAdapter.voToModel(vo);
        order = repository.saveAndFlush(order);
        vo = OrderAdapter.modelToVo(order);
        producer.send(vo);
        return vo;
    }

    public OrderVo findById(Long id) {
        Optional<Order> order = repository.findById(id);
        OrderVo vo = order.map(OrderAdapter::modelToVo).orElse(null);
        return vo;
    }

    public List<OrderVo> findOrdersWithIssues() {
        List<Order> orders = repository.findByIssueIsNotNull();
        return OrderAdapter.modelToVo(orders);
    }

    public RouteVo getRoute(Long id) {
        Optional<Order> order = repository.findById(id);
        return order.map(ord -> Optional.ofNullable(ord.getRoute()).map(RouteAdapter::modelToVo).orElse(null)).orElse(null);
    }

    public VehicleVo getVehicle(Long id) {
        Optional<Order> order = repository.findById(id);
        Optional<Route> route = Optional.empty();
        if(order.isPresent()){
            route = Optional.ofNullable(order.get().getRoute());
        }
        return route.map( rt -> Optional.ofNullable(rt.getVehicle()).map(VehicleAdapter::modelToVo).orElse(null)).orElse(null);
    }

}
