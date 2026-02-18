package com.project.figureout.service;

import com.project.figureout.dto.ChangeCartProductQuantityDTO;
import com.project.figureout.model.*;
import com.project.figureout.repository.CartRepository;
import com.project.figureout.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CartService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private SaleService saleService;

    public Cart getCartById(long id) {
        return cartRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Carrinho não encontrado com base no ID."));
    }

    public Client getClientByCart(Cart cart) {
        return cart.getClient();
    }

    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }

    public void deleteProductFromCart(Cart cart, Product product) {
        cart.getCartProducts().removeIf(cartProduct -> cartProduct.getProduct().getId() == (product.getId()));
        setCartTotal(cart);
    }

    public void addProductToCart(Cart cart, Product product, ChangeCartProductQuantityDTO changeCartProductQuantityDTO) {
        CartsProductsKey cartsProductsKey = new CartsProductsKey(cart.getId(), product.getId());

        CartsProducts cartProduct = new CartsProducts();
        cartProduct.setId(cartsProductsKey);
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cartProduct.setProductQuantity(changeCartProductQuantityDTO.getQuantity());
        cartProduct.setUnitaryPrice(product.getPrice()); // setting a temporary price here so it doesnt come out as 0 when i check for promotional coupon
        cartProduct.setExchangeableQuantity(changeCartProductQuantityDTO.getQuantity());

        if(cart.getPromotionalCoupon() != null) {
            setPromotionalCouponDiscount(cartProduct, cart.getPromotionalCoupon());
        } else {
            cartProduct.setUnitaryPrice(product.getPrice());
            cartProduct.setFinalPrice(cartProduct.getUnitaryPrice().multiply(BigDecimal.valueOf(cartProduct.getProductQuantity())));
        }

        LocalDateTime now = LocalDateTime.now();
        cartProduct.setProductAddedTime(now);

        cart.getCartProducts().add(cartProduct);

        setCartTotal(cart);
    }

    public void changeProductQuantity(CartsProducts cartProduct, ChangeCartProductQuantityDTO changeCartProductQuantityDTO) {
        cartProduct.setProductQuantity(changeCartProductQuantityDTO.getQuantity());
        cartProduct.setExchangeableQuantity(changeCartProductQuantityDTO.getQuantity());
        cartProduct.setFinalPrice(cartProduct.getUnitaryPrice().multiply(BigDecimal.valueOf(cartProduct.getProductQuantity())));
    }

    public void setPromotionalCouponDiscount(CartsProducts cartProduct, PromotionalCoupon promotionalCoupon) {
        BigDecimal cartProductOldPrice = cartProduct.getProduct().getPrice();

        BigDecimal newPrice = cartProductOldPrice.subtract(cartProductOldPrice.multiply(promotionalCoupon.getCouponDiscount()));

        cartProduct.setUnitaryPrice(newPrice);
        cartProduct.setFinalPrice(newPrice.multiply(BigDecimal.valueOf(cartProduct.getProductQuantity())));
    }

    public void applyPromotionalCoupon(Cart cart, PromotionalCoupon promotionalCoupon) {

        if(promotionalCoupon.getCouponExpirationDate().isBefore(LocalDate.now())) {
            return;
        }

        List<Sale> clientSales = saleService.getClientSalesByClientId(cart.getClient().getId());
        if(clientSales.stream().map(Sale::getPromotionalCouponApplied).anyMatch(promotionalCouponOnSale -> promotionalCouponOnSale != null && promotionalCouponOnSale.equals(promotionalCoupon))) {
            return;
        }

        List<CartsProducts> cartsProducts = cart.getCartProducts();

        if(cart.getPromotionalCoupon() != null) {
            if(cart.getPromotionalCoupon().equals(promotionalCoupon)) {
                return;
            }

            for(CartsProducts cartProduct: cartsProducts) {
                System.out.println(cartProduct.getUnitaryPrice());
                cartProduct.setUnitaryPrice(cartProduct.getProduct().getPrice());
                cartProduct.setFinalPrice(cartProduct.getProduct().getPrice().multiply(BigDecimal.valueOf(cartProduct.getProductQuantity())));
                System.out.println(cartProduct.getFinalPrice());
            }
        }

        cart.setPromotionalCoupon(promotionalCoupon);

        for(CartsProducts cartProduct: cartsProducts) {
            setPromotionalCouponDiscount(cartProduct, promotionalCoupon);
        }

        setCartTotal(cart);
    }

    public void removePromotionalCoupon(Cart cart) {
        List<CartsProducts> cartsProducts = cart.getCartProducts();

        if(cart.getPromotionalCoupon() != null) {

            for(CartsProducts cartProduct: cartsProducts) {
                cartProduct.setUnitaryPrice(cartProduct.getProduct().getPrice());
                cartProduct.setFinalPrice(cartProduct.getProduct().getPrice().multiply(BigDecimal.valueOf(cartProduct.getProductQuantity())));
            }

        }

        cart.setPromotionalCoupon(null);

        setCartTotal(cart);

    }

    public BigDecimal isExchangeCouponSurpassingCartTotalTooMuch(Cart cart, ExchangeCoupon exchangeCoupon) {

        BigDecimal percentile = BigDecimal.valueOf(0.20);
        BigDecimal couponAmount = exchangeCoupon.getAmountWorth();
        BigDecimal cartTotal = cart.getTotalPrice();
        BigDecimal cartTotalPercentile = cartTotal.multiply(percentile);

        // 200 > 100
        if(couponAmount.compareTo(cartTotal) > 0) { // if the amount is bigger than the current total price in the cart
            // 100 - 200 = -100
            BigDecimal subtractedValue = cartTotal.subtract(couponAmount).abs(); // now we have the subtracted value
            // 100
            // 100 > 20?
            if(subtractedValue.compareTo(cartTotalPercentile) >= 0) { // if that value is bigger/equal to the percentile, then we cant let the coupon be applied
                // true, so return and dont even apply the coupon
                return BigDecimal.valueOf(0);
            } else {
                return subtractedValue;
            }

        }

        return BigDecimal.valueOf(-1);
    }

    public void applyExchangeCoupon(Cart cart, ExchangeCoupon exchangeCoupon) {
        if(cart.getExchangeCoupons().contains(exchangeCoupon)) {
            return;
        }
        if(exchangeCoupon.isUsed()) {
            return;
        }

        cart.getExchangeCoupons().add(exchangeCoupon);
        saveCart(cart);

        setCartTotal(cart);
    }

    public void removeExchangeCoupon(Cart cart, ExchangeCoupon exchangeCoupon) {
        cart.getExchangeCoupons().remove(exchangeCoupon);
        saveCart(cart);

        setCartTotal(cart);
    }

    public void setCartTotal(Cart cart) {
        BigDecimal total = new BigDecimal(0);
        System.out.println("calling setCartTotal");

        // get the list of products in the cart
        List<CartsProducts> cartProducts = cart.getCartProducts();

        // loop through each product in the cart and calculate the total price
        for (CartsProducts cartsProduct : cartProducts) {
            BigDecimal productTotal = cartsProduct.getFinalPrice();
            System.out.println(productTotal);
            total = total.add(productTotal);
        }

        List<ExchangeCoupon> cartExchangeCoupons = cart.getExchangeCoupons();

        if(!cartExchangeCoupons.isEmpty()) {

            for(ExchangeCoupon exchangeCoupon : cartExchangeCoupons) {

                total = total.subtract(exchangeCoupon.getAmountWorth());

            }

        }

        // set the total price in the cart
        if(total.compareTo(BigDecimal.valueOf(0)) < 0) {
            total = BigDecimal.valueOf(0);
        }

        cart.setTotalPrice(total);

        // save the cart with the updated total
        saveCart(cart);
    }

    @Transactional
    public void changeClientCart(Client client) {
        LocalDateTime now = LocalDateTime.now();
        Cart newCart = new Cart(now);

        for(Cart currentCart: client.getCartList()) {
            System.out.println("Setting " + currentCart.getId() + " cart to false");
            currentCart.setBeingUsed(false);
            saveCart(currentCart);
        }

        client.getCartList().add(newCart);

        newCart.setClient(client);
        newCart.setBeingUsed(true);

        saveCart(newCart);
    }

    public Client getClientWithCarts(long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow();
        Hibernate.initialize(client.getCartList());  // Ensure the cartList is initialized within the session
        return client;
    }

    public void expireClientCart(long id) {
        Client client = getClientWithCarts(id);
        System.out.println(client.getName());

        // Create a copy of the cartList to avoid modifying the original list while iterating
        List<Cart> cartListCopy = new ArrayList<>(client.getCartList());

        for (Cart currentCart : cartListCopy) {
            if (currentCart.isBeingUsed()) {

                List<CartsProducts> cartProductsCopy = new ArrayList<>(currentCart.getCartProducts());

                if(cartProductsCopy.isEmpty()) {
                    return;
                }

                LocalDateTime now = LocalDateTime.now();
                LocalDateTime expirationTime = currentCart.getDateOfCreation().plusMinutes(20);

                if (now.isAfter(expirationTime)) {
                    System.out.println("Cart expired");

                    changeClientCart(client);  // Handle expired cart
                }
            }
        }

    }

    private static final int qntMinutesExpire = 20;  // Set to 20 minutes
    private static final long qntMinutesExpireInMs = qntMinutesExpire * 60 * 1000; // Convert to milliseconds

    @Scheduled(fixedRate = qntMinutesExpireInMs)  // Run every minute (60000 ms)
    public void checkForExpiredCarts() {

        List<Client> clients = clientRepository.findAll();

        if (!clients.isEmpty()) {
            for (Client currentClient : clients) {
                expireClientCart(currentClient.getId());
            }
        }
    }

}
