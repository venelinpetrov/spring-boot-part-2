CREATE TABLE carts (
    cart_id VARCHAR(36) NOT NULL,
    user_id BIGINT NULL,
    date_created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (cart_id),
    CONSTRAINT fk_carts_on_users
        FOREIGN KEY (user_id)
        REFERENCES users (id)
);

CREATE TABLE cart_items (
    item_id INT NOT NULL AUTO_INCREMENT,
    quantity SMALLINT UNSIGNED,
    product_id BIGINT NOT NULL,
    cart_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (item_id),
    CONSTRAINT fk_cart_items_on_products
        FOREIGN KEY (product_id)
        REFERENCES products (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_cart_items_on_carts
        FOREIGN KEY (cart_id)
        REFERENCES carts (cart_id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT
);

