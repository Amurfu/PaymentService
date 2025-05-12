-- ─── 1. Base de datos ────────────────────────────────────────────────────────
CREATE DATABASE IF NOT EXISTS payments
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE payments;

-- ─── 2. Catálogo: payment_status ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS payment_status (
  status_id TINYINT      NOT NULL AUTO_INCREMENT,
  code      VARCHAR(20)  NOT NULL,      
  label     VARCHAR(50)  NOT NULL,        
  PRIMARY KEY (status_id),
  UNIQUE KEY uq_status_code (code)
) ENGINE = InnoDB;

-- Datos fijos del catálogo
INSERT INTO payment_status (status_id, code, label) VALUES
  (1, 'PENDIENTE', 'Pendiente'),
  (2, 'PAGADO',    'Pagado'),
  (3, 'CANCELADO', 'Cancelado')
ON DUPLICATE KEY UPDATE label = VALUES(label);   -- idempotente

-- ─── 3. Tabla principal: payments ────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS payments (
  id              BIGINT        NOT NULL AUTO_INCREMENT,
  concepto        VARCHAR(100)  NOT NULL,
  quien_realiza   VARCHAR(100)  NOT NULL,
  a_quien_se_paga VARCHAR(100)  NOT NULL,
  monto           DECIMAL(12,2) NOT NULL CHECK (monto > 0),

  status_id       TINYINT       NOT NULL,
  CONSTRAINT fk_payment_status
      FOREIGN KEY (status_id) REFERENCES payment_status(status_id),

  created_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at      TIMESTAMP     NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE INDEX idx_payments_status_id ON payments (status_id);

-- 4. Datos demo
INSERT INTO payments (concepto, quien_realiza, a_quien_se_paga, monto, status_id)
VALUES
  ('Servicio de hosting', 'Ana',  'ProveedorCloud',  1200.00, 1),
  ('Compra de insumos',   'Luis', 'PaperCorp',        750.50, 2),
  ('Licencia software',   'Ada',  'SoftHouse',        999.99, 3);
