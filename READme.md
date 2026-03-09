# 💰 Sistema Financeiro Pessoal

Sistema completo de gerenciamento financeiro pessoal desenvolvido com Spring Boot e React.

## 📋 Funcionalidades

- ✅ Autenticação de usuários (JWT)
- ✅ Gerenciamento de categorias (receitas e despesas)
- ✅ Registro de transações financeiras
- ✅ Dashboard com resumo financeiro
- ✅ Filtros e visualizações

## 🛠️ Tecnologias Utilizadas

### Backend
- Java 17
- Spring Boot 3.4.1
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL 15
- Maven

### Frontend
- React 18
- Vite
- React Router
- Axios
- Tailwind CSS
- Context API

## 🚀 Como Executar

### Pré-requisitos
- Java 17+
- Node.js 20+
- PostgreSQL 15+
- Maven

### Backend

1. Clone o repositório
```bash
git clone https://github.com/IgorDF10/SFP.git
cd SFP/backend
```

2. Configure o banco de dados no `application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/financeiro_db
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA
```

3. Execute o projeto
```bash
mvn spring-boot:run
```

O backend estará rodando em: `http://localhost:8080`

### Frontend

1. Navegue até a pasta frontend
```bash
cd ../frontend
```

2. Instale as dependências
```bash
npm install
```

3. Execute o projeto
```bash
npm run dev
```

O frontend estará rodando em: `http://localhost:5173`

## 📸 Screenshots

[Adicione prints das telas aqui]

## 📚 Aprendizados

Este projeto foi desenvolvido para consolidar conhecimentos em:
- Arquitetura REST API
- Autenticação JWT
- Integração Backend-Frontend
- Gerenciamento de estado com React
- Estilização com Tailwind CSS

## 👨‍💻 Autor

Igor Fernandes - [LinkedIn](seu-linkedin) - [GitHub](seu-github)

## 📄 Licença

Este projeto está sob a licença MIT.