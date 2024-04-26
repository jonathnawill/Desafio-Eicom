-- Inserção de clientes
INSERT INTO tb_cliente (nome) VALUES
    ('Cliente 1'),
    ('Cliente 2'),
    ('Cliente 3'),
    ('Cliente 4'),
    ('Cliente 5'),
    ('Cliente 6'),
    ('Cliente 7'),
    ('Cliente 8'),
    ('Cliente 9'),
    ('Cliente 10');

-- Inserção de pedidos
INSERT INTO tb_pedido (numero_controle, data_cadastro, nome, valor, quantidade, cliente_id) VALUES 
    (1, '2024-04-25', 'Produto A', 100.00, 1, 1),
    (2, '2024-04-25', 'Produto B', 200.00, 3, 2),
    (3, '2024-04-25', 'Produto C', 300.00, 5, 3),
    (4, '2024-04-25', 'Produto D', 400.00, 7, 4),
    (5, '2024-04-25', 'Produto E', 500.00, 10, 5);
