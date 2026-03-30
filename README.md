# Backend - Notification App

Backend desenvolvido com **Java + Spring Boot**, responsável pela API e regras de negócio da aplicação.

## 🚀 Tecnologias utilizadas

* **Spring Web** – construção de APIs REST
* **Spring Data MongoDB** – integração com banco NoSQL
* **Spring Security** – autenticação e segurança da aplicação

## 📦 Estrutura do projeto

O código-fonte está localizado em:

```
src/main/java/com.ditossystem.ditos
```

## ⚙️ Configuração

As variáveis de ambiente são gerenciadas via arquivo `.env`.

Antes de rodar o projeto, crie um arquivo `.env` baseado no `.env.example`:

```
cp .env.example .env
```

## 🐳 Execução com Docker

Para subir os serviços (backend + MongoDB):

```
docker compose up --build
```

A aplicação estará disponível em:

```
http://localhost:8080
```

## 🔐 Observações

* O arquivo `.env` não deve ser versionado
* Utilize o `.env.example` como referência para configuração
