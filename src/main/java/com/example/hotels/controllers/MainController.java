package com.example.hotels.controllers;

import com.example.hotels.models.Global;
import com.example.hotels.models.Hotel;
import com.example.hotels.models.User;
import com.example.hotels.models.UserProfile;
import com.example.hotels.repository.HotelRepository;
import com.example.hotels.repository.UserProfileRepository;
import com.example.hotels.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class MainController {
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    /**
        *Переход на главную страницу Get
     *
     * @return переадресация на главную страницу
     */
    @GetMapping("/")
    public String greeting(Model model) {
        Iterable<Hotel> hotels = hotelRepository.findAll();
        model.addAttribute("hotels", hotels);

        return "main";
    }

    /**
     *Переход на главную страницу Post
     *
     * @return переадресация на главную страницу
     */
    @PostMapping("/")
    public String userLogin(Model model) {
        return "main";
    }

    /**
     * Переход на страницу авторизации Get
     *
     * @param model
     * @return переадресация на страницу авторизации
     */
    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    /**
     * Переход на страницу авторизации Post
     *
     * @param username имя пользователя
     * @param password пароль
     * @param model
     * @return переадресация на главнуюстраницу
     */
    @PostMapping("/login")
    public String usersLogin(@RequestParam String username, @RequestParam String password, Model model) {
        User user = userRepository.findByUsername(username);
        if (user != null){
            if(!user.getPassword().equals(password)) return "mistake";
            UserProfile userProfile = userProfileRepository.findByUserKey(user);
            Global.TempUserProfile = userProfile;
            Global.TempID = user.getUserID();
        }
        else {
            return "mistake";
        }
        model.addAttribute("user", user);
        model.addAttribute("username", Global.TempUserProfile.getFirstname());

        return "redirect:/";
    }

    /**
     * Переход на страницу регистрации
     *
     * @param model
     * @return переадресация на страницу регитсрации
     */
    @GetMapping("/registration")
    public String registration(Model model) {
        return "registration";
    }

    /**
     *
     * @param username имя пользоватля
     * @param login логин
     * @param password пароль
     * @param model
     * @return
     */
    @PostMapping("/registration")
    public String userRegistration(@RequestParam String username, @RequestParam String login,
                                   @RequestParam String password, Model model){
        User user = new User(login, password);
        UserProfile userProfile = new UserProfile(user, username);

        userRepository.save(user);
        userProfileRepository.save(userProfile);

        return "login";
    }
}
