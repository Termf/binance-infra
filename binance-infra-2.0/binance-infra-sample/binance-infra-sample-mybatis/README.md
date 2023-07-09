
# MybatisPlus使用演示

## Quick Guide

```sql
CREATE TABLE `t_book` (
  `id` int NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `press` varchar(45) DEFAULT NULL,
  `price` decimal(6,2) DEFAULT NULL,
  `author` varchar(45) DEFAULT NULL,
  `book_type` varchar(45) DEFAULT NULL,
  `created_at` bigint DEFAULT NULL,
  `last_modified_at` bigint DEFAULT NULL,
  `deleted` binary(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

```

## 枚举使用

数据库存储的枚举类型名称无法被枚举类型识别
```text
Caused by: java.sql.SQLException: Out of range value for column 'book_type' : value Computer
```

## 