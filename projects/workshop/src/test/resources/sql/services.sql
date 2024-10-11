INSERT INTO services(id, category_id, title, description, image_urls, priority, price, created_at, last_modified_at)
VALUES (1,1,'Title','Description',ARRAY ['image_url'],100,
        '{
          "type": "per-item",
          "value": 250
        }'::jsonb,
        '2024-10-24T16:22:09.266615Z',
        '2024-10-25T17:28:19.266615Z'),
       (2,2,'New Title','New Description',ARRAY ['new_image_url'],200,
        '{
          "type": "fixed",
          "value": 1000
        }'::jsonb,
        '2024-10-25T16:22:09.266615Z',
        '2024-10-26T17:28:19.266615Z');