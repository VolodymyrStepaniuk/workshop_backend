INSERT INTO order_items (order_id, id, service_id, price,price_properties)
VALUES (1, 1, 1,
        '{
          "type": "fixed",
          "value": 1500
        }'::jsonb,
        null),
       (2, 2, 1,
        '{
          "type": "per-item",
          "value": 250
        }'::jsonb,
        '{
          "type": "per-item",
          "quantity": 4
        }'::jsonb);