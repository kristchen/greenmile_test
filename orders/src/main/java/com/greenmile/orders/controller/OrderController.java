package com.greenmile.orders.controller;

import com.greenmile.commons.vo.OrderVo;
import com.greenmile.commons.vo.RouteVo;
import com.greenmile.commons.vo.VehicleVo;
import com.greenmile.orders.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    @Autowired
    private OrderService service;

    @PostMapping( consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderVo> createOrder(@RequestBody OrderVo vo){
        vo = this.service.createOrder(vo);
        return Optional.ofNullable(vo).map(orderVo -> new ResponseEntity<>(orderVo, HttpStatus.CREATED)).orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/with-issues")
    public ResponseEntity<List<OrderVo>> findOrdersWithIssues(){
        List<OrderVo> vo = this.service.findOrdersWithIssues();
        return ResponseEntity.ok(vo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderVo> findById(@PathVariable Long id){
        OrderVo vo = this.service.findById(id);
        return Optional.ofNullable(vo).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/route")
    public ResponseEntity<RouteVo> getRoute(@PathVariable Long id){
        RouteVo vo = this.service.getRoute(id);
        return Optional.ofNullable(vo).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/vehicle")
    public ResponseEntity<VehicleVo> getVehicle(@PathVariable Long id){
        VehicleVo vo = this.service.getVehicle(id);
        return Optional.ofNullable(vo).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

}
