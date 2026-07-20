CREATE TABLE supplier (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    phone       VARCHAR(50),
    country     VARCHAR(100),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE product (
    id                BIGSERIAL PRIMARY KEY,
    sku               VARCHAR(64) NOT NULL UNIQUE,
    name              VARCHAR(255) NOT NULL,
    description       TEXT,
    unit_price_minor  BIGINT NOT NULL,
    currency          VARCHAR(3) NOT NULL,
    stock_quantity    INTEGER NOT NULL DEFAULT 0,
    reorder_level     INTEGER NOT NULL DEFAULT 0,
    supplier_id       BIGINT NOT NULL REFERENCES supplier(id),
    version           BIGINT NOT NULL DEFAULT 0,
    created_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT chk_product_stock_non_negative CHECK (stock_quantity >= 0)
);

CREATE INDEX idx_product_supplier_id ON product(supplier_id);

CREATE TABLE "order" (
    id             BIGSERIAL PRIMARY KEY,
    customer_name  VARCHAR(255) NOT NULL,
    customer_email VARCHAR(255) NOT NULL,
    status         VARCHAR(20) NOT NULL,
    total_minor    BIGINT NOT NULL,
    currency       VARCHAR(3) NOT NULL,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE order_item (
    id                            BIGSERIAL PRIMARY KEY,
    order_id                      BIGINT NOT NULL REFERENCES "order"(id),
    product_id                    BIGINT NOT NULL REFERENCES product(id),
    quantity                      INTEGER NOT NULL,
    unit_price_minor_at_order     BIGINT NOT NULL,
    CONSTRAINT chk_order_item_quantity_positive CHECK (quantity > 0)
);

CREATE INDEX idx_order_item_order_id ON order_item(order_id);
CREATE INDEX idx_order_item_product_id ON order_item(product_id);
