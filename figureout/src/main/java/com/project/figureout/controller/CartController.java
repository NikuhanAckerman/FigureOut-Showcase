package com.project.figureout.controller;

import com.project.figureout.ClientNavigator;
import com.project.figureout.dto.*;
import com.project.figureout.model.*;
import com.project.figureout.repository.ExchangeCouponRepository;
import com.project.figureout.repository.PromotionalCouponRepository;
import com.project.figureout.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PromotionalCouponRepository promotionalCouponRepository;

    @Autowired
    private ExchangeCouponRepository exchangeCouponRepository;

    private ClientNavigator clientNavigator;

    @Autowired
    private StockService stockService;

    @GetMapping("/getCart/{cartId}")
    public Cart getSpecificCart(@PathVariable Long id) {
        return cartService.getCartById(id);
    }

    @GetMapping("/{cartId}")
    public Cart showCart(@PathVariable Long id, Model model) {
        Cart cart = cartService.getCartById(id);

        model.addAttribute("cart", cart);

        return cart;
    }

    @PostMapping("/addProductToCart/{productId}/{cartId}")
    public String addProductToCart(@PathVariable Long productId, @PathVariable Long cartId,
                                   @ModelAttribute ChangeCartProductQuantityDTO changeCartProductQuantityDTO, HttpServletRequest request,
                                   Model model, RedirectAttributes redirectAttributes) {
        Product product = productService.getProductById(productId);
        Cart cart = cartService.getCartById(cartId);

        int quantityOrdered = changeCartProductQuantityDTO.getQuantity();
        int availableQuantity = stockService.getProductInStockByProductId(productId).getProductQuantityAvailable();

        String referer = request.getHeader("Referer");
        List<String> errors = new ArrayList<>();

        if(quantityOrdered > availableQuantity) {
            errors.add("Quantidade indisponível em estoque.");
        }

        if(quantityOrdered <= 0) {
            errors.add("Não se pode pedir 0 ou menos produtos.");
        }

        boolean isAlreadyInCart = false;
        for(CartsProducts cartProduct: cart.getCartProducts()) {

            if(cartProduct.getProduct().getId() == productId) {
                isAlreadyInCart = true;
            }

        }

        if(isAlreadyInCart) {
            errors.add("Não se pode adicionar o mesmo produto no carrinho múltiplas vezes.");
        }

        if(!errors.isEmpty()) {
            redirectAttributes.addFlashAttribute("errors", errors);
            //Stock stock = stockService.getProductInStockByProductId(productId);
            //List<Category> productCategoryList = product.getCategories();
            //Client client = clientService.getClientById(clientNavigator.getInstance().getClientId());
            ////model.addAttribute("errors", errors);
            //model.addAttribute("stock", stock);
            //model.addAttribute("changeCartProductQuantityDTO", new ChangeCartProductQuantityDTO());
            //model.addAttribute("product", product);
            //model.addAttribute("cart", client.getCartList().getLast());

            return "redirect:/products/specificProduct/" + productId;
        }

        cartService.addProductToCart(cart, product, changeCartProductQuantityDTO);

        return "redirect:" + referer;
    }

    @DeleteMapping("/removeProductFromCart/{productId}/{cartId}")
    public String removeProductFromCart(@PathVariable Long productId, @PathVariable Long cartId, HttpServletRequest request) {
        Product product = productService.getProductById(productId);
        Cart cart = cartService.getCartById(cartId);

        cartService.deleteProductFromCart(cart, product);

        if(cart.getCartProducts().isEmpty()) {
            System.out.println("cart is empty");
            return "redirect:/products/shop";
        }

        System.out.println("cart is populated");

        // Get the previous page URL from the Referer header
        String referer = request.getHeader("Referer");

        // Redirect back to the same page
        return "redirect:" + referer;
    }

    @PutMapping("/changeProductQuantity/{productId}/{cartId}")
    public String changeProductQuantity(@PathVariable Long productId, @PathVariable Long cartId,
                                        @ModelAttribute ChangeCartProductQuantityDTO changeCartProductQuantityDTO, HttpServletRequest request,
                                        Model model, RedirectAttributes redirectAttributes) {

        Product product = productService.getProductById(productId);
        Cart cart = cartService.getCartById(cartId);
        String referer = request.getHeader("Referer");

        int quantityOrdered = changeCartProductQuantityDTO.getQuantity();
        int availableQuantity = stockService.getProductInStockByProductId(productId).getProductQuantityAvailable();

        List<String> errors = new ArrayList<>();

        if(quantityOrdered > availableQuantity) {
            errors.add("Quantidade indisponível em estoque do produto " + product.getName() + ".");
        }

        if(quantityOrdered <= 0) {
            errors.add("Não se pode pedir 0 ou menos produtos.");
        }

        if (!errors.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorsProductChangeQuantity", errors);
            return "redirect:" + referer;
        }

        for(CartsProducts cartsProducts: cart.getCartProducts()) {

            if(cartsProducts.getProduct().getId() == product.getId()) {

                cartService.changeProductQuantity(cartsProducts, changeCartProductQuantityDTO);

            }

        }

        cartService.setCartTotal(cart);

        return "redirect:" + referer;
    }

    @PutMapping("/addPromotionalCoupon/{cartId}")
    public String addPromotionalCoupon(@PathVariable long cartId, @ModelAttribute PromotionalCouponDTO promotionalCouponDTO, HttpServletRequest request) {
        Cart cart = cartService.getCartById(cartId);

        for(PromotionalCoupon promotionalCoupon: promotionalCouponRepository.findAll()) {

            if(promotionalCouponDTO.getCouponName().equals(promotionalCoupon.getCouponName())) {

                cartService.applyPromotionalCoupon(cart, promotionalCoupon);

            }

        }

        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }

    @DeleteMapping("/removePromotionalCoupon/{cartId}")
    public String removePromotionalCoupon(@PathVariable long cartId, HttpServletRequest request) {
        Cart cart = cartService.getCartById(cartId);

        cartService.removePromotionalCoupon(cart);

        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }

    @PutMapping("/addExchangeCoupon/{cartId}")
    public String addExchangeCoupon(@PathVariable long cartId, @ModelAttribute ExchangeCouponIndividualDTO exchangeCouponIndividualDTO,
                                    HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Cart cart = cartService.getCartById(cartId);

        List<String> errors = new ArrayList<>();
        String referer = request.getHeader("Referer");

        long exchangeCouponId = 0;


        for(ExchangeCoupon exchangeCoupon: exchangeCouponRepository.findAll()) {

            if(exchangeCouponIndividualDTO.getExchangeCouponCode().equals(exchangeCoupon.getExchangeCouponCode())) {

                exchangeCouponId = exchangeCoupon.getId();

            }

        }

        if(exchangeCouponId != 0) {
            ExchangeCoupon exchangeCoupon = exchangeCouponRepository.findById(exchangeCouponId).orElseThrow(() -> new NoSuchElementException("Cupom de troca não encontrado por ID."));

            if(cart.getExchangeCoupons().size() >= 3) {
                errors.add("Você não pode adicionar mais cupons de troca.");
            }

            if(cart.getClient().getId() == exchangeCoupon.getClient().getId()) { // same client

                if(exchangeCoupon.isUsed()) {

                    errors.add("Este cupom de troca já foi utilizado.");

                } else {

                    if(cartService.isExchangeCouponSurpassingCartTotalTooMuch(cart, exchangeCoupon).compareTo(BigDecimal.valueOf(0)) == 0) {

                        errors.add("Este cupom não pode ser aplicado, pois ultrapassa o preço da compra excessivamente.");

                    } else {

                        cartService.applyExchangeCoupon(cart, exchangeCoupon);

                    }

                }

            } else {

                errors.add("Você não pode usar esse cupom de troca.");

            }

        } else {
            System.out.println("o cupom nao existe lol");
            errors.add("Este cupom de troca não existe.");
        }

        if(!errors.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorsExchangeCoupon", errors);
            return "redirect:" + referer;
        }

        return "redirect:" + referer;
    }

    @DeleteMapping("/removeExchangeCoupon/{cartId}/{exchangeCouponId}")
    public String removeExchangeCoupon(@PathVariable long cartId, @PathVariable long exchangeCouponId, HttpServletRequest request) {
        Cart cart = cartService.getCartById(cartId);
        ExchangeCoupon exchangeCoupon = exchangeCouponRepository.findById(exchangeCouponId).orElseThrow(() -> new NoSuchElementException("Cupom de troca não encontrado."));

        cartService.removeExchangeCoupon(cart, exchangeCoupon);

        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }

}
