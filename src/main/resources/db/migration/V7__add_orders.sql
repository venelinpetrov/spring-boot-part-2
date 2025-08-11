CREATE TABLE orders (
    order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    STATUS VARCHAR(20) NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    CONSTRAINT orders_users_id_fk FOREIGN KEY (customer_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE order_items (
    order_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    CONSTRAINT order_items_products_id_fk FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT order_items_orders_id_fk FOREIGN KEY (order_id) REFERENCES orders (order_id)
);