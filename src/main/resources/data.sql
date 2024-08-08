-- product_image 테이블 생성
CREATE TABLE IF NOT EXISTS product_image (
    product_image_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    original_image_name VARCHAR(255) NOT NULL,
    path VARCHAR(255) NOT NULL,
    product_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    status VARCHAR(50),
    section VARCHAR(50),
    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE
);


-- 서브 카테고리 데이터 삽입
INSERT INTO sub_category (name) VALUES ('티셔츠');
INSERT INTO sub_category (name) VALUES ('바지');

-- 제품 데이터 삽입 (중복 데이터 방지를 위해 조건문 사용)
INSERT INTO product (product_name, product_description, sub_category_id, image_url)
VALUES ('반팔A', '유행하는 반팔 입니다.', 1, 'http://example.com/image.jpg');

INSERT INTO product (product_name, product_description, sub_category_id, image_url)
VALUES ('바지A', '세일중인 바지 입니다.', 2, 'http://example.com/image2.jpg');
