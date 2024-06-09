package org.delivery.storeadmin.domain.userorder.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.delivery.storeadmin.domain.authorization.model.UserSession;
import org.delivery.storeadmin.domain.userorder.business.UserOrderBusiness;
import org.delivery.storeadmin.domain.userorder.controller.model.UserOrderResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/store-order")
@RequiredArgsConstructor
@Slf4j(topic = "[8081]user Order Controller")
public class UserOrderController {
    private final UserOrderBusiness userOrderBusiness;

    @GetMapping(path = "/", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public String orderInfo(@AuthenticationPrincipal UserSession userSession, Model model){
        List<UserOrderResponse> orderList = userOrderBusiness.getOrdersByStoreId(userSession.getStoreId());
        model.addAttribute("orderList",orderList);

        return "home";
    }

    @PostMapping("/id/{orderId}")
    public @ResponseBody ResponseEntity updateOrder(@PathVariable Long orderId){
        userOrderBusiness.updateOrderState(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }


}
