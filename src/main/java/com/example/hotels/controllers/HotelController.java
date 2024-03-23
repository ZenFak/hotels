package com.example.hotels.controllers;

import com.example.hotels.models.Global;
import com.example.hotels.models.Hotel;
import com.example.hotels.models.Order;
import com.example.hotels.models.UserProfile;
import com.example.hotels.repository.HotelRepository;
import com.example.hotels.repository.OrderRepository;
import com.example.hotels.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
public class HotelController {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    /**
     * Переход на страницу о детялях отеля
     *
     * @param id id отеля
     * @param model
     * @return переадресация на стрнаицу с подробными данными об отеле
     */
    @GetMapping("/hotel/{id}")
    public String hotelDetails(@PathVariable(value = "id") long id, Model model) {
        if (!hotelRepository.existsById(id)) {
            return "redirect:/";
        }
        Optional<Hotel> hotel = hotelRepository.findById(id);
        ArrayList<Hotel> res = new ArrayList<>();
        hotel.ifPresent(res::add);
        model.addAttribute("hotels", res);
        return "hotel-details";
    }

    /**
     * Переход на страницу бронирования отеля
     *
     * @param id id отеля
     * @param model
     * @return переадресация на страницу бронирования отеля
     */
    @GetMapping("/hotel/{id}/reservation")
    public String reservation(@PathVariable(value = "id") long id, Model model) {
        Optional<Hotel> hotels = hotelRepository.findById(id);


        ArrayList<Hotel> res = new ArrayList<>();
        hotels.ifPresent(res::add);

        model.addAttribute("hotels", res);
        return "reservation";
    }

    /**
     * Логика бронирования отеля
     *
     * @param id id отеля
     * @param checkInDate дата заселения
     * @param checkOutDate дата выселения
     * @param redirectAttributes
     * @param model
     * @return ссылка на страницу с информацией о брони
     * @throws ParseException
     */
    @PostMapping("/hotel/{id}/reservation")
    public String reservationPost(@PathVariable(value = "id") long id,
                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkInDate,
                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkOutDate,
                            RedirectAttributes redirectAttributes, Model model) throws ParseException {


        Optional<Hotel> hotel = hotelRepository.findById(id);
        Iterable<Order> orders = orderRepository.findOrders(hotel.get(), checkInDate, checkOutDate);
        List<Order> fsd = (List<Order>) orders;
        if (!fsd.isEmpty()) {
            return "mistake-order";
        }


        long millisecondsDifference = checkOutDate.getTime() - checkInDate.getTime();
        long totalDays = millisecondsDifference / (24 * 60 * 60 * 1000);

        //Optional<Hotel> hotel = hotelRepository.findById(id);
        if (!userProfileRepository.existsById(Global.TempID)) return "redirect:/login";
        Optional<UserProfile> up = userProfileRepository.findById(Global.TempID);

        Double totalCost = totalDays * hotel.get().getPrice();

        Order order = new Order(up.get(), hotel.get(), totalCost, checkInDate, checkOutDate, Integer.parseInt(Long.toString(totalDays)));
        orderRepository.save(order);

        model.addAttribute(order);
        redirectAttributes.addAttribute("orderId", order.getOrderID());

        return "redirect:/reservation/{orderId}/details";
    }

    /**
     * Страница с информацией о брони
     *
     * @param orderId
     * @param model
     * @return переадресация на страницу с информацией о брони
     */
    @GetMapping("/reservation/{orderId}/details")
    public String reservationDetails(@PathVariable(value = "orderId") long orderId, Model model){
        Optional<Order> order = orderRepository.findById(orderId);
        ArrayList<Order> res = new ArrayList<>();
        order.ifPresent(res::add);
        model.addAttribute("order", res);
        return "reservation-details";
    }
}