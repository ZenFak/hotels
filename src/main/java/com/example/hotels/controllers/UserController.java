package com.example.hotels.controllers;

import com.example.hotels.models.*;
import com.example.hotels.repository.HotelRepository;
import com.example.hotels.repository.OrderRepository;
import com.example.hotels.repository.UserProfileRepository;
import com.example.hotels.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public String profile(Model model, RedirectAttributes redirectAttributes){
        if (!userProfileRepository.existsById(Global.TempID)) return "redirect:/login";
        redirectAttributes.addAttribute("id", Global.TempID);
        return "redirect:/getProfile/{id}";
    }

    @GetMapping("/getProfile/{id}")
    public String profile(@PathVariable("id") long id, Model model) {
        Optional<UserProfile> userProfile = userProfileRepository.findById(id);

        ArrayList<UserProfile> res = new ArrayList<>();
        userProfile.ifPresent(res::add);

        model.addAttribute("userProfile", res);

        return "profile";
    }

    @GetMapping("/getProfile/{id}/orders")
    public String viewOrders(@PathVariable("id") long id, Model model) {
        Optional<UserProfile> userProfile = userProfileRepository.findById(id);

        Iterable<Order> orders = orderRepository.findOrdersByUser(userProfile.get());
        model.addAttribute("orders", orders);

        return "userOrders";
    }


    @PostMapping("/order/{orderId}/delete")
    public String deleteOrder(@PathVariable("orderId") long orderId,
                              RedirectAttributes redirectAttributes, Model model) {
        if (!checkAuth()) return "redirect:/login";
        Optional<UserProfile> userProfile = userProfileRepository.findById(Global.TempID);

        Optional<Order> order = orderRepository.findById(orderId);
        orderRepository.delete(order.get());

        redirectAttributes.addAttribute("id", userProfile.get().getUserProfileId());
        return "redirect:/getProfile/{id}/orders";
    }






    /*@GetMapping("/hotel/{id}/order")
    public String orderPost(@PathVariable(value = "id") long id, @RequestParam String summ, @RequestParam Integer totalDays,
                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date1,
                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date2,
                            RedirectAttributes redirectAttributes, Model model) {


        Double totalCost = Double.parseDouble(summ.replaceAll(" ", ""));
        if (!userProfileRepository.existsById(Global.TempID)) return "redirect:/login";
        Optional<UserProfile> up = userProfileRepository.findById(Global.TempID);


        Order order = new Order(up.get(), hotel.get(), totalCost, date1, date2, totalDays);
        orderRepository.save(order);

        model.addAttribute(order);
        redirectAttributes.addAttribute("orderId", order.getOrderID());

        return "redirect:/order/{orderId}";
        *//*return "order-details";*//*
    }*/







    public boolean checkAuth() {
        return userProfileRepository.existsById(Global.TempID);
    }
}
