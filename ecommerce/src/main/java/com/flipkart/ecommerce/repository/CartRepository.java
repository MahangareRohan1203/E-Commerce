package com.flipkart.ecommerce.repository;

import com.flipkart.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c from Cart c WHERE c.user.id = :userId")
    public Cart findByUserId(@Param("userId") Long userId);
}
