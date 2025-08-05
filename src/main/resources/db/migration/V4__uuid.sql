ALTER TABLE cart_items DROP FOREIGN KEY fk_cart_items_on_carts;

ALTER TABLE cart_items
MODIFY COLUMN cart_id BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) NOT NULL;

ALTER TABLE carts
MODIFY COLUMN cart_id BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) NOT NULL;

ALTER TABLE carts
MODIFY COLUMN date_created DATE DEFAULT (CURDATE()) NOT NULL;

ALTER TABLE cart_items
    ADD CONSTRAINT fk_cart_items_on_carts
        FOREIGN KEY (cart_id) REFERENCES carts (cart_id)
            ON DELETE CASCADE
            ON UPDATE RESTRICT;