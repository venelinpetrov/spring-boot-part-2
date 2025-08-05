ALTER TABLE cart_items
MODIFY COLUMN quantity SMALLINT UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE cart_items
ADD CONSTRAINT uq_item_product UNIQUE (item_id, product_id);
