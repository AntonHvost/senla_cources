BEGIN;

CREATE TABLE IF NOT EXISTS public.book
(
    id bigserial,
    title varchar(255) NOT NULL,
    author varchar(255) NOT NULL,
    description text,
	publish_date timestamp without time zone,
    price numeric(10, 2) NOT NULL,
    status varchar(20) NOT NULL,
    CONSTRAINT pk_id PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.consumer
(
    id bigserial,
    name varchar(255) NOT NULL,
    phone varchar(50),
    email varchar(255),
    CONSTRAINT pk_consumers_id PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public."order"
(
    id bigserial,
    consumer_id bigint NOT NULL,
    created_at timestamp without time zone NOT NULL,
    completed_at timestamp without time zone,
    total_price numeric(10, 2) NOT NULL,
    status varchar(20) NOT NULL,
    CONSTRAINT pk_order_id PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.order_item
(
    id bigserial,
    order_id bigint NOT NULL,
    book_id bigint NOT NULL,
    quantity integer NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.book_request
(
    id bigserial,
    order_id bigint NOT NULL,
    book_id bigint NOT NULL,
    create_at timestamp without time zone NOT NULL,
    delivery_date timestamp without time zone,
    status varchar(20) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public."order"
    ADD CONSTRAINT fk_consumer FOREIGN KEY (consumer_id)
    REFERENCES public.consumer (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public.order_item
    ADD CONSTRAINT fk_order_order_item FOREIGN KEY (order_id)
    REFERENCES public."order" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.order_item
    ADD CONSTRAINT fk_book_order_item FOREIGN KEY (book_id)
    REFERENCES public.book (id) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public.book_request
    ADD CONSTRAINT fk_order_book_request FOREIGN KEY (order_id)
    REFERENCES public."order" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.book_request
    ADD CONSTRAINT fk_book_book_request FOREIGN KEY (book_id)
    REFERENCES public.book (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;

CREATE TYPE book_status AS ENUM('AVAILABLE', 'OUT_OF_STOCK');
CREATE TYPE order_status AS ENUM ('NEW', 'IN_PROCESS', 'COMPLETED', 'CANCELLED');
CREATE TYPE request_status AS ENUM ('PENDING', 'FULFILLED', 'CANCELLED');

END;