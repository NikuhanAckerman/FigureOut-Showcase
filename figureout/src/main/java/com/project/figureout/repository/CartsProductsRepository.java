package com.project.figureout.repository;

import com.project.figureout.model.Cart;
import com.project.figureout.model.CartsProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartsProductsRepository extends JpaRepository<CartsProducts, Long> {

    public CartsProducts getCartsProductsByProductIdAndCart(long productId, Cart cart);

    public CartsProducts getCartsProductsByCart(Cart cart);

}
