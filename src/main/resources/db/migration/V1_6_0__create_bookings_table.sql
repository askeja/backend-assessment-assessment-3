
CREATE SEQUENCE bookings_seq;

CREATE TABLE bookings (
  id bigint check (id > 0) NOT NULL DEFAULT NEXTVAL ('bookings_seq'),
  room_id bigint check (room_id > 0) NOT NULL,
  created_date date NOT NULL,
  booking_price_before_tax  decimal NOT NULL,
  booking_price_after_tax  decimal NOT NULL,
  booking_currency character(3) NOT NULL,
  checkin date NOT NULL,
  checkout date NOT NULL,
  guest_name text NOT NULL,
  guest_surname text NOT NULL,
  guest_birthdate date NOT NULL,
  payment_card_holder text NOT NULL,
  payment_card_number text NOT NULL,
  payment_card_cvv character(3) NOT NULL,
  payment_expiry_month text NOT NULL,
  payment_expiry_year text NOT NULL,
  correlation_id text,
  PRIMARY KEY (id),
  CONSTRAINT bookings_room_id_foreign FOREIGN KEY (room_id) REFERENCES rooms (id) ON DELETE CASCADE
);

