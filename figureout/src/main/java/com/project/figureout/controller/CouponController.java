package com.project.figureout.controller;

import com.project.figureout.dto.*;
import com.project.figureout.model.ExchangeCoupon;
import com.project.figureout.model.PromotionalCoupon;
import com.project.figureout.repository.ExchangeCouponRepository;
import com.project.figureout.repository.PromotionalCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/coupons")
public class CouponController {

    @Autowired
    PromotionalCouponRepository promotionalCouponRepository;

    @Autowired
    ExchangeCouponRepository exchangeCouponRepository;

    @GetMapping("/")
    public String showAllCoupons(Model model) {
        List<PromotionalCoupon> allPromotionalCoupons = promotionalCouponRepository.findAll();
        List<ExchangeCoupon> allExchangeCoupons = exchangeCouponRepository.findAll();
        ChangePromotionalCouponExpirationDateDTO changePromotionalCouponExpirationDateDTO = new ChangePromotionalCouponExpirationDateDTO();
        ChangePromotionalCouponDiscountDTO changePromotionalCouponDiscountDTO = new ChangePromotionalCouponDiscountDTO();
        ChangePromotionalCouponCodeDTO changePromotionalCouponCodeDTO = new ChangePromotionalCouponCodeDTO();

        model.addAttribute("promotionalCoupons", allPromotionalCoupons);
        model.addAttribute("exchangeCoupons", allExchangeCoupons);
        model.addAttribute("changePromotionalCouponExpirationDateDTO", changePromotionalCouponExpirationDateDTO);
        model.addAttribute("changePromotionalCouponDiscountDTO", changePromotionalCouponDiscountDTO);
        model.addAttribute("changePromotionalCouponCodeDTO", changePromotionalCouponCodeDTO);

        return "adminCouponsPage";
    }

    @PutMapping("/changePromotionalCouponExpirationDate/{promotionalCouponId}")
    public String changePromotionalCouponExpirationDate(@PathVariable long promotionalCouponId, @ModelAttribute ChangePromotionalCouponExpirationDateDTO
            changePromotionalCouponExpirationDateDTO) {
        PromotionalCoupon promotionalCoupon = promotionalCouponRepository.findById(promotionalCouponId).orElseThrow(() -> new NoSuchElementException("Cupom promocional não encontrado com base no ID."));

        promotionalCoupon.setCouponExpirationDate(changePromotionalCouponExpirationDateDTO.getExpirationDate());

        promotionalCouponRepository.save(promotionalCoupon);

        return "redirect:/coupons/";
    }

    @PutMapping("/changePromotionalCouponDiscount/{promotionalCouponId}")
    public String changePromotionalCouponDiscount(@PathVariable long promotionalCouponId, @ModelAttribute ChangePromotionalCouponDiscountDTO
            changePromotionalCouponDiscountDTO) {
        PromotionalCoupon promotionalCoupon = promotionalCouponRepository.findById(promotionalCouponId).orElseThrow(() -> new NoSuchElementException("Cupom promocional não encontrado com base no ID."));

        BigDecimal discount = changePromotionalCouponDiscountDTO.getDiscount();
        discount = discount.divide(BigDecimal.valueOf(100));

        promotionalCoupon.setCouponDiscount(discount);

        promotionalCouponRepository.save(promotionalCoupon);

        return "redirect:/coupons/";
    }

    @PutMapping("/changePromotionalCouponCode/{promotionalCouponId}")
    public String changePromotionalCouponCode(@PathVariable long promotionalCouponId, @ModelAttribute ChangePromotionalCouponCodeDTO
            changePromotionalCouponCodeDTO) {
        PromotionalCoupon promotionalCoupon = promotionalCouponRepository.findById(promotionalCouponId).orElseThrow(() -> new NoSuchElementException("Cupom promocional não encontrado com base no ID."));

        promotionalCoupon.setCouponName(changePromotionalCouponCodeDTO.getCouponCode());

        promotionalCouponRepository.save(promotionalCoupon);

        return "redirect:/coupons/";
    }

    @GetMapping("/createPromotionalCoupon/")
    public String createPromotionalCouponGet(Model model) {
        CreatePromotionalCouponDTO createPromotionalCouponDTO = new CreatePromotionalCouponDTO();

        model.addAttribute("createPromotionalCouponDTO", createPromotionalCouponDTO);

        return "createPromotionalCoupon";
    }

    @PostMapping("/createPromotionalCoupon/")
    public String createPromotionalCoupon(@ModelAttribute CreatePromotionalCouponDTO createPromotionalCouponDTO) {
        PromotionalCoupon promotionalCoupon = new PromotionalCoupon();
        promotionalCoupon.setCouponName(createPromotionalCouponDTO.getCouponName());
        promotionalCoupon.setCouponDiscount(createPromotionalCouponDTO.getCouponDiscount().divide(BigDecimal.valueOf(100)));
        promotionalCoupon.setCouponExpirationDate(createPromotionalCouponDTO.getCouponExpirationDate());

        promotionalCouponRepository.save(promotionalCoupon);

        return "redirect:/coupons/";
    }

    @DeleteMapping("/deletePromotionalCoupon/{promotionalCouponId}")
    public String deletePromotionalCoupon(@PathVariable long promotionalCouponId) {
        promotionalCouponRepository.deleteById(promotionalCouponId);
        return "redirect:/coupons/";
    }

}
