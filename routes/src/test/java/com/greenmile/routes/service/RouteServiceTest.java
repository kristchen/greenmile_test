package com.greenmile.routes.service;


import com.greenmile.commons.entity.Order;
import com.greenmile.commons.entity.Route;
import com.greenmile.commons.entity.Vehicle;
import com.greenmile.commons.repository.OrderRepository;
import com.greenmile.commons.repository.RouteRepository;
import com.greenmile.commons.vo.OrderVo;
import com.greenmile.commons.vo.RouteVo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.MapKeyClass;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class RouteServiceTest {


    @InjectMocks
    private RouteService routeService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RouteRepository routeRepository;

    private final  Order order = Order.builder().id(38L).longitude("-38.530040").latitude("-3.734320").cargo(BigDecimal.TEN).build();
    private final  Vehicle vehicle = Vehicle.builder().capacity(BigDecimal.valueOf(100L)).build();
    private final  Route route = Route.builder().id(10L).orders(Collections.emptyList()).vehicle(vehicle).build();

    @Test
    public void testAllocateOrderWithoutRoutes(){

        Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.saveAndFlush(Mockito.any(Order.class))).thenReturn(order);
        OrderVo vo = OrderVo.builder().id(38L).build();
        RouteVo routeVo = routeService.alocateOrder(vo);
        Assert.assertNull(routeVo);

    }

    @Test
    public void testAllocateOrderWithRoutes(){

        List<Route> routeList = new ArrayList<Route>();
        routeList.add(route);

        Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(order));
        Mockito.when(routeRepository.findByVehicleIsNotNull()).thenReturn(routeList);
        Mockito.when(orderRepository.saveAndFlush(Mockito.any(Order.class))).thenReturn(order);
        OrderVo vo = OrderVo.builder().id(38L).build();
        RouteVo routeVo = routeService.alocateOrder(vo);
        vo.setRouteId(routeVo.getId());

        Assert.assertNotNull(routeVo);
        Assert.assertEquals(routeVo.getId(), vo.getRouteId());

    }


}
