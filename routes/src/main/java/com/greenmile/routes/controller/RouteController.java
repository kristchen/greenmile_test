package com.greenmile.routes.controller;

import com.greenmile.commons.vo.OrderVo;
import com.greenmile.commons.vo.RouteStatisticsVo;
import com.greenmile.commons.vo.RouteVo;
import com.greenmile.routes.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/routes", produces = MediaType.APPLICATION_JSON_VALUE)
public class RouteController {

    @Autowired
    private RouteService service;

    @PostMapping( consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RouteVo> createRoute(@RequestBody RouteVo vo){
        vo = service.createRoute(vo);
        return Optional.ofNullable(vo).map(routeVo -> new ResponseEntity<>(routeVo, HttpStatus.CREATED))
                                                 .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping
    public ResponseEntity<List<RouteVo>> getRoutes(){
        return ResponseEntity.ok(service.getRoutes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteVo> findById(@PathVariable Long id){
        return Optional.ofNullable(service.findById(id)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/statistics")
    public ResponseEntity<RouteStatisticsVo> getAverageCargo(){
        return ResponseEntity.ok(service.getStatisticsRoutes());
    }


}
